/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.canalcosts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils;
import com.mmxlabs.models.lng.scenario.importWizards.canalcosts.ImportCanalCostsWizard;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class CanalCostsUpdateCommandHandler extends AbstractHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CanalCostsUpdateCommandHandler.class);

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}
		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);
		
		final ScenarioInstance scenarioInstance = editor.getScenarioInstance();
		
		BiConsumer<CompoundCommand, IScenarioDataProvider> suezCostsUpdater = null;
		BiConsumer<CompoundCommand, IScenarioDataProvider> panamaCostsUpdater = null;
		
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/suez-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				suezCostsUpdater = SharedScenarioDataUtils.UpdateJob.createSuezTariffUpdater(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/panama-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				panamaCostsUpdater = SharedScenarioDataUtils.UpdateJob.createPanamaTariffUpdater(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (panamaCostsUpdater == null || suezCostsUpdater == null) {
			return null;
		}
		
		final ImportCanalCostsWizard importCanalCostsWizard = 
				new ImportCanalCostsWizard(scenarioInstance, "Canal Costs Import", panamaCostsUpdater, suezCostsUpdater);
		importCanalCostsWizard.init(activeWorkbenchWindow.getWorkbench(), null);
		
		final WizardDialog dialog = new WizardDialog(shell, importCanalCostsWizard);
		dialog.create();
		dialog.open();

		return null;
	}
}
