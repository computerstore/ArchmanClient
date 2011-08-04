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
import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.AddressTreeView;
import it.archiworld.archman.client.protocol.InProtocolEditor;
import it.archiworld.archman.client.protocol.OutProtocolEditor;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Person;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.EntryCompanyAddress;
import it.archiworld.common.protocol.EntryPersonAddress;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;

public class InsertAddressIntoEntryCommandHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart addresstreeview = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		IEditorPart editor = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editor==null)
			return null;
		TreeSelection selection = (TreeSelection)addresstreeview.getViewSite().getSelectionProvider().getSelection();
		if (selection.isEmpty()) 
			return null;
		Address address = (Address) selection.getFirstElement();
		if(address!=null) {
			Addressbook book;
			try {
				book = (Addressbook) ConnectionFactory.getConnection("AddressbookBean"); //$NON-NLS-1$
				address=book.getAddress(address);
				if (editor instanceof InProtocolEditor){
					if(address instanceof Company)
						((InProtocolEditor)editor).setSender(new EntryCompanyAddress((Company)address));
					if(address instanceof Person)
						((InProtocolEditor)editor).setSender(new EntryPersonAddress((Person)address));
					((InProtocolEditor)editor).setDirty(true);
				}
				if (editor instanceof OutProtocolEditor){
					boolean contained=false;
					for(EntryAddress addr :((OutProtocolEditor)editor).getDestinations())
						if(address.getEntity_id().equals(addr.getAddress().getEntity_id()))
							contained=true;
					if(!contained){
						if(address instanceof Person)
							((OutProtocolEditor)editor).addDestination(new EntryPersonAddress((Person)address));
						if(address instanceof Company)
							((OutProtocolEditor)editor).addDestination(new EntryCompanyAddress((Company)address));
						((OutProtocolEditor)editor).setDirty(true);
					}
				}
				editor.setFocus();
			} catch (ConnectionException e) {
				e.printStackTrace();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

