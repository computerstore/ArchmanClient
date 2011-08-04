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

import org.eclipse.jface.resource.ImageDescriptor;

public class PersonEditorInput extends CRMEditorInput {

	private Person address;

	public PersonEditorInput(Person person) {
		super(person);
		this.address = (Person) getBean();
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromFile(this.getClass(),
				"/help/icons/user.png"); //$NON-NLS-1$
	}

	public String getName() {
		if (address.getFirstname() == null || address.getLastname() == null)
			return Messages.PersonEditorInput_EmptyPerson_Name;
		return address.toString();
	}

	public String getToolTipText() {
		String text = new String();
		if (address.getTitle() != null)
			text += address.getTitle();
		if (address.getFirstname() != null)
			text += address.getFirstname();
		if (address.getLastname() != null)
			text += address.getLastname();
		if (address.getCity() != null)
			text += " (" + address.getCity() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		return getName();
	}

	@Override
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	public Object getAdapter(Class adapter) {
		if (adapter == Person.class || adapter == Object.class) {
			return getBean();
		}
		System.out
				.println("[WARN] PersonEditorInput.getAdaper(Class): Adapter for not implemented class " //$NON-NLS-1$
						+ adapter.toString() + " requested!"); //$NON-NLS-1$
		return null;
	}

}
