/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;

public class MicroCaseDateUtils {
	
	public static ZonedDateTime getDateTimeFromHour(LNGScenarioToOptimiserBridge bridge, int hours, String tz) {
		return getDateTimeFromHour(bridge.getDataTransformer().getModelEntityMap(), hours, tz);
	}
	
	public static ZonedDateTime getDateTimeFromHour(ModelEntityMap modelEntityMap, int hours, String tz) {
		return modelEntityMap.getDateFromHours(hours, tz).withMinute(0).withSecond(0).withNano(0);
	}
	
	public static ZonedDateTime getZonedDateTime(int year, int month, int day, int hour, Port port) {
		return getZonedDateTime(year, month, day, hour, port.getTimeZone());
	}

	public static ZonedDateTime getZonedDateTime(int year, int month, int day, int hour, String tz) {
		return getZonedDateTime(year, month, day, hour, ZoneId.of(tz));
	}

	public static ZonedDateTime getZonedDateTime(int year, int month, int day, int hour, ZoneId tz) {
		return ZonedDateTime.of(year, month, day, hour, 0, 0, 0, tz);
	}

}
