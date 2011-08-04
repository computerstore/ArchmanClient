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

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.committee.CommitteeEditor;
import it.archiworld.archman.client.committee.CommitteeMemberTable;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Member;
import it.archiworld.common.committee.Committeemember;

import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

public class AddressDropListener extends ViewerDropAdapter {

	private CommitteeMemberTable viewer;

	private CommitteeEditor editor;

	public AddressDropListener(CommitteeEditor editor,
			CommitteeMemberTable viewer) {
		super(viewer.getTableViewer());
		this.viewer = viewer;
		this.editor = editor;
	}

	@Override
	public final boolean performDrop(Object data) {
		try {
			Addressbook book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
			Address[] addresslist = (Address[]) data;
			Member[] memberlist = new Member[addresslist.length];
			for(int i=0;i<memberlist.length;i++)
				if(addresslist[i] instanceof Member)
					memberlist[i]=(Member)addresslist[i];
			if(memberlist[0]==null)
				return false;
			for (int i = 0; i < viewer.getTableViewer().getTable()
					.getItemCount(); i++)
				if (memberlist[0].getEntity_id().equals(
						((Committeemember) viewer.getTableViewer()
								.getElementAt(i)).getMember().getEntity_id()))
					return false;
			Member member = (Member) book.getAddress(memberlist[0]);
			Committeemember committeemember = new Committeemember();
			committeemember.setCommittee(editor.getCommittee());
			committeemember.setMember(member);
			viewer.getTableViewer().add(committeemember);
			editor.getMembers().add(committeemember);
			viewer.getTableViewer().reveal(committeemember);
			viewer.getTableViewer().refresh(committeemember);
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
