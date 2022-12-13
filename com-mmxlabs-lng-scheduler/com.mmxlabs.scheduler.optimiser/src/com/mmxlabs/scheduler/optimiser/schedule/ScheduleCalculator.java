/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotationsMap;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.IVoyagePlanEvaluator;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.ScheduledVoyagePlanResult;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.ICalculatorProvider;
import com.mmxlabs.scheduler.optimiser.providers.IHeelCarrySlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.scheduling.IArrivalTimeScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * The {@link ScheduleCalculator} performs {@link ScheduledSequences} wide
 * processing of a basic schedule - such as P&L calculations. This
 * implementation also perform the basic break-even analysis, charter out
 * generation and volume allocations then finally the overall P&L calculations.
 * 
 * 
 * @author Simon Goodall
 */
@NonNullByDefault
public class ScheduleCalculator {

	@Inject
	private IVoyagePlanEvaluator voyagePlanEvaluator;

	@Inject
	private ICalculatorProvider calculatorProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IArrivalTimeScheduler arrivalTimeScheduler;

	@Inject
	private LatenessChecker latenessChecker;
	
	@Inject
	@Named(SchedulerConstants.Key_UseHeelRetention)
	private boolean retainHeel;
	
	@Inject
	private IHeelCarrySlotProvider heelCarrySlotProvider;

	@Nullable
	public ProfitAndLossSequences schedule(final ISequences sequences, @Nullable final IAnnotatedSolution solution) {
		final Map<IResource, List<@NonNull IPortTimesRecord>> allPortTimeRecords = arrivalTimeScheduler.schedule(sequences);
		return schedule(sequences, allPortTimeRecords, solution);
	}

	@Nullable
	public ProfitAndLossSequences schedule(final ISequences sequences, final Map<IResource, List<IPortTimesRecord>> allPortTimeRecords, @Nullable final IAnnotatedSolution solution) {
		final ProfitAndLossSequences volumeAllocatedSequences = new ProfitAndLossSequences();

		for (final ISalesPriceCalculator shippingCalculator : calculatorProvider.getSalesPriceCalculators()) {
			shippingCalculator.prepareSalesForEvaluation(sequences);
		}

		// Prime the load price calculators with the scheduled result
		for (final ILoadPriceCalculator calculator : calculatorProvider.getLoadPriceCalculators()) {
			calculator.preparePurchaseForEvaluation(sequences);
		}

		final List<@NonNull IResource> resources = sequences.getResources();

		for (int i = 0; i < resources.size(); ++i) {
			final IResource resource = resources.get(i);
			final ISequence sequence = sequences.getSequence(resource);
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

			final List<@NonNull IPortTimesRecord> portTimeRecords = allPortTimeRecords.get(resource);
			// recompute the volumeAllocatedSequence and clump up the heel
			final VolumeAllocatedSequence volumeAllocatedSequence = doSchedule(resource, sequence, portTimeRecords, sequences.getProviders(), solution);

			if (volumeAllocatedSequence == null) {
				return null;
			}
			// Check for max duration lateness
			if (!volumeAllocatedSequence.getVoyagePlanRecords().isEmpty()) {
				latenessChecker.calculateLateness(volumeAllocatedSequence, solution);
			}

			volumeAllocatedSequences.add(vesselCharter, volumeAllocatedSequence);
		}

		if (solution != null) {
			annotate(volumeAllocatedSequences, solution);
		}

		return volumeAllocatedSequences;

	}

	/**
	 * Schedule an {@link ISequence} using the given array of arrivalTimes, indexed
	 * according to sequence elements. These times will be used as the base arrival
	 * time. However is some cases the time between elements may be too short (i.e.
	 * because the vessel is already travelling at max speed). In such cases, if
	 * adjustArrivals is true, then arrival times will be adjusted in the
	 * {@link VoyagePlan}s. Otherwise null will be returned.
	 * 
	 * @param resource
	 * @param sequence
	 * @param arrivalTimes Array of arrival times at each {@link ISequenceElement}
	 *                     in the {@link ISequence}
	 * @return
	 * @throws InfeasibleVoyageException
	 */

	private @Nullable VolumeAllocatedSequence doSchedule(final IResource resource, final ISequence sequence, final @Nullable List<IPortTimesRecord> records,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			@Nullable
			IPortTimesRecord portTimesRecord = null;
			if (records != null && !records.isEmpty()) {
				portTimesRecord = records.get(0);
			}

			// Virtual vessels are those operated by a third party, for FOB and DES
			// situations.
			// Should we compute a schedule for them anyway? The arrival times don't mean
			// much,
			// but contracts need this kind of information to make up numbers with.
			return desOrFobSchedule(resource, vesselCharter, sequence, portTimesRecord, sequencesAttributesProvider, annotatedSolution);
		}

		return shippedSchedule(resource, vesselCharter, sequence, records, sequencesAttributesProvider, annotatedSolution);
	}

	private VolumeAllocatedSequence shippedSchedule(final IResource resource, final IVesselCharter vesselCharter, final ISequence sequence, final @Nullable List<IPortTimesRecord> records,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {
		if (records == null || records.isEmpty()) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}

		final int vesselStartTime = records.get(0).getFirstSlotTime();

		// Generate all the voyage plans and extra annotations for this sequence
		final List<VoyagePlanRecord> voyagePlans = new LinkedList<>();

		ScheduledVoyagePlanResult lastResult = null;

		@Nullable IPort firstLoadPort = getFirstLoadPort(records);
		VesselStartState vesselStartState = new VesselStartState(vesselStartTime, firstLoadPort);
		
		for (int idx = 0; idx < records.size(); idx++) {

			final IPortTimesRecord portTimeWindowsRecord = records.get(idx);
			final boolean lastPlan = idx == records.size() - 1;

			final PreviousHeelRecord previousHeelRecord = lastResult == null ? new PreviousHeelRecord() : lastResult.endHeelState;

			final List<@NonNull ScheduledVoyagePlanResult> results = voyagePlanEvaluator.evaluateShipped(resource, vesselCharter, //
					vesselCharter.getCharterCostCalculator(), //
					vesselStartState, //
					previousHeelRecord, //
					portTimeWindowsRecord, //
					lastPlan, //
					false, // Return best
					true, // annotatedSolution != null, // Keep solutions for export
					sequencesAttributesProvider, annotatedSolution);

			assert !results.isEmpty();
			final ScheduledVoyagePlanResult result = results.get(0);
			voyagePlans.addAll(result.voyagePlans);

			lastResult = result;

		}
		
		if (retainHeel && vesselCharter.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			retainHeelPairwise(resource, vesselCharter, records, annotatedSolution, vesselStartState, voyagePlans);
		}

		return new VolumeAllocatedSequence(resource, sequence, vesselStartState.startTime(), voyagePlans);
	}
	
	@FunctionalInterface
	public interface HeelRetentionValueFunction {
	    long applyAsLong(boolean apply);
	}
	
	private class RetentionPair {
		private static final long ScaleFactor = 1000L;
		private static final long MinVolumeCutOff = 500L * ScaleFactor;
		public final VoyagePlanRecord first;
		public final VoyagePlanRecord second;
		public final TempCargoRecord fCargo;
		public final TempCargoRecord sCargo;
		private final long fVesselCapacityMMBTu;
		private final long sVesselCapacityMMBTu;
		public long firstCargoTransferredVolumeMMBTu = 0;
		public long secondCargoTransferredVolumeMMBTu = 0;
		public long value = 0;
		
		public RetentionPair(VoyagePlanRecord first, VoyagePlanRecord second, IVessel vessel) {
			this.first = first;
			this.second = second;
			
			final ICargoValueAnnotation firstCva = first.getCargoValueAnnotation();
			
			assert firstCva != null;
			
			fCargo = new TempCargoRecord(firstCva);
			
			final ICargoValueAnnotation secondCva = second.getCargoValueAnnotation();
			
			assert secondCva != null;
			
			sCargo = new TempCargoRecord(secondCva);
			
			fVesselCapacityMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getCargoCapacity(), fCargo.cv);
			sVesselCapacityMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getCargoCapacity(), sCargo.cv);
			
			heelRetentionFunctions.add(this::extraLoadFirstShortLoadSecond);
			heelRetentionFunctions.add(this::shortDischargeFirstExtraDischargeSecond);
			heelRetentionFunctions.add(this::extraLoadFirstExtraDischargeSecond);
			heelRetentionFunctions.add(this::shortDischargeFirstShortLoadSecond);
		}
		
		/**
		 * Checks that the pair might retain heel.
		 * The following conditions are checked:
		 *   1. Both Voyage Plan Records (VPR) are cargo records
		 *   2. The first VPR has non null @ICargoValueAnnotation
		 *   3. The first cargo's follow up slot is a load slot
		 *   4. There is no requirment to run dry on the first VPR's CVA
		 *   5. Second cargo's discharge slot must allow heel carry
		 * @return
		 */
		private boolean validate() {
			if (!first.isCargoRecord() || !second.isCargoRecord() || !allowsHeelCarry(sCargo.discharge)) {
				return false;
			}
			final IPortTimesRecord firstIptr = first.getPortTimesRecord();
			final ICargoValueAnnotation firstCva = first.getCargoValueAnnotation();
			
			assert firstCva != null;
			assert firstIptr.getReturnSlot() instanceof ILoadSlot;
			return firstCva.getRemainingHeelVolumeInM3() != 0;
		}
		
		List<HeelRetentionValueFunction> heelRetentionFunctions = new ArrayList<>(4);
		
		// check which one (or which combinations are better)
		// accept the best combinations
		public boolean evaluate() {
			if (validate()) {
				
				while (!heelRetentionFunctions.isEmpty()) {
					long bestValue = Long.MIN_VALUE;
					HeelRetentionValueFunction bestFunc = null;
					for (HeelRetentionValueFunction f : heelRetentionFunctions) {
						long currentValue = f.applyAsLong(false);
						if (bestFunc == null || currentValue > bestValue) {
							bestFunc = f;
							bestValue = currentValue;    
						}
					}
					if (bestFunc == null || bestValue == 0) {
						break;
					}

					bestFunc.applyAsLong(true);
					heelRetentionFunctions.remove(bestFunc);
					value += bestValue;
				}
				if ((firstCargoTransferredVolumeMMBTu > Calculator.convertM3ToMMBTu(MinVolumeCutOff, fCargo.cv)) //
						|| (secondCargoTransferredVolumeMMBTu > Calculator.convertM3ToMMBTu(MinVolumeCutOff, fCargo.cv))) {
					return value > 0;
				}
				
				return false;
			}
			return false;
		}
		
		private long extraLoadFirstShortLoadSecond(boolean apply) {
			long sSpareCapacityMMBTu = sCargo.scheduledLoadVolumeMMBTu - sCargo.minLoadMMBTu;
			if (sSpareCapacityMMBTu <= 0) {
				return 0L;
			}
			long sSpareCapacityM3t10 = convertMMBTUtoM3t10(sSpareCapacityMMBTu, sCargo.cv);
			long s2fSpareCapacityMMBTu = convertM3t10toMMBTu(sSpareCapacityM3t10, fCargo.cv);
			// determine extra volume which can be bought on first cargo (valued with load price)
			long fExtraLoadVolumeMMBTu = Math.min(s2fSpareCapacityMMBTu, //
					Math.min(fCargo.maxLoadMMBTu, fVesselCapacityMMBTu - fCargo.startHeelMMBTu ) - fCargo.scheduledLoadVolumeMMBTu);
			long fExtraLoadValue = 0;
			if (fExtraLoadVolumeMMBTu > 0) {
				
				long fExtraLoadVolumeM3t10 = convertMMBTUtoM3t10(fExtraLoadVolumeMMBTu, fCargo.cv);
				long fValue = Calculator.costFromVolume(fExtraLoadVolumeMMBTu, fCargo.loadPriceMMBTu);
				long f2sExtraLoadVolumeMMMBTu = convertM3t10toMMBTu(fExtraLoadVolumeM3t10, sCargo.cv);
				long sValue = Calculator.costFromVolume(f2sExtraLoadVolumeMMMBTu, sCargo.loadPriceMMBTu);
				fExtraLoadValue = sValue - fValue;
				
				assert fCargo.startHeelMMBTu + fCargo.scheduledLoadVolumeMMBTu + fExtraLoadVolumeMMBTu <= fVesselCapacityMMBTu;
				assert fCargo.scheduledLoadVolumeMMBTu + fExtraLoadVolumeMMBTu <= fCargo.maxLoadMMBTu;
				assert sCargo.scheduledLoadVolumeMMBTu - f2sExtraLoadVolumeMMMBTu >= sCargo.minLoadMMBTu;
				
				if (fExtraLoadValue > 0) {
					fCargo.scheduledLoadVolumeMMBTu += fExtraLoadVolumeMMBTu;
					fCargo.endHeelMMBTu += fExtraLoadVolumeMMBTu;
					firstCargoTransferredVolumeMMBTu += fExtraLoadVolumeMMBTu;
					sCargo.scheduledLoadVolumeMMBTu -= f2sExtraLoadVolumeMMMBTu;
					secondCargoTransferredVolumeMMBTu += f2sExtraLoadVolumeMMMBTu;
					sCargo.startHeelMMBTu += f2sExtraLoadVolumeMMMBTu;
				}
			}
			return fExtraLoadValue;
		}
		
		private long extraLoadFirstExtraDischargeSecond(boolean apply) {
			long sSpareCapacityMMBTu = Math.min(sCargo.maxDischargeMMBTu, sVesselCapacityMMBTu) - sCargo.scheduledDischargeVolumeMMBTu;
			sSpareCapacityMMBTu = Math.min(sSpareCapacityMMBTu, sVesselCapacityMMBTu - sCargo.startHeelMMBTu - sCargo.scheduledLoadVolumeMMBTu);
			if (sSpareCapacityMMBTu <= 0) {
				return 0L;
			}
			long sSpareCapacityM3t10 = convertMMBTUtoM3t10(sSpareCapacityMMBTu, sCargo.cv);
			long s2fSpareCapacityMMBTu = convertM3t10toMMBTu(sSpareCapacityM3t10, fCargo.cv);
			// determine extra volume which can be bought on first cargo (valued with load price)
			long fExtraLoadVolumeMMBTu = Math.min(s2fSpareCapacityMMBTu, //
					Math.min(fCargo.maxLoadMMBTu, fVesselCapacityMMBTu - fCargo.startHeelMMBTu) - fCargo.scheduledLoadVolumeMMBTu);
			long fExtraLoadValue = 0;
			if (fExtraLoadVolumeMMBTu > 0) {
				
				long fExtraLoadVolumeM3t10 = convertMMBTUtoM3t10(fExtraLoadVolumeMMBTu, fCargo.cv);
				long fValue = Calculator.costFromVolume(fExtraLoadVolumeMMBTu, fCargo.loadPriceMMBTu);
				long f2sExtraLoadVolumeMMMBTu = convertM3t10toMMBTu(fExtraLoadVolumeM3t10, sCargo.cv);
				long sValue = Calculator.costFromVolume(f2sExtraLoadVolumeMMMBTu, sCargo.loadPriceMMBTu);
				fExtraLoadValue = sValue - fValue;
				
				assert fCargo.startHeelMMBTu + fCargo.scheduledLoadVolumeMMBTu + fExtraLoadVolumeMMBTu <= fVesselCapacityMMBTu;
				assert fCargo.scheduledLoadVolumeMMBTu + fExtraLoadVolumeMMBTu <= fCargo.maxLoadMMBTu;
				assert sCargo.startHeelMMBTu + sCargo.scheduledLoadVolumeMMBTu + f2sExtraLoadVolumeMMMBTu <= sVesselCapacityMMBTu;
				assert sCargo.scheduledDischargeVolumeMMBTu + f2sExtraLoadVolumeMMMBTu <= Math.min(sCargo.maxDischargeMMBTu, sVesselCapacityMMBTu);
				
				if (fExtraLoadValue > 0) {
					fCargo.scheduledLoadVolumeMMBTu += fExtraLoadVolumeMMBTu;
					fCargo.endHeelMMBTu += fExtraLoadVolumeMMBTu;
					firstCargoTransferredVolumeMMBTu += fExtraLoadVolumeMMBTu;
					sCargo.scheduledDischargeVolumeMMBTu += f2sExtraLoadVolumeMMMBTu;
					secondCargoTransferredVolumeMMBTu += f2sExtraLoadVolumeMMMBTu;
					sCargo.startHeelMMBTu += f2sExtraLoadVolumeMMMBTu;
				}
			}
			return fExtraLoadValue;
		}
		
		private long shortDischargeFirstExtraDischargeSecond(boolean apply) {
			long sSpareCapacityMMBTu = Math.min(sCargo.maxDischargeMMBTu, sVesselCapacityMMBTu) - sCargo.scheduledDischargeVolumeMMBTu;
			sSpareCapacityMMBTu = Math.min(sSpareCapacityMMBTu, sVesselCapacityMMBTu - sCargo.startHeelMMBTu - sCargo.scheduledLoadVolumeMMBTu);
			if (sSpareCapacityMMBTu <= 0) {
				return 0L;
			}
			long sSpareCapacityM3t10 = convertMMBTUtoM3t10(sSpareCapacityMMBTu, sCargo.cv);
			long s2fSpareCapacityMMBTu = convertM3t10toMMBTu(sSpareCapacityM3t10, fCargo.cv);
			// determine if short discharge is possible
			// get the spare capacity with under discharge
			long fSpareDischargeCapacityMMBTu = Math.min(fCargo.scheduledDischargeVolumeMMBTu - fCargo.minDischargeMMBTu, //
					s2fSpareCapacityMMBTu);
			long fSpareDischargeValue = 0;

			if (fSpareDischargeCapacityMMBTu > 0) {
				
				long fSpareDischargeVolumeM3t10 = convertMMBTUtoM3t10(fSpareDischargeCapacityMMBTu, fCargo.cv);
				long fValue = Calculator.costFromVolume(fSpareDischargeCapacityMMBTu, fCargo.dischargePriceMMBTu);
				long f2sSpareDischargeVolumeMMMBTu = convertM3t10toMMBTu(fSpareDischargeVolumeM3t10, sCargo.cv);
				long sValue = Calculator.costFromVolume(f2sSpareDischargeVolumeMMMBTu, sCargo.dischargePriceMMBTu);

				fSpareDischargeValue = sValue - fValue;
				
				assert fCargo.scheduledDischargeVolumeMMBTu - fSpareDischargeCapacityMMBTu >= fCargo.minDischargeMMBTu;
				assert sCargo.startHeelMMBTu + sCargo.scheduledLoadVolumeMMBTu + f2sSpareDischargeVolumeMMMBTu <= sVesselCapacityMMBTu;
				assert sCargo.scheduledDischargeVolumeMMBTu + f2sSpareDischargeVolumeMMMBTu <= Math.min(sCargo.maxDischargeMMBTu, sVesselCapacityMMBTu);
				
				if (fSpareDischargeValue > 0) {
					fCargo.scheduledDischargeVolumeMMBTu -= fSpareDischargeCapacityMMBTu;
					fCargo.endHeelMMBTu += fSpareDischargeCapacityMMBTu;
					sCargo.startHeelMMBTu += f2sSpareDischargeVolumeMMMBTu;
					sCargo.scheduledDischargeVolumeMMBTu += f2sSpareDischargeVolumeMMMBTu;
					firstCargoTransferredVolumeMMBTu += fSpareDischargeCapacityMMBTu;
					secondCargoTransferredVolumeMMBTu += f2sSpareDischargeVolumeMMMBTu;
				}
			}
			return fSpareDischargeValue;
		}
		
		private long shortDischargeFirstShortLoadSecond(boolean apply) {
			long sSpareCapacityMMBTu = sCargo.scheduledLoadVolumeMMBTu - sCargo.minLoadMMBTu;
			if (sSpareCapacityMMBTu <= 0) {
				return 0L;
			}
			
			long sSpareCapacityM3t10 = convertMMBTUtoM3t10(sSpareCapacityMMBTu, sCargo.cv);
			long s2fSpareCapacityMMBTu = convertM3t10toMMBTu(sSpareCapacityM3t10, fCargo.cv);
			// determine if short discharge is possible
			// get the spare capacity with under discharge
			long fSpareDischargeCapacityMMBTu = Math.min(fCargo.scheduledDischargeVolumeMMBTu - fCargo.minDischargeMMBTu, //
					s2fSpareCapacityMMBTu);
			long fSpareDischargeValue = 0;

			if (fSpareDischargeCapacityMMBTu > 0) {
				long fSpareDischargeCapacityM3t10 = convertMMBTUtoM3t10(fSpareDischargeCapacityMMBTu, fCargo.cv);
				long fValue = Calculator.costFromVolume(fSpareDischargeCapacityMMBTu, fCargo.dischargePriceMMBTu);
				long f2sSpareDischargeCapacityMMMBTu = convertM3t10toMMBTu(fSpareDischargeCapacityM3t10, sCargo.cv);
				long sValue = Calculator.costFromVolume(f2sSpareDischargeCapacityMMMBTu, sCargo.loadPriceMMBTu);
				fSpareDischargeValue = sValue - fValue;

				assert sCargo.scheduledLoadVolumeMMBTu - f2sSpareDischargeCapacityMMMBTu >= sCargo.minLoadMMBTu;
				assert fCargo.scheduledDischargeVolumeMMBTu - fSpareDischargeCapacityMMBTu >= fCargo.minDischargeMMBTu;
				
				if (fSpareDischargeValue > 0) {
					fCargo.scheduledDischargeVolumeMMBTu -= fSpareDischargeCapacityMMBTu;
					fCargo.endHeelMMBTu += fSpareDischargeCapacityMMBTu;
					sCargo.startHeelMMBTu += f2sSpareDischargeCapacityMMMBTu;
					sCargo.scheduledLoadVolumeMMBTu -= f2sSpareDischargeCapacityMMMBTu;
					firstCargoTransferredVolumeMMBTu += fSpareDischargeCapacityMMBTu;
					secondCargoTransferredVolumeMMBTu += f2sSpareDischargeCapacityMMMBTu;
				}
			}
			return fSpareDischargeValue;
		}
		
		private long convertMMBTUtoM3t10(long volumeMMBTU, int cv) {
			return Calculator.convertMMBTuToM3(volumeMMBTU * 10, cv);
		}
		
		private long convertM3t10toMMBTu(long volumeM3t10, int cv) {
			return Calculator.convertM3ToMMBTu(volumeM3t10, cv) / 10;
		}
	}
	
	private class TempCargoRecord{
		public final IPortSlot load;
		public final IPortSlot discharge;
		
		public TempCargoRecord(ICargoValueAnnotation cva) {
			this.load = getLoadSlot(cva);
			this.discharge = getDischargeSlot(cva);
			
			if (load instanceof LoadSlot ls) {
				cv = ls.getCargoCVValue();
				minLoadMMBTu = ls.getMinLoadVolumeMMBTU();
				maxLoadMMBTu = ls.getMaxLoadVolumeMMBTU();
				// we need to choose which one to pick by checking the over-capacity flag (refill BOF while loading)
				scheduledLoadVolumeMMBTu = cva.getCommercialSlotVolumeInMMBTu(load);
				loadBOGMMBTu = cva.getCommercialSlotVolumeInMMBTu(load) - cva.getPhysicalSlotVolumeInMMBTu(load);
			}
			assert cv != 0;
			if (discharge instanceof DischargeSlot ds) {
				minDischargeMMBTu = ds.getMinDischargeVolumeMMBTU(cv);
				maxDischargeMMBTu = ds.getMaxDischargeVolumeMMBTU(cv);
				// should be the same
				assert cva.getCommercialSlotVolumeInMMBTu(discharge) - cva.getPhysicalSlotVolumeInMMBTu(discharge) == 0;
				scheduledDischargeVolumeMMBTu = cva.getCommercialSlotVolumeInMMBTu(discharge);
				dischargeBOGMMBTu = cva.getCommercialSlotVolumeInMMBTu(discharge) - cva.getPhysicalSlotVolumeInMMBTu(discharge);
			}
			
			loadPriceMMBTu = cva.getSlotPricePerMMBTu(load);
			dischargePriceMMBTu = cva.getSlotPricePerMMBTu(discharge);
			
			startHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(cva.getStartHeelVolumeInM3(), cv);
			endHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(cva.getRemainingHeelVolumeInM3(), cv);
		}
		
		public int cv;
		public long minLoadMMBTu;
		public long maxLoadMMBTu;
		public long scheduledLoadVolumeMMBTu;
		public long loadBOGMMBTu;
		public int loadPriceMMBTu;
		
		public long minDischargeMMBTu;
		public long maxDischargeMMBTu;
		public long scheduledDischargeVolumeMMBTu;
		public long dischargeBOGMMBTu;
		public int dischargePriceMMBTu;
		
		public long startHeelMMBTu;
		public long endHeelMMBTu;
		
		private IPortSlot getLoadSlot(final ICargoValueAnnotation cva) {
			for(final IPortSlot slot : cva.getSlots()) {
				if (slot instanceof LoadSlot ls) {
					return ls;
				}
			}
			
			throw new IllegalStateException("Cargo Value Annotation must have a Load Slot reference.");
		}
	}
	
	private IPortSlot getDischargeSlot(final @Nullable ICargoValueAnnotation cva) {
		if (cva != null) {
			for(final IPortSlot slot : cva.getSlots()) {
				if (slot instanceof DischargeSlot ds) {
					return ds;
				}
			}
		}
		throw new IllegalStateException("Cargo Value Annotation must have a Discharge Slot reference.");
	}
	
	private boolean allowsHeelCarry(final IPortSlot discharge) {
		return heelCarrySlotProvider.isHeelCarryAllowed(discharge);
	}
	
	// Create a list of pairwise VPRs and keep only the ones where heel can be retained
	private void retainHeelPairwise(final IResource resource, final IVesselCharter vesselCharter, final List<IPortTimesRecord> records, 
			final @Nullable IAnnotatedSolution annotatedSolution,
			final VesselStartState vesselStartState, final List<VoyagePlanRecord> voyagePlans) {
	
		final List<RetentionPair> allPairs = new LinkedList<>();
		final List<Pair<VoyagePlan, IPortTimesRecord>> otherVoyagePlansAndPortTimeRecords = new LinkedList<>();
		final Map<VoyagePlanRecord, Pair<VoyagePlan, IPortTimesRecord>> mm = new HashMap<>();
		// Creating a full set of retention pairs
		createOverlappingPairs(voyagePlans, allPairs, otherVoyagePlansAndPortTimeRecords, mm, vesselCharter.getVessel());
		
		// Filter out the pairs which are not valid or can not retain any heel
		final List<RetentionPair> nullablePairsToKeep = allPairs.stream().filter(r -> r.evaluate()).toList();
		
		if (nullablePairsToKeep != null) {
			
			// Remove the overlapping records
			final List<RetentionPair> pairsToKeep = removeOverlappingValidPairs(nullablePairsToKeep);

			if (!pairsToKeep.isEmpty()) {
				PortTimesRecord tempRecord = new PortTimesRecord();
				tempRecord.setSlotTime(records.get(0).getFirstSlot(), 0);

				// Apply changes from retention pair records to VoyagePlanRecord
				for (final RetentionPair rp: pairsToKeep) {
					VoyagePlanRecord vpr1 = rp.first;
					VoyagePlanRecord vpr2 = rp.second;

					setRecord(rp.fCargo, vpr1, mm.get(vpr1), true);
					setRecord(rp.sCargo, vpr2, mm.get(vpr2), false);
				}

				// Recompute the new voyage plan
				final List<@NonNull ScheduledVoyagePlanResult> otherResults = new LinkedList<>();
				voyagePlanEvaluator.evaluateVoyagePlan(resource, vesselCharter, vesselStartState, new PreviousHeelRecord(), 
						tempRecord, false, true, annotatedSolution, otherResults).accept(otherVoyagePlansAndPortTimeRecords);

				voyagePlans.clear();
				voyagePlans.addAll(otherResults.get(0).voyagePlans);
			}
		}
		
	}

	private List<RetentionPair> removeOverlappingValidPairs(final List<RetentionPair> nullablePairsToKeep) {
		final List<RetentionPair> pairsToKeep = new LinkedList<>();
		pairsToKeep.addAll(nullablePairsToKeep);
		
		if (!pairsToKeep.isEmpty()) {
            RetentionPair previousPair = null;
            List<RetentionPair> toDelete = new LinkedList<>();
            boolean usedLastPair = false;
            for (final RetentionPair currentPair : pairsToKeep) {
                if (previousPair == null) {
                    // Nothing special
                } else if (usedLastPair && previousPair.second == currentPair.first) {
                    usedLastPair = false;
                    toDelete.add(currentPair);
                } else if (previousPair.second == currentPair.first) {
                    if (previousPair.value >= currentPair.value) {
                        usedLastPair = false;
                        toDelete.add(currentPair);
                    } else {
                        usedLastPair = true;
                        toDelete.add(previousPair);
                    }
                }
                
                previousPair = currentPair;
               
            }
            if (!toDelete.isEmpty()) {
                pairsToKeep.removeAll(toDelete);
            }
        }
        return pairsToKeep;
	}

	private void createOverlappingPairs(final List<VoyagePlanRecord> voyagePlans, final List<RetentionPair> allPairs, //
			final List<Pair<VoyagePlan, IPortTimesRecord>> otherVoyagePlansAndPortTimeRecords,//
			final Map<VoyagePlanRecord, Pair<VoyagePlan, IPortTimesRecord>> mm,//
			final IVessel vessel) {
		{
			VoyagePlanRecord lastRecord = null;
			Iterator<VoyagePlanRecord> iter = voyagePlans.iterator();
			while(iter.hasNext()) {
				final VoyagePlanRecord current = iter.next();
				if (current != null) {
					if (lastRecord != null) {
						if (current.isCargoRecord() && lastRecord.isCargoRecord() && allowsHeelCarry(getDischargeSlot(current.getCargoValueAnnotation()))) {
							allPairs.add(new RetentionPair(lastRecord, current, vessel));
						}
					}
					lastRecord = current;
					
					final VoyagePlan vp = current.getVoyagePlan();
					final IPortTimesRecord iptr = current.getPortTimesRecord();

					Pair<VoyagePlan, IPortTimesRecord> pair = new Pair(vp, iptr);
					otherVoyagePlansAndPortTimeRecords.add(pair);
					mm.put(current, pair);
				}
			}
		}
	}
	
	private void setRecord(TempCargoRecord tcr, VoyagePlanRecord vpr, Pair<VoyagePlan, IPortTimesRecord> pair, boolean isFirst) {
		
		final ICargoValueAnnotation cva = vpr.getCargoValueAnnotation();
		assert cva != null;
		AllocationAnnotation temp = AllocationAnnotation.from(cva);
		
		temp.setCommercialSlotVolumeInMMBTu(tcr.load, tcr.scheduledLoadVolumeMMBTu);
		temp.setPhysicalSlotVolumeInMMBTu(tcr.load, tcr.scheduledLoadVolumeMMBTu - tcr.loadBOGMMBTu);
		long loadM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.scheduledLoadVolumeMMBTu + 5L, tcr.cv);
		long phLoadM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.scheduledLoadVolumeMMBTu - tcr.loadBOGMMBTu + 5L, tcr.cv);
		temp.setCommercialSlotVolumeInM3(tcr.load, loadM3);
		temp.setPhysicalSlotVolumeInM3(tcr.load, phLoadM3);
		
		temp.setCommercialSlotVolumeInMMBTu(tcr.discharge, tcr.scheduledDischargeVolumeMMBTu);
		temp.setPhysicalSlotVolumeInMMBTu(tcr.discharge, tcr.scheduledDischargeVolumeMMBTu - tcr.dischargeBOGMMBTu);
		long dischargeM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.scheduledDischargeVolumeMMBTu + 5L, tcr.cv);
		long phDischargeM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.scheduledDischargeVolumeMMBTu - tcr.dischargeBOGMMBTu + 5L, tcr.cv);
		temp.setCommercialSlotVolumeInM3(tcr.discharge, dischargeM3);
		temp.setPhysicalSlotVolumeInM3(tcr.discharge, phDischargeM3);
		temp.setHeelCarrySource(isFirst);
		temp.setHeelCarrySink(!isFirst);
		
		if (isFirst) {
			long endHeelM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.endHeelMMBTu * 10L + 5L, tcr.cv)/10L;
			temp.setRemainingHeelVolumeInM3(endHeelM3);
		} else {
			long startHeelM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.startHeelMMBTu * 10L + 5L, tcr.cv)/10L;
			temp.setStartHeelVolumeInM3(startHeelM3);
		}
		pair.setSecond(temp);
	}

	private @Nullable IPort getFirstLoadPort(@Nullable List<IPortTimesRecord> records) {
		if (records != null) {
			for (var rec : records) {
				if (rec.getFirstSlot() instanceof ILoadOption) {
					return rec.getFirstSlot().getPort();
				}
			}
		}
		return null;
	}

	/**
	 * This method replaces the normal shipped cargo calculation path with one
	 * specific to DES purchase or FOB sale cargoes. However this currently merges
	 * in behaviour from other classes - such as scheduling and volume allocation -
	 * which should really stay in those other classes.
	 * 
	 * @param resource
	 * @param sequence
	 * @return
	 */

	private VolumeAllocatedSequence desOrFobSchedule(final IResource resource, final IVesselCharter vesselCharter, final ISequence sequence, final @Nullable IPortTimesRecord portTimesRecord,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {

		if (portTimesRecord == null) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}

		final ScheduledVoyagePlanResult result = voyagePlanEvaluator.evaluateNonShipped(resource, vesselCharter, //
				portTimesRecord, //
				true, // annotatedSolution != null, // Keep solutions for export
				sequencesAttributesProvider, annotatedSolution);

		final int vesselStartTime = portTimesRecord.getFirstSlotTime();
		return new VolumeAllocatedSequence(resource, sequence, vesselStartTime, result.voyagePlans);
	}

	private void annotate(final ProfitAndLossSequences profitAndLossSequences, final IAnnotatedSolution annotatedSolution) {

		// now add some more data for each load slot
		final IElementAnnotationsMap elementAnnotations = annotatedSolution.getElementAnnotations();
		for (final VolumeAllocatedSequence scheduledSequence : profitAndLossSequences) {

			for (final VoyagePlanRecord vpr : scheduledSequence.getVoyagePlanRecords()) {
				final IAllocationAnnotation allocationAnnotation = vpr.getAllocationAnnotation();
				final ICargoValueAnnotation cargoValueAnnotation = vpr.getCargoValueAnnotation();

				for (final IPortSlot portSlot : vpr.getPortTimesRecord().getSlots()) {
					assert portSlot != null;

					final ISequenceElement portElement = portSlotProvider.getElement(portSlot);
					assert portElement != null;

					if (allocationAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_volumeAllocationInfo, allocationAnnotation);
					}
					if (cargoValueAnnotation != null) {
						elementAnnotations.setAnnotation(portElement, SchedulerConstants.AI_cargoValueAllocationInfo, cargoValueAnnotation);
					}
				}
			}
		}
	}
}
