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
package it.archiworld.archman.client.protocol.util;

import it.archiworld.archman.client.protocol.ReferenceTable;
import it.archiworld.archman.client.protocol.ScrolledProtocolFormEditor;
import it.archiworld.common.protocol.Entry;

import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

public class EntryDropListener extends ViewerDropAdapter {

	private ReferenceTable referenceTable;
	
	private ScrolledProtocolFormEditor form;

	public EntryDropListener(ScrolledProtocolFormEditor form, ReferenceTable referenceTable) {
		super(referenceTable.getTableViewer());
		this.referenceTable = referenceTable;
		this.form = form;
	}

	@Override
	public final boolean performDrop(Object data) {
		try {
			if (data != null && data instanceof Entry) {
				Entry entry = (Entry) data;
				System.out.println("Entry: " + entry.getEntry_id()); //$NON-NLS-1$
				referenceTable.addReference(entry);
				this.form.setDirty(true);
				return true;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;

	}

	@Override
	public final boolean validateDrop(final Object target, final int operation,
			TransferData transferType) {
		return EntryTransfer.getInstance().isSupportedType(transferType);
	}

	@Override
	public final void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_LINK) != 0) {
				event.detail = DND.DROP_LINK;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
		;
	}

	@Override
	public final void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_LINK) != 0) {
				event.detail = DND.DROP_LINK;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
	}

}
