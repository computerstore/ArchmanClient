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
package it.archiworld.archman.client.core.connect;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "it.archiworld.archman.client.core.connect.messages"; //$NON-NLS-1$
	public static String RMIConnectionSettingsPage_Label_RMIPort;
	public static String RMIConnectionSettingsPage_Label_RMIServer;
	public static String RMIConnectionSettingsPage_Label_RMIPrincipal;
	public static String RMIConnectionSettingsPage_Label_RMICredentials;
	public static String TemplateFolder_LabelExportFolder;
	public static String TemplateFolder_LabelSaveFolder;
	public static String TemplateFolder_LabelTemplateFolder;

	static {
// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
