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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	TrayItem trayItem;

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowPerspectiveBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowFastViewBars(false);
	}

	@Override
	public void postWindowCreate() {
		trayItem = new TrayItem(getWindowConfigurer().getWindow().getShell()
				.getDisplay().getSystemTray(), SWT.NONE);
		trayItem
				.setText(Messages.ApplicationWorkbenchWindowAdvisor_TrayItem_ToolTipText);
		ImageDescriptor imgDescr = ImageDescriptor.createFromFile(this.getClass(), "/immages/archman_icon_16_32.bmp"); //$NON-NLS-1$
		trayItem.setImage(imgDescr.createImage());
		trayItem.setVisible(false);
		trayItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(e.toString());
				Shell shell =  getWindowConfigurer().getWindow().getShell();
				shell.setFocus();
				shell.setActive();
				shell.setVisible(true);
				shell.setMinimized(false);
			}
			
		});
		getWindowConfigurer().getWindow().getShell().addShellListener(
				new ShellListener() {

					@Override
					public void shellActivated(ShellEvent e) {
						;
					}

					@Override
					public void shellClosed(ShellEvent e) {
						;
					}

					@Override
					public void shellDeactivated(ShellEvent e) {
						;
					}

					@Override
					public void shellDeiconified(ShellEvent e) {
						getWindowConfigurer().getWindow().getShell().setVisible(true);
						trayItem.setVisible(false);
					}

					@Override
					public void shellIconified(ShellEvent e) {
						getWindowConfigurer().getWindow().getShell().setVisible(false);
						trayItem.setVisible(true);
					}

				});

		super.postWindowCreate();
	}

	@Override
	public boolean preWindowShellClose() {
		return super.preWindowShellClose();
	}

}
