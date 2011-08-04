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
package it.archiworld.archman.client.protocol.util;

import it.archiworld.common.Member;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

public class MemberTransfer extends ByteArrayTransfer {

	private static MemberTransfer instance = new MemberTransfer();
	private static final String TYPE_NAME = "member-transfer-format"; //$NON-NLS-1$
	private static final int TYPEID = registerType(TYPE_NAME);

	/**
	 * Returns the singleton gadget transfer instance.
	 */
	public static MemberTransfer getInstance() {
		return instance;
	}

	/**
	 * Avoid explicit instantiation
	 */
	private MemberTransfer() {
	}

	@Override
	public TransferData[] getSupportedTypes() {
		// TODO Auto-generated method stub
		return super.getSupportedTypes();
	}

	@Override
	public boolean isSupportedType(TransferData transferData) {
		return super.isSupportedType(transferData);
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if (!validate(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		Member member = (Member) object;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writeOut = new ObjectOutputStream(out);
			writeOut.writeObject(member);
			byte[] buffer = out.toByteArray();
			writeOut.close();
			super.javaToNative(buffer, transferData);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {
		Member member = null;
		if (isSupportedType(transferData)) {
			try {
				byte[] buffer = (byte[]) super.nativeToJava(transferData);
				if (buffer == null) {
					return null;
				}

				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				ObjectInputStream readIn = new ObjectInputStream(in);
				member = (Member) readIn.readObject();
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			return member;
		}
		return null;
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}

	@Override
	protected boolean validate(Object object) {
		if ((object == null) || !(object instanceof Member))
			return false;
		return true;
	}

}
