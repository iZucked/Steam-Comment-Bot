/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord;

@NonNullByDefault
public class DateTimeConstrainedLiftTimeSpecifier implements ILiftTimeSpecifier {

	final NavigableMap<LocalDate, ILiftTimeSpecifier> allowedLiftTimes = new TreeMap<>();

	public DateTimeConstrainedLiftTimeSpecifier(final List<AllowedArrivalTimeRecord> allowedArrivalTimeRecords) {
		if (allowedArrivalTimeRecords.isEmpty()) {
			throw new IllegalStateException("Allowed times must be provided");
		}
		for (final AllowedArrivalTimeRecord allowedArrivalTimeRecord : allowedArrivalTimeRecords) {
			final ILiftTimeSpecifier wrappedLiftSpecifier;
			if (allowedArrivalTimeRecord.getAllowedTimes().isEmpty() || allowedArrivalTimeRecord.getAllowedTimes().size() == 24) {
				wrappedLiftSpecifier = new AnyTimeSpecifier();
			} else {
				wrappedLiftSpecifier = new SingleTimeSetLiftTimeSpecifier(allowedArrivalTimeRecord.getAllowedTimes());
			}
			final ILiftTimeSpecifier prevVal = allowedLiftTimes.put(allowedArrivalTimeRecord.getPeriodStart(), wrappedLiftSpecifier);
			if (prevVal != null) {
				throw new IllegalStateException("Cannot have different allowed times for same date");
			}
		}
	}

	@Override
	public boolean isValidLiftTime(final LocalDateTime potentialLiftTime) {
		final LocalDate dateToFetch = potentialLiftTime.toLocalDate();
		final Entry<LocalDate, ILiftTimeSpecifier> lowerAllowedArrivalTimes = allowedLiftTimes.floorEntry(dateToFetch);
		return lowerAllowedArrivalTimes == null || lowerAllowedArrivalTimes.getValue().isValidLiftTime(potentialLiftTime);
	}

}
