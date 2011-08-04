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

import it.archiworld.archman.client.core.util.AddressViewer;
import it.archiworld.archman.client.core.util.AddressViewerSWTObservable;
import it.archiworld.archman.client.core.util.DateConverter;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.Inentry;

import java.sql.Timestamp;
import java.text.DateFormat;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class InProtocolEditor extends ScrolledProtocolFormEditor {

	public static String ID = "it.archiworld.archman.client.protocol.inprotocoleditor"; //$NON-NLS-1$

	protected Inentry entry;
	private DocumentsTable documentsTable;
	private ReferenceTable referenceTable;
	private AddressViewer senderViewer;

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			if (!this.authenticate()){
				return;  
			}
			if(entry.getSender()==null){
				MessageDialog.openInformation(this.getSite().getShell(),
						Messages.InProtocolEditor_SaveFailureDialog_Heading,
						Messages.InProtocolEditor_Error_CanNotSaveIncomingProtocolEntryWithoutSender);
				return;
			}
			entry.setOwner(user);
			entry.setLast_change_date(new Timestamp(System.currentTimeMillis()));
			
			InProtocolEditorInput input = (InProtocolEditorInput) getEditorInput();
			entry = (Inentry)input.createOrUpdate();

			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.InProtocolEditor_SaveSuccessDialog_Heading,
					Messages.InProtocolEditor_SaveSuccessDialog_Text+"\n"+entry.toString()); //$NON-NLS-1$
			setDirty(false);
			setPartName(input.getName());
			this.populateForm();
			getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(
					this, false);

			Display.getCurrent().asyncExec(new Runnable(){

				@Override
				public void run() {
					AddInProtocolActionDelegate action = new AddInProtocolActionDelegate();
					action.init(getEditorSite().getWorkbenchWindow());
					action.run(null);					
				}
				
			});
		} catch (Throwable t) {
			t.printStackTrace();
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.InProtocolEditor_SaveFailureDialog_Heading,
					Messages.InProtocolEditor_SaveFailureDialog_Text);
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.entry = (Inentry) ((InProtocolEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		String name="";
		System.out.println("emergency:"+entry.getEmergency());
		if (entry.getEmergency()){
			name=name.concat(" "+Messages.ProtocolEditor_Header_Emergency_Marker); //$NON-NLS-1$
//			form.setBackground(new Color(header.getDisplay(),new RGB(255,125,125)));
			form.setForeground(new Color(header.getDisplay(),new RGB(200,0,0)));
		}
		form.setText(Messages.InProtocolEditor_Form_Title+ " " + name); //$NON-NLS-1$
		header.setLayout(new GridLayout(1, true));

		Label subjectLabel = toolkit.createLabel(header,
				Messages.InProtocolEditor_Label_Subject);
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
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));

		createTabItem(Messages.InProtocolEditor_Tab_Label_General,
				new GridLayout(2, true));

		createSection(Messages.InProtocolEditor_Section_Heading_Protocol,
					Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);
		createArchiveDropDown(entry);
//		createResponsibleDropDown(entry);
		//TODO changeString and field representation
		if(entry.getEmergency()!=null && entry.getEmergency())
			createLabeledText(Messages.InProtocolEditor_Label_EntryId, entry, "entry_id"); //$NON-NLS-1$
		else
			createLabeledReadOnly(Messages.InProtocolEditor_Label_EntryId, null, entry, "entry_id"); //$NON-NLS-1$
		if(entry.getEmergency()!=null && entry.getEmergency())
			createLabeledText(Messages.InProtocolEditor_Label_ProtocolId, entry, "protocol"); //$NON-NLS-1$
		else
			createLabeledReadOnly(Messages.InProtocolEditor_Label_ProtocolId, null,	entry, "protocol"); //$NON-NLS-1$
		
		createLabeledReadOnly(Messages.InProtocolEditor_Label_Owner, null,
				entry, "owner"); //$NON-NLS-1$
//		createLabeledReadOnly(Messages.InProtocolEditor_Label_State, null,
//				entry, "state"); //$NON-NLS-1$
		createLabeledReadOnlyTimestamp(Messages.InProtocolEditor_Label_RegistrationDate,
				DateConverter.TIMESTAMP, DateFormat.LONG, DateFormat.SHORT, entry,
				"registration_date"); //$NON-NLS-1$
		createDateField(Messages.InProtocolEditor_Label_ProtocolDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM
						| CDT.BORDER, entry, "protocol_date"); //$NON-NLS-1$
		createDateField(Messages.InProtocolEditor_Label_RecievedDate,
				CDT.DROP_DOWN | CDT.TAB_FIELDS | CDT.DATE_MEDIUM
						| CDT.BORDER, entry, "recived_date"); //$NON-NLS-1$
		// createLabeledText("Receieve type:", inentry, "receieve_type"); //
		// Missing getters and setters in entity-bean.
		//createLabeledText("Archive", inentry, "archive");
		Label dropLabelTestLabel = toolkit.createLabel(activeSection,
				Messages.InProtocolEditor_Label_Sender, SWT.NONE);
		dropLabelTestLabel.setLayoutData(new GridData(SWT.BEGINNING,
				SWT.BEGINNING, false, false));
		senderViewer = new AddressViewer(activeSection, toolkit,
				Messages.InProtocolEditor_AddressViewer_Sender_InitialText,
				SWT.BORDER);
		
		GridData avGd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		avGd.heightHint = 80;
		avGd.widthHint = 200;
		senderViewer.setLayoutData(avGd);
		dataBindingContext.bindValue(
				new AddressViewerSWTObservable(senderViewer),
				PojoObservables.observeValue(entry, "sender"), null, null) //$NON-NLS-1$
				.getTarget().addChangeListener(dirtyListener);
		senderViewer.update();
		createSection(Messages.InProtocolEditor_Section_Note_Heading,
				Section.TITLE_BAR | Section.TWISTIE	| Section.EXPANDED);

		createLabeledMultiText(Messages.InProtocolEditor_Label_Note, entry,
				"note"); //$NON-NLS-1$

		createTabItem(Messages.InProtocolEditor_Tab_Label_Documents,
				new GridLayout(1, true));

		documentsTable = new DocumentsTable(this, activePart, SWT.None, entry);

		createTabItem(Messages.InProtocolEditor_Tab_Label_References,
				new GridLayout(1, true));

		referenceTable = new ReferenceTable(this, activePart, SWT.NONE,
				(Inentry) entry);

		// createTabItem("FollowUps");

	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}
	
	public void setSender(EntryAddress address){
		senderViewer.setAddress(address);
	}

	@Override
	public void setFocus(){
			subjectText.setFocus();
			System.out.println(subjectText.forceFocus());
	}
}
