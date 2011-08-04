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

public class ConnectionFactory {

	private static final String CONNECTION = "preference.Connection"; //$NON-NLS-1$
	private static String connectionclass = "it.archiworld.archman.client.core.connect.RMIConnection"; //$NON-NLS-1$
	private ConnectionFactory(){}
	
	public static Object getConnection(String ejb) throws ConnectionException {
		try {
			if (Activator.getDefault().getPreferenceStore().getString(CONNECTION)!="") //$NON-NLS-1$
				connectionclass = Activator.getDefault().getPreferenceStore().getString(CONNECTION);
			System.out.println("Connection: "+connectionclass); //$NON-NLS-1$
			
			Connection connection = (Connection)Class.forName(connectionclass).newInstance();
			return connection.get(ejb);
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
			throw new ConnectionException("Connection class not found exception"); //$NON-NLS-1$
		}
		catch (InstantiationException e){
			e.printStackTrace();
			throw new ConnectionException("Could not instantiate connection class");			 //$NON-NLS-1$
		}
		catch (IllegalAccessException e){
			e.printStackTrace();
			throw new ConnectionException("Illegal access to connection class");						 //$NON-NLS-1$
		}
	}
}
