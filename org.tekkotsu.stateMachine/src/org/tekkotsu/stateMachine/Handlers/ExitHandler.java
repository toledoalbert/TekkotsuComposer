package org.tekkotsu.stateMachine.Handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

public class ExitHandler {
	//Closes WorkBench
	@Execute
	  public void execute(IWorkbench workbench) {
	    workbench.close();
	  }
}
