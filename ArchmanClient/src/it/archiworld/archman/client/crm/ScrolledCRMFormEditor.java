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
package it.archiworld.archman.client.crm;

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.util.ComboBoxEnumSWTObservable;
import it.archiworld.archman.client.core.util.ComboKeyListener;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class ScrolledCRMFormEditor extends ScrolledFormEditorBase {

	public NamePart createNamePart(Object bean, String titleProperty,
			String firstNameProperty, String lastNameProperty) {
		return new NamePart(bean, titleProperty, firstNameProperty,
				lastNameProperty);
	}	

	public NamePart createNamePart(Object bean, String genderProperty, String titleProperty,
			String firstNameProperty, String lastNameProperty) {
		return new NamePart(bean, genderProperty, titleProperty, firstNameProperty,
				lastNameProperty);
	}	
	
	protected class NamePart extends Composite {

		private Text titleText;

		private Text firstNameText;

		private Text lastNameText;
		
		private NamePart(Object bean, String titleProperty,
				String firstNameProperty, String lastNameProperty) {
			super(activePart, SWT.NONE);
			toolkit.adapt(this);
			setLayout(new GridLayout(3, false));
			
			Label titleLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Title);
			titleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label firstNameLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Firstname);
			firstNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END,
					true, false));
			Label lastNameLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Lastname);
			lastNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END,
					true, false));

			titleText = toolkit.createText(this, null, SWT.BORDER);
			GridData titleGd = new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false);
			titleGd.widthHint = 100;
			titleText.setLayoutData(titleGd);
			dataBindingContext.bindValue(
					SWTObservables.observeText(titleText, SWT.Modify),
					PojoObservables.observeValue(bean, titleProperty), null,
					null).getTarget().addChangeListener(dirtyListener);

			firstNameText = toolkit.createText(this, null, SWT.BORDER);
			firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext.bindValue(
					SWTObservables.observeText(firstNameText, SWT.Modify),
					PojoObservables.observeValue(bean, firstNameProperty),
					null, null).getTarget().addChangeListener(dirtyListener);

			lastNameText = toolkit.createText(this, null, SWT.BORDER);
			lastNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext.bindValue(
					SWTObservables.observeText(lastNameText, SWT.Modify),
					PojoObservables.observeValue(bean, lastNameProperty),
					null, null).getTarget().addChangeListener(dirtyListener);
		}

		private NamePart(Object bean, String genderProperty, String titleProperty,
				String firstNameProperty, String lastNameProperty) {
			super(activePart, SWT.NONE);
			toolkit.adapt(this);
			setLayout(new GridLayout(4, false));
			
			Label genderLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_LabelGender);
			genderLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label titleLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Title);
			titleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label firstNameLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Firstname);
			firstNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END,
					true, false));
			Label lastNameLabel = toolkit.createLabel(this, Messages.ScrolledCRMFormEditor_Label_Lastname);
			lastNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END,
					true, false));

			CCombo genderCombo = new CCombo(this, SWT.BORDER
					| SWT.READ_ONLY);
			toolkit.adapt(genderCombo, true, true);
			GridData genderGd = new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false);
			genderGd.widthHint = 120;
			genderCombo.setLayoutData(genderGd);
			String[] items = new String[]{"",Messages.ScrolledCRMFormEditor_ComboValueMale,Messages.ScrolledCRMFormEditor_ComboValueFemale};//$NON-NLS-1$
			String[] keys = new String[] {"", "male", "female"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			genderCombo.addKeyListener(new ComboKeyListener(genderCombo,items, keys));
			dataBindingContext
			.bindValue(
					new ComboBoxEnumSWTObservable(genderCombo, keys), 
							PojoObservables 
							.observeValue(bean, "gender")).getTarget().addChangeListener(dirtyListener); //$NON-NLS-1$
			
			titleText = toolkit.createText(this, null, SWT.BORDER);
			GridData titleGd = new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false);
			titleGd.widthHint = 100;
			titleText.setLayoutData(titleGd);
			dataBindingContext.bindValue(
					SWTObservables.observeText(titleText, SWT.Modify),
					PojoObservables.observeValue(bean, titleProperty), null,
					null).getTarget().addChangeListener(dirtyListener);

			firstNameText = toolkit.createText(this, null, SWT.BORDER);
			firstNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext.bindValue(
					SWTObservables.observeText(firstNameText, SWT.Modify),
					PojoObservables.observeValue(bean, firstNameProperty),
					null, null).getTarget().addChangeListener(dirtyListener);

			lastNameText = toolkit.createText(this, null, SWT.BORDER);
			lastNameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext.bindValue(
					SWTObservables.observeText(lastNameText, SWT.Modify),
					PojoObservables.observeValue(bean, lastNameProperty),
					null, null).getTarget().addChangeListener(dirtyListener);
		}

		public final Text getTitleText() {
			return titleText;
		}

		public final Text getFirstNameText() {
			return firstNameText;
		}

		public final Text getLastNameText() {
			return lastNameText;
		}

	}

	protected String controlFiscalCode(String cf){
		if(cf==null||cf.equals("")) //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		cf=cf.trim();
		int i, s, c;
		String cf2;
		int setdisp[] = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20,
				11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
		if( cf.length() == 0 ) return ""; //$NON-NLS-1$
		if( cf.length() != 16 )
			return Messages.ScrolledCRMFormEditor_FiscalCode_Wrong_Length;
		cf2 = cf.toUpperCase();
		for( i=0; i<16; i++ ){
			c = cf2.charAt(i);
			if( ! ( c>='0' && c<='9' || c>='A' && c<='Z' ) )
				return Messages.ScrolledCRMFormEditor_FiscalCode_Non_Valid_Char;
		}
		s = 0;
		for( i=1; i<=13; i+=2 ){
			c = cf2.charAt(i);
			if( c>='0' && c<='9' )
				s = s + c - '0';
			else
				s = s + c - 'A';
		}
		for( i=0; i<=14; i+=2 ){
			c = cf2.charAt(i);
			if( c>='0' && c<='9' )	 c = c - '0' + 'A';
			s = s + setdisp[c - 'A'];
		}
		if( s%26 + 'A' != cf2.charAt(15) )
			return Messages.ScrolledCRMFormEditor_FiscalCode_Incorrect;
		return ""; //$NON-NLS-1$
	}
	
	static String controlTaxCode(String pi)
	{
		if(pi==null||pi.equals("")) //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		pi=pi.trim();
		int i, c, s;
		if( pi.length() == 0 )  return ""; //$NON-NLS-1$
		if( pi.length() != 11 )
			return Messages.ScrolledCRMFormEditor_TaxCode_Wrong_Length;
		for( i=0; i<11; i++ ){
			if( pi.charAt(i) < '0' || pi.charAt(i) > '9' )
				return Messages.ScrolledCRMFormEditor_TaxCode_Invalid_Char;
		}
		s = 0;
		for( i=0; i<=9; i+=2 )
			s += pi.charAt(i) - '0';
		for( i=1; i<=9; i+=2 ){
			c = 2*( pi.charAt(i) - '0' );
			if( c > 9 )  c = c - 9;
			s += c;
		}
		if( ( 10 - s%10 )%10 != pi.charAt(10) - '0' )
			return Messages.ScrolledCRMFormEditor_TaxCode_Invalid;
		return ""; //$NON-NLS-1$
	}
}
