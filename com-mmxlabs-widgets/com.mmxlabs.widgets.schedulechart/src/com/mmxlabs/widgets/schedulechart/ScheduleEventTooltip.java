/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record ScheduleEventTooltip( //
		ScheduleEvent se, //
		List<String> headerNames, //
		String eventType, //
		Map<String, String> bodyFields, //
		List<String> footerText //
) {

	public static ScheduleEventTooltipBuilder of(ScheduleEvent se, Function<ScheduleEvent, Object> getData) {
		return new ScheduleEventTooltipBuilder(se, getData);
	}
	
	public static ScheduleEventTooltipBuilder of(ScheduleEvent se) {
		return new ScheduleEventTooltipBuilder(se, (s -> s.getData()));
	}

	public static class ScheduleEventTooltipBuilder {

		private final ScheduleEvent se;
		private final List<String> headerNames = new ArrayList<>();
		private final Map<String, String> bodyFields = new LinkedHashMap<>();
		private final List<String> footerText = new ArrayList<>();
		private String eventType = null;
		private Function<ScheduleEvent, Object> getData;

		public ScheduleEventTooltipBuilder(ScheduleEvent se, Function<ScheduleEvent, Object> getData) {
			this.se = se;
			this.getData = getData;
		}

		public ScheduleEventTooltipBuilder add(ScheduleEventTooltipData type, Function<ScheduleEvent, String> f) {
			addBasedOnType(type, f.apply(se));
			return this;
		}

		public <T> ScheduleEventTooltipBuilder add(ScheduleEventTooltipData type, Class<T> clazz, Function<T, String> f) {
			addBasedOnType(type, f.apply(clazz.cast(getData.apply(se))));
			return this;
		}

		public ScheduleEventTooltipBuilder addBodyField(final String key, Function<ScheduleEvent, String> f) {
			bodyFields.put(key, f.apply(se));
			return this;
		}

		public <T> ScheduleEventTooltipBuilder addBodyField(final String key, Class<T> clazz, Function<T, String> f) {
			bodyFields.put(key, f.apply(clazz.cast(getData.apply(se))));
			return this;
		}

		private void addBasedOnType(ScheduleEventTooltipData type, String s) {
			switch (type) {
			case HEADER_NAME -> headerNames.add(s);
			case FOOTER_TEXT -> footerText.add(s);
			case EVENT_TYPE -> eventType = s;
			default -> {
			}
			}
		}

		public ScheduleEventTooltip create() {
			return new ScheduleEventTooltip(se, headerNames, eventType, bodyFields, footerText);
		}

		public boolean isEmpty() {
			return headerNames.isEmpty() && bodyFields.isEmpty() && footerText.isEmpty() && eventType == null;
		}

		public enum ScheduleEventTooltipData {
			HEADER_NAME, EVENT_TYPE, BODY_FIELD, FOOTER_TEXT;
		}
	}

}
