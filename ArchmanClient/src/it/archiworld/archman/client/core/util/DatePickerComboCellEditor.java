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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.gface.date.DatePickerCombo;
import com.gface.date.DatePickerStyle;

public class DatePickerComboCellEditor extends CellEditor {

	DatePickerCombo datePickerCombo;

	private final static int defaultStyle = DatePickerStyle.SINGLE_CLICK_SELECTION
			| DatePickerStyle.YEAR_BUTTONS
			| DatePickerStyle.WEEKS_STARTS_ON_MONDAY;

	public DatePickerComboCellEditor() {
		setStyle(defaultStyle);
	}

	public DatePickerComboCellEditor(Composite parent) {
		super(parent, defaultStyle);
	}

	public DatePickerComboCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Control createControl(Composite parent) {
		datePickerCombo = new DatePickerCombo(parent, SWT.NONE, getStyle());
		return datePickerCombo;
	}

	@Override
	protected Object doGetValue() {
		return datePickerCombo.getDate();
	}

	@Override
	protected void doSetFocus() {
		datePickerCombo.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		if (value == null || value instanceof Date)
			datePickerCombo.setDate((Date) value);
	}

	@Override
	public void dispose() {
		datePickerCombo.dispose();
		super.dispose();
	}

}
