package com.mmxlabs.scheduler.optmiser.fitness;

import java.util.Map;

import com.mmxlabs.optimiser.ISequence;

public final class SimpleSequenceScheduler<T> implements ISequenceScheduler<T> {

	private Map<T, SequenceSchedulerAdditionalInfo> additionalInfos;

	@Override
	public <U> U getAdditionalInformation(T element, String key, Class<U> clz) {
		if (additionalInfos.containsKey(element)) {
			return additionalInfos.get(element).get(key, clz);
		}

		return null;
	}

	@Override
	public int getEndTime(T element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStartTime(T element) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean schedule(ISequence<T> sequence) {
		// TODO Auto-generated method stub
		return false;
	}

}
