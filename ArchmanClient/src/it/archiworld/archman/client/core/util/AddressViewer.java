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
package it.archiworld.archman.client.core.util;

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.CompanyEditor;
import it.archiworld.archman.client.crm.CompanyEditorInput;
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.archman.client.crm.MemberEditorInput;
import it.archiworld.archman.client.crm.PersonEditor;
import it.archiworld.archman.client.crm.PersonEditorInput;
import it.archiworld.archman.client.crm.ServiceMemberEditor;
import it.archiworld.archman.client.crm.ServiceMemberEditorInput;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.ServiceMember;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.EntryCompanyAddress;
import it.archiworld.common.protocol.EntryPersonAddress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.itextpdf.text.html.HtmlEncoder;

public class AddressViewer extends Composite {

	Composite parent;
	FormText viewer;

	EntryAddress address;

	DropTarget target;

	String initialText;

	List<SelectionListener> listeners = new ArrayList<SelectionListener>();

	DropTargetAdapter adapter = new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) {
				event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY
						: DND.DROP_NONE;
			}
			for (int i = 0, n = event.dataTypes.length; i < n; i++) {
				if (AddressTransfer.getInstance().isSupportedType(
						event.dataTypes[i])) {
					event.currentDataType = event.dataTypes[i];
				}
			}
		}

		@Override
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
		}

		@Override
		public void drop(DropTargetEvent event) {
			if (AddressTransfer.getInstance().isSupportedType(
					event.currentDataType)) {
				if (event.data != null && event.data instanceof Address[]) {
					Address[] addresses = (Address[]) event.data;
					try {
						Addressbook book = (Addressbook) ConnectionFactory
								.getConnection("AddressbookBean"); //$NON-NLS-1$
						EntryAddress address = null;
						if(addresses[0] instanceof Company)
							address = new EntryCompanyAddress((Company)book.getAddress(addresses[0]));
						if(addresses[0] instanceof Person)
							address = new EntryPersonAddress((Person)book.getAddress(addresses[0]));
						setAddress(address);
					} catch (Throwable t) {
						t.printStackTrace();
					}
				} else {
					System.out
						.println("Warning! Invalid input dropped in AddressViewer!"); //$NON-NLS-1$
					System.out.println(event.data.getClass());
					setAddress(null);
				}
			}
		}
	};

	KeyListener keyListener = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.keyCode == SWT.DEL) {
				setAddress(null);
			}
			if(e.keyCode == SWT.CR){
				openReference();
			}
		}
	};
	
	MouseListener mouselistener = new MouseListener() {

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			openReference();
		}

		@Override
		public void mouseDown(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseUp(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public AddressViewer(Composite parent, FormToolkit toolkit,
			String initialText, int style) {
		super(parent, style);
		this.initialText = initialText;
		toolkit.adapt(this);
		this.setLayout(new FillLayout());
		viewer = toolkit.createFormText(this, false);
		setAddress(null);
		this.target = new DropTarget(this.viewer, DND.DROP_COPY | DND.DROP_LINK
				| DND.DROP_DEFAULT);
		this.target
				.setTransfer(new Transfer[] { AddressTransfer.getInstance() });
		target.addDropListener(adapter);
		viewer.addKeyListener(keyListener);
		viewer.addMouseListener(mouselistener);
	}

	public void setAddress(EntryAddress address) {
		this.address = address;
		if (address != null) {
			String text = createAddressString(address);
			this.viewer.setText(text, true, false);
		} else {
			this.viewer.setText("<form><p><b>" + initialText //$NON-NLS-1$
					+ "</b></p></form>", true, false); //$NON-NLS-1$
		}
		fireSelectionEvent(address);
	}

	private void fireSelectionEvent(EntryAddress address) {
		Event event = new Event();
		event.data = address;
		event.widget = this;
		SelectionEvent e = new SelectionEvent(event);
		for (SelectionListener listener : listeners) {
			listener.widgetDefaultSelected(e);
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionListener(SelectionListener listener) {
		listeners.remove(listeners);
	}

	public EntryAddress getAddress() {
		return this.address;
	}
	
	protected String createAddressString(EntryAddress address){
		String text;
		if(address.getAddress() instanceof Member){
			Member member = (Member)address.getAddress();
			String street=null;
			String zip=null;
			String city=null;
			String state=null;
			String country=null;
			if (member.getStdaddress().equals("address")){//$NON-NLS-1$
				street=member.getStreet();
				zip=member.getZip();
				city=member.getCity();
				state=member.getState();
				country=member.getCountry();
			}
			if (member.getStdaddress().equals("address_de")){//$NON-NLS-1$
				street=member.getStreet_de();
				zip=member.getZip();
				city=member.getCity_de();
				state=member.getState_de();
				country=member.getCountry_de();
			}
			if (member.getStdaddress().equals("homeaddress")){//$NON-NLS-1$
				street=member.getHomestreet();
				zip=member.getHomezip();
				city=member.getHomecity();
				state=member.getHomestate();
				country=member.getHomecountry();
			}
			if (member.getStdaddress().equals("homeaddress_de")){//$NON-NLS-1$
				street=member.getHomestreet_de();
				zip=member.getHomezip();
				city=member.getHomecity_de();
				state=member.getHomestate_de();
				country=member.getHomecountry_de();
			}
			text="<form><p><b>" + HtmlEncoder.encode(member.toString()) + "</b><br/>"; //$NON-NLS-1$ //$NON-NLS-2$
			if(street!=null)
				text+=HtmlEncoder.encode(street)+"<br/>";//$NON-NLS-1$
			if(zip!=null && city!=null)
				text+=HtmlEncoder.encode(zip)+ " " +HtmlEncoder.encode(city)+"<br/>";//$NON-NLS-1$
			if(state!=null)
				text+=HtmlEncoder.encode(state)+"<br/>";//$NON-NLS-1$
			if(country!=null)
				text+=HtmlEncoder.encode(country)+"<br/>";//$NON-NLS-1$
			text += "</p></form>"; //$NON-NLS-1$
		}
		else {
			text="<form><p><b>" + HtmlEncoder.encode(address.toString()) + "</b><br/>"; //$NON-NLS-1$ //$NON-NLS-2$
			if (address.getStreet() != null)
				text += HtmlEncoder.encode(address.getStreet()) + "<br/>"; //$NON-NLS-1$
			if (address.getZip() != null && address.getCity() != null)
				text += HtmlEncoder.encode(address.getZip()) + " " + address.getCity() + "<br/>"; //$NON-NLS-1$ //$NON-NLS-2$
			if (address.getState() != null)
				text += HtmlEncoder.encode(address.getState()) + "<br/>"; //$NON-NLS-1$
			if (address.getCountry() != null)
				text += HtmlEncoder.encode(address.getCountry()) + "<br/>"; //$NON-NLS-1$
			text += "</p></form>"; //$NON-NLS-1$
		}
		return text;
	}
	
	protected void openReference(){
		if(address!=null){
			if(address.getAddress() instanceof Member){
				MemberEditorInput input = new MemberEditorInput((Member)address.getAddress());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, MemberEditor.ID);
				} catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}
			else if(address.getAddress() instanceof Person){
				PersonEditorInput input = new PersonEditorInput((Person)address.getAddress());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, PersonEditor.ID);
				} catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}
			else if(address.getAddress() instanceof Company){
				CompanyEditorInput input = new CompanyEditorInput((Company)address.getAddress());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, CompanyEditor.ID);
				} catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}
			else if(address.getAddress() instanceof ServiceMember){
				ServiceMemberEditorInput input = new ServiceMemberEditorInput((ServiceMember)address.getAddress());
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.openEditor(input, ServiceMemberEditor.ID);
				} catch (PartInitException ex) {
					ex.printStackTrace();
				}
			}
		}

	}
}
