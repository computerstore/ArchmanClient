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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.archiworld.archman.client.crm.Messages;
import it.archiworld.common.Member;
import it.archiworld.common.committee.Committeemember;
import it.archiworld.common.member.Formation;
import it.archiworld.common.member.Renewal;
import it.archiworld.common.member.Specialization;

public class MemberExportDecorator extends Member {
	
	private static final long serialVersionUID = 1L;
	private Member member;
	
	public MemberExportDecorator(Member member){
		this.member=member;
	}
	
	public String getGender_de(){
		if(member.getGender().equals("male"))
			return "männlich";
		else
			return "weiblich";
	}

	public String getGender_it(){
		if(member.getGender().equals("male"))
			return "maschile";
		else
			return "femminile";
	}	
	
	public String getGender_ms(){
		if(member.getRegister_address().equalsIgnoreCase("italian"))
			return getGender_it();
		else
			return getGender_de();
	}
	
	public String getProfession_it(){
		if(member.getProfession()==null)
			return "";
		else if(member.getProfession().equals("male freelancer")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_MaleFreelancer_it; 
		else if(member.getProfession().equals("female freelancer")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_FemaleFreelancer_it; 
		else if(member.getProfession().equals("self-employment")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_SelfEmployment_it; 
		else if(member.getProfession().equals("teacher")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Teacher_it; 
		else if(member.getProfession().equals("employee")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Employee_it; 
		else if(member.getProfession().equals("associate")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Associate_it; 
		else if(member.getProfession().equals("other")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Other_it;
		return "";
	}
	
	public String getProfession_de(){
		if(member.getProfession()==null)
			return "";
		else if(member.getProfession().equals("male freelancer")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_MaleFreelancer_de; 
		else if(member.getProfession().equals("female freelancer")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_FemaleFreelancer_de; 
		else if(member.getProfession().equals("self-employment")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_SelfEmployment_de; 
		else if(member.getProfession().equals("teacher")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Teacher_de; 
		else if(member.getProfession().equals("employee")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Employee_de; 
		else if(member.getProfession().equals("associate")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Associate_de; 
		else if(member.getProfession().equals("other")) //$NON-NLS-1$
			return Messages.ExportActionDelegate_Other_de;
		return "";
	}
	
	public String getProfession_ms(){
		if(member.getRegister_address().equalsIgnoreCase("italian"))
			return getProfession_it();
		else
			return getProfession_de();
	}
	
	public String getBirthplace_ms(){
		if(member.getRegister_address().equalsIgnoreCase("italian"))
			return getBirthplace();
		else
			return getBirthplace_de();
	}
	
	public String getSector(){
		if(member.getSector()==null)
			return "";
		else if(member.getSector()!=null && member.getSector().charAt(1)=='A')
			return "A"; //$NON-NLS-1$
		else if(member.getSector()!=null && member.getSector().charAt(1)=='B')
			return "B"; //$NON-NLS-1$
		return "";
	}
	
	public String getSection_it(){
		if(member.getSector()==null)
			return "";
		else if(member.getSector()!=null && member.getSector().equals("[A]architect")){ //$NON-NLS-1$
			return "Architetto"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]urbanplaner")){ //$NON-NLS-1$
			return "Pianificatore"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]landscapedesigner")){ //$NON-NLS-1$
			return "Paesaggista"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]monumentconservator")){ //$NON-NLS-1$
			return "Conservatore"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorarchitect")){ //$NON-NLS-1$
			return "Architetto iunior"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorurbanplaner")){ //$NON-NLS-1$
			return "Pianificatore iunior"; //$NON-NLS-1$
		}
		return "";
	}
	public String getSection_de(){
		if(member.getSector()==null)
			return "";
		else if(member.getSector()!=null && member.getSector().equals("[A]architect")){ //$NON-NLS-1$
			return "Architekt"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]urbanplaner")){ //$NON-NLS-1$
			return "Raumplaner"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]landscapedesigner")){ //$NON-NLS-1$
			return "Landschaftsplaner"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[A]monumentconservator")){ //$NON-NLS-1$
			return "Denkmalpfleger"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorarchitect")){ //$NON-NLS-1$
			return "Iunior Architekt"; //$NON-NLS-1$
		} else if(member.getSector()!=null && member.getSector().equals("[B]iuniorurbanplaner")){ //$NON-NLS-1$
			return "Iunior Raumplaner"; //$NON-NLS-1$		
		}
		return "";
	}
		
	public String getSection_ms(){
		if(member.getRegister_address().equalsIgnoreCase("italian"))
			return getSection_it();
		else
			return getSection_de();
	}
	
	public List<String> getHomeaddress_de(){
		List<String> result = new ArrayList<String>();
		result.add(getHomestreet_de());
		result.add(getHomezip());
		result.add(getHomecity_de());
		result.add(getHomestate_de());
		result.add(getHomecountry_de());
		return result;
	}

	public List<String> getHomeaddress_it(){
		List<String> result = new ArrayList<String>();
		result.add(getHomestreet());
		result.add(getHomezip());
		result.add(getHomecity());
		result.add(getHomestate());
		result.add(getHomecountry());
		return result;
	}

	public List<String> getHomeaddress_ms(){
		if(getRegister_address().equalsIgnoreCase("italian"))
			return getHomeaddress_it();
		else
			return getHomeaddress_de();
	}

	public List<String> getAddress_it(){
		List<String> result = new ArrayList<String>();
		result.add(getStreet());
		result.add(getZip());
		result.add(getCity());
		result.add(getState());
		result.add(getCountry());
		return result;
	}

	public List<String> getAddress_de(){
		List<String> result = new ArrayList<String>();
		result.add(getStreet_de());
		result.add(getZip());
		result.add(getCity_de());
		result.add(getState_de());
		result.add(getCountry_de());
		return result;
	}

	public List<String> getAddress_ms(){
		if(getRegister_address().equalsIgnoreCase("italian"))
			return getAddress_it();
		else
			return getAddress_de();
	}

	//Override Methods
	
	@Override
	public Timestamp getBirthdate() {
		return member.getBirthdate();
	}

	@Override
	public String getBirthplace_de() {
		return member.getBirthplace_de();
	}

	@Override
	public String getBirthplace() {
		return member.getBirthplace();
	}

	@Override
	public String getCity_de() {
		return member.getCity_de();
	}

	@Override
	public Set<Committeemember> getCommitteemembers() {
		return member.getCommitteemembers();
	}

	@Override
	public String getCountry_de() {
		return member.getCountry_de();
	}

	@Override
	public Boolean getCulturenewsletter() {
		return member.getCulturenewsletter();
	}

	@Override
	public Timestamp getDeregister_date() {
		return member.getDeregister_date();
	}

	@Override
	public String getDeregister_exemption() {
		return member.getDeregister_exemption();
	}

	@Override
	public String getDeregister_reason() {
		return member.getDeregister_reason();
	}

	@Override
	public Boolean getDirectory_email() {
		return member.getDirectory_email();
	}

	@Override
	public Boolean getDirectory_fax() {
		return member.getDirectory_fax();
	}

	@Override
	public Boolean getDirectory_homefax() {
		return member.getDirectory_homefax();
	}

	@Override
	public Boolean getDirectory_homephone() {
		return member.getDirectory_homephone();
	}

	@Override
	public Boolean getDirectory_mobile() {
		return member.getDirectory_mobile();
	}

	@Override
	public Boolean getDirectory_officeaddress() {
		return member.getDirectory_officeaddress();
	}

	@Override
	public Boolean getDirectory_phone() {
		return member.getDirectory_phone();
	}

	@Override
	public Boolean getDirectory_website() {
		return member.getDirectory_website();
	}

	@Override
	public Set<Formation> getFormations() {
		return member.getFormations();
	}

	@Override
	public String getGender() {
		return member.getGender();
	}

	@Override
	public Timestamp getGraduation_approval_date() {
		return member.getGraduation_approval_date();
	}

	@Override
	public Timestamp getGraduation_date() {
		return member.getGraduation_date();
	}

	@Override
	public String getGraduation_institute() {
		return member.getGraduation_institute();
	}

	@Override
	public String getHabilitation() {
		return member.getHabilitation();
	}

	@Override
	public String getHomecity_de() {
		return member.getHomecity_de();
	}

	@Override
	public String getHomecity() {
		return member.getHomecity();
	}

	@Override
	public String getHomecountry_de() {
		return member.getHomecountry_de();
	}

	@Override
	public String getHomecountry() {
		return member.getHomecountry();
	}

	@Override
	public String getHomefax() {
		return member.getHomefax();
	}

	@Override
	public String getHomephone() {
		return member.getHomephone();
	}

	@Override
	public String getHomestate_de() {
		return member.getHomestate_de();
	}

	@Override
	public String getHomestate() {
		return member.getHomestate();
	}

	@Override
	public String getHomestreet_de() {
		return member.getHomestreet_de();
	}

	@Override
	public String getHomestreet() {
		return member.getHomestreet();
	}

	@Override
	public String getHomezip() {
		return member.getHomezip();
	}

	@Override
	public Timestamp getLast_change_date() {
		return member.getLast_change_date();
	}

	@Override
	public String getNationality() {
		return member.getNationality();
	}

	@Override
	public Boolean getNewsletter() {
		return member.getNewsletter();
	}

	@Override
	public String getProfession() {
		return member.getProfession();
	}

	@Override
	public String getRegister_address() {
		return member.getRegister_address();
	}

	@Override
	public Timestamp getRegistration_date() {
		return member.getRegistration_date();
	}

	@Override
	public String getRegistration_exemption() {
		return member.getRegistration_exemption();
	}

	@Override
	public Integer getRegistration_number() {
		return member.getRegistration_number();
	}

	@Override
	public Set<Renewal> getRenewal() {
		return member.getRenewal();
	}

	@Override
	public Timestamp getSector_date() {
		return member.getSector_date();
	}

	@Override
	public String getSector_exemption() {
		return member.getSector_exemption();
	}

	@Override
	public String getSector_habilitation() {
		return member.getSector_habilitation();
	}

	@Override
	public Set<Specialization> getSpecializations() {
		return member.getSpecializations();
	}

	@Override
	public String getState_de() {
		return member.getState_de();
	}

	@Override
	public String getState_exam_year() {
		return member.getState_exam_year();
	}

	@Override
	public String getStdaddress() {
		return member.getStdaddress();
	}

	@Override
	public String getStreet_de() {
		return member.getStreet_de();
	}

	@Override
	public String getFirstname() {
		return member.getFirstname();
	}

	@Override
	public String getLastname() {
		return member.getLastname();
	}

	@Override
	public String getTitle() {
		return member.getTitle();
	}

	@Override
	public String getAccount_nr() {
		return member.getAccount_nr();
	}

	@Override
	public String getBank_nr() {
		return member.getBank_nr();
	}

	@Override
	public String getBic() {
		return member.getBic();
	}

	@Override
	public String getCity() {
		return member.getCity();
	}

	@Override
	public String getCountry() {
		return member.getCountry();
	}

	@Override
	public String getEmail() {
		return member.getEmail();
	}

	@Override
	public Long getEntity_id() {
		return member.getEntity_id();
	}

	@Override
	public String getFax() {
		return member.getFax();
	}

	@Override
	public String getFiscal_code() {
		return member.getFiscal_code();
	}

	@Override
	public String getIban() {
		return member.getIban();
	}

	@Override
	public Integer getMarker() {
		return member.getMarker();
	}

	@Override
	public String getMobilephone() {
		return member.getMobilephone();
	}

	@Override
	public String getNote() {
		return member.getNote();
	}

	@Override
	public Integer getParent() {
		return member.getParent();
	}

	@Override
	public String getPhone() {
		return member.getPhone();
	}

	@Override
	public String getState() {
		return member.getState();
	}

	@Override
	public String getStreet() {
		return member.getStreet();
	}

	@Override
	public String getTax_code() {
		return member.getTax_code();
	}

	@Override
	public String getType() {
		return member.getType();
	}

	@Override
	public String getVersion() {
		return member.getVersion();
	}

	@Override
	public String getWebsite() {
		return member.getWebsite();
	}

	@Override
	public String getZip() {
		return member.getZip();
	}
	
	//Override Methods
}
