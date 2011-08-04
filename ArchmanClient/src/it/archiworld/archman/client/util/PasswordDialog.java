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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PasswordDialog extends Dialog {
  private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;

  private Text passwordField;
  private PasswordHandler passhandler;

  public PasswordDialog(Shell parentShell) {
	  super(parentShell);
  }

  protected Control createDialogArea(Composite parent) {
    Composite comp = (Composite) super.createDialogArea(parent);

    GridLayout layout = (GridLayout) comp.getLayout();
    layout.numColumns = 1;

    GridData data = new GridData(GridData.FILL_HORIZONTAL);

    Label passwordLabel = new Label(comp, SWT.RIGHT);
    passwordLabel.setText(Messages.getString("PasswordDialog.LabelPassword")); //$NON-NLS-1$

    passwordField = new Text(comp, SWT.SINGLE | SWT.PASSWORD);
    data = new GridData(GridData.FILL_HORIZONTAL);
    passwordField.setLayoutData(data);

    return comp;
  }

  protected void createButtonsForButtonBar(Composite parent) {
    super.createButtonsForButtonBar(parent);
    createButton(parent, RESET_ID, Messages.getString("PasswordDialog.ButtonLabelResetAll"), false); //$NON-NLS-1$
  }

  protected void buttonPressed(int buttonId) {
    if (buttonId == RESET_ID) {
      passwordField.setText(""); //$NON-NLS-1$
    } else {
      passhandler.setPassword(passwordField.getText());
      super.buttonPressed(buttonId);
    }
  }

  public PasswordHandler getPasshandler() {
	  return passhandler;
  }

  public void setPasshandler(PasswordHandler passhandler) {
	  this.passhandler = passhandler;
  }
  
}
