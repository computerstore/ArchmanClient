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

import org.eclipse.swt.widgets.Combo;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;

public class ComboBoxCriterionBuilder extends CriteriaBuilder {

	public static final int OPERATOR_INDIFFERENT = 0;

	public static final int OPERATOR_EQUAL = 1;

	public static final int OPERATOR_NOT_EQUAL = 2;

	public static final int OPERATOR_EMPTY = 3;

	public static final int OPERATOR_NOT_EMPTY = 4;

	private String column;

	private Combo operator;

	private Combo first;

	private String[] keys;

	public ComboBoxCriterionBuilder(Combo operator, Combo first, String[] keys,
			String column) {
		this.column = column;
		this.operator = operator;
		this.first = first;
		this.keys = keys;
	}

	@Override
	public Conjunction updateConjunction(Conjunction conjunction) {
		if (operator.getSelectionIndex() == OPERATOR_EQUAL) {
			if (first.getSelectionIndex() >= 0) {
				conjunction.add(Restrictions.eq(this.column, keys[first
						.getSelectionIndex()]));
			}
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_EQUAL) {
			if (first.getSelectionIndex() >= 0) {
				conjunction.add(Restrictions.ne(this.column, keys[first
						.getSelectionIndex()]));
			}
		} else if (operator.getSelectionIndex() == OPERATOR_EMPTY) {
			conjunction.add(Restrictions.isNull(this.column));
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_EMPTY) {
			conjunction.add(Restrictions.isNotNull(this.column));
		}
		return conjunction;
	}
}
