package org.tekkotsu.stateMachine.parts;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.tekkotsu.api.DefaultClassReader;
import org.tekkotsu.api.NodeClass;
import org.tekkotsu.api.NodeInstance;
import org.tekkotsu.api.SetupMachine;
import org.tekkotsu.api.TransitionClass;
import org.tekkotsu.api.TransitionInstance;

public class MyFirstBehaviorPart {
	@PostConstruct
	public void createUserInterface(Composite parent) throws FileNotFoundException {
		
			// Enable a table as a Drop Target
			final Table dropTable = new Table(parent, SWT.BORDER);
			
			//Create button to generate to code.
			Button fsm = new Button(parent, SWT.BUTTON1);
			fsm.setText("Get FSM Code!");
			fsm.setSize(200, 100);
			
			//Create the text area for the code.
			Text code = new Text(parent, SWT.NONE);
			code.setText("Code will be placed here.");
			
			//Create setupmachine for the behavior.
			final SetupMachine setup = new SetupMachine();
			
			//Hashmaps to store the nodeclasses and names.
			final HashMap<String, NodeClass> nodesMap = new HashMap<String, NodeClass>();
			final HashMap<String, TransitionClass> transMap = new HashMap<String, TransitionClass>();
			
			//ArrayLists to read the default nodes and transitions from xml to.
			final ArrayList<NodeClass> nodesList = new DefaultClassReader().getNodes();
			final ArrayList<TransitionClass> transList = new DefaultClassReader().getTransitions();
			
			//Adding the names and the objects to the nodesMap.
			for(int i = 0; i < nodesList.size(); i++){
				
				//Current node to operate with.
				NodeClass currentNode = nodesList.get(i);
				
				nodesMap.put(currentNode.getName(), currentNode);
			}
		
			
			//Adding the names and the objects to the transMap
			for(int i = 0; i < transList.size(); i++){
				
				//Current node to operate with.
				TransitionClass currentTrans = transList.get(i);
				
				transMap.put(currentTrans.getName(), currentTrans);
				
			}
			
			 
			// Allow data to be copied or moved to the drop target
			int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
			DropTarget target = new DropTarget(dropTable, operations);
			 
			// Receive data in Text or File format
			final TextTransfer textTransfer = TextTransfer.getInstance();
			Transfer[] types = new Transfer[] {textTransfer};
			target.setTransfer(types);
			 
			target.addDropListener(new DropTargetListener() {
			  public void dragEnter(DropTargetEvent event) {
			     if (event.detail == DND.DROP_DEFAULT) {
			         if ((event.operations & DND.DROP_COPY) != 0) {
			             event.detail = DND.DROP_COPY;
			         } else {
			             event.detail = DND.DROP_NONE;
			         }
			     }
			     

			   }
			   public void dragOver(DropTargetEvent event) {
			        event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
			        if (textTransfer.isSupportedType(event.currentDataType)) {
			            // NOTE: on unsupported platforms this will return null
			            Object o = textTransfer.nativeToJava(event.currentDataType);
			            String t = (String)o;
			            if (t != null) System.out.println(t);
			        }
			    }
			    public void dragOperationChanged(DropTargetEvent event) {
			        if (event.detail == DND.DROP_DEFAULT) {
			            if ((event.operations & DND.DROP_COPY) != 0) {
			                event.detail = DND.DROP_COPY;
			            } else {
			                event.detail = DND.DROP_NONE;
			            }
			        }
			        
			    }
			    public void dragLeave(DropTargetEvent event) {
			    }
			    public void dropAccept(DropTargetEvent event) {
			    }
			    
			    
			    //TODO This is where the drop happens.
			    public void drop(DropTargetEvent event) {
			        if (textTransfer.isSupportedType(event.currentDataType)) {
			            
			        	//Text transfered from drag source.
			        	String textIn = (String)event.data;
			        	
			        	//Create string to hold output text.
			        	String textOut = "";
			        	
			        	//Check what is dragged.
			        	//If its a node.
			        	if(nodesMap.containsKey(textIn)){
			        		
			        		//create instance from it.
			        		NodeInstance dragged = new NodeInstance(nodesMap.get(textIn));
			        		
			        		//Write output text and store in variable.
			        		textOut = dragged.getLabel() + "  color: " + dragged.getColor();
			        	}
			        	else if(transMap.containsKey(textIn)){
			        		
			        		//create instance from it.
			        		TransitionInstance dragged = new TransitionInstance(transMap.get(textIn));
			        		
			        		//Write output text and store in variable.
			        		textOut = dragged.getType().getName().toLowerCase() + " color: " + dragged.getColor();
			        		
			        	}
			        	
			        	
			            //Actual drop event-Sets text here
			            TableItem item = new TableItem(dropTable, SWT.NONE);
			            item.setText(textOut);
			            
			      
			            
			            
			        }
			        
			    }

	}); 
	

	}
}
