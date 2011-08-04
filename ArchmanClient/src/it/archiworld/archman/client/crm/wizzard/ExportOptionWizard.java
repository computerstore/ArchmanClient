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
package it.archiworld.archman.client.crm.wizzard;


import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.AddressTreeView;
import it.archiworld.archman.client.crm.Messages;
import it.archiworld.common.Member;
import it.archiworld.util.MemberFieldExportSelector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.program.Program;

public class ExportOptionWizard extends Wizard {

	ExportOptionWizardPageOne one;
	ExportOptionWizardPageTwo two;
	ExportOptionWizardPageThree three;
	AddressTreeView view;
	File file;
	MemberFieldExportSelector entity=new MemberFieldExportSelector();
	WritableWorkbook workbook;
	WritableSheet sheet;
	
	Map<String, String> fields;
	private String selected=null;
	
	public MemberFieldExportSelector getEntity() {
		return entity;
	}

	public void setEntity(MemberFieldExportSelector entity) {
		System.out.println("setEntity"); //$NON-NLS-1$
		System.out.println(entity.changes.getPropertyChangeListeners().length);
		System.out.println(this.entity.changes.getPropertyChangeListeners().length);
//		entity.changes=this.entity.changes;
		System.out.println(entity.changes.getPropertyChangeListeners().length);
//		this.entity.mergeObjects(entity);
		this.entity = entity;
		System.out.println(this.entity.changes.getPropertyChangeListeners().length);
	}

	protected void init(){
		setNeedsProgressMonitor(true);
		view = (AddressTreeView)Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AddressTreeView.ID);
		try{
			System.out.println("output_"+new Date().getTime()); //$NON-NLS-1$
			file = File.createTempFile("output_"+new Date().getTime(),".xls"); //$NON-NLS-1$ //$NON-NLS-2$ 
			workbook = Workbook.createWorkbook(file);
			sheet = workbook.createSheet("Export", 0); //$NON-NLS-1$
		}
		catch (IOException ex){
			ex.printStackTrace();
		}		
	}
	
	public ExportOptionWizard() {
		super();
		init();
	}
	
	public ExportOptionWizard(String selected){
		super();
		this.selected=selected;
		init();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean performFinish() {
		
		List<String> data = three.getData();

		if(data.isEmpty()) return false;
		
		int column=0;
		
		//Define Headers
		for(int i=0;i<data.size();i++){
			if(data.get(i).equalsIgnoreCase("directory_officeaddress") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("stdaddress") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("register_address") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("address_de") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("address_it") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("address_ms") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("homeaddress_de") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("homeaddress_it") ||  //$NON-NLS-1$
				data.get(i).equalsIgnoreCase("homeaddress_ms") ){ //$NON-NLS-1$
				exportField(column++,0,Messages.PersonEditor_Label_Street);
				exportField(column++,0,Messages.PersonEditor_Label_Zip);
				exportField(column++,0,Messages.PersonEditor_Label_City);
				exportField(column++,0,Messages.PersonEditor_Label_State);
				exportField(column++,0,Messages.PersonEditor_Label_Country);
			}
			else
				exportField(column++, 0, fields.get(data.get(i)));
		}
		List<Member> members;
		if(selected==null){
			members = view.getCurrentMembers();
		}
		else
			if(selected.equalsIgnoreCase(Messages.AddressTreeView_Folder_Label_Member)){
				members=((AddressTreeView)view.getViewSite().getPart()).getMembers();
			}
			else if(selected.equalsIgnoreCase(Messages.AddressTreeView_FolderLabelSearchResult)){
				members=((AddressTreeView)view.getViewSite().getPart()).getSearchedMembers();
			}
			else if(selected.equalsIgnoreCase(Messages.AddressTreeView_deregistered_members)){
				members=((AddressTreeView)view.getViewSite().getPart()).getDeletedMembers();
			}
			else
				return false;
		
		
		MemberExportDecorator abstract_member = new MemberExportDecorator(new Member());
		Class<MemberExportDecorator> entityClass = (Class<MemberExportDecorator>)abstract_member.getClass();
		Map<String,Method> methodMap = new HashMap<String, Method>();
		Method[] methods = entityClass.getMethods();
		for(int i=0;i<methods.length;i++){
			if(methods[i].getName().startsWith("get") && data.contains(methods[i].getName().substring(3).toLowerCase())) //$NON-NLS-1$
				methodMap.put(methods[i].getName().substring(3).toLowerCase(),methods[i]);
		}
		try{
			Addressbook book = (Addressbook) ConnectionFactory.getConnection("AddressbookBean"); //$NON-NLS-1$
			for(int j=0; j<members.size(); j++){
				MemberExportDecorator member = new MemberExportDecorator((Member)book.getAddress(members.get(j)));
				column=0;
				for(int i=0; i<data.size(); i++){
					Method method = methodMap.get(data.get(i));
					try{			
						if(method.getName().equalsIgnoreCase("getNewsletter")) //$NON-NLS-1$
							exportField(column++, j+1, member.getEmail());
						else if(method.getName().equalsIgnoreCase("getCulturenewsletter")) //$NON-NLS-1$
							exportField(column++, j+1, member.getEmail());
						else if(method.getName().equalsIgnoreCase("getDirectory_email")) //$NON-NLS-1$
							exportField(column++, j+1, member.getEmail());
						else if(method.getName().equalsIgnoreCase("getDirectory_fax")) //$NON-NLS-1$
							exportField(column++, j+1, member.getFax());
						else if(method.getName().equalsIgnoreCase("getDirectory_homefax")) //$NON-NLS-1$
							exportField(column++, j+1, member.getHomefax());
						else if(method.getName().equalsIgnoreCase("getDirectory_homephone")) //$NON-NLS-1$
							exportField(column++, j+1, member.getHomephone());
						else if(method.getName().equalsIgnoreCase("getDirectory_mobile")) //$NON-NLS-1$
							exportField(column++, j+1, member.getMobilephone());
						else if(method.getName().equalsIgnoreCase("getDirectory_phone")) //$NON-NLS-1$
							exportField(column++, j+1, member.getPhone());
						else if(method.getName().equalsIgnoreCase("getDirectory_website")) //$NON-NLS-1$
							exportField(column++, j+1, member.getWebsite());
						else if(method.getName().equalsIgnoreCase("getRegister_address")){ //$NON-NLS-1$
							if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian") && member.getDirectory_officeaddress() != null && member.getDirectory_officeaddress()){ //$NON-NLS-1$
								exportField(column++, j+1, member.getStreet());
								exportField(column++, j+1, member.getZip());
								exportField(column++, j+1, member.getCity());
								exportField(column++, j+1, member.getState());
								exportField(column++, j+1, member.getCountry());
							}
							else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german") && member.getDirectory_officeaddress() != null && member.getDirectory_officeaddress()){ //$NON-NLS-1$
								exportField(column++, j+1, member.getStreet_de());
								exportField(column++, j+1, member.getZip());
								exportField(column++, j+1, member.getCity_de());
								exportField(column++, j+1, member.getState_de());
								exportField(column++, j+1, member.getCountry_de());
							}								
							else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getHomestreet());
								exportField(column++, j+1, member.getHomezip());
								exportField(column++, j+1, member.getHomecity());
								exportField(column++, j+1, member.getHomestate());
								exportField(column++, j+1, member.getHomecountry());
							}
							else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getHomestreet_de());
								exportField(column++, j+1, member.getHomezip());
								exportField(column++, j+1, member.getHomecity_de());
								exportField(column++, j+1, member.getHomestate_de());
								exportField(column++, j+1, member.getHomecountry_de());
							}
							else column+=5;
						}
						else if(method.getName().equalsIgnoreCase("getDirectory_officeaddress")){ //$NON-NLS-1$
							if(member.getDirectory_officeaddress()!=null && member.getDirectory_officeaddress()){
								if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("italian")){ //$NON-NLS-1$
									exportField(column++, j+1, member.getStreet());
									exportField(column++, j+1, member.getZip());
									exportField(column++, j+1, member.getCity());
									exportField(column++, j+1, member.getState());
									exportField(column++, j+1, member.getCountry());
								}
								else if(member.getRegister_address()!=null && member.getRegister_address().equalsIgnoreCase("german")){ //$NON-NLS-1$
									exportField(column++, j+1, member.getStreet_de());
									exportField(column++, j+1, member.getZip());
									exportField(column++, j+1, member.getCity_de());
									exportField(column++, j+1, member.getState_de());
									exportField(column++, j+1, member.getCountry_de());
								}
								else column+=5;
							}
							else column+=5;
						}
						else if(method.getName().equalsIgnoreCase("getStdaddress")){ //$NON-NLS-1$
							if(member.getStdaddress()!=null && member.getStdaddress().equals("address")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getStreet());
								exportField(column++, j+1, member.getZip());
								exportField(column++, j+1, member.getCity());
								exportField(column++, j+1, member.getState());
								exportField(column++, j+1, member.getCountry());
							}
							else if(member.getStdaddress()!=null && member.getStdaddress().equals("address_de")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getStreet_de());
								exportField(column++, j+1, member.getZip());
								exportField(column++, j+1, member.getCity_de());
								exportField(column++, j+1, member.getState_de());
								exportField(column++, j+1, member.getCountry_de());
							}
							else if(member.getStdaddress()!=null && member.getStdaddress().equals("homeaddress")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getHomestreet());
								exportField(column++, j+1, member.getHomezip());
								exportField(column++, j+1, member.getHomecity());
								exportField(column++, j+1, member.getHomestate());
								exportField(column++, j+1, member.getHomecountry());
							}
							else if(member.getStdaddress()!=null && member.getStdaddress().equals("homeaddress_de")){ //$NON-NLS-1$
								exportField(column++, j+1, member.getHomestreet_de());
								exportField(column++, j+1, member.getHomezip());
								exportField(column++, j+1, member.getHomecity_de());
								exportField(column++, j+1, member.getHomestate_de());
								exportField(column++, j+1, member.getHomecountry_de());
							}
							else column+=5;
						}
						else
							if(method.getName().equalsIgnoreCase("getAddress_de") || method.getName().equalsIgnoreCase("getAddress_it") || method.getName().equalsIgnoreCase("getAddress_ms") || //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									method.getName().equalsIgnoreCase("getHomeaddress_de") || method.getName().equalsIgnoreCase("getHomeaddress_it") || method.getName().equalsIgnoreCase("getHomeaddress_ms")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
									for(String value : (List<String>)(method.invoke(member, (Object[])null))){
										if(value==null)
											exportField(column++, j+1 , ""); //$NON-NLS-1$
										else
											exportField(column++, j+1 , value);
									}
						else
							exportField(column++, j+1, (method.invoke(member, (Object[])null)));
					} catch (IllegalAccessException e) {
						System.out.println(e);
					} catch (InvocationTargetException e) {
						System.out.println(e);
					}
				}
			}
		}
		catch (ConnectionException e){
//			MessageDialog.openError(, "Error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		catch (Throwable t){
//			MessageDialog.openError(, "Error", e.getLocalizedMessage());
			t.printStackTrace();
		}
		try{
			workbook.write();
			workbook.close();
			Program.launch(file.toString());
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		catch(WriteException ex){
			ex.printStackTrace();
		}
		
		return true;
	}

	public void exportField(int x, int y, Object obj){
		if (obj==null) return;
		try{
			if (obj instanceof Integer){
				sheet.addCell(new Number(x,y,new Double(obj.toString())));
			}
			else if (obj instanceof Date){
				WritableCellFormat format = new WritableCellFormat (new DateFormat ("dd MMM yyyy")); //$NON-NLS-1$
				sheet.addCell(new DateTime(x,y, (Date) obj, format));
			}
			else {
				sheet.addCell(new Label(x,y,obj.toString()));
			}
		}
		catch(RowsExceededException ex){
			ex.printStackTrace();
		}
		catch(WriteException ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void addPages() {

		one = new ExportOptionWizardPageOne("one"); //$NON-NLS-1$
		two = new ExportOptionWizardPageTwo("two"); //$NON-NLS-1$
		three = new ExportOptionWizardPageThree("three"); //$NON-NLS-1$
		addPage(one);
		addPage(two);
		addPage(three);
	}
	
	public void initFields(){
		fields = new TreeMap<String, String>();

		fields.put("gender_de", 				Messages.ExportOptionWizard_gender_de); 						//$NON-NLS-1$
		fields.put("gender_it", 				Messages.ExportOptionWizard_gender_it); 						//$NON-NLS-1$
		fields.put("gender_ms", 				Messages.ExportOptionWizard_gender_ws); 						//$NON-NLS-1$

		fields.put("title", 					Messages.ExportOptionWizard_title);							//$NON-NLS-1$
		fields.put("firstname", 				Messages.ExportOptionWizard_firstname); 						//$NON-NLS-1$
		fields.put("lastname", 					Messages.ExportOptionWizard_lastname); 						//$NON-NLS-1$
	
		fields.put("nationality",				Messages.ExportOptionWizard_nationality);						//$NON-NLS-1$

		fields.put("profession_de",				Messages.ExportOptionWizard_profession_de);					//$NON-NLS-1$
		fields.put("profession_it",				Messages.ExportOptionWizard_profession_it);					//$NON-NLS-1$
		fields.put("profession_ms",				Messages.ExportOptionWizard_profession_ms);					//$NON-NLS-1$	

		fields.put("birthdate",					Messages.ExportOptionWizard_birthdate);						//$NON-NLS-1$

		fields.put("birthplace_de",				Messages.ExportOptionWizard_birthplace_de);					//$NON-NLS-1$
		fields.put("birthplace",				Messages.ExportOptionWizard_birthplace_it);					//$NON-NLS-1$
		fields.put("birthplace_ms",				Messages.ExportOptionWizard_birthplace_ms);					//$NON-NLS-1$
		
		fields.put("graduation_date",			Messages.ExportOptionWizard_graduationdate);					//$NON-NLS-1$
		fields.put("graduation_institute",	 	Messages.ExportOptionWizard_graduationinstitute);				//$NON-NLS-1$
		fields.put("state_exam_year",			Messages.ExportOptionWizard_stateexam);						//$NON-NLS-1$
		fields.put("habilitation",				Messages.ExportOptionWizard_habilitation);					//$NON-NLS-1$

		fields.put("registration_date",			Messages.ExportOptionWizard_registrationdate);				//$NON-NLS-1$
		fields.put("registration_exemption",	Messages.ExportOptionWizard_registrationexemption);			//$NON-NLS-1$
		fields.put("sector",					Messages.ExportOptionWizard_sector);							//$NON-NLS-1$
		fields.put("section_it",				Messages.ExportOptionWizard_section_it);						//$NON-NLS-1$
		fields.put("section_de",				Messages.ExportOptionWizard_section_de);						//$NON-NLS-1$
		fields.put("section_ms",				Messages.ExportOptionWizard_section_ms);						//$NON-NLS-1$
		fields.put("sector_date",				Messages.ExportOptionWizard_sectordate);						//$NON-NLS-1$
		fields.put("sector_habilitation",		Messages.ExportOptionWizard_habilitation);					//$NON-NLS-1$
		fields.put("sector_exemption",		 	Messages.ExportOptionWizard_sectorexemption);					//$NON-NLS-1$
		fields.put("registration_number",		Messages.ExportOptionWizard_registrationnumber);				//$NON-NLS-1$

		fields.put("homephone",					Messages.ExportOptionWizard_homephone);						//$NON-NLS-1$
		fields.put("directory_homephone",		Messages.ExportOptionWizard_directory_homephone);				//$NON-NLS-1$

		fields.put("phone",						Messages.ExportOptionWizard_phone);							//$NON-NLS-1$
		fields.put("directory_phone",			Messages.ExportOptionWizard_directory_phone);					//$NON-NLS-1$
		
		fields.put("homefax",					Messages.ExportOptionWizard_homefax);							//$NON-NLS-1$
		fields.put("directory_homefax",			Messages.ExportOptionWizard_directory_homefax);				//$NON-NLS-1$

		fields.put("fax",						Messages.ExportOptionWizard_fax);								//$NON-NLS-1$
		fields.put("directory_fax",				Messages.ExportOptionWizard_directory_fax);					//$NON-NLS-1$
		
		fields.put("mobilephone",				Messages.ExportOptionWizard_mobile);							//$NON-NLS-1$
		fields.put("directory_mobile",		 	Messages.ExportOptionWizard_directory_mobile);				//$NON-NLS-1$

		fields.put("email",						Messages.ExportOptionWizard_email);							//$NON-NLS-1$
		fields.put("directory_email", 			Messages.ExportOptionWizard_directory_email);					//$NON-NLS-1$

		fields.put("website",					Messages.ExportOptionWizard_website);							//$NON-NLS-1$
		fields.put("directory_website", 		Messages.ExportOptionWizard_directory_website);				//$NON-NLS-1$

		fields.put("newsletter",				Messages.ExportOptionWizard_newsletter);						//$NON-NLS-1$
		fields.put("culturenewsletter",			Messages.ExportOptionWizard_informationnewsletter);			//$NON-NLS-1$
	
		fields.put("tax_code",					Messages.ExportOptionWizard_taxcode);							//$NON-NLS-1$
		fields.put("fiscal_code",				Messages.ExportOptionWizard_fiscalcode);						//$NON-NLS-1$

		fields.put("address_de",				Messages.ExportOptionWizard_address_de);						//$NON-NLS-1$
		fields.put("address_it",				Messages.ExportOptionWizard_address_it);						//$NON-NLS-1$
		fields.put("address_ms",				Messages.ExportOptionWizard_address_ms);						//$NON-NLS-1$

		fields.put("homeaddress_de", 			Messages.ExportOptionWizard_homeaddress_de);					//$NON-NLS-1$
		fields.put("homeaddress_it", 			Messages.ExportOptionWizard_homeaddress_it);					//$NON-NLS-1$
		fields.put("homeaddress_ms", 			Messages.ExportOptionWizard_homeaddress_ms);					//$NON-NLS-1$

		fields.put("stdaddress",				Messages.ExportOptionWizard_standardaddress);					//$NON-NLS-1$
		fields.put("register_address",			Messages.ExportOptionWizard_registerAddress);					//$NON-NLS-1$
		
		fields.put("deregister_date",			Messages.ExportOptionWizard_deregistrationdate);				//$NON-NLS-1$
		fields.put("deregister_reason",			Messages.ExportOptionWizard_deregistrationreason);			//$NON-NLS-1$
		fields.put("deregister_exemption",		Messages.ExportOptionWizard_deregistrationexeption);			//$NON-NLS-1$
		
		fields.put("note", 						Messages.ExportOptionWizard_note);							//$NON-NLS-1$
	}
	
	public Map<String, String> getFields() {
		if (fields==null)
			initFields();
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
}
