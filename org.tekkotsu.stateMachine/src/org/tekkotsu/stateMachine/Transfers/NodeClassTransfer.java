package org.tekkotsu.stateMachine.Transfers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import org.tekkotsu.api.NodeClass;

public static class NodeClassTransfer extends ByteArrayTransfer {

	private static final String MYTYPENAME = "MytypeTransfer";
	
	private static final int MYTYPEID = registerType(MYTYPENAME);
	
	private static NodeClassTransfer _instance = new NodeClassTransfer();
	
	public static NodeClassTransfer getInstance(){
		return _instance;
	}
	
	public void javaToNative(Object object, TransferData transferData) {
	      if (!checkMyType(object) || !isSupportedType(transferData)) {
	        DND.error(DND.ERROR_INVALID_DATA);
	      }
	      NodeClass[] myTypes = (NodeClass[]) object;
	      try {
	    	  
	        // write data to a byte array and then ask super to convert to
	        // pMedium
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        DataOutputStream writeOut = new DataOutputStream(out);
	        for (int i = 0, length = myTypes.length; i < length; i++) {
	        
	          byte[] buffer1 = myTypes[i].getName().getBytes();
	          writeOut.writeInt(buffer1.length);
	          writeOut.write(buffer1);
	          
	          byte[] buffer2 = myTypes[i].getDefinition().getBytes();
	          writeOut.writeInt(buffer2.length);
	          writeOut.write(buffer2);
	          
	          byte[] buffer = myTypes[i].getBytes();
	          writeOut.writeInt(buffer.length);
	          writeOut.write(buffer);
	          
	          writeOut.writeLong(myTypes[i].fileLength);
	          writeOut.writeLong(myTypes[i].lastModified);
	          
	          
	        }
	        byte[] buffer = out.toByteArray();
	        writeOut.close();
	        super.javaToNative(buffer, transferData);
	      } catch (IOException e) {
	     }
	
}
}
