package org.tekkotsu.stateMachine.parts;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.tekkotsu.api.DefaultClassReader;
import org.tekkotsu.api.NodeClass;
import org.tekkotsu.api.TransitionClass;

public class LinksPart {
	@PostConstruct
	public void createUserInterface(Composite parent) throws FileNotFoundException {
		parent.setLayout(new GridLayout(1, false));
		
		
		//Create lables for nodeclasses from xml file
		//Get lists of default classes
		ArrayList<TransitionClass> nodes = new DefaultClassReader().getTransitions();
		
		//Make arraylist for labels
		ArrayList<Label> labels = new ArrayList<Label>();
		
		// Create integer to represent copy operation
		int operations = DND.DROP_COPY;
		
		// Create array of type transfer with texttransfer instance
		Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
		
		//go trough the nodeclasses and create a label with name for each.
		for(int i = 0; i < nodes.size(); i++){
			
			final Label nodeLabel = new Label(parent, SWT.BORDER);
			nodeLabel.setText(nodes.get(i).getName());
			labels.add(nodeLabel);
			
			//Create a drag source with the label created and the operations.
			DragSource source = new DragSource(nodeLabel, operations);
			
			//Set the types array to the source.
			source.setTransfer(types);
			
	
			//add Drag listener to the dragsource
			source.addDragListener(new DragSourceListener() {
				
				//start the drag (can give conditions in which we don't want to start.)
				public void dragStart(DragSourceEvent event) {
				}
				
				//set the data that will be dragged. (must suit the transfer type.)
				public void dragSetData(DragSourceEvent event) {
				   
					// Provide the data of the requested type.
					if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
						event.data = nodeLabel.getText();
					}
				}
				
				public void dragFinished(DragSourceEvent event) {
					
					// If a move operation has been performed, remove the data
					// from the source
					if (event.detail == DND.DROP_MOVE)
						nodeLabel.setText("");
			     	}
				
			   }); //end draglistener adding method.
			
		}
		

	} 

}
