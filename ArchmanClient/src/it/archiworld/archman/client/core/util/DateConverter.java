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
import java.util.Date;

import org.eclipse.core.databinding.conversion.IConverter;

public class DateConverter implements IConverter {

	public static final int TIMESTAMP = 0;

	public static final int DATE = 1;

	public static final int TIME = 2;

	private int type = DateConverter.TIMESTAMP;

	private int dateFormat = DateFormat.DEFAULT;

	private int timeFormat = DateFormat.DEFAULT;

	public DateConverter() {
		;
	}

	public DateConverter(int type, int dateFormat, int timeFormat) {
		this.type = type;
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
	}

	@Override
	public Object convert(Object fromObject) {
		String result = new String();
		switch (type) {
		case 1:
			result = DateFormat.getDateInstance(this.dateFormat).format(
					(Date) fromObject);
			break;
		case 2:
			result = DateFormat.getTimeInstance(this.timeFormat).format(
					(Date) fromObject);
			break;
		default:
			result = DateFormat.getDateTimeInstance(this.dateFormat, this.timeFormat)
					.format((Date) fromObject);
		}
		return result;
	}

	@Override
	public Object getFromType() {
		return Date.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

}
