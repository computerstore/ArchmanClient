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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ConnectionSettingsPage extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private static final String CONNECTION = "preference.Connection"; //$NON-NLS-1$
		
	Combo servercombo; 
	
	@Override
	protected Control createContents(Composite parent) {
		servercombo = new Combo(parent,SWT.NONE);
		ComboViewer viewer = new ComboViewer(servercombo);
		String[] servers = new String[] {"it.archiworld.archman.client.core.connect.RMIConnection"}; //$NON-NLS-1$
		viewer.add(servers);
		viewer.setSelection(new StructuredSelection(getPreferenceStore().getString(CONNECTION)),true);
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
	    if ((servercombo != null) && (servercombo.getSelectionIndex()!=-1)){
	    	this.getPreferenceStore().setValue(CONNECTION, servercombo.getItem(servercombo.getSelectionIndex()));
	    }
	     
//	    try {
	        this.getPreferenceStore();
	        //TODO save();
//      } catch (IOException e) {
//       return false;
//	      }
	   
	    return true;
	}

	@Override
	protected void performDefaults() {
		// TODO Auto-generated method stub
		super.performDefaults();
	}

}
