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
import it.archiworld.archman.client.core.BaseEditorInput;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.common.Address;

import javax.persistence.NoResultException;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

public abstract class CRMEditorInput extends BaseEditorInput {

	CRMEditorInput(Address address) {
		super(address);
	}

	@Override
	public void refresh() {
		if (((Address) getBean()).getEntity_id() == null)
			return;
		Address address = (Address) getBean();
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			address = book.getAddress(address);
			if (address != null)
				setBean(address);
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public Object createOrUpdate() throws Throwable {
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			Address address = book.saveAddress((Address) getBean());
			setBean(address);
			return address;
		} catch (Throwable t) {
			throw t;
		}
	}
	
	@Override
	public boolean exists() {
		Address address = (Address) getBean();
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			address = book.getAddress(address);
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return false;
		} catch (NoResultException nre) {
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		if (address != null)
			return true;
		return false;
	}

	@Override
	public IPersistableElement getPersistable() {
		return new IPersistableElement() {

			@Override
			public String getFactoryId() {
				return "it.archiworld.archman.client.crm.elementfactory"; //$NON-NLS-1$
			}

			@Override
			public void saveState(IMemento memento) {
				if (getBean() != null) {
					memento.putString("class", getBean().getClass() //$NON-NLS-1$
							.getSimpleName());
					if (getBean() instanceof Address
							&& ((Address) getBean()).getEntity_id() != null)
						memento.putInteger("id", ((Address) getBean()) //$NON-NLS-1$
								.getEntity_id().intValue());
				}
			}
		};
	}

}
