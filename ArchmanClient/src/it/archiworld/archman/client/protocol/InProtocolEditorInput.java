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

import it.archiworld.common.protocol.Inentry;

import org.eclipse.jface.resource.ImageDescriptor;

public class InProtocolEditorInput extends ProtocolEditorInput {

	private Inentry entry;

	public InProtocolEditorInput(Inentry entry) {
		super(entry);
		this.entry = entry;
	}

	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromFile(this.getClass(),
				"/icons/arrow_left.png"); //$NON-NLS-1$
	}

	public String getName() {

		if (entry == null || entry.getSubject() == null)
			return Messages.InProtocolEditorInput_EmptyInentry_Name;
		return entry.toString();
	}

	public String getToolTipText() {
		return getName();
	}

	@Override
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	public Object getAdapter(Class adapter) {
		if (adapter == Inentry.class || adapter == Object.class) {
			return getBean();
		}
		System.out
				.println("[WARN] InProtocolEditorInput.getAdaper(Class): Adapter for not implemented class " //$NON-NLS-1$
						+ adapter.toString() + " requested!"); //$NON-NLS-1$
		return null;
	}

}
