/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
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
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
		final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvider:1");
		
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/suez-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				final CompoundCommand command = new CompoundCommand();
				final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createSuezTariffUpdater(json);
				updater.accept(command, scenarioDataProvider);
				command.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		{
			try (InputStream inputStream = ScenarioBuilder.class.getResourceAsStream("/panama-tariff.json")) {
				final String json = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				final CompoundCommand command = new CompoundCommand();
				final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.UpdateJob.createPanamaTariffUpdater(json);
				updater.accept(command, scenarioDataProvider);
				command.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

		return null;
	}
}
