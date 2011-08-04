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

import it.archiworld.archman.client.crm.report.OpenNotPaidRecordsAction;
import it.archiworld.archman.client.protocol.AddEmergencyInProtocolAction;
import it.archiworld.archman.client.protocol.AddEmergencyOutProtocolAction;
import it.archiworld.archman.client.protocol.report.TVIDReportAction;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	private IContributionItem reopenEditors;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAllAction;
	private IWorkbenchAction closeAllAction;
	private IWorkbenchAction closeAction;
	private IWorkbenchAction closeAllSavedAction;
	private IWorkbenchAction closeAllOtherAction;
	private IWorkbenchAction closePerspectiveAction;
	private IWorkbenchAction closeAllPerspectivesAction;
	private IWorkbenchAction resetPerspectiveAction;
	private IWorkbenchAction exitAction;
	private IWorkbenchAction helpContentsAction;
	private IWorkbenchAction helpSearchAction;
	private IWorkbenchAction dynamicHelpAction;
	private IWorkbenchAction aboutAction;
	private IContributionItem perspectivesShortlist;
	private IWorkbenchAction preferencesAction;
//	private IWorkbenchAction importDocumentAction;
	private IWorkbenchAction notPaidReportAction;
	private IWorkbenchAction tvidReportAction;
	private IWorkbenchAction emergencyOutProtocolAction;
	private IWorkbenchAction emergencyInProtocolAction;
	
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.
		reopenEditors = ContributionItemFactory.REOPEN_EDITORS.create(window);
		saveAction = ActionFactory.SAVE.create(window);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		closeAction = ActionFactory.CLOSE.create(window);
		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		closeAllSavedAction = ActionFactory.CLOSE_ALL_SAVED.create(window);
		closeAllOtherAction = ActionFactory.CLOSE_OTHERS.create(window);
		closePerspectiveAction = ActionFactory.CLOSE_PERSPECTIVE.create(window);
		closeAllPerspectivesAction = ActionFactory.CLOSE_ALL_PERSPECTIVES
				.create(window);
		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		exitAction = ActionFactory.QUIT.create(window);
//		introAction = ActionFactory.INTRO.create(window);
		helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
		helpSearchAction = ActionFactory.HELP_SEARCH.create(window);
		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		aboutAction = ActionFactory.ABOUT.create(window);
		perspectivesShortlist = ContributionItemFactory.PERSPECTIVES_SHORTLIST
				.create(window);
		preferencesAction = ActionFactory.PREFERENCES.create(window);
//		importDocumentAction = new ImportDocument(window);
		notPaidReportAction = new OpenNotPaidRecordsAction(window);
		tvidReportAction = new TVIDReportAction(window);
		emergencyInProtocolAction = new AddEmergencyInProtocolAction(window);
		emergencyOutProtocolAction = new AddEmergencyOutProtocolAction(window);
		
		register(saveAction);
		register(saveAllAction);
		register(closeAction);
		register(closeAllAction);
		register(closeAllSavedAction);
		register(closeAllOtherAction);
		register(closePerspectiveAction);
		register(closeAllPerspectivesAction);
		register(resetPerspectiveAction);
		register(exitAction);

//		register(introAction);
		register(helpContentsAction);
		register(helpSearchAction);
		register(dynamicHelpAction);
		register(aboutAction);

		register(preferencesAction);
//		register(importDocumentAction);
		register(notPaidReportAction);
		register(tvidReportAction);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager(
				Messages.ApplicationActionBarAdvisor_SubMenu_Label_File,
				IWorkbenchActionConstants.M_FILE);

		fileMenu.add(new Separator("new")); //$NON-NLS-1$
		
		fileMenu.add(new Separator("close")); //$NON-NLS-1$
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(closeAllSavedAction);
		fileMenu.add(closeAllOtherAction);

		fileMenu.add(new Separator("save")); //$NON-NLS-1$
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		
		fileMenu.add(new Separator("print")); //$NON-NLS-1$
		
		fileMenu.add(new Separator("import_export")); //$NON-NLS-1$
//		fileMenu.add(importDocumentAction);
		fileMenu.add(reopenEditors);
		fileMenu.add(new Separator("exit")); //$NON-NLS-1$
		fileMenu.add(exitAction);

		menuBar.add(fileMenu);

		MenuManager windowMenu = new MenuManager(
				Messages.ApplicationActionBarAdvisor_SubMenu_Label_Window,
				IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);

		MenuManager submenu = new MenuManager(
				Messages.ApplicationActionBarAdvisor_MenuEntry_OpenPerspective,
				"perspective_submenu"); //$NON-NLS-1$
		submenu.add(new Separator("perspective_shortcuts")); //$NON-NLS-1$
		submenu.add(perspectivesShortlist);

		windowMenu.add(new Separator("open_in_window")); //$NON-NLS-1$
		windowMenu.add(submenu);

		windowMenu.add(new Separator("perspective_manager")); //$NON-NLS-1$
		windowMenu.add(closePerspectiveAction);
		windowMenu.add(closeAllPerspectivesAction);
		windowMenu.add(resetPerspectiveAction);
		windowMenu.add(new Separator("preferences")); //$NON-NLS-1$
		windowMenu.add(preferencesAction);

		MenuManager actionMenu = new MenuManager("Actions");
		menuBar.add(actionMenu);
		MenuManager emergencyMenu = new MenuManager("Emergency Register");
		emergencyMenu.add(emergencyInProtocolAction);
		emergencyMenu.add(emergencyOutProtocolAction);
		actionMenu.add(emergencyMenu);

//		actionMenu.add(new Separator("emergency_shortcuts")); //$NON-NLS-1$

		MenuManager reportMenu = new MenuManager(
				Messages.ApplicationActionBarAdvisor_Menu_Report);
		reportMenu.add(new Separator(Messages.ApplicationActionBarAdvisor_MenuSeparator_Reports));
		reportMenu.add(notPaidReportAction);
		reportMenu.add(tvidReportAction);
		actionMenu.add(reportMenu);

		
		
		
		MenuManager helpMenu = new MenuManager(
				Messages.ApplicationActionBarAdvisor_SubMenu_Label_Help,
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);
		helpMenu.add(new Separator("welcome")); //$NON-NLS-1$
//		helpMenu.add(introAction);

		helpMenu.add(new Separator("help")); //$NON-NLS-1$
		helpMenu.add(helpContentsAction);
		helpMenu.add(helpSearchAction);
		helpMenu.add(dynamicHelpAction);

		helpMenu.add(new Separator("about")); //$NON-NLS-1$
		helpMenu.add(aboutAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		toolbar.add(new Separator("new")); //$NON-NLS-1$
		toolbar.add(new Separator("save")); //$NON-NLS-1$
		toolbar.add(saveAction);
		toolbar.add(saveAllAction);
		coolBar.add(new ToolBarContributionItem(toolbar, "File")); //$NON-NLS-1$
	}

}
