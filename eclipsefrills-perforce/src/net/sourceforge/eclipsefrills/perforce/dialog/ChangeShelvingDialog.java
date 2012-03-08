package net.sourceforge.eclipsefrills.perforce.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.perforce.p4api.IChangelist;
import com.perforce.p4api.IPerforceFile;

public class ChangeShelvingDialog extends Dialog {

    private String title,message;
    
    private String shelfTitle = "";
    private String shelfDesc = "";
    private boolean autoRevert = false;
    private IPerforceFile[] selectedFiles;
    
    private IInputValidator validator;
    private Button okButton;
    private Text titleText;
    private Text errorMessageText;
    private String errorMessage;
    private IChangelist[] changelists;
	private Text descriptionTxt;
	private Button revertCheck;
	private CheckboxTreeViewer changeTreeViewer;

    /**
     * Creates an input dialog with OK and Cancel buttons. Note that the dialog
     * will have no visual representation (no widgets) until it is told to open.
     * <p>
     * Note that the <code>open</code> method blocks for input dialogs.
     * </p>
     * 
     * @param parentShell
     *            the parent shell, or <code>null</code> to create a top-level
     *            shell
     * @param dialogTitle
     *            the dialog title, or <code>null</code> if none
     * @param dialogMessage
     *            the dialog message, or <code>null</code> if none
     * @param initialValue
     *            the initial input value, or <code>null</code> if none
     *            (equivalent to the empty string)
     * @param validator
     *            an input validator, or <code>null</code> if none
     */
    public ChangeShelvingDialog(final Shell parentShell) {
        super(parentShell);
        this.title = "Shelve Changes";
        message = "Select changes to be shelved.";

        this.validator = new IInputValidator(){
			public String isValid(final String newText) {
				return titleText.getText() != null && titleText.getText().trim().length() > 0 ? null : "No title specified!";
			}
        };
    }
    
    public void setInput(final IChangelist[] changelists){
    	this.changelists = changelists;
    }
    
    @Override
    protected Point getInitialSize() {
    	return new Point(800,600);
    }

    @Override
	protected void buttonPressed(final int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
            shelfTitle = titleText.getText();
            shelfDesc = descriptionTxt.getText();
            autoRevert = revertCheck.getSelection();

        	final Object[] checkedElts = changeTreeViewer.getCheckedElements();
        	final List<IPerforceFile> files = new ArrayList<IPerforceFile>(checkedElts.length);
        	for(final Object item : checkedElts){
        		if(item instanceof IPerforceFile){
        			files.add((IPerforceFile) item);
        		}
        	}
        	selectedFiles = files.toArray(new IPerforceFile[files.size()]);
            
        } else {
            shelfTitle = null;
            shelfDesc = null;
            autoRevert = false;
            selectedFiles = null;
        }
        super.buttonPressed(buttonId);
    }

    @Override
	protected void configureShell(final Shell shell) {
        super.configureShell(shell);
        if (title != null) {
			shell.setText(title);
		}
    }

    @Override
	protected void createButtonsForButtonBar(final Composite parent) {
        // create OK and Cancel buttons by default
        okButton = createButton(parent, IDialogConstants.OK_ID,IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
        //do this here because setting the text will set enablement on the ok
        // button
        titleText.setFocus();
        if (shelfTitle != null) {
            titleText.setText(shelfTitle);
            titleText.selectAll();
        }
    }

    @Override
	protected Control createDialogArea(final Composite parent) {
        // create composite
        final Composite composite = (Composite) super.createDialogArea(parent);
        
        // create message
        if (message != null) {
            final Label label = new Label(composite, SWT.WRAP);
            label.setText(message);
            final GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER);
            data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
            label.setLayoutData(data);
            label.setFont(parent.getFont());
        }
        
        setupDialogArea(composite);
        
        errorMessageText = new Text(composite, SWT.READ_ONLY | SWT.WRAP);
        errorMessageText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        setErrorMessage(errorMessage);

        applyDialogFont(composite);
        return composite;
    }
    
    private void setupDialogArea(final Composite composite){
    	final Label titleLbl = new Label(composite, SWT.NONE);
    	titleLbl.setLayoutData(new GridData());
    	titleLbl.setText("Title:");

    	final GridData titleTxtLData = new GridData();
    	titleTxtLData.grabExcessHorizontalSpace = true;
    	titleTxtLData.horizontalAlignment = GridData.FILL;
    	titleText = new Text(composite, SWT.BORDER);
    	titleText.setLayoutData(titleTxtLData);
    	titleText.addModifyListener(new ModifyListener(){
			public void modifyText(final ModifyEvent e) {
				validateInput();
			}
    	});

    	final Label descriptionLbl = new Label(composite, SWT.NONE);
    	descriptionLbl.setLayoutData(new GridData());
    	descriptionLbl.setText("Description:");

    	final GridData descriptionTxtLData = new GridData();
    	descriptionTxtLData.grabExcessHorizontalSpace = true;
    	descriptionTxtLData.horizontalAlignment = GridData.FILL;
    	descriptionTxt = new Text(composite, SWT.BORDER);
		descriptionTxt.setLayoutData(descriptionTxtLData);

    	final Label changeLbl = new Label(composite, SWT.NONE);
    	changeLbl.setLayoutData(new GridData());
    	changeLbl.setText("Changelists:");
    	
    	changeTreeViewer = new CheckboxTreeViewer(composite, SWT.NONE);
		changeTreeViewer.setContentProvider(new ChangelistContentProvider());
    	changeTreeViewer.setLabelProvider(new ChangelistLabelProvider());
    	final GridData treeGD = new GridData(GridData.FILL,GridData.FILL,true,true);
    	treeGD.minimumHeight = 300;
    	changeTreeViewer.getControl().setLayoutData(treeGD);
    	changeTreeViewer.setInput(changelists);
    	changeTreeViewer.addCheckStateListener(new ICheckStateListener(){
			public void checkStateChanged(final CheckStateChangedEvent event) {
				if(event.getElement() instanceof IChangelist){
					final IChangelist list = (IChangelist)event.getElement();
					final boolean checked = event.getChecked();
					for(final IPerforceFile file : list.getFiles()){
						changeTreeViewer.setChecked(file, checked);
					}
				}
			}
    	});
    	
    	revertCheck = new Button(composite, SWT.CHECK | SWT.LEFT);
		revertCheck.setLayoutData(new GridData());
    	revertCheck.setText("Revert changes once shelved.");
    }

    /**
     * Returns the ok button.
     * 
     * @return the ok button
     */
    protected Button getOkButton() {
        return okButton;
    }

    /**
     * Returns the string typed into this input dialog.
     * 
     * @return the input string
     */
    public String getShelfTitle() {
        return shelfTitle;
    }
    
    public String getShelfDescription(){return shelfDesc;}
    
    public boolean isAutoRevertShelvedChanges(){return autoRevert;}
    
    public IPerforceFile[] getShelvedFiles(){return selectedFiles;}

    /**
     * Validates the input.
     * <p>
     * The default implementation of this framework method delegates the request
     * to the supplied input validator object; if it finds the input invalid,
     * the error message is displayed in the dialog's message line. This hook
     * method is called whenever the text changes in the input field.
     * </p>
     */
    protected void validateInput() {
        String errorMessage = null;
        if (validator != null) {
            errorMessage = validator.isValid(titleText.getText());
        }
        // Bug 16256: important not to treat "" (blank error) the same as null
        // (no error)
        setErrorMessage(errorMessage);
    }

    /**
     * Sets or clears the error message.
     * If not <code>null</code>, the OK button is disabled.
     * 
     * @param errorMessage
     *            the error message, or <code>null</code> to clear
     * @since 3.0
     */
    public void setErrorMessage(final String errorMessage) {
    	this.errorMessage = errorMessage;
    	if (errorMessageText != null && !errorMessageText.isDisposed()) {
    		errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
    		// Disable the error message text control if there is no error, or
    		// no error text (empty or whitespace only).  Hide it also to avoid
    		// color change.
    		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=130281
    		final boolean hasError = errorMessage != null && StringConverter.removeWhiteSpaces(errorMessage).length() > 0;
    		errorMessageText.setEnabled(hasError);
    		errorMessageText.setVisible(hasError);
    		errorMessageText.getParent().update();
    		// Access the ok button by id, in case clients have overridden button creation.
    		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
    		final Control button = getButton(IDialogConstants.OK_ID);
    		if (button != null) {
    			button.setEnabled(errorMessage == null);
    		}
    	}
    }
    
	/**
	 * 
	 * @author cstehno
	 */
    static final class ChangelistLabelProvider extends LabelProvider {
    	
    	@Override
    	public String getText(final Object element) {
    		if(element instanceof IChangelist){
				return ((IChangelist)element).getID();
				
			} else if(element instanceof IPerforceFile){
				return ((IPerforceFile)element).getDepotPath();
				
			}
			return null;
    	}
    }
    
	/**
	 * 
	 * @author cstehno
	 */
	static final class ChangelistContentProvider implements ITreeContentProvider {
		
		private IChangelist[] changelists;
		
		public Object[] getChildren(final Object parentElement) {
			if(parentElement instanceof IChangelist){
				return ((IChangelist)parentElement).getFiles();
			}
			return null;
		}

		public Object getParent(final Object element) {
			// FIXME: should be able to work this out
//			if(element instanceof IPerforceFile){
//				IPerforceFile file = (IPerforceFile)element;
//				return file.get
//			}
			return null;
		}

		public boolean hasChildren(final Object element) {
			if(element instanceof IChangelist) return true;
			return false;
		}

		public Object[] getElements(final Object inputElement) {
			return changelists;
		}

		public void dispose() {}

		public void inputChanged(final Viewer viewer, final Object oldInput,final Object newInput) {
			this.changelists = (IChangelist[])newInput;
		}		
	}
}
