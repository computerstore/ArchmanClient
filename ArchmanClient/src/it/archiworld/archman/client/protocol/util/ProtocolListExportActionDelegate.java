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
package it.archiworld.archman.client.protocol.util;

import it.archiworld.archman.client.core.Activator;
import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.protocol.ProtocolTreeView;
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
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class ProtocolListExportActionDelegate extends Action implements IViewActionDelegate, IWorkbenchAction, IPartListener2 {

	protected static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$
	protected static final String EXPORTFOLDER = "preference.ExportFolder"; //$NON-NLS-1$

	protected IViewPart view;
	protected IWorkbenchWindow window;
	protected Shell shell;
	
	public ProtocolListExportActionDelegate(){
		
	}
	
	public ProtocolListExportActionDelegate(IWorkbenchWindow window){
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
		List<Entry> entries = (((ProtocolTreeView)view.getViewSite().getPart()).getCurrentList());
		
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
				filename = openWordFileDialog("protocol"); //$NON-NLS-1$
				if(filename!=null)
					Program.launch(filename);
			} catch (IOException e){
				MessageDialog.openError(shell, Messages.getString("ProtocolListExportActionDelegate_DialogError"), e.getLocalizedMessage()); //$NON-NLS-1$
				e.printStackTrace();
			} catch (ConnectionException e){
				MessageDialog.openError(shell, Messages.getString("ProtocolListExportActionDelegate_DialogError"), e.getLocalizedMessage()); //$NON-NLS-1$
				e.printStackTrace();
			} catch (Throwable t){
				MessageDialog.openError(shell, Messages.getString("ProtocolListExportActionDelegate_DialogError"), t.getLocalizedMessage()); //$NON-NLS-1$
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {	
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
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
	
	protected String openWordFileDialog(String subdir) {
		if (view!=null)
			shell=view.getViewSite().getShell();
		else
			shell=window.getShell();
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[] { "*.doc" }); //$NON-NLS-1$
		String dir = ""; //$NON-NLS-1$
		try {
			if(!Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER).equals("")) //$NON-NLS-1$
				dir = Activator.getDefault().getPreferenceStore().getString(TEMPLATEFOLDER)+"/"+subdir; //$NON-NLS-1$
			else
				dir = (new File(".")).getCanonicalPath()+"/"+subdir; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Throwable t) {
			MessageDialog.openError(shell, "Error", t.getLocalizedMessage()); //$NON-NLS-1$
			t.printStackTrace();
		}
		dialog.setFilterPath(dir);
		String filename = dialog.open();
		return filename;
	}

}
