package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.IPostExportProcessor;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

/**
 * A helper class to export a disconnected Schedule into a new forked scenario.
 * 
 * @author sg
 *
 */
public class ExportScheduleHelper {

	private static final Logger LOG = LoggerFactory.getLogger(ExportScheduleHelper.class);

	public static void export(final ScenarioResult scenarioResult) throws IOException {
		// Original data
		final LNGScenarioModel o_scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		final ScheduleModel o_scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);
		final ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();

		@Nullable
		final String newForkName = ScenarioServiceModelUtils.getNewForkName(scenarioInstance, false);
		if (newForkName == null) {
			return;
		}

		// Clone data
		final EcoreUtil.Copier copier = new Copier();
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

		copier.copyReferences();

		final ScheduleModel source_scheduleModel = (ScheduleModel) copier.get(o_scheduleModel);

		@NonNull
		final EditingDomain editingDomain = LNGSchedulerJobUtils.createLocalEditingDomain();
		// Contain new scenario in resource for Delete commands to work correctly.
		final ResourceImpl r = new ResourceImpl();
		r.getContents().add(scenarioModel);

		editingDomain.getResourceSet().getResources().add(r);

		final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
		final CargoModel cargoModel = scenarioModel.getCargoModel();

		final CompoundCommand command = new CompoundCommand();

		final Schedule schedule = source_scheduleModel.getSchedule();
		// Uncontain these slots so the call to #derive will re-parent them
		for (final SlotAllocation a : schedule.getSlotAllocations()) {
			final Slot slot = a.getSlot();
			if (slot != null && slot.eContainer() != cargoModel) {
				EReference ref = slot.eContainmentFeature();
				if (ref.isMany()) {
					List<EObject> l = (List<EObject>) slot.eContainer().eGet(ref);
					l.remove(slot);
				} else {
					slot.eContainer().eUnset(ref);
				}
			}
		}

		// Clear any insertion plans - assume no longer relevant
		AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
		// analyticsModel.getActionPlans().clear();
		// analyticsModel.getInsertionPlans().clear();

		// scheduleModel.setSchedule(schedule);
		command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.eINSTANCE.getScheduleModel_Schedule(), schedule));

		// TODO: Hook in the PostExportProcessors.

		// // new LNGExportTransformer(eveal/optimisationTransofrmer, hints);
		// final Injector childInjector = injector.createChildInjector(new PostExportProcessorModule());
		//
		// final Key<List<IPostExportProcessor>> key = Key.get(new TypeLiteral<List<IPostExportProcessor>>() {
		// });

		final Iterable<IPostExportProcessor> postExportProcessors = null;
		// try {
		// postExportProcessors = childInjector.getInstance(key);
		// //
		// } catch (final ConfigurationException e) {
		// postExportProcessors = null;
		// }

		command.append(LNGSchedulerJobUtils.derive(editingDomain, scenarioModel, schedule, cargoModel, postExportProcessors));

		// Mark schedule as clean
		command.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));

		command.execute();

		final IScenarioService scenarioService = scenarioInstance.getScenarioService();
		final ScenarioInstance fork = scenarioService.insert(scenarioInstance, scenarioModel);
		fork.setName(newForkName);

		// Copy across various bits of information
		fork.getMetadata().setContentType(scenarioInstance.getMetadata().getContentType());
		fork.getMetadata().setCreated(new Date());
		fork.getMetadata().setLastModified(new Date());

		// Copy version context information
		fork.setVersionContext(scenarioInstance.getVersionContext());
		fork.setScenarioVersion(scenarioInstance.getScenarioVersion());

		fork.setClientVersionContext(scenarioInstance.getClientVersionContext());
		fork.setClientScenarioVersion(scenarioInstance.getClientScenarioVersion());

		try {
			OpenScenarioUtils.openScenarioInstance(fork);
		} catch (final PartInitException e) {
			LOG.error(e.getMessage(), e);
		}

	}
}
