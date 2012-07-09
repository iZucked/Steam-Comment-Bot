/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.IDetailTreeElement;
import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;
import com.mmxlabs.common.detailtree.impl.DurationDetailElement;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.trading.optimiser.TradingConstants;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.trading.optimiser.annotations.IProfitAndLossEntry;

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
		// final EList<BookedRevenue> revenues = outputSchedule.getRevenue();
		final IPortSlotProvider slotProvider = annotatedSolution.getContext().getOptimisationData().getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
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
					if (cargoAllocation != null) {
						int idx = 0;
						for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {

							// TODO: Keep idx in sync with ProfitAndLossAllocationComponent
							final ExtraData streamData;
							switch (idx) {
							case 0:
								streamData = cargoAllocation.addExtraData(TradingConstants.ExtraData_upstream, "Upstream");
								break;
							case 1:
								streamData = cargoAllocation.addExtraData(TradingConstants.ExtraData_shipped, "Shipping");
								break;
							case 2:
								streamData = cargoAllocation.addExtraData(TradingConstants.ExtraData_downstream, "Downstream");
								break;
							default:
								throw new IllegalStateException("Expected three item in the profit and loss list - got " + profitAndLoss.getEntries().size());
							}

							final ExtraData entityData = streamData.addExtraData(entry.getEntity().getName(), entry.getEntity().getName());

							final ExtraData pnlData = entityData.addExtraData(TradingConstants.ExtraData_pnl, "P&L", (int) (entry.getFinalGroupValue() / Calculator.ScaleFactor),
									ExtraDataFormatType.CURRENCY);

							pnlData.addExtraData("date", "Date", entities.getDateFromHours(profitAndLoss.getBookingTime()), ExtraDataFormatType.AUTO);
							ExtraData detail = exportDetailTree(entry.getDetails());
							detail.setName("Details");
							pnlData.getExtraData().add(detail);

							++idx;
						}
					}
				} else if (slot instanceof IVesselEventPortSlot) {
					// final VesselEvent modelEvent = entities.getModelObject(slot, VesselEvent.class);
					// VesselEventVisit visit = null;
					//
					// for (final Sequence sequence : outputSchedule.getSequences()) {
					// for (final Event event : sequence.getEvents()) {
					// if (event instanceof VesselEventVisit) {
					// if (((VesselEventVisit) event).getVesselEvent() == modelEvent) {
					// visit = (VesselEventVisit) event;
					// }
					// }
					// }
					// }
					//
					// for (final IProfitAndLossEntry entry : profitAndLoss.getEntries()) {
					// final VesselEventRevenue revenue = ScheduleFactory.eINSTANCE.createVesselEventRevenue();
					// revenue.setEntity(entities.getModelObject(entry.getEntity(), Entity.class));
					// revenue.setDate(entities.getDateFromHours(profitAndLoss.getBookingTime()));
					// revenue.setVesselEventVisit(visit);
					// revenue.setValue((int) (entry.getFinalGroupValue() / Calculator.ScaleFactor));
					// revenue.setDetails(DetailTreeExporter.exportDetail(entry.getDetails()));
					// revenues.add(revenue);
					// visit.setRevenue(revenue);
					// }
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

		// clear refs, just in case.
		inputScenario = null;
		entities = null;
		outputSchedule = null;
		annotatedSolution = null;
	}

	private ExtraData exportDetailTree(final IDetailTree details) {
		final ExtraData ad = TypesFactory.eINSTANCE.createExtraData();

		ad.setName(details.getKey());
		ad.setKey(details.getKey());

		Object value = details.getValue();
		if (value instanceof IDetailTreeElement) {
			IDetailTreeElement element = (IDetailTreeElement) value;
			value = element.getObject();
			if (element instanceof CurrencyDetailElement) {
				ad.setFormatType(ExtraDataFormatType.CURRENCY);
			} else if (element instanceof DurationDetailElement) {
				ad.setFormatType(ExtraDataFormatType.DURATION);
			}
		}

		if (value instanceof Serializable) {
			if (value instanceof Integer) {
				int x = (Integer) value;
				if (x % Calculator.ScaleFactor == 0) {
					ad.setValue(x / Calculator.ScaleFactor);
				} else {
					ad.setValue(x / (double) Calculator.ScaleFactor);
				}
			} else if (value instanceof Long) {
				long x = (Long) value;
				if (x % Calculator.ScaleFactor == 0) {
					ad.setValue((int) (x / Calculator.ScaleFactor));
				} else {
					ad.setValue(x / (double) Calculator.ScaleFactor);
				}
			} else {
				ad.setValue((Serializable) value);
			}
		}

		for (final IDetailTree child : details.getChildren()) {
			ad.getExtraData().add(exportDetailTree(child));
		}

		return ad;
	}
}
