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

import it.archiworld.archman.client.committee.CommitteeEditor;
import it.archiworld.archman.client.committee.CommitteeEditorInput;
import it.archiworld.archman.client.core.util.DateConverter;
import it.archiworld.common.Member;
import it.archiworld.common.committee.Committeemember;

import java.text.DateFormat;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.internal.databinding.swt.SWTVetoableValueDecorator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
//import org.metawidget.swt.SwtMetawidget;

public class MemberEditor extends ScrolledCRMFormEditor {

	public static String ID = "it.archiworld.archman.client.crm.membereditor"; //$NON-NLS-1$
	
	private Member member;

	private RenewalTable renewalTable;

	private FormationTable formationTable;

	private SpecializationTable specializationTable;

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			String error;
			if ((error = controlFiscalCode(member.getFiscal_code())) != "") { //$NON-NLS-1$
				MessageDialog.openError(this.getSite().getShell(),
						Messages.PersonEditor_SaveFailureDialog_Heading, error);
				return;
			}
			if ((error = controlTaxCode(member.getTax_code())) != "") { //$NON-NLS-1$
				MessageDialog.openError(this.getSite().getShell(),
						Messages.PersonEditor_SaveFailureDialog_Heading, error);
				return;
			}
			if (!this.authenticate()) {
				return;
			}
			MemberEditorInput input = (MemberEditorInput) getEditorInput();
			member=(Member)input.createOrUpdate();

			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.MemberEditor_SaveSuccessDialog_Heading,
					Messages.MemberEditor_SaveSuccessDialog_Text);
			setPartName(input.getName());
			setDirty(false);
			getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(
					this, false);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.MemberEditor_SaveFailureDialog_Heading,
					Messages.MemberEditor_SaveFailureDialog_Text);
		}

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.member = (Member) ((MemberEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		String name=member.toString();
		if (member.getDeregister_date()!=null){
			name=name.concat(" "+Messages.MemberEditor_Header_Deleted_Marker); //$NON-NLS-1$
//			form.setBackground(new Color(header.getDisplay(),new RGB(255,125,125)));
			form.setForeground(new Color(header.getDisplay(),new RGB(200,0,0)));
		}
		form.setText(Messages.MemberEditor_Form_Heading + " " //$NON-NLS-1$
				+ name);
		header.setLayout(new FillLayout());
		NamePart namePart = createNamePart(member,
				"gender", "title", "firstname", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"lastname"); //$NON-NLS-1$
		namePart.getTitleText().forceFocus();
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.MemberEditor_Tab_Label_General, new GridLayout(
				2, true));

		createSection(Messages.MemberEditor_Section_Heading_Personal,
				Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

		createLabeledText(Messages.MemberEditor_Label_Nationality, member,
				"nationality"); //$NON-NLS-1$
		System.out.println(member.getProfession());
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
		createLabeledText(Messages.MemberEditor_Label_ItalianBirthplace,
				member, "birthplace"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanBirthplace, member,
				"birthplace_de"); //$NON-NLS-1$
		createLabeledReadOnlyTimestamp(Messages.MemberEditor_Label_LastChange,
				DateConverter.TIMESTAMP, DateFormat.LONG, DateFormat.SHORT,
				member, "last_change_date"); //$NON-NLS-1$
		createSection(Messages.MemberEditor_Section_Heading_Contact,
				Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

		createLabeledTextWithDirectory(Messages.MemberEditor_Label_HomePhone,
				member, "homephone", "directory_homephone"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_HomeFax,
				member, "homefax", "directory_homefax"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Phone,
				member, "phone", "directory_phone"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Fax, member,
				"fax", "directory_fax"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Mobile,
				member, "mobilephone", "directory_mobile"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Email,
				member, "email", "directory_email"); //$NON-NLS-1$ //$NON-NLS-2$
		createLabeledTextWithDirectory(Messages.MemberEditor_Label_Website,
				member, "website", "directory_website"); //$NON-NLS-1$ //$NON-NLS-2$
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
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
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

		createLabeledText(Messages.MemberEditor_LabelSectorHabilitation,
				member, "sector_habilitation"); //$NON-NLS-2$
		createLabeledText(Messages.MemberEditor_SectorExemption, member,
				"sector_exemption"); //$NON-NLS-2$
		createLabeledReadOnly(Messages.MemberEditor_Label_RegistrationNumber,
				null, member, "registration_number"); //$NON-NLS-1$

		createSection(Messages.MemberEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED, 1, 1, true, true);
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

		/* German Home Address */
		Section dehome = createSection(Messages.MemberEditor_Section_Heading_GermanHomeAddress,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
		createLabeledText(Messages.MemberEditor_Label_GermanHomeStreet, member,
				"homestreet_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeCity, member,
				"homecity_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeState, member,
				"homestate_de"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_GermanHomeCountry,
				member, "homecountry_de"); //$NON-NLS-1$
		
		/* German Business Address */
		Section debus= createSection(
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

		
		createTabItem(Messages.MemberEditor_Tab_Label_Renewal, new GridLayout(
				1, true));
		renewalTable = new RenewalTable(this, activePart, SWT.NONE,	member);

		createTabItem(Messages.MemberEditor_Tab_Label_Specialization,
				new GridLayout(1, true));
		specializationTable = new SpecializationTable(this, activePart,
				SWT.NONE, member);

		createTabItem(Messages.MemberEditor_Tab_Label_Formation,
				new GridLayout(1, true));
		this.formationTable = new FormationTable(this, activePart, SWT.NONE,
				member);

		createTabItem(Messages.MemberEditor_TabLabelCommittee, new GridLayout(
				2, true));

		Table committeeList = new Table(activeSection, SWT.BORDER);
		toolkit.adapt(committeeList, true, true);
		TableViewer committeeViewer = new TableViewer(committeeList);
		committeeViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				return member.getCommitteemembers()
						.toArray();
			}

			@Override
			public void dispose() {
				;
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				viewer.refresh();

			}

		});
		committeeViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				if (((Committeemember) element).getCommittee() == null)
					return ""; //$NON-NLS-1$
				return ((Committeemember) element).getCommittee().toString();
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

		});
		committeeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IEditorInput input = new CommitteeEditorInput(
						((Committeemember) ((IStructuredSelection) event
								.getSelection()).getFirstElement())
								.getCommittee());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, CommitteeEditor.ID);
				} catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}

		});
		committeeViewer.setInput(member
				.getCommitteemembers());
		createTabItem(Messages.MemberEditor_Tab_Label_Registration,
				new GridLayout(2, true));

		createSection(
				Messages.MemberEditor_Label_Section_Heading_Deregistration,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);
		createDateField(Messages.MemberEditor_Label_DeregistrationDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM, member,
				"deregister_date"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_DeregistrationExeption,
				member, "deregister_exemption"); //$NON-NLS-1$
		createLabeledText(Messages.MemberEditor_Label_DeregistrationReason,
				member, "deregister_reason"); //$NON-NLS-1$

		/*SwtMetawidget widget = new SwtMetawidget(body, SWT.NONE);
		widget.setToInspect(member);
		GridData data = new GridData();
		data.verticalAlignment = SWT.FILL;
		data.horizontalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		widget.setLayoutData( data );
		*/
	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

	public Member getMember() {
		return member;
	}

}
