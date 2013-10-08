/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * EMF export side for trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author hinton
 * 
 * @since 3.0
 */
public class TradingExporterExtension implements IExporterExtension {
	private ModelEntityMap entities;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
		this.entities = entities;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		// final EList<BookedRevenue> revenues = outputSchedule.getRevenue();
		final IPortSlotProvider slotProvider = annotatedSolution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		final IVesselProvider vesselProvider = annotatedSolution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		for (final ISequenceElement element : annotatedSolution.getContext().getOptimisationData().getSequenceElements()) {
			{
				final IProfitAndLossAnnotation profitAndLossWithTimeCharter = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
						IProfitAndLossAnnotation.class);
				final IProfitAndLossAnnotation profitAndLossWithoutTimeCharter = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLossNoTimeCharterRate,
						IProfitAndLossAnnotation.class);
				// final IShippingCostAnnotation shippingCost = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_shippingCost, IShippingCostAnnotation.class);
				// final IShippingCostAnnotation shippingCostWithBoilOff = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff,
				// IShippingCostAnnotation.class);

				if (profitAndLossWithTimeCharter != null) {
					// emit p&l entry - depends on the type of slot associated with the element.
					final IPortSlot slot = slotProvider.getPortSlot(element);

					if (slot instanceof ILoadOption) {
						final Slot modelSlot = entities.getModelObject(slot, Slot.class);
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
							setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, cargoAllocation);
							// setShippingCosts(shippingCost, cargoAllocation, false);
							// setShippingCosts(shippingCostWithBoilOff, cargoAllocation, true);
						} else {

							MarketAllocation marketAllocation = null;
							for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
								if (allocation.getSlot() == modelSlot) {
									marketAllocation = allocation;
									break;
								}
							}
							if (marketAllocation != null) {
								setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, marketAllocation);
							}
						}
					} else if (slot instanceof IDischargeOption) {
						final Slot modelSlot = entities.getModelObject(slot, Slot.class);

						MarketAllocation marketAllocation = null;
						for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
							if (allocation.getSlot() == modelSlot) {
								marketAllocation = allocation;
								break;
							}
						}
						if (marketAllocation != null) {
							setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, marketAllocation);
						}

					} else if (slot instanceof IVesselEventPortSlot) {
						final com.mmxlabs.models.lng.fleet.VesselEvent modelEvent = entities.getModelObject(slot, com.mmxlabs.models.lng.fleet.VesselEvent.class);
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
							setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, visit);
							// setShippingCosts(shippingCost, visit, false);
							// setShippingCosts(shippingCostWithBoilOff, visit, true);
						}
						// }
					} else if (slot instanceof StartPortSlot) {
						final StartEvent startEvent = findStartEvent(vesselProvider, element);

						if (startEvent != null) {
							setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, startEvent);
							// setShippingCosts(shippingCost, startEvent, false);
							// setShippingCosts(shippingCostWithBoilOff, startEvent, true);
						}
					} else if (slot instanceof EndPortSlot) {
						final EndEvent endEvent = findEndEvent(element);

						if (endEvent != null) {
							setPandLentries(profitAndLossWithTimeCharter, profitAndLossWithoutTimeCharter, endEvent);
							// setShippingCosts(shippingCost, endEvent, false);
							// setShippingCosts(shippingCostWithBoilOff, endEvent, true);
						}
					} else {
						// for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {
						// AdditionalData additionalData = ScheduleFactory.eINSTANCE.createAdditionalData();
						//
						// final BookedRevenue revenue = ScheduleFactory.eINSTANCE.createBookedRevenue();
						// revenue.setEntity(entities.getModelObject(entry.getEntity(), Entity.class));
						// revenue.setDate(entities.getDateFromHours(profitAndLoss.getBookingTime()));
						// revenue.setValue((int) (entry.getFinalGroupValue() / Calculator.ScaleFactor));
						// revenue.setDetails(DetailTreeExporter.exportDetail(entry.getDetails()));
						// revenues.add(revenue);
						// }
					}
				}
			}
			final IProfitAndLossAnnotation generatedCharterOutProfitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_charterOutProfitAndLoss,
					IProfitAndLossAnnotation.class);
			final IProfitAndLossAnnotation generatedCharterOutProfitAndLossWithoutTimeCharter = annotatedSolution.getElementAnnotations().getAnnotation(element,
					SchedulerConstants.AI_charterOutProfitAndLossNoTimeCharterRate, IProfitAndLossAnnotation.class);
			if (generatedCharterOutProfitAndLoss != null) {
				// emit p&l entry - depends on the type of slot associated with the element.
				final IPortSlot slot = slotProvider.getPortSlot(element);

				if (slot instanceof ILoadOption) {
					final Slot modelSlot = entities.getModelObject(slot, Slot.class);
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
							setPandLentries(generatedCharterOutProfitAndLoss, generatedCharterOutProfitAndLossWithoutTimeCharter, (GeneratedCharterOut) nextEvent);
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
								setPandLentries(generatedCharterOutProfitAndLoss, generatedCharterOutProfitAndLossWithoutTimeCharter, (GeneratedCharterOut) nextEvent);
							}
						}
					} else if (slot instanceof EndPortSlot) {
						// ? Unexpected state!
					} else if (slot instanceof IVesselEventPortSlot) {
						final com.mmxlabs.models.lng.fleet.VesselEvent modelEvent = entities.getModelObject(slot, com.mmxlabs.models.lng.fleet.VesselEvent.class);
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
								setPandLentries(generatedCharterOutProfitAndLoss, generatedCharterOutProfitAndLossWithoutTimeCharter, (GeneratedCharterOut) nextEvent);
							}
						}
					}

				}
			}
		}

		// clear refs, just in case.
		entities = null;
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

					final IVessel iVessel = entities.getOptimiserObject(vesselAvailability, IVessel.class);
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
					final IVessel iVessel = entities.getOptimiserObject(vesselAvailability, IVessel.class);

					// Look up correct instance (NOTE: Even though IVessel extends IResource, they seem to be different instances.
					if (iVessel == vesselProvider.getVessel(res)) {
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

	private void setPandLentries(final IProfitAndLossAnnotation profitAndLossWithTCRate, final IProfitAndLossAnnotation profitAndLossWithoutTCRate, final ProfitAndLossContainer container) {
		setPandLentries(profitAndLossWithTCRate, container, true);
		setPandLentries(profitAndLossWithoutTCRate, container, false);
	}

	private void setPandLentries(final IProfitAndLossAnnotation profitAndLoss, final ProfitAndLossContainer container, final boolean includeTimeCharterRate) {
		int totalGroupValue = 0;
		final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
		if (includeTimeCharterRate) {
			container.setGroupProfitAndLoss(groupProfitAndLoss);
		} else {
			container.setGroupProfitAndLossNoTimeCharter(groupProfitAndLoss);
		}

		final Collection<IProfitAndLossEntry> entries = profitAndLoss.getEntries();

		final Map<LegalEntity, Integer> groupProfitMap = new HashMap<LegalEntity, Integer>();

		// We may see the same entity multiple times - so aggregate results
		for (final IProfitAndLossEntry entry : entries) {

			final LegalEntity entity = entities.getModelObject(entry.getEntity(), LegalEntity.class);
			int groupProfit = OptimiserUnitConvertor.convertToExternalFixedCost(entry.getFinalGroupValue());

			if (groupProfitMap.containsKey(entity)) {
				groupProfit += groupProfitMap.get(entity);
			}
			groupProfitMap.put(entity, groupProfit);
		}
		// Now create output data on the unique set.
		for (final Map.Entry<LegalEntity, Integer> e : groupProfitMap.entrySet()) {

			final EntityProfitAndLoss streamData = ScheduleFactory.eINSTANCE.createEntityProfitAndLoss();
			streamData.setEntity(e.getKey());
			final int groupValue = e.getValue();

			streamData.setProfitAndLoss(groupValue);
			totalGroupValue += groupValue;

			groupProfitAndLoss.getEntityProfitAndLosses().add(streamData);
		}

		groupProfitAndLoss.setProfitAndLoss(totalGroupValue);
	}

	// private void setShippingCosts(final IShippingCostAnnotation shippingCostAnnotation, final ProfitAndLossContainer container) {
	// if (shippingCostAnnotation == null) {
	// return;
	// }
	// // final int shippingCostValue = OptimiserUnitConvertor.convertToExternalFixedCost(shippingCostAnnotation.getShippingCost());
	// // final ExtraData shippingCostData = (incBoilOff) ? container.addExtraData(TradingConstants.ExtraData_ShippingCostIncBoilOff, "Shipping Costs (With Boil-Off)", shippingCostValue,
	// // ExtraDataFormatType.CURRENCY) : container.addExtraData(TradingConstants.ExtraData_ShippingCost, "Shipping Costs", shippingCostValue, ExtraDataFormatType.CURRENCY);
	// //
	// // shippingCostData.addExtraData("date", "Date", entities.getDateFromHours(shippingCostAnnotation.getBookingTime()), ExtraDataFormatType.AUTO);
	// //
	// // if (shippingCostAnnotation.getDetails() != null) {
	// // final ExtraData detail = exportDetailTree(shippingCostAnnotation.getDetails());
	// // detail.setName("Details");
	// // shippingCostData.getExtraData().add(detail);
	// // }
	// }

	// private ExtraData exportDetailTree(final IDetailTree details) {
	// final ExtraData ad = TypesFactory.eINSTANCE.createExtraData();
	//
	// ad.setName(details.getKey());
	// ad.setKey(details.getKey());
	//
	// Object value = details.getValue();
	// boolean round = false;
	// boolean valueSet = false;
	// if (value instanceof IDetailTreeElement) {
	// final IDetailTreeElement element = (IDetailTreeElement) value;
	// value = element.getObject();
	// if (element instanceof CurrencyDetailElement) {
	// ad.setFormatType(ExtraDataFormatType.CURRENCY);
	// round = true;
	// } else if (element instanceof TotalCostDetailElement) {
	// round = true;
	// ad.setFormatType(ExtraDataFormatType.CURRENCY);
	// } else if (element instanceof UnitPriceDetailElement) {
	// ad.setFormatType(ExtraDataFormatType.STRING_FORMAT);
	// ad.setFormat("$%,f");
	// } else if (element instanceof DurationDetailElement) {
	// // Set value here to avoid scaling below. Force to an Int as ExtraDataImpl expects an Integer object.
	// ad.setFormatType(ExtraDataFormatType.DURATION);
	// ad.setValue(((Number) value).intValue());
	// valueSet = true;
	// }
	// }
	// if (!valueSet) {
	// if (value instanceof Serializable) {
	// if (value instanceof Integer) {
	// final int x = (Integer) value;
	// if (round || x % Calculator.ScaleFactor == 0) {
	// ad.setValue(x / Calculator.ScaleFactor);
	// } else {
	// ad.setValue(x / (double) Calculator.ScaleFactor);
	// }
	// } else if (value instanceof Long) {
	// final long x = (Long) value;
	// if (round || x % Calculator.ScaleFactor == 0) {
	// ad.setValue((int) (x / Calculator.ScaleFactor));
	// } else {
	// ad.setValue(x / (double) Calculator.ScaleFactor);
	// }
	// } else {
	// ad.setValue((Serializable) value);
	// }
	// }
	// }
	//
	// for (final IDetailTree child : details.getChildren()) {
	// ad.getExtraData().add(exportDetailTree(child));
	// }
	//
	// return ad;
	// }
}
