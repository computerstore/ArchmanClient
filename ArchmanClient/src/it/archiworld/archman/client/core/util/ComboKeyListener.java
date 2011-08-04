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

import it.archiworld.archman.client.core.ScrolledFormEditorBase;

import java.util.Date;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;

public class ComboKeyListener implements KeyListener {
	
	ComboViewer combo;
	String filter;
    Date lastchange=new Date();
    ISelection selected;
    final String[] items;
    final String[] keys;
    ScrolledFormEditorBase editor;
    
   	public ComboKeyListener(final CCombo combo, final String[] items, final String[] keys){
		super();

		this.combo=new ComboViewer(combo);
		this.items = items;
		this.keys = keys;
		this.combo.setContentProvider(new IStructuredContentProvider() {
			
			public Object[] getElements(Object inputElement) {
				return keys;
			}

			public void dispose() {				
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {				
			}
			
		});
		
		this.combo.setLabelProvider(new ILabelProvider(){

			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getText(Object element) {
//				System.out.println("element"+element.toString());
				for(int i=0;i<keys.length;i++){
//					System.out.println("item1"+items[i]);
					if(keys[i].equals(element.toString())){
//						System.out.println("item2"+items[i]);
						return items[i];
					}
				}
				return element == null ? "" : element.toString();//$NON-NLS-1$
			}

			@Override
			public void addListener(ILabelProviderListener listener) {				
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		this.combo.setInput("");

	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
    public void keyReleased(KeyEvent e) {
		
	  	System.out.println("selected:"+((IStructuredSelection)combo.getSelection()).getFirstElement());

    	if ((new Date().getTime()-lastchange.getTime())>4000)
    		filter="";
    	lastchange=new Date();

 	   if(e.keyCode==SWT.ARROW_DOWN || 
 		  e.keyCode==SWT.ARROW_UP ||
 		  e.keyCode==SWT.PAGE_DOWN ||
 		  e.keyCode==SWT.PAGE_UP) {
 		   if(combo.getSelection()!=null)
 			   selected=combo.getSelection();
 		   return;
 	   }
	   
    	if(e.character==SWT.CR || e.keyCode == SWT.KEYPAD_CR){
//    		combo.setSelection(selected,true);
    		//combo.setText(combo.getItem(selected));
    		return;
    	}

    	if(e.keyCode==SWT.CTRL || 
    	   e.keyCode==SWT.ALT || 
    	   e.keyCode==SWT.ARROW_LEFT ||
    	   e.keyCode==SWT.ARROW_RIGHT ||
    	   e.keyCode==SWT.BREAK ||
    	   e.keyCode==SWT.COMMAND ||
    	   e.keyCode==SWT.CAPS_LOCK ||
    	   e.keyCode==SWT.END ||
    	   e.keyCode==SWT.HELP ||
    	   e.keyCode==SWT.INSERT ||
    	   e.keyCode==SWT.NUM_LOCK || 
    	   e.keyCode==SWT.PAUSE || 
    	   e.keyCode==SWT.PRINT_SCREEN
    	   )
    		return;
    	
      if(e.character == SWT.DEL || 
         e.character == SWT.ESC || 
         e.character == SWT.TAB)
        return;
    	
    	if(e.keyCode==SWT.BS)
    		filter = filter.substring(0, filter.length()-1);
    	else
    		if(e.keyCode!=SWT.SHIFT)
    			filter += Character.toString(e.character);

    	combo.getCCombo().setText(filter);
    	
        for (int i = 0; i < items.length; i++) {
        	if (items[i].toLowerCase().startsWith(filter.toLowerCase())) {
       	  		combo.setSelection(new StructuredSelection(keys[i]),true);
    			Event event = new Event();
    			event.time = e.time;
    			event.stateMask = e.stateMask;
    			event.doit = e.doit;
    			combo.getCCombo().notifyListeners(SWT.Selection, event);
    			e.doit = event.doit;

       	  		break;
           	}
       }
       
	}

}
