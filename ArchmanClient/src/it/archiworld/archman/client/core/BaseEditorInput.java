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
package it.archiworld.archman.client.core;



import org.eclipse.ui.IEditorInput;

public abstract class BaseEditorInput implements IEditorInput {

	private Object bean;

	public BaseEditorInput(Object bean) {
			try {
				System.out.println("Create BeanSupervisor with class " + bean.getClass().getCanonicalName()); //$NON-NLS-1$
				this.bean = bean;
				refresh();
			} catch (Throwable t) {
				t.printStackTrace();
			}
	}

	protected void setBean(Object bean) {
		this.bean=bean;
	}

	public final Object getBean() {
		return this.bean;
	}
	
	public abstract void refresh();

	public abstract Object createOrUpdate() throws Throwable;

}
