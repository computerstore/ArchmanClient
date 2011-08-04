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
package it.archiworld.archman.client.committee;

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.archman.client.crm.MemberEditor;
import it.archiworld.archman.client.crm.MemberEditorInput;
import it.archiworld.archman.client.util.AddressTransfer;
import it.archiworld.archman.client.util.CommitteeDragListener;
import it.archiworld.committee.Committees;
import it.archiworld.common.committee.Committee;
import it.archiworld.common.committee.Committeemember;

import java.util.List;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class CommitteeTreeView extends ViewPart implements IPropertyListener {

	public static final String ID = "it.archiworld.archman.client.committee.committeetreeview"; //$NON-NLS-1$

	private IPropertyListener propertyListener = this;
	private TreeViewer viewer;
	private Text searchInput = null;
	private Composite treeComposite = null;
	private Composite searchPane = null;
	protected Committees committees;

	private Image committeeImage = ImageDescriptor.createFromFile(
			this.getClass(), "/help/icons/vcard.png").createImage(); //$NON-NLS-1$

	protected static List<Committee> data;

	public final void propertyChanged(final Object source, final int propId) {
		if (propId == IWorkbenchPartConstants.PROP_DIRTY
				&& source instanceof ScrolledFormEditorBase) {
			ScrolledFormEditorBase form = (ScrolledFormEditorBase) source;
			if (form.isDirty() == false)
				updateList();
		}
	}

	private void updateList() {
		try {
			//Committees committees = (Committees) ConnectionFactory
			//		.getConnection("CommitteesBean"); //$NON-NLS-1$
			data = committees.searchCommitteeList(searchInput.getText());
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.CommitteeTreeView_RefreshFailureDialog_Heading,
					Messages.CommitteeTreeView_RefreshFailureDialog_Text);
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
	class ViewContentProvider implements ITreeContentProvider {

		public ViewContentProvider() {
			try {
				committees = (Committees) ConnectionFactory
						.getConnection("CommitteesBean"); //$NON-NLS-1$
				data = committees.searchCommitteeList(new String());
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
				return data.toArray();
			}
			return new Object[0];
		}

		public Object[] getChildren(final Object parentElement) {
			List<Committeemember> elements = null;
			if(parentElement instanceof Committee)
				elements = ((Committee) parentElement).getMember();
			if (elements != null) {
				for(int i=0;i<elements.size();i++)
					System.out.println(elements.get(i).toString());
				return elements.toArray();
			}
			return new Object[0];
		}

		public Object getParent(final Object element) {
			return null;
		}

		public boolean hasChildren(final Object element) {
			List<Committeemember> elements=null;
			if(element instanceof Committee)
				elements = ((Committee) element).getMember();
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
			} else if (obj instanceof Committee) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FOLDER);
			} else if (obj instanceof Committeemember) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_FILE);
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
		GridData gridData11 = new GridData();
		gridData11.widthHint = 20;
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		gridData11.heightHint = 20;
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.verticalSpacing = 5;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
		gridLayout.horizontalSpacing = 5;
		searchPane = new Composite(parent, SWT.NONE);
		searchPane.setLayout(gridLayout);

		searchInput = new Text(searchPane, SWT.BORDER);
		searchInput.setLayoutData(gridData);
		searchInput
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(
							final org.eclipse.swt.events.ModifyEvent e) {
						updateList();
					}
				});
		treeComposite = new Composite(searchPane, SWT.NONE);
		treeComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData gridData1 = new org.eclipse.swt.layout.GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.horizontalSpan = 2;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		treeComposite.setLayoutData(gridData1);
		viewer = new TreeViewer(treeComposite, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.addDragSupport(DND.DROP_LINK | DND.DROP_TARGET_MOVE
				| DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_MOVE
				| DND.DROP_NONE,
				new Transfer[] { AddressTransfer.getInstance() },
				new CommitteeDragListener(viewer));
		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(
					final org.eclipse.jface.viewers.DoubleClickEvent event) {

				System.out
						.println("Double click on:" + event.getSelection().toString()); //$NON-NLS-1$
				Object obj = ((StructuredSelection) event.getSelection())
						.getFirstElement();
				try {
					if (obj instanceof Committeemember) {
						Committeemember m = null;
						try {
							m = (Committeemember) obj;
						} catch (Exception e) {
							e.printStackTrace();
							m = null;
						}
						IEditorInput input = new MemberEditorInput(m
								.getMember());
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, MemberEditor.ID)
								.addPropertyListener(propertyListener);
					} else if (obj instanceof Committee) {
						Committee committee = null;
						try {
							committee = (Committee) obj;
						} catch (Exception e) {
							e.printStackTrace();
							committee = null;
						}
						IEditorInput input = new CommitteeEditorInput(committee);
						getViewSite().getWorkbenchWindow().getActivePage()
								.openEditor(input, CommitteeEditor.ID)
								.addPropertyListener(propertyListener);
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
	public final void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		committeeImage.dispose();
	}

}
