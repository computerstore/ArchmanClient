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

import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.widgets.Combo;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class DateCriterionBuilder extends CriteriaBuilder {

	public static final int OPERATOR_EQUAL_OR = 0;

	public static final int OPERATOR_FROM_TO = 1;

	public static final int OPERATOR_NOT_AND = 2;

	public static final int OPERATOR_EMPTY = 3;

	public static final int OPERATOR_NOT_EMPTY = 4;

	public static final int OPERATOR_MIN = 5;

	public static final int OPERATOR_MAX = 6;

	private Combo operator;

	private CDateTime first;

	private CDateTime second;

	private String column;

	public DateCriterionBuilder(Combo operator, CDateTime first,
			CDateTime second, String column) {
		this.operator = operator;
		this.first = first;
		this.second = second;
		this.column = column;
	}

	@Override
	public Conjunction updateConjunction(Conjunction conjunction) {
		if (operator.getSelectionIndex() == OPERATOR_EQUAL_OR) {
			if ((this.first.getSelection() != null)
					|| (this.second.getSelection() != null)) {
				if (this.first.getSelection() != null) {
					if (this.second.getSelection() != null) {
						conjunction
								.add(Restrictions.or(
										Restrictions.eq(this.column, this.first
												.getSelection()), Restrictions
												.eq(column, second
														.getSelection())));
					} else {
						conjunction.add(Restrictions.eq(this.column, first
								.getSelection()));
					}
				} else {
					if (this.second.getSelection() != null) {
						conjunction.add(Restrictions.ilike(this.column,
								this.second.getSelection()));
					}
				}
			}
		} else if (operator.getSelectionIndex() == OPERATOR_FROM_TO) {
			conjunction.add(Restrictions.and(Restrictions.lt(this.column,
					this.second.getSelection()), Restrictions.gt(this.column,
					this.first.getSelection())));
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_AND) {
			if ((this.first.getSelection() != null)
					|| (this.second.getSelection() != null)) {
				if (this.first.getSelection() != null) {
					if (this.second.getSelection() != null) {
						conjunction.add(Restrictions.and(Restrictions
								.not(Restrictions.eq(this.column, this.first
										.getSelection())), Restrictions
								.not(Restrictions.eq(this.column, this.second
										.getSelection()))));
					} else {
						conjunction.add(Restrictions.not(Restrictions.eq(
								this.column, this.first.getSelection())));
					}
				} else {
					conjunction.add(Restrictions.not(Restrictions.eq(
							this.column, this.second.getSelection())));
				}
			}
		} else if (operator.getSelectionIndex() == OPERATOR_EMPTY) {
			conjunction.add(Restrictions.isNull(this.column));
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_EMPTY) {
			conjunction.add(Restrictions.isNotNull(this.column));
		} else if (operator.getSelectionIndex() == OPERATOR_MIN) {

		} else if (operator.getSelectionIndex() == OPERATOR_MAX) {

		}
		return conjunction;
	}

	public Order getOrder() {
		if (this.operator.getSelectionIndex() == OPERATOR_MIN) {
			return Order.asc(this.column);
		} else if (this.operator.getSelectionIndex() == OPERATOR_MAX) {
			return Order.desc(this.column);
		}
		return null;
	}

}
