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
import com.mmxlabs.models.lng.scenario.importWizards.canalcosts.ImportSuezRebateWizard;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class SuezRebateUpdateCommandHandler extends AbstractHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuezRebateUpdateCommandHandler.class);

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

		BiConsumer<CompoundCommand, IScenarioDataProvider> rebateUpdater = null;

		{
			try (InputStream inputStream1 = ScenarioBuilder.class.getResourceAsStream("/suez-rebates.json")) {
				final String rebatesJSON = CharStreams.toString(new InputStreamReader(inputStream1, StandardCharsets.UTF_8));
				try (InputStream inputStream2 = ScenarioBuilder.class.getResourceAsStream("/suez-rebate-portgroups.json")) {
					final String portGroupsJSON = CharStreams.toString(new InputStreamReader(inputStream2, StandardCharsets.UTF_8));
					rebateUpdater = SharedScenarioDataUtils.UpdateJob.createSuezTariffRebateUpdater(portGroupsJSON, rebatesJSON);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (rebateUpdater == null) {
			return null;
		}

		final ImportSuezRebateWizard importRebateWizard = new ImportSuezRebateWizard(scenarioInstance, "Suez tariff rebate ", rebateUpdater);
		importRebateWizard.init(activeWorkbenchWindow.getWorkbench(), null);

		final WizardDialog dialog = new WizardDialog(shell, importRebateWizard);
		dialog.create();
		dialog.open();

		return null;
	}
}
