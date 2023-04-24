/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
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
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.transformer.export.IPortSlotEventProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.components.ICharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterLengthEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IGeneratedCharterOutVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMaintenanceVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.StartPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.providers.ICharterLengthElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

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

	@Inject
	private IPortSlotEventProvider portSlotEventProvider;
	
	@Inject
	private ICharterLengthElementProvider charterLengthProvider;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		final Set<ISequenceElement> allElements = annotatedSolution.getEvaluationState().getData(SchedulerEvaluationProcess.ALL_ELEMENTS, Set.class);
		if (allElements != null) {
			IProfitAndLossAnnotation previousPNL = null;
			for (final ISequenceElement element : allElements) {
				{
					final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss,
							IProfitAndLossAnnotation.class);

					if (profitAndLoss != null) {
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
								setPandLentries(profitAndLoss, cargoAllocation);
							} else {

								OpenSlotAllocation openSlotAllocation = null;
								for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
									if (allocation.getSlot() == modelSlot) {
										openSlotAllocation = allocation;
										break;
									}
								}
								if (openSlotAllocation != null) {
									setPandLentries(profitAndLoss, openSlotAllocation);
								} else {
									MarketAllocation marketAllocation = null;
									for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
										if (allocation.getSlot() == modelSlot) {
											marketAllocation = allocation;
											break;
										}
									}
									if (marketAllocation != null) {
										setPandLentries(profitAndLoss, marketAllocation);
									}
								}
							}
						} else if (slot instanceof IDischargeOption) {
							final Slot<?> modelSlot = modelEntityMap.getModelObject(slot, Slot.class);

							OpenSlotAllocation openSlotAllocation = null;
							for (final OpenSlotAllocation allocation : outputSchedule.getOpenSlotAllocations()) {
								if (allocation.getSlot() == modelSlot) {
									openSlotAllocation = allocation;
									break;
								}
							}
							if (openSlotAllocation != null) {
								setPandLentries(profitAndLoss, openSlotAllocation);
							} else {
								MarketAllocation marketAllocation = null;
								for (final MarketAllocation allocation : outputSchedule.getMarketAllocations()) {
									if (allocation.getSlot() == modelSlot) {
										marketAllocation = allocation;
										break;
									}
								}
								if (marketAllocation != null) {
									setPandLentries(profitAndLoss, marketAllocation);
								}
							}
						} else if (slot instanceof IVesselEventPortSlot) {
							if (slot instanceof IGeneratedCharterOutVesselEventPortSlot) {
								final GeneratedCharterOut gco = portSlotEventProvider.getEventFromPortSlot(slot, GeneratedCharterOut.class);
								if (gco != null) {
									setPandLentries(profitAndLoss, gco);
								}
							} else if (slot instanceof IGeneratedCharterLengthEventPortSlot) {
								final GeneratedCharterLengthEvent gco = portSlotEventProvider.getEventFromPortSlot(slot, GeneratedCharterLengthEvent.class);
								if (gco != null) {
									setPandLentries(profitAndLoss, gco);
								}
							} else {
								
								final IPortSlot slotToFetch;
								if(slot instanceof IMaintenanceVesselEventPortSlot mSlot) {
									slotToFetch = mSlot.getFormerPortSlot();
								} else if(slot instanceof ICharterLengthEventPortSlot clSlot) {
									slotToFetch = charterLengthProvider.getOriginalCharterLengthSlot(clSlot);
								} else {
									slotToFetch = slot;
								}
								final com.mmxlabs.models.lng.cargo.VesselEvent modelEvent = modelEntityMap.getModelObject(slotToFetch, com.mmxlabs.models.lng.cargo.VesselEvent.class);
								VesselEventVisit visit = null;
								//
								for (final Sequence sequence : outputSchedule.getSequences()) {
									for (final Event event : sequence.getEvents()) {
										if (event instanceof VesselEventVisit) {
											VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
											if (vesselEventVisit.getVesselEvent() == modelEvent) {
												visit = vesselEventVisit;
											}
										}
									}
								}
								if(slot instanceof ICharterLengthEventPortSlot) {
									setPandLentries(profitAndLoss, visit);
								}
								if (visit != null) {
									
									IVesselEventPortSlot veps = (IVesselEventPortSlot) slot;
									final List<IProfitAndLossAnnotation> pnlAnnotations = new ArrayList<>(3);
									for (final ISequenceElement se : veps.getEventSequenceElements()) {
										final IProfitAndLossAnnotation pnl = annotatedSolution.getElementAnnotations().getAnnotation(se, SchedulerConstants.AI_profitAndLoss,
												IProfitAndLossAnnotation.class);
										if (pnl != null) {
											pnlAnnotations.add(pnl);
										}
									}
									setPandLentries(pnlAnnotations, visit);
									
								}
							}
						} else if (slot instanceof StartPortSlot) {
							final StartEvent startEvent = findStartEvent(vesselProvider, element);

							if (startEvent != null) {
								setPandLentries(profitAndLoss, startEvent);
							}
						} else if (slot.getPortType() == PortType.End) {
							final EndEvent endEvent = findEndEvent(element);

							if (endEvent != null) {
								setPandLentries(profitAndLoss, endEvent);
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
		return ExporterExtensionUtils.findEndEvent(element, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
	}

	private StartEvent findStartEvent(final IVesselProvider vesselProvider, final ISequenceElement element) {
		return ExporterExtensionUtils.findStartEvent(element, modelEntityMap, outputSchedule, annotatedSolution, vesselProvider);
	}
	
	private void setPandLentries(final Collection<IProfitAndLossAnnotation> profitAndLossCollection, final ProfitAndLossContainer container) {
		for (final IProfitAndLossAnnotation profitAndLoss : profitAndLossCollection) {
			setPandLentries(profitAndLoss, container);
		}
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
		final boolean gpnlExists = container.getGroupProfitAndLoss() != null;
		final GroupProfitAndLoss groupProfitAndLoss;
		if (!gpnlExists) {
			groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
		} else {
			groupProfitAndLoss = container.getGroupProfitAndLoss();
		}
		container.setGroupProfitAndLoss(groupProfitAndLoss);

		final Collection<IProfitAndLossEntry> entries = profitAndLoss.getEntries();

		final Map<BaseEntityBook, int[]> groupProfitMap = new HashMap<>();

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
			if (gpnlExists) {
				for (final EntityProfitAndLoss ePNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
					if (ePNL.getEntity().equals(e.getKey().eContainer()) && ePNL.getEntityBook().equals(e.getKey())) {
						ePNL.setProfitAndLoss(ePNL.getProfitAndLoss() + e.getValue()[0]);
						ePNL.setProfitAndLossPreTax(ePNL.getProfitAndLossPreTax() + e.getValue()[1]);
					}
				}
			} else {
				final EntityProfitAndLoss streamData = ScheduleFactory.eINSTANCE.createEntityProfitAndLoss();
				streamData.setEntity((BaseLegalEntity) e.getKey().eContainer());
				streamData.setEntityBook(e.getKey());
				final int groupValue = e.getValue()[0];
				final int groupValuePreTax = e.getValue()[1];
	
				streamData.setProfitAndLoss(groupValue);
				streamData.setProfitAndLossPreTax(groupValuePreTax);
	
				groupProfitAndLoss.getEntityProfitAndLosses().add(streamData);
			}
		}
		
		for (final EntityProfitAndLoss ePNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
			totalGroupValue += ePNL.getProfitAndLoss();
			totalGroupValuePreTax += ePNL.getProfitAndLossPreTax();
		}

		groupProfitAndLoss.setProfitAndLoss(totalGroupValue);
		groupProfitAndLoss.setProfitAndLossPreTax(totalGroupValuePreTax);
	}
}
