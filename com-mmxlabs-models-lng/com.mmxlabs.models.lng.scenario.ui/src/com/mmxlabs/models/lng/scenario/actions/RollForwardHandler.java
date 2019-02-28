package com.mmxlabs.models.lng.scenario.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class RollForwardHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {

		final IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		if (activeEditor == null) {
			return null;
		}
		final IEditorInput editorInput = activeEditor.getEditorInput();
		if (editorInput instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput ssEditorInput = (IScenarioServiceEditorInput) editorInput;
			final ScenarioInstance scenarioInstance = ssEditorInput.getScenarioInstance();

			final @NonNull ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("RollForwardHandler:1")) {

				final Shell shell = HandlerUtil.getActiveShell(event);
				// Check state
				{
					final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(sdp);
					final Schedule schedule = scheduleModel.getSchedule();
					if (schedule == null || scheduleModel.isDirty()) {
						MessageDialog.openError(shell, "Dirty Scenario", "Scenario is dirty - please evaluate before attempting to roll forward");
						return null;
					}
				}

				final RollForwardDialog dialog = new RollForwardDialog(shell, sdp);
				dialog.open();
			}
		}

		return null;
	}
}
