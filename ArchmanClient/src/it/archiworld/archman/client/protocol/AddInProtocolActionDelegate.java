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
package it.archiworld.archman.client.protocol;

import it.archiworld.common.protocol.Inentry;

import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class AddInProtocolActionDelegate implements
		IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;

	public void dispose() {
		;
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		Inentry empty_entry = new Inentry();
		empty_entry.setProtocol_date(new Timestamp(new Date().getTime()));
		empty_entry.setRecived_date(new Timestamp(new Date().getTime()));
		empty_entry.setRegistration_date(new Timestamp(new Date().getTime()));
		IEditorInput input = new InProtocolEditorInput(empty_entry);
		try {
			IEditorPart editor = window.getActivePage().openEditor(input, InProtocolEditor.ID, true);
			editor.addPropertyListener(
							(IPropertyListener) window.getActivePage()
									.findView(ProtocolTreeView.ID));
			window.getActivePage().activate(editor);
			((InProtocolEditor)editor).setFocus();

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		;
	}

}
