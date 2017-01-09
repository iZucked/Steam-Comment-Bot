/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.fitness.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.UnitCurve;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;

/**
 * A base class for fitness components which use a discount factor. Perhaps this should instead be a helper class outside the hierarchy, but it's lightweight, so not too much trouble to move if it
 * becomes a problem.
 * 
 * When a typical subclass is calculating its fitness, it should probably call {@link AbstractDiscountedFitnessComponent#resetAccumulator()} to clear the accumulator, and then during fitness
 * calculation it should call {@link AbstractDiscountedFitnessComponent#addDiscountedValue(int, long)} to add the different costs it sees at different times to the accumulator.
 * 
 * The total discounted fitness can then be recovered using {@link AbstractDiscountedFitnessComponent#getAccumulator()}; in the default implementation the {@link IFitnessComponent#getFitness()} method
 * just returns this value, but you may (indeed, probably will) want to override it instead.
 * 
 * Users of an {@link AbstractDiscountedFitnessComponent} can use the {@link AbstractDiscountedFitnessComponent#setDiscountCurve(ICurve)} method to provide a discount curve. If no discount curve is
 * set, the {@link UnitCurve} is used, which has the effect of making this class a dumb accumulator with a little overhead. It may also introduce numerical error, because the conversion is done with
 * floating-point arithmetic at the moment. Perhaps scaled integer arithmetic would be better.
 * 
 * TODO deal with MAX_VALUE
 * 
 * @author hinton
 * 
 */
public abstract class AbstractDiscountedFitnessComponent implements IFitnessComponent {

	@NonNull
	private ICurve discountCurve = UnitCurve.getInstance();

	private long accumulator;

	/**
	 * Returns the {@link ICurve} used for discounting this fitness function
	 * 
	 * @return
	 */
	@NonNull
	public ICurve getDiscountCurve() {
		return discountCurve;
	}

	/**
	 * Sets the {@link ICurve} used for discounting in this fitness function
	 * 
	 * @param discountCurve
	 */
	public void setDiscountCurve(@NonNull final ICurve discountCurve) {
		this.discountCurve = discountCurve;
	}

	/**
	 * Returns the current value of the field accumulating discounted fitness
	 * 
	 * @return
	 */
	protected final long getAccumulator() {
		return accumulator;
	}

	/**
	 * Sets the current total discounted fitness
	 * 
	 * @param accumulatorValue
	 */
	protected final void setAccumulator(final long accumulatorValue) {
		accumulator = accumulatorValue;
	}

	/**
	 * equivalent to setAccumulator(0)
	 */
	protected final void resetAccumulator() {
		setAccumulator(0);
	}

	/**
	 * Returns the discounted amount for deltaFitness at the given time
	 * 
	 * @param time
	 * @param deltaFitness
	 * @return
	 */
	protected final long getDiscountedValue(final int time, final long deltaFitness) {
		// return deltaFitness;
		return (long) (discountCurve.getValueAtPoint(time) * deltaFitness);
	}

	/**
	 * Adds the given fitness amount, discounted at the given time, to the accumulator
	 * 
	 * @param time
	 * @param deltaFitness
	 */
	protected final void addDiscountedValue(final int time, final long deltaFitness) {
		accumulator += getDiscountedValue(time, deltaFitness);
	}

	@Override
	public long getFitness() {
		return getAccumulator();
	}
}
