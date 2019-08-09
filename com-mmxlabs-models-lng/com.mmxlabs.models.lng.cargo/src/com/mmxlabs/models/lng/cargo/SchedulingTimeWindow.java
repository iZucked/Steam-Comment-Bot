package com.mmxlabs.models.lng.cargo;

import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.types.TimePeriod;

public interface SchedulingTimeWindow {
	ZonedDateTime getStart();
	ZonedDateTime getEnd();
	ZonedDateTime getStartWithFlex();
	ZonedDateTime getEndWithFlex();
	int getSize();
	TimePeriod getSizeUnits();
	int getDuration();
	int getSizeInHours();
}
