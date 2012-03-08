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

package net.sourceforge.eclipsefrills.markdown.preference;

import net.sourceforge.eclipsefrills.markdown.Activator;
import net.sourceforge.eclipsefrills.markdown.Messages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MarkdownPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public MarkdownPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.PreferencePage_Description);
	}

	public void init(final IWorkbench workbench) {}

	@Override
	public void createFieldEditors() {
		// default editor tab field
		addField(new RadioGroupFieldEditor(
			MarkdownPreference.DefaultEditorTab.key(),
			Messages.PreferencePage_DefaultTab_Label,
			1,
			new String[][] {
				{Messages.PreferencePage_DefaultTab_Source_Label,MarkdownPreference.DEFAULTTAB_SOURCE },
				{Messages.PreferencePage_DefaultTab_Preview_Label,MarkdownPreference.DEFAULTTAB_PREVIEW}
			}, getFieldEditorParent())
		);

		// stylesheet use field
		addField(new BooleanFieldEditor(MarkdownPreference.UseStyleSheet.key(),Messages.PreferencePage_UseStylesheet_Label,getFieldEditorParent()));

		// stylesheet file field
		addField(new FileFieldEditor(MarkdownPreference.StyleSheetFile.key(),Messages.PreferencePage_StylesheetFile_Label, getFieldEditorParent()));

		// always reload stylesheet
		addField(new BooleanFieldEditor(MarkdownPreference.AlwaysReloadStyleSheet.key(),Messages.PreferencePage_ReloadStylesheet_Label,getFieldEditorParent()));
	}
}