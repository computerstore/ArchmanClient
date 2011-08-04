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


import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class PersonEditorActionBarContributor implements
		IEditorActionBarContributor {

	private IWorkbenchAction saveAsMemberAction;
	private IWorkbenchAction saveAsServiceMemberAction;

//	private IEditorPart activeEditor;

	public void dispose() {
		saveAsMemberAction.dispose();
		saveAsServiceMemberAction.dispose();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		saveAsMemberAction = new SaveAsMemberAction(page.getWorkbenchWindow());
		((IMenuManager) bars.getMenuManager().find(
				IWorkbenchActionConstants.M_FILE)).appendToGroup("save", //$NON-NLS-1$
				saveAsMemberAction);

		bars.getToolBarManager().add(saveAsMemberAction);
		
		saveAsServiceMemberAction = new SaveAsServiceMemberAction(page.getWorkbenchWindow());
		((IMenuManager) bars.getMenuManager().find(
				IWorkbenchActionConstants.M_FILE)).appendToGroup("save", //$NON-NLS-1$
				saveAsServiceMemberAction);

		bars.getToolBarManager().add(saveAsServiceMemberAction);
		
		bars.updateActionBars();

	}

	public void setActiveEditor(IEditorPart targetEditor) {
//		this.activeEditor = targetEditor;
	}

}
