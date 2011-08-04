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
package it.archiworld.archman.client.crm;

import it.archiworld.addressbook.Addressbook;
import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.util.AddressDragListener;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.archman.client.util.ListSorter;
import it.archiworld.common.Address;
import it.archiworld.common.Company;
import it.archiworld.common.Member;
import it.archiworld.common.Person;
import it.archiworld.common.ServiceMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public class AddressTreeView extends ViewPart implements IPropertyListener {

	public static final String ID = "it.archiworld.archman.client.crm.addresstreeview"; //$NON-NLS-1$

	private IPropertyListener propertyListener = this;
	private TreeViewer viewer;
	private Text searchInput = null;
	protected Addressbook book;

	private Image personImage = ImageDescriptor.createFromFile(this.getClass(),
			"/help/icons/user.png").createImage(); //$NON-NLS-1$
	private Image memberImage_male = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/vcard.png").createImage(); //$NON-NLS-1$
	private Image memberImage_female = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/vcard_female.png").createImage(); //$NON-NLS-1$
	private Image companyImage = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/building.png").createImage(); //$NON-NLS-1$
	private Image serviceMemberImage = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/tux.png").createImage(); //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	protected static Map<String, List> data;

	@Override
	public void dispose() {
		personImage.dispose();
		memberImage_male.dispose();
		memberImage_female.dispose();
		companyImage.dispose();
		serviceMemberImage.dispose();
	}

	@Override
	public final void propertyChanged(final Object source, final int propId) {
		if (propId == IWorkbenchPartConstants.PROP_DIRTY
				&& source instanceof ScrolledFormEditorBase) {
			ScrolledFormEditorBase form = (ScrolledFormEditorBase) source;
			if (form.isDirty() == false)
				updateList();
		}
	}

	private void updateList() {
		String pattern = searchInput.getText().toUpperCase()+"%"; //$NON-NLS-1$
		System.out.println(pattern);
		try {
			/*			data.put(Messages.AddressTreeView_Folder_Label_Person, book.searchPersonList(pattern));
			data.put(Messages.AddressTreeView_Folder_Label_Company, book.searchCompanyList(pattern));
			data.put(Messages.AddressTreeView_Folder_Label_Member, book.searchMemberList(pattern));
			data.put(Messages.AddressTreeView_FolderLabelServiceMember, book.searchServiceMemberList(pattern));
			data.put(Messages.AddressTreeView_deregistered_members, book.searchDeletedMemberList(pattern));
			*/
			data.put(Messages.AddressTreeView_Folder_Label_Person, ListSorter.sort(book.searchPersonList(pattern)));
			data.put(Messages.AddressTreeView_Folder_Label_Company, ListSorter.sort(book.searchCompanyList(pattern)));
			data.put(Messages.AddressTreeView_Folder_Label_Member, ListSorter.sort(book.searchMemberList(pattern)));
			data.put(Messages.AddressTreeView_FolderLabelServiceMember, ListSorter.sort(book.searchServiceMemberList(pattern)));
			data.put(Messages.AddressTreeView_deregistered_members, ListSorter.sort(book.searchDeletedMemberList(pattern)));
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.AddressTreeView_ErrorDialog_Heading,
					Messages.AddressTreeView_ErrorDialog_Text);
			t.printStackTrace();
		}
		viewer.refresh();
	}

	@SuppressWarnings("unchecked")
	public void updateList(Address address) {
		try {
			data = new HashMap<String, List>();
			data.put(Messages.AddressTreeView_FolderLabelSearchResult, ListSorter.sort(book.searchAddress(address)));
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.AddressTreeView_ErrorDialog_Heading,
					Messages.AddressTreeView_ErrorDialog_Text);
			t.printStackTrace();
		}
		viewer.refresh();
	}

	@SuppressWarnings("unchecked")
	public void updateList(Class entityClass, Criterion criterion,
			List<Order> orders) {
		System.out.println("updateList with criteria"); //$NON-NLS-1$
		try {
			data = new HashMap<String, List>();
			System.out.println(book);
			data.put(Messages.AddressTreeView_FolderLabelSearchResult, ListSorter.sort(book.searchAddress(entityClass, criterion, orders)));
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.AddressTreeView_ErrorDialog_Heading,
					Messages.AddressTreeView_ErrorDialog_Text);
			t.printStackTrace();
		}
		viewer.refresh();

	}

	@SuppressWarnings("unchecked")
	public List<Member> getCurrentMembers() {
		List<Member> members = data
				.get(Messages.AddressTreeView_Folder_Label_Member);
		if (members == null)
			members = new ArrayList<Member>();
		List<Member> deletedmembers = data
				.get(Messages.AddressTreeView_deregistered_members);
		if (deletedmembers != null)
			members.addAll(deletedmembers);
		if (data.get(Messages.AddressTreeView_FolderLabelSearchResult) != null)
			for (Address address : (List<Address>) data
					.get(Messages.AddressTreeView_FolderLabelSearchResult))
				if (address instanceof Member) {
					boolean add = true;
					for (Member member : members)
						if (member.getEntity_id()
								.equals(address.getEntity_id()))
							add = false;
					if (add)
						members.add((Member) address);
				}
		return members;
	}

	@SuppressWarnings("unchecked")
	public List<Member> getMembers() {
		return data.get(Messages.AddressTreeView_Folder_Label_Member);
	}

	@SuppressWarnings("unchecked")
	public List<Member> getDeletedMembers() {
		return data.get(Messages.AddressTreeView_deregistered_members);
	}

	@SuppressWarnings("unchecked")
	public List<Member> getSearchedMembers() {
		List<Member> members = new ArrayList<Member>();
		if (data.get(Messages.AddressTreeView_FolderLabelSearchResult) != null)
			for (Address address : (List<Address>) data
					.get(Messages.AddressTreeView_FolderLabelSearchResult))
				if (address instanceof Member) {
					members.add((Member) address);
				}
		return members;
	}

	@SuppressWarnings("unchecked")
	public List<Company> getCompanies(){
		return data.get(Messages.AddressTreeView_Folder_Label_Company);
	}
	
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements ITreeContentProvider {

		@SuppressWarnings("unchecked")
		public ViewContentProvider() {
			try {
				book = (Addressbook) ConnectionFactory
					.getConnection("AddressbookBean"); //$NON-NLS-1$
				data = new HashMap<String, List>();
				updateList();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		public void inputChanged(final Viewer v, final Object oldInput,
				final Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(final Object parent) {
			if (data != null) {
				return data.keySet().toArray();
			}
			return new Object[0];
		}

		@SuppressWarnings( { "unchecked" })//$NON-NLS-1$
		public Object[] getChildren(final Object parentElement) {
			List<Object> elements = data.get(parentElement);
			if (elements != null) {
				return elements.toArray();
			}
			return new Object[0];
		}

		public Object getParent(final Object element) {
			return null;
		}

		@SuppressWarnings( { "unchecked" })//$NON-NLS-1$
		public boolean hasChildren(final Object element) {
			List<Object> elements = data.get(element);
			return elements != null && !elements.isEmpty();
		}

	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(final Object obj, final int index) {
			return getText(obj);
		}

		@Override
		public String getText(final Object obj) {
			if (obj instanceof String) {
				return obj.toString() + " (" + data.get(obj).size() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}

			return obj.toString();
		}

		public Image getColumnImage(final Object obj, final int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(final Object obj) {
			if (obj instanceof String) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			} else if (obj instanceof Member) {
				if (((Member) obj).getGender() != null
						&& ((Member) obj).getGender().equals("female")) //$NON-NLS-1$
					return memberImage_female;
				else
					return memberImage_male;
			} else if (obj instanceof ServiceMember) {
				return serviceMemberImage;
			} else if (obj instanceof Person) {
				return personImage;
			} else if (obj instanceof Company) {
				return companyImage;
			} else {
				return null;
			}
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public final void createPartControl(final Composite parent) {
		GridLayout gd = new GridLayout(1, true);
		gd.marginWidth = 1;
		gd.marginHeight = 1;
		gd.horizontalSpacing = 0;
		gd.verticalSpacing = 3;
		parent.setLayout(gd);
		Label label = new Label(parent, SWT.NONE);
		label.setText(Messages.AddressTreeView_Label_Search);
		searchInput = new Text(parent, SWT.BORDER);
		searchInput.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
				false));
/*		searchInput
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(
							final org.eclipse.swt.events.ModifyEvent e) {
						updateList();
					}
				});
*/
		/*
		 * searchInput.addModifyListener(new
		 * org.eclipse.swt.events.ModifyListener() { public void
		 * modifyText(org.eclipse.swt.events.ModifyEvent e) { //updateList(); }
		 * });
		 */
		searchInput.addKeyListener(new KeyListener() {

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
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.getTree().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.addDragSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { AddressTransfer.getInstance() },
				new AddressDragListener(viewer));

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(
					final org.eclipse.jface.viewers.DoubleClickEvent event) {

				Object obj = ((StructuredSelection) event.getSelection())
						.getFirstElement();
				try {
					if (obj instanceof Member) {
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
					} else if (obj instanceof ServiceMember) {
						ServiceMember m = null;
						try {
							m = (ServiceMember) obj;
						} catch (Exception e) {
							e.printStackTrace();
						}
						IEditorInput input = new ServiceMemberEditorInput(m);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, ServiceMemberEditor.ID)
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
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			};
		});
		getSite().setSelectionProvider(viewer);
		createContextMenu();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public final void setFocus() {
		viewer.getControl().setFocus();
		searchInput.setFocus();
	}
	
	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});

		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager mgr) {
		mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(new Separator());
	}

	public void deleteSearchFolder() {
		data.remove(Messages.AddressTreeView_FolderLabelSearchResult);
		viewer.refresh();
	}
}
// @jve:decl-index=0:visual-constraint="-86,11"
