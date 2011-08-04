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

import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CDateTimeCellEditor extends CellEditor {

	private CDateTime dateTime;

	private ModifyListener modifyListener;

	private static final int defaultStyle = CDT.DROP_DOWN | CDT.DATE_MEDIUM | CDT.COMPACT;

	public CDateTimeCellEditor() {
		setStyle(defaultStyle);
	}

	public CDateTimeCellEditor(Composite parent) {
		super(parent, defaultStyle);
	}

	public CDateTimeCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Control createControl(Composite parent) {
		dateTime = new CDateTime(parent, getStyle());
		dateTime.addModifyListener(getModifyListener());
		return dateTime;
	}

    protected void editOccured(ModifyEvent e) {
        Date value = dateTime.getSelection();
        if (value == null) {
			value = new Date();
		}
        Object typedValue = value;
        boolean oldValidState = isValueValid();
        boolean newValidState = isCorrect(typedValue);
        if (typedValue == null && newValidState) {
			Assert.isTrue(false,
                    "Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
        if (!newValidState) {
            // try to insert the current value into the error message.
            setErrorMessage(MessageFormat.format(getErrorMessage(),
                    new Object[] { value }));
        }
        valueChanged(oldValidState, newValidState);
    }

    private ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    editOccured(e);
                }
            };
        }
        return modifyListener;
    }

	@Override
	protected Object doGetValue() {
		return dateTime.getSelection();
	}

	@Override
	protected void doSetFocus() {
		dateTime.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		Assert.isTrue(value == null || value instanceof Date);
		dateTime.removeModifyListener(getModifyListener());
		dateTime.setSelection((Date) value);
		dateTime.addModifyListener(getModifyListener());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
