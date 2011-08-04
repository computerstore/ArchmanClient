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

import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.util.ProtocolWordDataSourceGenerator;
import it.archiworld.common.protocol.Outentry;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class OpenWordTemplateAction extends Action implements
		ActionFactory.IWorkbenchAction, IPartListener2 {

	protected static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
	protected static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$

	private ImageDescriptor icon;

	public final static String ID = "it.archiworld.archman.client.protocol.openwordtemplateaction"; //$NON-NLS-1$

	private IWorkbenchPart activePart;

	private OutProtocolEditor outProtocolEditor;

	private IWorkbenchWindow window;

	public OpenWordTemplateAction(IWorkbenchWindow window) {
		this.window = window;
		icon = ImageDescriptor.createFromFile(this.getClass(),
				"/help/icons/page_white_word.png"); //$NON-NLS-1$
		setId(ID);
		setText(Messages.OpenWordTemplateAction_OpenWordTemplateActionText);
		setToolTipText(Messages.OpenWordTemplateAction_OpenWordTemplateActionToolTip);
		setImageDescriptor(icon);
		window.getPartService().addPartListener(this);
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		System.out.println("Info: Part " + partRef.getId() + " activated!"); //$NON-NLS-1$ //$NON-NLS-2$
		if (partRef.getId().equals(OutProtocolEditor.ID)) {
			activePart = partRef.getPart(false);
			outProtocolEditor = (OutProtocolEditor) activePart;

		} else {
			outProtocolEditor = null;
			activePart = partRef.getPart(false);
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		;
	}

	@Override
	public void run() {
		try{
			if (outProtocolEditor != null) {
				OutProtocolEditorInput input = (OutProtocolEditorInput) outProtocolEditor
				.getEditorInput();
				Outentry outentry = (Outentry) (input.getBean());
				String dir=""; //$NON-NLS-1$
				if(Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)!="") //$NON-NLS-1$
					dir = Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)+"/protocol.txt"; //$NON-NLS-1$
				else
					dir = (new File(".")).getCanonicalPath()+"/protocol.txt"; //$NON-NLS-1$ //$NON-NLS-2$
				
				ProtocolWordDataSourceGenerator generator = new ProtocolWordDataSourceGenerator(dir, outentry); //$NON-NLS-1$
				generator.generate();
				System.out.println("Export file generated!"); //$NON-NLS-1$
				String filename;
				filename = outProtocolEditor.openWordFileDialog("/"); //$NON-NLS-1$
				if(filename!=null)
					Program.launch(filename);
			} else {
				System.out
					.println("Warning! OpenWordTemplate with invalid Editor called."); //$NON-NLS-1$
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		window.getPartService().removePartListener(this);
	}

}
