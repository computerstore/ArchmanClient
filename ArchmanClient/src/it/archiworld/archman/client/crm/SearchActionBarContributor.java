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

public class SearchActionBarContributor implements	IEditorActionBarContributor {

	private IWorkbenchAction searchAction;

//	private IEditorPart activeEditor;

	public void dispose() {
		searchAction.dispose();
	}

	public void init(IActionBars bars, IWorkbenchPage page) {
		searchAction = new SearchAction(page.getWorkbenchWindow());
		((IMenuManager) bars.getMenuManager().find(
				IWorkbenchActionConstants.M_FILE)).appendToGroup("save", //$NON-NLS-1$
				searchAction);

		bars.getToolBarManager().add(searchAction);
		bars.updateActionBars();

	}

	public void setActiveEditor(IEditorPart targetEditor) {
//		this.activeEditor = targetEditor;
	}

}
