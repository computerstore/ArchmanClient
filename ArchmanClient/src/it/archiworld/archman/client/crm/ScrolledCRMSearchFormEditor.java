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

import it.archiworld.archman.client.core.ScrolledSearchEditorBase;
import it.archiworld.archman.client.core.util.ComboBoxCriterionBuilder;
import it.archiworld.archman.client.core.util.TextCriterionBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class ScrolledCRMSearchFormEditor extends
		ScrolledSearchEditorBase {

	public NamePart createNamePart(Object bean, String titleProperty,
			String firstNameProperty, String lastNameProperty) {
		return new NamePart(bean, titleProperty, firstNameProperty,
				lastNameProperty);
	}

	public NamePart createNamePart(Object bean, String genderProperty,
			String titleProperty, String firstNameProperty,
			String lastNameProperty) {
		return new NamePart(bean, genderProperty, titleProperty,
				firstNameProperty, lastNameProperty);
	}

	protected class NamePart extends Composite {

		private NamePart(Object bean, String titleProperty,
				String firstNameProperty, String lastNameProperty) {
			super(activePart, SWT.NONE);
			toolkit.adapt(this);
			setLayout(new GridLayout(3, false));

			Label titleLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Title);
			titleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label firstNameLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Firstname);
			firstNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
					false));
			Label lastNameLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Lastname);
			lastNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
					false));

			Composite titleComposite = toolkit.createComposite(this, SWT.NONE);
			titleComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout titleLayout = new GridLayout(3, false);
			titleComposite.setLayout(titleLayout);
			Combo titleOperatorCombo = new Combo(titleComposite, SWT.READ_ONLY);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty, TextCriterionBuilder.OPERATOR_EMPTY);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			titleOperatorCombo.select(0);
			toolkit.adapt(titleComposite);
			Text titleSearchString = toolkit.createText(titleComposite, null);
			GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			titleSearchString.setLayoutData(gd);
			Text titleLtSearchText = toolkit.createText(titleComposite, null);
			titleLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(titleOperatorCombo,
					titleSearchString, titleLtSearchText, titleProperty));

			Composite firstNameComposite = toolkit.createComposite(this,
					SWT.NONE);
			firstNameComposite.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			GridLayout firstNameLayout = new GridLayout(3, false);
			firstNameComposite.setLayout(firstNameLayout);
			Combo firstNameOperatorCombo = new Combo(firstNameComposite,
					SWT.READ_ONLY);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty,
					TextCriterionBuilder.OPERATOR_EMPTY);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			firstNameOperatorCombo.select(0);
			toolkit.adapt(firstNameComposite);
			Text firstNameSearchString = toolkit.createText(firstNameComposite,
					null);
			gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			firstNameSearchString.setLayoutData(gd);
			Text firstNameLtSearchText = toolkit.createText(firstNameComposite,
					null);
			firstNameLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(firstNameOperatorCombo,
					firstNameSearchString, firstNameLtSearchText,
					firstNameProperty));

			Composite lastNameComposite = toolkit.createComposite(this,
					SWT.NONE);
			lastNameComposite.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			GridLayout lastNameLayout = new GridLayout(3, false);
			lastNameComposite.setLayout(lastNameLayout);
			Combo lastNameOperatorCombo = new Combo(lastNameComposite,
					SWT.READ_ONLY);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty,
					TextCriterionBuilder.OPERATOR_EMPTY);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			lastNameOperatorCombo.select(0);
			toolkit.adapt(lastNameComposite);
			Text lastNameSearchString = toolkit.createText(lastNameComposite,
					null);
			gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			lastNameSearchString.setLayoutData(gd);
			Text lastNameLtSearchText = toolkit.createText(lastNameComposite,
					null);
			lastNameLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(lastNameOperatorCombo,
					lastNameSearchString, lastNameLtSearchText,
					lastNameProperty));
		}

		private NamePart(Object bean, String genderProperty,
				String titleProperty, String firstNameProperty,
				String lastNameProperty) {
			super(activePart, SWT.NONE);
			toolkit.adapt(this);
			setLayout(new GridLayout(4, false));

			Label genderLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_LabelGender);
			genderLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label titleLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Title);
			titleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END,
					false, false));
			Label firstNameLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Firstname);
			firstNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
					false));
			Label lastNameLabel = toolkit.createLabel(this,
					Messages.ScrolledCRMFormEditor_Label_Lastname);
			lastNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
					false));

			Composite genderComposite = toolkit.createComposite(this, SWT.NONE);
			genderComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout genderLayout = new GridLayout(2, false);
			genderComposite.setLayout(genderLayout);
			Combo genderOperatorCombo = new Combo(genderComposite,
					SWT.READ_ONLY);
			genderOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Whatever,
					ComboBoxCriterionBuilder.OPERATOR_INDIFFERENT);
			genderOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Equal,
					ComboBoxCriterionBuilder.OPERATOR_EQUAL);
			genderOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqual,
					ComboBoxCriterionBuilder.OPERATOR_NOT_EQUAL);
			genderOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty,
					ComboBoxCriterionBuilder.OPERATOR_EMPTY);
			genderOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					ComboBoxCriterionBuilder.OPERATOR_NOT_EMPTY);
			genderOperatorCombo.select(0);
			toolkit.adapt(genderOperatorCombo, true, true);
			Combo genderCombo = new Combo(genderComposite, SWT.READ_ONLY);
			GridData genderGd = new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false);
			genderGd.widthHint = 120;
			genderCombo.setLayoutData(genderGd);
			genderCombo.add(""); //$NON-NLS-1$
			genderCombo.add(Messages.ScrolledCRMFormEditor_ComboValueMale);
			genderCombo.add(Messages.ScrolledCRMFormEditor_ComboValueFemale);
			toolkit.adapt(genderCombo, true, true);
			addBuilder(new ComboBoxCriterionBuilder(genderOperatorCombo,
					genderCombo, new String[] { "", "male", "female" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					genderProperty));

			Composite titleComposite = toolkit.createComposite(this, SWT.NONE);
			titleComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout titleLayout = new GridLayout(3, false);
			titleComposite.setLayout(titleLayout);
			Combo titleOperatorCombo = new Combo(titleComposite, SWT.READ_ONLY);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty, TextCriterionBuilder.OPERATOR_EMPTY);
			titleOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			titleOperatorCombo.select(0);
			toolkit.adapt(titleComposite);
			Text titleSearchString = toolkit.createText(titleComposite, null);
			GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			titleSearchString.setLayoutData(gd);
			Text titleLtSearchText = toolkit.createText(titleComposite, null);
			titleLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(titleOperatorCombo,
					titleSearchString, titleLtSearchText, titleProperty));

			Composite firstNameComposite = toolkit.createComposite(this,
					SWT.NONE);
			firstNameComposite.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			GridLayout firstNameLayout = new GridLayout(3, false);
			firstNameComposite.setLayout(firstNameLayout);
			Combo firstNameOperatorCombo = new Combo(firstNameComposite,
					SWT.READ_ONLY);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty,
					TextCriterionBuilder.OPERATOR_EMPTY);
			firstNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			firstNameOperatorCombo.select(0);
			toolkit.adapt(firstNameComposite);
			Text firstNameSearchString = toolkit.createText(firstNameComposite,
					null);
			gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			firstNameSearchString.setLayoutData(gd);
			Text firstNameLtSearchText = toolkit.createText(firstNameComposite,
					null);
			firstNameLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(firstNameOperatorCombo,
					firstNameSearchString, firstNameLtSearchText,
					firstNameProperty));

			Composite lastNameComposite = toolkit.createComposite(this,
					SWT.NONE);
			lastNameComposite.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			GridLayout lastNameLayout = new GridLayout(3, false);
			lastNameComposite.setLayout(lastNameLayout);
			Combo lastNameOperatorCombo = new Combo(lastNameComposite,
					SWT.READ_ONLY);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_FromTo,
					TextCriterionBuilder.OPERATOR_FROM_TO);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_Empty,
					TextCriterionBuilder.OPERATOR_EMPTY);
			lastNameOperatorCombo.add(Messages.ScrolledCRMSearchFormEditor_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			lastNameOperatorCombo.select(0);
			toolkit.adapt(lastNameComposite);
			Text lastNameSearchString = toolkit.createText(lastNameComposite,
					null);
			gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			lastNameSearchString.setLayoutData(gd);
			Text lastNameLtSearchText = toolkit.createText(lastNameComposite,
					null);
			lastNameLtSearchText.setLayoutData(new GridData(SWT.FILL,
					SWT.BEGINNING, true, false));
			addBuilder(new TextCriterionBuilder(lastNameOperatorCombo,
					lastNameSearchString, lastNameLtSearchText,
					lastNameProperty));
		}

	}

	protected String controlFiscalCode(String cf) {
		if (cf == null || cf == "") //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		int i, s, c;
		String cf2;
		int setdisp[] = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11,
				3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
		// if( cf.length() == 0 ) return "No fiscal code set";
		if (cf.length() != 16)
			return Messages.ScrolledCRMFormEditor_FiscalCode_Wrong_Length;
		cf2 = cf.toUpperCase();
		for (i = 0; i < 16; i++) {
			c = cf2.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z'))
				return Messages.ScrolledCRMFormEditor_FiscalCode_Non_Valid_Char;
		}
		s = 0;
		for (i = 1; i <= 13; i += 2) {
			c = cf2.charAt(i);
			if (c >= '0' && c <= '9')
				s = s + c - '0';
			else
				s = s + c - 'A';
		}
		for (i = 0; i <= 14; i += 2) {
			c = cf2.charAt(i);
			if (c >= '0' && c <= '9')
				c = c - '0' + 'A';
			s = s + setdisp[c - 'A'];
		}
		if (s % 26 + 'A' != cf2.charAt(15))
			return Messages.ScrolledCRMFormEditor_FiscalCode_Incorrect;
		return ""; //$NON-NLS-1$
	}

	static String controlTaxCode(String pi) {
		if (pi == null || pi == "") //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		int i, c, s;
		// if( pi.length() == 0 ) return "Tax Code not set";
		if (pi.length() != 11)
			return Messages.ScrolledCRMFormEditor_TaxCode_Wrong_Length;
		for (i = 0; i < 11; i++) {
			if (pi.charAt(i) < '0' || pi.charAt(i) > '9')
				return Messages.ScrolledCRMFormEditor_TaxCode_Invalid_Char;
		}
		s = 0;
		for (i = 0; i <= 9; i += 2)
			s += pi.charAt(i) - '0';
		for (i = 1; i <= 9; i += 2) {
			c = 2 * (pi.charAt(i) - '0');
			if (c > 9)
				c = c - 9;
			s += c;
		}
		if ((10 - s % 10) % 10 != pi.charAt(10) - '0')
			return Messages.ScrolledCRMFormEditor_TaxCode_Invalid;
		return ""; //$NON-NLS-1$
	}
}
