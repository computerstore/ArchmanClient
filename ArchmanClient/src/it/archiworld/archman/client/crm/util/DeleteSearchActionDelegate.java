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
package it.archiworld.archman.client.crm.util;

import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.crm.AddressTreeView;
import it.archiworld.archman.client.crm.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class DeleteSearchActionDelegate extends Action implements IViewActionDelegate{


	IViewPart view;
	Boolean remove=false;
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}

	@Override
	public void run(IAction action) {
		view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		if(remove){
			((AddressTreeView)view.getViewSite().getPart()).deleteSearchFolder();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(((IStructuredSelection)selection).getFirstElement()!=null){
			String element=((IStructuredSelection)selection).getFirstElement().toString();
			if(element==Messages.AddressTreeView_FolderLabelSearchResult)
				remove=true;
			else
				remove=false;
		}

	}

}
