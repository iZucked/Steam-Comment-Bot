/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.calendars.ui;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.scenario.importWizards.holidaycalendars.ImportHolidayCalendarsWizard;
import com.mmxlabs.models.lng.scenario.importWizards.pricingcalendars.ImportPricingCalendarsWizard;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class CalendarsUpdateCommandHandler extends AbstractHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarsUpdateCommandHandler.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		String filename = null;

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}
		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		// Holiday Calendars
		try {
			filename = exportResource("/Holiday Calendars.csv");
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		final ImportHolidayCalendarsWizard holCalWizard = new ImportHolidayCalendarsWizard(editor != null ? editor.getScenarioInstance() : null, "Import holiday calendars", filename);
		holCalWizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog holCalDialog = new WizardDialog(shell, holCalWizard);
		holCalDialog.create();
		holCalDialog.open();

		// Pricing Calendars
		try {
			filename = exportResource("/Pricing Calendars.csv");
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		final ImportPricingCalendarsWizard pricingCalWizard = new ImportPricingCalendarsWizard(editor != null ? editor.getScenarioInstance() : null, "Import pricing calendars", filename);
		pricingCalWizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog pricingCalDialog = new WizardDialog(shell, pricingCalWizard);
		pricingCalDialog.create();
		pricingCalDialog.open();

		return null;
	}

	private String exportResource(final String resourceName) throws Exception {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final String jarFolder = (workspaceLocation.toOSString() + IPath.SEPARATOR + "temp").replace('\\', '/');
		try (InputStream stream = CalendarsUpdateCommandHandler.class.getResourceAsStream(resourceName)) {
			if (stream == null) {
				throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}

			int readBytes;
			final byte[] buffer = new byte[4096];
			try (OutputStream resStreamOut = new FileOutputStream(jarFolder + resourceName)) {
				while ((readBytes = stream.read(buffer)) > 0) {
					resStreamOut.write(buffer, 0, readBytes);
				}
			}
		}

		return jarFolder + resourceName;
	}
}
