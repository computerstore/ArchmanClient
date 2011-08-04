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
package it.archiworld.archman.client.util;

import it.archiworld.common.Address;
import it.archiworld.common.committee.Committee;
import it.archiworld.common.committee.Committeemember;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

public class CommitteeDragListener extends DragSourceAdapter {

	private StructuredViewer viewer;
	
	public CommitteeDragListener(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void dragFinished(DragSourceEvent event) {
	    if (!event.doit)
	          return;
	    viewer.refresh();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dragSetData(DragSourceEvent event) {
		try{
			IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
			List select = selection.toList();
			List<Address> addresses = new ArrayList<Address>();  
			for(int i=0;i<select.size();i++){
				if(select.get(i) instanceof Committeemember)
					addresses.add(((Committeemember)select.get(i)).getMember());
				if(select.get(i) instanceof Committee){
					for(Committeemember member :((Committee)select.get(i)).getMember())
						addresses.add(member.getMember());
				}
			}
			
			Address[] addr = new Address[addresses.size()];
			addresses.toArray(addr);
			event.data = addr;
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	      event.doit = !viewer.getSelection().isEmpty();
	}

}
