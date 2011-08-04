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
package it.archiworld.archman.client.core.util;

import it.archiworld.common.protocol.EntryAddress;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class AddressViewerSWTObservable extends AbstractObservableValue {

	AddressViewer viewer;

	boolean updating;

	EntryAddress currentValue;

	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (!updating) {
				EntryAddress oldValue = currentValue;
				EntryAddress currentValue = (EntryAddress) doGetValue();
				fireValueChange(Diffs.createValueDiff(oldValue, currentValue));
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			;
		}
	};

	public AddressViewerSWTObservable(AddressViewer viewer) {
		this.viewer = viewer;
		this.currentValue = null;
		this.viewer.addSelectionListener(listener);
	}

	@Override
	public Object getValueType() {
		return EntryAddress.class;
	}

	@Override
	protected Object doGetValue() {
		return viewer.getAddress();
	}

	@Override
	public synchronized void dispose() {
		super.dispose();
	}

	@Override
	protected void doSetValue(Object value) {
		if (value == null || value instanceof EntryAddress) {
			this.updating = true;
			EntryAddress oldValue = (EntryAddress) doGetValue();
			viewer.setAddress((EntryAddress) value);
			this.currentValue = (EntryAddress) doGetValue();
			this.updating = false;
			fireValueChange(Diffs.createValueDiff(oldValue, currentValue));
		} else {
			System.out
					.println("Warning! doSetValue called with invalid value on AddressViewerSWTObservable!"); //$NON-NLS-1$
		}
	}

}
