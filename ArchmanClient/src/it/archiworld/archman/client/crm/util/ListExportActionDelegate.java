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
package it.archiworld.archman.client.crm.util;

import it.archiworld.archman.client.crm.wizzard.ExportOptionWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

public class ListExportActionDelegate extends ExportActionDelegate {

	@Override
	public void run(IAction action) {

		ExportOptionWizard wizard = new ExportOptionWizard();
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		dialog.open();
		return;
/*		List<Member> members = (((AddressTreeView)view.getViewSite().getPart()).getCurrentMembers());
		
		if(members!=null){
			StringBuffer output=new StringBuffer(exportHeader());
			System.out.println(exportHeader());
			try{
				Addressbook book = (Addressbook) ConnectionFactory
				.getConnection("AddressbookBean"); //$NON-NLS-1$
				for(Member member:members){
					member=(Member)book.getAddress(member);
					System.out.println(exportBody(member));
					output.append(exportBody(member));
				}	
				String dir=""; //$NON-NLS-1$
				if(Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)!="") //$NON-NLS-1$
					dir = Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)+"/memberlist.txt"; //$NON-NLS-1$
				else
					dir = (new File(".")).getCanonicalPath()+"/memberlist.txt"; //$NON-NLS-1$ //$NON-NLS-2$
				FileWriter fwriter=new FileWriter(dir);
				BufferedWriter bwriter=new BufferedWriter(fwriter);
				bwriter.append(output);
				bwriter.close();
				fwriter.close();
				String filename;
				filename = openWordFileDialog("member"); //$NON-NLS-1$
				if(filename!=null)
					Program.launch(filename);
			} catch (IOException e){
				MessageDialog.openError(shell, "Error", e.getLocalizedMessage());
				e.printStackTrace();
			} catch (ConnectionException e){
				MessageDialog.openError(shell, "Error", e.getLocalizedMessage());
				e.printStackTrace();
			} catch (Throwable t){
				MessageDialog.openError(shell, "Error", t.getLocalizedMessage());
				t.printStackTrace();
			}
		}
*/	}
}
