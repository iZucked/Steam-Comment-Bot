package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.types.TimePeriod;

public interface TimeWindow {
	LocalDate getStartDate();
	ZonedDateTime getEndDate();
	TimePeriod getSizeUnits();
	int getSize();
	int getDuration();
	int getSizeInHours();
}
