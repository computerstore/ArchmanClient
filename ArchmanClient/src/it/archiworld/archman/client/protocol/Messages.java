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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.archiworld.archman.client.protocol.messages"; //$NON-NLS-1$
	public static String ProtocolEditor_Header_Emergency_Marker;
	public static String DocumentsTable_Button_CreateNewEntry;
	public static String DocumentsTable_Button_DeleteSelectedEntries;
	public static String DocumentsTable_CellLabel_File_IsEmpty;
	public static String DocumentsTable_Column_Heading_File;
	public static String InProtocolEditor_AddressViewer_Sender_InitialText;
	public static String InProtocolEditor_Error_CanNotSaveIncomingProtocolEntryWithoutSender;
	public static String InProtocolEditor_Form_Title;
	public static String InProtocolEditor_Label_EntryId;
	public static String InProtocolEditor_Label_Note;
	public static String InProtocolEditor_Label_Owner;
	public static String InProtocolEditor_Label_ProtocolDate;
	public static String InProtocolEditor_Label_ProtocolId;
	public static String InProtocolEditor_Label_RecievedDate;
	public static String InProtocolEditor_Label_RegistrationDate;
	public static String InProtocolEditor_Label_Sender;
	public static String InProtocolEditor_Label_Subject;
	public static String InProtocolEditor_SaveFailureDialog_Heading;
	public static String InProtocolEditor_SaveFailureDialog_Text;
	public static String InProtocolEditor_SaveSuccessDialog_Heading;
	public static String InProtocolEditor_SaveSuccessDialog_Text;
	public static String InProtocolEditor_Section_Heading_Protocol;
	public static String InProtocolEditor_Section_Note_Heading;
	public static String InProtocolEditor_Tab_Label_Documents;
	public static String InProtocolEditor_Tab_Label_General;
	public static String InProtocolEditor_Tab_Label_References;
	public static String InProtocolEditorInput_EmptyInentry_Name;
	public static String OpenWordTemplateAction_OpenWordTemplateActionText;
	public static String OpenWordTemplateAction_OpenWordTemplateActionToolTip;
	public static String OutProtocolEditor_Error_CanNotSaveOutgoingProtocolEntryWithoutDestination;
	public static String OutProtocolEditor_Form_Title;
	public static String OutProtocolEditor_Label_EntryId;
	public static String OutProtocolEditor_Label_Note;
	public static String OutProtocolEditor_Label_Owner;
	public static String OutProtocolEditor_Label_ProtocolDate;
	public static String OutProtocolEditor_Label_ProtocolId;
	public static String OutProtocolEditor_Label_Receiver;
	public static String OutProtocolEditor_Label_RegistrationDate;
	public static String OutProtocolEditor_Label_State;
	public static String OutProtocolEditor_Label_Subject;
	public static String OutProtocolEditor_LabelYear;
	public static String OutProtocolEditor_SaveFailureDialog_Heading;
	public static String OutProtocolEditor_SaveFailureDialog_Text;
	public static String OutProtocolEditor_SaveSuccessDialog_Heading;
	public static String OutProtocolEditor_SaveSuccessDialog_Text;
	public static String OutProtocolEditor_Section_Heading_Note;
	public static String OutProtocolEditor_Section_Heading_Protocol;
	public static String OutProtocolEditor_Tab_Label_Documents;
	public static String OutProtocolEditor_Tab_Label_General;
	public static String OutProtocolEditor_Tab_Label_References;
	public static String OutProtocolEditorInput_EmptyOutentryIsNull;
	public static String ProtocolTreeView_Label_Search;
	public static String ProtocolTreeView_RefreshFailureDialog_Heading;
	public static String ProtocolTreeView_RefreshFailureDialog_Text;
	public static String ReferenceTable_Button_DeleteSelectedEntries;
	public static String ReferenceTable_CellLabel_Subject_IsEmpty;
	public static String ReferenceTable_Column_Heading_Subject;
	public static String ScrolledProtocolFormEditor_Label_Responsible;
	public static String ScrolledProtocolFormEditor_Voice;
	static {
// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
