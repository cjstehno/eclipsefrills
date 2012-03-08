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
package net.sourceforge.eclipsefrills.workingset.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetSelectionDialog;

/**
 * Action that allows selection of working sets for search results.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class SelectWorkingSetForResultsAction implements IViewActionDelegate {

	private IViewPart viewPart;

	@Override
	public void init(final IViewPart view) {
		this.viewPart = view;
	}

	@Override
	public void run(final IAction action) {
		final ISelection selection = viewPart.getSite().getSelectionProvider().getSelection();
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final IResource[] resources = new IResource[sel.size()];
				int i = 0;
				for(final Object obj : sel.toArray()){
					resources[i++] = (IResource)obj;
				}

				createWorkingSet(resources);
			}
		}
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {}

	private void createWorkingSet(final IResource[] resources){
		final IWorkingSetManager workingSetMgr = PlatformUI.getWorkbench().getWorkingSetManager();

		final IWorkingSetSelectionDialog dialog = workingSetMgr.createWorkingSetSelectionDialog(viewPart.getSite().getShell(), true);
		if(Window.OK == dialog.open()){
			final IWorkingSet[] workingSets = dialog.getSelection();
			if(workingSets != null && workingSets.length != 0){
				for(final IWorkingSet ws : workingSets){
					final Set<IAdaptable> items = new HashSet<IAdaptable>(Arrays.asList(ws.getElements()));
					items.addAll(Arrays.asList(resources));
					ws.setElements(items.toArray(new IAdaptable[items.size()]));
				}
			}
		}
	}
}
