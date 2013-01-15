/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.extensions;

import java.io.Serializable;
import java.util.Collection;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.IDetailTreeElement;
import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.common.detailtree.impl.TotalCostDetailElement;
import com.mmxlabs.common.detailtree.impl.UnitPriceDetailElement;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.annotations.IShippingCostAnnotation;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.trading.optimiser.TradingConstants;

/**
 * EMF export side for trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author hinton
 * 
 * @since 2.0
 */
public class TradingExporterExtension implements IExporterExtension {
	private ModelEntityMap entities;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Override
	public void startExporting(final MMXRootObject inputScenario, final Schedule outputSchedule, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
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
			final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss, IProfitAndLossAnnotation.class);
			final IShippingCostAnnotation shippingCost = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_shippingCost, IShippingCostAnnotation.class);
			final IShippingCostAnnotation shippingCostWithBoilOff = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_shippingCostWithBoilOff,
					IShippingCostAnnotation.class);
			if (profitAndLoss != null) {
				// emit p&l entry - depends on the type of slot associated with the element.
				final IPortSlot slot = slotProvider.getPortSlot(element);

				if (slot instanceof ILoadOption) {
					final Slot modelSlot = entities.getModelObject(slot, Slot.class);
					CargoAllocation cargoAllocation = null;
					for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
						if (allocation.getLoadAllocation().getSlot() == modelSlot || allocation.getDischargeAllocation().getSlot() == slot) {
							cargoAllocation = allocation;
							break;
						}
					}
					if (cargoAllocation != null) {
						setPandLentries(profitAndLoss, cargoAllocation);
						setShippingCosts(shippingCost, cargoAllocation, false);
						setShippingCosts(shippingCostWithBoilOff, cargoAllocation, true);
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
						setPandLentries(profitAndLoss, visit);
						setShippingCosts(shippingCost, visit, false);
						setShippingCosts(shippingCostWithBoilOff, visit, true);
					}
					// }
				} else if (slot instanceof StartPortSlot) {
					StartEvent startEvent = null;
					//
					LOOP_OUTER: for (int i = 0; i < annotatedSolution.getSequences().size(); ++i) {

						// Find the optimiser sequence for the start element
						final ISequence seq = annotatedSolution.getSequences().getSequence(i);
						if (seq.get(0) == element) {
							// Found the sequence, so no find the matching EMF sequence
							for (final Sequence sequence : outputSchedule.getSequences()) {
								// Get the EMF Vessel
								final Vessel vessel = sequence.getVessel();
								if (vessel == null) {
									continue;
								}
								// Find the matching
								final IResource res = annotatedSolution.getSequences().getResources().get(i);
								final IVessel iVessel = entities.getOptimiserObject(vessel, IVessel.class);

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

					if (startEvent != null) {
						setPandLentries(profitAndLoss, startEvent);
						setShippingCosts(shippingCost, startEvent, false);
						setShippingCosts(shippingCostWithBoilOff, startEvent, true);
					}
				} else if (slot instanceof EndPortSlot) {
					EndEvent endEvent = null;
					//
					for (int i = 0; i < annotatedSolution.getSequences().size(); ++i) {
						final ISequence seq = annotatedSolution.getSequences().getSequence(i);
						final IResource res = annotatedSolution.getSequences().getResources().get(i);
						if (seq.get(0) == element) {
							for (final Sequence sequence : outputSchedule.getSequences()) {
								final Vessel vessel = sequence.getVessel();
								if (vessel == null) {
									continue;
								}

								final IVessel iVessel = entities.getOptimiserObject(vessel, IVessel.class);
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

					if (endEvent != null) {
						setPandLentries(profitAndLoss, endEvent);
						setShippingCosts(shippingCost, endEvent, false);
						setShippingCosts(shippingCostWithBoilOff, endEvent, true);
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
			final IProfitAndLossAnnotation generatedCharterOutProfitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_charterOutProfitAndLoss,
					IProfitAndLossAnnotation.class);
			if (generatedCharterOutProfitAndLoss != null) {
				// emit p&l entry - depends on the type of slot associated with the element.
				final IPortSlot slot = slotProvider.getPortSlot(element);

				if (slot instanceof ILoadOption) {
					final Slot modelSlot = entities.getModelObject(slot, Slot.class);
					CargoAllocation cargoAllocation = null;
					for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
						if (allocation.getLoadAllocation().getSlot() == modelSlot || allocation.getDischargeAllocation().getSlot() == slot) {
							cargoAllocation = allocation;
							break;
						}
					}
					if (cargoAllocation != null) {

						// TODO: Quick hack to find the charter event. Should do better search in case it is not here!
						final SlotVisit slotVisit = cargoAllocation.getDischargeAllocation().getSlotVisit();
						final Event nextEvent = slotVisit.getNextEvent().getNextEvent();
						if (nextEvent instanceof GeneratedCharterOut) {
							setPandLentries(generatedCharterOutProfitAndLoss, (ExtraDataContainer) nextEvent);
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

	private void setPandLentries(final IProfitAndLossAnnotation profitAndLoss, final ExtraDataContainer container) {
		int idx = 0;
		int totalGroupValue = 0;
		final Collection<IProfitAndLossEntry> entries = profitAndLoss.getEntries();
		if (entries.size() == 1) {
			// For single entry, we look at shipping only.
			// TODO: This is a mess!
			idx = 1;
		}
		for (final IProfitAndLossEntry entry : entries) {

			// TODO: Keep idx in sync with ProfitAndLossAllocationComponent
			final ExtraData streamData;
			switch (idx) {
			case 0:
				streamData = container.addExtraData(TradingConstants.ExtraData_upstream, "Upstream");
				break;
			case 1:
				streamData = container.addExtraData(TradingConstants.ExtraData_shipped, "Shipping");
				break;
			case 2:
				streamData = container.addExtraData(TradingConstants.ExtraData_downstream, "Downstream");
				break;
			default:
				throw new IllegalStateException("Expected three item in the profit and loss list - got " + entries.size());
			}

			final ExtraData entityData = streamData.addExtraData(entry.getEntity().getName(), entry.getEntity().getName());

			final int groupValue = OptimiserUnitConvertor.convertToExternalFixedCost(entry.getFinalGroupValue());
			totalGroupValue += groupValue;
			final ExtraData pnlData = entityData.addExtraData(TradingConstants.ExtraData_pnl, "P&L", groupValue, ExtraDataFormatType.CURRENCY);

			pnlData.addExtraData("date", "Date", entities.getDateFromHours(profitAndLoss.getBookingTime()), ExtraDataFormatType.AUTO);
			final ExtraData detail = exportDetailTree(entry.getDetails());
			detail.setName("Details");
			pnlData.getExtraData().add(detail);

			++idx;
		}
		container.addExtraData(TradingConstants.ExtraData_GroupValue, "Group Value", totalGroupValue, ExtraDataFormatType.CURRENCY);
	}

	private void setShippingCosts(final IShippingCostAnnotation shippingCostAnnotation, final ExtraDataContainer container, final boolean incBoilOff) {
		if (shippingCostAnnotation == null) {
			return;
		}
		final int shippingCostValue = OptimiserUnitConvertor.convertToExternalFixedCost(shippingCostAnnotation.getShippingCost());
		final ExtraData shippingCostData = (incBoilOff) ? container.addExtraData(TradingConstants.ExtraData_ShippingCostIncBoilOff, "Shipping Costs (With Boil-Off)", shippingCostValue,
				ExtraDataFormatType.CURRENCY) : container.addExtraData(TradingConstants.ExtraData_ShippingCost, "Shipping Costs", shippingCostValue, ExtraDataFormatType.CURRENCY);

		shippingCostData.addExtraData("date", "Date", entities.getDateFromHours(shippingCostAnnotation.getBookingTime()), ExtraDataFormatType.AUTO);

		if (shippingCostAnnotation.getDetails() != null) {
			final ExtraData detail = exportDetailTree(shippingCostAnnotation.getDetails());
			detail.setName("Details");
			shippingCostData.getExtraData().add(detail);
		}
	}

	private ExtraData exportDetailTree(final IDetailTree details) {
		final ExtraData ad = TypesFactory.eINSTANCE.createExtraData();

		ad.setName(details.getKey());
		ad.setKey(details.getKey());

		Object value = details.getValue();
		boolean round = false;
		boolean valueSet = false;
		if (value instanceof IDetailTreeElement) {
			final IDetailTreeElement element = (IDetailTreeElement) value;
			value = element.getObject();
			if (element instanceof CurrencyDetailElement) {
				ad.setFormatType(ExtraDataFormatType.CURRENCY);
				round = true;
			} else if (element instanceof TotalCostDetailElement) {
				round = true;
				ad.setFormatType(ExtraDataFormatType.CURRENCY);
			} else if (element instanceof UnitPriceDetailElement) {
				ad.setFormatType(ExtraDataFormatType.STRING_FORMAT);
				ad.setFormat("$%,f");
			} else if (element instanceof DurationDetailElement) {
				// Set value here to avoid scaling below. Force to an Int as ExtraDataImpl expects an Integer object.
				ad.setFormatType(ExtraDataFormatType.DURATION);
				ad.setValue(((Number) value).intValue());
				valueSet = true;
			}
		}
		if (!valueSet) {
			if (value instanceof Serializable) {
				if (value instanceof Integer) {
					final int x = (Integer) value;
					if (round || x % Calculator.ScaleFactor == 0) {
						ad.setValue(x / Calculator.ScaleFactor);
					} else {
						ad.setValue(x / (double) Calculator.ScaleFactor);
					}
				} else if (value instanceof Long) {
					final long x = (Long) value;
					if (round || x % Calculator.ScaleFactor == 0) {
						ad.setValue((int) (x / Calculator.ScaleFactor));
					} else {
						ad.setValue(x / (double) Calculator.ScaleFactor);
					}
				} else {
					ad.setValue((Serializable) value);
				}
			}
		}

		for (final IDetailTree child : details.getChildren()) {
			ad.getExtraData().add(exportDetailTree(child));
		}

		return ad;
	}
}
