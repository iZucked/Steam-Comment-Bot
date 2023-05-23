package com.mmxlabs.lngdataserver.lng.importers.portgroups;

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

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils;
import com.mmxlabs.models.lng.scenario.importWizards.portgroups.ImportEastOfSuezPortGroupWizard;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class EastOfSuezPortGroupUpdateCommandHandler extends AbstractHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EastOfSuezPortGroupUpdateCommandHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}
		final Shell shell = HandlerUtil.getActiveShell(event);
		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);
		final ScenarioInstance scenarioInstance = editor.getScenarioInstance();

		final BiConsumer<CompoundCommand, IScenarioDataProvider> eastOfSuezPortGroupUpdater = SharedScenarioDataUtils.UpdateJob.createSuezPortGroupUpdater();
		if (eastOfSuezPortGroupUpdater == null) {
			return null;
		}
		final ImportEastOfSuezPortGroupWizard importEastOfSuezPortGroupWizard = new ImportEastOfSuezPortGroupWizard(scenarioInstance, "East of Suez ", eastOfSuezPortGroupUpdater);
		importEastOfSuezPortGroupWizard.init(activeWorkbenchWindow.getWorkbench(), null);
		final WizardDialog dialog = new WizardDialog(shell, importEastOfSuezPortGroupWizard);
		dialog.create();
		dialog.open();
		return null;
	}

}
