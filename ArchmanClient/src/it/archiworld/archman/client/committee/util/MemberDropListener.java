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
package it.archiworld.archman.client.committee.util;

import it.archiworld.archman.client.committee.CommitteeEditor;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Member;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

public class MemberDropListener extends ViewerDropAdapter {

	private ListViewer viewer;

	private CommitteeEditor editor;

	public MemberDropListener(CommitteeEditor editor, ListViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		this.editor = editor;
	}

	@Override
	public final boolean performDrop(Object data) {
		try {
			Member member = (Member) data;
			for (int i = 0; i < viewer.getList().getItemCount(); i++)
				if (member.getEntity_id().equals(
						((Address) viewer.getElementAt(i)).getEntity_id()))
					viewer.remove(viewer.getElementAt(i));
			viewer.add(member);
			//editor.getMembers().add(member);
			viewer.reveal(member);
			viewer.refresh(member);
			editor.setDirty(true);
			return true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;

	}

	@Override
	public final boolean validateDrop(final Object target, final int operation,
			TransferData transferType) {
		return AddressTransfer.getInstance().isSupportedType(transferType);
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
