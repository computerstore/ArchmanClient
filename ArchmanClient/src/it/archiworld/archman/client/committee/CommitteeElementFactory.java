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

import it.archiworld.common.committee.Committee;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class CommitteeElementFactory implements IElementFactory {

	@Override
	public IAdaptable createElement(IMemento memento) {
		String inputType = memento.getString("class"); //$NON-NLS-1$
		if (inputType.equals("Committee")) { //$NON-NLS-1$
			Committee committee = new Committee();
			committee.setCommittee_id(Long.parseLong(memento.getString("id"))); //$NON-NLS-1$
			return new CommitteeEditorInput(committee);
		}
		return null;
	}

}
