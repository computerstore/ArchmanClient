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
import it.archiworld.common.Member;
import it.archiworld.common.member.Renewal;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.internal.databinding.swt.TextEditableProperty;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
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

import de.ralfebert.rcputils.tables.ColumnBuilder;
import de.ralfebert.rcputils.tables.TableViewerBuilder;

public class RenewalTable extends Composite {

	private TableViewer tableViewer;

	private Member input;

//	private Set<Renewal> renewalSet;

	private ScrolledFormEditorBase form;

	private static final String[] DVALUE_SET = new String[]{"", "bank", "cash", "check", "post"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	private static final String[] VALUE_SET = new String[]{"", Messages.RenewalTable_Bank, Messages.RenewalTable_Cash, Messages.RenewalTable_Check, Messages.RenewalTable_Post}; //$NON-NLS-1$

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {
		
		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Renewal>) inputElement).toArray();
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

	public RenewalTable(final ScrolledFormEditorBase form,
			final Composite parent, int style, Member input) {
		super(parent, style);
		form.getToolkit().adapt(this);
		this.form = form;
		this.input = input;
//		this.renewalSet = input.getRenewal();
		this.setLayout(new GridLayout(1, true));

		final Table table = new Table(parent, SWT.MULTI | SWT.FULL_SELECTION
				| SWT.BORDER);
		form.getToolkit().adapt(table);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.addKeyListener(tableKeyListener);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(10, 85, true));
		layout.addColumnData(new ColumnWeightData(10, 85, true));		
		layout.addColumnData(new ColumnWeightData(60, 200, true));
		table.setLayout(layout);

		TableColumn dateColumn = new TableColumn(table, SWT.BEGINNING);
		dateColumn.setText(Messages.RenewalTable_ColumnLabel_RenewalDate);
		TableColumn yearColumn = new TableColumn(table, SWT.BEGINNING);
		yearColumn.setText(Messages.RenewalTable_ColumnLabel_Year);
		TableColumn typeColumn = new TableColumn(table, SWT.BEGINNING);
		typeColumn.setText(Messages.RenewalTable_ColumnLabel_PaymentType);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);

		ViewerSupport.bind(tableViewer, PojoObservables.observeSet(input, "renewal"), PojoProperties.values(new String[] {"renewal_date","year","paymenttype"}));
		tableViewer.setContentProvider(contentProvider);	

		tableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				if(((Renewal)e1).getYear()!=null && ((Renewal)e2).getYear()!=null)
					return ((Renewal)e1).getYear().compareTo(((Renewal)e2).getYear());
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
		 * Column renewal_date
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				dateColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Renewal
						&& ((Renewal) element).getRenewal_date() != null)
					return DateFormat
					.getDateInstance(DateFormat.MEDIUM)
					.format((((Renewal) element).getRenewal_date()));
				return Messages.RenewalTable_NoDateEntered;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Renewal)
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
				if (element != null && element instanceof Renewal)
					return ((Renewal) element).getRenewal_date();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Renewal) {
					Renewal renewal = (Renewal) element;
					if (value == null) {
						renewal.setRenewal_date(new Timestamp(new Date()
						.getTime()));
					}
					if (value instanceof Date) {
						renewal.setRenewal_date(new Timestamp(
								((Date) value).getTime()));
						form.setDirty(true);
						getViewer().refresh(renewal);
					}
				}
			}
		});

		/**
		 * Column year
		 */
		column = new TableViewerColumn(tableViewer, yearColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Renewal
						&& ((Renewal) element).getYear() != null) {
					return ((Renewal) element).getYear();
				} 
				return new String(Messages.RenewalTable_StringNotDefined);

			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Renewal)
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
				if (element != null && element instanceof Renewal)
					return ((Renewal) element).getYear();
				return null;
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Renewal) {
					Renewal renewal = (Renewal) element;
					if (value == null || value instanceof String) {
						renewal.setYear((String) value);
						form.setDirty(true);
						getViewer().refresh(renewal);
					}
				}
			}
		});

		/**
		 * Column payment_type
		 */
		column = new TableViewerColumn(tableViewer, typeColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null && element instanceof Renewal
						&& ((Renewal) element).getPaymenttype() != null) {
					for(int i=0;i<DVALUE_SET.length;i++)
						if(DVALUE_SET[i].equals(((Renewal) element).getPaymenttype())){
							return VALUE_SET[i];
						}
				} 
				return new String(Messages.RenewalTable_StringNotDefined);

			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private CellEditor textCellEditor;
			@Override
			protected boolean canEdit(Object element) {
//				if (element != null && element instanceof Renewal)
					return true;
//				return false;
			}
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (textCellEditor == null) {
					//textCellEditor = new TextCellEditor(table);
					textCellEditor = new ComboBoxCellEditor(table,VALUE_SET,SWT.READ_ONLY);					
				}
				//textCellEditor.setValue(new String());
				return this.textCellEditor;
			}
			@Override
			protected Object getValue(Object element) {
//				if (element != null && element instanceof Renewal)
//					return ((Renewal) element).getPaymenttype();
				for(int i=0;i<DVALUE_SET.length;i++)
					if(DVALUE_SET[i].equals(((Renewal)element).getPaymenttype().toString()))
						return Integer.valueOf(i);
				return Integer.valueOf(0);//element.toString();
			}
			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Renewal) {
					Renewal renewal = (Renewal) element;
					if (value == null || value instanceof Integer) {
						renewal.setPaymenttype(DVALUE_SET[(Integer)value]);
						form.setDirty(true);
						getViewer().refresh(renewal);
					}
				}
	
			}
		});


		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button addButton = form.getToolkit().createButton(buttonBar,
				Messages.RenewalTable_Button_Add, SWT.NONE);
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
				Messages.RenewalTable_Button_Delete, SWT.NONE);
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

/*		
		TableViewerBuilder tvb = new TableViewerBuilder(new Composite(parent, style));
		tvb.setInput(input.getRenewal());
		ColumnBuilder renewal_date_builder = tvb.createColumn("renewal_date");
		renewal_date_builder.bindToProperty("renewal_date");
		renewal_date_builder.makeEditable(new DatePickerComboCellEditor(tvb.getTable()));
		renewal_date_builder.build();
		ColumnBuilder date_builder = tvb.createColumn("date");
		date_builder.bindToProperty("year");
		date_builder.makeEditable(new TextCellEditor(tvb.getTable()));
		date_builder.useAsDefaultSortColumn();
		date_builder.build();
		ColumnBuilder payment_type_builder = tvb.createColumn("paymenttype");
		payment_type_builder.bindToProperty("paymenttype");
		payment_type_builder.makeEditable(new ComboBoxCellEditor(tvb.getTable(),VALUE_SET,SWT.READ_ONLY));
		payment_type_builder.build();
*/	
	}

	public void createNew() {
		tableViewer.cancelEditing();
		Renewal renewal = new Renewal();
		
//		renewal.setMember((Member)input);
		renewal.setRenewal_date(new Timestamp(new Date().getTime()));
		renewal.setYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		renewal.setPaymenttype("bank"); //$NON-NLS-1$
		
		input.getRenewal().add(renewal);
		tableViewer.add(renewal);
		form.setDirty(true);
		tableViewer.refresh(renewal);
		tableViewer.editElement(renewal, 0);
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
		.getSelection();
        if (selection.isEmpty())
        	return;
		for (Iterator<Renewal> iterator = selection.iterator(); iterator.hasNext();)
			input.getRenewal().remove(iterator.next());
        tableViewer.refresh();
	}

}
