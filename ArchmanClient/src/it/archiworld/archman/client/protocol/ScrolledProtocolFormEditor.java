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
package it.archiworld.archman.client.protocol;

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.util.ComboBoxEnumSWTObservable;
import it.archiworld.archman.client.core.util.ComboKeyListener;
import it.archiworld.archman.client.protocol.util.ArchivePreferencePage;
import it.archiworld.archman.client.protocol.util.ResponsiblePreferencePage;
import it.archiworld.archman.client.util.RCPEJBPreferenceStore;
import it.archiworld.common.protocol.Entry;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class ScrolledProtocolFormEditor extends ScrolledFormEditorBase {
	
	public static int EMERGENCY = 1;
	public static int HISTORY = 2;
	
	protected Entry entry;
	protected Boolean history;

	Text subjectText;
	String[] res = new String[0];
	String[] voices = new String[0];
	/*			{ 
			"", //$NON-NLS-1$
			"A/CON/BANCA.ESTR.C.C.", //$NON-NLS-1$
			"A/CON/BANCA.FATTU.", //$NON-NLS-1$
			"A/CON/BANCA.TITOLI", //$NON-NLS-1$
			"A/CON/CASSA.DOC.SPES", //$NON-NLS-1$
			"A/CON/CASSA.GIORNALE", //$NON-NLS-1$
			"A/CON/CASSA.RICEV.ASS.", //$NON-NLS-1$
			"A/CON/CONT.GIORN.BILAN.", //$NON-NLS-1$
			"A/CON/DICH.770.760.", //$NON-NLS-1$
			"A/CON/SITUAZ.PATRIM.", //$NON-NLS-1$
			"A/SED/ARREDO.MOBILI", //$NON-NLS-1$
			"A/SED/CANTINA", //$NON-NLS-1$
			"A/SED/CONTR.AFFITTO", //$NON-NLS-1$
			"A/SED/LAV.RISTRUTT.", //$NON-NLS-1$
			"A/SED/MANUT.PULIZIA", //$NON-NLS-1$
			"A/SED/PREV.VARI", //$NON-NLS-1$
			"A/SED/PROGR.COMPUT.", //$NON-NLS-1$
			"A/SED/SPESE.AFFITTO", //$NON-NLS-1$
			"A/SEG/CANCELL.", //$NON-NLS-1$
			"A/SEG/CARTA.INTEST.", //$NON-NLS-1$
			"A/SEG/CONTRATTO", //$NON-NLS-1$
			"A/SEG/CORRISP.ENTRATA", //$NON-NLS-1$
			"A/SEG/CORRISP.USCITA", //$NON-NLS-1$
			"A/SEG/DELIBERE", //$NON-NLS-1$
			"A/SEG/PAGA.INAIL", //$NON-NLS-1$
			"A/SEG/PRESENZE", //$NON-NLS-1$
			"A/SEG/STATISTICHE", //$NON-NLS-1$
			"A/SEG/BUSTE", //$NON-NLS-1$
			"C/CEE/CONC.ATTUALI", //$NON-NLS-1$
			"C/CEE/CONC.SVOLTI", //$NON-NLS-1$
			"C/CON/BANDO.TIPO", //$NON-NLS-1$
			"C/CON/INTERNAZ.", //$NON-NLS-1$
			"C/CON/NORM.CEE", //$NON-NLS-1$
			"C/CON/NORM.PROV.", //$NON-NLS-1$
			"C/CON/NORM.STATO", //$NON-NLS-1$
			"C/GAR/ATTUALI", //$NON-NLS-1$
			"C/GAR/NORM.", //$NON-NLS-1$
			"C/GAR/SVOLTE", //$NON-NLS-1$
			"C/NAZ/CONC.ATTUALI", //$NON-NLS-1$
			"C/NAZ/CONC.SVOLTI", //$NON-NLS-1$
			"C/PRE/PREMI", //$NON-NLS-1$
			"C/PRO/CONC.ATTUALI", //$NON-NLS-1$
			"C/PRO/CONC.SVOLTI", //$NON-NLS-1$
			"E/AS/LVH.ARTIG.", //$NON-NLS-1$
			"E/ASS/ANAB", //$NON-NLS-1$
			"E/ASS/COOP.AIS", //$NON-NLS-1$
			"E/ASS/COOP.SNILPI", //$NON-NLS-1$
			"E/ASS/COSTR.EDILI", //$NON-NLS-1$
			"E/ASS/CULTR.ITALIA.NOSTRA.", //$NON-NLS-1$
			"E/ASS/HGV.ALBERG.", //$NON-NLS-1$
			"E/ASS/IND.FEDERARCH.", //$NON-NLS-1$
			"E/ASS/INU", //$NON-NLS-1$
			"E/ASS/SOC.ING.OICE", //$NON-NLS-1$
			"E/ASS/STUDI.ASSOC.", //$NON-NLS-1$
			"E/ASS/UIA", //$NON-NLS-1$
			"E/ASS/VARIE", //$NON-NLS-1$
			"E/ENT/AEC", //$NON-NLS-1$
			"E/ENT/CATASTO.LIB.FOND.", //$NON-NLS-1$
			"E/ENT/COMUN.BASSAATESINA", //$NON-NLS-1$
			"E/ENT/COMUN.BOLZANO", //$NON-NLS-1$
			"E/ENT/COMUN.VALISARCO", //$NON-NLS-1$
			"E/ENT/COMUN.VALPUSTERIA", //$NON-NLS-1$
			"E/ENT/COMUN.VALVEN.MERANO", //$NON-NLS-1$
			"E/ENT/CONSORZIO.REG.EDIL.", //$NON-NLS-1$
			"E/ENT/FIERA", //$NON-NLS-1$
			"E/ENT/GENIOCIVILE", //$NON-NLS-1$
			"E/ENT/IPEAA", //$NON-NLS-1$
			"E/ENT/MINISTERI", //$NON-NLS-1$
			"E/ENT/SCUOLE", //$NON-NLS-1$
			"E/ENT/TRIBUNALE", //$NON-NLS-1$
			"E/ENT/UFF.ECON.MONTANA", //$NON-NLS-1$
			"E/ENT/UFF.IMPOSTE", //$NON-NLS-1$
			"E/ENT/UFF.LAV.PUBBL.", //$NON-NLS-1$
			"E/ENT/UFF.RISP.ENER", //$NON-NLS-1$
			"E/ENT/UFF.TUT.PAESAGGIO", //$NON-NLS-1$
			"E/ENT/UFF.TUT.PATRIM.", //$NON-NLS-1$
			"E/ENT/UFF.URBANIST.", //$NON-NLS-1$
			"E/ENT/USL", //$NON-NLS-1$
			"E/PRO/AGR.FORES.ALBO.", //$NON-NLS-1$
			"E/PRO/AGR.FORES.CORRISP.", //$NON-NLS-1$
			"E/PRO/ARCH.ASSICUR.", //$NON-NLS-1$
			"E/PRO/ARCH.COMPETENZE", //$NON-NLS-1$
			"E/PRO/ARCH.INCOMP.", //$NON-NLS-1$
			"E/PRO/ARCH.PROGETT.", //$NON-NLS-1$
			"E/PRO/ARCH.PUBBLICI.", //$NON-NLS-1$
			"E/PRO/ARCH.SVOL.PROF.", //$NON-NLS-1$
			"E/PRO/ARTIGIANI", //$NON-NLS-1$
			"E/PRO/AVVOCATI.", //$NON-NLS-1$
			"E/PRO/COMIT.INT.PRO.CORR.", //$NON-NLS-1$
			"E/PRO/COMIT.INT.PRO.MEMBRI", //$NON-NLS-1$
			"E/PRO/COMMERCIALISTI", //$NON-NLS-1$
			"E/PRO/GEOM.ALBO", //$NON-NLS-1$
			"E/PRO/GEOM.CORRISP.", //$NON-NLS-1$
			"E/PRO/INDUSTRIALI", //$NON-NLS-1$
			"E/PRO/ING.ALBO", //$NON-NLS-1$
			"E/PRO/ING.CORRISP.", //$NON-NLS-1$
			"E/PRO/NOTAI", //$NON-NLS-1$
			"E/PRO/PERITI.ALBO", //$NON-NLS-1$
			"E/PRO/PERITI.CORRSIP.", //$NON-NLS-1$
			"E/PRO/RAG.ALBO", //$NON-NLS-1$
			"E/PRO/RAG.CORRISP.", //$NON-NLS-1$
			"E/PRO/SQ.ISO9000", //$NON-NLS-1$
			"F/FOR/BIBLIOTECA", //$NON-NLS-1$
			"F/FOR/CONTR.ELEN.CONTRA.", //$NON-NLS-1$
			"F/FOR/CONTR.FORM.PROF.", //$NON-NLS-1$
			"F/FOR/CONTR.NORMAT.", //$NON-NLS-1$
			"F/FOR/CORS.EST.CONVEG.", //$NON-NLS-1$
			"F/FOR/CORSI494", //$NON-NLS-1$
			"F/FOR/INTERNET", //$NON-NLS-1$
			"F/FOR/ISPETT.F.CONTRIBU", //$NON-NLS-1$
			"F/FOR/ISPETT.FORM.CORSI", //$NON-NLS-1$
			"F/FOR/ISPETT.FORM.PROF.", //$NON-NLS-1$
			"F/FOR/UNI", //$NON-NLS-1$
			"F/INI/CONTRIBUTI", //$NON-NLS-1$
			"F/INI/CORRISP.", //$NON-NLS-1$
			"F/INI/INVITI.MANIF.", //$NON-NLS-1$
			"F/INI/RICERC.URBAN.", //$NON-NLS-1$
			"F/INI/TESTI", //$NON-NLS-1$
			"F/INI/VIAGGI.CORRISP.", //$NON-NLS-1$
			"F/INI/VIAGGI.PROGRAM.", //$NON-NLS-1$
			"F/TUR/BANCA", //$NON-NLS-1$
			"F/TUR/COPIE.TB", //$NON-NLS-1$
			"F/TUR/CORRISP", //$NON-NLS-1$
			"F/TUR/TARIF.POSTALE", //$NON-NLS-1$
			"F/TUR/CONTABIL", //$NON-NLS-1$
			"I/CNA/ALTRI.ORD.CORR", //$NON-NLS-1$
			"I/CNA/CNA.CORR", //$NON-NLS-1$
			"I/CON/CNPAIA.CORRISP.", //$NON-NLS-1$
			"I/CON/CNPAIA.MODULI", //$NON-NLS-1$
			"I/CON/CNPAIA.RAPPRES.", //$NON-NLS-1$
			"I/CON/CNPAIA.STATUTO", //$NON-NLS-1$
			"I/CON/CONTROVERSIE", //$NON-NLS-1$
			"I/CON/INPS.CORRISP.", //$NON-NLS-1$
			"I/CON/INPS.MODULI", //$NON-NLS-1$
			"I/CON/INPS.NORM.", //$NON-NLS-1$
			"I/CON/IVA", //$NON-NLS-1$
			"I/CON/LEGGE.FINAN.", //$NON-NLS-1$
			"I/CON/TASSE.740", //$NON-NLS-1$
			"I/CON/TASSE.CASSA.DEP. E.P", //$NON-NLS-1$
			"I/CON/TASSE.MIN.TAX.", //$NON-NLS-1$
			"I/COS/ASSICUR", //$NON-NLS-1$
			"I/COS/BOLL.UFF.", //$NON-NLS-1$
			"I/COS/COMUN.STAMP.", //$NON-NLS-1$
			"I/COS/CONSULENZE", //$NON-NLS-1$
			"I/COS/CONVOCA", //$NON-NLS-1$
			"I/COS/DELIB", //$NON-NLS-1$
			"I/COS/FONDAZIONE", //$NON-NLS-1$
			"I/COS/VERB", //$NON-NLS-1$
			"I/MEM/ALBO.NORMATIVA", //$NON-NLS-1$
			"I/MEM/ARBITRI.ALBO", //$NON-NLS-1$
			"I/MEM/ARBITRI.NORM.", //$NON-NLS-1$
			"I/MEM/CART.PERS", //$NON-NLS-1$
			"I/MEM/COLL.TUT.AMB.ALBO", //$NON-NLS-1$
			"I/MEM/COLL.TUT.AMB.NORM", //$NON-NLS-1$
			"I/MEM/COLLAUD.ALBO", //$NON-NLS-1$
			"I/MEM/COLLAUD.NORM.", //$NON-NLS-1$
			"I/MEM/COMIT.TEC.ALBO", //$NON-NLS-1$
			"I/MEM/COMIT.TEC.NORM.", //$NON-NLS-1$
			"I/MEM/CONS.ANTIC.ALBO.", //$NON-NLS-1$
			"I/MEM/CONS.ANTIC.NORM.", //$NON-NLS-1$
			"I/MEM/DEONTO.DENUNC.", //$NON-NLS-1$
			"I/MEM/DEONTO.NORM.", //$NON-NLS-1$
			"I/MEM/ESP.COMM.EDI.ALBO", //$NON-NLS-1$
			"I/MEM/ESP.COMM.EDI.NORM.", //$NON-NLS-1$
			"I/MEM/GIURIE.ALBO", //$NON-NLS-1$
			"I/MEM/GIURIE.NORM.", //$NON-NLS-1$
			"I/MEM/LEGGE818.ALBO.", //$NON-NLS-1$
			"I/MEM/LEGGE818.NORM.", //$NON-NLS-1$
			"I/MEM/PREST.SERV.", //$NON-NLS-1$
			"I/MEM/PUBBL", //$NON-NLS-1$
			"I/MEM/RAPPRES.ALBO", //$NON-NLS-1$
			"I/MEM/RAPPRES.NORM.", //$NON-NLS-1$
			"I/MEM/RUOLI.ESATT.", //$NON-NLS-1$
			"I/MEM/STRA.ATTI.RESP.ATT", //$NON-NLS-1$
			"I/MEM/STRA.NOR.CEE.", //$NON-NLS-1$
			"I/MEM/STRA.NOR.NON.CEE.", //$NON-NLS-1$
			"I/ORD/ASS.ELEZ.CONSIG.", //$NON-NLS-1$
			"I/ORD/ASS.VERB.", //$NON-NLS-1$
			"I/ORD/ORDINAM.INFO.", //$NON-NLS-1$
			"I/ORD/ORDINAM.PROC.AMM.", //$NON-NLS-1$
			"L/AMB/LEROP", //$NON-NLS-1$
			"L/AMB/RISP.ENERG.", //$NON-NLS-1$
			"L/AMB/VIA", //$NON-NLS-1$
			"L/EDI/ABIT.AGEVOL.", //$NON-NLS-1$
			"L/EDI/ANTINCENDIO", //$NON-NLS-1$
			"L/EDI/BAR.ARCH.13", //$NON-NLS-1$
			"L/EDI/CONDONO", //$NON-NLS-1$
			"L/EDI/IMPIANTI46", //$NON-NLS-1$
			"L/EDI/SICUREZ.626.494", //$NON-NLS-1$
			"L/LEG/BASSANINI", //$NON-NLS-1$
			"L/LEG/EDIT.GARANTE", //$NON-NLS-1$
			"L/LEG/FINANZIARIA", //$NON-NLS-1$
			"L/LEG/INFO", //$NON-NLS-1$
			"L/LEG/ISTAT", //$NON-NLS-1$
			"L/LEG/PRIVACY", //$NON-NLS-1$
			"L/LEG/RASSEGNE", //$NON-NLS-1$
			"L/TEC/NORM.TECN.SISMA", //$NON-NLS-1$
			"L/TEC/NORM.TECN.UNI", //$NON-NLS-1$
			"L/URB/LAV.PUBB.PROV.", //$NON-NLS-1$
			"L/URB/LAV.PUBB.STATO", //$NON-NLS-1$
			"L/URB/URBANISTICA", //$NON-NLS-1$
			"T/CON/COMM.CONV.VID.", //$NON-NLS-1$
			"T/CON/VIDI.CONTABIL.", //$NON-NLS-1$
			"T/CON/VIDI.DOCUMENTI", //$NON-NLS-1$
			"T/CON/VIDI.VERBALI", //$NON-NLS-1$
			"T/LIQ/COMM.LIQ.", //$NON-NLS-1$
			"T/LIQ/CORRISP.", //$NON-NLS-1$
			"T/LIQ/PRATICHE", //$NON-NLS-1$
			"T/LIQ/VERBALI", //$NON-NLS-1$
			"T/TAR/AGRONOMI", //$NON-NLS-1$
			"T/TAR/DELIB.CONSIG.", //$NON-NLS-1$
			"T/TAR/DISCIPLINAR.TIPO", //$NON-NLS-1$
			"T/TAR/ESTERE.GOA", //$NON-NLS-1$
			"T/TAR/ESTERE.HOA", //$NON-NLS-1$
			"T/TAR/GEOM.", //$NON-NLS-1$
			"T/TAR/ING.ARCH.", //$NON-NLS-1$
			"T/TAR/PERITI", //$NON-NLS-1$
			"T/TAR/TOPOGRAFI", //$NON-NLS-1$
			"T/TAR/TRATTAT.INTERPRE.", //$NON-NLS-1$
			"T/VID/COMM.VID.", //$NON-NLS-1$
			"T/VID/DOCUMENTI", //$NON-NLS-1$
			"T/VID/FATT", //$NON-NLS-1$
			"T/VID/OFF", //$NON-NLS-1$
			"T/VID/OFF.PRVBZ.FREIE.UNI.CS", //$NON-NLS-1$
			"T/VID/OFF.PRVBZ.FREIE.UNI.DL", //$NON-NLS-1$
			"T/VID/PROTOCOLLI", //$NON-NLS-1$
			"T/VID/VERBALI" }; //$NON-NLS-1$
*/
	protected void createArchiveDropDown(Entry entry) {
		if (this.activeSection != null) {
			Label labelVoice = toolkit.createLabel(this.activeSection, Messages.ScrolledProtocolFormEditor_Voice); 
			labelVoice.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false));

			final CCombo comboVoice = new CCombo(this.activeSection, SWT.BORDER
			| SWT.READ_ONLY | SWT.DROP_DOWN );
					
			toolkit.adapt(comboVoice);
			
			comboVoice.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));

			try {
//				if(new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date())!=null && 
//						!new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date()).isEmpty()){
//					res = new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date()).toArray(new String[0]);
					List<String> voiceList = new RCPEJBPreferenceStore().getStringList(ArchivePreferencePage.ARCHIVES,entry.getRegistration_date());
					if(voiceList!=null)
						voices= voiceList.toArray(new String[0]);
					Arrays.sort(voices);
//				}
			} catch (ConnectionException e1) {
				e1.printStackTrace();
			}
			
			comboVoice.setItems(voices);
			
			/*for (int i = 0; i < voices.length; i++) {
				comboVoice.add(voices[i]);
//				System.out.println((((Entry)entry.getBean()).getArchive()));
				if(voices[i].equals(entry.getArchive())) {
//					System.out.println("Selecting "+voices[i]);
					comboVoice.select(i);
					comboVoice.setText(voices[i]);
//					System.out.println(comboVoice.getSelectionIndex());
//					System.out.println(comboVoice.getText());
//					System.out.println("++++++++++++++");
				}
			}*/
						
			comboVoice.addKeyListener(new ComboKeyListener(comboVoice, voices, voices));
			
			dataBindingContext
					.bindValue(
							new ComboBoxEnumSWTObservable(comboVoice, voices),
							PojoObservables.observeValue(entry, "archive"), null, null) //$NON-NLS-1$
					.getTarget().addChangeListener(dirtyListener);



			comboVoice.addSelectionListener(new SelectionListener(){

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					System.out.println(e.getClass());
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println(e.getClass());					
				}
				
			});
			comboVoice.addTraverseListener(new TraverseListener() {
				   
				// Ohne diesen Listener ist es nicht möglich das Control mit Shift-Tab zu verlassen
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS){
						comboVoice.getShell().setFocus();
				    }
			}});
				 
				 
			comboVoice.addFocusListener(new FocusListener(){
				 
				@SuppressWarnings("restriction")
				public void focusGained(FocusEvent e){
					if (e.display.msg.message != org.eclipse.swt.internal.win32.OS.WM_LBUTTONDOWN){
				         // if (e.display.msg.message == org.eclipse.swt.internal.win32.OS.WM_KEYDOWN) { // Alternative, ginge auch
				            // Focus gained via Tab
						comboVoice.getShell().setFocus();
						comboVoice.setFocus();
				    }
				}
				 
				public void focusLost(FocusEvent e){
					System.out.println("selection ="+ comboVoice.getSelectionIndex());
				}
				 
			});
		}
		
	}

	protected void createResponsibleDropDown(Entry entry) {
		if (this.activeSection != null) {
			Label labelRes = toolkit.createLabel(this.activeSection, Messages.ScrolledProtocolFormEditor_Label_Responsible); 
			labelRes.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					false, false));
			final CCombo comboRes = new CCombo(this.activeSection, SWT.BORDER
					| SWT.READ_ONLY | SWT.DROP_DOWN );
			toolkit.adapt(comboRes);
			comboRes.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));

			
			try {
//				if(new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date())!=null && 
//						!new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date()).isEmpty()){
//					res = new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,((IEntry)entry.getBean()).getRegistration_date()).toArray(new String[0]);
					List<String> resList = new RCPEJBPreferenceStore().getStringList(ResponsiblePreferencePage.RESPONSIBLES,entry.getRegistration_date());
					if(resList!=null)
						res= resList.toArray(new String[0]);
					Arrays.sort(res);
//				}
			} catch (ConnectionException e1) {
				e1.printStackTrace();
			}

			comboRes.setItems(res);
			
			comboRes.addKeyListener(new ComboKeyListener(comboRes, res, res));

			dataBindingContext
					.bindValue(
							new ComboBoxEnumSWTObservable(comboRes, res),
							PojoObservables.observeValue(entry, "responsible"), null, null) //$NON-NLS-1$
					.getTarget().addChangeListener(dirtyListener);
						
			comboRes.addTraverseListener(new TraverseListener() {
				   
				// Ohne diesen Listener ist es nicht möglich das Control mit Shift-Tab zu verlassen
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS){
						comboRes.getShell().setFocus();
				    }
			}});
				 
				 
			comboRes.addFocusListener(new FocusListener(){
				 
				@SuppressWarnings("restriction")
				public void focusGained(FocusEvent e){
					if (e.display.msg.message != org.eclipse.swt.internal.win32.OS.WM_LBUTTONDOWN){
				         // if (e.display.msg.message == org.eclipse.swt.internal.win32.OS.WM_KEYDOWN) { // Alternative, ginge auch
				            // Focus gained via Tab
						comboRes.getShell().setFocus();
						comboRes.setFocus();
				    }
				}
				 
				public void focusLost(FocusEvent e){
				}
				 
			});
		}
	}

	public Entry getEntry(){
		return entry;
	}

}
