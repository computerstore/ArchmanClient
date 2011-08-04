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

import it.archiworld.archman.client.protocol.InProtocolEditorInput;
import it.archiworld.archman.client.protocol.OutProtocolEditorInput;
import it.archiworld.common.protocol.Inentry;
import it.archiworld.common.protocol.Outentry;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class ProtocolElementFactory implements IElementFactory {

	@Override
	public IAdaptable createElement(IMemento memento) {
		String inputType = memento.getString("class"); //$NON-NLS-1$
		if (inputType.equals("Inentry")) { //$NON-NLS-1$
			Inentry inentry = new Inentry();
			inentry.setEntry_id(Long.parseLong(memento.getString("protocol_id"))); //$NON-NLS-1$
			return new InProtocolEditorInput(inentry);
		}
		if (inputType.equals("Outentry")) { //$NON-NLS-1$
			Outentry outentry = new Outentry();
			outentry.setEntry_id(Long.parseLong(memento.getString("protocol_id"))); //$NON-NLS-1$
			return new OutProtocolEditorInput(outentry);
		}
		return null;
	}

}
