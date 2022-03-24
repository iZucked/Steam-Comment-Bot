package com.mmxlabs.models.lng.adp.mull.container.manipulation;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;

@NonNullByDefault
public class FinalPhaseMullComparatorWrapper implements IMullComparatorWrapper {

	@Override
	public Comparator<IMudContainer> getComparator(LocalDateTime currentDateTime) {
		throw new IllegalStateException("Final phase should not need comparator");
	}


}
