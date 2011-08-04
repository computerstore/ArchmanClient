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
package it.archiworld.archman.client.crm.wizzard;

import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.util.ExportOptionWizardManager;
import it.archiworld.util.MemberFieldExportSelector;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ExportOptionWizardPageThree extends WizardPage {
	
	private Composite container;
	protected FormToolkit toolkit;
	protected DataBindingContext dataBindingContext = new DataBindingContext();
	protected MemberFieldExportSelector entity;
	Button upButton, downButton;
	ListViewer viewer;

	protected ExportOptionWizardPageThree(String pageName) {
		super(pageName);
		toolkit = new FormToolkit(Display.getCurrent());	
		toolkit.setBackground(new Color(Display.getDefault(),222,222,222)); //TODO Set right grey level
	}

	@Override
	public void createControl(Composite parent) {
		WizardPageSupport.create(this, dataBindingContext);

		entity = ((ExportOptionWizard)this.getWizard()).getEntity();

		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		org.eclipse.swt.widgets.List orderList = new org.eclipse.swt.widgets.List(container, SWT.V_SCROLL | SWT.FILL);
		orderList.setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer = new ListViewer(orderList);

		populateViewer();

		Composite buttonBar = new Composite(container,SWT.NULL);
		buttonBar.setLayout(new FillLayout(SWT.VERTICAL));
		
		ListOrderListener orderLister = new ListOrderListener();
		upButton = toolkit.createButton(buttonBar, Messages.getString("ExportOptionWizardPageThree_Button_Up"), SWT.FLAT); //$NON-NLS-1$
		upButton.addSelectionListener(orderLister);
		
		downButton = toolkit.createButton(buttonBar, Messages.getString("ExportOptionWizardPageThree_Button_Down"), SWT.FLAT); //$NON-NLS-1$
		downButton.addSelectionListener(orderLister);
		
		Button saveButton = toolkit.createButton(container, Messages.getString("ExportOptionWizardPageThree_Button_Save"), SWT.FLAT); //$NON-NLS-1$
		saveButton.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = openNewDialog();
				if(name!=null){
					entity.setName(name);
					try {
						((ExportOptionWizardManager)ConnectionFactory.getConnection("ExportOptionWizardManagerBean")).saveMemberFieldExportSelector(entity); //$NON-NLS-1$
					} catch (ConnectionException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}	
			}
		});
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);

	}

	@Override
	public Control getControl() {
		return container;
	}

	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return super.canFlipToNextPage();
	}

	public List<String> getData(){		
		List<String> list = new ArrayList<String>(); 
		for(int i=0;i<viewer.getList().getItems().length;i++)
			list.add(viewer.getElementAt(i).toString());
		return list;
	}
	
	protected void populateViewer(){
//		System.out.println("Populating Viewer page Three"); //$NON-NLS-1$
		entity=((ExportOptionWizard)getWizard()).getEntity();
				 		
		viewer.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return (Object[])inputElement;
			}
			
		});
		viewer.setLabelProvider(new ILabelProvider(){

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				return (((ExportOptionWizard)getWizard()).getFields().get(element));
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		if(entity!=null && entity.getProperties()!=null && !entity.getProperties().isEmpty())
			viewer.setInput(entity.getProperties().toArray());
		viewer.refresh(true);	
	}

	protected class ListOrderListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			handleButtonPressed((Button)e.widget);		
		}

		private void handleButtonPressed(Button button) {
			if (button == upButton) {
				moveSelectionUp();
			} else if (button == downButton) {
				moveSelectionDown();
			}
	        if (getControl().isDisposed()) {
	            return;
	        }
		//	handleListSelectionChanged();
			viewer.getList().setFocus();
		}

		/**
		 * Move the current selection in the build list down.
		 */
		private void moveSelectionDown() {
			org.eclipse.swt.widgets.List sortList= viewer.getList();
			int indices[]= sortList.getSelectionIndices();
			if (indices.length < 1) {
				return;
			}
			int newSelection[]= new int[indices.length];
			int max= sortList.getItemCount() - 1;
			for (int i = indices.length - 1; i >= 0; i--) {
				int index= indices[i];
				if (index < max) {
					Object object = viewer.getElementAt(index);
					viewer.remove(object);
					viewer.insert(object, index+1);
					newSelection[i]= index+1;
				}
			}
			sortList.setSelection(newSelection);
		}

		/**
		 * Move the current selection in the build list up.
		 */
		private void moveSelectionUp() {
			org.eclipse.swt.widgets.List sortList = viewer.getList();
			int indices[]= sortList.getSelectionIndices();
			int newSelection[]= new int[indices.length];
			for (int i = 0; i < indices.length; i++) {
				int index= indices[i];
				if (index > 0) {
					Object object = viewer.getElementAt(index);
					viewer.remove(object);
					viewer.insert(object, index-1);					
					newSelection[i]= index-1;
				}
			}
			sortList.setSelection(newSelection);
		}

	}
	
	private String openNewDialog() {
		 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		 shell = Display.getCurrent().getActiveShell();
		 String dialogTitle = Messages.getString("ExportOptionWizardPageThree_Info_PleaseEnterName"); //$NON-NLS-1$
		 String dialogMessage = Messages.getString("ExportOptionWizardPageThree_Info_PleaseEnterNameDesc"); //$NON-NLS-1$
		 IInputValidator validator = new IInputValidator() {
		 	public String isValid(String newText) {
		 		if(newText=="") //$NON-NLS-1$
		 			return Messages.getString("ExportOptionWizardPageThree_InfoError_NameNotEmpty"); //$NON-NLS-1$
		 		return null;
			}  
		};
		InputDialog dialog = new InputDialog(shell, dialogTitle, dialogMessage, null, validator);
		int result = dialog.open();
		return result == Window.OK ? dialog.getValue() : null;
		
	}
}
