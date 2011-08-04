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
package it.archiworld.archman.client.core.connect;

import it.archiworld.archman.client.core.Activator;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RMIConnection implements Connection {
		
	private String server="127.0.0.1"; //$NON-NLS-1$
	private String port="1099"; //$NON-NLS-1$

	public Object get(String ejb) throws ConnectionException {
		try {
			if(Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMISERVER)!="")
				server=Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMISERVER);
			if(Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMIPORT)!="")
				port=Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMIPORT);

			System.out.println("Server: "+server); //$NON-NLS-1$
			System.out.println("Port: "+port); //$NON-NLS-1$
			Hashtable<String,String> env = new Hashtable<String,String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory"); //$NON-NLS-1$
			env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces"); //$NON-NLS-1$
			env.put(Context.PROVIDER_URL, server+":"+port); //$NON-NLS-1$
			
			if(Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMISERVER)!="")
				env.put(Context.SECURITY_PRINCIPAL, Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMISERVER));
			if(Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMIPORT)!="")
				env.put(Context.SECURITY_CREDENTIALS, Activator.getDefault().getPreferenceStore().getString(RMIConnectionSettingsPage.RMIPORT));
			InitialContext ctx = new InitialContext(env);
			return ctx.lookup(ejb+"/remote"); //$NON-NLS-1$
		}
		catch (NamingException e){
			e.printStackTrace();
			throw new ConnectionException("Naming exception"); //$NON-NLS-1$
		}
	}

}
