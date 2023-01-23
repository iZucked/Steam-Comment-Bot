/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadOption;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketLoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.MarkToMarketVesselCharter;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.exposures.OptimiserExposureRecords;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.paperdeals.PaperDealsCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IMarkToMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ProfitAndLossCalculator} performs {@link ISequences} wide processing of P&L calculations.
 * 
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public class ProfitAndLossCalculator {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject(optional = true)
	private @Nullable IMarkToMarketProvider markToMarketProvider;

	@Inject
	private PaperDealsCalculator paperDealsCalculator;

	@Inject
	@Named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)
	private boolean generatedPapersInPNL;

	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;

	@Nullable
	public ProfitAndLossSequences calculateProfitAndLoss(final ISequences sequences, final ProfitAndLossSequences profitAndLossSequences, final @Nullable IAnnotatedSolution annotatedSolution) {

		// Some tests cases have no EVC
		if (entityValueCalculator == null) {
			return profitAndLossSequences;
		}

		// Calculating the paper deals allocations and exposures
		final Map<IPortSlot, OptimiserExposureRecords> exposuresMap = new HashMap<>();
		if (annotatedSolution != null || generatedPapersInPNL || exposuresEnabled) {
			for (VolumeAllocatedSequence s : profitAndLossSequences) {
				for (VoyagePlanRecord vpr : s.getVoyagePlanRecords()) {
					for (IPortSlot portSlot : vpr.getPortTimesRecord().getSlots()) {
						OptimiserExposureRecords records = vpr.getPortExposureRecord(portSlot);
						if (records != null) {
							exposuresMap.put(portSlot, records);
						}
					}
				}
			}

		}
		// Calculating the paper deals allocations and exposures
		if (annotatedSolution != null || generatedPapersInPNL) {
			profitAndLossSequences.setPaperDealRecords(paperDealsCalculator.processPaperDeals(exposuresMap));
		}
		//

		calculateUnusedSlotPNL(sequences, profitAndLossSequences, annotatedSolution);

		if (annotatedSolution != null && markToMarketProvider != null) {
			// calculateMarkToMarketPNL(sequences, annotatedSolution);
		}

		return profitAndLossSequences;
	}

	protected void calculateUnusedSlotPNL(final ISequences sequences, final ProfitAndLossSequences profitAndLossSequences, @Nullable final IAnnotatedSolution annotatedSolution) {
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			if (portSlot instanceof ILoadOption || portSlot instanceof IDischargeOption) {
				// Calculate P&L
				final long groupValue = entityValueCalculator.evaluateUnusedSlot(portSlot, annotatedSolution);
				profitAndLossSequences.setUnusedSlotGroupValue(portSlot, groupValue);
			}
		}
	}

	protected void calculateMarkToMarketPNL(final ISequences sequences, final IAnnotatedSolution annotatedSolution) {
		// Mark-to-Market Calculations
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			final IMarkToMarket market = markToMarketProvider.getMarketForElement(element);
			if (market == null) {
				continue;
			}

			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

			final ILoadOption loadOption;
			final IDischargeOption dischargeOption;
			final int time;

			final IVesselCharter vesselCharter;
			if (portSlot instanceof ILoadOption) {
				loadOption = (ILoadOption) portSlot;
				if (loadOption instanceof ILoadSlot) {
					dischargeOption = new MarkToMarketDischargeOption(market, loadOption);
				} else {
					dischargeOption = new MarkToMarketDischargeSlot(market, loadOption);
				}
				@Nullable
				ITimeWindow timeWindow = loadOption.getTimeWindow();
				assert timeWindow != null;
				time = timeWindow.getInclusiveStart();
				vesselCharter = new MarkToMarketVesselCharter(market, loadOption);
			} else if (portSlot instanceof IDischargeOption) {
				dischargeOption = (IDischargeOption) portSlot;
				if (dischargeOption instanceof IDischargeSlot) {
					loadOption = new MarkToMarketLoadOption(market, dischargeOption);
				} else {
					loadOption = new MarkToMarketLoadSlot(market, dischargeOption);
				}
				@Nullable
				ITimeWindow timeWindow = dischargeOption.getTimeWindow();
				assert timeWindow != null;
				time = timeWindow.getInclusiveStart();
				vesselCharter = new MarkToMarketVesselCharter(market, dischargeOption);
			} else {
				continue;
			}

			final PortTimesRecord portTimesRecord = new PortTimesRecord();
			portTimesRecord.setSlotTime(loadOption, time);
			portTimesRecord.setSlotTime(dischargeOption, time);
			portTimesRecord.setSlotDuration(loadOption, 0);
			portTimesRecord.setSlotDuration(dischargeOption, 0);

			// Create voyage plan
			final VoyagePlan voyagePlan = new VoyagePlan();
			{
				final PortOptions loadOptions = new PortOptions(loadOption);
				final PortDetails loadDetails = new PortDetails(loadOptions);
				loadOptions.setVisitDuration(0);

				final PortOptions dischargeOptions = new PortOptions(dischargeOption);
				final PortDetails dischargeDetails = new PortDetails(dischargeOptions);
				dischargeOptions.setVisitDuration(0);

				voyagePlan.setSequence(new IDetailsSequenceElement[] { loadDetails, dischargeDetails });
			}
			voyagePlan.setIgnoreEnd(false);
			// Create an allocation annotation.
			final IAllocationAnnotation allocationAnnotation = volumeAllocator.allocate(vesselCharter, voyagePlan, portTimesRecord, annotatedSolution);
			if (allocationAnnotation != null) {
				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
				// Calculate P&L
				final Pair<@NonNull CargoValueAnnotation, @NonNull Long> p = entityValueCalculator.evaluate(voyagePlan, allocationAnnotation, vesselCharter, null, annotatedSolution);
				final ICargoValueAnnotation cargoValueAnnotation = p.getFirst();

				annotatedSolution.getElementAnnotations().setAnnotation(element, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
			}
		}
	}
}
