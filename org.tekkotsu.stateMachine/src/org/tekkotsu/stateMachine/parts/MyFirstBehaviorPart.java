package org.tekkotsu.stateMachine.parts;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.draw2d.GridData;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
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
		
//////Setup data structures///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
			
			//Hashmap for table date
			final HashMap<TableItem, List<List<NodeInstance>>> tableMap = new HashMap<TableItem, List<List<NodeInstance>>>();
		
			//Hashmaps to store the nodeclasses and names.
			final HashMap<String, NodeClass> nodesMap = new HashMap<String, NodeClass>();
			final HashMap<String, TransitionClass> transMap = new HashMap<String, TransitionClass>();
			
			//ArrayLists to read the default nodes and transitions from xml to.
			final ArrayList<NodeClass> nodesList = new DefaultClassReader().getNodes();
			final ArrayList<TransitionClass> transList = new DefaultClassReader().getTransitions();
			
			//Create the setup machine for the behavior
			final SetupMachine setup = new SetupMachine();
			
///Create the UI Elements ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
			// Create tables
			final Table dropTable = new Table(parent, SWT.BORDER | SWT.MULTI);
			final Table sourceTable = new Table(parent, SWT.BORDER);
			final Table targetTable = new Table(parent, SWT.BORDER);
				
			//Create variable to store selected item
			final TableItem[] selected = new TableItem[1];
			
			//Create button to generate to code.
			Button fsm = new Button(parent, SWT.BUTTON1);
			fsm.setText("Get FSM Code!");
			
			//Create the text area for the code.
			Text code = new Text(parent, SWT.NONE);
			code.setText("Code will be placed here.");
			

			
///Add default values to data structures  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
			//Adding the names and the objects to the nodesMap.
			for(int i = 0; i < nodesList.size(); i++){
				
				//Current node to operate with.
				NodeClass currentNode = nodesList.get(i);
				
				System.out.println(currentNode.getName() + " added to map.");
				
				nodesMap.put(currentNode.getName(), currentNode);
			}
		
			
			//Adding the names and the objects to the transMap
			for(int i = 0; i < transList.size(); i++){
				
				//Current node to operate with.
				TransitionClass currentTrans = transList.get(i);
				
				transMap.put(currentTrans.getName(), currentTrans);
				
			}
			
///DROPS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			 
			
			// Operations declared.
			final int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
			
			//TextTransfer object
			final TextTransfer textTransfer = TextTransfer.getInstance();
			
			//Transfer types array to be used
			final Transfer[] types = new Transfer[] {textTransfer};
			
			//Declare object.
			final DropTarget sourceTarget = new DropTarget(sourceTable, operations);
			 
			//Set transfer types
			sourceTarget.setTransfer(types);
			
			//Declare object.
			final DropTarget target = new DropTarget(dropTable, operations);
			 
			//Set transfer types
			target.setTransfer(types);
			
			//Declare object.
			final DropTarget targetTarget = new DropTarget(targetTable, operations);
			 
			//Set transfer types
			targetTarget.setTransfer(types);
			 
			
			
//DROPTARGET: Transitions Table /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			

			 
			//Add the drop listener
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
			        	
			        	//create instance from it.
			        	TransitionInstance dragged = new TransitionInstance(transMap.get(textIn));
			        	
			        	//Add instance to the setup machine
			        	//setup.addTransition(dragged);
			        	
			        	//Set the item text
			            TableItem item = new TableItem(dropTable, SWT.NONE);
			            
			        	//Create list for sources and target lists.
			        	List<List<NodeInstance>> sourceTarget = new ArrayList<List<NodeInstance>>(2);
			        	
			        	//Create source and target lists themselves.
			        	sourceTarget.add(0, new ArrayList<NodeInstance>());
			        	sourceTarget.add(1, new ArrayList<NodeInstance>());
			        	
			        	//Add instance to the tableMap
			        	tableMap.put(item, sourceTarget);
			        	
			        	//Write output text and store in variable.
			        	textOut = dragged.getType().getName().toLowerCase();
			        	
			        	//Set text to the table item
			        	item.setText(textOut);
			            
  
			        }
			        
			    }

	}); 
			

			
		
///Selection Listener for the  selected transition  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			
			//Selection listener for the transitions table
			dropTable.addListener(SWT.DefaultSelection, new Listener(){
			
				//Event handler
				public void handleEvent(Event e){
					
					//Get the selected item (Only one, and we pick the first one)
					selected[0] = dropTable.getSelection()[0];
					
					
							
					
					
					
					
////////////////////////////CLEAN THE SOURCES AND SHOW NEW ONES///////////////////////////
					
					//Remove all the items.
					sourceTable.removeAll();
					
					//add the items from sources of the selected one.
					for(int i = 0; i < tableMap.get(selected[0]).get(0).size(); i++){
						
						TableItem sourceItem = new TableItem(sourceTable, SWT.NONE);
						sourceItem.setText(tableMap.get(selected[0]).get(0).get(i).getLabel());
						
					}
					
/////////////////////CLEAN THE TARGETS AND SHOW NEW ONES///////////////////////////
					
					//Remove all the items.
					targetTable.removeAll();
					
					//add the items from sources of the selected one.
					for(int i = 0; i < tableMap.get(selected[0]).get(1).size(); i++){
						
						TableItem sourceItem = new TableItem(targetTable, SWT.NONE);
						sourceItem.setText(tableMap.get(selected[0]).get(1).get(i).getLabel());
						
					}					
					
				}
				
			});
			
////////////////////////END SELECTED ITEM LISTENER////////////////////////////////////////////
			
			

			
			
			
			//DROPTARGET: Sources Table /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
			
			

			 
			//Add the drop listener
			sourceTarget.addDropListener(new DropTargetListener() {
			  
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
			        	
			        	System.out.println(textIn);
			        	
			        	//Create string to hold output text.
			        	String textOut = "";
			        	
			        	//create instance from it.
			        	NodeInstance dragged = new NodeInstance(nodesMap.get(textIn));
			        	
			        	//Add to setup machine
			        	//setup.addNode(dragged);
			        	
			        	
			        	//Add instance to the tableMap
			        	List<List<NodeInstance>> list2 = tableMap.get(selected[0]);
			        	List<NodeInstance> list3 = list2.get(0);
			        	list3.add(dragged);
			        	
			        	//tableMap.get(selected[0]).get(0).add(dragged);
			        	
			        	//Write output text and store in variable.
			        	textOut = dragged.getType().getName().toLowerCase();

			            //Set the label text
			            TableItem item = new TableItem(sourceTable, SWT.NONE);
			            item.setText(textOut);
  
			        }
			        
			    }

	}); 			
			
			
////////////////////////////////////////////////END SOURCES TABLE DROP///////////////					
			
			
			
//DROPTARGET: Targets Table /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////								
			
			
			
			//Add the drop listener
			targetTarget.addDropListener(new DropTargetListener() {
			  
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
			        	
			        	System.out.println(textIn);
			        	
			        	//Create string to hold output text.
			        	String textOut = "";
			        	
			        	//create instance from it.
			        	NodeInstance dragged = new NodeInstance(nodesMap.get(textIn));
			        	
			        	//Add instance to the tableMap
			        	
			        	if(selected[0]==null){
			        		System.out.println("selected is null");
			        	}
			        	
			        	List<List<NodeInstance>> list2 = tableMap.get(selected[0]);
			        	List<NodeInstance> list3 = list2.get(1);
			        	list3.add(dragged);
			        	
			        	//tableMap.get(selected[0]).get(0).add(dragged);
			        	
			        	//Write output text and store in variable.
			        	textOut = dragged.getType().getName().toLowerCase();

			            //Set the label text
			            TableItem item = new TableItem(targetTable, SWT.NONE);
			            item.setText(textOut);
  
			        }
			        
			    }

	}); 								
			
			
////////////////////////////////////////////////END TARGETS TABLE DROP///////////////				
			
			
			
			
			
			
////////////////////////////////BUTTON LISTENER/////////////////////////////////////////////////////////////
			/*
			fsm.addListener(SWT.Selection, new Listener(){
				
				public void handleEvent(Event event){
					
					tableMap.
					
				}
				
				
			});*/

	}
}
