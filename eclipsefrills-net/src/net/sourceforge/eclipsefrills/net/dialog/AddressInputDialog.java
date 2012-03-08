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
package net.sourceforge.eclipsefrills.net.dialog;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sourceforge.eclipsefrills.net.Messages;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Internet Address Resolver Input Dialog.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class AddressInputDialog extends InputDialog {

	private static final String EMPTY = "";

	/**
	 * Creates the dialog with the given parent shell.
	 *
	 * @param parentShell
	 */
	public AddressInputDialog(final Shell parentShell){
		super(
			parentShell,
			Messages.AddressInputDialog_Input_Title,
			Messages.AddressInputDialog_Input_Description,
			EMPTY,
			new AddressInputValidator()
		);
	}

	/**
	 * Retrieves the address from the input dialog as the populated InetAddress object.
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	public InetAddress getAddress() throws UnknownHostException {
		return InetAddress.getByName(getValue());
	}

	/**
	 * Input validation handler for the dialog.
	 *
	 * @author Christopher J. Stehno (chris@stehno.com)
	 */
	private static class AddressInputValidator implements IInputValidator {
		@Override
		public String isValid(final String newText) {
			return newText != null && newText.length() > 0 ? null : Messages.AddressInputDialog_Error_NoAddress;
		}
	}
}