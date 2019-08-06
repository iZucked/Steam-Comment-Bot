package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public interface TimeWindow {
	LocalDate getStartDate();
	ZonedDateTime getEndDate();
	int getDuration();
}
