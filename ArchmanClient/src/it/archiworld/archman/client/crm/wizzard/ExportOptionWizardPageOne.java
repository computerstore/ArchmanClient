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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ExportOptionWizardPageOne extends WizardPage {
	
	private Composite container;
	protected FormToolkit toolkit;
	protected DataBindingContext dataBindingContext;
//	protected MemberFieldExportSelector entity;
	protected List<MemberFieldExportSelector> mfeslist;
	CCombo savedCombo;
	
	protected ExportOptionWizardPageOne(String pageName) {
		super(pageName);
		toolkit = new FormToolkit(Display.getCurrent());
		toolkit.setBackground(new Color(Display.getDefault(),222,222,222)); //TODO Set right grey level
	}

	@Override
	public IWizardPage getNextPage() {
		ExportOptionWizardPageTwo pagetwo=(ExportOptionWizardPageTwo) super.getNextPage();			
		pagetwo.populateViewer();
		return pagetwo;
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		try {
			mfeslist = ((ExportOptionWizardManager)ConnectionFactory.getConnection("ExportOptionWizardManagerBean")).getMemberFieldExportSelectorList(); //$NON-NLS-1$
			Iterator<MemberFieldExportSelector> mfesiter = mfeslist.iterator();
			savedCombo = new CCombo(container, SWT.BORDER | SWT.READ_ONLY);
			savedCombo.add(""); //$NON-NLS-1$
			while(mfesiter.hasNext())
				savedCombo.add(mfesiter.next().getName());

			toolkit.adapt(savedCombo, true, true);
			savedCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			Button loadButton = toolkit.createButton(container, Messages.getString("ExportOptionWizardPageOne_Button_Load"), SWT.FLAT); //$NON-NLS-1$
			loadButton.addSelectionListener(new SelectionListener(){

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					((ExportOptionWizard)getWizard()).setEntity(mfeslist.get(savedCombo.getSelectionIndex()-1));
				}
				
			});
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);
		
	}

	// You need to overwrite this method otherwise you receive a
	// AssertionFailedException
	// This method should always return the top widget of the application
	@Override
	public Control getControl() {
		return container;
	}

}
