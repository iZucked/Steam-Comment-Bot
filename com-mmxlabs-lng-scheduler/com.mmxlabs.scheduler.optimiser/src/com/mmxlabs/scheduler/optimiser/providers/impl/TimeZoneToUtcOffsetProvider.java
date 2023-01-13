/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * 
 * Helper class that provides time offsets for translating local times to UTC.
 * 
 * There are two main functions:
 * 
 * UTC(localtime, timezoneId):long which translates a local time in the given
 * timezone to UTC
 * 
 *  localtime(utctime, timezoneId):long which translates a utc time to local time 
 *  in the given timezone
 * 
 * The calculations are done via the internal Java-Time library, which itself uses
 * the tz database {@link http://www.iana.org/time-zones}.
 * 
 * @author berkan, sg
 * 
 */

public class TimeZoneToUtcOffsetProvider implements ITimeZoneToUtcOffsetProvider {

	private final Map<String, ZoneId> zoneCache = new ConcurrentHashMap<>();

	private final ZoneId utcTimeZone = ZoneId.of("UTC");
	private final Function<String, ZoneId> generator = timezoneId -> timezoneId == null ? utcTimeZone : ZoneId.of(timezoneId);

	private long timeZeroInMillis;

	private int convertMillisToInternal(final long time) {
		final long adjustedToTimeZero = time - timeZeroInMillis;
		final long convertedToHours = adjustedToTimeZero / 1000L / 60L / 60L;
		return (int) convertedToHours;
	}

	private long convertInternalToMillis(final int internal) {
		final long scaleToMillis = (long) internal * 60L * 60L * 1000L;
		final long removeTimeZeroAdjustment = scaleToMillis + timeZeroInMillis;
		return removeTimeZeroAdjustment;
	}

	/**
	 * Convenience functions to translate local time at a port to UTC time
	 * 
	 * The (intended) usage is to replace calls to
	 * 
	 * curve.getValueAtPoint(portLocalTime)
	 * 
	 * with
	 * 
	 * curve.getValueAtPoint( UTC(portLocalTime,portTimezone) )
	 * 
	 * Q: However - these functions need to return long to represent time; curve
	 * lookup times are kept in int. What's the right way to remedy the situation?
	 * 
	 * 
	 * @param local time
	 * @param port  , or a slot at port, or the time zone id of the port
	 * @return UTC time
	 */
	@Override
	public int UTC(final int localTime, final String timezoneId) {
		final ZoneId tz = timezoneId == null ? utcTimeZone : zoneCache.computeIfAbsent(timezoneId, generator);

		final Instant i = Instant.ofEpochMilli(convertInternalToMillis(localTime));
		final ZonedDateTime atZone = i.atZone(tz);
		final Instant i2 = Instant.from(atZone.withZoneSameLocal(utcTimeZone));
		final long oo = tz.getRules().getOffset(i).getTotalSeconds() % (60 * 60);

		final long utcInMillis = i2.toEpochMilli() - (oo * 1000L);

		return convertMillisToInternal(utcInMillis);

	}

	@Override
	public int UTC(final int localTime, @Nullable final IPort port) {

		String timeZoneId = port == null ? "UTC" : port.getTimeZoneId();
		if (timeZoneId == null || timeZoneId.isEmpty()) {
			timeZoneId = "UTC";
		}
		return UTC(localTime, timeZoneId);
	}

	@Override
	public int UTC(final int localTime, @Nullable final IPortSlot portSlot) {
		final IPort port = portSlot == null ? null : portSlot.getPort();
		final String timeZoneId = port == null ? "UTC" : port.getTimeZoneId();

		return UTC(localTime, timeZoneId);
	}

	@Override
	public int localTime(final int utcTime, @Nullable final String timezoneId) {
		final ZoneId tz = timezoneId == null ? utcTimeZone : zoneCache.computeIfAbsent(timezoneId, generator);

		final long utcTimeInMillis = convertInternalToMillis(utcTime);
		final Instant i = Instant.ofEpochMilli(utcTimeInMillis);
		final ZonedDateTime atZone = i.atZone(utcTimeZone);
		final Instant i2 = Instant.from(atZone.withZoneSameLocal(tz));
		final long oo = tz.getRules().getOffset(i).getTotalSeconds() % (60 * 60);

		final long localInMillis = i2.toEpochMilli() + (oo * 1000L);

		return convertMillisToInternal(localInMillis);
	}

	@Override
	public int localTime(final int utcTime, @Nullable final IPort port) {

		String timeZoneId = port == null ? "UTC" : port.getTimeZoneId();
		if (timeZoneId == null || timeZoneId.isEmpty()) {
			timeZoneId = "UTC";
		}
		return localTime(utcTime, timeZoneId);
	}

	@Override
	public int localTime(final int utcTime, @Nullable final IPortSlot portSlot) {
		final IPort port = portSlot == null ? null : portSlot.getPort();
		final String timeZoneId = port == null ? "UTC" : port.getTimeZoneId();

		return localTime(utcTime, timeZoneId);
	}

	public long getTimeZeroInMillis() {
		return timeZeroInMillis;
	}

	public void setTimeZeroInMillis(final long timeZeroInMillis) {
		this.timeZeroInMillis = timeZeroInMillis;
	}

}
