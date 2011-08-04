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
package it.archiworld.archman.client.committee;

import it.archiworld.archman.client.crm.AddressTreeView;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class CommitteePerspective implements IPerspectiveFactory {

	public final void createInitialLayout(final IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, //$NON-NLS-1$
				0.3f, editorArea);
		left.addView(CommitteeTreeView.ID);
		left.addView(AddressTreeView.ID);

		layout
				.addPerspectiveShortcut("it.archiworld.archman.client.crm.perspective"); //$NON-NLS-1$
		layout
				.addPerspectiveShortcut("it.archiworld.archman.client.protocol.perspective"); //$NON-NLS-1$
		layout
				.addActionSet("it.archiworld.archman.client.committee.perspective.actionset"); //$NON-NLS-1$
	}

}
