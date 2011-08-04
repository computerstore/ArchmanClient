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

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.protocol.OutProtocolEditor;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Person;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.EntryCompanyAddress;
import it.archiworld.common.protocol.EntryPersonAddress;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

public class AddressDropListener extends ViewerDropAdapter {

	private TableViewer viewer;

	private OutProtocolEditor editor;

	public AddressDropListener(OutProtocolEditor editor, TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		this.editor = editor;
	}

	@Override
	public final boolean performDrop(Object data) {
		try {
			Address[] address = (Address[]) data;
			for(int j=0;j<address.length;j++){
				boolean add=true;
				for (int i = 0; i < viewer.getTable().getItemCount(); i++)
					if (address[j].getEntity_id().equals(((EntryAddress) viewer.getElementAt(i)).getAddress().getEntity_id()))
						add=false;
					if(add){
						Addressbook book = (Addressbook) ConnectionFactory.getConnection("AddressbookBean"); //$NON-NLS-1$
						address[j]=book.getAddress(address[j]);
						if(address[j] instanceof Person)
							editor.addDestination(new EntryPersonAddress((Person) address[j]));
						if(address[j] instanceof Company)
							editor.addDestination(new EntryCompanyAddress((Company) address[j]));
						viewer.refresh();
						editor.setDirty(true);
				}
			}
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
