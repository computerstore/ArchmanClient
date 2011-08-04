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
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.ServiceMember;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
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
//import org.metawidget.swt.SwtMetawidget;

public class PersonEditor extends ScrolledCRMFormEditor {

	public static String ID = "it.archiworld.archman.client.crm.personeditor"; //$NON-NLS-1$

	private Person address;

	@Override
	public void doSave(IProgressMonitor monitor) {

		try {
			if (!this.authenticate()){
				return;  
			}
			String error=""; //$NON-NLS-1$
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

			PersonEditorInput input = (PersonEditorInput) getEditorInput();
			address = (Person)input.createOrUpdate();
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.PersonEditor_SaveSuccessDialog_Heading,
					Messages.PersonEditor_SaveSuccessDialog_Text);
			setDirty(false);
			setPartName(this.getEditorInput().getName());
			getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(
					this, false);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.PersonEditor_SaveFailureDialog_Heading,
					Messages.PersonEditor_SaveFailureDialog_Text);
		}
	}

	public void doSaveAsMember(IProgressMonitor monitor) {
		Member member=new Member();
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			member = book.savePersonAsMember(address);
			this.setDirty(false);
			firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.PersonEditor_SaveSuccessDialog_Heading,
					Messages.PersonEditor_SaveSuccessDialog_Text);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.PersonEditor_SaveFailureDialog_Heading,
					Messages.PersonEditor_SaveFailureDialog_Text);
		}
		try {
			IEditorPart memberEditor = getEditorSite().getWorkbenchWindow()
					.getActivePage().openEditor(new MemberEditorInput(member),
							MemberEditor.ID);
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

	public void doSaveAsServiceMember(IProgressMonitor monitor) {
		ServiceMember member = new ServiceMember();
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			member = book.savePersonAsServiceMember(address);
			this.setDirty(false);
			firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.PersonEditor_ServiceMemberSaveDialogTitle,
					Messages.PersonEditor_ServiceMemberSaveDialogText);
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openError(this.getSite().getShell(),
					Messages.PersonEditor_ServiceMemberErrorDialogTitle,
					Messages.PersonEditor_ServiceMemberErrorDialogText);
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
		this.address = (Person) ((PersonEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		form.setText(Messages.PersonEditor_Form_Heading);
		header.setLayout(new FillLayout());
		NamePart namePart = createNamePart(address, "title", "firstname", //$NON-NLS-1$ //$NON-NLS-2$
				"lastname"); //$NON-NLS-1$
		namePart.getTitleText().forceFocus();
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(2, true));

		createSection(Messages.PersonEditor_Section_Heading_Address,
				Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED);

		createLabeledText(Messages.PersonEditor_Label_Street, address, "street"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Zip, address, "zip"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_City, address, "city"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_State, address, "state"); //$NON-NLS-1$
		createLabeledText(Messages.PersonEditor_Label_Country, address,
				"country"); //$NON-NLS-1$

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

		createSection(Messages.PersonEditor_Section_Heading_Finances,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

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

		createSection(Messages.PersonEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.PersonEditor_Label_Note, address,
				"note"); //$NON-NLS-1$
	
/*		SwtMetawidget widget = new SwtMetawidget(body, SWT.NONE);
		widget.setToInspect(address);
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

}
