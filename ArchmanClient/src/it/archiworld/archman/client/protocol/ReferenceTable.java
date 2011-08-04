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

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.protocol.util.EntryDropListener;
import it.archiworld.archman.client.protocol.util.EntryTransfer;
import it.archiworld.common.protocol.Entry;
import it.archiworld.common.protocol.Inentry;
import it.archiworld.common.protocol.Outentry;

import java.util.Set;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ReferenceTable extends Composite {

	private TableViewer tableViewer;

	private Entry entry;

	private ScrolledFormEditorBase form;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Entry>) inputElement).toArray();
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
		}

	};

	public ReferenceTable(final ScrolledProtocolFormEditor form,
			final Composite parent, int style, Entry input) {
		super(parent, style);
		form.getToolkit().adapt(this);
		this.form = form;
		this.setLayout(new GridLayout(1, true));
		this.entry=input;
		final Table table = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		form.getToolkit().adapt(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addKeyListener(tableKeyListener);

		TableLayout layout = new TableLayout();
//		layout.addColumnData(new ColumnWeightData(10, 85, true));
		layout.addColumnData(new ColumnWeightData(70, 200, true));
		table.setLayout(layout);

//		TableColumn dateColumn = new TableColumn(table, SWT.BEGINNING);
//		dateColumn.setText(Messages.ReferenceTable_Column_Heading_ProtocolDate);
		TableColumn subjectColumn = new TableColumn(table, SWT.BEGINNING);
		subjectColumn.setText(Messages.ReferenceTable_Column_Heading_Subject);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.addDropSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { EntryTransfer.getInstance() },
				new EntryDropListener(form, this));
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				Entry entry = (Entry)((IStructuredSelection)event.getSelection()).getFirstElement();
				IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
				IEditorInput input = null;
				try{
					if(entry instanceof Inentry){
						input = new InProtocolEditorInput((Inentry)entry);
						page.openEditor(input, InProtocolEditor.ID);
						return;
					}
					if(entry instanceof Outentry){
						input = new OutProtocolEditorInput((Outentry)entry);
						page.openEditor(input, OutProtocolEditor.ID);
						return;
					}
				}
				catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}
			
		});
		/**
		 * Column protocol_date
		 */
/*		TableViewerColumn column = new TableViewerColumn(tableViewer,
				dateColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Entry
						&& ((Entry) element).getProtocol_date() != null)
					return DateFormat.getDateInstance(DateFormat.MEDIUM)
							.format((((Entry) element).getProtocol_date()));
				return Messages.ReferenceTable_CellLabel_ProtocolDate_IsEmpty;
			}
		});
*/
		/**
		 * Column subject
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer, subjectColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Entry) {
					return ((Entry) element).toString();
				} 
				return new String(Messages.ReferenceTable_CellLabel_Subject_IsEmpty);
			}
		});
		tableViewer.setInput(entry.getReferences());

		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button deleteButton = form.getToolkit().createButton(buttonBar,
				Messages.ReferenceTable_Button_DeleteSelectedEntries, SWT.NONE);
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

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		tableViewer.cancelEditing();
		for (Object o : selection.toList()) {
			tableViewer.remove(o);
			entry.getReferences().remove(o);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

	public void addReference(Entry reference){
		entry.getReferences().add(reference);
		tableViewer.refresh();
	};
	
	public final TableViewer getTableViewer() {
		return tableViewer;
	}

}
