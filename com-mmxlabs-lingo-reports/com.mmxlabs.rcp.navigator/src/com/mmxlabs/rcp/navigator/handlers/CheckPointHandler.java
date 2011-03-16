package com.mmxlabs.rcp.navigator.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CheckPointHandler extends AbstractHandler {
	
	/**
	 * The constructor.
	 */
	public CheckPointHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		
		// Copy scenario into new workspace resource.
		
		// --> e.g. find root name Add on timestamp.
		
		// --> Refresh parent folder
	
		// Update button state?
		
		return null;
	}
}
