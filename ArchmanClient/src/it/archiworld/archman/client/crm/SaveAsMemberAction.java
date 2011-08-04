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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;

public class SaveAsMemberAction extends Action implements
		ActionFactory.IWorkbenchAction, IPartListener2 {

	private ImageDescriptor icon;

	public final static String ID = "it.archiworld.archman.client.crm.saveasmemberaction"; //$NON-NLS-1$

	private IWorkbenchPart activePart;
	
	private PersonEditor personEditor;

	private IWorkbenchWindow window;

	public SaveAsMemberAction(IWorkbenchWindow window) {
		this.window = window;
		icon = ImageDescriptor.createFromFile(this.getClass(),
				"/help/icons/vcard_add.png"); //$NON-NLS-1$
		setId(ID);
		setText(Messages.SaveAsMemberAction_Action_Text);
		setToolTipText(Messages.SaveAsMemberAction_Action_ToolTipText);
		setImageDescriptor(icon);
		window.getPartService().addPartListener(this);
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		System.out.println("Info: Part " + partRef.getId() + " activated!"); //$NON-NLS-1$ //$NON-NLS-2$
		if (partRef.getId().equals(PersonEditor.ID)) {
			activePart = partRef.getPart(false);
			personEditor = (PersonEditor) activePart;
			
		} else {
			personEditor = null;
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
		if (personEditor != null) {
			personEditor.doSaveAsMember(null);
		} else {
			System.out
					.println("Warning! SaveAsMember with invalid Editor called."); //$NON-NLS-1$
		}
	}

	@Override
	public void dispose() {
		window.getPartService().removePartListener(this);
	}

}
