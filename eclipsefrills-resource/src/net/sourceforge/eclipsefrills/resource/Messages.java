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
package net.sourceforge.eclipsefrills.resource;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized messages for the EclipseFrills Resource plug-in.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.eclipsefrills.resource.messages";

	public static String MarkDerivedAction_Input_Title;
	public static String MarkDerivedAction_Input_Description;
	public static String MarkDerivedAction_Result_Title;
	public static String MarkDerivedAction_Result_Message;
	public static String MarkDerivedAction_Error_Folder;
	public static String MarkDerivedAction_Error_Title;
	public static String MarkDerivedAction_Error_Message;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){super();}
}
