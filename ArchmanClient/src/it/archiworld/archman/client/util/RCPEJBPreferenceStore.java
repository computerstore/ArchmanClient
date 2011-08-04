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
package it.archiworld.archman.client.util;

import it.archiworld.archman.client.core.connect.ConnectionException;
import it.archiworld.archman.client.core.connect.ConnectionFactory;
import it.archiworld.util.EJBPreferenceStore;

import java.sql.Timestamp;
import java.util.List;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;

public class RCPEJBPreferenceStore extends EventManager implements IPreferenceStore {

	boolean dirty=false;
	EJBPreferenceStore store; 
	
	public RCPEJBPreferenceStore() throws ConnectionException {
		super();
		try{
			store = (EJBPreferenceStore)ConnectionFactory.getConnection("EJBPreferenceStoreBean"); //$NON-NLS-1$
		}
		catch(ConnectionException ex){
			throw ex;
		}
	}
	
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	@Override
	public boolean contains(String name) {
		return store.contains(name);
	}

	@Override
	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
		final Object[] finalListeners = getListeners();
		// Do we need to fire an event.
		if (finalListeners.length > 0 && (oldValue == null || !oldValue.equals(newValue))) {
			final PropertyChangeEvent pe = new PropertyChangeEvent(this, name, oldValue, newValue);
		    for (int i = 0; i < finalListeners.length; ++i) {
		    	final IPropertyChangeListener l = (IPropertyChangeListener) finalListeners[i];
		    	SafeRunnable.run(new SafeRunnable(JFaceResources.getString("PreferenceStore.changeError")) { //$NON-NLS-1$
		    		public void run() {
		    			l.propertyChange(pe);
		            }
		        });
		    }
        }
	}

	@Override
	public boolean getBoolean(String name) {
		return store.getBoolean(name);
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		return store.getDefaultBoolean(name);
	}

	@Override
	public double getDefaultDouble(String name) {
		return store.getDefaultDouble(name);
	}

	@Override
	public float getDefaultFloat(String name) {
		return store.getDefaultFloat(name);
	}

	@Override
	public int getDefaultInt(String name) {
		return store.getDefaultInt(name);
	}

	@Override
	public long getDefaultLong(String name) {
		return store.getDefaultLong(name);
	}

	@Override
	public String getDefaultString(String name) {
		return store.getDefaultString(name);
	}

	@Override
	public double getDouble(String name) {
		return store.getDouble(name);
	}

	@Override
	public float getFloat(String name) {
		return store.getFloat(name);
	}

	@Override
	public int getInt(String name) {
		return store.getInt(name);
	}

	@Override
	public long getLong(String name) {
		return store.getLong(name);
	}

	@Override
	public String getString(String name) {
		return store.getString(name);
	}

	public List<String> getStringList(String name, Timestamp timestamp) {
		return store.getStringList(name, timestamp);
	}

	@Override
	public boolean isDefault(String name) {
		return false;
	}

	@Override
	public boolean needsSaving() {
		return dirty;
	}

	@Override
	public void putValue(String name, String value) {
		String oldValue = getString(name);
		if (oldValue == null || !oldValue.equals(value)) {
			setValue(name, value);
		    dirty = true;
		}
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
	    removeListenerObject(listener);
	}

	@Override
	public void setDefault(String name, double value) {
		store.setDefault(name,value);
	}

	@Override
	public void setDefault(String name, float value) {
		store.setDefault(name,value);
	}

	@Override
	public void setDefault(String name, int value) {
		store.setDefault(name,value);
	}

	@Override
	public void setDefault(String name, long value) {
		store.setDefault(name,value);
	}

	@Override
	public void setDefault(String name, String defaultObject) {
		store.setDefault(name,defaultObject);
	}

	@Override
	public void setDefault(String name, boolean value) {
		store.setDefault(name,value);
	}

	@Override
	public void setToDefault(String name) {
		store.setToDefault(name);
	}

	@Override
	public void setValue(String name, double value) {
		store.setValue(name,value);
	}

	@Override
	public void setValue(String name, float value) {
		store.setValue(name,value);
	}

	@Override
	public void setValue(String name, int value) {
		store.setValue(name,value);
	}

	@Override
	public void setValue(String name, long value) {
		store.setValue(name,value);
	}

	@Override
	public void setValue(String name, String value) {
		store.setValue(name,value);
	}

	@Override
	public void setValue(String name, boolean value) {
		store.setValue(name,value);
	}

	public void setValue(String name, List<String> value) {
		store.setValue(name,value);
	}

}
