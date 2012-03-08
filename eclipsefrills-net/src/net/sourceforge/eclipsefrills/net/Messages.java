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
package net.sourceforge.eclipsefrills.net;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized messages for the EclipseFrills Net plug-in.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.eclipsefrills.net.messages";

	public static String AddressInputDialog_Input_Title;
	public static String AddressInputDialog_Input_Description;
	public static String AddressInputDialog_Error_NoAddress;

	public static String ResolveAddressAction_Error_Message;
	public static String ResolveAddressAction_Error_Title;
	public static String ResolveAddressAction_Results_Title;
	public static String ResolveAddressAction_Results_Message;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){super();}
}
