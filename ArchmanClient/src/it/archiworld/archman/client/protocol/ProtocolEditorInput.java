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
package it.archiworld.archman.client.protocol;

import it.archiworld.archman.client.core.BaseEditorInput;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.common.protocol.Entry;
import it.archiworld.protocol.Protocol;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

public abstract class ProtocolEditorInput extends BaseEditorInput {

	List<String> responsible = new ArrayList<String>();
	public ProtocolEditorInput(Entry entry) {
		super(entry);
	}

	@Override
	public Object createOrUpdate() throws Throwable {
		try {
			Protocol protocol = (Protocol) ConnectionFactory
					.getConnection("ProtocolBean"); //$NON-NLS-1$
			Entry entry = protocol.saveEntry((Entry) getBean());
			setBean(entry);
			return entry;
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public void refresh() {
		if (((Entry) getBean()).getEntry_id() == null)
			return;
		Entry entry = (Entry) getBean();
		try {
			Protocol protocol = (Protocol) ConnectionFactory
					.getConnection("ProtocolBean"); //$NON-NLS-1$
			entry = protocol.getEntry(entry);
			if (entry != null)
				setBean(entry);
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public boolean exists() {
		Entry entry = (Entry) getBean();
		try {
			Protocol protocol = (Protocol) ConnectionFactory
					.getConnection("ProtocolBean"); //$NON-NLS-1$
			entry = protocol.getEntry(entry);
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return false;
		} catch (NoResultException nre) {
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		if (entry != null)
			return true;
		return false;
	}

	@Override
	public IPersistableElement getPersistable() {
		return new IPersistableElement() {

			@Override
			public String getFactoryId() {
				return "it.archiworld.archman.client.protocol.elementfactory"; //$NON-NLS-1$
			}

			@Override
			public void saveState(IMemento memento) {
				if (getBean() != null) {
					memento.putString("class", getBean().getClass() //$NON-NLS-1$
							.getSimpleName());
					if (getBean() instanceof Entry	&& ((Entry) getBean()).getEntry_id() != null) {
						memento.putString("protocol_id", ((Entry) getBean()) //$NON-NLS-1$
								.getEntry_id().toString());
					}
				}
			}
		};
	}

}
