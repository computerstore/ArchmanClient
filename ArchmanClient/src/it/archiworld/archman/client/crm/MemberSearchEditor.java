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

import it.archiworld.archman.client.core.util.DateCriterionBuilder;
import it.archiworld.common.Member;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.hibernate.criterion.Criterion;

public class MemberSearchEditor extends ScrolledCRMSearchFormEditor implements
		SearchEditor {

	public static String ID = "it.archiworld.archman.client.crm.membersearcheditor"; //$NON-NLS-1$

	private Member member;

	@Override
	public void doSearch(IProgressMonitor monitor) {
		System.out.println("Search Member"); //$NON-NLS-1$
		IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
		AddressTreeView view = (AddressTreeView) window.getActivePage()
				.findView(AddressTreeView.ID);
		if (view != null) {
			System.out.println(view.getTitle());
			Criterion criterion = this.buildCriterion();
			view.updateList(Member.class, criterion, this.orders);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		form.setText(Messages.MemberEditor_Form_Heading);
		header.setLayout(new FillLayout());
		createNamePart(member,
				"gender", "title", "firstname", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"lastname"); //$NON-NLS-1$
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.MemberEditor_Tab_Label_General, new GridLayout(
				2, true));

		createSection(Messages.MemberEditor_Section_Heading_Personal,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledInteger(Messages.MemberEditor_Label_RegistrationNumber,
				member, "registration_number"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_Nationality, member,
				"nationality"); //$NON-NLS-1$
		createLabeledComboBox(
				Messages.MemberEditor_Label_Profession,
				new String[] {
						"", Messages.MemberEditor_Profession_Label_MaleFreelancer, Messages.MemberEditor_Profession_Label_FemaleFreelancer, //$NON-NLS-1$
						Messages.MemberEditor_Profession_Label_SelfEmployment,
						Messages.MemberEditor_Profession_Label_Teacher,
						Messages.MemberEditor_Profession_Label_Employee,
						Messages.MemberEditor_Profession_Label_Associate,
						Messages.MemberEditor_Profession_Label_Other },
				new String[] { "", "male freelancer",  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
						"female freelancer", "self-employment", "teacher", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"employee", "associate", "other" }, member, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"profession"); //$NON-NLS-1$
		createDateField(Messages.MemberEditor_Label_Birthdate, CDT.DROP_DOWN
				| CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member, "birthdate"); //$NON-NLS-1$
		//$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianBirthplace,
				member, "birthplace"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanBirthplace, member,
				"birthplace_de"); //$NON-NLS-1$
		createDateField(Messages.MemberEditor_Label_LastChange, CDT.DROP_DOWN
				| CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member, "last_change_date"); //$NON-NLS-1$
		//$NON-NLS-1$

		createSection(Messages.MemberEditor_Section_Heading_Contact,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Phone,
				member, "phone", "directory_phone"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_HomePhone,
				member, "homephone", "directory_homephone"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Mobile,
				member, "mobilephone", "directory_mobile"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Fax, member,
				"fax", "directory_fax"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_HomeFax,
				member, "homefax", "directory_homefax"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Email,
				member, "email", "directory_email"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Website,
				member, "website", "directory_website"); //$NON-NLS-1$ //$NON-NLS-2$
		//$NON-NLS-1$
		createLabeledCheckbox(Messages.MemberEditor_Label_Newsletter, member,
				"newsletter"); //$NON-NLS-1$
		createLabeledCheckbox(Messages.MemberEditor_LabelInformationNewsletter,
				member, "culturenewsletter"); //$NON-NLS-1$

		createSection(Messages.MemberEditor_Section_Heading_Graduation,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);
		createDateField(Messages.MemberEditor_Label_GraduationDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member,
				"graduation_date"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GraduationInstitute,
				member, "graduation_institute"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_StateExam, member,
				"state_exam_year"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_Habilitation, member,
				"habilitation"); //$NON-NLS-1$

		createSection(Messages.MemberEditor_Section_Heading_Finances,
				Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_FiscalCode, member,
				"fiscal_code"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_TaxCode, member,
				"tax_code"); //$NON-NLS-1$

		createSection(Messages.MemberEditor_Section_Heading_Registration,
				Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		createDateField(Messages.MemberEditor_Label_RegistrationDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member,
				"registration_date"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_RegistrationExemption,
				member, "registration_exemption"); //$NON-NLS-1$
		createLabeledComboBox(Messages.MemberEditor_Label_Sector, new String[] {
				"", Messages.MemberEditor_ComboItemArchitect, //$NON-NLS-1$
				Messages.MemberEditor_ComboItemUrbanPlaner,
				Messages.MemberEditor_ComboItemLandscapeDesigner,
				Messages.MemberEditor_ComboItemMonumentConservator,
				Messages.MemberEditor_ComboItemIuniorArchitect,
				Messages.MemberEditor_ComboItemIuniorUrbanPlaner },
				new String[] { "", "[A]architect", "[A]urbanplaner", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"[A]landscapedesigner", "[A]monumentconservator", //$NON-NLS-1$ //$NON-NLS-2$
						"[B]iuniorarchitect", "[B]iuniorurbanplaner" }, member, //$NON-NLS-1$ //$NON-NLS-2$
				"sector"); //$NON-NLS-1$
		createDateField(Messages.MemberEditor_LabelSectorDate, CDT.DROP_DOWN
				| CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member, "sector_date"); //$NON-NLS-1$
		//$NON-NLS-1$

		createLabeledText(Messages.MemberEditor_LabelSectorHabilitation,
				member, "sector_habilitation"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledText(Messages.MemberEditor_SectorExemption, member,
				"sector_exemption"); //$NON-NLS-1$ //$NON-NLS-2$

		createSection(Messages.MemberEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.MemberEditor_Label_Note, member, "note"); //$NON-NLS-1$

		createTabItem(Messages.MemberEditor_Tab_Label_Address, new GridLayout(
				2, true));

		/* Italian Home Address */
		Section ithome = createSection(Messages.MemberEditor_Section_Heading_ItalianHomeAddress,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_ItalianHomeStreet,
				member, "homestreet"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianHomeZip, member,
				"homezip"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianHomeCity, member,
				"homecity"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianHomeState, member,
				"homestate"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianHomeCountry,
				member, "homecountry"); //$NON-NLS-1$

		/* Italian Business Address */
		Section itbus = createSection(
				Messages.MemberEditor_Section_Heading_ItalianBusinessAddress,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_ItalianBusinessStreet,
				member, "street"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianBusinessZip,
				member, "zip"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianBusinessCity,
				member, "city"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianBusinessState,
				member, "state"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_ItalianBusinessCountry,
				member, "country"); //$NON-NLS-1$

		// /* German Home Address */
		Section dehome = createSection(Messages.MemberEditor_Section_Heading_GermanHomeAddress,
				Section.TWISTIE | Section.TITLE_BAR
						| Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_GermanHomeStreet, member,
				"homestreet_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeCity, member,
				"homecity_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeState, member,
				"homestate_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeCountry,
				member, "homecountry_de"); //$NON-NLS-1$

		// /* German Business Address */
		Section debus = createSection(
				Messages.MemberEditor_Section_Heading_GermanBusinessAddress,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_GermanBusinessStreet,
				member, "street_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanBusinessCity,
				member, "city_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanBusinessState,
				member, "state_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanBusinessCountry,
				member, "country_de"); //$NON-NLS-1$
		
		Section imp = createSection(Messages.MemberEditor_Section_Heading_StandardAddress,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 2, 1, true, false);
		createLabeledComboBox(
				Messages.MemberEditor_Label_StandardAddress,
				new String[] {
						"", //$NON-NLS-1$
						Messages.MemberEditor_DropDownValue_StandardAddress_ItalianBusinessAddress,
						Messages.MemberEditor_DropDownValue_StandardAddress_ItalianHomeAddress,
						Messages.MemberEditor_DropDownValue_StandardAddress_GermanBusinessAddress,
						Messages.MemberEditor_DropDownValue_StandardAddress_GermanHomeAddress },
				new String[] { "", "address", "homeaddress", "address_de", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
						"homeaddress_de" }, member, "stdaddress"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledComboBox(Messages.MemberEditor_Label_RegisterAddress,
				new String[] { "", Messages.MemberEditor_German, //$NON-NLS-1$
						Messages.MemberEditor_Italian }, new String[] { "", //$NON-NLS-1$
						"german", "italian" }, member, "register_address"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		createLabeledCheckbox(Messages.MemberEditor_LabelOfficeAddress, member,
				"directory_officeaddress"); //$NON-NLS-1$

		Composite par = itbus.getParent();
		par.setTabList(new Control[]{ithome,dehome,itbus,debus,imp});

		createTabItem(Messages.MemberEditor_Tab_Label_Registration,
				new GridLayout(2, true));

		createSection(
				Messages.MemberEditor_Label_Section_Heading_Deregistration,
				Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		
		Label labelControl = toolkit.createLabel(this.activeSection, Messages.MemberEditor_Label_DeregistrationDate);
		labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		Composite searchComposite = toolkit.createComposite(
				this.activeSection, SWT.NONE);
		searchComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		GridLayout searchLayout = new GridLayout(3, false);
		searchComposite.setLayout(searchLayout);
		Combo operatorCombo = new Combo(searchComposite, SWT.READ_ONLY);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_EqualOr,
				DateCriterionBuilder.OPERATOR_EQUAL_OR);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_FromTo, DateCriterionBuilder.OPERATOR_FROM_TO);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_NotEqualAnd,
				DateCriterionBuilder.OPERATOR_NOT_AND);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_Empty, DateCriterionBuilder.OPERATOR_EMPTY);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_NotEmpty,
				DateCriterionBuilder.OPERATOR_NOT_EMPTY);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_Min, DateCriterionBuilder.OPERATOR_MIN);
		operatorCombo.add(Messages.MemberSearchEditor_ComboOption_Max, DateCriterionBuilder.OPERATOR_MAX);
		operatorCombo.select(3);
		toolkit.adapt(searchComposite);
		CDateTime dateTime = new CDateTime(searchComposite, CDT.BORDER
				| CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM);
		toolkit.adapt(dateTime);
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gd.verticalIndent = 0;
		gd.horizontalIndent = 0;
		dateTime.setLayoutData(gd);
		CDateTime dateTime2 = new CDateTime(searchComposite, CDT.BORDER
				| CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM);
		toolkit.adapt(dateTime2);
		dateTime2.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
				false));
		this.addBuilder(new DateCriterionBuilder(operatorCombo, dateTime,
				dateTime2, "deregister_date")); //$NON-NLS-1$
		
		createLabeledText(Messages.MemberEditor_Label_DeregistrationExeption,
				member, "deregister_exemption"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_DeregistrationReason,
				member, "deregister_reason"); //$NON-NLS-1$
	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

}
