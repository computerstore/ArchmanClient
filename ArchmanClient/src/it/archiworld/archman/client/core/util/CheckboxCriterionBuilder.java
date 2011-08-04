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

public class CheckboxCriterionBuilder extends CriteriaBuilder {

	public static final int OPERATOR_IGNORE = 0;

	public static final int OPERATOR_IS_TRUE = 1;

	public static final int OPERATOR_IS_FALSE = 2;

	private Combo operator;

	private String column;

	public CheckboxCriterionBuilder(Combo operator, String column) {
		this.operator = operator;
		this.column = column;
	}

	@Override
	public Conjunction updateConjunction(Conjunction conjunction) {
		if (operator.getSelectionIndex() == OPERATOR_IS_TRUE) {
			conjunction.add(Restrictions.eq(this.column, true));
		} else if (operator.getSelectionIndex() == OPERATOR_IS_FALSE) {
			conjunction.add(Restrictions.eq(this.column, false));
		}
		return conjunction;
	}
}
