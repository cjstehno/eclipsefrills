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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class MarkdownContentExportWizardPage extends WizardPage {

	private static final String EMPTY_STRING = "";
	private Text filePath;

	MarkdownContentExportWizardPage(){
		super("exportWizard","Export Rendered Markdown Content",null);
	}

	/**
	 * Used to retrieve the file path selected by the user.
	 *
	 * @return the selected file path.
	 */
	String getFilePath(){return(filePath.getText());}

	public void createControl(final Composite parent) {
		final Composite panel = new Composite(parent, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));

		// path
		createLabel(panel,"Export File:","The rendered content will be exported to this directory.",1);

		filePath = new Text(panel,SWT.BORDER | SWT.SINGLE);
		filePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filePath.setEditable(false);
		filePath.addModifyListener(new ModifyListener(){
			public void modifyText(final ModifyEvent e){dialogChanged();}
		});

		final Button directoryBtn = new Button(panel,SWT.PUSH);
		directoryBtn.setText("Browse...");
		directoryBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final DirectoryDialog dialog = new DirectoryDialog(getShell(),SWT.SAVE);
				final String path = dialog.open();
				filePath.setText(path != null ? path : EMPTY_STRING);
			}
		});

		dialogChanged();
		setControl(panel);
	}

	private void dialogChanged(){
		if(filePath.getText() == null || filePath.getText().length() == 0){
			updateStatus("No export directory specified!");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private Label createLabel(final Composite container, final String text, final String tip, final int hspan){
		final Label lbl = new Label(container,SWT.NULL);
		lbl.setText(text);
		lbl.setToolTipText(tip);
		lbl.setLayoutData(createGridData(hspan));
		return(lbl);
	}

	private GridData createGridData(final int hspan){
		final GridData gd = new GridData();
		gd.horizontalSpan = hspan;
		return(gd);
	}
}
