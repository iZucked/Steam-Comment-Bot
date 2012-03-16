/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.jobmanager.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ScheduleFitnessExportHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		final ISelection selection = window.getSelectionService().getSelection();

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection items = (IStructuredSelection) selection;

			final FileDialog saveDialog = new FileDialog(window.getShell(), SWT.SAVE);

			final String[][] filter = new String[][] { new String[] { "CSV Files" }, new String[] { "*.csv" } };

			saveDialog.setFileName("fitness_trace.csv");
			saveDialog.setFilterNames(filter[0]);
			saveDialog.setFilterExtensions(filter[1]);

			final String fileName = saveDialog.open();

			if (fileName == null) {
				return null;
			}

//			try {
//				final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//
//				final Set<String> componentNames = new TreeSet<String>();
//
//				boolean doneHeaders = false;
//
//				for (final Object object : items.toList()) {
//					if (object instanceof Schedule) {
//						final Schedule schedule = (Schedule) object;
//						// export components to file
//
//						final EList<ScheduleFitness> components = schedule.getFitness();
//						if (!doneHeaders) {
//							for (final ScheduleFitness f : components) {
//								componentNames.add(f.getName());
//							}
//
//							for (final String cn : componentNames) {
//								if (doneHeaders) {
//									writer.write(", ");
//								}
//								writer.write(cn);
//								doneHeaders = true;
//							}
//
//							writer.write("\n");
//						}
//
//						boolean line = false;
//
//						for (final String cn : componentNames) {
//							for (final ScheduleFitness f : components) {
//								if (f.getName().equals(cn)) {
//									if (line) {
//										writer.write(", ");
//									}
//									line = true;
//									writer.write(Long.toString(f.getValue()));
//								}
//							}
//						}
//
//						writer.write("\n");
//					}
//				}
//
//				writer.close();
//			} catch (final IOException e) {
//				throw new ExecutionException(fileName + " : " + e.getMessage());
//			}

		}
		return null;
	}
}
