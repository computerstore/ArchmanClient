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

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.common.ServiceMember;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class ServiceMemberEditor extends ScrolledCRMFormEditor {

	public static String ID = "it.archiworld.archman.client.crm.servicemembereditor"; //$NON-NLS-1$

	private ServiceMember address;
	private ServiceTable serviceTable;
	
	@Override
	public void doSave(IProgressMonitor monitor) {

		try {
			if (!this.authenticate()){
				return;  
			}
			String error;
			if((error=controlFiscalCode(address.getFiscal_code()))!=""){ //$NON-NLS-1$
				MessageDialog.openError(this.getSite().getShell(),
						Messages.PersonEditor_SaveFailureDialog_Heading,
						error);
				return;
			}
			if((error=controlTaxCode(address.getTax_code()))!=""){ //$NON-NLS-1$
				MessageDialog.openError(this.getSite().getShell(),
						Messages.PersonEditor_SaveFailureDialog_Heading,
						error);
				return;
			}
			ServiceMemberEditorInput input = (ServiceMemberEditorInput) getEditorInput();
			address = (ServiceMember)input.createOrUpdate();

			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.ServiceMemberEditor_ServiceMemberSavedDialogTitle,
					Messages.ServiceMemberEditor_ServiceMemberSavedDialogText);
			setDirty(false);
			setPartName(this.getEditorInput().getName());
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.ServiceMemberEditor_ServiceMemberErrorDialogTitle,
					Messages.ServiceMemberEditor_ServiceMemberErrorDialogText);
		}
	}

	public void doSaveAsMember(IProgressMonitor monitor) {
		ServiceMember member = new ServiceMember(address);
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$

			member = (ServiceMember) book.saveAddress(member);
			book.removeAddress(address);
			this.setDirty(false);
			firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.ServiceMemberEditor_ServiceMemberSaveDialogTitle,
					Messages.ServiceMemberEditor_ServiceMemberSaveDialogText);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.ServiceMemberEditor_ServiceMemberErrorDialogTitle,
					Messages.ServiceMemberEditor_ServiceMemberErrorDialogText);
		}
		try {
			IEditorPart memberEditor = getEditorSite().getWorkbenchWindow()
					.getActivePage().openEditor(new ServiceMemberEditorInput(member),
							ServiceMemberEditor.ID);
			for (Object listener : this.getListeners()) {
				if (listener instanceof IViewPart
						&& listener instanceof IPropertyListener) {
					memberEditor
							.addPropertyListener((IPropertyListener) listener);
					this.removePropertyListener((IPropertyListener) listener);
				}
			}
			getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(
					this, false);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.address = (ServiceMember) ((ServiceMemberEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		form.setText(Messages.ServiceMemberEditor_LabelServiceMember+" "+address.toString()); //$NON-NLS-1$
		header.setLayout(new FillLayout());
		NamePart namePart = createNamePart(address, "title", "firstname", //$NON-NLS-1$ //$NON-NLS-2$
				"lastname"); //$NON-NLS-1$
		namePart.getTitleText().forceFocus();
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.ServiceMemberEditor_TabLabelGeneral, new GridLayout(
				2, true));
		
		createSection(Messages.PersonEditor_Section_Heading_Contact,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_PhoneNumber, address,
				"phone"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Mobile, address,
				"mobilephone"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Fax, address, "fax"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Email, address, "email"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Website, address,
				"website"); //$NON-NLS-1$

		createSection(Messages.PersonEditor_Section_Heading_Address,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_Street, address, "street"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Zip, address, "zip"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_City, address, "city"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_State, address, "state"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Country, address,
				"country"); //$NON-NLS-1$

		createSection(Messages.ServiceMemberEditor_SectionLabelPersonalData,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);
		createLabeledText(Messages.ServiceMemberEditor_Label_RegistrationExemption, address, "exemption"); //$NON-NLS-1$

		createLabeledText(Messages.ServiceMemberEditor_LabelNationality, address,
				"nationality"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelBirthplace, address,
				"birthplace"); //$NON-NLS-1$
		createDateField(Messages.ServiceMemberEditor_LabelBirthdate, CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM, address,
				"birthdate"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelOriginstate, address,
				"originstate"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelChamber, address, 
				"chamber"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_TitleApprovalExcemption, address, "title_approval_exemption");  //$NON-NLS-1$
/*
		createSection(Messages.ServiceMemberEditor_SectionTitleAddressDe,
				Messages.ServiceMemberEditor_SectionDescriptionAddressDE,
				Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR
						| Section.EXPANDED);

		createLabeledText(Messages.ServiceMemberEditor_LabelStreetDe, address, 
				"street_de"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelCityDe, address, 
				"city_de"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelStateDe, address, 
				"state_de"); //$NON-NLS-1$
		createLabeledText(Messages.ServiceMemberEditor_LabelCountryDe, address, 
				"country_de"); //$NON-NLS-1$

		createSection(Messages.PersonEditor_Section_Heading_Finances,
				Messages.PersonEditor_Section_Description_Finances,
				Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR
						| Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_FiscalCode, address,
				"fiscal_code"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_TaxCode, address,
				"tax_code"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_AccountNumber, address,
				"account_nr"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_BankNumber, address,
				"bank_nr"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_IBAN, address, "iban"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_BIC, address, "bic"); //$NON-NLS-1$
*/
		createSection(Messages.PersonEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.PersonEditor_Label_Note, address,
				"note"); //$NON-NLS-1$
		
		createTabItem(Messages.ServiceMemberEditor_LabelServices, new GridLayout(
				1, true));
		serviceTable = new ServiceTable(this, activePart, SWT.NONE, address);
		
	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

}
