/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.tradingexporter;

import java.util.Collection;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.parseutils.IExposuresCustomiser;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.export.ExporterExtensionUtils;
import com.mmxlabs.models.lng.transformer.export.IExporterExtension;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.ICancellationAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IMiscCostsAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class BasicSlotPNLExporterExtension implements IExporterExtension {
	private ModelEntityMap modelEntityMap;
	private IAnnotatedSolution annotatedSolution;
	private Schedule outputSchedule;

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IOptimisationData optimisationData;
	
	@Inject
	private IExposuresCustomiser exposureCustomiser;
	
	@Inject
	private LNGScenarioModel lngScenarioModel;

	@Inject
	private ExporterExtensionUtils exporterExtensionUtils;

	@Override
	public void startExporting(final Schedule outputSchedule, final ModelEntityMap modelEntityMap, final IAnnotatedSolution annotatedSolution) {
		this.modelEntityMap = modelEntityMap;
		this.annotatedSolution = annotatedSolution;
		this.outputSchedule = outputSchedule;
	}

	@Override
	public void finishExporting() {
		for (final ISequenceElement element : optimisationData.getSequenceElements()) {
			assert element != null;
			final IPortSlot slot = slotProvider.getPortSlot(element);

			if (slot instanceof ILoadOption || slot instanceof IDischargeOption) {
				final Slot modelSlot = modelEntityMap.getModelObjectNullChecked(slot, Slot.class);

				final ProfitAndLossContainer profitAndLossContainer = exporterExtensionUtils.findProfitAndLossContainer(element, slot, modelEntityMap, outputSchedule, annotatedSolution);

				if (profitAndLossContainer != null) {

					final ICargoValueAnnotation cargoValueAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_cargoValueAllocationInfo,
							ICargoValueAnnotation.class);

					final IMiscCostsAnnotation miscCostsAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_miscCostsValue, IMiscCostsAnnotation.class);
					final ICancellationAnnotation cancellationAnnotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_cancellationFees,
							ICancellationAnnotation.class);
					if (cancellationAnnotation != null || cargoValueAnnotation != null || miscCostsAnnotation != null) {
						final BasicSlotPNLDetails details = ScheduleFactory.eINSTANCE.createBasicSlotPNLDetails();

						if (cargoValueAnnotation != null) {
							details.setAdditionalPNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalOtherPNL(slotProvider.getPortSlot(element))));
							details.setExtraUpsidePNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalUpsidePNL(slotProvider.getPortSlot(element))));
							details.setExtraShippingPNL(OptimiserUnitConvertor.convertToExternalFixedCost(cargoValueAnnotation.getSlotAdditionalShippingPNL(slotProvider.getPortSlot(element))));
						}
						if (miscCostsAnnotation != null) {
							details.setMiscCostsValue(OptimiserUnitConvertor.convertToExternalFixedCost(miscCostsAnnotation.getMiscCostsValue()));
						}
						if (cancellationAnnotation != null) {
							details.setCancellationFees(OptimiserUnitConvertor.convertToExternalFixedCost(cancellationAnnotation.getCancellationFees()));
						}
						final String referenceCurves = getCurves(modelSlot);
						details.setReferenceCurves(referenceCurves);
						ExporterExtensionUtils.addSlotPNLDetails(profitAndLossContainer, modelSlot, details);
					}
				}
			}
		}
		// clear refs, just in case.
		modelEntityMap = null;
		outputSchedule = null;
		annotatedSolution = null;
	}
	
	public String getCurves(final Slot<?> slot) {

		if (lngScenarioModel != null) {
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(lngScenarioModel);
			final ModelMarketCurveProvider mmCurveProvider = new ModelMarketCurveProvider(pricingModel);

			if (slot != null) {
				String priceExpression = exposureCustomiser.provideExposedPriceExpression(slot);

				final Collection<AbstractYearMonthCurve> curves = mmCurveProvider.getLinkedCurves(priceExpression);

				final StringBuilder results = new StringBuilder();
				for (AbstractYearMonthCurve curve : curves) {
					if (curve instanceof CommodityCurve) {
						results.append(',').append(curve.getName());
					}
				}
				if (results.length() != 0){
					return results.deleteCharAt(0).toString();
				}
			}
		}

		return "N/A";
	}
}
