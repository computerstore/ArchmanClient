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

import it.archiworld.common.Address;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class AddressTransfer extends ByteArrayTransfer{

   private static AddressTransfer instance = new AddressTransfer();
   private static final String TYPE_NAME = "address-transfer-format"; //$NON-NLS-1$
   private static final int TYPEID = registerType(TYPE_NAME);

   /**
    * Returns the singleton gadget transfer instance.
    */
   public static AddressTransfer getInstance() {
      return instance;
   }
   /**
    * Avoid explicit instantiation
    */
   private AddressTransfer() {
   }

   @Override
   public TransferData[] getSupportedTypes() {
	   return super.getSupportedTypes();
   }

   @Override
   public boolean isSupportedType(TransferData transferData) {
	   return super.isSupportedType(transferData);
   }

   @Override
   protected void javaToNative(Object object, TransferData transferData) {
		if (object == null || !(object instanceof Address[])) return;
	 	if (isSupportedType(transferData)) {
	 		Address[] myTypes = (Address[]) object;	
	 		try {
	 			// write data to a byte array and then ask super to convert to pMedium
	 			ByteArrayOutputStream out = new ByteArrayOutputStream();
	 			ObjectOutputStream writeOut = new ObjectOutputStream(out);
	 			writeOut.writeInt(myTypes.length);
	 			for (int i = 0, length = myTypes.length; i < length;  i++){
	 	 	       writeOut.writeObject(myTypes[i]);
	 			}
	 			byte[] buffer = out.toByteArray();
	 			writeOut.close();
	 
	 			super.javaToNative(buffer, transferData);
	 			
	 		} catch (IOException e) {
	 		}
	 	}
	}


   @Override
   protected Object nativeToJava(TransferData transferData) {
		if (isSupportedType(transferData)) {
	 		byte[] buffer = (byte[])super.nativeToJava(transferData);
	 		if (buffer == null) return null;
	 		List<Address> myData = new ArrayList<Address>();
	 		try {
	 			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		    	   ObjectInputStream readIn = new ObjectInputStream(in);
		    	   int size=readIn.readInt();
		    	   for(int i=0;i<size;i++)
		    		   myData.add((Address)readIn.readObject());
		    	   	 			
	 			readIn.close();
	 		} catch (IOException ex) {
	 			ex.printStackTrace();
	 			return null;
	 		}
	 		catch (ClassNotFoundException ex) {
	 			ex.printStackTrace();
	 			return null;
	 		}
	 		Address[] addr = new Address[myData.size()];
	 		myData.toArray(addr);
	 		return addr;
	 	}
	 	return null;
   }
   
   @Override
   protected int[] getTypeIds() {
	   return new int[] {TYPEID};
   }

   @Override
   protected String[] getTypeNames() {
	   return new String[] {TYPE_NAME};
   }

   @Override
   protected boolean validate(Object object) {
	   if ((object == null) || !(object instanceof Address[]))
		   return false;
	   return true;
   }

}
