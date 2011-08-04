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

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class AddEmergencyInProtocolAction extends Action implements ActionFactory.IWorkbenchAction {

	public final static String ID = "it.archiworld.archman.client.protocol.addemergencyinprotocolaction"; //$NON-NLS-1$
	private IWorkbenchWindow window;

	public AddEmergencyInProtocolAction(IWorkbenchWindow window) {
		this.window=window;
	    setId(ID);
	    setText("New Emergency Incoming Protocol");
	    setToolTipText("Adds a new Incoming Emergency Protocol");
	}
	
	@Override
	public void run() {
		Inentry empty_entry = new Inentry();
		empty_entry.setEmergency(true);
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

	@Override
	public void dispose() {
	}


}
