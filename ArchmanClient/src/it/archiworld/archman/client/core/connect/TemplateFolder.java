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

public class TemplateFolder extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
	private static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$
	private static final String SAVEFOLDER = "preference.SaveFolder"; //$NON-NLS-1$

	Text templatefolder;
	Text exportfolder;
	Text savefolder;
	
	@Override
	protected Control createContents(Composite parent) {
//		parent.setLayout(new FillLayout());
		FormToolkit form = new FormToolkit(parent.getDisplay());
		form.createLabel(parent, Messages.TemplateFolder_LabelTemplateFolder);
		templatefolder = form.createText(parent, (getPreferenceStore().getString(TEMPLATEFOLDER)), SWT.BORDER);
		form.createLabel(parent, Messages.TemplateFolder_LabelExportFolder);
		exportfolder = form.createText(parent, (getPreferenceStore().getString(EXPORTFOLDER)), SWT.BORDER);
		form.createLabel(parent, Messages.TemplateFolder_LabelSaveFolder);
		savefolder = form.createText(parent, (getPreferenceStore().getString(SAVEFOLDER)), SWT.BORDER);
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
	    this.getPreferenceStore().setValue(TEMPLATEFOLDER, templatefolder.getText());
	    this.getPreferenceStore().setValue(EXPORTFOLDER, exportfolder.getText());
	    this.getPreferenceStore().setValue(SAVEFOLDER, savefolder.getText());
	     	   
	    return true;
	}
}
