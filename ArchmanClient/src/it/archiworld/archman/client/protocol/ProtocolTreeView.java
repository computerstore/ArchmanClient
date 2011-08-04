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
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.CompanyEditor;
import it.archiworld.archman.client.crm.CompanyEditorInput;
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.archman.client.crm.MemberEditorInput;
import it.archiworld.archman.client.crm.PersonEditor;
import it.archiworld.archman.client.crm.PersonEditorInput;
import it.archiworld.archman.client.protocol.util.EntryDragListener;
import it.archiworld.archman.client.protocol.util.EntryTransfer;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.protocol.Entry;
import it.archiworld.common.protocol.EntryAddress;
import it.archiworld.common.protocol.Inentry;
import it.archiworld.common.protocol.Outentry;
import it.archiworld.protocol.Protocol;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ProtocolTreeView extends ViewPart implements IPropertyListener {

	public static final String ID = "it.archiworld.archman.client.protocol.protocoltreeview"; //$NON-NLS-1$

	private IPropertyListener propertyListener = this;
	private TableViewer viewer;
	private Text searchInput = null;
	protected Protocol proto;
	protected Label searchLabel;

	protected List<Entry> data;

	public void propertyChanged(Object source, int propId) {
		if (propId == IWorkbenchPartConstants.PROP_DIRTY
				&& source instanceof ScrolledFormEditorBase) {
			ScrolledFormEditorBase form = (ScrolledFormEditorBase) source;
			if (form.isDirty() == false)
				updateList();
		}
	}

	protected void updateList() {
		if(data!=null)
			viewer.remove(data.toArray());
		try {
			data = proto.getEntryList(searchInput.getText());
			updateSearchLabelText();
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.ProtocolTreeView_RefreshFailureDialog_Heading,
					Messages.ProtocolTreeView_RefreshFailureDialog_Text.concat("\n").concat(t.getMessage()).concat("\n").concat(t.getCause().getMessage()));
			t.printStackTrace();
		}
		viewer.refresh();
		
	}

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {

		public ViewContentProvider() {
			try {
				proto = (Protocol) ConnectionFactory
						.getConnection("ProtocolBean"); //$NON-NLS-1$
				data = proto.getEntryList(searchInput.getText());
				updateSearchLabelText();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (data != null)
				return data.toArray();
			return new Object[0];
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Outentry)
				return ((Outentry) (parentElement)).getDestinations().toArray();
			if (parentElement instanceof Inentry) {
				List<EntryAddress> elements = new ArrayList<EntryAddress>();
				elements.add(((Inentry) (parentElement)).getSender());
				return elements.toArray();
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof Outentry)
				return element != null
						&& !((Outentry) (element)).getDestinations().isEmpty();
			if (element instanceof Inentry)
				return ((Inentry) element).getSender() != null;

			List<Address> elements = null;
			return elements != null && !elements.isEmpty();
		}

	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		Image out_entry_icon;
		
		Image in_entry_icon;
		
		Image deleted_entry_icon;
		
		public ViewLabelProvider()
		{
			super();
			this.out_entry_icon = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/mail_out.png").createImage();  //$NON-NLS-1$
			this.in_entry_icon = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/mail_in.png").createImage();  //$NON-NLS-1$
			this.deleted_entry_icon = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/email_delete.png").createImage();  //$NON-NLS-1$

		}
		
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			if (obj instanceof String) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			} else if (obj instanceof Entry && ((Entry)obj).getEmergency()) {
				return this.deleted_entry_icon;
			} else if (obj instanceof Outentry) {
				return this.out_entry_icon;
			} else if (obj instanceof Inentry) {
				return this.in_entry_icon;
			} else {
				return null;
			}
		}

		@Override
		public void dispose() {
			
			super.dispose();
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout gd = new GridLayout(1, true);
		gd.marginWidth = 1;
		gd.marginHeight = 1;
		gd.horizontalSpacing = 0;
		gd.verticalSpacing = 3;
		parent.setLayout(gd);
		searchLabel = new Label(parent, SWT.NONE);
		searchLabel.setText(""); //$NON-NLS-1$
		searchInput = new Text(parent, SWT.BORDER);
		searchInput.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
				false));
		
		/*
		 * searchInput.addModifyListener(new
		 * org.eclipse.swt.events.ModifyListener() { public void
		 * modifyText(org.eclipse.swt.events.ModifyEvent e) { //updateList(); }
		 * });
		 */searchInput.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR)
					updateList();

			}

		});
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.addDragSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { EntryTransfer.getInstance() },
				new EntryDragListener(viewer));
		createContextMenu();
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(
					org.eclipse.jface.viewers.DoubleClickEvent event) {

				System.out
						.println("Double click on:" + event.getSelection().toString()); //$NON-NLS-1$
				Object obj = ((StructuredSelection) event.getSelection())
						.getFirstElement();
				try {
					if (obj instanceof Outentry) {
						Outentry entry = null;
						try {
							entry = (Outentry) obj;
						} catch (Exception e) {
							e.printStackTrace();
							entry = null;
						}
						IEditorInput input = new OutProtocolEditorInput(entry);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, OutProtocolEditor.ID)
								.addPropertyListener(propertyListener);
					} else if (obj instanceof Inentry) {
						Inentry entry = null;
						try {
							entry = (Inentry) obj;
						} catch (Exception e) {
							e.printStackTrace();
							entry = null;
						}
						IEditorInput input = new InProtocolEditorInput(entry);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, InProtocolEditor.ID)
								.addPropertyListener(propertyListener);
					} else if (obj instanceof Member) {
						Member m = null;
						try {
							m = (Member) obj;
						} catch (Exception e) {
							e.printStackTrace();
						}
						IEditorInput input = new MemberEditorInput(m);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, MemberEditor.ID)
								.addPropertyListener(propertyListener);
					} else if (obj instanceof Person) {
						Person p = null;
						try {
							p = (Person) obj;
						} catch (Exception e) {
							e.printStackTrace();
						}
						IEditorInput input = new PersonEditorInput(p);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, PersonEditor.ID)
								.addPropertyListener(propertyListener);
					} else if (obj instanceof Company) {
						Company c = null;
						try {
							c = (Company) obj;
						} catch (Exception e) {
							e.printStackTrace();
						}
						IEditorInput input = new CompanyEditorInput(c);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, CompanyEditor.ID)
								.addPropertyListener(propertyListener);
					} else {
						System.out.println(obj.getClass());
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			};
		});
		getSite().setSelectionProvider(viewer);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
		searchInput.setFocus();
	}

	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager mgr) {
		// mgr.add(addItemAction);
		mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		// mgr.add(deleteItemAction);
		mgr.add(new Separator());
		// mgr.add();
	}

	public void updateSearchLabelText() {
		this.searchLabel.setText(Messages.ProtocolTreeView_Label_Search+" "+data.size()); //$NON-NLS-1$
		this.searchLabel.redraw();
		this.searchLabel.update();
		this.searchLabel.getParent().layout();
	}

	public List<Entry> getCurrentList() {
		return data;
	}


}

// @jve:decl-index=0:visual-constraint="-86,11"
