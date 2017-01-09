/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
 * UTC(localtime, timezoneId):long which translates a local time in the given timezone to UTC
 * 
 * getUtcOffset(timezoneId, localtime):int which returns the localtime - UTC offset in milliseconds
 * 
 * 
 * The calculations are done via the Joda-Time library, which itself uses the tz database {@link http://www.iana.org/time-zones}.
 * 
 * (Note that the library is not released as frequently as the tz database -- so we will need to recompile the jar when the database is updated, see {@link http
 * ://www.joda.org/joda-time/tz_update.html})
 * 
 * At the moment, this class isn't bound in {@link com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule} like the other providers, as we only use the static methods here which
 * call Joda-Time directly. If we need to optimise those calls, via further caching, or using code from the library internals directly etc, this can still be initialised and injected with guice where
 * needed.
 * 
 * 
 * TO USE: make sure the joda-time jar/bundle is in place; enable "import org.joda.time.ZoneId", and alter the functions UTC(int localTime, String timezoneId) and getUtcOffset(String timezoneId,
 * int localTime) below.
 * 
 * 
 * @author berkan
 * 
 */

public class TimeZoneToUtcOffsetProvider implements ITimeZoneToUtcOffsetProvider {

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

	// MAYBE - in case we need to initialise joda-time, do our own caching etc.
	// private final Map<String, ZoneId> map = new HashMap<String, ZoneId>();
	// public TimeZoneToUtcOffsetProvider() { }

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
	 * Q: However - these functions need to return long to represent time; curve lookup times are kept in int. What's the right way to remedy the situation?
	 * 
	 * 
	 * @param local
	 *            time
	 * @param port
	 *            , or a slot at port, or the time zone id of the port
	 * @return UTC time
	 */
	@Override
	public int UTC(final int localTime, final String timezoneId) {
		final ZoneId tz = timezoneId == null ? ZoneId.of("UTC") : ZoneId.of(timezoneId);

		// for now, re-throw any exceptions
		try {
			// check for illegal/wrong timezoneId
			if (tz != null) {
				
				Instant i = Instant.ofEpochMilli( convertInternalToMillis(localTime));
				ZonedDateTime atZone = i.atZone(tz);
				Instant i2 = Instant.from(atZone.withZoneSameLocal(ZoneId.of("UTC")));
				
				final long utcInMillis = i2.toEpochMilli();//utcTimeInMillis - tz.getOffsetFromLocal(utcTimeInMillis);
				
//				final long localTimeInMillis = convertInternalToMillis(localTime);
//				// final long utcInMillis = tz.convertLocalToUTC(localTimeInMillis, true);
//				final long utcInMillis = localTimeInMillis + tz.getOffsetFromLocal(localTimeInMillis);

				return convertMillisToInternal(utcInMillis);
			} else {
				// TODO-3: Warn about the wrong timezoneId; currently returning localTime to keep things going
				return localTime;
			}
		} catch (final Exception e) {
			throw (e);
		}
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
		final ZoneId tz = ZoneId.of(timezoneId);

		// for now, re-throw any exceptions
		try {
			// check for illegal/wrong timezoneId
			if (tz != null) {
				final long utcTimeInMillis = convertInternalToMillis(utcTime);
				// final long utcInMillis = tz.convertLocalToUTC(localTimeInMillis, true);
				Instant i = Instant.ofEpochMilli(utcTimeInMillis);
				ZonedDateTime atZone = i.atZone(ZoneId.of("UTC"));
				Instant i2 = Instant.from(atZone.withZoneSameLocal(tz));
				
				final long localInMillis = i2.toEpochMilli();//utcTimeInMillis - tz.getOffsetFromLocal(utcTimeInMillis);

				return convertMillisToInternal(localInMillis);
			} else {
				// TODO-3: Warn about the wrong timezoneId; currently returning localTime to keep things going
				return utcTime;
			}
		} catch (final Exception e) {
			throw (e);
		}
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

//	/**
//	 * Get the local time - UTC offset in milliseconds
//	 * 
//	 * @param a
//	 *            time zone id
//	 * @param a
//	 *            local time instant in the specified timezone
//	 * @return the millisecond offset to subtract from local time to get UTC time
//	 */
//	private int getUtcOffset(final String timezoneId, final long localTimeInMillis) {
//		// for now, see if we're calling this method properly; also re-throw any exceptions
//		try {
//			// TODO: switch to using joda-time when the jar is in place
//			// return 0; //
//			return ZoneId.of(timezoneId).getOffsetFromLocal(localTimeInMillis);
//		} catch (final Exception e) {
//			throw (e);
//		}
//	}

//	/**
//	 * @param a
//	 *            port in a time zone
//	 * @param a
//	 *            local time instant at the specified port
//	 * @return the millisecond offset to subtract from local time at port to get UTC time
//	 */
//	private int getUtcOffset(@NonNull final IPort port, final int localTimeInMillis) {
//		return getUtcOffset(port.getTimeZoneId(), localTimeInMillis);
//	}
//
//	/**
//	 * @param a
//	 *            portSlot from which we can retrieve the port's timezone id
//	 * @param a
//	 *            local time instant at the specified port
//	 * @return the millisecond offset to subtract from local time at port to get UTC time
//	 */
//	private int getUtcOffset(@NonNull final IPortSlot portSlot, final int localTimeInMillis) {
//		return getUtcOffset(portSlot.getPort().getTimeZoneId(), localTimeInMillis);
//	}

	public long getTimeZeroInMillis() {
		return timeZeroInMillis;
	}

	public void setTimeZeroInMillis(long timeZeroInMillis) {
		this.timeZeroInMillis = timeZeroInMillis;
	}

}
