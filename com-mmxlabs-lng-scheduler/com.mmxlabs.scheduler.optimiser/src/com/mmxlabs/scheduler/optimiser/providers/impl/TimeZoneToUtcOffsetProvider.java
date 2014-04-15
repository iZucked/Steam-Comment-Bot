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
 * It uses the Joda-Time library, which itself uses the tz database {@link http://www.iana.org/time-zones} for its 
 * calculations. The library is not released as frequently as the tz database -- so we will need to recompile the jar 
 * when the database is updated, see {@link http://www.joda.org/joda-time/tz_update.html}.
 * 
 * At the moment, this class isn't bound in {@link com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule} 
 * like the other providers, as we only use the static methods here which call Joda-Time directly. If we need to optimise those 
 * calls, via further caching, or using code from the library internals directly etc, this can still be injected with
 * guice where needed.
 * 
 * 
 * TO USE: make sure the joda-time jar/bundle is in place; enable "import org.joda.time.DateTimeZone", and alter the 
 * function getUtcOffset(String timezoneId, int localTime) below.
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
	 * Example 1: 
	 * in {@link com.mmxlabs.lingo.shell.extensions.contracts.purchase.peru.PeruPurchaseContract.calculateFOBPricePerMMBTU()}
	 * 
	 *	 // Price at completion of loading
	 *	 final int pricingTime = loadTime + portVisitDurationProvider.getVisitDuration(loadSlot.getPort(),PortType.Load);
	 * 
	 * becomes 
	 * 
	 *   final int pricingTime = UTC( loadTime + portVisitDurationProvider.getVisitDuration(loadSlot.getPort(),PortType.Load),
	 *   							  loadSlot );
	 *   calcPrice( ... );

	 * Example2: 
	 * 
	 * Alternatively, in calcPrice ( ... ) at the point of the curve lookups:
	 *  
	 *   int HHPrice = HHCurve.getValueAtPoint( pricingTime );
	 * 
	 * becomes
	 * 
	 *   int HHPrice = HHCurve.getValueAtPoint( UTC(pricingTime,loadSlot) )
	 * 
	 * 
	 * @param local time
	 * @param port, or a slot at port, or the time zone id of the port
	 * @return UTC time
	 */
	
	public static int UTC(int localTime, IPort port) {
		return (int) (localTime - getUtcOffset(port.getTimeZoneId(), localTime));
	}		

	public static int UTC(int localTime, IPortSlot portSlot) {
		return UTC(localTime, portSlot.getPort());
	}
	
	public static int UTC(int localTime, String timezoneId) {
		return (int) (localTime - getUtcOffset(timezoneId, localTime));
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
			return 0; // DateTimeZone.forID(timezoneId).getOffset(localTime);
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
	

