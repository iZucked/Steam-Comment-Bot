/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
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
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

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

			volumeAllocatedSequences.add(vesselAvailability, volumeAllocatedSequence);
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

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

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
			return desOrFobSchedule(resource, vesselAvailability, sequence, portTimesRecord, sequencesAttributesProvider, annotatedSolution);
		}

		return shippedSchedule(resource, vesselAvailability, sequence, records, sequencesAttributesProvider, annotatedSolution);
	}

	private VolumeAllocatedSequence shippedSchedule(final IResource resource, final IVesselAvailability vesselAvailability, final ISequence sequence, final @Nullable List<IPortTimesRecord> records,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {
		if (records == null || records.isEmpty()) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}

		final int vesselStartTime = records.get(0).getFirstSlotTime();

		// Generate all the voyage plans and extra annotations for this sequence
		final List<VoyagePlanRecord> voyagePlans = new LinkedList<>();

		ScheduledVoyagePlanResult lastResult = null;

		IPort firstLoadPort = getFirstLoadPort(records);

		for (int idx = 0; idx < records.size(); idx++) {

			final IPortTimesRecord portTimeWindowsRecord = records.get(idx);
			final boolean lastPlan = idx == records.size() - 1;

			final PreviousHeelRecord previousHeelRecord = lastResult == null ? new PreviousHeelRecord() : lastResult.endHeelState;

			final List<@NonNull ScheduledVoyagePlanResult> results = voyagePlanEvaluator.evaluateShipped(resource, vesselAvailability, //
					vesselAvailability.getCharterCostCalculator(), //
					vesselStartTime, //
					firstLoadPort, //
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

		//retainHeelUnrestrainedSequence(resource, vesselAvailability, records, annotatedSolution, vesselStartTime, voyagePlans, firstLoadPort);
		
		retainHeelPairwise(resource, vesselAvailability, records, annotatedSolution, vesselStartTime, voyagePlans, firstLoadPort);
		
		//retainHeelPOC(resource, vesselAvailability, records, annotatedSolution, vesselStartTime, voyagePlans, firstLoadPort);

		return new VolumeAllocatedSequence(resource, sequence, vesselStartTime, voyagePlans);
	}
	
	private class RetentionRecord{
		public final Pair<VoyagePlan, IPortTimesRecord> pair;
		public final IAllocationAnnotation annotation;
		public final boolean isShortLoad;
		public final IPortSlot load;
		public final IPortSlot discharge;
		public final int cv;
		public long retainedVolumem3;
		public long retainedValue;
		
		public RetentionRecord(Pair<VoyagePlan, IPortTimesRecord> pair, IAllocationAnnotation annotation, IPortSlot loadSlot, //
				IPortSlot dischargeSlot, int cv, boolean load) {
			this.pair = pair;
			this.annotation = annotation;
			this.load = loadSlot;
			this.discharge = dischargeSlot;
			this.cv = cv;
			this.isShortLoad = load;
		}
	}
	
	private class RetentionPair {
		public final VoyagePlanRecord first;
		public final VoyagePlanRecord second;
		public final TempCargoRecord fCargo;
		public final TempCargoRecord sCargo;
		public long value = 0;
		
		public RetentionPair(VoyagePlanRecord first, VoyagePlanRecord second) {
			this.first = first;
			this.second = second;
			
			final ICargoValueAnnotation firstCva = first.getCargoValueAnnotation();
			
			assert firstCva != null;
			
			fCargo = new TempCargoRecord(firstCva);
			
			final ICargoValueAnnotation secondCva = second.getCargoValueAnnotation();
			
			assert secondCva != null;
			
			sCargo = new TempCargoRecord(secondCva);
		}
		
		/**
		 * Checks that the pair might retain heel.
		 * The following conditions are checked:
		 *   1. Both Voyage Plan Records (VPR) are cargo records
		 *   2. The first VPR has non null @ICargoValueAnnotation
		 *   3. The first cargo's follow up slot is a load slot
		 *   4. There is no requirment to run dry on the first VPR's CVA
		 * @return
		 */
		private boolean validate() {
			if (!first.isCargoRecord() || !second.isCargoRecord()) {
				return false;
			}
			final IPortTimesRecord firstIptr = first.getPortTimesRecord();
			final ICargoValueAnnotation firstCva = first.getCargoValueAnnotation();
			
			assert firstCva != null;
			assert firstIptr.getReturnSlot() instanceof ILoadSlot;
			return firstCva.getRemainingHeelVolumeInM3() != 0;
		}
		
		
		
		// TODO: split each value allocation into methods
		// TODO: check which one (or which combinations are better)
		// TODO: accept the best combinations
		public boolean evaluate(final IVessel vessel) {
			if (validate()) {
				
				long fVesselCapacityMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getCargoCapacity(), fCargo.cv);
				long fVesselSafetyHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getSafetyHeel(), fCargo.cv);
				long sVesselCapacityMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getCargoCapacity(), sCargo.cv);
				long sVesselSafetyHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(vessel.getSafetyHeel(), sCargo.cv);
				// We can run out of the safety heel!
				long fVesselUsefulCapacityMMBTu = fVesselCapacityMMBTu;// - fVesselSafetyHeelMMBTu;
				long sVesselUsefulCapacityMMBTu = sVesselCapacityMMBTu;// - sVesselSafetyHeelMMBTu;
				
				value = 0;
				long fTransferredVolumeMMBTU = 0;
				long sTransferredVolumeMMBTU = 0;
				
				// phase 1 - check the spare capacity on the second cargo and extra volume that can be bought on the first cargo
				// determine spare capacity on second cargo
				long sSpareLoadMMBTu = sVesselUsefulCapacityMMBTu - sCargo.startHeelMMBTu - sCargo.scheduledLoadVolumeMMBTu;
				long sExtraDischargeMMBTu = Math.min(sCargo.maxDischargeMMBTu, sVesselUsefulCapacityMMBTu) - sCargo.scheduledDischargeVolumeMMBTu;
				long sSpareCapacityMMBTu = Math.min(sVesselUsefulCapacityMMBTu - sCargo.startHeelMMBTu - sCargo.scheduledLoadVolumeMMBTu, //
						Math.min(sCargo.maxDischargeMMBTu, sVesselUsefulCapacityMMBTu) - sCargo.scheduledDischargeVolumeMMBTu);
				long sSpareCapacityM3t10 = convertMMBTUtoM3t10(sSpareCapacityMMBTu, sCargo.cv);
				long s2fSpareCapacityMMBTu = convertM3t10toMMBTU(sSpareCapacityM3t10, fCargo.cv);

				long sSpareCapacityRemainderMMBTu = sSpareCapacityMMBTu;
				
				{
					// determine extra volume which can be bought on first cargo (valued with load price)
					long fExtraLoadVolumeMMBTu = Math.min(s2fSpareCapacityMMBTu, Math.min(fCargo.maxLoadMMBTu, fVesselUsefulCapacityMMBTu) - fCargo.scheduledLoadVolumeMMBTu);
					long fExtraLoadValue = 0;
					if (fExtraLoadVolumeMMBTu > 0) {
						
						long fExtraLoadVolumeM3t10 = convertMMBTUtoM3t10(fExtraLoadVolumeMMBTu, fCargo.cv);
						long fValue = Calculator.costFromVolume(fExtraLoadVolumeMMBTu, fCargo.loadPriceMMBTU);
						long f2sExtraLoadVolumeMMMBTu = convertM3t10toMMBTU(fExtraLoadVolumeM3t10, sCargo.cv);
						long sValue = Calculator.costFromVolume(f2sExtraLoadVolumeMMMBTu, sCargo.loadPriceMMBTU);
						fExtraLoadValue = sValue - fValue;
						
						// TODO: check the sign!
						if (fExtraLoadValue > 0 && (sCargo.scheduledLoadVolumeMMBTu - f2sExtraLoadVolumeMMMBTu >= sCargo.minLoadMMBTu)) {
							fCargo.scheduledLoadVolumeMMBTu += fExtraLoadVolumeMMBTu;
							
							fTransferredVolumeMMBTU += fExtraLoadVolumeMMBTu;
							sTransferredVolumeMMBTU += f2sExtraLoadVolumeMMMBTu;
							
							sCargo.scheduledLoadVolumeMMBTu -= f2sExtraLoadVolumeMMMBTu;
							
							//f2sExtraLoadVolumeMMMBTu.scheduledDischargeVolumeMMBTu += f2sExtraLoadVolumeMMMBTu;
							sSpareCapacityRemainderMMBTu -= f2sExtraLoadVolumeMMMBTu;
							value += fExtraLoadValue;
						}
					}


					// determine if short discharge is possible
					// get the spare capacity with under discharge
					long fSpareDischargeCapacityMMBTu = Math.min(fCargo.scheduledDischargeVolumeMMBTu - fCargo.minDischargeMMBTu, sSpareCapacityRemainderMMBTu);
					long fSpareDischargeValue = 0;

					if (fSpareDischargeCapacityMMBTu > 0) {
						
						long fSpareDischargeVolumeM3t10 = convertMMBTUtoM3t10(fSpareDischargeCapacityMMBTu, fCargo.cv);
						long fValue = Calculator.costFromVolume(fSpareDischargeCapacityMMBTu, fCargo.dischargePriceMMBTU);
						long f2sSpareDischargeVolumeMMMBTu = convertM3t10toMMBTU(fSpareDischargeVolumeM3t10, sCargo.cv);
						long sValue = Calculator.costFromVolume(f2sSpareDischargeVolumeMMMBTu, sCargo.dischargePriceMMBTU);

						fSpareDischargeValue = sValue - fValue;
						// TODO: check the sign!
						if (fSpareDischargeValue > 0 && (sCargo.scheduledDischargeVolumeMMBTu + f2sSpareDischargeVolumeMMMBTu <= Math.min(sCargo.maxDischargeMMBTu, sVesselUsefulCapacityMMBTu))) {
							fCargo.scheduledDischargeVolumeMMBTu -= fSpareDischargeCapacityMMBTu;
							fTransferredVolumeMMBTU += fSpareDischargeCapacityMMBTu;
							sTransferredVolumeMMBTU += f2sSpareDischargeVolumeMMMBTu;
							sCargo.scheduledDischargeVolumeMMBTu += f2sSpareDischargeVolumeMMMBTu;
							sSpareCapacityRemainderMMBTu -= f2sSpareDischargeVolumeMMMBTu;
							value += fSpareDischargeValue;
						}
					}
				}
				
				//===========PHASE 2=============
				long sNewSpareCapacityMMBTu = sCargo.scheduledLoadVolumeMMBTu - sCargo.minLoadMMBTu;
				long sNewSpareCapacityRemainderMMBTu = sNewSpareCapacityMMBTu;
				// determine
				{
					// determine extra volume which can be bought on first cargo (valued with load price)
//					long fExtraLoadVolumeMMBTu = Math.min(sNewSpareCapacityMMBTu, Math.min(fCargo.maxLoadMMBTu, fVesselUsefulCapacityMMBTu) - fCargo.scheduledLoadVolumeMMBTu);
//					long fExtraLoadValue = 0;
//					
//					if (fExtraLoadVolumeMMBTu > 0) {
//						
//						long fExtraLoadVolumeM3t10 = convertMMBTUtoM3t10(fExtraLoadVolumeMMBTu, fCargo.cv);
//						long fValue = Calculator.costFromVolume(fExtraLoadVolumeMMBTu, fCargo.loadPriceMMBTU);
//						long f2sExtraLoadVolumeMMMBTu = convertM3t10toMMBTU(fExtraLoadVolumeM3t10, sCargo.cv);
//						long sValue = Calculator.costFromVolume(f2sExtraLoadVolumeMMMBTu, sCargo.loadPriceMMBTU);
//						
//						fExtraLoadValue = sValue - fValue;
//						// TODO: check the sign!
//						if (fExtraLoadValue > 0 && (sCargo.scheduledLoadVolumeMMBTu - f2sExtraLoadVolumeMMMBTu >= sCargo.minLoadMMBTu)) {
//							fCargo.scheduledLoadVolumeMMBTu += fExtraLoadVolumeMMBTu;
//							fTransferredVolumeMMBTU += fExtraLoadVolumeMMBTu;
//							sTransferredVolumeMMBTU += f2sExtraLoadVolumeMMMBTu;
//							//TODO : in future - check the CV compa
//							sCargo.scheduledLoadVolumeMMBTu -= f2sExtraLoadVolumeMMMBTu;
//							sNewSpareCapacityRemainderMMBTu -= f2sExtraLoadVolumeMMMBTu;
//							value += fExtraLoadValue;
//						}
//					}


					// determine if short discharge is possible
					// get the spare capacity with under discharge
					long fSpareDischargeCapacityMMBTu = Math.min(fCargo.scheduledDischargeVolumeMMBTu - fCargo.minDischargeMMBTu, sNewSpareCapacityRemainderMMBTu);
					long fSpareDischargeValue = 0;

					if (fSpareDischargeCapacityMMBTu > 0) {
						
						// TODO: do that for all four
						long fSpareDischargeCapacityM3t10 = convertMMBTUtoM3t10(fSpareDischargeCapacityMMBTu, fCargo.cv);
						long fValue = Calculator.costFromVolume(fSpareDischargeCapacityMMBTu, fCargo.dischargePriceMMBTU);
						long f2sSpareDischargeCapacityMMMBTu = convertM3t10toMMBTU(fSpareDischargeCapacityM3t10, sCargo.cv);
						long sValue = Calculator.costFromVolume(f2sSpareDischargeCapacityMMMBTu, sCargo.loadPriceMMBTU);
						fSpareDischargeValue = sValue - fValue;

						//fSpareDischargeValue = Calculator.costFromVolume(fSpareDischargeCapacityMMBTu, sCargo.loadPriceMMBTU - fCargo.dischargePriceMMBTU);
						// TODO: check the sign!
						if (fSpareDischargeValue > 0 && (sCargo.scheduledLoadVolumeMMBTu - f2sSpareDischargeCapacityMMMBTu >= sCargo.minLoadMMBTu)) {
							fCargo.scheduledDischargeVolumeMMBTu -= fSpareDischargeCapacityMMBTu;
							fTransferredVolumeMMBTU += fSpareDischargeCapacityMMBTu;
							sTransferredVolumeMMBTU += f2sSpareDischargeCapacityMMMBTu;
							sCargo.scheduledLoadVolumeMMBTu -= f2sSpareDischargeCapacityMMMBTu;
							sNewSpareCapacityRemainderMMBTu -= f2sSpareDischargeCapacityMMMBTu;
							value += fSpareDischargeValue;
						}
					}
				}
				
				//use calculator
//				if ((fTransferredVolumeMMBTU < 500L * fCargo.cv) || (sTransferredVolumeMMBTU < 500L * sCargo.cv)) {
//					return false;
//				}
				fCargo.endHeelMMBTu += fTransferredVolumeMMBTU;
				sCargo.startHeelMMBTu += sTransferredVolumeMMBTU;
				// 
				
				
				return value > 0;
			}
			return false;
		}
		
		private long convertMMBTUtoM3t10(long volumeMMBTU, int cv) {
			return Calculator.convertMMBTuToM3WithOverflowProtection(volumeMMBTU *10, cv);
		}
		
		private long convertM3t10toMMBTU(long volumeM3t10, int cv) {
			return Calculator.convertM3ToMMBTuWithOverflowProtection(volumeM3t10, cv)/10;
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
				//scheduledLoadVolumeMMBTu = Math.max(cva.getCommercialSlotVolumeInMMBTu(load), cva.getPhysicalSlotVolumeInMMBTu(load));
				scheduledLoadVolumeMMBTu = cva.getCommercialSlotVolumeInMMBTu(load);
				loadBOGMMBTu = cva.getCommercialSlotVolumeInMMBTu(load) - cva.getPhysicalSlotVolumeInMMBTu(load);
				
			}
			assert cv != 0;
			if (discharge instanceof DischargeSlot ds) {
				minDischargeMMBTu = ds.getMinDischargeVolumeMMBTU(cv);
				maxDischargeMMBTu = ds.getMaxDischargeVolumeMMBTU(cv);
				// should be the same
				//scheduledDischargeVolumeMMBTu = Math.max(cva.getCommercialSlotVolumeInMMBTu(discharge), cva.getPhysicalSlotVolumeInMMBTu(discharge));
				assert cva.getCommercialSlotVolumeInMMBTu(discharge) == cva.getPhysicalSlotVolumeInMMBTu(discharge);
				scheduledDischargeVolumeMMBTu = cva.getCommercialSlotVolumeInMMBTu(discharge);
				dischargeBOGMMBTu = cva.getCommercialSlotVolumeInMMBTu(discharge) - cva.getPhysicalSlotVolumeInMMBTu(discharge);
			}
			
			loadPriceMMBTU = cva.getSlotPricePerMMBTu(load);
			dischargePriceMMBTU = cva.getSlotPricePerMMBTu(discharge);
			
			startHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(cva.getStartHeelVolumeInM3(), cv);
			endHeelMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(cva.getRemainingHeelVolumeInM3(), cv);
		}
		
		public int cv;
		public long minLoadMMBTu;
		public long maxLoadMMBTu;
		public long scheduledLoadVolumeMMBTu;
		public long loadBOGMMBTu;
		public int loadPriceMMBTU;
		
		public long minDischargeMMBTu;
		public long maxDischargeMMBTu;
		public long scheduledDischargeVolumeMMBTu;
		public long dischargeBOGMMBTu;
		public int dischargePriceMMBTU;
		
		public long startHeelMMBTu;
		public long endHeelMMBTu;
	}
	
	private IPortSlot getLoadSlot(final ICargoValueAnnotation cva) {
		IPortSlot result = null;
		
		for(final IPortSlot slot : cva.getSlots()) {
			if (slot instanceof LoadSlot ls) {
				return ls;
			}
		}
		
		assert result != null;
		return result;
	}
	
	private IPortSlot getDischargeSlot(final ICargoValueAnnotation cva) {
		IPortSlot result = null;
		
		for(final IPortSlot slot : cva.getSlots()) {
			if (slot instanceof DischargeSlot ds) {
				return ds;
			}
		}
		
		assert result != null;
		return result;
	}
	
	// Create a list of pairwise VPRs and keep only the ones where heel can be retained
	private void retainHeelPairwise(final IResource resource, final IVesselAvailability vesselAvailability, final List<IPortTimesRecord> records, 
			final @Nullable IAnnotatedSolution annotatedSolution,
			final int vesselStartTime, final List<VoyagePlanRecord> voyagePlans, @Nullable IPort firstLoadPort) {
	
		final List<RetentionPair> allPairs = new LinkedList();
		final List<Pair<VoyagePlan, IPortTimesRecord>> otherVoyagePlansAndPortTimeRecords = new LinkedList<>();
		final Map<VoyagePlanRecord, Pair<VoyagePlan, IPortTimesRecord>> mm = new HashMap<>();
		{
			VoyagePlanRecord lastRecord = null;
			Iterator<VoyagePlanRecord> iter = voyagePlans.iterator();
			while(iter.hasNext()) {
				final VoyagePlanRecord current = iter.next();
				if (current != null) {
					if (lastRecord != null) {
						if (current.isCargoRecord() && lastRecord.isCargoRecord()) {
							allPairs.add(new RetentionPair(lastRecord, current));
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
		
		final IVessel vessel = vesselAvailability.getVessel();
		
		
		final List<RetentionPair> foo = allPairs.stream().filter(r -> r.evaluate(vessel)).toList();
		
		if (foo != null) {
			
			final List<RetentionPair> pairsToKeep = new LinkedList<>();
			pairsToKeep.addAll(foo);

			if (!pairsToKeep.isEmpty()) {
				RetentionPair lastRecord = null;
				List<RetentionPair> toDelete = new LinkedList<>();

				for (final RetentionPair current : pairsToKeep) {
					if (lastRecord != null) {
						if (lastRecord.first != current.second) {
							if (lastRecord.value >= current.value) {
								toDelete.add(current);
								lastRecord = null;
							} else {
								toDelete.add(lastRecord);
								lastRecord = null;
							}
						}
					} else {
						lastRecord = current;
					}
				}
				if (!toDelete.isEmpty()) {
					pairsToKeep.removeAll(toDelete);
				}
			}

			if (!pairsToKeep.isEmpty()) {
				PortTimesRecord record = new PortTimesRecord();
				record.setSlotTime(records.get(0).getFirstSlot(), 0);

				for (final RetentionPair rp: pairsToKeep) {
					VoyagePlanRecord vpr1 = rp.first;
					VoyagePlanRecord vpr2 = rp.second;

					setFirst(rp.fCargo, vpr1, mm.get(vpr1));
					setSecond(rp.sCargo, vpr2, mm.get(vpr2));
				}

				final List<@NonNull ScheduledVoyagePlanResult> otherResults = new LinkedList<>();
				voyagePlanEvaluator.evaluateVoyagePlan(resource, vesselAvailability, vesselStartTime, firstLoadPort, new PreviousHeelRecord(), 
						record, false, true, annotatedSolution, otherResults).accept(otherVoyagePlansAndPortTimeRecords);

				voyagePlans.clear();
				voyagePlans.addAll(otherResults.get(0).voyagePlans);
			}
		}
		
	}
	
	// check the mmbtu to cv conversion against min and max
	private void setFirst(TempCargoRecord tcr, VoyagePlanRecord vpr, Pair<VoyagePlan, IPortTimesRecord> pair) {
		
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
		
		long endHeelM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.endHeelMMBTu * 10L + 5L, tcr.cv)/10L;
		temp.setRemainingHeelVolumeInM3(endHeelM3);
		pair.setSecond(temp);
	}
	
	private void setSecond(TempCargoRecord tcr, VoyagePlanRecord vpr, Pair<VoyagePlan, IPortTimesRecord> pair) {
		
		final ICargoValueAnnotation cva = vpr.getCargoValueAnnotation();
		assert cva != null;
		AllocationAnnotation temp = AllocationAnnotation.from(cva);
		
		long startHeelM3 = Calculator.convertMMBTuToM3WithOverflowProtection(tcr.startHeelMMBTu * 10L + 5L, tcr.cv)/10L;
		temp.setStartHeelVolumeInM3(startHeelM3);
		
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
		
		pair.setSecond(temp);
	}

	// WARNING: at the moment does not consider the in-port boil-off
	// WARNING: does not consider the custom contracts where price depends on a scheduled load/discharge volume
	private void retainHeelUnrestrainedSequence(final IResource resource, final IVesselAvailability vesselAvailability, final List<IPortTimesRecord> records, 
			final @Nullable IAnnotatedSolution annotatedSolution,
			final int vesselStartTime, final List<VoyagePlanRecord> voyagePlans, @Nullable IPort firstLoadPort) {
		PortTimesRecord record = new PortTimesRecord();
		record.setSlotTime(records.get(0).getFirstSlot(), 0);
		final List<@NonNull ScheduledVoyagePlanResult> otherResults = new LinkedList<>();

		final List<Pair<VoyagePlan, IPortTimesRecord>> otherVoyagePlansAndPortTimeRecords = new LinkedList<>();
		final List<RetentionRecord> possibleRetention = new LinkedList<>();
		
		// Cargoes we have:
		// 1. load max and discharge max
		// 2. max load is bigger than vessel capacity
		
		final IVessel vessel = vesselAvailability.getVessel();
		final long safetyHeelm3 = vessel.getSafetyHeel();
		final long cargoCapacitym3 = vessel.getCargoCapacity();
		
		// do the last record instead
		RetentionRecord lastRecord = null;
		long heelDiffm3 = 0;
		
		for (final VoyagePlanRecord vpr: voyagePlans) {
			final VoyagePlan vp = vpr.getVoyagePlan();
			final IPortTimesRecord iptr = vpr.getPortTimesRecord();
			Pair<VoyagePlan, IPortTimesRecord> pair = new Pair(vp, iptr);
			otherVoyagePlansAndPortTimeRecords.add(pair);
			if (vpr.isCargoRecord()) {
				
				ICargoValueAnnotation cva = vpr.getCargoValueAnnotation();
				
				int cv = -1;
				long loadMinm3 = 0;
				long loadMaxm3 = 0;
				long dischargeMinm3 = 0;
				long dischargeMaxm3 = 0;
				long startHeelm3 = cva.getStartHeelVolumeInM3();
				long realLoadVolumem3 = 0;
				long realDischargeVolumem3 = 0;
				IPortSlot discharge = getDischargeSlot(cva);
				IPortSlot load = getLoadSlot(cva);

				if (load instanceof LoadSlot ls) {
					cv = ls.getCargoCVValue();
					loadMinm3 = ls.getMinLoadVolume();
					loadMaxm3 = ls.getMaxLoadVolume();
					realLoadVolumem3 = Math.max(cva.getCommercialSlotVolumeInM3(load), cva.getPhysicalSlotVolumeInM3(load));
				}
				if (discharge instanceof DischargeSlot ds) {
					dischargeMinm3 = ds.getMinDischargeVolume(cv);
					dischargeMaxm3 = ds.getMaxDischargeVolume(cv);
					realDischargeVolumem3 = Math.max(cva.getCommercialSlotVolumeInM3(discharge), cva.getPhysicalSlotVolumeInM3(discharge));
				}

				assert cv != -1;	
				assert realLoadVolumem3 != 0;
				assert realDischargeVolumem3 != 0;
				//
				
				int loadPriceMMBTU = cva.getSlotPricePerMMBTu(load);
				int dischargePriceMMBTU = cva.getSlotPricePerMMBTu(discharge);
				
				
				if (iptr.getReturnSlot() instanceof ILoadSlot) {
					// check that there is no cooldown scheduled on the next cargo
					// and that there is something to retain
					if (cva.getRemainingHeelVolumeInM3() != 0 && realDischargeVolumem3 != dischargeMinm3) {
						// check that the minimum discharge allows us to 
						long retVolm3 = realDischargeVolumem3 - dischargeMinm3 - 1;
						long vm3 = realDischargeVolumem3 - retVolm3; //dischargeMinm3 + 1;
						if (dischargeMinm3 <= vm3 && retVolm3 > 500*1000) {
							RetentionRecord rr = new RetentionRecord(pair, cva, load, discharge, cv, true);
							rr.retainedVolumem3 = retVolm3;
							heelDiffm3 += retVolm3;
							long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(retVolm3, cv);
							long value = Calculator.costFromVolume(vmmbtu, dischargePriceMMBTU);
							if (dischargePriceMMBTU > loadPriceMMBTU) {
								rr.retainedValue = -value;
							} else {
								rr.retainedValue = value;
							}
							possibleRetention.add(rr);
							lastRecord = rr;
						}
					}
				} else {
					//check that we have anything retained
					if (lastRecord != null) {
						long volumeOnBoardm3 = startHeelm3 + realLoadVolumem3;
						long vm3 = volumeOnBoardm3 - heelDiffm3;
						if (vm3 >= loadMinm3 && vm3 <= loadMaxm3) {
							RetentionRecord rr = new RetentionRecord(pair, cva, load, discharge, cv, false);
							rr.retainedVolumem3 = heelDiffm3;
							long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(heelDiffm3, cv);
							long value = Calculator.costFromVolume(vmmbtu, loadPriceMMBTU);
							if (dischargePriceMMBTU > loadPriceMMBTU) {
								rr.retainedValue = value;
							} else {
								rr.retainedValue = -value;
							}
							// calculate volume and value
							possibleRetention.add(rr);
							heelDiffm3 = 0;
						} else {
							possibleRetention.remove(lastRecord);
							heelDiffm3 = 0;
						}
					}
				}
			
			} else {
				possibleRetention.remove(lastRecord);
				// breaks the possibleRetention
			}
		}
		
		{
			final List<RetentionRecord> toRemove = new LinkedList<>();
			final List<RetentionRecord> tempList = new LinkedList<>();
			
			Iterator<RetentionRecord> iter = possibleRetention.iterator();
			long rv = 0;
			while(iter.hasNext()) {
				final RetentionRecord rr = iter.next();
				if (rr != null) {
					tempList.add(rr);
					if (rr.isShortLoad) {
						rv += rr.retainedValue;
					} else {
						if (rr.retainedValue + rv < 0) {
							//delete the sequence
							toRemove.addAll(tempList);
							tempList.clear();
						}
						rv = 0;
					}
				}
			}
			
			if (!toRemove.isEmpty()) {
				possibleRetention.removeAll(toRemove);
			}
		}
		
		if (!possibleRetention.isEmpty()) {
			for(RetentionRecord rr : possibleRetention) {

				AllocationAnnotation temp = AllocationAnnotation.from(rr.annotation);

				if (rr.isShortLoad) {
					long cvm3 = temp.getCommercialSlotVolumeInM3(rr.discharge);
					long pvm3 = temp.getPhysicalSlotVolumeInM3(rr.discharge);
					long vm3 = Math.max(cvm3, pvm3);
					vm3 -= rr.retainedVolumem3;
					temp.setCommercialSlotVolumeInM3(rr.discharge, vm3);
					temp.setPhysicalSlotVolumeInM3(rr.discharge, vm3);
					long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, rr.cv);
					temp.setCommercialSlotVolumeInMMBTu(rr.discharge, vmmbtu);
					temp.setPhysicalSlotVolumeInMMBTu(rr.discharge, vmmbtu);

					temp.setRemainingHeelVolumeInM3(temp.getRemainingHeelVolumeInM3() + rr.retainedVolumem3);
				} else {
					temp.setStartHeelVolumeInM3(temp.getStartHeelVolumeInM3() + rr.retainedVolumem3);

					long cvm3 = temp.getCommercialSlotVolumeInM3(rr.load);
					long pvm3 = temp.getPhysicalSlotVolumeInM3(rr.load);
					long vm3 = Math.max(cvm3, pvm3);
					//short load instead of discharging more
					vm3 -= rr.retainedVolumem3;
					// check the capacity
					temp.setCommercialSlotVolumeInM3(rr.load, vm3);
					temp.setPhysicalSlotVolumeInM3(rr.load, vm3);
					long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, rr.cv);
					temp.setCommercialSlotVolumeInMMBTu(rr.load, vmmbtu);
					temp.setPhysicalSlotVolumeInMMBTu(rr.load, vmmbtu);
				}
				rr.pair.setSecond(temp);
			}

			voyagePlanEvaluator.evaluateVoyagePlan(resource, vesselAvailability, vesselStartTime, firstLoadPort, new PreviousHeelRecord(), 
					record, false, true, annotatedSolution, otherResults).accept(otherVoyagePlansAndPortTimeRecords);

			voyagePlans.clear();
			voyagePlans.addAll(otherResults.get(0).voyagePlans);
		}
	}
	
	private static final long retainedVolumeM3 = 2_500_000L;
	
	private void retainHeelPOC(final IResource resource, final IVesselAvailability vesselAvailability, final List<IPortTimesRecord> records, 
			final @Nullable IAnnotatedSolution annotatedSolution,
			final int vesselStartTime, final List<VoyagePlanRecord> voyagePlans, @Nullable IPort firstLoadPort) {
		PortTimesRecord record = new PortTimesRecord();
		record.setSlotTime(records.get(0).getFirstSlot(), 0);
		final List<@NonNull ScheduledVoyagePlanResult> otherResults = new LinkedList<>();

		final List<Pair<VoyagePlan, IPortTimesRecord>> otherVoyagePlansAndPortTimeRecords = new LinkedList<>();
		
		// re-alloc the volumes here
		long heelDiffm3 = 0;
		boolean hasChanges = false;
		long value = 0;
		AllocationAnnotation previous = null;
		Pair<VoyagePlan, IPortTimesRecord> prevPair = null;
		for (final VoyagePlanRecord vpr: voyagePlans) {
			final VoyagePlan vp = vpr.getVoyagePlan();
			final IPortTimesRecord iptr = vpr.getPortTimesRecord();

			Pair<VoyagePlan, IPortTimesRecord> pair = new Pair(vp, iptr);
			otherVoyagePlansAndPortTimeRecords.add(pair);
			if (vpr.isCargoRecord()) {
				ICargoValueAnnotation cva = vpr.getCargoValueAnnotation();
				assert cva != null;
				
				int cv = -1;
				long dvm3 = 0;
				IPortSlot discharge = getDischargeSlot(cva);
				IPortSlot load = getLoadSlot(cva);
				
				if (load instanceof LoadSlot ls) {
					cv = ls.getCargoCVValue();
				}
				if (discharge instanceof DischargeSlot ds) {
					dvm3 = ds.getMinDischargeVolume(cv);
				}
				
				int loadPriceMMBTU = cva.getSlotPricePerMMBTu(load);
				int dischargePriceMMBTU = cva.getSlotPricePerMMBTu(discharge);

				long cvm3 = 0;
				long pvm3 = 0;

				assert cv != -1;
				assert cva != null;
				AllocationAnnotation temp = AllocationAnnotation.from(cva);
				
				if (heelDiffm3 == 0 && iptr.getReturnSlot() instanceof ILoadSlot) {
					cvm3 = temp.getCommercialSlotVolumeInM3(discharge);
					pvm3 = temp.getPhysicalSlotVolumeInM3(discharge);
					long vm3 = Math.max(cvm3, pvm3);
					vm3 -= (retainedVolumeM3);
					if (dvm3 <= vm3 && temp.getRemainingHeelVolumeInM3() != 0) {
						heelDiffm3 += (retainedVolumeM3);
						temp.setCommercialSlotVolumeInM3(discharge, vm3);
						temp.setPhysicalSlotVolumeInM3(discharge, vm3);
						long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, cv);
						temp.setCommercialSlotVolumeInMMBTu(discharge, vmmbtu);
						temp.setPhysicalSlotVolumeInMMBTu(discharge, vmmbtu);
						
						temp.setRemainingHeelVolumeInM3(temp.getRemainingHeelVolumeInM3() + (retainedVolumeM3));
						//vpTemp.setRemainingHeelInM3(temp.getRemainingHeelVolumeInM3());
						// add into the list
						previous = temp;
						prevPair = pair;
						//pair.setSecond(temp);
						//hasChanges = true;
						long hdMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(heelDiffm3, cv);
						value -= Calculator.costFromVolume(hdMMBTu, dischargePriceMMBTU);
					}
				} else if (heelDiffm3 > 0) {
					temp.setStartHeelVolumeInM3(temp.getStartHeelVolumeInM3() + heelDiffm3);
					//vpTemp.setStartingHeelInM3(temp.getStartHeelVolumeInM3());
					
					cvm3 = temp.getCommercialSlotVolumeInM3(load);
					pvm3 = temp.getPhysicalSlotVolumeInM3(load);
					long vm3 = Math.max(cvm3, pvm3);
					//short load instead of discharging more
					vm3 -= heelDiffm3;
					// check the capacity
					
					temp.setCommercialSlotVolumeInM3(load, vm3);
					temp.setPhysicalSlotVolumeInM3(load, vm3);
					long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, cv);
					temp.setCommercialSlotVolumeInMMBTu(load, vmmbtu);
					temp.setPhysicalSlotVolumeInMMBTu(load, vmmbtu);
					// add into the list
					
					// reset the heel
					heelDiffm3 = 0;
					long hdMMBTu = Calculator.convertM3ToMMBTuWithOverflowProtection(heelDiffm3, cv);
					value += Calculator.costFromVolume(hdMMBTu, loadPriceMMBTU);
					if (previous != null && prevPair != null) {
						hasChanges = true;
						pair.setSecond(temp);
						prevPair.setSecond(previous);
						
						previous = null;
						prevPair = null;
					}
				}

			}
		}
		
		if (hasChanges) {
			voyagePlanEvaluator.evaluateVoyagePlan(resource, vesselAvailability, vesselStartTime, firstLoadPort, new PreviousHeelRecord(), 
					record, false, true, annotatedSolution, otherResults).accept(otherVoyagePlansAndPortTimeRecords);
	
			voyagePlans.clear();
			voyagePlans.addAll(otherResults.get(0).voyagePlans);
		}
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

	private VolumeAllocatedSequence desOrFobSchedule(final IResource resource, final IVesselAvailability vesselAvailability, final ISequence sequence, final @Nullable IPortTimesRecord portTimesRecord,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable final IAnnotatedSolution annotatedSolution) {

		if (portTimesRecord == null) {
			return VolumeAllocatedSequence.empty(resource, sequence);
		}

		final ScheduledVoyagePlanResult result = voyagePlanEvaluator.evaluateNonShipped(resource, vesselAvailability, //
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
