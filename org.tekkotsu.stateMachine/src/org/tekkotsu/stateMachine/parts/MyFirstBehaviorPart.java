package org.tekkotsu.stateMachine.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class MyFirstBehaviorPart {
	@PostConstruct
	public void createUserInterface(Composite parent) {
		
		// Enable a table as a Drop Target
			final Table dropTable = new Table(parent, SWT.BORDER);
			 
			    TableItem item = new TableItem(dropTable, SWT.NONE);
			    item.setText("Test");
			
			 
			// Allow data to be copied or moved to the drop target
			int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(dropTable, operations);
			 
			// Receive data in Text or File format
			final TextTransfer textTransfer = TextTransfer.getInstance();
			final FileTransfer fileTransfer = FileTransfer.getInstance();
			Transfer[] types = new Transfer[] {fileTransfer, textTransfer};
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
			     // will accept text but prefer to have files dropped
			     for (int i = 0; i < event.dataTypes.length; i++) {
			         if (fileTransfer.isSupportedType(event.dataTypes[i])){
			             event.currentDataType = event.dataTypes[i];
			             // files should only be copied
			             if (event.detail != DND.DROP_COPY) {
			                 event.detail = DND.DROP_NONE;
			             }
			             break;
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
			        // allow text to be moved but files should only be copied
			        if (fileTransfer.isSupportedType(event.currentDataType)){
			            if (event.detail != DND.DROP_COPY) {
			                event.detail = DND.DROP_NONE;
			            }
			        }
			    }
			    public void dragLeave(DropTargetEvent event) {
			    }
			    public void dropAccept(DropTargetEvent event) {
			    }
			    public void drop(DropTargetEvent event) {
			        if (textTransfer.isSupportedType(event.currentDataType)) {
			            String text = (String)event.data;
			           //Actual drop event-Sets text here
			            TableItem item = new TableItem(dropTable, SWT.NONE);
			            item.setText(text);
			        }
			        if (fileTransfer.isSupportedType(event.currentDataType)){
			            String[] files = (String[])event.data;
			            for (int i = 0; i < files.length; i++) {
			                TableItem item = new TableItem(dropTable, SWT.NONE);
			                item.setText(files[i]);
			            }
			        }
			    }

	}); 
	}
}
