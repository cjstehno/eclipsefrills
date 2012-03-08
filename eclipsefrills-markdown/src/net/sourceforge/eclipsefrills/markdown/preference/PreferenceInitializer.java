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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Used to initialize the default preference values.
 *
 * @author Christopher J. Stehno (cjstehno@users.sourceforge.net)
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(MarkdownPreference.DefaultEditorTab.key(), MarkdownPreference.DEFAULTTAB_SOURCE);
		store.setDefault(MarkdownPreference.UseStyleSheet.key(), false);
		store.setDefault(MarkdownPreference.AlwaysReloadStyleSheet.key(), false);
	}
}
