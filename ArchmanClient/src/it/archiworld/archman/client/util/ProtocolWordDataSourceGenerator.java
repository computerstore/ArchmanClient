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
package it.archiworld.archman.client.util;


import it.archiworld.common.Company;
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.ServiceMember;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.Outentry;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProtocolWordDataSourceGenerator extends WordDataSourceGenerator {

	private Outentry protocol;
	private Iterator<EntryAddress> receipients;

	private Boolean legend;

	private List<String> column_labels;

	private Map<String, String> columns;

	private DateFormat date_format;

	/*
	 * private String[] column_labels = { "Attenzione", "Numero_Protocollo",
	 * "Data_Protocollo", "Data_Invio", "Responsabile", "Segretario",
	 * "RifVosta", "RifNostra", "Oggetto", "Allegati", "NumAllegati",
	 * "VociInArchivio1", "VociInArchivio2", "Titolo", "Cognome", "Nome",
	 * "Città", "Indirizzo", "Cap", "Nazione", "TelefonoAbitazione",
	 * "TelefonoCellulare", "NumeroFaxCasa", "CAPUfficio", "TelefonoUfficio",
	 * "NumeroFaxUfficio", "IndirizzoPostaElettr", "Coordinate_Bancarie",
	 * "Nazionalità", "CodiceFiscale", "PartitaIVA", "NomeOrganizzazione",
	 * "NomeUfficio", "NomeRipartizione", "NomeEnte", "PartitaIVAPersGiurid",
	 * "PartitaIVAEnte", "RecapitoCorrisponden", "Tipo", "NumeroProtocollo" };
	 */

	public ProtocolWordDataSourceGenerator(String filename, Outentry protocol) {
		super(filename);
		this.column_labels = new ArrayList<String>();
		this.columns = new HashMap<String, String>();
		this.protocol = protocol;
		this.receipients = this.protocol.getDestinations().iterator();
		this.legend = true;
		this.date_format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
				DateFormat.DEFAULT);
	}

	protected void registerField(String name) {
		this.column_labels.add(name);
	}

	protected void registerField(Integer position, String name) {
		this.column_labels.add(position, name);
	}

	protected void setField(Integer position, String value) {
		this.columns.put(this.column_labels.get(position), value);
	}

	protected void setField(String name, String value) {
		this.columns.put(name, value);
	}

	protected void setAndRegisterField(String name, String value) {
		this.column_labels.add(name);
		this.columns.put(name, value);
	}

	protected void setAndRegisterField(Integer position, String name,
			String value) {
		this.column_labels.add(position, name);
		this.columns.put(name, value);
	}

	protected String getField(Integer index) {
		if (index < 0 || index >= column_labels.size())
			return null;
		String label = column_labels.get(index);
		if (label != null && columns.containsKey(label))
			return columns.get(label);
		else
			return null;
	}

	protected String getField(String label) {
		if (columns.containsKey(label))
			return columns.get(label);
		else
			return null;
	}

	protected String encodeText(String text) {
		return text;
	}

	protected void initializeFields() {
		if (this.protocol == null || !(this.protocol instanceof Outentry))
			this.protocol = new Outentry();
		setAndRegisterField("protocol", //$NON-NLS-1$
				(protocol.getProtocol() != null ? protocol.getProtocol()
						.toString() : null));
		setAndRegisterField("year", (protocol.getYear() != null ? protocol //$NON-NLS-1$
				.getYear().toString() : null));
		setAndRegisterField("protocol_date", //$NON-NLS-1$
				(protocol.getProtocol_date() != null ? date_format
						.format(protocol.getProtocol_date()) : null));
		System.out.println(protocol.getProtocol_date());
		System.out.println(date_format.format(protocol.getProtocol_date()));
		setAndRegisterField("subject", encodeText(protocol.getSubject())); //$NON-NLS-1$
		setAndRegisterField("registration_date", (protocol //$NON-NLS-1$
				.getRegistration_date() != null ? date_format.format(protocol
				.getRegistration_date()) : null));
		setAndRegisterField("owner", //$NON-NLS-1$
				(protocol.getOwner() != null ? protocol.getOwner()
						.toString() : null));

		setAndRegisterField("responsible", encodeText(protocol.getResponsible())); //$NON-NLS-1$

		setAndRegisterField("note", encodeText(protocol.getNote())); //$NON-NLS-1$
		setAndRegisterField("state", (protocol.getState() != null ? protocol //$NON-NLS-1$
				.getState() : null));
		setAndRegisterField("archive", encodeText(protocol.getArchive())); //$NON-NLS-1$

		registerField("entity_id"); //$NON-NLS-1$
		registerField("parent"); //$NON-NLS-1$
		registerField("type"); //$NON-NLS-1$
		registerField("marker"); //$NON-NLS-1$
		registerField("version"); //$NON-NLS-1$
		registerField("email"); //$NON-NLS-1$
		registerField("phone"); //$NON-NLS-1$
		registerField("mobilephone"); //$NON-NLS-1$
		registerField("fax"); //$NON-NLS-1$
		registerField("website"); //$NON-NLS-1$
		registerField("street"); //$NON-NLS-1$
		registerField("zip"); //$NON-NLS-1$
		registerField("city"); //$NON-NLS-1$
		registerField("state"); //$NON-NLS-1$
		registerField("country"); //$NON-NLS-1$
		registerField("tax_code"); //$NON-NLS-1$
		registerField("account_nr"); //$NON-NLS-1$
		registerField("bank_nr"); //$NON-NLS-1$
		registerField("iban"); //$NON-NLS-1$
		registerField("bic"); //$NON-NLS-1$
		registerField("recipient_note"); //$NON-NLS-1$

		registerField("title"); //$NON-NLS-1$
		registerField("firstname"); //$NON-NLS-1$
		registerField("lastname"); //$NON-NLS-1$

		registerField("denomination"); //$NON-NLS-1$

		registerField("fiscal_code"); //$NON-NLS-1$

		registerField("gender"); //$NON-NLS-1$
		registerField("nationality"); //$NON-NLS-1$
		registerField("profession"); //$NON-NLS-1$
		registerField("sector"); //$NON-NLS-1$
		registerField("sector_date"); //$NON-NLS-1$
		registerField("sector_habilitation"); //$NON-NLS-1$
		registerField("sector_exemption"); //$NON-NLS-1$
		registerField("member_registraion_date"); //$NON-NLS-1$
		registerField("member_registraion_exemption"); //$NON-NLS-1$
		registerField("graduation_date"); //$NON-NLS-1$
		registerField("graduation_institute"); //$NON-NLS-1$
		registerField("graduation_approval_date"); //$NON-NLS-1$
		registerField("habilitation"); //$NON-NLS-1$
		registerField("state_exam_year"); //$NON-NLS-1$
		registerField("registration_number"); //$NON-NLS-1$
		registerField("deregister_date"); //$NON-NLS-1$
		registerField("deregister_reason"); //$NON-NLS-1$
		registerField("deregister_exemption"); //$NON-NLS-1$
		registerField("last_change_date"); //$NON-NLS-1$
		registerField("birthplace_de"); //$NON-NLS-1$
		registerField("birthplace"); //$NON-NLS-1$
		registerField("birthdate"); //$NON-NLS-1$
		registerField("register_address"); //$NON-NLS-1$
		registerField("newsletter"); //$NON-NLS-1$
		registerField("culturenewsletter"); //$NON-NLS-1$
		registerField("directory_homephone"); //$NON-NLS-1$
		registerField("directory_homefax"); //$NON-NLS-1$
		registerField("directory_phone"); //$NON-NLS-1$
		registerField("directory_fax"); //$NON-NLS-1$
		registerField("directory_mobile"); //$NON-NLS-1$
		registerField("directory_email"); //$NON-NLS-1$
		registerField("directory_website"); //$NON-NLS-1$
		registerField("directory_officeaddress"); //$NON-NLS-1$

		registerField("originstate"); //$NON-NLS-1$
		registerField("birthplace"); //$NON-NLS-1$
		registerField("birthdate"); //$NON-NLS-1$
		registerField("chamber"); //$NON-NLS-1$
		registerField("title_approval_examption"); //$NON-NLS-1$
		registerField("exemption"); //$NON-NLS-1$
		registerField("lastrecord"); //$NON-NLS-1$
	}

	protected void updateFields(EntryAddress address) {
		if (address == null)
			return;
		setField("entity_id", address.getEntity_id().toString()); //$NON-NLS-1$
/*		if(address.getParent()!=null)
			setField("parent", address.getParent().toString()); //$NON-NLS-1$
		else setField("parent", ""); //$NON-NLS-1$ //$NON-NLS-2$
		setField("type", address.getType()); //$NON-NLS-1$
		if(address.getMarker()!=null)
			setField("marker", address.getMarker().toString()); //$NON-NLS-1$
		else
			setField("marker", ""); //$NON-NLS-1$ //$NON-NLS-2$
		setField("version", address.getVersion()); //$NON-NLS-1$
		setField("email", encodeText(address.getEmail())); //$NON-NLS-1$
		setField("mobilephone", encodeText(address.getMobilephone())); //$NON-NLS-1$
		setField("website", encodeText(address.getWebsite())); //$NON-NLS-1$
		setField("tax_code", encodeText(address.getTax_code())); //$NON-NLS-1$
		setField("account_nr", encodeText(address.getAccount_nr())); //$NON-NLS-1$
		setField("bank_nr", encodeText(address.getBank_nr())); //$NON-NLS-1$
		setField("iban", encodeText(address.getIban())); //$NON-NLS-1$
		setField("bic", encodeText(address.getBic())); //$NON-NLS-1$
		setField("recipient_note", (address.getNote())); //$NON-NLS-1$
*/		if (address.getAddress() instanceof Company) {
			Company company = (Company) address.getAddress();
			setField("denomination", encodeText(company.getDenomination())); //$NON-NLS-1$
			setField("title", encodeText(company.getContact_title())); //$NON-NLS-1$
			setField("firstname", encodeText(company.getContact_firstname())); //$NON-NLS-1$
			setField("lastname", encodeText(company.getContact_lastname())); //$NON-NLS-1$
			setField("phone", encodeText(company.getPhone())); //$NON-NLS-1$
			setField("fax", encodeText(company.getFax())); //$NON-NLS-1$
			setField("street", encodeText(company.getStreet())); //$NON-NLS-1$
			setField("zip", encodeText(company.getZip())); //$NON-NLS-1$
			setField("city", encodeText(company.getCity())); //$NON-NLS-1$
			setField("state", encodeText(company.getState())); //$NON-NLS-1$
			setField("country", encodeText(company.getCountry())); //$NON-NLS-1$
		} else if (address.getAddress() instanceof ServiceMember) {
			ServiceMember servicemember = (ServiceMember) address.getAddress();
			setField("title", encodeText(servicemember.getTitle())); //$NON-NLS-1$
			setField("firstname", encodeText(servicemember.getFirstname())); //$NON-NLS-1$
			setField("lastname", encodeText(servicemember.getLastname())); //$NON-NLS-1$
			setField("fiscal_code", encodeText(servicemember.getFiscal_code())); //$NON-NLS-1$
			setField("phone", encodeText(servicemember.getPhone())); //$NON-NLS-1$
			setField("fax", encodeText(servicemember.getFax())); //$NON-NLS-1$
			setField("street", encodeText(servicemember.getStreet())); //$NON-NLS-1$
			setField("zip", encodeText(servicemember.getZip())); //$NON-NLS-1$
			setField("city", encodeText(servicemember.getCity())); //$NON-NLS-1$
			setField("state", encodeText(servicemember.getState())); //$NON-NLS-1$
			setField("country", encodeText(servicemember.getCountry())); //$NON-NLS-1$

			setField("originstate", encodeText(servicemember.getOriginstate())); //$NON-NLS-1$
			setField("birthplace", encodeText(servicemember.getBirthplace())); //$NON-NLS-1$
			if(servicemember.getBirthdate() != null)
				setField("birthdate", date_format.format(servicemember //$NON-NLS-1$
					.getBirthdate()));
			setField("chamber", encodeText(servicemember.getChamber())); //$NON-NLS-1$
			setField("title_approval_examption", encodeText(servicemember //$NON-NLS-1$
					.getTitle_approval_exemption()));
			setField("exemption", encodeText(servicemember.getExemption())); //$NON-NLS-1$
		} else if (address.getAddress() instanceof Member) {
			Member member = (Member) address.getAddress();
			setField("title", encodeText(member.getTitle())); //$NON-NLS-1$
			setField("firstname", encodeText(member.getFirstname())); //$NON-NLS-1$
			setField("lastname", encodeText(member.getLastname())); //$NON-NLS-1$
			if (member.getStdaddress().equals("address_de")) { //$NON-NLS-1$
				setField("phone", encodeText(member.getPhone())); //$NON-NLS-1$
				setField("fax", encodeText(member.getFax())); //$NON-NLS-1$
				setField("street", encodeText(member.getStreet_de())); //$NON-NLS-1$
				setField("zip", encodeText(member.getZip())); //$NON-NLS-1$
				setField("city", encodeText(member.getCity_de())); //$NON-NLS-1$
				setField("state", encodeText(member.getState_de())); //$NON-NLS-1$
				setField("country", encodeText(member.getCountry_de())); //$NON-NLS-1$
			} else if (member.getStdaddress().equals("homeaddress_de")) { //$NON-NLS-1$
				setField("phone", encodeText(member.getHomephone())); //$NON-NLS-1$
				setField("fax", encodeText(member.getHomefax())); //$NON-NLS-1$
				setField("street", encodeText(member.getHomestreet_de())); //$NON-NLS-1$
				setField("zip", encodeText(member.getHomezip())); //$NON-NLS-1$
				setField("city", encodeText(member.getHomecity_de())); //$NON-NLS-1$
				setField("state", encodeText(member.getHomestate_de())); //$NON-NLS-1$
				setField("country", encodeText(member.getHomecountry_de())); //$NON-NLS-1$
			} else if (member.getStdaddress().equals("address")) { //$NON-NLS-1$
				setField("phone", encodeText(member.getPhone())); //$NON-NLS-1$
				setField("fax", encodeText(member.getFax())); //$NON-NLS-1$
				setField("street", encodeText(member.getStreet())); //$NON-NLS-1$
				setField("zip", encodeText(member.getZip())); //$NON-NLS-1$
				setField("city", encodeText(member.getCity())); //$NON-NLS-1$
				setField("state", encodeText(member.getState())); //$NON-NLS-1$
				setField("country", encodeText(member.getCountry())); //$NON-NLS-1$
			} else if (member.getStdaddress().equals("homeaddress")) { //$NON-NLS-1$
				setField("phone", encodeText(member.getHomephone())); //$NON-NLS-1$
				setField("fax", encodeText(member.getHomefax())); //$NON-NLS-1$
				setField("street", encodeText(member.getHomestreet())); //$NON-NLS-1$
				setField("zip", encodeText(member.getHomezip())); //$NON-NLS-1$
				setField("city", encodeText(member.getHomecity())); //$NON-NLS-1$
				setField("state", encodeText(member.getHomestate())); //$NON-NLS-1$
				setField("country", encodeText(member.getHomecountry()));				 //$NON-NLS-1$
			}
			setField("gender", encodeText(member.getGender())); //$NON-NLS-1$
			setField("nationality", encodeText(member.getNationality())); //$NON-NLS-1$
			setField("profession", encodeText(member.getProfession())); //$NON-NLS-1$
			setField("sector", encodeText(member.getSector())); //$NON-NLS-1$
			if(member.getSector_date() != null)
				setField("sector_date", date_format.format(member.getSector_date())); //$NON-NLS-1$
			setField("sector_habilitation", encodeText(member.getSector_habilitation())); //$NON-NLS-1$
			setField("sector_exemption", encodeText(member.getSector_exemption())); //$NON-NLS-1$
			if(member.getRegistration_date() != null)
				setField("member_registraion_date", date_format.format(member.getRegistration_date())); //$NON-NLS-1$
			setField("member_registraion_exemption", encodeText(member.getRegistration_exemption())); //$NON-NLS-1$
			if(member.getGraduation_date() != null)
				setField("graduation_date", date_format.format(member.getGraduation_date())); //$NON-NLS-1$
			setField("graduation_institute", encodeText(member.getGraduation_institute())); //$NON-NLS-1$
			if(member.getGraduation_approval_date() != null)
				setField("graduation_approval_date", date_format.format(member.getGraduation_approval_date())); //$NON-NLS-1$
			setField("habilitation", encodeText(member.getHabilitation())); //$NON-NLS-1$
			setField("state_exam_year", encodeText(member.getState_exam_year())); //$NON-NLS-1$
			setField("registration_number", member.getRegistration_number().toString()); //$NON-NLS-1$
			if(member.getDeregister_date() != null)			
				setField("deregister_date", date_format.format(member.getDeregister_date())); //$NON-NLS-1$
			setField("deregister_reason", encodeText(member.getDeregister_reason())); //$NON-NLS-1$
			setField("deregister_exemption", encodeText(member.getDeregister_exemption())); //$NON-NLS-1$
			if(member.getLast_change_date() != null)
				setField("last_change_date", date_format.format(member.getLast_change_date())); //$NON-NLS-1$
			setField("birthplace_de", encodeText(member.getBirthplace_de())); //$NON-NLS-1$
			setField("birthplace", encodeText(member.getBirthplace())); //$NON-NLS-1$
			if(member.getBirthdate() != null)
				setField("birthdate", date_format.format(member.getBirthdate())); //$NON-NLS-1$
			setField("register_address", encodeText(member.getRegister_address())); //$NON-NLS-1$
			if(member.getNewsletter()!= null)
				setField("newsletter", (member.getNewsletter() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getCulturenewsletter()!= null)
				setField("culturenewsletter", (member.getCulturenewsletter() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_homephone()!= null)
				setField("directory_homephone", (member.getDirectory_homephone() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_homefax() != null)
				setField("directory_homefax", (member.getDirectory_homefax() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_phone()!=null)
				setField("directory_phone", (member.getDirectory_phone() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_homefax()!=null)
				setField("directory_fax", (member.getDirectory_fax() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_mobile()!=null)
				setField("directory_mobile", (member.getDirectory_mobile() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_email()!=null)
				setField("directory_email", (member.getDirectory_email() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_website()!=null)
				setField("directory_website", (member.getDirectory_website() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if(member.getDirectory_officeaddress()!=null)
				setField("directory_officeaddress", (member.getDirectory_officeaddress() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		else if (address.getAddress() instanceof Person) {
			Person person = (Person) address.getAddress();
			setField("title", encodeText(person.getTitle())); //$NON-NLS-1$
			setField("firstname", encodeText(person.getFirstname())); //$NON-NLS-1$
			setField("lastname", encodeText(person.getLastname())); //$NON-NLS-1$
			setField("fiscal_code", encodeText(person.getFiscal_code())); //$NON-NLS-1$
			setField("phone", encodeText(person.getPhone())); //$NON-NLS-1$
			setField("fax", encodeText(person.getFax())); //$NON-NLS-1$
			setField("street", encodeText(person.getStreet())); //$NON-NLS-1$
			setField("zip", encodeText(person.getZip())); //$NON-NLS-1$
			setField("city", encodeText(person.getCity())); //$NON-NLS-1$
			setField("state", encodeText(person.getState())); //$NON-NLS-1$
			setField("country", encodeText(person.getCountry())); //$NON-NLS-1$
		}
		setField("lastrecord", (!this.receipients.hasNext() ? "Yes" : "No")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public String fetchLine() {
		String line;
		EntryAddress address;
		if (this.legend) {
			initializeFields();
			this.legend = false;
			line = new String();
			for (int i = 0; i < this.column_labels.size(); i++) {
				String label = this.column_labels.get(i);
				if (label == null)
					label = "#" + i; //$NON-NLS-1$
				line += "\"" + label + "\""; //$NON-NLS-1$ //$NON-NLS-2$
				if (i < this.column_labels.size() - 1)
					line += "\t"; //$NON-NLS-1$
			}
			return line;
		}
		if (this.receipients == null || this.receipients.hasNext() == false)
			return null;
		address = receipients.next();
		if (address == null)
			return null;
		
		//FIXXXME Quick Hack to clear unused columns.
		// Has to be replaced with something efficient.
		this.column_labels = new ArrayList<String>();
		this.columns = new HashMap<String, String>();
		initializeFields();
		
		updateFields(address);
		line = new String();
		for (int i = 0; i < this.column_labels.size(); i++) {
			String value = getField(i);
			if(value == null || value == "null") //$NON-NLS-1$
				value = ""; //$NON-NLS-1$
			line += "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			if (i < this.column_labels.size() - 1)
				line += "\t"; //$NON-NLS-1$
		}
		return line;
	}

}
