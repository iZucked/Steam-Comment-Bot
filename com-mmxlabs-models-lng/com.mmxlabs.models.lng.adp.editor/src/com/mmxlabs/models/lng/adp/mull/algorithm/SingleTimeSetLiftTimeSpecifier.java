package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class SingleTimeSetLiftTimeSpecifier implements ILiftTimeSpecifier {

	final boolean[] allowedLiftTimes = new boolean[24];

	public SingleTimeSetLiftTimeSpecifier(final List<Integer> allowedLiftHours) {
		if (allowedLiftHours.isEmpty()) {
			throw new IllegalStateException("Lift hours must be provided");
		}
		for (final int hour : allowedLiftHours) {
			if (hour < 0 || hour >= 24) {
				throw new IllegalStateException("Lift hours must be between 0 and 23");
			}
			allowedLiftTimes[hour] = true;
		}
	}

	@Override
	public boolean isValidLiftTime(final LocalDateTime potentialLiftTime) {
		return allowedLiftTimes[potentialLiftTime.getHour()];
	}
}
