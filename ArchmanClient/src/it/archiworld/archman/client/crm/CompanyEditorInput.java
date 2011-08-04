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

import org.eclipse.jface.resource.ImageDescriptor;

public class CompanyEditorInput extends CRMEditorInput {

	private Company company;

	public CompanyEditorInput(Company company) {
		super(company);
		this.company = (Company) getBean();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromFile(this.getClass(),
				"/icons/building.png"); //$NON-NLS-1$
	}

	@Override
	public String getName() {
		if (company.getDenomination() == null)
			return Messages.CompanyEditorInput_EmptyCompany_Name;
		return company.getDenomination();
	}

	@Override
	public String getToolTipText() {
		if (company.getDenomination() == null)
			return Messages.CompanyEditorInput_EmptyCompany_Name;
		String name = company.getDenomination();
		if (company.getContact_lastname() != null)
			name += " (" + company.getContact_lastname() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		return name;
	}

	@Override
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	public Object getAdapter(Class adapter) {
		if (adapter == Company.class || adapter == Company.class
				|| adapter == Object.class) {
			return getBean();
		}
		System.out
				.println("[WARN] CompanyEditorInput.getAdaper(Class): Adapter for not implemented class " //$NON-NLS-1$
						+ adapter.toString() + " requested!"); //$NON-NLS-1$
		return null;
	}

}
