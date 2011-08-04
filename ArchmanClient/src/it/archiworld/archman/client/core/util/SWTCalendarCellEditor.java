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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.vafada.swtcalendar.SWTCalendar;

public class SWTCalendarCellEditor extends CellEditor {
	
	SWTCalendar calendar;
	
	Text dateText;
	
	Button selectButton;
	
	ModifyListener modifyListener;
	
	private static final int defaultStyle = SWT.NONE;
	
	SWTCalendarCellEditor() {
		setStyle(defaultStyle);
	}
	
	SWTCalendarCellEditor(Composite parent) {
		super(parent, defaultStyle);
	}
	
	SWTCalendarCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Control createControl(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doSetFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doSetValue(Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		this.calendar.dispose();
		this.dateText.dispose();
		this.selectButton.dispose();
		super.dispose();
	}

    protected void editOccured(ModifyEvent e) {
         valueChanged(true, true);
    }
	
	public final ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    editOccured(e);
                }
            };
        }
        return modifyListener;
	}

}
