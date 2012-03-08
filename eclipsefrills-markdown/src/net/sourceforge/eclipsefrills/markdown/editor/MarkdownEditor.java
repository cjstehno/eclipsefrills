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

import net.sourceforge.eclipsefrills.markdown.Activator;
import net.sourceforge.eclipsefrills.markdown.Messages;
import net.sourceforge.eclipsefrills.markdown.preference.MarkdownPreference;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.petebevin.markdown.MarkdownProcessor;

/**
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class MarkdownEditor extends MultiPageEditorPart implements IResourceChangeListener {

	private final MarkdownProcessor markdown = new MarkdownProcessor();
	private TextEditor editor;
	private Browser browser;
	private final StylesheetProvider styleProvider = new StylesheetProvider();

	public MarkdownEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	@Override
	protected void createPages() {
		createEditorPage();
		createPreviewPage();

		// set the default display tab
		final String defaultTab = Activator.getDefault().getPreferenceStore().getString(MarkdownPreference.DefaultEditorTab.key());
		if(defaultTab.equalsIgnoreCase(MarkdownPreference.DEFAULTTAB_SOURCE)){
			setActivePage(0);
		} else if(defaultTab.equalsIgnoreCase(MarkdownPreference.DEFAULTTAB_PREVIEW)){
			setActivePage(1);
		}
	}

	/**
	 * Creates page 0 of the multi-page editor,
	 * which contains a text editor.
	 */
	void createEditorPage() {
		try {
			editor = new TextEditor();
			final int index = addPage(editor, getEditorInput());
			setPartName(editor.getTitle());
			setPageText(index,Messages.Editor_SourceTab_Label);
		} catch (final PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),"Error creating nested text editor",null,e.getStatus());
		}
	}

	/**
	 * Renders markdown as html and displays in browser view.
	 */
	void createPreviewPage() {
		final Composite composite = new Composite(getContainer(), SWT.NONE);
		final FillLayout layout = new FillLayout();
		composite.setLayout(layout);

		browser = new Browser(composite,SWT.NONE);

		final int index = addPage(composite);
		setPageText(index, Messages.Editor_PreviewTab_Label);
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	@Override
	public void doSave(final IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	@Override
	public void doSaveAs() {
		final IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	public void gotoMarker(final IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(final IEditorSite site, final IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput)){
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		}
		super.init(site, editorInput);
	}

	@Override
	public boolean isSaveAsAllowed() {return true;}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	@Override
	protected void pageChange(final int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			renderMarkdown();
		}
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					final IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							final IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}
			});
		}
	}

	/**
	 * Convert the text editor markdown content into HTML and feed it to the browser pane.
	 */
	private void renderMarkdown(){
		final String editorText = editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();
		browser.setText(styleProvider.provideStyleSheet(markdown.markdown(editorText)));
	}
}
