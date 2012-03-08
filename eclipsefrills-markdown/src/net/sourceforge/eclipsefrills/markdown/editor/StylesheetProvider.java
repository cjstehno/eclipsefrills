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
package net.sourceforge.eclipsefrills.markdown.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sourceforge.eclipsefrills.markdown.Activator;
import net.sourceforge.eclipsefrills.markdown.preference.MarkdownPreference;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Helper used to provide the stylesheet loading and injection. It also caches the stylesheet content
 * so that it does not re-load each time the preview is loaded (unless the always reload preference is set).
 *
 * @author Christopher J. Stehno (cjstehno@users.sourceforge.net)
 *
 */
public class StylesheetProvider {

	private static final String[] parts = {"<html><head><style type='text/css'>","</style></head><body>","</body></html>"};
	private String cachedStylePath, cachedStyleSheet;

	public String provideStyleSheet(final String renderedTxt){
		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		if(store.getBoolean(MarkdownPreference.UseStyleSheet.key())){
			final String stylePath = store.getString(MarkdownPreference.StyleSheetFile.key());
			if(forceReload(store) || !same(cachedStylePath,stylePath)){
				cachedStylePath = stylePath;
				cachedStyleSheet = loadStyleSheet(stylePath);
			}

			return parts[0] + cachedStyleSheet + parts[1] + renderedTxt + parts[2];
		} else {
			return renderedTxt;
		}
	}

	private boolean forceReload(final IPreferenceStore store){
		return store.getBoolean(MarkdownPreference.AlwaysReloadStyleSheet.key());
	}

	private boolean same(final String cached, final String str){
		return (cached == null && str == null) || (cached != null && cached.equals(str));
	}

	private String loadStyleSheet(final String path){
		final File styleFile = new File(path);
		if(styleFile.exists() && styleFile.canRead()){
			final StringBuilder css = new StringBuilder();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(styleFile));
				for(String line = reader.readLine(); line != null; line = reader.readLine()){
					css.append(line);
					css.append('\n');
				}
			} catch(final IOException ioe){
				// FIXME: temporary
				System.out.println(ioe);
			} finally {
				if(reader != null){
					try {reader.close();} catch(final IOException ioe){}
				}
			}

			return css.toString();

		} else {
			return "";
		}
	}
}
