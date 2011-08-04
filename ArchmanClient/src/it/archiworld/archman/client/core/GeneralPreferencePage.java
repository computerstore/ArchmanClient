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
package it.archiworld.archman.client.core;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class GeneralPreferencePage extends PreferencePage implements
								IWorkbenchPreferencePage {

	private static final String LANGUAGE = "preference.language"; //$NON-NLS-1$
	
	Combo languagecombo;
	
	@Override
	protected Control createContents(Composite parent) {
		languagecombo = new Combo(parent,SWT.NONE);
		ComboViewer viewer = new ComboViewer(languagecombo);
		String[] servers = new String[] {Messages.GeneralPreferencePage_ComboEntry_German, Messages.GeneralPreferencePage_ComboEntry_Italian, Messages.GeneralPreferencePage_ComboEntry_English};
		viewer.add(servers);
		viewer.setSelection(new StructuredSelection(getPreferenceStore().getString(LANGUAGE)),true);
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
	    if ((languagecombo != null) && (languagecombo.getSelectionIndex()!=-1)){
	    	this.getPreferenceStore().setValue(LANGUAGE, languagecombo.getItem(languagecombo.getSelectionIndex()));
	    }	     	   
	    return true;
	}

}
