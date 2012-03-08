package net.sourceforge.eclipsefrills.perforce.handler;

import net.sourceforge.eclipsefrills.perforce.Activator;
import net.sourceforge.eclipsefrills.perforce.dialog.ChangeShelvingDialog;
import net.sourceforge.eclipsefrills.perforce.manager.IShelvedChangeManager;
import net.sourceforge.eclipsefrills.perforce.model.IShelvedChange;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.perforce.p4api.IPerforceFile;
import com.perforce.team.core.PerforceProviderPlugin;

public class ShelveChangesHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Activator plugin = Activator.getDefault();
		
		final ChangeShelvingDialog dialog = new ChangeShelvingDialog(plugin.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.setInput(plugin.extractPendingLocalChanges());
		
		if(dialog.open() == ChangeShelvingDialog.OK){
			final IShelvedChangeManager shelvedChangeManager = plugin.getShelvedChangeManager();
			
			final IShelvedChange shelvedChange = shelvedChangeManager.create(dialog.getShelfTitle());
			shelvedChange.setDescription(dialog.getShelfDescription());
			
			final IPerforceFile[] shelvedFiles = dialog.getShelvedFiles();
			final IPath[] filePaths = new IPath[shelvedFiles.length];
			for(int f=0; f<shelvedFiles.length; f++){
				final String clientPath = shelvedFiles[f].getClientPath();
				final IFile ifile = PerforceProviderPlugin.getWorkspaceFile(clientPath);
				filePaths[f] = ifile == null ? new Path(clientPath) : ifile.getFullPath(); // null happens when a changed file is unknown to eclipse
			}
			shelvedChange.setFilePaths(filePaths);
			
			shelvedChangeManager.shelve(shelvedChange,dialog.isAutoRevertShelvedChanges());
		} 
		return null;
	}
}
