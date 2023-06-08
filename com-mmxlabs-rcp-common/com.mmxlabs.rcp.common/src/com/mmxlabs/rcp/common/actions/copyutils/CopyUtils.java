/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions.copyutils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mmxlabs.common.time.DateTimeFormats;

/**
 * Helper methods for copying special strings such as dates
 * @author Andrey Popov
 *
 */
public class CopyUtils {
	
	private CopyUtils() {
		// no instance
	}
	
	/**
	 * Returns the given text with modifications if they were needed.
	 * Modifications include preferred date formatting.
	 * @param itemText
	 * @return modified text with proper formating adjustments
	 */
	public static String reformatText(String itemText, CopyAction copyAction) {
		for (final String dateFormatPattern : copyAction.getDateFormatsToProcess()) {
			try {
				final LocalDate localDate = LocalDate.parse(itemText, DateTimeFormatter.ofPattern(dateFormatPattern));
				return localDate.format(DateTimeFormatter.ofPattern(DateTimeFormats.PREFERRED_DATE_FORMAT));
			} catch (final DateTimeException | IllegalArgumentException e) {
				// skip and try the next one
			}
		}
		for (final String dateTimeFormatPattern : copyAction.getDateFormatsToProcess()) {
			try {
				final LocalDateTime localDateTime = LocalDateTime.parse(itemText, DateTimeFormatter.ofPattern(dateTimeFormatPattern));
				return localDateTime.format(DateTimeFormatter.ofPattern(DateTimeFormats.PREFERRED_DATE_TIME_FORMAT));
			} catch (final DateTimeException | IllegalArgumentException e) {
				// skip and try the next one
			}
		}
		/*
		 * Default date format check
		 */
		try {
			final LocalDate localDate = LocalDate.parse(itemText, DateTimeFormatter.ofPattern(DateTimeFormats.getDateStringDisplay()));
			return localDate.format(DateTimeFormatter.ofPattern(DateTimeFormats.PREFERRED_DATE_FORMAT));
		} catch (final DateTimeException | IllegalArgumentException e) {
			// skip and try the next one
		}
		try {
			final LocalDateTime localDateTime = LocalDateTime.parse(itemText, DateTimeFormatter.ofPattern(DateTimeFormats.getDateTimeStringDisplay()));
			return localDateTime.format(DateTimeFormatter.ofPattern(DateTimeFormats.PREFERRED_DATE_TIME_FORMAT));
		} catch (final DateTimeException | IllegalArgumentException e) {
			// skip and try the next one
		}
		return itemText;
	}
}
