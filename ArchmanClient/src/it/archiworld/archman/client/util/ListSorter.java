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

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListSorter {
	
	@SuppressWarnings("unchecked")
	public static List<Object> sort(List list){
		Collections.sort(list,new Comparator<Object>(){

			@Override
			public int compare(Object o1, Object o2) {
				Collator col = Collator.getInstance();
				return col.compare(o1.toString(), o2.toString());
			}
			
		});
		return list;
	}
}
