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


import it.archiworld.archman.client.core.util.DateConverter;
import it.archiworld.archman.client.crm.CompanyEditor;
import it.archiworld.archman.client.crm.CompanyEditorInput;
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.archman.client.crm.MemberEditorInput;
import it.archiworld.archman.client.crm.PersonEditor;
import it.archiworld.archman.client.crm.PersonEditorInput;
import it.archiworld.archman.client.crm.ServiceMemberEditor;
import it.archiworld.archman.client.crm.ServiceMemberEditorInput;
import it.archiworld.archman.client.protocol.util.AddressDropListener;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.ServiceMember;
import it.archiworld.common.protocol.Document;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.Outentry;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;


public class OutProtocolEditor extends ScrolledProtocolFormEditor {

	public static String ID = "it.archiworld.archman.client.protocol.outprotocoleditor"; //$NON-NLS-1$

	protected Outentry entry;
	private DocumentsTable documentsTable;
	private ReferenceTable referenceTable;
	// private TableViewer followupsList;
	private TableViewer receiverViewer;

	private List<EntryAddress> destinations;

	@Override
	public void doSave(IProgressMonitor monitor) {
		
		for (Document doc : entry.getDocuments()) {
			System.out.println(doc);
		}
		try {
			if (!this.authenticate()){
				return;
			}
			entry.setOwner(user);
			entry.setLast_change_date(new Timestamp(System.currentTimeMillis()));
			
			System.out.println(entry.getArchive());
			OutProtocolEditorInput input = (OutProtocolEditorInput) getEditorInput();

			entry = (Outentry)input.createOrUpdate();
			
			this.destinations = entry.getDestinations();
			
			if(this.destinations==null || this.destinations.isEmpty()){
				MessageDialog.openInformation(this.getSite().getShell(),
						Messages.OutProtocolEditor_SaveFailureDialog_Heading,
						Messages.OutProtocolEditor_Error_CanNotSaveOutgoingProtocolEntryWithoutDestination);
				return;
			}
			
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.OutProtocolEditor_SaveSuccessDialog_Heading,
					Messages.OutProtocolEditor_SaveSuccessDialog_Text+"\n"+(entry.toString())); //$NON-NLS-1$
			setDirty(false);
			setPartName(input.getName());
			
//Don't close the editor, because export to word only works with saved entry
/*			getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(
					this, false);
			AddOutProtocolActionDelegate action = new AddOutProtocolActionDelegate();
			action.init(this.getSite().getWorkbenchWindow());
			action.run(null);
*/
			
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.OutProtocolEditor_SaveFailureDialog_Heading,
					Messages.OutProtocolEditor_SaveFailureDialog_Text);
		}
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.entry = (Outentry) ((OutProtocolEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		String name="";
		if (entry.getEmergency()){
			name=name.concat(" "+Messages.ProtocolEditor_Header_Emergency_Marker); //$NON-NLS-1$
//			form.setBackground(new Color(header.getDisplay(),new RGB(255,125,125)));
			form.setForeground(new Color(header.getDisplay(),new RGB(200,0,0)));
		}
		
		form.setText(Messages.OutProtocolEditor_Form_Title+ " " + name); //$NON-NLS-1$
		header.setLayout(new GridLayout(1, true));

		// createSection(null, null, Section.NO_TITLE);

		Label subjectLabel = toolkit.createLabel(header,
				Messages.OutProtocolEditor_Label_Subject);
		GridData subjectLabelGd = new GridData(SWT.BEGINNING, SWT.END, false,
				false);
		subjectLabel.setLayoutData(subjectLabelGd);
		subjectText = toolkit.createText(header, null, SWT.BORDER);
		GridData subjectTextGd = new GridData(SWT.FILL, SWT.BEGINNING, true,
				false);
		subjectText.setLayoutData(subjectTextGd);
		dataBindingContext.bindValue(
				SWTObservables.observeText(subjectText, SWT.Modify),
				PojoObservables.observeValue(entry, "subject"), null, null) //$NON-NLS-1$
				.getTarget().addChangeListener(dirtyListener);
	}

	@Override
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.OutProtocolEditor_Tab_Label_General,
				new GridLayout(2, true));

		createSection(Messages.OutProtocolEditor_Section_Heading_Protocol,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);
		createArchiveDropDown(entry);
		createResponsibleDropDown(entry);
		//TODO changeString and field representation

		if(entry.getEmergency()!=null && entry.getEmergency())
			createLabeledText(Messages.OutProtocolEditor_Label_EntryId, entry, "entry_id"); //$NON-NLS-1$
		else
			createLabeledReadOnly(Messages.OutProtocolEditor_Label_EntryId, null, entry, "entry_id"); //$NON-NLS-1$

		if(entry.getEmergency()!=null && entry.getEmergency())
			createLabeledText(Messages.OutProtocolEditor_Label_ProtocolId, entry, "protocol"); //$NON-NLS-1$
		else
			createLabeledReadOnly(Messages.OutProtocolEditor_LabelYear, null,entry, "year"); //$NON-NLS-1$

		createLabeledReadOnly(Messages.OutProtocolEditor_Label_ProtocolId,
				null, entry, "protocol"); //$NON-NLS-1$
		createLabeledReadOnly(Messages.OutProtocolEditor_Label_Owner, null,
				entry, "owner"); //$NON-NLS-1$
		createLabeledReadOnly(Messages.OutProtocolEditor_Label_State, null,
				entry, "state"); //$NON-NLS-1$
		createLabeledReadOnlyTimestamp(Messages.OutProtocolEditor_Label_RegistrationDate,
				DateConverter.TIMESTAMP, DateFormat.LONG, DateFormat.SHORT, entry,
				"registration_date"); //$NON-NLS-1$
		createDateField(Messages.OutProtocolEditor_Label_ProtocolDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM
						 | CDT.BORDER, entry,
				"protocol_date"); //$NON-NLS-1$
		//createLabeledText("Archive", entry, "archive");
		Label receiverLabel = toolkit.createLabel(activeSection,
				Messages.OutProtocolEditor_Label_Receiver);
		receiverLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));
		GridData avGd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		avGd.heightHint = 80;
		avGd.widthHint = 200;
		Table receiverList = new Table(activeSection, SWT.BORDER|SWT.SCROLL_LINE);
		receiverList
				.setLayoutData(avGd);
		this.receiverViewer = new TableViewer(receiverList);
		this.receiverViewer.addDropSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { AddressTransfer.getInstance() },
				new AddressDropListener(this, this.receiverViewer));
		this.receiverViewer
				.setContentProvider(new IStructuredContentProvider() {
					@Override
					public Object[] getElements(Object inputElement) {
						if (inputElement != null && inputElement instanceof java.util.List) {
							java.util.List<Outentry> input = (java.util.List<Outentry>) inputElement;
							return input.toArray();
						}
						return null;
					}

					@Override
					public void inputChanged(Viewer viewer, Object oldInput,
							Object newInput) {
						viewer.refresh();
					}

					@Override
					public void dispose() {
						;
					}
				});
		this.receiverViewer.setLabelProvider(new ITableLabelProvider(){

			private Image personImage = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/user.png").createImage(); //$NON-NLS-1$
			private Image memberImage_male = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/vcard.png").createImage(); //$NON-NLS-1$
			private Image memberImage_female = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/vcard_female.png").createImage(); //$NON-NLS-1$
			private Image companyImage = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/building.png").createImage(); //$NON-NLS-1$
			private Image serviceMemberImage = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/tux.png").createImage(); //$NON-NLS-1$
	
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return getImage(element);
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				return element.toString();
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				
			}
			
			public Image getImage(final Object obj) {
				final Address addr = ((EntryAddress)obj).getAddress();
				if (obj instanceof String) {
					return PlatformUI.getWorkbench().getSharedImages().getImage(
							ISharedImages.IMG_OBJ_FOLDER);
				} else if (addr instanceof Member) {
					if (((Member)addr).getGender()!=null && ((Member)addr).getGender().equals("female")) //$NON-NLS-1$
						return memberImage_female;
					else 
						return memberImage_male;	
				} else if (addr instanceof ServiceMember) {
					return serviceMemberImage;
				} else if (addr instanceof Person) {
					return personImage;
				} else if (addr instanceof Company) {
					return companyImage;
				} else {
					return null;
				}
			}
			
		});
		receiverList.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				IStructuredSelection selection = (IStructuredSelection) receiverViewer
						.getSelection();
				if (e.keyCode == SWT.DEL) {
					for (Object o : selection.toList()) {
						System.out.println("oclass"+o.getClass());
						entry.getDestinations().remove(o);
					}
					receiverViewer.refresh();
					setDirty(true);
				}
			}
		});
		receiverViewer.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				
				Address address=((EntryAddress)((IStructuredSelection)event.getSelection()).getFirstElement()).getAddress();
				IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
				IEditorInput input=null;
				try{
					if(address instanceof Member){
						input = new MemberEditorInput((Member)address);
						page.openEditor(input, MemberEditor.ID);
						return;
					}
					if(address instanceof ServiceMember){
						input = new ServiceMemberEditorInput((ServiceMember)address);
						page.openEditor(input, ServiceMemberEditor.ID);
						return;
					}
					if(address instanceof Company){
						input = new CompanyEditorInput((Company)address);
						page.openEditor(input, CompanyEditor.ID);
						return;
					}
					if(address instanceof Person){
						input = new PersonEditorInput((Person)address);
						page.openEditor(input, PersonEditor.ID);
						return;
					}	
				}
				catch (PartInitException ex) {
					ex.printStackTrace();
				}

			}
			
		});

		this.destinations = ((Outentry) entry).getDestinations();
		receiverViewer.setInput(destinations);

		createSection(Messages.OutProtocolEditor_Section_Heading_Note,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledMultiText(Messages.OutProtocolEditor_Label_Note, entry,
				"note"); //$NON-NLS-1$

		createTabItem(Messages.OutProtocolEditor_Tab_Label_Documents,
				new GridLayout(1, true));

		documentsTable = new DocumentsTable(this, activePart, SWT.NONE,
				entry);

		createTabItem(Messages.OutProtocolEditor_Tab_Label_References,
				new GridLayout(1, true));

		referenceTable = new ReferenceTable(this, activeSection, SWT.NONE,
				entry);

		// createTabItem("Follow ups", new GridLayout(1,true));

	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

	public final java.util.List<EntryAddress> getDestinations() {
		return destinations;
	}
	
	public void addDestination(EntryAddress address){
		this.destinations.add(address);
		receiverViewer.refresh();
	}

	@Override
	public void setFocus(){
		subjectText.setFocus();
	}
}
