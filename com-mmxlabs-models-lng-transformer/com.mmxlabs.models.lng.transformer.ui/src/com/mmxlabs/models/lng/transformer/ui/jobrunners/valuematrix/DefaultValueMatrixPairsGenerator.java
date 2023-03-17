package com.mmxlabs.models.lng.transformer.ui.jobrunners.valuematrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.Range;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;

public class DefaultValueMatrixPairsGenerator implements Iterable<Pair<Integer, Integer>> {

	private final int baseRangeStartInclusive;
	private final int baseRangeEndInclusive;
	private final int baseRangeStepSize;

	private final int swapRangeStartInclusive;
	private final int swapRangeEndInclusive;
	private final int swapRangeStepSize;

	public DefaultValueMatrixPairsGenerator(final @NonNull SwapValueMatrixModel model) {
		final Range baseRange = model.getParameters().getBasePriceRange();
		this.baseRangeStartInclusive = baseRange.getMin();
		this.baseRangeEndInclusive = baseRange.getMax();
		assert baseRangeStartInclusive <= baseRangeEndInclusive;
		this.baseRangeStepSize = baseRange.getStepSize();
		assert baseRangeStepSize > 0;

		final Range swapRange = model.getParameters().getSwapPriceRange();
		this.swapRangeStartInclusive = swapRange.getMin();
		this.swapRangeEndInclusive = swapRange.getMax();
		assert swapRangeStartInclusive <= swapRangeEndInclusive;
		this.swapRangeStepSize = swapRange.getStepSize();
		assert swapRangeStepSize > 0;
	}

	@Override
	public Iterator<Pair<Integer, Integer>> iterator() {
		return new Iterator<>() {
			private int nextBaseValue = baseRangeStartInclusive;
			private int nextSwapValue = swapRangeStartInclusive;

			@Override
			public boolean hasNext() {
				return nextSwapValue <= swapRangeEndInclusive;
			}

			@Override
			public Pair<Integer, Integer> next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				int baseValueToReturn = nextBaseValue;
				int swapValueToReturn = nextSwapValue;
				if (hasNext()) {
					int newBaseValue = nextBaseValue + baseRangeStepSize;
					if (newBaseValue > baseRangeEndInclusive) {
						nextSwapValue += swapRangeStepSize;
						nextBaseValue = baseRangeStartInclusive;
					} else {
						nextBaseValue = newBaseValue;
					}
				}
				return Pair.of(baseValueToReturn, swapValueToReturn);
			}

		};
	}
}
