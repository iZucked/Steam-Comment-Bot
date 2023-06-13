package com.mmxlabs.scheduler.optimiser.sequenceproviders;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;

@NonNullByDefault
public class PreSequencedSegmentProviderImpl implements IPreSequencedSegmentProvider {

	private final Map<ISequenceElement, ISequenceElement> preSequenceMap = new HashMap<>();

	public void setSequence(final ISequenceElement a, final ISequenceElement b) {
		if (preSequenceMap.containsKey(a)) {
			throw new IllegalArgumentException("Element already sequenced");
		}
		preSequenceMap.put(a, b);
	}

	@Override
	public boolean validSequence(final ISequenceElement a, final ISequenceElement b) {
		final ISequenceElement expectedB = preSequenceMap.get(a);
		return expectedB == null || expectedB == b;
	}
	
	@Override
	public boolean isPreSequenced(ISequenceElement a) {
		return preSequenceMap.containsKey(a);
	}
}
