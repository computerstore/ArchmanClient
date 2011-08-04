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

import it.archiworld.common.Person;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class PersonSearchEditor extends ScrolledCRMFormEditor implements
		SearchEditor {

	public static String ID = "it.archiworld.archman.client.crm.personsearcheditor"; //$NON-NLS-1$

	private Person address;

	@Override
	public void doSearch(IProgressMonitor monitor) {
		System.out.println("Search Person"); //$NON-NLS-1$
		IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
		AddressTreeView view = (AddressTreeView) window.getActivePage()
				.findView(AddressTreeView.ID);
		if (view != null) {
			System.out.println(view.getTitle());
			view.updateList(address);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	public void doSaveAsMember(IProgressMonitor monitor) {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.address = (Person) ((PersonEditorInput) input)
				.getBean();
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
	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void setDirty(boolean dirty) {
	}

}
