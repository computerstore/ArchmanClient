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
import it.archiworld.archman.client.core.util.DatePickerComboCellEditor;
import it.archiworld.common.ServiceMember;
import it.archiworld.common.servicemember.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

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

public class ServiceTable extends Composite {

	private TableViewer tableViewer;

	private ServiceMember input;

	private ScrolledFormEditorBase form;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Service>) inputElement).toArray();
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

	public ServiceTable(final ScrolledFormEditorBase form,
			final Composite parent, int style, ServiceMember input) {
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
		layout.addColumnData(new ColumnWeightData(10, 50, true));
		layout.addColumnData(new ColumnWeightData(10, 50, true));
		layout.addColumnData(new ColumnWeightData(40, 150, true));		
		layout.addColumnData(new ColumnWeightData(10, 50, true));
		layout.addColumnData(new ColumnWeightData(50, 50, true));
		table.setLayout(layout);

		TableColumn startColumn = new TableColumn(table, SWT.BEGINNING);
		startColumn.setText(Messages.ServiceTable_ColumnLabelStart);
		TableColumn stopColumn = new TableColumn(table, SWT.BEGINNING);
		stopColumn.setText(Messages.ServiceTable_ColumnLabelStop);
		TableColumn serviceTypeColumn = new TableColumn(table, SWT.BEGINNING);
		serviceTypeColumn.setText(Messages.ServiceTable_ColumnLabelType);
		TableColumn locationColumn = new TableColumn(table, SWT.BEGINNING);
		locationColumn.setText(Messages.ServiceTable_ColumnLabelLocation);
		TableColumn meetingColumn = new TableColumn(table, SWT.BEGINNING);
		meetingColumn.setText(Messages.ServiceTable_ColumnLabelMeeting);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);

		tableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				if(((Service)e1).getStart()!=null && ((Service)e2).getStart()!=null)
					return ((Service)e1).getStart().compareTo(((Service)e2).getStart());
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
		 * Column service start
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				startColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Service
						&& ((Service) element).getStart() != null)
					return DateFormat
							.getDateInstance(DateFormat.MEDIUM)
							.format((((Service) element).getStart()));
			return Messages.ServiceTable_ColumnLabelNoDateEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Service)
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
				if (element != null && element instanceof Service)
					return ((Service) element).getStart();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Service) {
					Service service = (Service) element;
					if (value == null) {
						service.setStart(new Timestamp(new Date()
								.getTime()));
					}
					if (value instanceof Date) {
						service.setStart(new Timestamp(
								((Date) value).getTime()));
						form.setDirty(true);
						getViewer().refresh(service);
					}
				}
			}
		});

		/**
		 * Column stop
		 */
		column = new TableViewerColumn(tableViewer,
				stopColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Service
						&& ((Service) element).getStop() != null)
					return DateFormat
							.getDateInstance(DateFormat.MEDIUM)
							.format((((Service) element).getStop()));
			return Messages.ServiceTable_ColumnLabelNoDateEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Service)
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
				if (element != null && element instanceof Service)
					return ((Service) element).getStop();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Service) {
					Service service = (Service) element;
					if (value == null) {
						service.setStop(new Timestamp(new Date()
								.getTime()));
					}
					if (value instanceof Date) {
						service.setStop(new Timestamp(
								((Date) value).getTime()));
						form.setDirty(true);
						getViewer().refresh(service);
					}
				}
			}
		});

		/**
		 * Column type
		 */
		column = new TableViewerColumn(tableViewer, serviceTypeColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Service
						&& ((Service) element).getServicetype() != null) {
					return ((Service) element).getServicetype();
				} 
				return new String(Messages.ServiceTable_ColumnLabelNoServicetypeDefined);
				
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Service)
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
				if (element != null && element instanceof Service)
					return ((Service) element).getServicetype();
				return null;
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Service) {
					Service service = (Service) element;
					if (value == null || value instanceof String) {
						service.setServicetype((String) value);
						form.setDirty(true);
						getViewer().refresh(service);
					}
				}
			}
		});

		
		/**
		 * Column location
		 */
		column = new TableViewerColumn(tableViewer, locationColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Service
						&& ((Service) element).getServicelocation() != null) {
					return ((Service) element).getServicelocation();
				} 
				return new String(Messages.ServiceTable_ColumnLabelServiceLocationNotDefined);
				
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Service)
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
				if (element != null && element instanceof Service)
					return ((Service) element).getServicelocation();
				return null;
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Service) {
					Service service = (Service) element;
					if (value == null || value instanceof String) {
						service.setServicelocation((String) value);
						form.setDirty(true);
						getViewer().refresh(service);
					}
				}
			}
		});

		/**
		 * Column exemption_type
		 */
		column = new TableViewerColumn(tableViewer, meetingColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Service
						&& ((Service) element).getMeeting() != null) {
					return ((Service) element).getMeeting();
				} 
				return new String(Messages.ServiceTable_ColumnLabelExemptionNotDefined);
				
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Service)
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
				if (element != null && element instanceof Service)
					return ((Service) element).getMeeting();
				return null;
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Service) {
					Service service = (Service) element;
					if (value == null || value instanceof String) {
						service.setMeeting((String) value);
						form.setDirty(true);
						getViewer().refresh(service);
					}
				}
			}
		});

		
		tableViewer.setInput(input.getServices());

		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button addButton = form.getToolkit().createButton(buttonBar,
				Messages.ServiceTable_ButonLabelAdd, SWT.NONE);
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
				Messages.ServiceTable_ButtonLabelDelete, SWT.NONE);
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
		Service service = new Service();
		service.setService_id(0);
		service.setService_nr(0);
		service.setServicelocation(new String());
		service.setServicetype(new String());
		service.setStart(new Timestamp(System.currentTimeMillis()));
		service.setStop(new Timestamp(System.currentTimeMillis()));
		service.setMeeting(new String());
		input.getServices().add(service);
		form.setDirty(true);
		tableViewer.refresh();
		tableViewer.editElement(service, 0);
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		tableViewer.cancelEditing();
		for (Object o : selection.toList()) {
			tableViewer.remove(o);
			input.getServices().remove(o);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

}
