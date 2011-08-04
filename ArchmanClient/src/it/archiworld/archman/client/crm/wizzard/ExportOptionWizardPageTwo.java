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


import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ExportOptionWizardPageTwo extends WizardPage {
	
	private Composite container;
	protected FormToolkit toolkit;
	protected DataBindingContext dataBindingContext = new DataBindingContext();
	protected Map<String, Button> buttons=new HashMap<String, Button>();
	
	protected ExportOptionWizardPageTwo(String pageName) {
		super(pageName);
		toolkit = new FormToolkit(Display.getCurrent());
		toolkit.setBackground(new Color(Display.getDefault(),222,222,222)); //TODO Set right grey level
	}
		 
	
	@Override
	public void createControl(Composite parent) {
		WizardPageSupport.create(this, dataBindingContext);

		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(3,false));
		container.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true, true, 1, 1));
		
		Composite left = new Composite(container, SWT.NULL);
		left.setLayout(new GridLayout(2,false));
		left.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true, true, 1, 1));

		Composite middle = new Composite(container, SWT.NULL);
		middle.setLayout(new GridLayout(2,false));
		middle.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true, true, 1, 1));
		
		Composite right = new Composite(container, SWT.NULL);
		right.setLayout(new GridLayout(2,false));	
		right.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true, true, 1, 1));
		
		Set<Map.Entry<String, String>> entrySet = ((ExportOptionWizard)getWizard()).getFields().entrySet();
		Set<Map.Entry<String, String>> sortedSet = new TreeSet<Map.Entry<String, String>>(new Comparator<Map.Entry<String, String>>(){

			@Override
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getValue(), o2.getValue());
			}
			
		});
		Iterator<Entry<String, String>> iter = entrySet.iterator();
		while(iter.hasNext())
			sortedSet.add(iter.next());
			
		iter = sortedSet.iterator();
		
		int i=0;
		while (iter.hasNext()){
			Map.Entry<String, String> entry =iter.next();
			if(i%3==0)
				createLabeledCheckbox(left, entry.getValue(), entry.getKey());
			else if(i%3==1)
				createLabeledCheckbox(middle, entry.getValue(), entry.getKey());
			else
				createLabeledCheckbox(right, entry.getValue(), entry.getKey());
			i++;
		}
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(true);

	}

	
	@Override
	public IWizardPage getNextPage() {
		ExportOptionWizardPageThree pagethree=(ExportOptionWizardPageThree) super.getNextPage();
		pagethree.populateViewer();
		return pagethree;
	}

	@Override
	public Control getControl() {
		return container;
	}

	protected void createLabeledCheckbox(Composite activeSection, String label,	String property) {

		if (activeSection != null) {
			toolkit.createLabel(activeSection, label);
			
			Button checkbox = toolkit.createButton(activeSection, null,
					SWT.CHECK);
			checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					true, false));
			buttons.put(property,checkbox);
		}
	}
	
	protected void populateViewer(){
		System.out.println("Populating Viewer page Two"); //$NON-NLS-1$
		Iterator<String> iter = buttons.keySet().iterator();
		Button checkbox;
		String property=""; //$NON-NLS-1$
		while(iter.hasNext()){
			property = iter.next();
			checkbox = buttons.get(property);
			System.out.println(property);
			dataBindingContext
			.bindValue(SWTObservables.observeSelection(checkbox),
					PojoObservables.observeValue(Realm.getDefault(), ((ExportOptionWizard)getWizard()).getEntity(), property),
					new UpdateValueStrategy().setAfterConvertValidator(new IValidator(){

						@Override
						public IStatus validate(Object value) {
							return ValidationStatus.ok();
						}
						
					}), new UpdateValueStrategy().setAfterConvertValidator(new IValidator(){
						
						@Override
						public IStatus validate(Object value) {
							return ValidationStatus.ok();
						}
						
					}));
		}
	}
}
