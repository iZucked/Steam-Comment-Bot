/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
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

		retainHeel(resource, vesselAvailability, records, annotatedSolution, vesselStartTime, voyagePlans, firstLoadPort);

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
	
	private class RetentionSequence {
		public final List<Pair<VoyagePlan, IPortTimesRecord>> seq = new LinkedList();
	}

	// WARNING: at the moment does not consider the in-port boil-off
	// WARNING: does not consider the custom contracts where price depends on a scheduled load/discharge volume
	private void retainHeel(final IResource resource, final IVesselAvailability vesselAvailability, final List<IPortTimesRecord> records, 
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
				IPortSlot discharge = null;
				IPortSlot load = null;
				for (IPortSlot ps : cva.getSlots()) {
					if (ps instanceof LoadSlot ls) {
						load = ls;
						cv = ls.getCargoCVValue();
						loadMinm3 = ls.getMinLoadVolume();
						loadMaxm3 = ls.getMaxLoadVolume();
						realLoadVolumem3 = Math.max(cva.getCommercialSlotVolumeInM3(load), cva.getPhysicalSlotVolumeInM3(load));
					}
					if (ps instanceof DischargeSlot ds) {
						discharge = ps;
						dischargeMinm3 = ds.getMinDischargeVolume(cv);
						dischargeMaxm3 = ds.getMaxDischargeVolume(cv);
						realDischargeVolumem3 = Math.max(cva.getCommercialSlotVolumeInM3(discharge), cva.getPhysicalSlotVolumeInM3(discharge));
					}
				}

				assert cv != -1;
				assert discharge != null;
				assert load != null;		
				assert realLoadVolumem3 != 0;
				assert realDischargeVolumem3 != 0;
				//
				
				int loadPriceMMBTU = cva.getSlotPricePerMMBTu(load);
				int dischargePriceMMBTU = cva.getSlotPricePerMMBTu(discharge);
				long loadValue = cva.getSlotValue(load);
				long dischargeValue = cva.getSlotValue(discharge);
				
				
				if (iptr.getReturnSlot() instanceof ILoadSlot) {
					// check that there is no cooldown scheduled on the next cargo
					// and that there is something to retain
					if (cva.getRemainingHeelVolumeInM3() != 0 && realDischargeVolumem3 != dischargeMinm3) {
						// check that the minimum discharge allows us to 
						long retVolm3 = realDischargeVolumem3 - dischargeMinm3 - 1;
						long vm3 = realDischargeVolumem3 - retVolm3;
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
						}
					}
				} else {
					//check that we have anything retained
					if (heelDiffm3 > 0) {
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
						}
					}
				}
			
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
		
//		// re-alloc the volumes here
//		long heelDiffm3 = 0;
//		for (final VoyagePlanRecord vpr: voyagePlans) {
//			final VoyagePlan vp = vpr.getVoyagePlan();
//			final IPortTimesRecord iptr = vpr.getPortTimesRecord();
//
//			Pair<VoyagePlan, IPortTimesRecord> pair = new Pair(vp, iptr);
//			//otherVoyagePlansAndPortTimeRecords.add(pair);
//			if (vpr.isCargoRecord()) {
//				IAllocationAnnotation cva = vpr.getAllocationAnnotation();
//				int cv = -1;
//				long dvm3 = 0;
//				IPortSlot discharge = null;
//				IPortSlot load = null;
//				for (IPortSlot ps : cva.getSlots()) {
//					if (ps instanceof LoadSlot ls) {
//						load = ls;
//						cv = ls.getCargoCVValue();
//					}
//					if (ps instanceof DischargeSlot ds) {
//						discharge = ps;
//						dvm3 = ds.getMinDischargeVolume(cv);
//					}
//				}
//				long cvm3 = 0;
//				long pvm3 = 0;
//
//				assert cv != -1;
//				assert discharge != null;
//				assert load != null;
//				AllocationAnnotation temp = AllocationAnnotation.from(cva);
//
////				VoyagePlan vpTemp = VoyagePlan.from(vp);
//
//				
//				if (heelDiffm3 == 0 && iptr.getReturnSlot() instanceof ILoadSlot) {
//					cvm3 = temp.getCommercialSlotVolumeInM3(discharge);
//					pvm3 = temp.getPhysicalSlotVolumeInM3(discharge);
//					long vm3 = Math.max(cvm3, pvm3);
//					vm3 -= (2500*1000);
//					if (dvm3 <= vm3 && temp.getRemainingHeelVolumeInM3() != 0) {
//						heelDiffm3 += (2500*1000);
//						temp.setCommercialSlotVolumeInM3(discharge, vm3);
//						temp.setPhysicalSlotVolumeInM3(discharge, vm3);
//						long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, cv);
//						temp.setCommercialSlotVolumeInMMBTu(discharge, vmmbtu);
//						temp.setPhysicalSlotVolumeInMMBTu(discharge, vmmbtu);
//						
//						temp.setRemainingHeelVolumeInM3(temp.getRemainingHeelVolumeInM3() + (2500*1000));
//						//vpTemp.setRemainingHeelInM3(temp.getRemainingHeelVolumeInM3());
//						// add into the list
////						pair.setBoth(vpTemp, temp);
//						pair.setSecond(temp);
//					}
//				} else if (heelDiffm3 > 0) {
//					temp.setStartHeelVolumeInM3(temp.getStartHeelVolumeInM3() + heelDiffm3);
//					//vpTemp.setStartingHeelInM3(temp.getStartHeelVolumeInM3());
//					
//					cvm3 = temp.getCommercialSlotVolumeInM3(load);
//					pvm3 = temp.getPhysicalSlotVolumeInM3(load);
//					long vm3 = Math.max(cvm3, pvm3);
//					//short load instead of discharging more
//					vm3 -= heelDiffm3;
//					// check the capacity
//					
//					temp.setCommercialSlotVolumeInM3(load, vm3);
//					temp.setPhysicalSlotVolumeInM3(load, vm3);
//					long vmmbtu = Calculator.convertM3ToMMBTuWithOverflowProtection(vm3, cv);
//					temp.setCommercialSlotVolumeInMMBTu(load, vmmbtu);
//					temp.setPhysicalSlotVolumeInMMBTu(load, vmmbtu);
//					// add into the list
////					pair.setBoth(vpTemp, temp);
//					pair.setSecond(temp);
//					// reset the heel
//					heelDiffm3 = 0;
//				}
//
//			}

			//			if (iptr instanceof AllocationAnnotation aa) {
			//				
			//			} else if (iptr instanceof CargoValueAnnotation cva) {
			//				// 1. Grab slots cva.getSlots();
			//				// 2. Get the min discharge on the dishcarge slot (max of commercial or physical volume)
			//				// 3. Compute the max load taking the start heel and vessel capacity in to account
			//				// 4. Set the discharge slot's discharge volumes
			//				// 5. Set the end heel
			//			} else if (iptr instanceof PortTimesRecord ptr) {
			//				
			//			}
//		}
		
		voyagePlanEvaluator.evaluateVoyagePlan(resource, vesselAvailability, vesselStartTime, firstLoadPort, new PreviousHeelRecord(), 
				record, false, true, annotatedSolution, otherResults).accept(otherVoyagePlansAndPortTimeRecords);

		voyagePlans.clear();
		voyagePlans.addAll(otherResults.get(0).voyagePlans);
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
