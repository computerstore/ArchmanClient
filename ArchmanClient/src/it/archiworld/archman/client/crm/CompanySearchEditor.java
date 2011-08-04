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

import it.archiworld.common.Company;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class CompanySearchEditor extends ScrolledCRMFormEditor implements SearchEditor {

	public static String ID = "it.archiworld.archman.client.crm.companysearcheditor"; //$NON-NLS-1$

	private Company company;

	@Override
	public void doSearch(IProgressMonitor monitor) {
		System.out.println("Search Company"); //$NON-NLS-1$
		IWorkbenchWindow window = this.getSite().getWorkbenchWindow();
		AddressTreeView view = (AddressTreeView) window.getActivePage()
				.findView(AddressTreeView.ID);
		if (view != null) {
			System.out.println(view.getTitle());
			view.updateList(company);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor){
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.company = (Company) ((CompanyEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		form.setText(Messages.CompanyEditor_Form_Heading);
		header.setLayout(new GridLayout(1, true));

		Label denominationLabel = toolkit.createLabel(header,
				Messages.CompanyEditor_Label_Denomination);
		denominationLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
				false));
		Text denominationText = toolkit.createText(header, null, SWT.BORDER);
		denominationText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
				true, false));
		dataBindingContext.bindValue(
				SWTObservables.observeText(denominationText, SWT.Modify),
				PojoObservables.observeValue(company, "denomination"), null, //$NON-NLS-1$
				null).getTarget().addChangeListener(dirtyListener);
		NamePart namePart = createNamePart(company, "contact_title", //$NON-NLS-1$
				"contact_firstname", "contact_lastname"); //$NON-NLS-1$ //$NON-NLS-2$
		namePart.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
				false));
		denominationText.forceFocus();
	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(2, true));

		createSection(Messages.CompanyEditor_Section_Heading_Contact,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledText(Messages.CompanyEditor_Label_Phone, company, "phone"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Mobile, company,
				"mobilephone"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Fax, company, "fax"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Email, company, "email"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Website, company,
				"website"); //$NON-NLS-1$

		createSection(Messages.CompanyEditor_Section_Heading_Address,
				Section.TWISTIE | Section.TITLE_BAR
						| Section.EXPANDED);

		createLabeledText(Messages.CompanyEditor_Label_Street, company,
				"street"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Zip, company, "zip"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_City, company, "city"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_State, company, "state"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_Country, company,
				"country"); //$NON-NLS-1$

		createSection(Messages.CompanyEditor_Section_Heading_Finances,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

		createLabeledText(Messages.CompanyEditor_Label_TaxCode, company,
				"tax_code"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_AccountNumber, company,
				"account_nr"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_BankNumber, company,
				"bank_nr"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_IBAN, company, "iban"); //$NON-NLS-1$
		createLabeledText(Messages.CompanyEditor_Label_BIC, company, "bic"); //$NON-NLS-1$

		createSection(Messages.CompanyEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.CompanyEditor_Label_Note, company,
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
	public void setDirty(boolean dirty){
	}

}
