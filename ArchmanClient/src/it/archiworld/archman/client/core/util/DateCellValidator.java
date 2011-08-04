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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.jface.viewers.ICellEditorValidator;

public class DateCellValidator implements ICellEditorValidator {

	public String isValid(Object value) {
		if (value == null)
			return Messages.DateCellValidator_ValidatorMessage_DateNull;
		if (value instanceof String) {
			try {
				DateFormat.getInstance().parse((String) value);
			} catch (ParseException pe) {
				return pe.getLocalizedMessage();
			}
		}
		if (value instanceof Date) {
			return null;
		}
		return Messages.DateCellValidator_ValidatorMessage_DateWrongType;
	}
}
