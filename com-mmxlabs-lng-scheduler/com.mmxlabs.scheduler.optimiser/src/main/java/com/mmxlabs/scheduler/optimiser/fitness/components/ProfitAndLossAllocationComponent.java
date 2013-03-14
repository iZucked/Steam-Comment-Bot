/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IProfitAndLossDetails;

/**
 * Basic group P&L calculator.
 * 
 * TODO: should this be divided differently?
 * 
 * Maybe in the new design we want different P&L bits in different places, specifically charter out revenue.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossAllocationComponent extends AbstractPerRouteSchedulerFitnessComponent {
	private long accumulator = 0;

	// @Inject
	// private IEntityProvider entityProvider;
	//
	// private long lastEvaluation, lastAcceptance;
	//
	// @Inject
	// private IVesselProvider vesselProvider;
	//
	// @Inject
	// private IEntityValueCalculator entityValueCalculator;

	// private long calibrationZeroLine = Long.MIN_VALUE;

	/**
	 * @since 2.0
	 */
	public ProfitAndLossAllocationComponent(final String name, final CargoSchedulerFitnessCore core) {
		super(name, core);
	}

	//
	// @Override
	// protected boolean reallyStartSequence(IResource resource) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// protected boolean reallyEvaluateObject(Object object, int time) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	//
	// @Override
	// protected long endSequenceAndGetCost() {
	// // TODO Auto-generated method stub
	// return 0;
	// }

	// @Override
	// public void init(final IOptimisationData data) {
	//
	// }
	//
	// @Override
	// public void rejectLastEvaluation() {
	// }
	//
	// @Override
	// public void acceptLastEvaluation() {
	// lastAcceptance = lastEvaluation;
	// }
	//
	// @Override
	// public void dispose() {
	// entityProvider = null;
	// vesselProvider = null;
	// }
	//
	// @Override
	// public String getName() {
	// return name;
	// }
	//
	// @Override
	// public long getFitness() {
	// return lastEvaluation;
	// }
	//
	// @Override
	// public IFitnessCore getFitnessCore() {
	// return core;
	// }
	//
	// @Override
	// public long evaluate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations) {
	// return evaluateAndMaybeAnnotate(solution, allocations, null);
	// }
	//
	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		if (object instanceof IProfitAndLossDetails) {
			IProfitAndLossDetails details = (IProfitAndLossDetails) object;
			// Minimising optimiser, so negate P&L
			accumulator -= details.getTotalGroupProfitAndLoss();
		}
		return true;
	}

	// private long evaluateAndMaybeAnnotate(final ScheduledSequences solution, final Collection<IAllocationAnnotation> allocations, final IAnnotatedSolution annotatedSolution) {
	//
	// long accumulator = 0;
	// // VoyagePlanIterator vpi = new
	// // for (ScheduledSequence s : solution) {
	// //
	// // }
	// // {
	// // IProfitAndLossAnnotation annotation = annotatedSolution.getElementAnnotations().getAnnotation(element, SchedulerConstants.AI_profitAndLoss);
	// // if (annotation != null) {
	// // Collection<IProfitAndLossEntry> entries = annotation.getEntries();
	// // for (IProfitAndLossEntry e : entries) {
	// // accumulator += e.getFinalGroupValue();
	// // }
	// //
	// // }
	// // }
	// // annotatedSolution.getGeneralAnnotation(key, clz)
	// //
	// final Iterator<IAllocationAnnotation> allocationIterator = allocations.iterator();
	// IAllocationAnnotation currentAllocation = null;
	// if (allocationIterator.hasNext()) {
	// currentAllocation = allocationIterator.next();
	// }
	//
	// for (final ScheduledSequence sequence : solution) {
	// final IVessel vessel = vesselProvider.getVessel(sequence.getResource());
	//
	// int time = sequence.getStartTime();
	// for (final VoyagePlan plan : sequence.getVoyagePlans()) {
	// boolean cargo = false;
	// if (plan.getSequence().length >= 3) {
	//
	// PortDetails firstDetails = (PortDetails) plan.getSequence()[0];
	// PortDetails lastDetails = (PortDetails) plan.getSequence()[2];
	// if ((currentAllocation != null) && ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() ==
	// currentAllocation.getDischargeOption()))) {
	// cargo = true;
	// final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, sequence.getStartTime(), annotatedSolution);
	// accumulator += cargoGroupValue;
	// if (allocationIterator.hasNext()) {
	// currentAllocation = allocationIterator.next();
	// } else {
	// currentAllocation = null;
	// }
	// } else if ((vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) && plan.getSequence().length == 4) {
	// firstDetails = (PortDetails) plan.getSequence()[1];
	// lastDetails = (PortDetails) plan.getSequence()[2];
	// if ((currentAllocation != null) && ((firstDetails.getOptions().getPortSlot() == currentAllocation.getLoadOption()) && (lastDetails.getOptions().getPortSlot() ==
	// currentAllocation.getDischargeOption()))) {
	// cargo = true;
	// // TODO: Perhaps use the real slot time rather than always load?
	// // TODO: Does it matter really?
	// final long cargoGroupValue = entityValueCalculator.evaluate(plan, currentAllocation, vessel, currentAllocation.getLoadTime(), annotatedSolution);
	// accumulator += cargoGroupValue;
	// if (allocationIterator.hasNext()) {
	// currentAllocation = allocationIterator.next();
	// } else {
	// currentAllocation = null;
	// }
	// }
	// }
	// }
	//
	// if (!cargo) {
	// final long otherGroupValue = entityValueCalculator.evaluate(plan, vessel, time, sequence.getStartTime(), annotatedSolution);
	// accumulator += otherGroupValue;
	// }
	// time += getPlanDuration(plan);
	// }
	// }
	//
	// if (calibrationZeroLine == Long.MIN_VALUE) {
	// calibrationZeroLine = accumulator;
	// }
	//
	// lastEvaluation = (-accumulator) / Calculator.ScaleFactor;
	// return lastEvaluation;
	// }
	//
	// @Override
	// public void annotate(final ScheduledSequences solution, final IAnnotatedSolution annotatedSolution) {
	// if (entityProvider == null) {
	// return;
	// }
	// evaluateAndMaybeAnnotate(solution, solution.getAllocations(), annotatedSolution);
	// }
	//
	// private int getPlanDuration(final VoyagePlan plan) {
	// return getPartialPlanDuration(plan, 0);
	// }
	//
	// private int getPartialPlanDuration(final VoyagePlan plan, final int skip) {
	// int planDuration = 0;
	// final Object[] sequence = plan.getSequence();
	// final int k = sequence.length - skip;
	// for (int i = 0; i < k; i++) {
	// final Object o = sequence[i];
	// planDuration += (o instanceof VoyageDetails) ? (((VoyageDetails) o).getIdleTime() + ((VoyageDetails) o).getTravelTime()) : ((PortDetails) o).getOptions().getVisitDuration();
	// }
	// return planDuration;
	// }

	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator / Calculator.ScaleFactor;
	}
}
