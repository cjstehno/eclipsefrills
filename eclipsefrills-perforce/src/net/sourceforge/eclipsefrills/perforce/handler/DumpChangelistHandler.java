package net.sourceforge.eclipsefrills.perforce.handler;

import java.io.IOException;

import net.sourceforge.eclipsefrills.perforce.Activator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.perforce.p4api.IChangelist;
import com.perforce.p4api.IPerforceFile;

/**
 */
public class DumpChangelistHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IChangelist[] pending = Activator.getDefault().extractPendingLocalChanges();
        
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
	            		outstream.write("\t" + file.getDepotPath() + "\n");
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
        
        return null;
	}
}
