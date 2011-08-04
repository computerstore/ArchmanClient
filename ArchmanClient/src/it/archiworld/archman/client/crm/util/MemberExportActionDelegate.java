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
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.common.Member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

public class MemberExportActionDelegate extends ExportActionDelegate {

	private ImageDescriptor icon;

	public final static String ID = "it.archiworld.archman.client.crm.memberexportactiondelegate"; //$NON-NLS-1$

	private IWorkbenchPart activePart;
	private MemberEditor memberEditor;
	
	public MemberExportActionDelegate(IWorkbenchWindow window) {
		super(window);
		this.window = window;
		icon = ImageDescriptor.createFromFile(this.getClass(),
				"/help/icons/page_white_word.png"); //$NON-NLS-1$
		setId(ID);
		setText(Messages.getString("MemberExportActionDelegate.ActionLabelSendDocumentToMember")); //$NON-NLS-1$
		setToolTipText(Messages.getString("MemberExportActionDelegate.ActionToolTipSendDocumentToMember")); //$NON-NLS-1$
		setImageDescriptor(icon);
		window.getPartService().addPartListener(this);
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		System.out.println("Info: Part " + partRef.getId() + " activated!"); //$NON-NLS-1$ //$NON-NLS-2$
		if (partRef.getId().equals(MemberEditor.ID)) {
			activePart = partRef.getPart(false);
			memberEditor = (MemberEditor) activePart;
			
		} else {
			memberEditor = null;
			activePart = partRef.getPart(false);
		}
	}

	@Override
	public void run(IAction action) {
		run();
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void run() {
		System.out.println("running Member Export Action Delegate"); //$NON-NLS-1$
		Member member = memberEditor.getMember();
		if(member!=null){
			StringBuffer output=new StringBuffer(exportHeader());
			System.out.println(exportHeader());
			try{
				Addressbook book = (Addressbook) ConnectionFactory
				.getConnection("AddressbookBean"); //$NON-NLS-1$
				member=(Member)book.getAddress(member);
				System.out.println(exportBody(member));
				output.append(exportBody(member));
				String dir=""; //$NON-NLS-1$
				if(Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)!="") //$NON-NLS-1$
					dir = Activator.getDefault().getPreferenceStore().getString(EXPORTFOLDER)+"/member.txt"; //$NON-NLS-1$
				else
					dir = (new File(".")).getCanonicalPath()+"/member.txt"; //$NON-NLS-1$ //$NON-NLS-2$
				FileWriter fwriter=new FileWriter(dir);
				BufferedWriter bwriter=new BufferedWriter(fwriter);
				bwriter.append(output);
				bwriter.close();
				fwriter.close();
				String filename;
				filename = openWordFileDialog("member"); //$NON-NLS-1$
				if(filename!=null)
					Program.launch(filename);
			} catch (IOException e){
				MessageDialog.openError(shell, Messages.getString("MemberExportActionDelegate_ErrorDialogHeading"), e.getLocalizedMessage()); //$NON-NLS-1$
				e.printStackTrace();
			} catch (ConnectionException e){
				MessageDialog.openError(shell, Messages.getString("MemberExportActionDelegate_ErrorDialogHeading"), e.getLocalizedMessage()); //$NON-NLS-1$
				e.printStackTrace();
			} catch (Throwable t){
				MessageDialog.openError(shell, Messages.getString("MemberExportActionDelegate_ErrorDialogHeading"), t.getLocalizedMessage()); //$NON-NLS-1$
				t.printStackTrace();
			}
		}
	}
}


