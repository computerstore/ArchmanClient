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

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;



public class AdvancedComboSelectionAdapter implements KeyListener, FocusListener {
	private KeyInputFilter filter;
	private ComboViewer viewer;

	public AdvancedComboSelectionAdapter(ComboViewer viewer) {
		this.filter = new KeyInputFilter();
		this.viewer = viewer;
		this.viewer.addFilter(filter);
		this.viewer.getCCombo().addKeyListener(this);
		this.viewer.getCCombo().addFocusListener(this);
	}

	public void keyPressed(KeyEvent e) {
		e.doit = false;
		System.out.println(e.keyCode);
		if (e.keyCode != SWT.ARROW_UP && e.keyCode != SWT.ARROW_DOWN && e.keyCode != SWT.ESC && e.keyCode != SWT.DEL && e.keyCode != SWT.CR
				&& e.keyCode != SWT.LF && e.keyCode != SWT.BS && e.keyCode!=SWT.SHIFT) {
			filter.actualString.append((char) e.keyCode);
		} else if (e.keyCode == SWT.DEL || e.keyCode == SWT.BS ) {
			if (filter.actualString.length() > 0) {
				filter.actualString = new StringBuffer(filter.actualString.substring(0, filter.actualString.length() - 1));
			}
		} else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
			e.doit = true;
		} else {
			filter.actualString = new StringBuffer();
		}

		if (!e.doit) {
			viewer.refresh();
			if (viewer.getSelection().isEmpty()) {
				Object value = viewer.getElementAt(0);
				if (value != null) {
					viewer.setSelection(new StructuredSelection(value));
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void focusGained(FocusEvent e) {
		filter.actualString = new StringBuffer();
	}

	public void focusLost(FocusEvent e) {
		filter.actualString = new StringBuffer();
		viewer.refresh();
	}
	
	protected class KeyInputFilter extends ViewerFilter {
		protected StringBuffer actualString = new StringBuffer();
		
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (actualString.equals("")) {
				return true;
			} else {
				ILabelProvider provider = (ILabelProvider)((ComboViewer)viewer).getLabelProvider();
				String labelValue = provider.getText(element).toLowerCase();
				return labelValue.startsWith(actualString.toString().toLowerCase());
			}
		}
	}
}
