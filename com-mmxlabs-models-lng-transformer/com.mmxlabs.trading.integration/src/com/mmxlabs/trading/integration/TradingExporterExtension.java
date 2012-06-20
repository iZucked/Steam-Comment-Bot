/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.ocl.ecore.opposites.AllInstancesContentAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.VesselEvent;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.trading.optimiser.TradingConstants;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.trading.optimiser.impl.Entity;

/**
 * EMF export side for trading optimiser information. Model may need reworking, so this isn't exactly final.
 * 
 * @author hinton
 * 
 */
public class TradingExporterExtension implements IExporterExtension {
	private final static Logger log = LoggerFactory.getLogger(TradingExporterExtension.class);
	private MMXRootObject inputScenario;
	private ModelEntityMap entities;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Override
	public void startExporting(final MMXRootObject inputScenario, final Schedule outputSchedule, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
		this.inputScenario = inputScenario;
		this.entities = entities;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
//		final EList<BookedRevenue> revenues = outputSchedule.getRevenue();
		final IPortSlotProvider slotProvider = annotatedSolution.getContext().getOptimisationData()
				.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		for (final ISequenceElement element : annotatedSolution.getContext().getOptimisationData().getSequenceElements()) {
			final IProfitAndLossAnnotation profitAndLoss = annotatedSolution.getElementAnnotations().getAnnotation(element, TradingConstants.AI_profitAndLoss, IProfitAndLossAnnotation.class);
			if (profitAndLoss != null) {
				// emit p&l entry - depends on the type of slot associated with the element.
				final IPortSlot slot = slotProvider.getPortSlot(element);

				if (slot instanceof ILoadSlot) {
					final Slot modelSlot = entities.getModelObject(slot, Slot.class);
					CargoAllocation cargoAllocation = null;
					for (final CargoAllocation allocation : outputSchedule.getCargoAllocations()) {
						if (allocation.getLoadAllocation().getSlot() == modelSlot || allocation.getDischargeAllocation().getSlot() == slot) {
							cargoAllocation = allocation;
							break;
						}
					}
					for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {
						final AdditionalData entityData = ScheduleFactory.eINSTANCE.createAdditionalData();
						entityData.setName(entry.getEntity().getName());
						entityData.setKey(entry.getEntity().getName());
						final AdditionalData pnlData = ScheduleFactory.eINSTANCE.createAdditionalData();
						pnlData.setValue((int)entry.getFinalGroupValue() / Calculator.ScaleFactor);
						pnlData.setName("P&L");
						pnlData.setKey("pnl");
						cargoAllocation.getAdditionalData().add(entityData);
						entityData.getAdditionalData().add(pnlData);
						
						final AdditionalData dateData = ScheduleFactory.eINSTANCE.createAdditionalData();
						dateData.setValue(entities.getDateFromHours(profitAndLoss.getBookingTime()));
						dateData.setName("Date");
						dateData.setKey("date");
						entityData.getAdditionalData().add(dateData);
						
						entityData.getAdditionalData().add(exportDetailTree(entry.getDetails()));
						
//						final CargoRevenue revenue = ScheduleFactory.eINSTANCE.createCargoRevenue();
//						revenue.setCargo(cargoAllocation);
//						revenue.setEntity(entities.getModelObject(entry.getEntity(), Entity.class));
//						revenue.setDate(entities.getDateFromHours(profitAndLoss.getBookingTime()));
//						revenue.setValue((int) (entry.getFinalGroupValue() / Calculator.ScaleFactor));
//						revenue.setDetails(DetailTreeExporter.exportDetail(entry.getDetails()));
//						revenues.add(revenue);
//						if (cargoAllocation.getLoadRevenue() == null) {
//							cargoAllocation.setLoadRevenue(revenue);
//						} else if (cargoAllocation.getShippingRevenue() == null) {
//							cargoAllocation.setShippingRevenue(revenue);
//						} else if (cargoAllocation.getDischargeRevenue() == null) {
//							cargoAllocation.setDischargeRevenue(revenue);
//						}
					}
				} else if (slot instanceof IVesselEventPortSlot) {
//					final VesselEvent modelEvent = entities.getModelObject(slot, VesselEvent.class);
//					VesselEventVisit visit = null;
//
//					for (final Sequence sequence : outputSchedule.getSequences()) {
//						for (final Event event : sequence.getEvents()) {
//							if (event instanceof VesselEventVisit) {
//								if (((VesselEventVisit) event).getVesselEvent() == modelEvent) {
//									visit = (VesselEventVisit) event;
//								}
//							}
//						}
//					}
//
//					for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {
//						final VesselEventRevenue revenue = ScheduleFactory.eINSTANCE.createVesselEventRevenue();
//						revenue.setEntity(entities.getModelObject(entry.getEntity(), Entity.class));
//						revenue.setDate(entities.getDateFromHours(profitAndLoss.getBookingTime()));
//						revenue.setVesselEventVisit(visit);
//						revenue.setValue((int) (entry.getFinalGroupValue() / Calculator.ScaleFactor));
//						revenue.setDetails(DetailTreeExporter.exportDetail(entry.getDetails()));
//						revenues.add(revenue);
//						visit.setRevenue(revenue);
//					}
				} else {
//					for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {
//						AdditionalData additionalData = ScheduleFactory.eINSTANCE.createAdditionalData();
//
//						final BookedRevenue revenue = ScheduleFactory.eINSTANCE.createBookedRevenue();
//						revenue.setEntity(entities.getModelObject(entry.getEntity(), Entity.class));
//						revenue.setDate(entities.getDateFromHours(profitAndLoss.getBookingTime()));
//						revenue.setValue((int) (entry.getFinalGroupValue() / Calculator.ScaleFactor));
//						revenue.setDetails(DetailTreeExporter.exportDetail(entry.getDetails()));
//						revenues.add(revenue);
//					}
				}
			}
		}

		// clear refs, just in case.
		inputScenario = null;
		entities = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

	private AdditionalData exportDetailTree(final IDetailTree details) {
		final AdditionalData ad = ScheduleFactory.eINSTANCE.createAdditionalData();
		
		ad.setName(details.getKey());
		ad.setKey(details.getKey());
		if (details.getValue() != null) {
			ad.setValue(details.getValue());
		}
		
		for (final IDetailTree child : details.getChildren()) {
			ad.getAdditionalData().add(exportDetailTree(child));
		}
		
		return ad;
	}
}
