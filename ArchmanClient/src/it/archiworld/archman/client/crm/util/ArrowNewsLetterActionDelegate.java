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
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.AddressTreeView;
import it.archiworld.common.Member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IViewPart;

public class ArrowNewsLetterActionDelegate extends ExportActionDelegate {

	IViewPart view;
	
	@Override
	public void init(IViewPart view) {
		this.view=view;
	}

	@Override
	public void run(IAction action) {
		view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		List<Member> members=new ArrayList<Member>();
		if(((AddressTreeView)view.getViewSite().getPart()).getMembers()!=null)
			members.addAll(((AddressTreeView)view.getViewSite().getPart()).getMembers());
		if(((AddressTreeView)view.getViewSite().getPart()).getSearchedMembers()!=null)
			members.addAll(((AddressTreeView)view.getViewSite().getPart()).getSearchedMembers());
		try{
			String output=""; //$NON-NLS-1$
			Addressbook book = (Addressbook) ConnectionFactory
			.getConnection("AddressbookBean"); //$NON-NLS-1$
			for(Member member:members){
				member=(Member)book.getAddress(member);
				output+=exportMember(member)+"\r\n"; //$NON-NLS-1$
			}
			String dir=""; //$NON-NLS-1$
			if(Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)!="") //$NON-NLS-1$
				dir = Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)+"/memberlistfreccia.txt"; //$NON-NLS-1$
			else
				dir = (new File(".")).getCanonicalPath()+"/memberlistfreccia.txt"; //$NON-NLS-1$ //$NON-NLS-2$
			FileWriter fwriter=new FileWriter(dir);
			BufferedWriter bwriter=new BufferedWriter(fwriter);
			bwriter.append(output);
			bwriter.close();
			fwriter.close();
			String filename;
			filename = openWordFileDialog("member"); //$NON-NLS-1$
			if(filename!=null)
				Program.launch(filename);

		}
		catch(Throwable t){
			MessageDialog.openError(shell, Messages.getString("ArrowNewsLetterActionDelegate_ErrorDialogHeading"), t.getLocalizedMessage()); //$NON-NLS-1$
			t.printStackTrace();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {		
	}

	protected String exportMember(Member member){
		StringBuffer buffer = new StringBuffer();
		buffer.append(new GregorianCalendar().get(Calendar.YEAR));//$NON-NLS-1$
		for(int i=0;i<(12-member.getRegistration_number().toString().length());i++)
			buffer.append("0");//$NON-NLS-1$
		buffer.append(member.getRegistration_number()+"\t");//$NON-NLS-1$
		buffer.append(formatter(member.getFirstname(),20));
		buffer.append(formatter(member.getLastname(),30));
		buffer.append(formatter(null,40));//Title
		buffer.append(formatter("An/Per",40));//$NON-NLS-1$
		if(member.getStdaddress().equalsIgnoreCase("address")){//$NON-NLS-1$
			buffer.append(formatter(member.getStreet(),30));
			buffer.append(formatter(member.getCity(),35));
			buffer.append(formatter(member.getZip(),5));
			buffer.append(formatter(member.getState(),2));
		}
		if(member.getStdaddress().equalsIgnoreCase("homeaddress")){//$NON-NLS-1$
			buffer.append(formatter(member.getHomestreet(),30));
			buffer.append(formatter(member.getHomecity(),35));
			buffer.append(formatter(member.getHomezip(),5));
			buffer.append(formatter(member.getHomestate(),2));			
		}
		if(member.getStdaddress().equalsIgnoreCase("address_de")){//$NON-NLS-1$
			buffer.append(formatter(member.getStreet_de(),30));
			buffer.append(formatter(member.getCity_de(),35));
			buffer.append(formatter(member.getZip(),5));
			buffer.append(formatter(member.getState_de(),2));
		}
		if(member.getStdaddress().equalsIgnoreCase("homeaddress_de")){//$NON-NLS-1$
			buffer.append(formatter(member.getHomestreet_de(),30));
			buffer.append(formatter(member.getHomecity_de(),35));
			buffer.append(formatter(member.getHomezip(),5));
			buffer.append(formatter(member.getHomestate_de(),2));						
		}
		buffer.append(formatter(null,16));//Fiscal Code
		buffer.append(formatter(null,11));//Tax Code
		buffer.append(formatter(null,30));//Phone
		buffer.append(formatter(null,30));//Fax
		buffer.append(formatter(null,50));//Email
		buffer.append(formatter(null,10));//program specific
		buffer.append(formatter(null,10));//program specific
		
		buffer.append(formatter(null,200));//program specific
		buffer.append(formatter(null,40));//program specific
		buffer.append(formatter(null,10));//program specific
		buffer.append(formatter(null,10));//program specific
		buffer.append(formatter(null,10));//program specific
		buffer.append(formatter(null,10));//program specific
		buffer.append(formatter(null,16));//program specific
		
		return buffer.toString();
	}

	protected String formatter(String value, int size){
		String output="";//$NON-NLS-1$
		if(value!=null)
			if(value.length()<size){
				output=value;
				size=size-value.length();
			}
			else 
				return value.substring(0,size)+"\t"; //$NON-NLS-1$
		for(int i=0;i<size;i++)
				output+=" ";//$NON-NLS-1$
		return output+"\t";//$NON-NLS-1$
	}

}
