/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ClonedScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
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

	public static @Nullable ScenarioInstance export(final ScenarioResult scenarioResult) throws Exception {
		return export(scenarioResult, null, true, null);
	}

	public static @Nullable ScenarioInstance export(final ScenarioResult scenarioResult, @Nullable final String nameSuggestion, final boolean openScenario,
			@Nullable final BiConsumer<LNGScenarioModel, Schedule> modelCustomiser) throws Exception {
		// Original data

		final LNGScenarioModel o_scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		final ScheduleModel o_scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);
		final ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();

		@Nullable
		final String newForkName = nameSuggestion != null ? nameSuggestion : ScenarioServiceModelUtils.getNewForkName(scenarioInstance, false);
		if (newForkName == null) {
			return null;
		}

		// Clone data
		final EcoreUtil.Copier copier = new Copier();
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) copier.copy(o_scenarioModel);

		copier.copyReferences();

		final ScheduleModel source_scheduleModel = (ScheduleModel) copier.get(o_scheduleModel);
		// Null mean original schedule model was not contained by the original scenario model
		assert source_scheduleModel != null;

		final ClonedScenarioDataProvider scenarioDataProvider = ClonedScenarioDataProvider.make(scenarioModel, scenarioResult.getScenarioDataProvider());

		@NonNull
		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);

		final Schedule schedule = source_scheduleModel.getSchedule();

		//
		for (final Sequence seq : schedule.getSequences()) {
			final CharterInMarket charterInMarket = seq.getCharterInMarket();
			if (charterInMarket != null) {
				if (charterInMarket.eContainer() == null) {
					spotMarketsModel.getCharterInMarkets().add(charterInMarket);
				} else if (charterInMarket.eContainer() != spotMarketsModel) {
					spotMarketsModel.getCharterInMarkets().add(charterInMarket);
				}
			}
		}

		for (final Sequence seq : schedule.getSequences()) {
			for (final Event event : seq.getEvents()) {
				if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					if (!(vesselEvent.eContainer() instanceof CargoModel)) {
						cargoModel.getVesselEvents().add(vesselEvent);
					}
				}
			}
		}

		// Uncontain these slots so the call to #derive will re-parent them
		for (final SlotAllocation a : schedule.getSlotAllocations()) {
			final Slot<?> slot = a.getSlot();
			if (slot != null && slot.eContainer() != cargoModel) {
				final EReference ref = slot.eContainmentFeature();
				if (ref.isMany()) {
					final List<EObject> l = (List<EObject>) slot.eContainer().eGet(ref);
					l.remove(slot);
				} else {
					slot.eContainer().eUnset(ref);
				}
			}

		}
		// Uncontain these slots so the call to #derive will re-parent them
		for (final OpenSlotAllocation a : schedule.getOpenSlotAllocations()) {
			final Slot<?> slot = a.getSlot();
			if (slot != null && slot.eContainer() != cargoModel) {
				final EReference ref = slot.eContainmentFeature();
				if (ref.isMany()) {
					final List<EObject> l = (List<EObject>) slot.eContainer().eGet(ref);
					l.remove(slot);
				} else {
					slot.eContainer().eUnset(ref);
				}
			}
			
		}

		// Clear any insertion plans - assume no longer relevant
		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
		LNGSchedulerJobUtils.clearAnalyticsResults(analyticsModel);

		if (modelCustomiser != null) {
			modelCustomiser.accept(scenarioModel, schedule);
		}

		// TODO: Need injector for correct post export processors
		final Command command = LNGSchedulerJobUtils.exportSchedule(null, scenarioModel, editingDomain, schedule);
		editingDomain.getCommandStack().execute(command);

		// Re-check after the customiser has run.
		for (final Cargo cargo : cargoModel.getCargoes()) {
			VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				if (charterInMarket.eContainer() == null) {
					spotMarketsModel.getCharterInMarkets().add(charterInMarket);
				}
			}
		}

		assert EMFUtils.checkValidContainment(scenarioModel);

		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(scenarioInstance);
		if (scenarioService == null) {
			// Open but deleted scenario?
			return null;
		}
		final ScenarioInstance theFork = scenarioService.copyInto(scenarioInstance, scenarioDataProvider, newForkName, new NullProgressMonitor());

		if (openScenario) {
			try {
				OpenScenarioUtils.openScenarioInstance(theFork);
			} catch (final PartInitException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return theFork;

	}
}
