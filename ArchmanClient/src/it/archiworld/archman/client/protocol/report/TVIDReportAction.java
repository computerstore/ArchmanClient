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
package it.archiworld.archman.client.protocol.report;

import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.common.protocol.Entry;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.Inentry;
import it.archiworld.common.protocol.Outentry;
import it.archiworld.protocol.Protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

public class TVIDReportAction extends Action implements ActionFactory.IWorkbenchAction {

		protected static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
		protected static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$

		public final static String ID = "it.archiworld.archman.client.report.tvidreportaction"; //$NON-NLS-1$
		public final IWorkbenchWindow window;

		public TVIDReportAction(IWorkbenchWindow window) {
			this.window=window;
		    setId(ID);
		    setText(it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.WindowTitle_ExportTVID")); //$NON-NLS-1$
		    setToolTipText(it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.Window_ToolTipText_ExportTVIDEntriesFromProtocol")); //$NON-NLS-1$
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
					List<Entry> protocols;
					protocols = ((Protocol)ConnectionFactory.getConnection("ProtocolBean")).getEntryList(year+" T/VID/OFF T/VID/FATT"); //$NON-NLS-1$ //$NON-NLS-2$
					if(!protocols.isEmpty())
						export(protocols);
					else
						MessageDialog.openError(window.getShell(),
								"No Protocols Entries", //$NON-NLS-1$
								"No Protocols Entries with T/VID/OFF or T/VID/FATT in the date range"); //$NON-NLS-1$
					return;
				} catch (ConnectionException e) {
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        }
		}
		
		public void export(List<Entry> entries) {
			
			if(entries!=null){
				StringBuffer output=new StringBuffer(exportHeader()+"\r\n"); //$NON-NLS-1$
				System.out.println(exportHeader()+"\r\n"); //$NON-NLS-1$
				try{
					Protocol proto = (Protocol) ConnectionFactory
					.getConnection("ProtocolBean"); //$NON-NLS-1$
					for(Entry entry:entries){
						entry=proto.getEntry(entry);
						output.append(exportBody(entry)+"\r\n"); //$NON-NLS-1$
						System.out.println(exportBody(entry)+"\r\n"); //$NON-NLS-1$
					}
					String dir=""; //$NON-NLS-1$
					if(Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)!="") //$NON-NLS-1$
						dir = Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)+"/protocollist.txt"; //$NON-NLS-1$
					else
						dir = (new File(".")).getCanonicalPath()+"/protocollist.txt"; //$NON-NLS-1$ //$NON-NLS-2$
					FileWriter fwriter=new FileWriter(dir);
					BufferedWriter bwriter=new BufferedWriter(fwriter);
					bwriter.append(output);
					bwriter.close();
					fwriter.close();
					String filename;
					filename=Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER)+"/protocol/TVID.doc"; //$NON-NLS-1$
//					filename = openWordFileDialog("protocol"); //$NON-NLS-1$
					if(filename!=null)
						Program.launch(filename);
				} catch (IOException e){
					MessageDialog.openError(window.getShell(), "Error", e.getLocalizedMessage()); //$NON-NLS-1$
					e.printStackTrace();
				} catch (ConnectionException e){
					MessageDialog.openError(window.getShell(), "Error", e.getLocalizedMessage()); //$NON-NLS-1$
					e.printStackTrace();
				} catch (Throwable t){
					MessageDialog.openError(window.getShell(), "Error", t.getLocalizedMessage()); //$NON-NLS-1$
					t.printStackTrace();
				}
			}
		}
		
		protected String exportHeader(){
			String output=""; //$NON-NLS-1$
			output+=headFormatter("protocol"); //$NON-NLS-1$
			output+=headFormatter("year"); //$NON-NLS-1$
			output+=headFormatter("subject"); //$NON-NLS-1$
			output+=headFormatter("protocol_date"); //$NON-NLS-1$
			output+=headFormatter("registration_date"); //$NON-NLS-1$
			output+=headFormatter("archive"); //$NON-NLS-1$
			output+=headFormatter("secretary"); //$NON-NLS-1$
			output+=headFormatter("responsible"); //$NON-NLS-1$
			output+=headFormatter("recived_date"); //$NON-NLS-1$
			output+=headFormatter("sender"); //$NON-NLS-1$
			output+="destinations"; //$NON-NLS-1$
			return output;
		}

		protected String exportBody(Entry entry){
			String output=""; //$NON-NLS-1$
			output+=formatter(entry.getProtocol());
			output+=formatter(entry.getYear());
			output+=formatter(entry.getSubject());
			output+=formatter(entry.getProtocol_date());
			output+=formatter(entry.getRegistration_date());
			output+=formatter(entry.getArchive());
			output+=formatter(entry.getOwner());
			if(entry instanceof Outentry)
				output+=formatter(((Outentry)entry).getResponsible());
			else
				output+="\t"; //$NON-NLS-1$
			if(entry instanceof Inentry){
				output+=formatter(((Inentry)entry).getRecived_date());
				if(((Inentry)entry).getSender()!=null)
					output+=((Inentry)entry).getSender();
				else
					output+="\t"; //$NON-NLS-1$
			}
			else 
				output+="\t\t"; //$NON-NLS-1$
			if(entry instanceof Outentry){
				List<EntryAddress> addresses=((Outentry)entry).getDestinations();
				for(int i=0;i<addresses.size();i++){
					output+=addresses.get(i).toString();
					if(i<(addresses.size()-1)) output+=";"; //$NON-NLS-1$
				}
			}
			else
				output+="\t"; //$NON-NLS-1$
			
			return output;
		}
		
		protected String headFormatter(String string){
			return string+"\t"; //$NON-NLS-1$
		}
		
		protected String formatter(Object input){
			if(input!=null){
				if(input instanceof Timestamp)
					return DateFormat.getInstance().format((Timestamp)input)+"\t"; //$NON-NLS-1$
				return input.toString()+"\t"; //$NON-NLS-1$
			}
			else return "\t"; //$NON-NLS-1$
		}
		/*
		protected String openWordFileDialog(String subdir) {
			Shell shell=window.getShell();
			FileDialog dialog = new FileDialog(shell);
			dialog.setFilterExtensions(new String[] { "*.doc" });
			String dir = "";
			try {
				System.out.println("Pref store: "+Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER));
				if(!Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER).equals(""))
					dir = Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER)+"/"+subdir;
				else
					dir = (new File(".")).getCanonicalPath()+"/"+subdir;
			} catch (Throwable t) {
				MessageDialog.openError(shell, "Error", t.getLocalizedMessage());
				t.printStackTrace();
			}
			dialog.setFilterPath(dir);
			String filename = dialog.open();
			return filename;
		}
		 */
		private String openNewDialog() {
			 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			 shell = Display.getCurrent().getActiveShell();
			 String dialogTitle = it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.DialogInfo_PleaseEnterTheDate"); //$NON-NLS-1$
			 String dialogMessage = it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.InfoMessage_EnterTheDateOfTheProtocolsWanted"); //$NON-NLS-1$
			 IInputValidator validator = new IInputValidator() {
			 	public String isValid(String newText) {
			 		if(newText=="") //$NON-NLS-1$
			 			return it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.InfoError_DateCanNotBeEmpty"); //$NON-NLS-1$
			 		try{
			 			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy"); //$NON-NLS-1$
			 			dateFormat.parse(newText);
			 		} catch (ParseException e) {

						e.printStackTrace();
			 			return it.archiworld.archman.client.protocol.report.Messages.getString("TVIDReportAction.InfoError_ErrorConvertingDate"); //$NON-NLS-1$
					}
			 		return null;
				}  
			};
			InputDialog dialog = new InputDialog(shell, dialogTitle, dialogMessage, null, validator);
			int result = dialog.open();
			return result == Window.OK ? dialog.getValue() : null;
		}
		
}

