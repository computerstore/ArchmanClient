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
package it.archiworld.archman.client.crm.report;

import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.common.Member;
import it.archiworld.util.ReportManager;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class OpenNotPaidRecordsAction extends Action implements ActionFactory.IWorkbenchAction {

	public final static String ID = "it.archiworld.archman.client.report.opennotparidrecordsaction"; //$NON-NLS-1$
	public final IWorkbenchWindow window;

	public OpenNotPaidRecordsAction(IWorkbenchWindow window) {
		this.window=window;
	    setId(ID);
	    setText(Messages.getString("OpenNotPaidRecordsAction.WindowTitle_OpenRenewals")); //$NON-NLS-1$
	    setToolTipText(Messages.getString("OpenNotPaidRecordsAction.Window_ToolTipText_OpenRenewals")); //$NON-NLS-1$
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public void run(){
    	String year = openNewDialog();
        if ( year!=null ) {
        	try {
				List<Member> members =((ReportManager)ConnectionFactory.getConnection("ReportManagerBean")).getNotPaidMembers(year); //$NON-NLS-1$
				export(members);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
        }
	}
	
	private void export(List<Member> members) {
		WritableWorkbook workbook;
		WritableSheet sheet;
		File file;
		try {
			file = File.createTempFile("output_"+new Date().getTime(),".xls"); //$NON-NLS-1$ //$NON-NLS-2$
			workbook = Workbook.createWorkbook(file);
			sheet = workbook.createSheet("Export", 0); //$NON-NLS-1$
			sheet.addCell(new Label(0,0,it.archiworld.archman.client.crm.Messages.MemberEditor_Label_RegistrationNumber));
			sheet.addCell(new Label(1,0,it.archiworld.archman.client.crm.Messages.ScrolledCRMFormEditor_Label_Lastname));
			sheet.addCell(new Label(2,0,it.archiworld.archman.client.crm.Messages.ScrolledCRMFormEditor_Label_Firstname));
			sheet.addCell(new Label(3,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_Street));
			sheet.addCell(new Label(4,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_City));
			sheet.addCell(new Label(5,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_State));
			sheet.addCell(new Label(6,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_Country));
			sheet.addCell(new Label(7,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_PhoneNumber));
			sheet.addCell(new Label(8,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_Fax));
			sheet.addCell(new Label(9,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_Mobile));
			sheet.addCell(new Label(10,0,it.archiworld.archman.client.crm.Messages.PersonEditor_Label_Email));
			for(int i=0; i<members.size(); i++){
				sheet.addCell(new Number(0,i+1,members.get(i).getRegistration_number()));
				sheet.addCell(new Label(1,i+1,members.get(i).getLastname()));
				sheet.addCell(new Label(2,i+1,members.get(i).getFirstname()));
				String stdaddress = members.get(i).getStdaddress();
				if (stdaddress.equals("address")){ 										//$NON-NLS-1$
					sheet.addCell(new Label(3,i+1,members.get(i).getStreet()));
					sheet.addCell(new Label(4,i+1,members.get(i).getCity()));
					sheet.addCell(new Label(5,i+1,members.get(i).getState()));
					sheet.addCell(new Label(6,i+1,members.get(i).getCountry()));
					sheet.addCell(new Label(7,i+1,members.get(i).getPhone()));
					sheet.addCell(new Label(8,i+1,members.get(i).getFax()));
				}
				else if (stdaddress.equals("homeaddress")){								//$NON-NLS-1$
					sheet.addCell(new Label(3,i+1,members.get(i).getHomestreet()));
					sheet.addCell(new Label(4,i+1,members.get(i).getHomecity()));
					sheet.addCell(new Label(5,i+1,members.get(i).getHomestate()));
					sheet.addCell(new Label(6,i+1,members.get(i).getHomecountry()));
					sheet.addCell(new Label(7,i+1,members.get(i).getHomephone()));
					sheet.addCell(new Label(8,i+1,members.get(i).getHomefax()));					
				}
				else if (stdaddress.equals("address_de")){								//$NON-NLS-1$
					sheet.addCell(new Label(3,i+1,members.get(i).getStreet_de()));
					sheet.addCell(new Label(4,i+1,members.get(i).getCity_de()));
					sheet.addCell(new Label(5,i+1,members.get(i).getState_de()));
					sheet.addCell(new Label(6,i+1,members.get(i).getCountry_de()));
					sheet.addCell(new Label(7,i+1,members.get(i).getPhone()));
					sheet.addCell(new Label(8,i+1,members.get(i).getFax()));
				}
				else if (stdaddress.equals("homeaddress_de")){ 							//$NON-NLS-1$
					sheet.addCell(new Label(3,i+1,members.get(i).getHomestreet_de()));
					sheet.addCell(new Label(4,i+1,members.get(i).getHomecity_de()));
					sheet.addCell(new Label(5,i+1,members.get(i).getHomestate_de()));
					sheet.addCell(new Label(6,i+1,members.get(i).getHomecountry_de()));
					sheet.addCell(new Label(7,i+1,members.get(i).getHomephone()));
					sheet.addCell(new Label(8,i+1,members.get(i).getHomefax()));					
				}
				sheet.addCell(new Label(9,i+1,members.get(i).getMobilephone()));					
				sheet.addCell(new Label(10,i+1,members.get(i).getEmail()));
			}
			workbook.write();
			workbook.close();
			Program.launch(file.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //$NON-NLS-1$ //$NON-NLS-2$ 
		catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String openNewDialog() {
		 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		 shell = Display.getCurrent().getActiveShell();
		 String dialogTitle = Messages.getString("OpenNotPaidRecordsAction.DialogText_PleaseEnterTheYear"); //$NON-NLS-1$
		 String dialogMessage = Messages.getString("OpenNotPaidRecordsAction.Info_EnterTheYearOfTheRenewalsWanted"); //$NON-NLS-1$
		 IInputValidator validator = new IInputValidator() {
		 	public String isValid(String newText) {
		 		if(newText=="") //$NON-NLS-1$
		 			return Messages.getString("OpenNotPaidRecordsAction.InfoError_YearCanNotBeEmpty"); //$NON-NLS-1$
		 		try{
		 			Integer.parseInt(newText);
		 		}
		 		catch(NumberFormatException ex){
		 			ex.printStackTrace();
		 			return Messages.getString("OpenNotPaidRecordsAction.InfoError_ConvertingNumber");  //$NON-NLS-1$
		 		}
		 		return null;
			}  
		};
		InputDialog dialog = new InputDialog(shell, dialogTitle, dialogMessage, null, validator);
		int result = dialog.open();
		return result == Window.OK ? dialog.getValue() : null;
	}
}
