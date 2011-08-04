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

import it.archiworld.common.protocol.Entry;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

public class EntryDragListener extends DragSourceAdapter {

	private StructuredViewer viewer;
	
	public EntryDragListener(StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void dragFinished(DragSourceEvent event) {
	    if (!event.doit)
	          return;
	    viewer.refresh();
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		try{
			IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
		    event.data = selection.getFirstElement();
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {
	      event.doit = !viewer.getSelection().isEmpty() && ((IStructuredSelection)viewer.getSelection()).getFirstElement() instanceof Entry;
	}

}
