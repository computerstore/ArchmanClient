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

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class CRMPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);
		
//		layout.addView(AddressTreeView.ID, IPageLayout.LEFT, 0.19f, editorArea);
		layout.addStandaloneView(AddressTreeView.ID,  true, IPageLayout.LEFT, 0.19f, editorArea);
		
		layout.addPerspectiveShortcut("it.archiworld.archman.client.committee.perspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("it.archiworld.archman.client.protocol.perspective"); //$NON-NLS-1$
		layout.addActionSet("it.archiworld.archman.client.crm.perspective.actionset"); //$NON-NLS-1$
	}

}
