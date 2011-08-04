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

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.AddressTreeView;
import it.archiworld.archman.client.crm.Messages;
import it.archiworld.common.Member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IViewPart;

public class ExportSelectedActionDelegate extends ExportActionDelegate {


	IViewPart view;
	String selected;
	
	
	@Override
	public void init(IViewPart view) {
		this.view = view;
	}

	@Override
	public void run(IAction action) {
		if(selected==null)
			return;
		List<Member> members=null;
		view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		if(selected.equalsIgnoreCase(Messages.AddressTreeView_Folder_Label_Member)){
			members=((AddressTreeView)view.getViewSite().getPart()).getMembers();
		}
		if(selected.equalsIgnoreCase(Messages.AddressTreeView_FolderLabelSearchResult)){
			members=((AddressTreeView)view.getViewSite().getPart()).getSearchedMembers();
		}
		if(selected.equalsIgnoreCase(Messages.AddressTreeView_deregistered_members)){
			members=((AddressTreeView)view.getViewSite().getPart()).getDeletedMembers();
		}
		
		if(members!=null && !members.isEmpty()){
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
				MessageDialog.openError(shell, Messages.ExportSelectedActionDelegate_ErrorDialogHeading, e.getLocalizedMessage());
				e.printStackTrace();
			} catch (ConnectionException e){
				MessageDialog.openError(shell, Messages.ExportSelectedActionDelegate_ErrorDialogHeading, e.getLocalizedMessage());
				e.printStackTrace();
			} catch (Throwable t){
				MessageDialog.openError(shell, Messages.ExportSelectedActionDelegate_ErrorDialogHeading, t.getLocalizedMessage());
				t.printStackTrace();
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(((IStructuredSelection)selection).getFirstElement()!=null)
			selected = ((IStructuredSelection)selection).getFirstElement().toString();
	}

}
