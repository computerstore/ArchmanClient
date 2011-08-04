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

import it.archiworld.archman.client.committee.util.AddressDropListener;
import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.util.DatePickerComboCellEditor;
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.archman.client.crm.MemberEditorInput;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.committee.Committeemember;
import it.archiworld.common.committee.Committee;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class CommitteeMemberTable extends Composite {

	private TableViewer tableViewer;

	private List<Committeemember> committeeMemberList;

	private ScrolledFormEditorBase form;
	
	private static final String[] DNOMINATION_TYPE_VALUE_SET = new String[] { "", "permanent", "substitute" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static final String[] NOMINATION_TYPE_VALUE_SET = new String[] { "", Messages.CommitteeMemberTable_nomination_permanent, Messages.CommitteeMemberTable_nomination_substitute }; //$NON-NLS-1$
	
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof List) {
				return ((List<Committeemember>) inputElement).toArray();
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

	public CommitteeMemberTable(final CommitteeEditor form,
			final Composite parent, int style, Committee input) {
		super(parent, style);
		form.getToolkit().adapt(this);
		this.form = form;
		this.committeeMemberList = input.getMember();
		this.setLayout(new GridLayout(1, true));

		final Table table = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		form.getToolkit().adapt(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addKeyListener(tableKeyListener);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(10, 40, true));
		layout.addColumnData(new ColumnWeightData(10, 40, true));
		layout.addColumnData(new ColumnWeightData(10, 40, true));
		layout.addColumnData(new ColumnWeightData(10, 40, true));
		layout.addColumnData(new ColumnWeightData(10, 40, true));
		table.setLayout(layout);

		TableColumn memberColumn = new TableColumn(table, SWT.BEGINNING);
		memberColumn.setText(Messages.CommitteeMemberTable_ColumnLabelMember);
		TableColumn startColumn = new TableColumn(table, SWT.BEGINNING);
		startColumn.setText(Messages.CommitteeMemberTable_ColumnLabelStart);
		TableColumn stopColumn = new TableColumn(table, SWT.BEGINNING);
		stopColumn.setText(Messages.CommitteeMemberTable_ColumnLabelStop);
		TableColumn roleColumn = new TableColumn(table, SWT.BEGINNING);
		roleColumn.setText(Messages.CommitteeMemberTable_ColumnLabelRole);
		TableColumn nominationTypeColumn = new TableColumn(table, SWT.BEGINNING);
		nominationTypeColumn
				.setText(Messages.CommitteeMemberTable_ColumnLabelNominationType);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.addDropSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { AddressTransfer.getInstance() },
				new AddressDropListener(form, this));
		tableViewer.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				System.out.println(((IStructuredSelection)event.getSelection()).getFirstElement().getClass());
				MemberEditorInput input = new MemberEditorInput(((Committeemember)((IStructuredSelection)event.getSelection()).getFirstElement()).getMember());
				try{
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, MemberEditor.ID);
				}
				catch(PartInitException e){
					e.printStackTrace();
				}
			}
			
		});

		TableViewerEditor.create(tableViewer, new TableViewerFocusCellManager(
				tableViewer, new FocusCellOwnerDrawHighlighter(tableViewer)),
				new ColumnViewerEditorActivationStrategy(tableViewer) {
					@Override
					protected boolean isEditorActivationEvent(
							ColumnViewerEditorActivationEvent event) {
						return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
								|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
								|| (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
								|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
					}
				}, ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		/**
		 * Column member
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				memberColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Committeemember
						&& ((Committeemember) element).getMember() != null) {
					return ((Committeemember) element).getMember().toString();
				}
				return Messages.CommitteeMemberTable_ValueNotEntered;
			}
		});
		/**
		 * Column start
		 */
		column = new TableViewerColumn(tableViewer, startColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Committeemember
						&& ((Committeemember) element).getStart() != null)
					return DateFormat.getDateInstance(DateFormat.MEDIUM)
							.format((((Committeemember) element).getStart()));
				return Messages.CommitteeMemberTable_ValueNotEntered;
			}
		});

		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Committeemember)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (dateCellEditor == null) {
					dateCellEditor = new DatePickerComboCellEditor(table);
				}
				dateCellEditor.setValue(new Date());
				return dateCellEditor;
			}

			@Override
			protected Object getValue(Object element) {
				if (element != null && element instanceof Committeemember)
					return ((Committeemember) element).getStart();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Committeemember) {
					Committeemember committeemember = (Committeemember) element;
					if (value == null) {
						committeemember.setStart(new Timestamp(new Date()
								.getTime()));
					}
					if (value instanceof Date) {
						committeemember.setStart(new Timestamp(((Date) value)
								.getTime()));
						form.setDirty(true);
						getViewer().refresh(committeemember);
					}
				}
			}
		});

		/**
		 * Column stop
		 */
		column = new TableViewerColumn(tableViewer, stopColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Committeemember
						&& ((Committeemember) element).getStop() != null)
					return DateFormat.getDateInstance(DateFormat.MEDIUM)
							.format((((Committeemember) element).getStop()));
				return Messages.CommitteeMemberTable_ValueNotEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Committeemember)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (dateCellEditor == null) {
					dateCellEditor = new DatePickerComboCellEditor(table);
				}
				dateCellEditor.setValue(new Date());
				return dateCellEditor;
			}

			@Override
			protected Object getValue(Object element) {
				if (element != null && element instanceof Committeemember)
					return ((Committeemember) element).getStop();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Committeemember) {
					Committeemember committeemember = (Committeemember) element;
					if (value == null) {
						committeemember.setStop(new Timestamp(new Date()
								.getTime()));
					}
					if (value instanceof Date) {
						committeemember.setStop(new Timestamp(((Date) value)
								.getTime()));
						form.setDirty(true);
						getViewer().refresh(committeemember);
					}
				}
			}
		});

		/**
		 * Column role
		 */
		column = new TableViewerColumn(tableViewer, roleColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Committeemember
						&& ((Committeemember) element).getRole() != null) {
							return ((Committeemember) element).getRole();
						}				
				return Messages.CommitteeMemberTable_ValueNotEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Committeemember)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (textCellEditor == null) {
					textCellEditor = new TextCellEditor(table);
				}
				textCellEditor.setValue(new String());
				return this.textCellEditor;
			}

			@Override
			protected Object getValue(Object element) {
				if(element!=null && element instanceof Committeemember)
						return ((Committeemember) element).getRole();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Committeemember) {
					Committeemember committeemember = (Committeemember) element;
					if (value == null || value instanceof String) {
						committeemember
								.setRole((String) value);
						form.setDirty(true);
						getViewer().refresh(committeemember);
					}
				}
			}
		});

		/**
		 * Column nominationType
		 */
		column = new TableViewerColumn(tableViewer, nominationTypeColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null
						&& element instanceof Committeemember
						&& ((Committeemember) element).getNomination_type() != null) {
					for(int i=0;i<DNOMINATION_TYPE_VALUE_SET.length;i++)
						if(DNOMINATION_TYPE_VALUE_SET[i].equals(((Committeemember) element).getNomination_type())){
							return NOMINATION_TYPE_VALUE_SET[i];
						}
				}
				return Messages.CommitteeMemberTable_ValueNotEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private ComboBoxCellEditor textCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Committeemember)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (textCellEditor == null) {
					textCellEditor = new ComboBoxCellEditor(table,
							NOMINATION_TYPE_VALUE_SET, SWT.READ_ONLY);
				}
				return this.textCellEditor;
			}

			@Override
			protected Object getValue(Object element) {
				if(element!=null)
					for (int i = 0; i < DNOMINATION_TYPE_VALUE_SET.length; i++)
						if (DNOMINATION_TYPE_VALUE_SET[i].equals(((Committeemember) element)
							.getNomination_type().toString()))
						return Integer.valueOf(i);
				return Integer.valueOf(0);// element.toString();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Committeemember) {
					Committeemember committeemember = (Committeemember) element;
					if (value == null || value instanceof Integer) {
						committeemember
								.setNomination_type(DNOMINATION_TYPE_VALUE_SET[(Integer) value]);
						form.setDirty(true);
						getViewer().refresh(committeemember);
					}
				}
			}
		});

		tableViewer.setInput(committeeMemberList);

		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button deleteButton = form.getToolkit().createButton(buttonBar,
				Messages.CommitteeMemberTable_ButtonLabelDelete, SWT.NONE);
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
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		tableViewer.cancelEditing();
		for (Object o : selection.toList()) {
			tableViewer.remove(o);
			committeeMemberList.remove(o);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

	public final List<Committeemember> getCommitteeMemberList() {
		return committeeMemberList;
	}

	public void setCommitteeMemberList(List<Committeemember> committeeMemberList) {
		this.committeeMemberList = committeeMemberList;
		this.tableViewer.setInput(committeeMemberList);
	}

	public final TableViewer getTableViewer() {
		return tableViewer;
	}

}
