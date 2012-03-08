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
package net.sourceforge.eclipsefrills.net.handler;

import net.sourceforge.eclipsefrills.net.Messages;
import net.sourceforge.eclipsefrills.net.dialog.AddressInputDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for the Resolve Address command. Opens the input dialog and responds with the
 * result or error in the appropriate standard dialog.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ResolveAddressHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final Shell shell = ((Event)event.getTrigger()).display.getActiveShell();
		final AddressInputDialog dialog = new AddressInputDialog(shell);
		if(dialog.open() == InputDialog.OK){
			try {
				MessageDialog.openInformation(
						shell,
						Messages.ResolveAddressAction_Results_Title,
						Messages.bind(Messages.ResolveAddressAction_Results_Message, dialog.getAddress().toString())
				);
			} catch(final Exception ex){
				MessageDialog.openError(
					shell,
					Messages.ResolveAddressAction_Error_Title,
					Messages.bind(Messages.ResolveAddressAction_Error_Message, ex.getMessage())
				);
			}
		}
		return null;
	}
}
