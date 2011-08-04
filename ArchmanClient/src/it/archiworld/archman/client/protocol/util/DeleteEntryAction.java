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


import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.protocol.ProtocolTreeView;
import it.archiworld.common.protocol.Entry;
import it.archiworld.protocol.Protocol;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class DeleteEntryAction extends Action implements IViewActionDelegate, IWorkbenchAction, IPartListener2 {

	protected Entry entry;
		
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		entry = (Entry)((IStructuredSelection)selection).getFirstElement();
		
	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(IAction action) {
		if (entry!=null && entry instanceof Entry){
			try {
				
				Protocol proto = (Protocol) ConnectionFactory.getConnection("ProtocolBean");
				//entry = proto.getEntry((Entry)entry);
				//entry.setDeleted(new Timestamp(System.currentTimeMillis()));
				//proto.saveEntry((Entry)entry);
				proto.removeEntry((Entry) entry);
				((ProtocolTreeView)Activator.getDefault().getWorkbench().getViewRegistry().find(ProtocolTreeView.ID)).updateSearchLabelText();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}
}
