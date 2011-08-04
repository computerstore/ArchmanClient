/*******************************************************************************
 * Copyright (C) 2008  CS-Computer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     CS-Computer - initial API and implementation
 ******************************************************************************/
package it.archiworld.archman.client.protocol.util;

import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.util.RCPEJBPreferenceStore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ResponsiblePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	protected TableViewer tableViewer;
	protected Button addButton;
	protected Button removeButton;
	protected List<String> responsibles = new ArrayList<String>();
	
	public static final String RESPONSIBLES = "preference.Responsibles"; //$NON-NLS-1$

	@Override
	protected Control createContents(Composite parent) {

		Composite topLevelContainer = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		topLevelContainer.setLayout(layout);
		createTable(topLevelContainer);
	
	    Composite buttonparent = new Composite(topLevelContainer, SWT.NULL);
	    buttonparent.setLayout(new GridLayout());
	    buttonparent.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		createAddButton(buttonparent);
		createRemoveButton(buttonparent);
		return topLevelContainer;
	}

	@Override
	public void init(IWorkbench workbench) {
		try {
			setPreferenceStore(new RCPEJBPreferenceStore());
			responsibles = ((RCPEJBPreferenceStore)this.getPreferenceStore()).getStringList(RESPONSIBLES, null);
			if (responsibles==null) responsibles = new ArrayList<String>();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		
	}
	
	protected void createTable(Composite topLevelContainer){
	     tableViewer = new TableViewer(topLevelContainer, SWT.BORDER);
	     tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	          
	     tableViewer.setContentProvider( new IStructuredContentProvider() {
	    	 public Object[] getElements(Object inputElement) {
	    		 //unconditionally set elements to types - no matter what the input is 
	    		 return responsibles.toArray();
	    	 }
	         public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
	         public void dispose() { }
	     });
	              
	     tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
	    	 public void selectionChanged(SelectionChangedEvent event) {
//	    		 if ( tableViewer.getSelection() != null ) {
//	    			 IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
//	    			 String currentValue = (String) selection.getFirstElement();
//	    			 boolean enableRemove = !Arrays.asList(DependencyTypeRegistr.KNOWN_TYPES).contains(currentType);
//	    			 removeTypeButton.setEnabled(enableRemove);
//	    		 }
	    	 }	 
	     });
	             
	     tableViewer.setSorter(new ViewerSorter() {
	    	 public int compare(Viewer viewer, Object e1, Object e2) {
	    		 return ((String) e1).compareTo((String) e2);
	    	 }
	     });
	     tableViewer.setInput(responsibles);
	}
	
	private void createAddButton(Composite bparent) {
		addButton = new Button(bparent, SWT.PUSH);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.setAlignment(SWT.CENTER);
		addButton.setText(Messages.getString("ResponsiblePreferencePage.Button_Add")); //$NON-NLS-1$
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				//do nothing
		    }
		    public void widgetSelected(SelectionEvent e) {
		    	String newType = openNewDialog();
		        if ( newType!=null ) { //&& !CANCEL
		        	responsibles.add(newType);
		        	tableViewer.refresh();
		        }
		    }
		});
	}
	
	private void createRemoveButton(Composite bparent) {
		removeButton = new Button(bparent, SWT.PUSH);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.setAlignment(SWT.CENTER);
		removeButton.setText(Messages.getString("ResponsiblePreferencePage.Button_Remove")); //$NON-NLS-1$
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
		    //do nothing
		    }
			public void widgetSelected(SelectionEvent e) {
				if ( tableViewer.getSelection() != null ) {
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
					String currentType = (String) selection.getFirstElement();
					responsibles.remove(currentType);
					tableViewer.refresh();
				}
			}
		});
	}
	
	private String openNewDialog() {
		 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		 shell = Display.getCurrent().getActiveShell();
		 String dialogTitle = Messages.getString("ResponsiblePreferencePage.Title_NewResponsible"); //$NON-NLS-1$
		 String dialogMessage = Messages.getString("ResponsiblePreferencePage.DialogMessage_AddNewResponsible"); //$NON-NLS-1$
		 IInputValidator validator = new IInputValidator() {
		 	public String isValid(String newText) {
		 		if ( responsibles.contains(newText) ) {
		 			return Messages.getString("ResponsiblePreferencePage.InfoError_ResponsibleExists"); //$NON-NLS-1$
		 		}
		 		if ( newText==null ) {
		 			return Messages.getString("ResponsiblePreferencePage.InfoError_NullNotValidResponsible"); //$NON-NLS-1$
		 		}
		 		return null;
			}  
		};
		InputDialog dialog = new InputDialog(shell, dialogTitle, dialogMessage, null, validator);
		int result = dialog.open();
		return result == Window.OK ? dialog.getValue() : null;
	}
	
	@Override
	public boolean performOk() {
		super.performOk();
		// save values
	    ((RCPEJBPreferenceStore)this.getPreferenceStore()).setValue(RESPONSIBLES, responsibles);
	    return true;
	}

}
