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

public enum MarkdownPreference {

	DefaultEditorTab("default-editor-tab"),
	UseStyleSheet("use-stylesheet"),
	StyleSheetFile("stylesheet-file"),
	AlwaysReloadStyleSheet("always-reload-stylesheet");

	public static final String DEFAULTTAB_SOURCE = "source";
	public static final String DEFAULTTAB_PREVIEW = "preview";

	private final String key;

	private MarkdownPreference(final String key){
		this.key = key;
	}

	public String key(){return key;}
}
