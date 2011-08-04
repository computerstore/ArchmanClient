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
package it.archiworld.archman.client.core.connect;

import it.archiworld.archman.client.core.Activator;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RMIConnectionSettingsPage extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	public static final String RMISERVER = "preference.RMIServer"; //$NON-NLS-1$
	public static final String RMIPORT = "preference.RMIPort"; //$NON-NLS-1$
	public static final String RMIPRINCIPAL = "preference.RMIPrincipal"; //$NON-NLS-1$
	public static final String RMICREDENTIALS = "preference.RMICREDENTIALS"; //$NON-NLS-1$
		
	Text server;
	Text port; 
	Text principal;
	Text credentials;
	
	@Override
	protected Control createContents(Composite parent) {
		FormToolkit form = new FormToolkit(parent.getDisplay());
		form.createLabel(parent, Messages.RMIConnectionSettingsPage_Label_RMIServer);
		server = form.createText(parent, (getPreferenceStore().getString(RMISERVER)), SWT.BORDER);
		form.createLabel(parent, Messages.RMIConnectionSettingsPage_Label_RMIPort);
		port = form.createText(parent, (getPreferenceStore().getString(RMIPORT)), SWT.BORDER);
		form.createLabel(parent, Messages.RMIConnectionSettingsPage_Label_RMIPrincipal);
		server = form.createText(parent, (getPreferenceStore().getString(RMIPRINCIPAL)), SWT.BORDER);
		form.createLabel(parent, Messages.RMIConnectionSettingsPage_Label_RMICredentials);
		server = form.createText(parent, (getPreferenceStore().getString(RMICREDENTIALS)), SWT.BORDER);
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public boolean performOk() {
		super.performOk();
		// save values
	    this.getPreferenceStore().setValue(RMISERVER, server.getText());
	    this.getPreferenceStore().setValue(RMIPORT, port.getText());
	     	   
	    return true;
	}

}
