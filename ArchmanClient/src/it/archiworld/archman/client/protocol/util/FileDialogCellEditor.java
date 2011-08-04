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
package it.archiworld.archman.client.protocol.util;

import java.io.File;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

public class FileDialogCellEditor extends DialogCellEditor
{

	public FileDialogCellEditor(Composite parent)
	{
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.DialogCellEditor#getDefaultLabel()
	 */
	protected Label getDefaultLabel()
	{
		return super.getDefaultLabel();
	}

	protected Object openDialogBox(Control cellEditorWindow)
	{
		FileDialog ftDialog = new FileDialog(cellEditorWindow.getShell(), SWT.SAVE);
//		File theFile = (File) getValue();
//		if(theFile != null)
//			ftDialog.setFileName(theFile.getAbsolutePath());
		String thisFile = ftDialog.open();
		File resFile = new File(thisFile);

		return resFile;
	}

}
