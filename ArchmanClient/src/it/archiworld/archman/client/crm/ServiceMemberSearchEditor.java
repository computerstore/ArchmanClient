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

import it.archiworld.common.ServiceMember;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.hibernate.criterion.Criterion;

public class ServiceMemberSearchEditor extends ScrolledCRMSearchFormEditor
		implements SearchEditor {

	public static String ID = "it.archiworld.archman.client.crm.servicemembersearcheditor"; //$NON-NLS-1$

	private ServiceMember service_member;

	@Override
	public void doSearch(IProgressMonitor monitor) {
		System.out.println("Search ServiceMember"); //$NON-NLS-1$
		IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
		AddressTreeView view = (AddressTreeView) window.getActivePage()
				.findView(AddressTreeView.ID);
		if (view != null) {
			System.out.println(view.getTitle());
			Criterion criterion = this.buildCriterion();
			view.updateList(ServiceMember.class, criterion, this.orders);
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
		form.setText(Messages.ServiceMemberEditor_LabelServiceMember);
		header.setLayout(new FillLayout());
		createNamePart(service_member,
				"title", "firstname", //$NON-NLS-1$ //$NON-NLS-2$
				"lastname"); //$NON-NLS-1$
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.ServiceMemberEditor_TabLabelGeneral,
				new GridLayout(2, true));

		createSection(Messages.PersonEditor_Section_Heading_Contact,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_PhoneNumber,
				service_member, "phone"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Mobile, service_member,
				"mobilephone"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Fax, service_member,
				"fax"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Email, service_member,
				"email"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Website, service_member,
				"website"); //$NON-NLS-1$

		createSection(Messages.PersonEditor_Section_Heading_Address,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_Street, service_member,
				"street"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Zip, service_member,
				"zip"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_City, service_member,
				"city"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_State, service_member,
				"state"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Country, service_member,
				"country"); //$NON-NLS-1$

		createSection(Messages.ServiceMemberEditor_SectionLabelPersonalData,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

		createLabeledText(Messages.ServiceMemberSearchEditor_Label_RegistrationExemption, service_member, "exemption"); //$NON-NLS-1$

		createLabeledText(Messages.ServiceMemberEditor_LabelNationality,
				service_member, "nationality"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelBirthplace,
				service_member, "birthplace"); //$NON-NLS-1$
		createDateField(Messages.ServiceMemberEditor_LabelBirthdate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM,
				service_member, "birthdate"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelOriginstate,
				service_member, "originstate"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelChamber,
				service_member, "chamber"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_TitleApprovalExcemption,
				service_member,
				"title_approval_exemption"); //$NON-NLS-1$

		createSection(Messages.PersonEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR
						| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.PersonEditor_Label_Note,
				service_member, "note"); //$NON-NLS-1$

//		createTabItem(Messages.ServiceMemberEditor_LabelServices,
//				new GridLayout(1, true));
//		serviceTable = new ServiceTable(this, activePart, SWT.NONE,
//				((IServiceMember) service_member));

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
