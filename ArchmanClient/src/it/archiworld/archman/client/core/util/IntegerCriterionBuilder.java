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
import org.eclipse.swt.widgets.Text;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class IntegerCriterionBuilder extends CriteriaBuilder {

	public static final int OPERATOR_EQUAL_OR = 0;

	public static final int OPERATOR_FROM_TO = 1;

	public static final int OPERATOR_NOT_AND = 2;

	public static final int OPERATOR_EMPTY = 3;

	public static final int OPERATOR_NOT_EMPTY = 4;

	public static final int OPERATOR_MIN = 5;

	public static final int OPERATOR_MAX = 6;

	private String column;

	private Combo operator;

	private Text first;

	private Text second;

	public IntegerCriterionBuilder(Combo operator, Text first, Text second,
			String column) {
		this.column = column;
		this.operator = operator;
		this.first = first;
		this.second = second;
	}

	@Override
	public Conjunction updateConjunction(Conjunction conjunction) {
		try {
			if (operator.getSelectionIndex() == OPERATOR_EQUAL_OR) {
				if ((this.first.getText() != null && this.first.getText() != "") //$NON-NLS-1$
						|| (this.second.getText() != null && this.second
								.getText() != "")) { //$NON-NLS-1$
					if (this.first.getText() != null
							&& this.first.getText() != "") { //$NON-NLS-1$
						if (this.second.getText() != null
								&& this.second.getText() != "") { //$NON-NLS-1$
							conjunction.add(Restrictions.or(Restrictions.eq(
									this.column, Integer.parseInt(this.first
											.getText())), Restrictions.eq(
									this.column, Integer.parseInt(this.second
											.getText()))));
						} else {
							conjunction.add(Restrictions.eq(this.column,
									Integer.parseInt(first.getText())));
						}
					} else {
						if (this.second.getText() != null
								&& this.second.getText() != "") { //$NON-NLS-1$
							conjunction.add(Restrictions.eq(this.column,
									Integer.parseInt(this.second.getText())));
						}
					}
				}
			} else if (operator.getSelectionIndex() == OPERATOR_FROM_TO) {
				conjunction.add(Restrictions.and(Restrictions.lt(this.column,
						Integer.parseInt(this.second.getText())),
						Restrictions.gt(this.column, Integer
								.parseInt(this.first.getText()))));
			} else if (operator.getSelectionIndex() == OPERATOR_NOT_AND) {
				if ((this.first.getText() != null && this.first.getText() != "") //$NON-NLS-1$
						|| (this.second.getText() != null && this.second
								.getText() != "")) { //$NON-NLS-1$
					if (this.first.getText() != null
							&& this.first.getText() != "") { //$NON-NLS-1$
						if (this.second.getText() != null
								&& this.second.getText() != "") { //$NON-NLS-1$
							conjunction.add(Restrictions.and(Restrictions
									.not(Restrictions.eq(this.column, Integer
											.parseInt(this.first.getText()))),
									Restrictions.not(Restrictions.eq(
											this.column, Integer
													.parseInt(this.second
															.getText())))));
						} else {
							conjunction.add(Restrictions.not(Restrictions.eq(
									this.column, Integer.parseInt(this.first
											.getText()))));
						}
					} else {
						conjunction.add(Restrictions.not(Restrictions.eq(
								this.column, Integer.parseInt(this.second
										.getText()))));
					}
				}
			} else if (operator.getSelectionIndex() == OPERATOR_EMPTY) {
				conjunction.add(Restrictions.isNull(this.column));
			} else if (operator.getSelectionIndex() == OPERATOR_NOT_EMPTY) {
				conjunction.add(Restrictions.isNotNull(this.column));
			} else if (operator.getSelectionIndex() == OPERATOR_MIN) {
				
			} else if (operator.getSelectionIndex() == OPERATOR_MAX) {

			}
		} catch (NumberFormatException nfe) {

		}
		return conjunction;
	}

	@Override
	public Order getOrder() {
		if (this.operator.getSelectionIndex() == OPERATOR_MIN) {
			return Order.asc(this.column);
		} else if (this.operator.getSelectionIndex() == OPERATOR_MAX) {
			return Order.desc(this.column);
		} else
		return null;
	}
}
