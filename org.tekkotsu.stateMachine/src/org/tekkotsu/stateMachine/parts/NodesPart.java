package org.tekkotsu.stateMachine.parts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.geom.RectangularShape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.*;

import javax.annotation.PostConstruct;
import org.tekkotsu.api.*;

import org.eclipse.draw2d.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.Triangle;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.tekkotsu.api.DefaultClassReader;




public class NodesPart  {
	@PostConstruct
	public void createUserInterface(Composite parent) throws IOException {
			
			parent.setLayout(new GridLayout(2, false));
		
			
			//Create lables for nodeclasses from xml file
			//Get lists of default classes
			ArrayList<NodeClass> nodes = new DefaultClassReader().getNodes();
			
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

