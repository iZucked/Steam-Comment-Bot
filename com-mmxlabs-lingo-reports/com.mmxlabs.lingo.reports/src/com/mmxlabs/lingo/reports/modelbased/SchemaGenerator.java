/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubColumnStyle;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubSummary;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubType;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

public class SchemaGenerator {

	private static Set<Class<?>> numericTypes = Sets.newHashSet(short.class, int.class, long.class, float.class, double.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
			Number.class);
	private static Set<Class<?>> dateTypes = Sets.newHashSet(LocalDate.class, LocalDateTime.class);

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class ReportModel {
		@JsonIgnore
		public int version;

		public String type;
		public List<Column> columns;
		public List<ColumnStyle> styles;
		public InitialSort initialSort;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class InitialSort {
		public String field;
		public int mode;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class ColumnFormat {
		public String type;
//		@JsonRawValue
		public Object detail;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ColumnStyle {
		public List<StyleObject> row;
		public List<StyleObject> cell;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class StyleObject {
		public List<StyleMatcher> matchers;
		public String condition;
		public String value;
		public String field;
		public List<CssStyle> styling;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class StyleMatcher {
		public String condition;
		public String value;
		public String field;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CssStyle {
		public String property;
		public String value;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class NumberDetail {
		public boolean thousandSeparated = true;
		public Integer decimals;
	}

	private static class DateTimeDetail {
		public String raw = "DD/MM/YYYY hh:mm";
		public String dateFormat = "date";

		public DateTimeDetail() {

		}

		public DateTimeDetail(String format) {
			this.raw = format;
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private class Column {
		public String field;
		public String header;
		public ColumnFormat format;
		public ColumnStyle styles;
		public InitialSort initialSort;
	}

	public enum Mode {
		FULL, SUMMARY
	}

	public String generateHubSchema(Class<?> cls, Mode mode) throws JsonProcessingException {

		ReportModel model = new ReportModel();
		{
			model.version = 1;
			SchemaVersion ver = cls.getAnnotation(SchemaVersion.class);
			if (ver != null) {
				model.version = ver.value();
			}
		}
		model.columns = doGenerateHubSchema(cls, mode);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);

	}

	public List<Column> doGenerateHubSchema(Class<?> cls, Mode mode) throws JsonProcessingException {

		List<Column> entries = new LinkedList<>();
		for (Field f : cls.getFields()) {

			if (f.getAnnotation(JsonIgnore.class) != null) {
				continue;
			}

			Column entry = new Column();
			boolean includeEntry = false;
			int insertIndex = -1;
			if (mode == Mode.SUMMARY) {
				HubSummary columnName = f.getAnnotation(HubSummary.class);
				if (columnName != null) {
					entry.header = columnName.name();
					includeEntry = true;
					insertIndex = columnName.index() - 1;
				}
			} else if (mode == Mode.FULL) {
				ColumnName columnName = f.getAnnotation(ColumnName.class);
				if (columnName != null) {
					if (!columnName.hubFull().isEmpty()) {
						entry.header = columnName.hubFull();
					} else {
						entry.header = columnName.value();
					}
					includeEntry = true;
				}
			} else {
				throw new IllegalArgumentException();
			}

			entry.field = f.getName();
			Class<?> declaringClass = f.getType();
			entry.format = new ColumnFormat();

			String hubType = null;
			{
				HubType a = f.getAnnotation(HubType.class);
				if (a != null) {
					hubType = a.value();
//					includeEntry = true;
				}
			}

			if (Objects.equals(hubType, "number") || (hubType == null && numericTypes.contains(declaringClass))) {
				entry.format.type = "number";
				HubFormat format = f.getAnnotation(HubFormat.class);
				if (format != null) {
					ObjectMapper m = new ObjectMapper();
					// Use jackson to convert object type
					entry.format.detail = m.readValue(format.value(), NumberDetail.class);
				}
			} else if (Objects.equals(hubType, "date") || (hubType == null && dateTypes.contains(declaringClass))) {
				entry.format.type = "date";
				HubFormat format = f.getAnnotation(HubFormat.class);
				if (format != null) {
					entry.format.detail = new DateTimeDetail(format.value());
				}
			} else {
				if (hubType != null) {
					entry.format.type = hubType;
				} else {
					entry.format.type = "string";
				}
			}

			{
				HubColumnStyle style = f.getAnnotation(HubColumnStyle.class);
				if (style != null) {
					ObjectMapper m = new ObjectMapper();
					// Use jackson to convert object type
					entry.styles = m.readValue(style.value(), ColumnStyle.class);
				}

			}

			if (includeEntry) {
				if (insertIndex >= 0) {
					while (insertIndex >= entries.size()) {
						entries.add(null);
					}
					entries.set(insertIndex, entry);
				} else {
					entries.add(entry);
				}
			}
		}
		return entries;
	}
}
