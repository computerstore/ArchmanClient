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
package it.archiworld.archman.client.crm;

import it.archiworld.common.Member;

import org.eclipse.jface.resource.ImageDescriptor;

public class MemberEditorInput extends CRMEditorInput {

	private Member member;

	public MemberEditorInput(Member member) {
		super(member);
		this.member = (Member) getBean();
	}

	public String getName() {
		if (member.getFirstname() == null || member.getLastname() == null)
			return Messages.MemberEditorInput_EmptyMember_Name;
		return member.toString();
	}

	public String getToolTipText() {
		String text = new String();
		if (member.getTitle() != null)
			text += member.getTitle();
		if (member.getFirstname() != null)
			text += member.getFirstname();
		if (member.getLastname() != null)
			text += member.getLastname();
		if (member.getCity() != null)
			text += " (" + member.getCity() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		return getName();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.createFromFile(this.getClass(),
				"/icons/vcard.png"); //$NON-NLS-1$
	}

	@Override
	@SuppressWarnings({"unchecked"}) //$NON-NLS-1$
	public Object getAdapter(Class adapter) {
		if (adapter == Member.class || adapter == Object.class) {
			return getBean();
		}
		System.out
				.println("[WARN] MemberEditorInput.getAdaper(Class): Adapter for not implemented class " //$NON-NLS-1$
						+ adapter.toString() + " requested!"); //$NON-NLS-1$
		return null;
	}

}
