package com.mmxlabs.scheduler.optimiser.providers.impl;

//TODO: enable the import after the joda-time jar is in the right place
//import org.joda.time.DateTimeZone;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;

/**
 * 
 * Helper class that provides time offsets for translating local times to UTC.
 * 
 * There are two main functions: 
 * 
 *   UTC(localtime, timezoneId):long  which translates a local time in the given timezone to UTC
 * 
 *   getUtcOffset(timezoneId, localtime):int  which returns the localtime - UTC offset in milliseconds
 * 
 * 
 * The calculations are done via the Joda-Time library, which itself uses the tz database {@link http://www.iana.org/time-zones}.
 * 
 * (Note that the library is not released as frequently as the tz database -- so we will need to recompile the jar when the database 
 * is updated, see {@link http://www.joda.org/joda-time/tz_update.html})
 * 
 * At the moment, this class isn't bound in {@link com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule} 
 * like the other providers, as we only use the static methods here which call Joda-Time directly. If we need to optimise those 
 * calls, via further caching, or using code from the library internals directly etc, this can still be initialised and injected 
 * with guice where needed.
 * 
 * 
 * TO USE: make sure the joda-time jar/bundle is in place; enable "import org.joda.time.DateTimeZone", and alter the 
 * functions UTC(int localTime, String timezoneId) and getUtcOffset(String timezoneId, int localTime) below.
 * 
 * 
 * @author berkan
 *
 */


public class TimeZoneToUtcOffsetProvider implements ITimeZoneToUtcOffsetProvider {

	//MAYBE - in case we need to initialise joda-time, do our own caching etc.
	// private final Map<String, DateTimeZone> map = new HashMap<String, DateTimeZone>();
	// public TimeZoneToUtcOffsetProvider() { }

	/**
	 * Convenience functions to translate local time at a port to UTC time
	 *
	 * The (intended) usage is to replace calls to 
	 * 
	 *   curve.getValueAtPoint(portLocalTime) 
	 *   
	 *   with 
	 *   
	 *   curve.getValueAtPoint( UTC(portLocalTime,portTimezone) ) 
	 * 
	 * Q: However - these functions need to return long to represent time; curve lookup times are kept in int. What's the right way to remedy the situation? 
	 * 
	 * 
	 * @param local time
	 * @param port, or a slot at port, or the time zone id of the port
	 * @return UTC time
	 */

	
	public static long UTC(int localTime, String timezoneId) {
		// TODO-1: switch to joda-time when the jar is in place
		Object tz = new Object();  //DateTimeZone tz = DateTimeZone.forID(timezoneId);
		
		// for now, re-throw any exceptions
		try {
			// check for illegal/wrong timezoneId
			if (tz != null) {
				// TODO-2: switch to using joda-time when the jar is in place
				return localTime; // return tz.convertLocalToUTC(localTime, true);						
			}
			else {
				// TODO-3: Warn about the wrong timezoneId; currently returning localTime to keep things going
				return localTime;
			}
		} catch (Exception e) {
			throw(e);
		}
	}
	
	
	public static long UTC(int localTime, IPort port) {
		return UTC(localTime, port.getTimeZoneId());
	}		

	public static long UTC(int localTime, IPortSlot portSlot) {
		return UTC(localTime, portSlot.getPort().getTimeZoneId());
	}


	
	/**
	 * Get the local time - UTC offset in milliseconds
	 * 
	 * @param a time zone id 
	 * @param a local time instant in the specified timezone
	 * @return the millisecond offset to subtract from local time to get UTC time
	 */	
	public static int getUtcOffset(String timezoneId, int localTime) {
		// for now, see if we're calling this method properly; also re-throw any exceptions
		try {			
			//TODO:  switch to using joda-time when the jar is in place
			return 0; // DateTimeZone.forID(timezoneId).getOffsetFromLocal(localTime)
		} catch (Exception e) {
			throw(e);
		}		
	}
	
	/**
	 * @param a port in a time zone 
	 * @param a local time instant at the specified port
	 * @return the millisecond offset to subtract from local time at port to get UTC time
	 */
	public static int getUtcOffset(IPort port, int localTime) {
		return getUtcOffset(port.getTimeZoneId(), localTime);
	}	
	

	/**
	 * @param a portSlot from which we can retrieve the port's timezone id
	 * @param a local time instant at the specified port
	 * @return the millisecond offset to subtract from local time at port to get UTC time
	 */
	public static int getUtcOffset(IPortSlot portSlot, int localTime) {
		return getUtcOffset(portSlot.getPort().getTimeZoneId(), localTime);
	}	
	
	
}
	

