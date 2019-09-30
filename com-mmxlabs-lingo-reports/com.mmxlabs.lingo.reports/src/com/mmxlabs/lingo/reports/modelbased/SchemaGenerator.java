package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubSummary;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

public class SchemaGenerator {

	private static Set<Class<?>> numericTypes = Sets.newHashSet(short.class, int.class, long.class, float.class, double.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
			Number.class);
	private static Set<Class<?>> dateTypes = Sets.newHashSet(LocalDate.class, LocalDateTime.class);

	private class SchemaModel {
		public int version;
		@JsonProperty(value = "columnsLite")
		public List<SchemaModelEntry> summary;
		@JsonProperty(value = "columns")
		public List<SchemaModelEntry> full;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private class SchemaModelEntry {
		public String name;
		// Handson table options
		public String data;
		public String type;
		public String dateFormat;
		@JsonRawValue

		public String numericFormat;

	}

	public String generateHubSchema(Class<?> cls) throws JsonProcessingException {

		SchemaModel model = new SchemaModel();
		{
			model.version = 1;
			SchemaVersion ver = cls.getAnnotation(SchemaVersion.class);
			if (ver != null) {
				model.version = ver.value();
			}
		}
		model.full = generateHubSchema(cls, false);
		model.summary = generateHubSchema(cls, true);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);

	}

	public List<SchemaModelEntry> generateHubSchema(Class<?> cls, boolean isSummary) throws JsonProcessingException {

		List<SchemaModelEntry> entries = new LinkedList<>();
		for (Field f : cls.getFields()) {

			if (f.getAnnotation(JsonIgnore.class) != null) {
				continue;
			}

			SchemaModelEntry entry = new SchemaModelEntry();
			boolean includeEntry = false;
			int insertIndex = -1;
			if (isSummary) {
				HubSummary columnName = f.getAnnotation(HubSummary.class);
				if (columnName != null) {
					entry.name = columnName.name();
					includeEntry = true;
					insertIndex = columnName.index() - 1;
				}
			} else {
				ColumnName columnName = f.getAnnotation(ColumnName.class);
				if (columnName != null) {
					if (!columnName.hubFull().isEmpty()) {
						entry.name = columnName.hubFull();
					} else {
						entry.name = columnName.value();
					}
					includeEntry = true;
				}
			}

			entry.data = f.getName();
			Class<?> declaringClass = f.getType();
			if (numericTypes.contains(declaringClass)) {
				entry.type = "numeric";
				HubFormat format = f.getAnnotation(HubFormat.class);
				if (format != null) {
					entry.numericFormat = format.value();
				}
			} else if (dateTypes.contains(declaringClass)) {
				entry.type = "date";
				HubFormat format = f.getAnnotation(HubFormat.class);
				if (format != null) {
					entry.dateFormat = format.value();
				}
			} else {
				entry.type = "text";
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
