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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class OutProtocolEditorActionContributor implements
		IEditorActionBarContributor {

	private IWorkbenchAction openWordTemplateAction;

	@Override
	public void dispose() {
		this.openWordTemplateAction.dispose();
	}

	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {
		openWordTemplateAction = new OpenWordTemplateAction(page.getWorkbenchWindow());
		((IMenuManager) bars.getMenuManager().find(
				IWorkbenchActionConstants.M_FILE)).appendToGroup("print", //$NON-NLS-1$
				openWordTemplateAction);

		bars.getToolBarManager().add(openWordTemplateAction);
		
		bars.updateActionBars();

	}

	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
//		this.activeEditor = targetEditor;
	}

}
