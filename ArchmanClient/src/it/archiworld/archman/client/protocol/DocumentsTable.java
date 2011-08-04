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
import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.protocol.util.DocumentDropListener;
import it.archiworld.common.protocol.Document;
import it.archiworld.common.protocol.Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class DocumentsTable extends Composite {

	private Entry entry;
	
	private TableViewer tableViewer;

	private ScrolledFormEditorBase form;
	
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Document>) inputElement).toArray();
			}
			return null;
		}

		@Override
		public void dispose() {
			;
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			viewer.refresh();
		}
	};

	private KeyListener tableKeyListener = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == SWT.DEL) {
				deleteSelected();
			}
			if (e.keyCode == SWT.INSERT) {
				createNew();
			}
		}

	};

	public DocumentsTable(final ScrolledProtocolFormEditor form,
			final Composite parent, int style, Entry input) {
		super(parent, style);
		form.getToolkit().adapt(this);
		this.entry = input;
		this.form = form;

		this.setLayout(new GridLayout(1, true));

		final Table table = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		form.getToolkit().adapt(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addKeyListener(tableKeyListener);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(100, 200, true));
		table.setLayout(layout);

		TableColumn fileColumn = new TableColumn(table, SWT.BEGINNING);
		fileColumn.setText(Messages.DocumentsTable_Column_Heading_File);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);

		tableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (((Document)e1).getName()!=null && ((Document)e2).getName()!=null)
					return ((Document)e1).getName().compareTo(((Document)e2).getName());
				return 0;
			}
		});

		tableViewer.addDropSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE, new Transfer[] { FileTransfer.getInstance() },
				new DocumentDropListener(form, this));
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof StructuredSelection) {
					if (((IStructuredSelection) event.getSelection())
							.getFirstElement() instanceof Document) {
						Document document = (Document) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						System.out.println(document);
						try {

							File file = File.createTempFile(document.getName(),
									"." + document.getSuffix()); //$NON-NLS-1$
							FileOutputStream fstream = new FileOutputStream(
									file);

							fstream.write(document.getFile());
							fstream.close();
							Program.launch(file.toString());
							file.deleteOnExit();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		/**
		 * Column File
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				fileColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Document
						&& ((Document) element).getName() != null) {
					String checksum = "NOTOK";
					try {
						if(element!=null && ((Document)element).getFilefingerprintSHA1()!=null){
							System.out.println("Stored Fingerprint:     "+new String(((Document) element).getFilefingerprintSHA1()));
							
							//SHA1 Calculation
							MessageDigest digest = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$
							digest.update(((Document) element).getFile());
							byte[] messageDigest = digest.digest();
							String fingerprint=""; //$NON-NLS-1$
							 for (int j = 0; j < messageDigest.length; j++ )
							      fingerprint+=String.format("%02x", messageDigest[j]); //$NON-NLS-1$
							System.out.println(fingerprint);
							 
							if(new String(((Document) element).getFilefingerprintSHA1()).equals(fingerprint))
								checksum = "OK";
						}
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return ((Document) element) + "." //$NON-NLS-1$
							+ " " + checksum;
				}
				return new String(Messages.DocumentsTable_CellLabel_File_IsEmpty);
				
			}
		});
		tableViewer.setInput(entry.getDocuments());

		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button newButton = form.getToolkit().createButton(buttonBar,
				Messages.DocumentsTable_Button_CreateNewEntry, SWT.NONE);
		newButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		newButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				createNew();
			}
		});
			
		Button deleteButton = form.getToolkit().createButton(buttonBar,
				Messages.DocumentsTable_Button_DeleteSelectedEntries, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		deleteButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteSelected();
			}
		});
		tableViewer.refresh();
	}

	public void createNew() {
		FileDialog fdialog = new FileDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());
		String filename = fdialog.open();
		
		Document document = new Document();
		File file = new File(filename);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("File is too large to process"); //$NON-NLS-1$
			return;
		}
		try{
			FileInputStream fstream = new FileInputStream(file);

			byte[] bytes = new byte[(int) length];

		// 	Read in the bytes
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
		}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch(IOException ex){
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entry.getDocuments().add(document);
		form.setDirty(true);
		tableViewer.refresh();
		tableViewer.editElement(document, 0);
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();

		for (Object o : selection.toList()) {
			entry.getDocuments().remove(o);
		}
		for (Document doc : entry.getDocuments()) {
			System.out.println(doc);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

	public final void addDocument(Document document) {
		entry.getDocuments().add(document);
		tableViewer.refresh();
		
	}

	public final TableViewer getTableViewer() {
		return tableViewer;
	}

}
