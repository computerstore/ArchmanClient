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

import it.archiworld.archman.client.protocol.DocumentsTable;
import it.archiworld.archman.client.protocol.ScrolledProtocolFormEditor;
import it.archiworld.common.protocol.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;

public class DocumentDropListener extends ViewerDropAdapter {

	private DocumentsTable table;

	private ScrolledProtocolFormEditor editor;

	public DocumentDropListener(ScrolledProtocolFormEditor editor,
			DocumentsTable table) {
		super(table.getTableViewer());
		this.table = table;
		this.editor = editor;
	}

	@Override
	public final boolean performDrop(Object data) {
		try {
			String[] datas = (String[]) data;
			for (int i = 0; i < datas.length; i++) {
				Document document = new Document();
				
				//document.setEntry(editor.getEntry());
				
				File file = new File(datas[i]);
				long length = file.length();
				if (length > Integer.MAX_VALUE) {
					System.out.println("File is too large to process"); //$NON-NLS-1$
					return false;
				}
				FileInputStream fstream = new FileInputStream(file);

				byte[] bytes = new byte[(int) length];

				// Read in the bytes
				int offset = 0;
				int numRead = 0;
				while ((offset < bytes.length)
						&& ((numRead = fstream.read(bytes, offset, bytes.length
								- offset)) >= 0)) {

					offset += numRead;

				}

				// Ensure all the bytes have been read in
				if (offset < bytes.length) {
					throw new IOException(
							"Could not completely read file " + file.getName()); //$NON-NLS-1$
				}
				fstream.close();

				document.setFile(bytes);

				//SHA512 Calculation
				MessageDigest digest = MessageDigest.getInstance("SHA-512"); //$NON-NLS-1$
				digest.update(bytes);
				byte[] messageDigest = digest.digest();
				String fingerprint=""; //$NON-NLS-1$
				 for (int j = 0; j < messageDigest.length; j++ )
				      fingerprint+=String.format("%02x", messageDigest[j]); //$NON-NLS-1$
				
				if (file.getName().lastIndexOf('.') != -1) {
					document.setName(file.getName().substring(0,
							file.getName().lastIndexOf('.')));
					document.setSuffix(file.getName().substring(
							file.getName().lastIndexOf('.') + 1));
				} else
					document.setName(file.getName());
				document.setLocation(file.getAbsolutePath());
				document.calculateFingerprintSHA1();
				table.addDocument(document);
				this.editor.setDirty(true);
			}

			return true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;

	}

	@Override
	public final boolean validateDrop(final Object target, final int operation,
			TransferData transferType) {
		System.out.println("validateDrop"); //$NON-NLS-1$
		return FileTransfer.getInstance().isSupportedType(transferType);
	}
}
