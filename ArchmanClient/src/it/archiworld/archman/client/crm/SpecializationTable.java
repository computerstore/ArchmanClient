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
import it.archiworld.common.member.Specialization;

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

public class SpecializationTable extends Composite {

	private TableViewer tableViewer;

	private Member input;

	private ScrolledFormEditorBase form;

	private static final String[] DVALUE_SET = new String[]{"",//$NON-NLS-1$
	"security coordinator 494/96",//$NON-NLS-1$
	"security expert 626/94",//$NON-NLS-1$
	"fire protection list 818/4",//$NON-NLS-1$
	"inspector state register",//$NON-NLS-1$
	"static inspector",//$NON-NLS-1$
	"technical consultant of judges",//$NON-NLS-1$
	"urban expert",//$NON-NLS-1$
	"expert for landscape protection",//$NON-NLS-1$
	"employee/municipal",//$NON-NLS-1$
	"freelancer/individual",//$NON-NLS-1$
	"freelancer/firm",//$NON-NLS-1$
	"employee/state",//$NON-NLS-1$
	"employee/private",//$NON-NLS-1$
	"retired",//$NON-NLS-1$
	"free collaborator",//$NON-NLS-1$
	"university teacher",//$NON-NLS-1$
	"teacher",//$NON-NLS-1$
	"member urban committee"};//$NON-NLS-1$
	
	private static final String[] VALUE_SET = new String[]{"",//$NON-NLS-1$
		Messages.SpecializationTable_SecurityCoordinator,
		Messages.SpecializationTable_SecurityExpert,
		Messages.SpecializationTable_FireProtectionList,
		Messages.SpecializationTable_InspectorStateRegister,
		Messages.SpecializationTable_StaticInspector,
		Messages.SpecializationTable_TechnicalConsultantOfJudges,
		Messages.SpecializationTable_UrbanExpert,
		Messages.SpecializationTable_ExpertforLandscapeProtection,
		Messages.SpecializationTable_EmployeeMunicipal,
		Messages.SpecializationTable_FreelancerIndividual,
		Messages.SpecializationTable_FreelancerFirm,
		Messages.SpecializationTable_EmployeeState,
		Messages.SpecializationTable_EmployeePrivate,
		Messages.SpecializationTable_Retired,
		Messages.SpecializationTable_FreeCollaborator,
		Messages.SpecializationTable_UniversityTeacher,
		Messages.SpecializationTable_Teacher,
		Messages.SpecializationTable_MemberofUrbanCommittee};
	
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof Set) {
				return ((Set<Specialization>) inputElement).toArray();
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

	public SpecializationTable(final ScrolledFormEditorBase form,
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
		dateColumn.setText(Messages.SpecializationTable_Column_Heading_RegistrationDate);
		TableColumn specializationColumn = new TableColumn(table, SWT.BEGINNING);
		specializationColumn.setText(Messages.SpecializationTable_Column_Heading_Specialization);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(contentProvider);

		tableViewer.setSorter(new ViewerSorter(){
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (((Specialization)e1).getRegistration_date()!=null && ((Specialization)e2).getRegistration_date()!=null)
					return ((Specialization)e1).getRegistration_date().compareTo(((Specialization)e2).getRegistration_date());
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
		 * Column registration_date
		 */
		TableViewerColumn column = new TableViewerColumn(tableViewer,
				dateColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null
						&& element instanceof Specialization
						&& ((Specialization) element).getRegistration_date() != null)
					return DateFormat.getDateInstance(DateFormat.MEDIUM)
							.format(
									(((Specialization) element)
											.getRegistration_date()));
				return Messages.SpecializationTable_CellLabel_RegistrationDate_IsNull;
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {

			private CellEditor dateCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Specialization)
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
				if (element != null && element instanceof Specialization)
					return ((Specialization) element).getRegistration_date();
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Specialization) {
					Specialization specialization = (Specialization) element;
					if (value == null) {
						specialization.setRegistration_date(new Timestamp(
								new Date().getTime()));
					}
					if (value instanceof Date) {
						specialization.setRegistration_date(new Timestamp(
								((Date) value).getTime()));
						form.setDirty(true);
						getViewer().refresh(specialization);
					}
				}
			}
		});

		/**
		 * Column specialization_type
		 */
		column = new TableViewerColumn(tableViewer, specializationColumn);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element != null
						&& element instanceof Specialization
						&& ((Specialization) element).getSpecialization() != null) {
					for(int i=0;i<DVALUE_SET.length;i++)
						if(DVALUE_SET[i].equals(((Specialization) element).getSpecialization())){
							return VALUE_SET[i];
						}
				} 
				return new String(Messages.SpecializationTable_CellLabel_Specialization_IsNull);
			}
		});
		column.setEditingSupport(new EditingSupport(tableViewer) {
			private ComboBoxCellEditor textCellEditor;

			@Override
			protected boolean canEdit(Object element) {
				if (element != null && element instanceof Specialization)
					return true;
				return false;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (textCellEditor == null) {
					textCellEditor = new ComboBoxCellEditor(table,VALUE_SET,SWT.READ_ONLY);
				}
				return this.textCellEditor;
			}

			@Override
			protected Object getValue(Object element) {

				for(int i=0;i<DVALUE_SET.length;i++)
					if(DVALUE_SET[i].equals(((Specialization)element).getSpecialization().toString()))
						return Integer.valueOf(i);
				return Integer.valueOf(0);//element.toString();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null && element instanceof Specialization) {
					Specialization specialization = (Specialization) element;
					if (value == null || value instanceof Integer) {
						specialization.setSpecialization(DVALUE_SET[(Integer) value]);
						form.setDirty(true);
						getViewer().refresh(specialization);
					}
				}
			}
		});
		tableViewer.setInput(input.getSpecializations());

		Composite buttonBar = form.getToolkit().createComposite(this, SWT.NONE);
		buttonBar.setLayout(new GridLayout(2, true));

		Button addButton = form.getToolkit().createButton(buttonBar,
				Messages.SpecializationTable_Button_AddEntry, SWT.NONE);
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
				Messages.SpecializationTable_Button_DeleteSelectedEntries, SWT.NONE);
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
		Specialization specialization = new Specialization();
		specialization
				.setRegistration_date(new Timestamp(new Date().getTime()));
		specialization.setSpecialization(new String());
		input.getSpecializations().add(specialization);
		tableViewer.add(specialization);
		form.setDirty(true);
		tableViewer.refresh(specialization);
		tableViewer.editElement(specialization, 0);
	}

	public void deleteSelected() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		tableViewer.cancelEditing();
		for (Object o : selection.toList()) {
			tableViewer.remove(o);
			input.getSpecializations().remove(o);
		}
		tableViewer.refresh();
		form.setDirty(true);
	}

}
