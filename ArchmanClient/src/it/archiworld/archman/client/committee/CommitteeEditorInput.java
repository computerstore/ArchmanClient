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
package it.archiworld.archman.client.committee;

import it.archiworld.archman.client.core.BaseEditorInput;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.committee.Committees;
import it.archiworld.common.committee.Committee;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

public class CommitteeEditorInput extends BaseEditorInput {

	private Committee committee;

	public CommitteeEditorInput(Committee committee) {
		super(committee);
		this.committee = committee;
	}

	public String getName() {
		if (committee.getName() == null)
			return Messages.CommitteeEditorInput_EmptyCommittee_Name;
		else
			return committee.getName();
	}

	public String getToolTipText() {
		return getName();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromFile(this.getClass(),
				"/icons/vcard.png"); //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == Committee.class	|| adapter == Object.class) {
			return getBean();
		}
		System.out
				.println("[WARN] CommitteeEditorInput.getAdaper(Class): Adapter for not implemented class " //$NON-NLS-1$
						+ adapter.toString() + " requested!"); //$NON-NLS-1$
		return null;
	}

	@Override
	public void refresh() {
		if (((Committee) getBean()).getCommittee_id() == null)
			return;
		Committee committee = (Committee) getBean();
		try {
			Committees committees = (Committees) ConnectionFactory
					.getConnection("CommitteesBean"); //$NON-NLS-1$
			committee = committees.getCommittee(committee);
			if (committee != null)
				setBean(committee);
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public Object createOrUpdate() throws Throwable {
		try {
			Committees committees = (Committees) ConnectionFactory
					.getConnection("CommitteesBean"); //$NON-NLS-1$
			Committee committee = committees
					.saveCommittee((Committee) getBean());
			setBean(committee);
			return committee;
		} catch (Throwable t) {
			throw t;
		}
	}

	@Override
	public boolean exists() {
		Committee committee = (Committee) getBean();
/*		try {
			Committees committees = (Committees) ConnectionFactory
					.getConnection("CommitteesBean"); //$NON-NLS-1$
			// committee = committees ...
		} catch (ConnectionException ce) {
			ce.printStackTrace();
			return false;
		} catch (NoResultException nre) {
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
*/		if (committee != null)
			return true;
		return false;
	}

	@Override
	public IPersistableElement getPersistable() {
		return new IPersistableElement() {

			@Override
			public String getFactoryId() {
				return "it.archiworld.archman.client.committee.elementfactory"; //$NON-NLS-1$
			}

			@Override
			public void saveState(IMemento memento) {
				if (getBean() != null) {
					memento.putString("class", getBean().getClass() //$NON-NLS-1$
							.getSimpleName());
					if (getBean() instanceof Committee
							&& ((Committee) getBean()).getCommittee_id() != null)
						memento.putInteger("id", ((Committee) getBean()) //$NON-NLS-1$
								.getCommittee_id().intValue());
				}
			}
		};
	}

}
