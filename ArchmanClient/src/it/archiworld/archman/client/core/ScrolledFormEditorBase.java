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
package it.archiworld.archman.client.core;

import it.archiworld.aaa.User;
import it.archiworld.archman.client.core.util.CDateTimeSWTObservable;
import it.archiworld.archman.client.core.util.ComboBoxEnumSWTObservable;
import it.archiworld.archman.client.core.util.ComboKeyListener;
import it.archiworld.archman.client.core.util.DateConverter;
import it.archiworld.archman.client.util.PasswordDialog;
import it.archiworld.archman.client.util.PasswordHandler;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

public abstract class ScrolledFormEditorBase extends EditorPart {

	protected User user;
	private static final String TEMPLATEFOLDER = "preference.TemplateFolder"; //$NON-NLS-1$

	protected boolean isDirty = false;

	protected FormToolkit toolkit;

	protected ScrolledForm form;

	protected Composite activePart;

	protected Composite activeSection;

	protected CTabFolder tabFolder = null;

	protected DataBindingContext dataBindingContext;

	protected FieldModifyListener modifyListener = new FieldModifyListener();

	protected FieldKeyListener keyListener = new FieldKeyListener();

	protected IChangeListener dirtyListener = new DirtyListener();

	@Override
	public void doSaveAs() {
		;
	}

	@Override
	public boolean isDirty() {
		return this.isDirty;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		;
	}

	public void setDirty(boolean dirty) {
		if (this.isDirty != dirty) {
			this.isDirty = dirty;
			firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
		}
	}

	public boolean authenticate() {
		PasswordHandler passhandler = new PasswordHandler();
		PasswordDialog pdialog = new PasswordDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());
		pdialog.setPasshandler(passhandler);
		pdialog.open();
		user = passhandler.isAuthorized();
		if (user != null) {
			return true;
		} else {
			MessageDialog
					.openError(
							this.getSite().getShell(),
							Messages.ScrolledFormEditorBase_AuthenticationErrorDialogTitle,
							Messages.ScrolledFormEditorBase_AuthenticationErrorDialogText);
			return false;
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setRedraw(true);
		form.getBody().setLayout(new GridLayout(1, true));
		Realm.runWithDefault(SWTObservables.getRealm(parent.getDisplay()),
				new Runnable() {

					@Override
					public void run() {
						dataBindingContext = new DataBindingContext();
						Composite header = toolkit.createComposite(form
								.getBody(), SWT.NONE);
						GridData headerGd = new GridData(SWT.FILL,
								SWT.BEGINNING, true, false);
						header.setLayoutData(headerGd);
						activePart = header;
						createFormHeader(form, header);
						Composite body = toolkit.createComposite(
								form.getBody(), SWT.NONE);
						GridData bodyGd = new GridData(SWT.FILL, SWT.FILL,
								true, true);
						body.setLayoutData(bodyGd);
						activePart = body;
						createFormBody(body);
						Composite footer = toolkit.createComposite(form
								.getBody(), SWT.NONE);
						GridData footerGd = new GridData(SWT.FILL,
								SWT.BEGINNING, true, false);
						footerGd.heightHint = 0;
						footer.setLayoutData(footerGd);
						activePart = footer;
						createFormFooter(footer);
					}

				});
		form.reflow(true);
		setPartName(getEditorInput().getName());
		populateForm();
	}

	protected Composite createTabItem(String label, Layout layout) {
		boolean first = false;
		if (tabFolder == null) {
			tabFolder = new CTabFolder(this.activePart, SWT.BORDER);
			tabFolder
					.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			toolkit.adapt(tabFolder, true, true);
			tabFolder.setSimple(false);
			first = true;
		}
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setText(label);
		Composite itemControl = toolkit.createComposite(tabFolder, SWT.NONE);
		itemControl.setLayout(layout);
		item.setControl(itemControl);
		this.activePart = itemControl;
		this.activeSection = itemControl;
		if (first)
			tabFolder.setSelection(item);
		return itemControl;
	}

	protected Section createSection(String heading, int style) {
		return createSection(heading, style, 1, 1, true, false);
	}

	protected Section createSection(String heading,
			int style, int hspan, int vspan, boolean hgrab, boolean vgrab) {
		Section section = toolkit.createSection(this.activePart, style);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, hgrab, vgrab);
		gd.horizontalSpan = hspan;
		gd.verticalSpan = vspan;
		section.setLayoutData(gd);
		section.setText(heading);
//		section.setDescription(description);
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout(2, false));
		this.activeSection = sectionClient;
		section.setClient(sectionClient);
		return section;
	}

	protected void createLabeledText(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			Text textControl = toolkit.createText(this.activeSection, null,
					SWT.BORDER);
			textControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext
					.bindValue(
							SWTObservables.observeText(textControl, SWT.Modify),
							PojoObservables.observeValue(entity, property),
							null, null).getTarget().addChangeListener(
							dirtyListener);
		}
	}

	protected void createLabeledTextWithDirectory(String label, Object entity,
			String property, String directory_property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
					false, false));
			Composite composite = this.getToolkit().createComposite(
					this.activeSection);
			GridData compositeGD = new GridData(SWT.FILL, SWT.BEGINNING, true,
					false);
			compositeGD.heightHint = 26;
			composite.setLayoutData(compositeGD);
			composite.setLayout(new GridLayout(2, false));
			Button checkboxControl = this.getToolkit().createButton(composite,
					null, SWT.CHECK);
			checkboxControl.setLayoutData(new GridData(SWT.END, SWT.BEGINNING,
					false, false));
			dataBindingContext.bindValue(
					SWTObservables.observeSelection(checkboxControl),
					PojoObservables.observeValue(entity, directory_property),
					null, null).getTarget().addChangeListener(dirtyListener);

			Text textControl = toolkit.createText(composite, null, SWT.BORDER);
			textControl.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING,
					true, false));
			dataBindingContext
					.bindValue(
							SWTObservables.observeText(textControl, SWT.Modify),
							PojoObservables.observeValue(entity, property),
							null, null).getTarget().addChangeListener(
							dirtyListener);
		}
	}

	protected void createLabeledMultiText(String label, Object entity,
			String property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING,
					SWT.BEGINNING, false, false));
			final Text textControl = toolkit.createText(this.activeSection,
					null, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
			gd.widthHint = 120;
			textControl.setLayoutData(gd);
			textControl.pack();
			Binding bnd = dataBindingContext.bindValue(SWTObservables
					.observeText(textControl, SWT.Modify), PojoObservables
					.observeValue(entity, property), null, null);
			bnd.getTarget().addChangeListener(dirtyListener);
		}
	}

	protected void createDateField(String label, int style, Object entity,
			String property) {
		if (this.activeSection != null) {
			Label labelControl = toolkit.createLabel(this.activeSection, label);
			labelControl.setLayoutData(new GridData(SWT.BEGINNING,
					SWT.BEGINNING, false, false));
			CDateTime dateTime = new CDateTime(this.activeSection, CDT.BORDER
					| style);
			toolkit.adapt(dateTime);
			dateTime.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
			dataBindingContext
					.bindValue(
							new CDateTimeSWTObservable(dateTime),
							PojoObservables.observeValue(entity, property),
							new UpdateValueStrategy()
									.setConverter(new IConverter() {

										public Object convert(Object fromObject) {
											if (fromObject == null
													|| !(fromObject instanceof Date)) {
												return null;
											}
											return new Timestamp(
													((Date) fromObject)
															.getTime());
										}

										public Object getFromType() {
											return Date.class;
										}

										public Object getToType() {
											return Timestamp.class;
										}

									}), new UpdateValueStrategy()).getTarget()
					.addChangeListener(dirtyListener);
		}
	}

	protected void createLabeledComboBox(String label, String[] labels,
			String[] keys, Object entity, String property) {
		if (this.activeSection != null) {
			createLabeledComboBox(label, labels, keys, entity, property,
					this.activeSection);
		}
	}

	protected void createLabeledComboBox(String label, String[] labels,
			String[] keys, Object entity, String property, Composite target) {
		toolkit.createLabel(target, label);
		final CCombo sectorCombo = new CCombo(target, SWT.BORDER | SWT.READ_ONLY);
//		for (int i = 0; i < labels.length; i++) {
//			sectorCombo.add(labels[i], i);
//		}
		toolkit.adapt(sectorCombo, true, true);
		sectorCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sectorCombo.addKeyListener(new ComboKeyListener(sectorCombo, labels, keys));
		dataBindingContext.bindValue(
				new ComboBoxEnumSWTObservable(sectorCombo, keys),
				PojoObservables.observeValue(entity, property))
				.getTarget().addChangeListener(dirtyListener);
	}

	protected void createLabeledReadOnlyTimestamp(String label,
			int dateConverterType, int dateFormat, int timeFormat,
			Object entity, String property) {
		if (activeSection != null) {
			toolkit.createLabel(activeSection, label);
			Label readOnly = toolkit.createLabel(activeSection, null, SWT.NONE);
			readOnly.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
			dataBindingContext.bindValue(SWTObservables.observeText(readOnly),
					PojoObservables.observeValue(entity, property), null,
					new UpdateValueStrategy().setConverter(new DateConverter(
							dateConverterType, dateFormat, timeFormat)));
		}
	}

	protected void createLabeledReadOnly(String label, String defaultValue,
			Object entity, String property) {
		if (activeSection != null) {
			toolkit.createLabel(activeSection, label);
			Label readOnly = toolkit.createLabel(activeSection, defaultValue,
					SWT.NONE);
			readOnly.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
			dataBindingContext
					.bindValue(SWTObservables.observeText(readOnly),
							PojoObservables.observeValue(entity, property),
							null, null);
		}
	}

	protected void createLabeledCheckbox(String label, Object entity,
			String property) {
		if (activeSection != null) {
			toolkit.createLabel(activeSection, label);
			Button checkbox = toolkit.createButton(activeSection, null,
					SWT.CHECK);
			checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
					true, false));
			dataBindingContext
					.bindValue(SWTObservables.observeSelection(checkbox),
							PojoObservables.observeValue(entity, property),
							null, null).getTarget().addChangeListener(
							dirtyListener);
		}
	}

	protected void populateForm() {
		;
	}

	abstract protected void createFormHeader(ScrolledForm form, Composite header);

	abstract protected void createFormBody(Composite body);

	abstract protected void createFormFooter(Composite footer);

	private class FieldKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {
			;
		}

		public void keyReleased(KeyEvent e) {
			setDirty(true);
		}

	}

	private class FieldModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			setDirty(true);
		}
	}

	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	private class DirtyListener implements IChangeListener {
		
		public void handleChange(ChangeEvent event) {
			setDirty(true);
		}

	}

	public final FormToolkit getToolkit() {
		return toolkit;
	}

	public final DataBindingContext getDataBindingContext() {
		return dataBindingContext;
	}

	public final IChangeListener getDirtyListener() {
		return dirtyListener;
	}

	public final void setDirtyListener(IChangeListener dirtyListener) {
		this.dirtyListener=dirtyListener;
	}
	
	public final Composite getActivePart() {
		return activePart;
	}

	public final Composite getActiveSection() {
		return activeSection;
	}

	public String openWordFileDialog(String subdir) {
		FileDialog dialog = new FileDialog(activeSection.getShell());
		dialog.setFilterExtensions(new String[] {
				"*.doc; *.xls", "*.dot", "*.xlt", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String dir = ""; //$NON-NLS-1$
		try {
			if (!Activator.getDefault().getPreferenceStore().getString(
					TEMPLATEFOLDER).equals("")) //$NON-NLS-1$
				dir = Activator.getDefault().getPreferenceStore().getString(
						TEMPLATEFOLDER)
						+ "/" + subdir; //$NON-NLS-1$
			else
				dir = (new File(".")).getCanonicalPath() + "/" + subdir; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (Throwable t) {
			t.printStackTrace();
		}
		dialog.setFilterPath(dir);
		String filename = dialog.open();
		return filename;
	}
}
