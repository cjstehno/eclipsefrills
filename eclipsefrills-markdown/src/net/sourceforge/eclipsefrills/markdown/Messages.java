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
package net.sourceforge.eclipsefrills.markdown;

import org.eclipse.osgi.util.NLS;

/**
 * Externalized messages for the EclipseFrills Markdown plug-in.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.eclipsefrills.markdown.messages";

	public static String PreferencePage_Description;
	public static String PreferencePage_DefaultTab_Label;
	public static String PreferencePage_DefaultTab_Source_Label;
	public static String PreferencePage_DefaultTab_Preview_Label;
	public static String PreferencePage_UseStylesheet_Label;
	public static String PreferencePage_StylesheetFile_Label;
	public static String PreferencePage_ReloadStylesheet_Label;

	public static String Editor_SourceTab_Label;
	public static String Editor_PreviewTab_Label;

	public static String NewWizard_CreateTask_Text;
	public static String NewWizard_OpenTask_Text;
	public static String NewWizard_Error_NoContainer;

	public static String NewWizardPage_Title;
	public static String NewWizardPage_Description;
	public static String NewWizardPage_Label_Container;
	public static String NewWizardPage_Label_Browse;
	public static String NewWizardPage_Label_FileName;
	public static String NewWizardPage_Invalid_SpecifyContainer;
	public static String NewWizardPage_Invalid_ContainerExist;
	public static String NewWizardPage_Invalid_ProjectWritable;
	public static String NewWizardPage_Invalid_NoFilename;
	public static String NewWizardPage_Invalid_Filename;
	public static String NewWizardPage_Invalid_Extension;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){super();}
}
