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

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.util.CDateTimeCellEditor;
import it.archiworld.common.Member;
import it.archiworld.common.member.Formation;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
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

public class FormationTable extends Composite {

	private TableViewer tableViewer;

	private Member input;

	private ScrolledFormEditorBase form;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Formation>) inputElement).toArray();
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
			if (e.keyCode == SWT.INSERT) {
				createNew();
			}
			if (e.keyCode == SWT.DEL) {
				deleteSelected();
			}
		}

	};

	public FormationTable(final ScrolledFormEditorBase form,
			final Composite parent, int style, Member input) {
		super(parent, style);
		form.getToolkit().adapt(this);
		this.form = form;
		this.input = input;
		this.setLayout(new GridLayout(1, true));

		final Table table = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		form.getToolkit().adapt(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addKeyListener(tableKeyListener);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(10, 85, true));
		layout.addColumnData(new ColumnWeightData(70, 200, true));
		table.setLayout(layout);

		TableColumn dateColumn = new TableColumn(table, SWT.BEGINNING);
		dateColumn.setText(Messages.FormationTable_Column_Heading_Date);
		TableColumn typeColumn = new TableColumn(table, SWT.BEGINNING);
		typeColumn.setText(Messages.FormationTable_Column_Heading_Type);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);

		ViewerSupport.bind(tableViewer, PojoObservables.observeSet(input, "formations"), PojoProperties.values(new String[] {"formation_date","formation_type"}));
		
		tableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				if(((Formation)e1).getFormation_date()!=null && ((Formation)e2).getFormation_date()!=null)
					return ((Formation)e1).getFormation_date().compareTo(((Formation)e2).getFormation_date());
				return 0;
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
		 * Column formation_date
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				dateColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Formation
						&& ((Formation) element).getFormation_date() != null)
					return DateFormat
							.getDateInstance(DateFormat.MEDIUM)
							.format((((Formation) element).getFormation_date()));
				return Messages.FormationTable_CellLabel_Date_IsNull;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Formation)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (dateCellEditor == null) {
					dateCellEditor = new CDateTimeCellEditor(table);
				}
				dateCellEditor.setValue(null);
				return dateCellEditor;
			}

			@Override
			protected Object getValue(Object element) {
				if (element != null && element instanceof Formation)
					return ((Formation) element).getFormation_date();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Formation) {
					Formation formation = (Formation) element;
					if (value == null) {
						formation.setFormation_date(null);
					}
					if (value instanceof Date) {
						formation.setFormation_date(new Timestamp(
								((Date) value).getTime()));
						form.setDirty(true);
						getViewer().refresh(formation);
					}
				}
			}
		});

		/**
		 * Column formation_type
		 */
		column = new TableViewerColumn(tableViewer, typeColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Formation
						&& ((Formation) element).getFormation_type() != null) {
					return ((Formation) element).getFormation_type();
				} 
				return new String(Messages.FormationTable_CellLabel_Type_IsNull);
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Formation)
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
				if (element != null && element instanceof Formation)
					return ((Formation) element).getFormation_type();
				return null;
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Formation) {
					Formation formation = (Formation) element;
					if (value == null || value instanceof String) {
						formation.setFormation_type((String) value);
						form.setDirty(true);
						getViewer().refresh(formation);
					}
				}
			}
		});


		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button addButton = form.getToolkit().createButton(buttonBar,
				Messages.FormationTable_Button_AddEntry, SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false));
		addButton.addSelectionListener(new SelectionListener() {
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
				Messages.FormationTable_Button_DeleteSelectedEntries, SWT.NONE);
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
		tableViewer.cancelEditing();
		Formation formation = new Formation();
		formation.setFormation_date(new Timestamp(new Date().getTime()));
		formation.setFormation_type(new String());
		input.getFormations().add(formation);
		tableViewer.add(formation);
		form.setDirty(true);
		tableViewer.refresh(formation);
		tableViewer.editElement(formation, 0);
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		tableViewer.cancelEditing();
		for (Object o : selection.toList()) {
			tableViewer.remove(o);
			input.getFormations().remove(o);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

}
