/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.DualModeSolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.transformer.ui.analytics.spec.ScheduleSpecificationHelper;
import com.mmxlabs.models.lng.transformer.ui.common.ScheduleToSequencesTransformer;
import com.mmxlabs.models.lng.transformer.util.ScheduleSpecificationTransformer;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

public class EvaluateSolutionSetHelper {
	final ScheduleSpecificationHelper scheduleSpecificationHelper;
	private @NonNull EditingDomain editingDomain;

	public EvaluateSolutionSetHelper(final IScenarioDataProvider scenarioDataProvider) {
		this.scheduleSpecificationHelper = new ScheduleSpecificationHelper(scenarioDataProvider);
		this.editingDomain = scenarioDataProvider.getEditingDomain();
	}

	public void processExtraData(SolutionOptionMicroCase solutionSet) {
		if (solutionSet != null) {
			scheduleSpecificationHelper.processExtraData_VesselCharters(solutionSet.getExtraVesselCharters());
			scheduleSpecificationHelper.processExtraData_CharterInMarketOverrides(solutionSet.getCharterInMarketOverrides());
		}

	}

	public void processExtraData(@Nullable AbstractSolutionSet solutionSet) {
		scheduleSpecificationHelper.processExtraData(solutionSet);
	}

	public void processSolution(ScheduleModel scheduleModel) {
		if (scheduleModel != null && scheduleModel.getSchedule() != null) {
			final BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> r = (bridge, injector) -> {
				final ScheduleToSequencesTransformer transformer = injector.getInstance(ScheduleToSequencesTransformer.class);
				final ISequences base = transformer.createSequences(scheduleModel.getSchedule(), bridge.getDataTransformer());

				try {
					final Schedule base_schedule = bridge.createSchedule(base, Collections.emptyMap());
					return () -> {
						CompoundCommand cmd = new CompoundCommand();
						cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__SCHEDULE, base_schedule));
						cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));
						return cmd;
					};
				} catch (final Throwable e) {

				}
				return null;
			};

			scheduleSpecificationHelper.addJobs(r);
		}
	}

	public void processSolution(final ScheduleSpecification scheduleSpecification, ScheduleModel scheduleModel) {
		if (scheduleSpecification != null) {
			final BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> r = (bridge, injector) -> {
				final ScheduleSpecificationTransformer transformer = injector.getInstance(ScheduleSpecificationTransformer.class);
				final ISequences base = transformer.createSequences(scheduleSpecification, bridge.getDataTransformer(), false);

				try {
					final Schedule baseSchedule = bridge.createSchedule(base, Collections.emptyMap());
					return () -> {
						CompoundCommand cmd = new CompoundCommand();
						cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__SCHEDULE, baseSchedule));
						cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));
						return cmd;
					};
				} catch (final Throwable e) {

				}
				return null;
			};

			scheduleSpecificationHelper.addJobs(r);
		} else if (scheduleModel != null) {
			processSolution(scheduleModel);
		}
	}

	public void generateResults(final ScenarioInstance scenarioInstance, final UserSettings userSettings, final EditingDomain editingDomain, IProgressMonitor monitor) {

		List<String> hints = Lists.newArrayList(LNGTransformerHelper.HINT_DISABLE_CACHES //
		// LNGEvaluationModule.HINT_PORTFOLIO_BREAKEVEN, //
		);

		scheduleSpecificationHelper.generateResults(scenarioInstance, userSettings, editingDomain, hints, monitor);
	}

	/**
	 * Create customiser to remove unused cargo model inputs.
	 * 
	 * @return
	 */
	public static BiConsumer<LNGScenarioModel, Schedule> createModelCustomiser() {
		return (input_scenario, input_schedule) -> {

			final Set<VesselCharter> usedVesselAvailabilites = new LinkedHashSet<>();
			final Set<CharterInMarketOverride> usedCharterInMarketOverrides = new LinkedHashSet<>();
			final Set<LoadSlot> usedLoadSlots = new LinkedHashSet<>();
			final Set<DischargeSlot> usedDischargeSlots = new LinkedHashSet<>();
			final Set<VesselEvent> usedVesselEvents = new LinkedHashSet<>();
			final Set<Cargo> usedCargoes = new LinkedHashSet<>();

			for (final SlotAllocation slotAllocation : input_schedule.getSlotAllocations()) {
				final Slot slot = slotAllocation.getSlot();
				if (slot instanceof LoadSlot) {
					usedLoadSlots.add((LoadSlot) slot);

					if (slot.getCargo() != null && slotAllocation.getCargoAllocation() != null) {
						if (slotAllocation.getCargoAllocation().getSlotAllocations().get(0) == slotAllocation) {
							usedCargoes.add(slot.getCargo());
						}
					}
				} else if (slot instanceof DischargeSlot) {
					usedDischargeSlots.add((DischargeSlot) slot);
				} else {
					assert false;
				}
			}
			for (final OpenSlotAllocation openSlotAllocation : input_schedule.getOpenSlotAllocations()) {
				final Slot slot = openSlotAllocation.getSlot();
				if (slot instanceof LoadSlot) {
					usedLoadSlots.add((LoadSlot) slot);
				} else if (slot instanceof DischargeSlot) {
					usedDischargeSlots.add((DischargeSlot) slot);
				} else {
					assert false;
				}
			}
			for (final Sequence sequence : input_schedule.getSequences()) {
				if (sequence.getVesselCharter() != null) {
					usedVesselAvailabilites.add(sequence.getVesselCharter());
				}
				if (sequence.getCharterInMarketOverride() != null) {
					usedCharterInMarketOverrides.add(sequence.getCharterInMarketOverride());
				}
				if (sequence.getCharterInMarket() != null) {
					if (!(sequence.getCharterInMarket().eContainer() instanceof SpotMarketsModel)) {

						EObject container = sequence.getCharterInMarket().eContainer();
						if (container != null) {
							((Collection) container.eGet(sequence.getCharterInMarket().eContainmentFeature())).remove(sequence.getCharterInMarket());
						}
					}
				}
				for (final Event event : sequence.getEvents()) {
					if (event instanceof VesselEventVisit) {
						final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
						usedVesselEvents.add(vesselEventVisit.getVesselEvent());
					}
				}
			}

			final Set<EObject> objectsToDelete = new LinkedHashSet<>();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(input_scenario);

			{
				final Iterator<LoadSlot> l_itr = cargoModel.getLoadSlots().iterator();
				while (l_itr.hasNext()) {
					final LoadSlot loadSlot = l_itr.next();
					if (!usedLoadSlots.contains(loadSlot)) {
						loadSlot.setCargo(null);
						objectsToDelete.add(loadSlot);
						l_itr.remove();
					}
				}
			}
			{
				final Iterator<DischargeSlot> l_itr = cargoModel.getDischargeSlots().iterator();
				while (l_itr.hasNext()) {
					final DischargeSlot dischargeSlot = l_itr.next();
					if (!usedDischargeSlots.contains(dischargeSlot)) {
						dischargeSlot.setCargo(null);
						objectsToDelete.add(dischargeSlot);
						l_itr.remove();
					}
				}
			}
			{
				final Iterator<Cargo> l_itr = cargoModel.getCargoes().iterator();
				while (l_itr.hasNext()) {
					final Cargo cargo = l_itr.next();
					if (cargo.getSlots().size() < 2 || !usedCargoes.contains(cargo)) {
						cargo.getSlots().clear();
						objectsToDelete.add(cargo);

						l_itr.remove();
					}
				}
			}
			{
				final Iterator<VesselEvent> l_itr = cargoModel.getVesselEvents().iterator();
				while (l_itr.hasNext()) {
					final VesselEvent vesselEvent = l_itr.next();
					if (!usedVesselEvents.contains(vesselEvent)) {
						objectsToDelete.add(vesselEvent);

						l_itr.remove();
					}
				}
			}
			{
				final Iterator<VesselCharter> l_itr = cargoModel.getVesselCharters().iterator();
				while (l_itr.hasNext()) {
					final VesselCharter vesselCharter = l_itr.next();
					if (!usedVesselAvailabilites.contains(vesselCharter)) {
						objectsToDelete.add(vesselCharter);
						l_itr.remove();
					}
				}
			}

			for (final VesselCharter vesselCharter : usedVesselAvailabilites) {
				if (!cargoModel.getVesselCharters().contains(vesselCharter)) {
					cargoModel.getVesselCharters().add(vesselCharter);
				}
			}
			cargoModel.getCharterInMarketOverrides().clear();
			cargoModel.getCharterInMarketOverrides().addAll(usedCharterInMarketOverrides);

			input_scenario.getScheduleModel().setSchedule(null);

			final Map<EObject, Collection<Setting>> crossReferences = EcoreUtil.UsageCrossReferencer.findAll(objectsToDelete, input_scenario);
			for (final Map.Entry<EObject, Collection<Setting>> ee : crossReferences.entrySet()) {
				final EObject object = ee.getKey();
				for (final Setting setting : ee.getValue()) {
					final EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
					final EObject container = setting.getEObject();
					if (eStructuralFeature.isMany()) {
						((Collection<?>) container.eGet(eStructuralFeature)).remove(object);
					} else {
						container.eUnset(eStructuralFeature);
					}
				}
			}
		};
	}

	public void evaluateBaseCase() {

		// Construct a new set of sequences for the initial state.
		// Note: In this mode, the data transformer initial sequences are not correct as they will contain *ALL* additional data.

		final BiFunction<LNGScenarioToOptimiserBridge, Injector, Supplier<Command>> r = (bridge, injector) -> {
			final ScheduleToSequencesTransformer transformer = injector.getInstance(ScheduleToSequencesTransformer.class);
			ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(bridge.getScenarioDataProvider());
			final ISequences base = transformer.createSequences(scheduleModel.getSchedule(), bridge.getDataTransformer());
			try {
				final Schedule base_schedule = bridge.createSchedule(base, Collections.emptyMap());
				return () -> {
					CompoundCommand cmd = new CompoundCommand();
					cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__SCHEDULE, base_schedule));
					cmd.append(SetCommand.create(editingDomain, scheduleModel, SchedulePackage.Literals.SCHEDULE_MODEL__DIRTY, Boolean.FALSE));
					return cmd;
				};
			} catch (final Throwable e) {

			}
			return null;
		};

		scheduleSpecificationHelper.addJobs(r);
	}

	public static void recomputeSolution(ScenarioModelRecord modelRecord, @Nullable ScenarioInstance scenarioInstance, @Nullable AbstractSolutionSet abstractSolutionSet, boolean fromSpecifications,
			boolean open) {

		IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("EvaluateSolutionSetHelper:recomputeSolution");
		try {
			recomputeSolution(sdp, scenarioInstance, abstractSolutionSet, fromSpecifications, open, true, true);
		} finally {
			// sdp.close();
		}
	}

	public static void recomputeSolution(IScenarioDataProvider sdp, @Nullable ScenarioInstance scenarioInstance, @Nullable AbstractSolutionSet abstractSolutionSet, boolean fromSpecifications,
			boolean open, boolean runAsync, boolean closeSDP) {

		try {

			ICoreRunnable runnable = (monitor) -> {
				try {
					Runnable exportAction = () -> {
						final EvaluateSolutionSetHelper helper = new EvaluateSolutionSetHelper(sdp);

						if (abstractSolutionSet != null) {
							UserSettings userSettings = abstractSolutionSet.getUserSettings();
							helper.processExtraData(abstractSolutionSet);

							Consumer<SolutionOption> action = opt -> {
								helper.processSolution(opt.getScheduleSpecification(), opt.getScheduleModel());
								if (opt instanceof DualModeSolutionOption) {
									final DualModeSolutionOption dualModeSolutionOption = (DualModeSolutionOption) opt;

									final SolutionOptionMicroCase base = dualModeSolutionOption.getMicroBaseCase();
									if (base != null) {
										// Re-evaluate from schedule
										helper.processExtraData(base);
										if (fromSpecifications) {
											// (Experimental version) Re-evaluate from change specification)
											helper.processSolution(base.getScheduleSpecification(), base.getScheduleModel());
										} else {
											helper.processSolution(base.getScheduleModel());
										}
									}

									final SolutionOptionMicroCase target = dualModeSolutionOption.getMicroTargetCase();
									if (target != null) {
										// Re-evaluate from schedule
										helper.processExtraData(target);

										if (fromSpecifications) {
											// (Experimental version) Re-evaluate from change specification)
											helper.processSolution(target.getScheduleSpecification(), target.getScheduleModel());
										} else {
											helper.processSolution(target.getScheduleModel());
										}
									}

								}
							};

							{
								SolutionOption opt = abstractSolutionSet.getBaseOption();
								if (opt != null) {
									action.accept(opt);
								}
							}

							for (final SolutionOption opt : abstractSolutionSet.getOptions()) {
								action.accept(opt);
							}

							final UserSettings copy = EMFCopier.copy(userSettings);
							helper.generateResults(scenarioInstance, copy, sdp.getEditingDomain(), monitor);
							if (open) {
								new AnalyticsSolution(scenarioInstance, abstractSolutionSet, abstractSolutionSet.getName()).open();
							}
						}
					};
					if (sdp instanceof SimpleScenarioDataProvider) {
						// ITS case may come here and we do not have a model reference to execute lock on
						exportAction.run();
					} else {
						sdp.getModelReference().executeWithLock(true, exportAction);
					}
				} finally {
					if (closeSDP) {
						sdp.close();
					}
				}
			};
			if (runAsync) {
				Job job = Job.create("Re-calculate solution(s)", runnable);
				job.setUser(true);
				job.setRule(new ScenarioInstanceSchedulingRule(scenarioInstance));
				job.schedule();
			} else {
				runnable.run(new NullProgressMonitor());
			}
		} catch (final Exception e) {
			if (closeSDP) {
				sdp.close();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
