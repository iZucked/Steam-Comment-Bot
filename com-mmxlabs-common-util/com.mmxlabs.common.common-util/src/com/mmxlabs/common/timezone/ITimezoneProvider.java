/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.timezone;

import java.util.TimeZone;

/**
 * An interface to find a {@link TimeZone} given a lat/lon co-ordinate.
 * 
 * @see http://stackoverflow.com/questions/41504/timezone-lookup-from-latitude-longitude
 * 
 * @author Simon Goodall
 * @since 2.1
 */
public interface ITimezoneProvider {

	TimeZone findTimeZone(float latitude, float longitude);
}
