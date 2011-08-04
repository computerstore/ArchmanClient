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
import it.archiworld.common.Company;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class CompanyExportActionDelegate extends Action implements IViewActionDelegate, IWorkbenchAction, IPartListener2 {

	protected static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
	protected static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$

	protected IViewPart view;
	protected IWorkbenchWindow window;
	protected Shell shell;
	
	public CompanyExportActionDelegate(){
		
	}
	
	public CompanyExportActionDelegate(IWorkbenchWindow window){
		this.window=window;
		shell=window.getShell();
		view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		System.out.println("VIEW:"+view.getClass());
	}
	
	@Override
	public void init(IViewPart view) {
		this.view=view;
		shell=view.getViewSite().getShell();
		System.out.println("VIEW::"+view.getClass());
	}

	@Override
	public void run(IAction action) {
		File file;
		try {
			view = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
			file = File.createTempFile("output_"+new Date().getTime(),".xls"); //$NON-NLS-1$ //$NON-NLS-2$
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("Export", 0); //$NON-NLS-1$
			sheet.addCell(new Label(0,0,Messages.CompanyEditor_Label_Denomination));
			sheet.addCell(new Label(1,0,Messages.ScrolledCRMFormEditor_Label_Title));
			sheet.addCell(new Label(2,0,Messages.ScrolledCRMFormEditor_Label_Firstname));
			sheet.addCell(new Label(3,0,Messages.ScrolledCRMFormEditor_Label_Lastname));
			sheet.addCell(new Label(4,0,Messages.CompanyEditor_Label_Street));
			sheet.addCell(new Label(5,0,Messages.CompanyEditor_Label_Zip));
			sheet.addCell(new Label(6,0,Messages.CompanyEditor_Label_City));
			sheet.addCell(new Label(7,0,Messages.CompanyEditor_Label_State));
			sheet.addCell(new Label(8,0,Messages.CompanyEditor_Label_Country));
			sheet.addCell(new Label(9,0,Messages.CompanyEditor_Label_Phone));
			sheet.addCell(new Label(10,0,Messages.CompanyEditor_Label_Mobile));
			sheet.addCell(new Label(11,0,Messages.CompanyEditor_Label_Fax));
			sheet.addCell(new Label(12,0,Messages.CompanyEditor_Label_Email));
			sheet.addCell(new Label(13,0,Messages.CompanyEditor_Label_Website));
			sheet.addCell(new Label(14,0,Messages.CompanyEditor_Label_Note));
			int i=1;
			Addressbook book = (Addressbook) ConnectionFactory.getConnection("AddressbookBean"); //$NON-NLS-1$
			for(Company company : ((AddressTreeView)view).getCompanies()) {
				company = (Company) book.getAddress(company); 
				sheet.addCell(new Label(0,i,company.getDenomination()));
				sheet.addCell(new Label(1,i,company.getContact_title()));
				sheet.addCell(new Label(2,i,company.getContact_firstname()));
				sheet.addCell(new Label(3,i,company.getContact_lastname()));
				sheet.addCell(new Label(4,i,company.getStreet()));
				sheet.addCell(new Label(5,i,company.getZip()));
				sheet.addCell(new Label(6,i,company.getCity()));
				sheet.addCell(new Label(7,i,company.getState()));
				sheet.addCell(new Label(8,i,company.getCountry()));
				sheet.addCell(new Label(9,i,company.getPhone()));
				sheet.addCell(new Label(10,i,company.getMobilephone()));
				sheet.addCell(new Label(11,i,company.getFax()));
				sheet.addCell(new Label(12,i,company.getEmail()));
				sheet.addCell(new Label(13,i,company.getWebsite()));
				sheet.addCell(new Label(14,i,company.getNote()));
				i++;
			}
			workbook.write();
			workbook.close();
			Program.launch(file.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Throwable t) {
			// TODO Auto-generated catch block
			t.printStackTrace();
		} 
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

}
