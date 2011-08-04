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

import java.io.IOException;
import java.net.URL;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import net.sourceforge.eclipsejaas.JaasApplication;
import net.sourceforge.eclipsejaas.internal.CallbackHandlerException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.osgi.framework.BundleException;

/**
 * This class controls all aspects of the application's execution
 */
public class Application extends JaasApplication{

	@Override
	protected WorkbenchAdvisor createWorkbenchAdvisor() {
		return new ApplicationWorkbenchAdvisor();
	}

	@SuppressWarnings("restriction")
	@Override
	protected Subject authenticate(Display display) throws LoginException,
			CoreException, CallbackHandlerException {
		return super.authenticate(display);
	}

	@Override
	protected void installPlugin(String id, URL url) throws IOException,
			BundleException {
		super.installPlugin(id, url);
	}

	@Override
	protected void logout(Display display) {
		super.logout(display);
	}

}
