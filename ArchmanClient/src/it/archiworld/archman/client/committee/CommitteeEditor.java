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
package it.archiworld.archman.client.committee;

import it.archiworld.archman.client.core.ScrolledFormEditorBase;
import it.archiworld.common.committee.Committee;
import it.archiworld.common.committee.Committeemember;

import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class CommitteeEditor extends ScrolledFormEditorBase {

	public final static String ID = "it.archiworld.archman.client.committee.committeeeditor"; //$NON-NLS-1$

	private Committee committee;
	private CommitteeMemberTable committeeMemberTable;

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			if (!this.authenticate()) {
				return;
			}
			CommitteeEditorInput input = (CommitteeEditorInput) getEditorInput();
			committee=(Committee)input.createOrUpdate();
			committeeMemberTable
					.setCommitteeMemberList(((Committee) committee)
							.getMember());
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.CommitteeEditor_SaveSuccessDialog_Heading,
					Messages.CommitteeEditor_SaveSuccessDialog_Text);
			setPartName(input.getName());
			setDirty(false);
		} catch (Throwable t) {
			MessageDialog.openInformation(this.getSite().getShell(),
					Messages.CommitteeEditor_SaveFailureDialog_Heading,
					Messages.CommitteeEditor_SaveFailureDialog_Text);
			t.printStackTrace();
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) {
		this.committee = (Committee) ((CommitteeEditorInput) input).getBean();
		setSite(site);
		setInput(input);
	}

	@Override
	protected void createFormHeader(ScrolledForm form, Composite header) {
		form.setText(Messages.CommitteeEditor_Form_Heading);
		header.setLayout(new GridLayout(1, true));

		Label denominationLabel = toolkit.createLabel(header,
				Messages.CommitteeEditor_Label_Denomination);
		GridData denominationLabelGd = new GridData(SWT.BEGINNING, SWT.END,
				false, false);
		denominationLabel.setLayoutData(denominationLabelGd);
		Text denominationText = toolkit.createText(header, null, SWT.BORDER);
		GridData denominationTextGd = new GridData(SWT.FILL, SWT.BEGINNING,
				true, false);
		denominationText.setLayoutData(denominationTextGd);
		dataBindingContext.bindValue(
				SWTObservables.observeText(denominationText, SWT.Modify),
				PojoObservables.observeValue(committee, "name"), null, null) //$NON-NLS-1$
				.getTarget().addChangeListener(dirtyListener);

	}

	@Override
	protected void createFormBody(Composite body) {
		body.setLayout(new GridLayout(1, true));
		createTabItem(Messages.CommitteeEditor_TabLabelGeneral, new GridLayout(2, true));
		createSection(Messages.CommitteeEditor_Section_Heading_Address,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED);

		createLabeledText(Messages.CommitteeEditor_Label_Street, committee,
				"street"); //$NON-NLS-1$
		createLabeledText(Messages.CommitteeEditor_Label_Zip, committee, "zip"); //$NON-NLS-1$
		createLabeledText(Messages.CommitteeEditor_Label_City, committee,
				"city"); //$NON-NLS-1$
		createLabeledText(Messages.CommitteeEditor_Label_State, committee,
				"state"); //$NON-NLS-1$
		createLabeledText(Messages.CommitteeEditor_Label_Country, committee,
				"country"); //$NON-NLS-1$
		createSection(Messages.CommitteeEditor_Section_Heading_Note,
				Section.TWISTIE | Section.TITLE_BAR	| Section.EXPANDED, 1, 1, true, true);
		createLabeledMultiText(Messages.CommitteeEditor_Label_Note, committee,
				"note"); //$NON-NLS-1$

		createTabItem(Messages.CommitteeEditor_TabLabelMembers, new GridLayout(1, true));
		committeeMemberTable = new CommitteeMemberTable(this, activePart,
				SWT.NONE, (Committee) committee);

	}

	@Override
	protected void createFormFooter(Composite footer) {
		footer.setLayout(new FillLayout());
	}

	public List<Committeemember> getMembers() {
		return ((Committee) this.committee).getMember();
	}

	public Committee getCommittee(){
		return committee;
	}
}
