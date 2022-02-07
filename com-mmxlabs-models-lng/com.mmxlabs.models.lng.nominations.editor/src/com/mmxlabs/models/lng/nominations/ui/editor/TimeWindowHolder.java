/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TimeWindowHolder {

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate windowStart;
	int windowSize;
	char windowUnits;
	
	public TimeWindowHolder() {
		windowStart = LocalDate.now();
		windowSize = 0;
		windowUnits = 'h';
	}
	
	public TimeWindowHolder(LocalDate d, int w, char u) {
		this.windowStart = d; 
		this.windowSize = w;
		this.windowUnits = u;
	}

	public LocalDate getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(LocalDate windowStart) {
		this.windowStart = windowStart;
	}

	public int getWindowSize() {
		return windowSize;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	public char getWindowUnits() {
		return windowUnits;
	}

	public void setWindowUnits(char windowUnits) {
		this.windowUnits = windowUnits;
	}	
	
	public String toString() {
		return DateWindowFormatter.format(this);
	}

	@JsonIgnore
	public LocalDate getWindowEnd() {
		switch (windowUnits) {
		case 'h':
			return this.windowStart.plusDays(windowSize/24);
		case 'd':
			return this.windowStart.plusDays(windowSize);
		case 'm':
			return this.windowStart.plusMonths(windowSize);
		default:
			return this.windowStart;
		}
	}
}

