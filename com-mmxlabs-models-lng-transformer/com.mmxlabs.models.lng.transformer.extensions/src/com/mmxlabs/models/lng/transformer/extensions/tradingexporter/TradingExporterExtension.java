/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * EMF export side for basic trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author hinton
 * 
 */
public class TradingExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		for (final ISequenceElement element : annotatedSolution.getContext().getOptimisationData().getSequenceElements()) {
			{
				final IProfitAndLossAnnotation profitAndLossWithTimeCharter = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
						IProfitAndLossAnnotation.class);

				if (profitAndLossWithTimeCharter != null) {
					// emit p&l entry - depends on the type of slot associated with the element.
					final IPortSlot slot = slotProvider.getPortSlot(element);

					if (slot instanceof ILoadOption) {
						final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
						CargoAllocation cargoAllocation = null;
						for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
							for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
								if (slotAllocation.getSlot() == modelSlot) {
									cargoAllocation = allocation;
									break;
								}
							}
						}
						if (cargoAllocation != null) {
							setPandLentries(profitAndLossWithTimeCharter, cargoAllocation);
						} else {

							OpenSlotAllocation openSlotAllocation = null;
							for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
								if (allocation.getSlot() == modelSlot) {
									openSlotAllocation = allocation;
									break;
								}
							}
							if (openSlotAllocation != null) {
								setPandLentries(profitAndLossWithTimeCharter, openSlotAllocation);
							} else {
								MarketAllocation marketAllocation = null;
								for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
									if (allocation.getSlot() == modelSlot) {
										marketAllocation = allocation;
										break;
									}
								}
								if (marketAllocation != null) {
									setPandLentries(profitAndLossWithTimeCharter, marketAllocation);
								}
							}
						}
					} else if (slot instanceof IDischargeOption) {
						final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);

						OpenSlotAllocation openSlotAllocation = null;
						for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
							if (allocation.getSlot() == modelSlot) {
								openSlotAllocation = allocation;
								break;
							}
						}
						if (openSlotAllocation != null) {
							setPandLentries(profitAndLossWithTimeCharter, openSlotAllocation);
						} else {
							MarketAllocation marketAllocation = null;
							for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
								if (allocation.getSlot() == modelSlot) {
									marketAllocation = allocation;
									break;
								}
							}
							if (marketAllocation != null) {
								setPandLentries(profitAndLossWithTimeCharter, marketAllocation);
							}
						}
					} else if (slot instanceof IVesselEventPortSlot) {
						final com.mmxlabs.models.lng.cargo.VesselEvent modelEvent = modelEntityMap.getModelObject(slot, com.mmxlabs.models.lng.cargo.VesselEvent.class);
						VesselEventVisit visit = null;
						//
						for (final Sequence sequence : outputSchedule.getSequences()) {
							for (final Event event : sequence.getEvents()) {
								if (event instanceof VesselEventVisit) {
									if (((VesselEventVisit) event).getVesselEvent() == modelEvent) {
										visit = (VesselEventVisit) event;
									}
								}
							}
						}
						if (visit != null) {
							setPandLentries(profitAndLossWithTimeCharter, visit);
						}
						// }
					} else if (slot instanceof StartPortSlot) {
						final StartEvent startEvent = findStartEvent(vesselProvider, element);

						if (startEvent != null) {
							setPandLentries(profitAndLossWithTimeCharter, startEvent);
						}
					} else if (slot instanceof EndPortSlot) {
						final EndEvent endEvent = findEndEvent(element);

						if (endEvent != null) {
							setPandLentries(profitAndLossWithTimeCharter, endEvent);
						}
					}
				}
			}
			final IProfitAndLossAnnotation generatedCharterOutProfitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_charterOutProfitAndLoss,
					IProfitAndLossAnnotation.class);
			if (generatedCharterOutProfitAndLoss != null) {
				// emit p&l entry - depends on the type of slot associated with the element.
				final IPortSlot slot = slotProvider.getPortSlot(element);

				if (slot instanceof ILoadOption) {
					final Slot modelSlot = modelEntityMap.getModelObject(slot, Slot.class);
					// CargoAllocation cargoAllocation = null;
					SlotVisit slotVisit = null;
					for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
						for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
							if (slotAllocation.getSlot() == modelSlot) {
								slotVisit = slotAllocation.getSlotVisit();
								break;

							}
						}
					}
					if (slotVisit != null) {

						// TODO: Quick hack to find the charter event. Should do better search in case it is not here!
						Event nextEvent = slotVisit.getNextEvent();
						while (nextEvent != null && !(nextEvent instanceof GeneratedCharterOut)) {
							nextEvent = nextEvent.getNextEvent();
						}
						if (nextEvent instanceof GeneratedCharterOut) {
							setPandLentries(generatedCharterOutProfitAndLoss, (GeneratedCharterOut) nextEvent);
						}
					}

				} else {
					if (slot instanceof StartPortSlot) {
						final StartEvent startEvent = findStartEvent(vesselProvider, element);
						if (startEvent != null) {
							// Look forward in sequence for a charter event
							Event nextEvent = startEvent.getNextEvent();
							while (nextEvent != null && !(nextEvent instanceof GeneratedCharterOut)) {
								nextEvent = nextEvent.getNextEvent();
							}
							if (nextEvent instanceof GeneratedCharterOut) {
								setPandLentries(generatedCharterOutProfitAndLoss, (GeneratedCharterOut) nextEvent);
							}
						}
					} else if (slot instanceof EndPortSlot) {
						// ? Unexpected state!
					} else if (slot instanceof IVesselEventPortSlot) {
						final com.mmxlabs.models.lng.cargo.VesselEvent modelEvent = modelEntityMap.getModelObject(slot, com.mmxlabs.models.lng.cargo.VesselEvent.class);
						VesselEventVisit visit = null;
						//
						for (final Sequence sequence : outputSchedule.getSequences()) {
							for (final Event event : sequence.getEvents()) {
								if (event instanceof VesselEventVisit) {
									if (((VesselEventVisit) event).getVesselEvent() == modelEvent) {
										visit = (VesselEventVisit) event;
									}
								}
							}
						}
						if (visit != null) {
							// Look forward in sequence for a charter event
							Event nextEvent = visit.getNextEvent();
							while (nextEvent != null && !(nextEvent instanceof GeneratedCharterOut)) {
								nextEvent = nextEvent.getNextEvent();
							}
							if (nextEvent instanceof GeneratedCharterOut) {
								setPandLentries(generatedCharterOutProfitAndLoss, (GeneratedCharterOut) nextEvent);
							}
						}
					}

				}
			}
		}

		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

	private EndEvent findEndEvent(final ISequenceElement element) {
		EndEvent endEvent = null;
		//
		for (int i = 0; i < annotatedSolution.getSequences().size(); ++i) {
			final ISequence seq = annotatedSolution.getSequences().getSequence(i);
			final IResource res = annotatedSolution.getSequences().getResources().get(i);
			if (seq.get(0) == element) {
				for (final Sequence sequence : outputSchedule.getSequences()) {
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					if (vesselAvailability == null) {
						continue;
					}

					final IVessel iVessel = modelEntityMap.getOptimiserObject(vesselAvailability, IVessel.class);
					if (iVessel == res) {
						if (sequence.getEvents().size() > 0) {
							final Event evt = sequence.getEvents().get(sequence.getEvents().size() - 1);
							if (evt instanceof EndEvent) {
								endEvent = (EndEvent) evt;
								break;
							}
						}
					}
				}
			}
		}
		return endEvent;
	}

	private StartEvent findStartEvent(final IVesselProvider vesselProvider, final ISequenceElement element) {
		StartEvent startEvent = null;
		//
		LOOP_OUTER: for (int i = 0; i < annotatedSolution.getSequences().size(); ++i) {

			// Find the optimiser sequence for the start element
			final ISequence seq = annotatedSolution.getSequences().getSequence(i);
			if (seq.get(0) == element) {
				// Found the sequence, so no find the matching EMF sequence
				for (final Sequence sequence : outputSchedule.getSequences()) {
					// Get the EMF Vessel
					final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
					if (vesselAvailability == null) {
						continue;
					}
					// Find the matching
					final IResource res = annotatedSolution.getSequences().getResources().get(i);
					final IVesselAvailability iVesselAvailability = modelEntityMap.getOptimiserObject(vesselAvailability, IVesselAvailability.class);

					// Look up correct instance (NOTE: Even though IVessel extends IResource, they seem to be different instances.
					if (iVesselAvailability == vesselProvider.getVesselAvailability(res)) {
						if (sequence.getEvents().size() > 0) {
							final Event evt = sequence.getEvents().get(0);
							if (evt instanceof StartEvent) {
								startEvent = (StartEvent) evt;
								break LOOP_OUTER;
							}
						}
					}
				}
			}
		}
		return startEvent;
	}

	private void setPandLentries(final IProfitAndLossAnnotation profitAndLoss, final ProfitAndLossContainer container) {

		if (container == null) {
			return;
		}
		if (profitAndLoss == null) {
			return;
		}

		int totalGroupValue = 0;
		int totalGroupValuePreTax = 0;
		final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
		container.setGroupProfitAndLoss(groupProfitAndLoss);

		final Collection<IProfitAndLossEntry> entries = profitAndLoss.getEntries();

		final Map<BaseEntityBook, int[]> groupProfitMap = new HashMap<BaseEntityBook, int[]>();

		// We may see the same entity multiple times - so aggregate results
		for (final IProfitAndLossEntry entry : entries) {

			// final BaseLegalEntity entity = entities.getModelObject(entry.getEntityBook().getEntity(), BaseLegalEntity.class);
			final BaseEntityBook entityBook = modelEntityMap.getModelObject(entry.getEntityBook(), BaseEntityBook.class);
			final int groupProfit = OptimiserUnitConvertor.convertToExternalFixedCost(entry.getFinalGroupValue());
			final int groupProfitPreTax = OptimiserUnitConvertor.convertToExternalFixedCost(entry.getFinalGroupValuePreTax());

			if (!groupProfitMap.containsKey(entityBook)) {
				groupProfitMap.put(entityBook, new int[2]);
			}
			groupProfitMap.get(entityBook)[0] += groupProfit;
			groupProfitMap.get(entityBook)[1] += groupProfitPreTax;
		}
		// Now create output data on the unique set.
		for (final Map.Entry<BaseEntityBook, int[]> e : groupProfitMap.entrySet()) {

			final EntityProfitAndLoss streamData = ScheduleFactory.eINSTANCE.createEntityProfitAndLoss();
			streamData.setEntity((BaseLegalEntity) e.getKey().eContainer());
			streamData.setEntityBook(e.getKey());
			final int groupValue = e.getValue()[0];
			final int groupValuePreTax = e.getValue()[1];

			streamData.setProfitAndLoss(groupValue);
			streamData.setProfitAndLossPreTax(groupValuePreTax);
			totalGroupValue += groupValue;
			totalGroupValuePreTax += groupValuePreTax;

			groupProfitAndLoss.getEntityProfitAndLosses().add(streamData);
		}

		groupProfitAndLoss.setProfitAndLoss(totalGroupValue);
		groupProfitAndLoss.setProfitAndLossPreTax(totalGroupValuePreTax);
	}
}
