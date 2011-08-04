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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.archiworld.archman.client.committee.messages"; //$NON-NLS-1$
	public static String CommitteeEditor_Form_Heading;
	public static String CommitteeEditor_Label_City;
	public static String CommitteeEditor_Label_Country;
	public static String CommitteeEditor_Label_Denomination;
	public static String CommitteeEditor_Label_Note;
	public static String CommitteeEditor_Label_State;
	public static String CommitteeEditor_Label_Street;
	public static String CommitteeEditor_Label_Zip;
	public static String CommitteeEditor_SaveFailureDialog_Heading;
	public static String CommitteeEditor_SaveFailureDialog_Text;
	public static String CommitteeEditor_SaveSuccessDialog_Heading;
	public static String CommitteeEditor_SaveSuccessDialog_Text;
	public static String CommitteeEditor_Section_Heading_Address;
	public static String CommitteeEditor_Section_Heading_Note;
	public static String CommitteeEditor_TabLabelGeneral;
	public static String CommitteeEditor_TabLabelMembers;
	public static String CommitteeEditorInput_EmptyCommittee_Name;
	public static String CommitteeMemberTable_ButtonLabelDelete;
	public static String CommitteeMemberTable_ColumnLabelMember;
	public static String CommitteeMemberTable_ColumnLabelNominationType;
	public static String CommitteeMemberTable_ColumnLabelRole;
	public static String CommitteeMemberTable_ColumnLabelStart;
	public static String CommitteeMemberTable_ColumnLabelStop;
	public static String CommitteeMemberTable_nomination_permanent;
	public static String CommitteeMemberTable_nomination_substitute;
	public static String CommitteeMemberTable_ValueNotEntered;
	public static String CommitteeTreeView_RefreshFailureDialog_Heading;
	public static String CommitteeTreeView_RefreshFailureDialog_Text;
	static {
// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
