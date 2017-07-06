package com.mmxlabs.models.lng.transformer.longterm;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.OptimisationJobRunner;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;

public class LongTermOptimiserMenuExtension implements ITradesTableContextMenuExtension {

	private static final Logger log = LoggerFactory.getLogger(LongTermOptimiserMenuExtension.class);

	public LongTermOptimiserMenuExtension() {
	}

	@Override
	public void contributeToMenu(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Slot slot, @NonNull final MenuManager menuManager) {
		if (!LicenseFeatures.isPermitted("features:long-term-optimiser")) {
			return;
		}

		if (slot.getCargo() == null) {
			final LongTermAction action = new LongTermAction(scenarioEditingLocation);
			menuManager.add(action);
			return;
		}
	}

	private static class LongTermAction extends Action {

		private final IScenarioEditingLocation scenarioEditingLocation;

		public LongTermAction(final IScenarioEditingLocation scenarioEditingLocation) {
			super("Long term optimisation");
			this.scenarioEditingLocation = scenarioEditingLocation;
		}

		@Override
		public void run() {

			final OptimisationJobRunner jobRunner = new OptimisationJobRunner();

			final ScenarioInstance instance = scenarioEditingLocation.getScenarioInstance();

			final @NonNull UserSettings userSettings = ScenarioUtils.createDefaultUserSettings();

			final Supplier<IJobDescriptor> createJobDescriptorCallback = () -> new LNGLongTermJobDescriptor(instance.getName(), instance, userSettings);

			final ModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
			jobRunner.run("Long term optimiser", instance, modelRecord, null, createJobDescriptorCallback, null);
		}
	}

	@Override
	public void contributeToMenu(@NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull IStructuredSelection selection, @NonNull MenuManager menuManager) {

	}

}
