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
package it.archiworld.archman.client.crm;

import it.archiworld.archman.client.core.SearchEditorInput;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;

public class ServiceMemberSearchActionDelegate implements IViewActionDelegate {

	private IWorkbenchWindow window;

	@Override
	public void init(IViewPart view) {
		this.window = view.getViewSite().getWorkbenchWindow();
	}

	public void run(IAction action) {
		try {
//			Addressbook addressbook = (Addressbook) ConnectionFactory
//					.getConnection("AddressbookBean"); //$NON-NLS-1$
			IEditorInput input = new SearchEditorInput();
			window.getActivePage().openEditor(input,
					ServiceMemberSearchEditor.ID).addPropertyListener(
					(IPropertyListener) window.getActivePage().findView(
							AddressTreeView.ID));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		;
	}

}
