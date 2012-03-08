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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class MarkdownMultipageEditorContributor extends MultiPageEditorActionBarContributor {

	private IEditorPart activeEditorPart;
	private Action sampleAction;

	public MarkdownMultipageEditorContributor() {
		super();
		createActions();
	}

	/**
	 * Returns the action registed with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(final ITextEditor editor, final String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}

	/* (non-JavaDoc)
	 * Method declared in AbstractMultiPageEditorActionBarContributor.
	 */
	@Override
	public void setActivePage(final IEditorPart part) {
		if (activeEditorPart == part) return;

		activeEditorPart = part;

		final IActionBars actionBars = getActionBars();
		if (actionBars != null) {
			final ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;

			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(),getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(),getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(),getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(),getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(),getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(),getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(),getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.updateActionBars();
		}
	}

	private void createActions() {
		sampleAction = new Action() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, "Acceleration Plug-in", "This should be a markdown cheatsheet");
			}
		};
		sampleAction.setText("Cheat Sheet");
		sampleAction.setToolTipText("Markdown cheat sheet");
		sampleAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
	}

	@Override
	public void contributeToMenu(final IMenuManager manager) {
		final IMenuManager menu = new MenuManager("&Markdown");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(sampleAction);
	}

	@Override
	public void contributeToToolBar(final IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(sampleAction);
	}
}
