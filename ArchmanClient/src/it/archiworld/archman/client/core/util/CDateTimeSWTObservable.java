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

import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
//import org.eclipse.jface.internal.databinding.provisional.swt.AbstractSWTObservableValue;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


public class CDateTimeSWTObservable	extends AbstractObservableValue {


	final CDateTime dateTime;

	private boolean updating = false;

	private Date currentValue;

	private SelectionListener selectionListener = new SelectionListener() {

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (!updating) {
				Date oldValue = currentValue;
				currentValue = dateTime.getSelection();
				fireValueChange(Diffs.createValueDiff(oldValue, currentValue));
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!updating) {
				Date oldValue = currentValue;
				currentValue = dateTime.getSelection();
				fireValueChange(Diffs.createValueDiff(oldValue, currentValue));
			}
		}

	};

	public CDateTimeSWTObservable(CDateTime widget) {
		this.dateTime = widget;
		currentValue = dateTime.getSelection();
		widget.addSelectionListener(selectionListener);

	}

	@Override
	protected void doSetValue(Object value) {
		Date oldValue = dateTime.getSelection();
		try {
			updating = true;
			dateTime.setSelection(value == null ? null : (Date) value);
			oldValue = dateTime.getSelection();
		} finally {
			updating = false;
		}
		fireValueChange(Diffs
				.createValueDiff(oldValue, dateTime.getSelection()));
	}

	@Override
	protected Object doGetValue() {
		return dateTime.getSelection();
	}

	@Override
	public Object getValueType() {
		return Date.class;
	}

	@Override
	public void dispose() {
		if (!dateTime.isDisposed()) {
			dateTime.removeSelectionListener(selectionListener);
		}
		super.dispose();
	}

}
