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
package it.archiworld.archman.client.core;

import it.archiworld.archman.client.core.util.CheckboxCriterionBuilder;
import it.archiworld.archman.client.core.util.ComboBoxCriterionBuilder;
import it.archiworld.archman.client.core.util.CriteriaBuilder;
import it.archiworld.archman.client.core.util.DateCriterionBuilder;
import it.archiworld.archman.client.core.util.IntegerCriterionBuilder;
import it.archiworld.archman.client.core.util.TextCriterionBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public abstract class ScrolledSearchEditorBase extends ScrolledFormEditorBase {

	protected List<CriteriaBuilder> builders = new ArrayList<CriteriaBuilder>();

	protected List<Order> orders = new ArrayList<Order>();

	@Override
	protected void createLabeledText(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			createLabeledTextSearch(label, entity, property, this.activeSection);
		}
	}

	protected Composite createLabeledTextSearch(String label, Object entity,
			String property, Composite target) {
		Label labelControl = toolkit.createLabel(target, label);
		labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		Composite searchComposite = toolkit.createComposite(target, SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		GridLayout searchLayout = new GridLayout(3, false);
		searchComposite.setLayout(searchLayout);
		Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
		operatorCombo
				.add(Messages.ScrolledSearchEditorBase_ComboOption_EqualOr, TextCriterionBuilder.OPERATOR_EQUAL_OR);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_FromTo, TextCriterionBuilder.OPERATOR_FROM_TO);
		operatorCombo
				.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEqualAnd, TextCriterionBuilder.OPERATOR_NOT_AND);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Empty, TextCriterionBuilder.OPERATOR_EMPTY);
		operatorCombo
				.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEmpty, TextCriterionBuilder.OPERATOR_NOT_EMPTY);

		operatorCombo.select(0);
		toolkit.adapt(searchComposite);
		Text searchString = toolkit.createText(searchComposite, null);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.verticalIndent = 0;
		gd.horizontalIndent = 0;
		searchString.setLayoutData(gd);
		Text ltText = toolkit.createText(searchComposite, null);
		ltText
				.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
						false));
		this.addBuilder(new TextCriterionBuilder(operatorCombo, searchString,
				ltText, property));
		return searchComposite;
	}

	protected void createLabeledInteger(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			createLabeledIntegerSearch(label, entity, property,
					this.activeSection);
		}
	}

	protected Composite createLabeledIntegerSearch(String label, Object entity,
			String property, Composite target) {
		Label labelControl = toolkit.createLabel(target, label);
		labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		Composite searchComposite = toolkit.createComposite(target, SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		GridLayout searchLayout = new GridLayout(3, false);
		searchComposite.setLayout(searchLayout);
		Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_EqualOr,
				IntegerCriterionBuilder.OPERATOR_EQUAL_OR);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_FromTo, IntegerCriterionBuilder.OPERATOR_FROM_TO);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEqualAnd,
				IntegerCriterionBuilder.OPERATOR_NOT_AND);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Empty, IntegerCriterionBuilder.OPERATOR_EMPTY);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEmpty,
				IntegerCriterionBuilder.OPERATOR_NOT_EMPTY);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Min, IntegerCriterionBuilder.OPERATOR_MIN);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Max, IntegerCriterionBuilder.OPERATOR_MAX);
		operatorCombo.select(0);
		toolkit.adapt(searchComposite);
		Text searchString = toolkit.createText(searchComposite, null);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.verticalIndent = 0;
		gd.horizontalIndent = 0;
		searchString.setLayoutData(gd);
		Text ltText = toolkit.createText(searchComposite, null);
		ltText
				.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
						false));
		this.addBuilder(new IntegerCriterionBuilder(operatorCombo,
				searchString, ltText, property));
		return searchComposite;
	}

	@Override
	protected void createLabeledTextWithDirectory(String label, Object entity,
			String property, String directory_property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			Composite searchComposite = toolkit.createComposite(
					this.activeSection, SWT.NONE);
			searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout searchLayout = new GridLayout(4, false);
			searchComposite.setLayout(searchLayout);
			Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_EqualOr,
					TextCriterionBuilder.OPERATOR_EQUAL_OR);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_FromTo, TextCriterionBuilder.OPERATOR_FROM_TO);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEqualAnd,
					TextCriterionBuilder.OPERATOR_NOT_AND);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Empty, TextCriterionBuilder.OPERATOR_EMPTY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEmpty,
					TextCriterionBuilder.OPERATOR_NOT_EMPTY);
			operatorCombo.select(0);
			toolkit.adapt(searchComposite);
			Text searchString = toolkit.createText(searchComposite, null);
			GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			searchString.setLayoutData(gd);
			Text ltText = toolkit.createText(searchComposite, null);
			ltText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
			this.addBuilder(new TextCriterionBuilder(operatorCombo,
					searchString, ltText, property));
			Combo operator2Combo = new Combo(searchComposite, SWT.READ_ONLY);
			operator2Combo
					.add(Messages.ScrolledSearchEditorBase_ComboOption_Whatever, CheckboxCriterionBuilder.OPERATOR_IGNORE);
			operator2Combo.add(Messages.ScrolledSearchEditorBase_ComboOption_Activated,
					CheckboxCriterionBuilder.OPERATOR_IS_TRUE);
			operator2Combo.add(Messages.ScrolledSearchEditorBase_ComboOption_Deactivated,
					CheckboxCriterionBuilder.OPERATOR_IS_FALSE);
			operator2Combo.select(0);
			toolkit.adapt(searchComposite);
			this.addBuilder(new CheckboxCriterionBuilder(operator2Combo,
					directory_property));
		}
	}

	@Override
	protected void createLabeledMultiText(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			createLabeledTextSearch(label, entity, property, this.activeSection);
		}
	}

	@Override
	protected void createDateField(String label, int style, Object entity,
			String property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			Composite searchComposite = toolkit.createComposite(
					this.activeSection, SWT.NONE);
			searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout searchLayout = new GridLayout(3, false);
			searchComposite.setLayout(searchLayout);
			Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_EqualOr,
					DateCriterionBuilder.OPERATOR_EQUAL_OR);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_FromTo, DateCriterionBuilder.OPERATOR_FROM_TO);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEqualAnd,
					DateCriterionBuilder.OPERATOR_NOT_AND);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Empty, DateCriterionBuilder.OPERATOR_EMPTY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEmpty,
					DateCriterionBuilder.OPERATOR_NOT_EMPTY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Min, DateCriterionBuilder.OPERATOR_MIN);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Max, DateCriterionBuilder.OPERATOR_MAX);
			operatorCombo.select(0);
			toolkit.adapt(searchComposite);
			CDateTime dateTime = new CDateTime(searchComposite, CDT.BORDER
					| style);
			toolkit.adapt(dateTime);
			GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gd.verticalIndent = 0;
			gd.horizontalIndent = 0;
			dateTime.setLayoutData(gd);
			CDateTime dateTime2 = new CDateTime(searchComposite, CDT.BORDER
					| style);
			toolkit.adapt(dateTime2);
			dateTime2.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
			this.addBuilder(new DateCriterionBuilder(operatorCombo, dateTime,
					dateTime2, property));
		}
	}

	@Override
	protected void createLabeledCheckbox(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			Composite target = this.activeSection;
			Label labelControl = toolkit.createLabel(target, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			Composite searchComposite = toolkit.createComposite(target,
					SWT.NONE);
			searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			GridLayout searchLayout = new GridLayout(2, false);
			searchComposite.setLayout(searchLayout);
			Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Whatever, CheckboxCriterionBuilder.OPERATOR_IGNORE);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Activated,
					CheckboxCriterionBuilder.OPERATOR_IS_TRUE);
			operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Deactivated,
					CheckboxCriterionBuilder.OPERATOR_IS_FALSE);
			operatorCombo.select(0);
			toolkit.adapt(searchComposite);
			this.addBuilder(new CheckboxCriterionBuilder(operatorCombo,
					property));
		}
	}

	@Override
	protected void createLabeledComboBox(String label, String[] labels,
			String[] keys, Object entity, String property, Composite target) {
		Label labelControl = toolkit.createLabel(target, label);
		labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		Composite searchComposite = toolkit.createComposite(this.activeSection,
				SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		GridLayout searchLayout = new GridLayout(2, false);
		searchComposite.setLayout(searchLayout);
		Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
		operatorCombo
				.add(Messages.ScrolledSearchEditorBase_ComboOption_Whatever, ComboBoxCriterionBuilder.OPERATOR_INDIFFERENT);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Equal, ComboBoxCriterionBuilder.OPERATOR_EQUAL);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEqual,
				ComboBoxCriterionBuilder.OPERATOR_NOT_EQUAL);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_Empty, ComboBoxCriterionBuilder.OPERATOR_EMPTY);
		operatorCombo.add(Messages.ScrolledSearchEditorBase_ComboOption_NotEmpty,
				ComboBoxCriterionBuilder.OPERATOR_NOT_EMPTY);
		operatorCombo.select(0);
		Combo inputCombo = new Combo(searchComposite, SWT.BORDER
				| SWT.READ_ONLY);
		for (int i = 0; i < labels.length; i++) {
			inputCombo.add(labels[i], i);
		}
		toolkit.adapt(inputCombo, true, true);
		inputCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.addBuilder(new ComboBoxCriterionBuilder(operatorCombo, inputCombo,
				keys, property));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	protected void addBuilder(CriteriaBuilder builder) {
		this.builders.add(builder);
	}

	protected Criterion buildCriterion() {
		Conjunction conjunction = Restrictions.conjunction();
		this.orders.clear();
		Iterator<CriteriaBuilder> critIt = this.builders.iterator();
		while (critIt.hasNext() == true) {
			CriteriaBuilder builder = critIt.next();
			if (builder.getOrder() != null) {
				this.orders.add(builder.getOrder());
			}
			builder.updateConjunction(conjunction);
		}
		return conjunction;
	}

}
