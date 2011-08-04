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

import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.crm.Messages;
import it.archiworld.common.Member;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ExportActionDelegate extends Action implements IViewActionDelegate, IWorkbenchAction, IPartListener2 {

	protected static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
	protected static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$

	protected IViewPart view;
	protected IWorkbenchWindow window;
	protected Shell shell;
	
	public ExportActionDelegate(){
	}
	
	public ExportActionDelegate(IWorkbenchWindow window){
		this.window=window;
		shell=window.getShell();
	}
	
	@Override
	public void init(IViewPart view) {
		this.view=view;
		shell=view.getViewSite().getShell();
	}

	@Override
	public void run(IAction action) {
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}


	public String exportHeader(){
		String output=""; //$NON-NLS-1$
		output+=headFormatter("Title"); //$NON-NLS-1$
		output+=headFormatter("Firstname"); //$NON-NLS-1$
		output+=headFormatter("Lastname"); //$NON-NLS-1$
		output+=headFormatter("Birthdate"); //$NON-NLS-1$
		output+=headFormatter("Birthplace"); //$NON-NLS-1$
		output+=headFormatter("Birthplace_de"); //$NON-NLS-1$
		output+=headFormatter("Street"); //$NON-NLS-1$
		output+=headFormatter("Zip"); //$NON-NLS-1$
		output+=headFormatter("City"); //$NON-NLS-1$
		output+=headFormatter("State"); //$NON-NLS-1$
		output+=headFormatter("Country"); //$NON-NLS-1$
		output+=headFormatter("Street_de"); //$NON-NLS-1$
		output+=headFormatter("Zip_de"); //$NON-NLS-1$
		output+=headFormatter("City_de"); //$NON-NLS-1$
		output+=headFormatter("State_de"); //$NON-NLS-1$
		output+=headFormatter("Country_de"); //$NON-NLS-1$
		output+=headFormatter("Homestreet"); //$NON-NLS-1$
		output+=headFormatter("Homezip"); //$NON-NLS-1$
		output+=headFormatter("Homecity"); //$NON-NLS-1$
		output+=headFormatter("Homestate"); //$NON-NLS-1$
		output+=headFormatter("Homecountry"); //$NON-NLS-1$
		output+=headFormatter("Homestreet_de"); //$NON-NLS-1$
		output+=headFormatter("Homezip_de"); //$NON-NLS-1$
		output+=headFormatter("Homecity_de"); //$NON-NLS-1$
		output+=headFormatter("Homestate_de"); //$NON-NLS-1$
		output+=headFormatter("Homecountry_de"); //$NON-NLS-1$
		output+=headFormatter("Email"); //$NON-NLS-1$
		output+=headFormatter("Phone"); //$NON-NLS-1$
		output+=headFormatter("Fax"); 			 //$NON-NLS-1$
		output+=headFormatter("Homephone"); 			 //$NON-NLS-1$
		output+=headFormatter("Homefax"); //$NON-NLS-1$
		output+=headFormatter("Mobilephone"); //$NON-NLS-1$
		output+=headFormatter("Website"); //$NON-NLS-1$
		output+=headFormatter("Fiscal_code"); //$NON-NLS-1$
		output+=headFormatter("Tax_code"); //$NON-NLS-1$
		output+=headFormatter("Graduation_approval_date"); //$NON-NLS-1$
		output+=headFormatter("Graduation_date"); //$NON-NLS-1$
		output+=headFormatter("Graduation_institute"); //$NON-NLS-1$
		output+=headFormatter("State_exam_year"); //$NON-NLS-1$
		output+=headFormatter("Habilitation"); //$NON-NLS-1$
		output+=headFormatter("Nationality"); //$NON-NLS-1$
		output+=headFormatter("Profession"); //$NON-NLS-1$
		output+=headFormatter("Registration_date"); //$NON-NLS-1$
		output+=headFormatter("Registration_exemption"); //$NON-NLS-1$
		output+=headFormatter("Registration_number"); //$NON-NLS-1$

		output+=headFormatter("Section"); //$NON-NLS-1$
		output+=headFormatter("Sector"); //$NON-NLS-1$
		output+=headFormatter("Sector_de"); //$NON-NLS-1$
		output+=headFormatter("Sector_date"); //$NON-NLS-1$
		output+=headFormatter("Sector_exemption"); //$NON-NLS-1$
		output+=headFormatter("Sector_habilitation"); //$NON-NLS-1$
		
		output+=headFormatter("Deregister_date"); //$NON-NLS-1$
		output+=headFormatter("Deregister_exemption"); //$NON-NLS-1$
		output+=headFormatter("Deregister_reason");		 //$NON-NLS-1$

		output+=headFormatter("Newsletter"); //$NON-NLS-1$
		output+=headFormatter("Culturenewsletter"); //$NON-NLS-1$

		//Standard Address for Markers
		output+=headFormatter("StdStreet"); //$NON-NLS-1$
		output+=headFormatter("StdZip"); //$NON-NLS-1$
		output+=headFormatter("StdCity"); //$NON-NLS-1$
		output+=headFormatter("StdState"); //$NON-NLS-1$
		output+=headFormatter("StdCountry"); //$NON-NLS-1$

		//Here start directory things
		output+=headFormatter("DirectoryAddress"); //$NON-NLS-1$
		output+=headFormatter("DirectoryOfficeAddress"); //$NON-NLS-1$

		output+=headFormatter("DirectoryEmail"); //$NON-NLS-1$
		output+=headFormatter("DirectoryFax"); 			 //$NON-NLS-1$
		output+=headFormatter("DirectoryHomephone"); 			 //$NON-NLS-1$
		output+=headFormatter("DirectoryHomefax"); 			 //$NON-NLS-1$
		output+=headFormatter("DirectoryMobilephone"); //$NON-NLS-1$
		output+=headFormatter("DirectoryPhone"); //$NON-NLS-1$
		output+=headFormatter("DirectoryWebsite");		 //$NON-NLS-1$

		output+=headFormatter("DirectoryHomestreet"); //$NON-NLS-1$
		output+=headFormatter("DirectoryHomezip"); //$NON-NLS-1$
		output+=headFormatter("DirectoryHomecity"); //$NON-NLS-1$
		output+=headFormatter("DirectoryHomestate"); //$NON-NLS-1$
		output+=headFormatter("DirectoryHomecountry"); //$NON-NLS-1$

		output+=headFormatter("DirectoryStreet"); //$NON-NLS-1$
		output+=headFormatter("DirectoryZip"); //$NON-NLS-1$
		output+=headFormatter("DirectoryCity"); //$NON-NLS-1$
		output+=headFormatter("DirectoryState"); //$NON-NLS-1$
		output+=headFormatter("DirectoryCountry"); //$NON-NLS-1$
		
		output+=headFormatter("DirectoryStdStreet"); //$NON-NLS-1$
		output+=headFormatter("DirectoryStdZip"); //$NON-NLS-1$
		output+=headFormatter("DirectoryStdCity"); //$NON-NLS-1$
		output+=headFormatter("DirectoryStdState"); //$NON-NLS-1$
		output+=headFormatter("DirectoryStdCountry"); //$NON-NLS-1$
		
		output+="LastElement"; //$NON-NLS-1$
		return output+="\r\n"; //$NON-NLS-1$
	}
	
	public String exportBody(Member member){
		String output=""; //$NON-NLS-1$
		output+=formatter(member.getTitle());
		output+=formatter(member.getFirstname());
		output+=formatter(member.getLastname());
		output+=formatter(member.getBirthdate());
		output+=formatter(member.getBirthplace());
		output+=formatter(member.getBirthplace_de());
		output+=formatter(member.getStreet());
		output+=formatter(member.getZip());
		output+=formatter(member.getCity());
		output+=formatter(member.getState());
		output+=formatter(member.getCountry());
		output+=formatter(member.getStreet_de());
		output+=formatter(member.getZip());
		output+=formatter(member.getCity_de());
		output+=formatter(member.getState_de());
		output+=formatter(member.getCountry_de());
		output+=formatter(member.getHomestreet());
		output+=formatter(member.getHomezip());
		output+=formatter(member.getHomecity());
		output+=formatter(member.getHomestate());
		output+=formatter(member.getHomecountry());
		output+=formatter(member.getHomestreet_de());
		output+=formatter(member.getHomezip());
		output+=formatter(member.getHomecity_de());
		output+=formatter(member.getHomestate_de());
		output+=formatter(member.getHomecountry_de());
		output+=formatter(member.getEmail());
		output+=formatter(member.getPhone());
		output+=formatter(member.getFax()); 			
		output+=formatter(member.getHomephone()); 			
		output+=formatter(member.getHomefax()); 			
		output+=formatter(member.getMobilephone());
		output+=formatter(member.getWebsite());
		output+=formatter(member.getFiscal_code());
		output+=formatter(member.getTax_code());
		output+=formatter(member.getGraduation_approval_date());
		output+=formatter(member.getGraduation_date());
		output+=formatter(member.getGraduation_institute());
		output+=formatter(member.getState_exam_year());
		output+=formatter(member.getHabilitation());
		output+=formatter(member.getNationality());
		
		if(member.getRegister_address()!=null && member.getRegister_address().equals("italian")){  //$NON-NLS-1$
			if(member.getProfession()==null)
				output+="\t"; //$NON-NLS-1$
			else if(member.getProfession().equals("male freelancer")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_MaleFreelancer_it); 
			else if(member.getProfession().equals("female freelancer")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_FemaleFreelancer_it); 
			else if(member.getProfession().equals("self-employment")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_SelfEmployment_it); 
			else if(member.getProfession().equals("teacher")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Teacher_it); 
			else if(member.getProfession().equals("employee")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Employee_it); 
			else if(member.getProfession().equals("associate")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Associate_it); 
			else if(member.getProfession().equals("other")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Other_it);	
			else
				output+="\t"; //$NON-NLS-1$ 
		}
		else {
			if(member.getProfession()==null)
				output+="\t"; //$NON-NLS-1$
			else if(member.getProfession().equals("male freelancer")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_MaleFreelancer_de);
			else if(member.getProfession().equals("female freelancer")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_FemaleFreelancer_de); 
			else if(member.getProfession().equals("self-employment")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_SelfEmployment_de); 
			else if(member.getProfession().equals("teacher")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Teacher_de); 
			else if(member.getProfession().equals("employee")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Employee_de); 
			else if(member.getProfession().equals("associate")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Associate_de);
			else if(member.getProfession().equals("other")) //$NON-NLS-1$
				output+=formatter(Messages.ExportActionDelegate_Other_de);	
			else
				output+="\t"; //$NON-NLS-1$
		}
		
		output+=formatter(member.getRegistration_date());
		output+=formatter(member.getRegistration_exemption());
		output+=formatter(member.getRegistration_number());

		//Sector
		if(member.getSector()==null)
			output+="\t\t\t"; //$NON-NLS-1$
		else {
			if(member.getSector()!=null && member.getSector().charAt(1)=='A')
				output+=formatter("A"); //$NON-NLS-1$
			else if(member.getSector()!=null && member.getSector().charAt(1)=='B')
				output+=formatter("B"); //$NON-NLS-1$
			//Section
			if(member.getSector()!=null && member.getSector().equals("[A]architect")){ //$NON-NLS-1$
				output+=formatter("Architetto"); //$NON-NLS-1$
				output+=formatter("Architekt"); //$NON-NLS-1$
			} else if(member.getSector()!=null && member.getSector().equals("[A]urbanplaner")){ //$NON-NLS-1$
				output+=formatter("Pianificatore"); //$NON-NLS-1$
				output+=formatter("Raumplaner"); //$NON-NLS-1$
			} else if(member.getSector()!=null && member.getSector().equals("[A]landscapedesigner")){ //$NON-NLS-1$
				output+=formatter("Paesaggista"); //$NON-NLS-1$
				output+=formatter("Landschaftsplaner"); //$NON-NLS-1$
			} else if(member.getSector()!=null && member.getSector().equals("[A]monumentconservator")){ //$NON-NLS-1$
				output+=formatter("Conservatore"); //$NON-NLS-1$
				output+=formatter("Denkmalpfleger"); //$NON-NLS-1$
			} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorarchitect")){ //$NON-NLS-1$
				output+=formatter("Architetto iunior"); //$NON-NLS-1$
				output+=formatter("Iunior Architekt"); //$NON-NLS-1$
			} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorurbanplaner")){ //$NON-NLS-1$
				output+=formatter("Pianificatore iunior"); //$NON-NLS-1$
				output+=formatter("Iunior Raumplaner"); //$NON-NLS-1$
			} else {
				output+="\t\t"; //$NON-NLS-1$
			}
		}
			
		output+=formatter(member.getSector_date());
		output+=formatter(member.getSector_exemption());
		output+=formatter(member.getSector_habilitation());
		
		output+=formatter(member.getDeregister_date());
		output+=formatter(member.getDeregister_exemption());
		output+=formatter(member.getDeregister_reason());

		output+=formatter(member.getNewsletter());
		output+=formatter(member.getCulturenewsletter());
		
		//Standard Address for Markers
		if(member.getStdaddress()!=null && member.getStdaddress().equalsIgnoreCase("address")){ //$NON-NLS-1$
			output+=formatter(member.getStreet());
			output+=formatter(member.getZip());
			output+=formatter(member.getCity());
			output+=formatter(member.getState());
			output+=formatter(member.getCountry());
		} 
		else
			if(member.getStdaddress()!=null && member.getStdaddress().equalsIgnoreCase("address_de")){ //$NON-NLS-1$
				output+=formatter(member.getStreet_de());
				output+=formatter(member.getZip());
				output+=formatter(member.getCity_de());
				output+=formatter(member.getState_de());
				output+=formatter(member.getCountry_de());
			} 
			else
				if(member.getStdaddress()!=null && member.getStdaddress().equalsIgnoreCase("homeaddress")){ //$NON-NLS-1$
					output+=formatter(member.getHomestreet());
					output+=formatter(member.getHomezip());
					output+=formatter(member.getHomecity());
					output+=formatter(member.getHomestate());
					output+=formatter(member.getHomecountry());
				}
				else
					if(member.getStdaddress()!=null && member.getStdaddress().equalsIgnoreCase("homeaddress_de")){ //$NON-NLS-1$
						output+=formatter(member.getHomestreet_de());
						output+=formatter(member.getHomezip());
						output+=formatter(member.getHomecity_de());
						output+=formatter(member.getHomestate_de());
						output+=formatter(member.getHomecountry_de());
					} 
					else output+="\t\t\t\t\t"; //$NON-NLS-1$

		//Here start directory things
		output+=formatter(member.getRegister_address());
		output+=formatter(member.getDirectory_officeaddress());

		if(member.getDirectory_email()!=null && member.getDirectory_email()) 
			output+=formatter(member.getEmail());
		else output+="\t";		 //$NON-NLS-1$
		
		if(member.getDirectory_fax()!=null && member.getDirectory_fax())
			output+=formatter(member.getFax()); 			
		else output+="\t"; //$NON-NLS-1$

		if(member.getDirectory_homephone()!=null && member.getDirectory_homephone())
			output+=formatter(member.getHomephone()); 			
		else output+="\t"; //$NON-NLS-1$

		if(member.getDirectory_homefax()!=null && member.getDirectory_homefax())
			output+=formatter(member.getHomefax()); 			
		else output+="\t"; //$NON-NLS-1$

		if(member.getDirectory_mobile()!=null && member.getDirectory_mobile())
			output+=formatter(member.getMobilephone());
		else output+="\t"; //$NON-NLS-1$

		if(member.getDirectory_phone()!=null && member.getDirectory_phone()) 
			output+=formatter(member.getPhone());
		else output+="\t"; //$NON-NLS-1$

		if(member.getDirectory_website()!=null && member.getDirectory_website())
			output+=formatter(member.getWebsite());
		else output+="\t"; //$NON-NLS-1$
		
		
		if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
			output+=formatter(member.getHomestreet());
			output+=formatter(member.getHomezip());
			output+=formatter(member.getHomecity());
			output+=formatter(member.getHomestate());
			output+=formatter(member.getHomecountry());
		}
		else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
			output+=formatter(member.getHomestreet_de());
			output+=formatter(member.getHomezip());
			output+=formatter(member.getHomecity_de());
			output+=formatter(member.getHomestate_de());
			output+=formatter(member.getHomecountry_de());
		}
		else output+="\t\t\t\t\t"; //$NON-NLS-1$
	
		
		if(member.getDirectory_officeaddress()!=null && member.getDirectory_officeaddress()){ 
			if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
				output+=formatter(member.getStreet());
				output+=formatter(member.getZip());
				output+=formatter(member.getCity());
				output+=formatter(member.getState());
				output+=formatter(member.getCountry());
			}
			else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
				output+=formatter(member.getStreet_de());
				output+=formatter(member.getZip());
				output+=formatter(member.getCity_de());
				output+=formatter(member.getState_de());
				output+=formatter(member.getCountry_de());
			}
			else output+="\t\t\t\t\t"; //$NON-NLS-1$
		}
		else output+="\t\t\t\t\t"; //$NON-NLS-1$
		

		if(member.getDirectory_officeaddress()!=null && member.getDirectory_officeaddress()){ 
			if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
				output+=formatter(member.getStreet());
				output+=formatter(member.getZip());
				output+=formatter(member.getCity());
				output+=formatter(member.getState());
				output+=formatter(member.getCountry());
			}
			else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
				output+=formatter(member.getStreet_de());
				output+=formatter(member.getZip());
				output+=formatter(member.getCity_de());
				output+=formatter(member.getState_de());
				output+=formatter(member.getCountry_de());
			}
			else output+="\t\t\t\t\t"; //$NON-NLS-1$
		}
		else {
			if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
				output+=formatter(member.getHomestreet());
				output+=formatter(member.getHomezip());
				output+=formatter(member.getHomecity());
				output+=formatter(member.getHomestate());
				output+=formatter(member.getHomecountry());
			}
			else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
				output+=formatter(member.getHomestreet_de());
				output+=formatter(member.getHomezip());
				output+=formatter(member.getHomecity_de());
				output+=formatter(member.getHomestate_de());
				output+=formatter(member.getHomecountry_de());	
			}
			else output+="\t\t\t\t\t"; //$NON-NLS-1$
		}

		

		return output+"\r\n"; //$NON-NLS-1$
	}
	
	private String headFormatter(String string){
		return string+"\t"; //$NON-NLS-1$
	}
	
	private String formatter(Object input){
		if(input!=null){
			if(input instanceof Timestamp)
				return new SimpleDateFormat("dd.M.yyyy").format((Timestamp)input)+"\t"; //$NON-NLS-1$
			return input.toString()+"\t"; //$NON-NLS-1$
		}
		else return "\t"; //$NON-NLS-1$
	}
	
	protected String openWordFileDialog(String subdir) {
		//if (view!=null)
		shell=Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
		//else
		//	shell=new Shell();//window.getShell();
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[] { "*.doc; *.xls", "*.dot", "*.xlt", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String dir = ""; //$NON-NLS-1$
		try {
			if(!Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER).equals("")) //$NON-NLS-1$
				dir = Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER)+"/"+subdir; //$NON-NLS-1$
			else
				dir = (new File(".")).getCanonicalPath()+"/"+subdir; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Throwable t) {
			MessageDialog.openError(shell, Messages.ExportSelectedActionDelegate_ErrorDialogHeading, t.getLocalizedMessage()); //$NON-NLS-1$
			t.printStackTrace();
		}
		dialog.setFilterPath(dir);
		String filename = dialog.open();
		return filename;
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
