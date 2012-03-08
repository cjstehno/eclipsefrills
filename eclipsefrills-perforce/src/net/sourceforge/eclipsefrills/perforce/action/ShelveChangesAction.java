package net.sourceforge.eclipsefrills.perforce.action;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.perforce.p4api.IChangelist;
import com.perforce.p4api.IPerforceFile;
import com.perforce.p4api.PerforceConnection;
import com.perforce.p4api.PerforceException;
import com.perforce.team.core.PerforceProviderPlugin;
import com.perforce.team.core.PerforceTeamProvider;
import com.perforce.team.ui.IPerforceRunnable;
import com.perforce.team.ui.PerforceAccess;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class ShelveChangesAction implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow window;
	
	public void run(final IAction action) {
		final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("wssamples");
		System.out.println("Found project: " + project);
		
		final IResource resource = project;
		final PerforceTeamProvider teamProvider = PerforceProviderPlugin.getPerforceProviderFor(resource);
		final PerforceConnection p4conn = PerforceAccess.getPerforceConnection(teamProvider);
		
//        final IChangelist defaults[] = (IChangelist[])PerforceAccess.exec(p4conn, new IPerforceRunnable() {
//            public Object run() throws PerforceException {
//                return p4conn.getChangelistAccess().getDefaultChanges();
//            }
//        });
        
        final IChangelist pending[] = (IChangelist[])PerforceAccess.exec(p4conn, new IPerforceRunnable() {
            public Object run() throws PerforceException {
                return p4conn.getChangelistAccess().getLocalPendingChanges();
            }
        });
        
//        System.out.println("default");
//        for(final IChangelist list : defaults){
//        	System.out.println(list.getDescription());
//        }
        
        System.out.println("Pending local changes:");
        for(final IChangelist list : pending){
        	System.out.println(list.getID() + ": " + list.getDescription());
        	final IPerforceFile[] files = list.getFiles();
        	final IFile[] ifiles = PerforceProviderPlugin.getWorkspaceResources(files);
        	for(final IFile file : ifiles){
        		System.out.println("\t" + file);
        	}
        }
        
        final IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
        final IConsole[] consoles = consoleManager.getConsoles();
        if(consoles != null && consoles.length != 0){
	        for(final IConsole cons : consoles){
	        	System.out.println("Console: " + cons.getName());
	        }
        } else {
        	final IOConsole console = new IOConsole("P4 Changelist",null);
        	consoleManager.addConsoles(new IConsole[]{console});
        	
        	final IOConsoleOutputStream outstream = console.newOutputStream();
        	try {
	            for(final IChangelist list : pending){
	            	outstream.write(list.getID() + ": " + list.getDescription() + "\n");
	            	
	            	final IPerforceFile[] files = list.getFiles();
	            	for(final IPerforceFile file : files){
	            		outstream.write("\t" + file + "\n");
	            	}
	            }
        	} catch(final IOException ioe){
        		ioe.printStackTrace();
        	} finally {
        		if(outstream != null){
        			try {outstream.close();} catch(final Exception e){}
        		}
        	}
        }
        
	}

	public void selectionChanged(final IAction action, final ISelection selection) {}

	public void dispose() {}

	public void init(final IWorkbenchWindow window) {
		this.window = window;
	}
}