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
import org.hibernate.criterion.Restrictions;

public class TextCriterionBuilder extends CriteriaBuilder {

	public static final int OPERATOR_EQUAL_OR = 0;

	public static final int OPERATOR_FROM_TO = 1;

	public static final int OPERATOR_NOT_AND = 2;

	public static final int OPERATOR_EMPTY = 3;

	public static final int OPERATOR_NOT_EMPTY = 4;

	private String column;

	private Combo operator;

	private Text first;

	private Text second;

	public TextCriterionBuilder(Combo operator, Text first, Text second,
			String column) {
		this.column = column;
		this.operator = operator;
		this.first = first;
		this.second = second;
	}

	@Override
	public Conjunction updateConjunction(Conjunction conjunction) {
		if (operator.getSelectionIndex() == OPERATOR_EQUAL_OR) {
			if ((this.first.getText() != null && this.first.getText() != "") //$NON-NLS-1$
					|| (this.second.getText() != null && this.second.getText() != "")) { //$NON-NLS-1$
				if (this.first.getText() != null && this.first.getText() != "") { //$NON-NLS-1$
					if (this.second.getText() != null
							&& this.second.getText() != "") { //$NON-NLS-1$
						conjunction.add(Restrictions.or(Restrictions.ilike(
								this.column, this.first.getText()),
								Restrictions.ilike(this.column, this.second
										.getText())));
					} else {
						conjunction.add(Restrictions.ilike(this.column, first
								.getText()
								+ "%")); //$NON-NLS-1$
					}
				} else {
					if (this.second.getText() != null
							&& this.second.getText() != "") { //$NON-NLS-1$
						conjunction.add(Restrictions.ilike(this.column,
								this.second.getText()));
					}
				}
			}
		} else if (operator.getSelectionIndex() == OPERATOR_FROM_TO) {
			conjunction.add(Restrictions.and(Restrictions.lt(this.column,
					this.second.getText()), Restrictions.gt(this.column,
					this.first.getText())));
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_AND) {
			if ((this.first.getText() != null && this.first.getText() != "") //$NON-NLS-1$
					|| (this.second.getText() != null && this.second.getText() != "")) { //$NON-NLS-1$
				if (this.first.getText() != null && this.first.getText() != "") { //$NON-NLS-1$
					if (this.second.getText() != null
							&& this.second.getText() != "") { //$NON-NLS-1$
						conjunction.add(Restrictions.and(Restrictions
								.not(Restrictions.ilike(this.column, this.first
										.getText())), Restrictions
								.not(Restrictions.ilike(this.column,
										this.second.getText()))));
					} else {
						conjunction.add(Restrictions.not(Restrictions.ilike(
								this.column, this.first.getText())));
					}
				} else {
					conjunction.add(Restrictions.not(Restrictions.ilike(
							this.column, this.second.getText())));
				}
			}
		} else if (operator.getSelectionIndex() == OPERATOR_EMPTY) {
			conjunction.add(Restrictions.isNull(this.column));
		} else if (operator.getSelectionIndex() == OPERATOR_NOT_EMPTY) {
			conjunction.add(Restrictions.isNotNull(this.column));
		}
		return conjunction;
	}
}
