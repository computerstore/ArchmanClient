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

import it.archiworld.common.protocol.Outentry;

import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class AddEmergencyOutProtocolAction extends Action implements ActionFactory.IWorkbenchAction {

	public final static String ID = "it.archiworld.archman.client.protocol.addemergencyoutprotocolaction"; //$NON-NLS-1$
	private IWorkbenchWindow window;

	public AddEmergencyOutProtocolAction(IWorkbenchWindow window) {
		this.window=window;
	    setId(ID);
	    setText("New Emergency Outgoing Protocol");
	    setToolTipText("Adds a new Outgoing Emergency Protocol");
	}

	public void dispose() {
		;
	}

	public void run() {
		Outentry empty_entry = new Outentry();
		empty_entry.setEmergency(true);
		empty_entry.setProtocol_date(new Timestamp(new Date().getTime()));
		empty_entry.setRegistration_date(new Timestamp(new Date().getTime()));
		IEditorInput input = new OutProtocolEditorInput(empty_entry);
		try {
			window.getActivePage().openEditor(input, OutProtocolEditor.ID)
				.addPropertyListener(
							(IPropertyListener) window.getActivePage().findView(ProtocolTreeView.ID));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
