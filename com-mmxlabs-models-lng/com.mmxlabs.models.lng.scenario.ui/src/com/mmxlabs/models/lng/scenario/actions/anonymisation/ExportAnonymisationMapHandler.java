package com.mmxlabs.models.lng.scenario.actions.anonymisation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ExportAnonymisationMapHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}
		final Shell shell = HandlerUtil.getActiveShell(event);
		final String exportFileName = AnonymisationMapIO.getFile(shell);
		if (exportFileName.isEmpty()) {
			return null;
		}
		String message;
		List<AnonymisationRecord> records = new ArrayList();
		try {
			records.addAll(AnonymisationMapIO.read(AnonymisationMapIO.anonyMapFile));
		} catch (Exception e) {
			message = "Can't get the anonymisation map contents:\n" + e.getMessage();
		}
		
		if (records.isEmpty()) {
			message = "Anonymisation map is empty";
		} else {
			message = AnonymisationMapIO.writeCSV(records, exportFileName);
		}
		MessageDialog dialog = new MessageDialog(shell, "Operation complete", null, 
				message, 0, 0, "OK");
		dialog.create();
		dialog.open();
		return null;
	}	
}
