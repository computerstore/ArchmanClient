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
package it.archiworld.archman.client.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.archiworld.archman.client.core.messages"; //$NON-NLS-1$

	public static String ApplicationActionBarAdvisor_Menu_Report;

	public static String ApplicationActionBarAdvisor_MenuEntry_OpenPerspective;

	public static String ApplicationActionBarAdvisor_MenuSeparator_Reports;

	public static String ApplicationActionBarAdvisor_SubMenu_Label_File;

	public static String ApplicationActionBarAdvisor_SubMenu_Label_Help;

	public static String ApplicationActionBarAdvisor_SubMenu_Label_Window;

	public static String ApplicationWorkbenchWindowAdvisor_TrayItem_ToolTipText;

	public static String GeneralPreferencePage_ComboEntry_English;

	public static String GeneralPreferencePage_ComboEntry_German;

	public static String GeneralPreferencePage_ComboEntry_Italian;

	public static String ScrolledFormEditorBase_AuthenticationErrorDialogText;

	public static String ScrolledFormEditorBase_AuthenticationErrorDialogTitle;

	public static String ScrolledSearchEditorBase_ComboOption_Activated;

	public static String ScrolledSearchEditorBase_ComboOption_Deactivated;

	public static String ScrolledSearchEditorBase_ComboOption_Empty;

	public static String ScrolledSearchEditorBase_ComboOption_Equal;

	public static String ScrolledSearchEditorBase_ComboOption_EqualOr;

	public static String ScrolledSearchEditorBase_ComboOption_FromTo;

	public static String ScrolledSearchEditorBase_ComboOption_Max;

	public static String ScrolledSearchEditorBase_ComboOption_Min;

	public static String ScrolledSearchEditorBase_ComboOption_NotEmpty;

	public static String ScrolledSearchEditorBase_ComboOption_NotEqual;

	public static String ScrolledSearchEditorBase_ComboOption_NotEqualAnd;

	public static String ScrolledSearchEditorBase_ComboOption_Whatever;

	public static String SearchEditorInput_SearchEditorInput_Name;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
