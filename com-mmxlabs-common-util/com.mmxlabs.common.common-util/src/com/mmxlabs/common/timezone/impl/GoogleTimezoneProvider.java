/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.timezone.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mmxlabs.common.timezone.ITimezoneProvider;
import com.mmxlabs.common.timezone.TimezoneProviderException;

/**
 * 
 * A {@link ITimezoneProvider} implementation using the Google APIs.
 * 
 * @see https://developers.google.com/maps/documentation/timezone/
 * 
 * @author Simon Goodall
 * @since 2.1
 */
@SuppressWarnings("unused")
public class GoogleTimezoneProvider implements ITimezoneProvider {

	private static final String dstOffset = "dstOffset";
	/** the offset from UTC (in seconds) for the given location. This does not take into effect daylight savings. */
	private static final String rawOffset = "rawOffset";

	/**
	 * a string containing the ID of the time zone, such as "America/Los_Angeles" or "Australia/Sydney".
	 */
	private static final String timeZoneId = "timeZoneId";
	/** a string containing the long form name of the time zone. This field will be localized if the language parameter is set. eg. "Pacific Daylight Time" or "Australian Eastern Daylight Time" */
	private static final String timeZoneName = "timeZoneName";

	/** a string indicating the status of the response. */
	private static final String status = "status";

	/** indicates that the request was successful. */
	private static final String STATUS_OK = "OK";
	/** indicates that the request was malformed. */
	private static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";
	/** indicates the requestor has exceeded quota. */
	private static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
	/**
	 * indicates that the the API did not complete the request. Confirm that the request was sent over http instead of https.
	 */
	private static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";
	/**
	 * indicates an unknown error.
	 */
	private static final String STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";
	/**
	 * indicates that no time zone data could be found for the specified position or time. Confirm that the request is for a location on land, and not over water.
	 */
	private static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";

	@Override
	public TimeZone findTimeZone(final float latitude, final float longitude) {
		InputStream requestStream = null;
		InputStreamReader in = null;
		try {
			final URL requestURL = new URL(buildQueryURL(latitude, longitude));
			requestStream = requestURL.openStream();
			in = new InputStreamReader(requestStream);
			final Object result = JSONValue.parse(in);
			if (result instanceof JSONObject) {
				final JSONObject jsonObject = (JSONObject) result;
				if (jsonObject.containsKey(status)) {
					final String statusString = (String) jsonObject.get(status);
					if (STATUS_OK.equals(statusString)) {

						if (jsonObject.containsKey(timeZoneId)) {
							final String timeZoneID = (String) jsonObject.get(timeZoneId);

							return TimeZone.getTimeZone(timeZoneID);
						}
					} else {
						throw new TimezoneProviderException(statusString);
					}
				}
			}
		} catch (final Exception e) {
			// Wrap up and re-throw
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					// Ignore
				}
			}
			if (requestStream != null) {
				try {
					requestStream.close();
				} catch (final IOException e) {
					// Ignore
				}
			}
		}
		return null;
	}

	/**
	 * Construct a request {@link URL} to request timezone in json format
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	private String buildQueryURL(final float lat, final float lon) {
		final long timestamp = System.currentTimeMillis() / 1000L;
		final String str = String.format("https://maps.googleapis.com/maps/api/timezone/json?location=%f,%f&timestamp=%d&sensor=false", lat, lon, timestamp);
		return str;
	}

}
