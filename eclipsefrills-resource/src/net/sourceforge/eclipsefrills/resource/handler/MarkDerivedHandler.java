/**************************************************************************
**  Copyright (c) 2006-2009 Christopher J. Stehno                        **
**  chris@stehno.com                                                     **
**  http://www.stehno.com                                                **
**                                                                       **
**  All rights reserved                                                  **
**                                                                       **
**  This program and the accompanying materials are made available under **
**  the terms of the Eclipse Public License v1.0 which accompanies this  **
**  distribution, and is available at:                                   **
**  http://www.stehno.com/legal/epl-1_0.html                             **
**                                                                       **
**  A copy is found in the file license.txt.                             **
**                                                                       **
**  This copyright notice MUST APPEAR in all copies of the file!         **
**************************************************************************/
package net.sourceforge.eclipsefrills.resource.handler;

import net.sourceforge.eclipsefrills.resource.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler used to mark specified folders as 'derived' so that they will not appear
 * in searches and quick find results.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class MarkDerivedHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		int count = 0;

		final Shell shell = ((Event)event.getTrigger()).display.getActiveShell();
		final InputDialog dialog = new InputDialog(
			shell,
			Messages.MarkDerivedAction_Input_Title,
			Messages.MarkDerivedAction_Input_Description,
			"target",
			new InputValidator()
		);

		if(dialog.open() == InputDialog.OK){
			final String folderName = dialog.getValue();
			try {
				for(final IProject proj : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
					final IFolder folder = proj.getFolder(folderName);
					if(folder.exists() && !folder.isDerived()){
						folder.setDerived(true);
						count++;
					}
				}
			} catch(final CoreException ce){
				MessageDialog.openError(
					shell,
					Messages.MarkDerivedAction_Error_Title,
					Messages.bind(Messages.MarkDerivedAction_Error_Message, ce.getMessage())
				);
			}

			MessageDialog.openInformation(
				shell,
				Messages.MarkDerivedAction_Result_Title,
				Messages.bind(Messages.MarkDerivedAction_Result_Message, count)
			);
		}
		return null;
	}

	private static final class InputValidator implements IInputValidator {

		public String isValid(final String txt){
			if(txt != null && txt.trim().length() != 0){
				return null;
			} else {
				return Messages.MarkDerivedAction_Error_Folder;
			}
		}
	}
}
