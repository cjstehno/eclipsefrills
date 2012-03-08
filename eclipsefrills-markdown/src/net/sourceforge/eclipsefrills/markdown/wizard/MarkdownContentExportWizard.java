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
package net.sourceforge.eclipsefrills.markdown.wizard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.eclipsefrills.markdown.Activator;
import net.sourceforge.eclipsefrills.markdown.editor.StylesheetProvider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.petebevin.markdown.MarkdownProcessor;


public class MarkdownContentExportWizard extends Wizard implements IExportWizard {

	private static final String EXTENSION = "mkd";
	private MarkdownContentExportWizardPage page;
	private final MarkdownProcessor markdown = new MarkdownProcessor();
	private final StylesheetProvider styleProvider = new StylesheetProvider();
	private List<IFile> files;

	@SuppressWarnings("unchecked")
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		files = new ArrayList<IFile>(selection.size());
		if(!selection.isEmpty()){
			final Iterator<IAdaptable> items = selection.iterator();
			while(items.hasNext()){
				final IFile file = (IFile)items.next().getAdapter(IFile.class);
				if(file != null && file.getFileExtension().equalsIgnoreCase(EXTENSION)){
					files.add(file);
				}
			}
		}
	}

	/**
	 * @see Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.page = new MarkdownContentExportWizardPage();
		addPage(page);
	}

	/**
	 * @see Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final File directory = new File(page.getFilePath());

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor,directory);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (final InterruptedException e) {
			return false;
		} catch (final InvocationTargetException e) {
			// FIXME: do something more useful here
			return false;
		}
		return true;
	}

	/**
	 * Performs the actual finishing step action of the externalizer (import/export).
	 *
	 * @param monitor the progress monitor
	 * @param file the target file
	 * @param format the target file format
	 * @throws CoreException if there is a problem finishing the wizard.
	 */
	protected void doFinish(final IProgressMonitor monitor, final File directory) throws CoreException {
		for(final IFile file : files){
			writeFile(directory, file);
		}
	}

	private void writeFile(final File directory, final IFile file) throws CoreException {
		final String content = styleProvider.provideStyleSheet(markdown.markdown(loadFile(file)));

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(directory,file.getName())));
			writer.write(content);
		} catch(final Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			if(writer != null){
				try {writer.close();} catch(final Exception ex){}
			}
		}
	}

	private String loadFile(final IFile file){
		final StringBuilder str = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file.getContents()));
			for(String line = reader.readLine(); line != null; line = reader.readLine()){
				str.append(line).append('\n');
			}
		} catch(final Exception ex){
			// FIXME: do something meaningful
			ex.printStackTrace();
		} finally {
			if(reader != null){
				try {reader.close();} catch(final Exception ex){}
			}
		}
		return str.toString();
	}
}
