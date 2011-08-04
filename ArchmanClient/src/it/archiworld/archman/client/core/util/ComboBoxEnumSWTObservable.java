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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class ComboBoxEnumSWTObservable extends AbstractObservableValue {

	private List<String> keyList;

	private CCombo widget;

	private boolean updating;

	private String currentValue;

	public ComboBoxEnumSWTObservable(CCombo combo, String[] keys) {
		this.widget = combo;
		this.keyList = Arrays.asList(keys);
		this.currentValue = (String) doGetValue();
		this.widget.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!updating) {
					String oldValue = currentValue;
					String currentValue = keyList.get(widget
							.getSelectionIndex());
					fireValueChange(Diffs.createValueDiff(oldValue,
							currentValue));
				}
			}
		});
	}

	@Override
	public Object getValueType() {
		return String.class;
	}

	@Override
	protected Object doGetValue() {
		int index = widget.getSelectionIndex();
		if (index >= 0){
			return keyList.get(index);
		}
		return null;
//		return widget.getText();
	}

	@Override
	public synchronized void dispose() {
		super.dispose();
	}

	@Override
	protected void doSetValue(Object value) {
		this.updating = true;
		String oldValue = (String) doGetValue();
		if (keyList.contains(value)) {
			widget.select(keyList.indexOf(value));
		}
		else{
			widget.select(0);
		}
		this.currentValue = (String) doGetValue();
		this.updating = false;
		fireValueChange(Diffs.createValueDiff(oldValue, currentValue));
	}

}
