package com.mmxlabs.lingo.its;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public class CSVBuilder {

	private final LinkedHashSet<String> headers = new LinkedHashSet<>();

	private final List<Map<String, @Nullable String>> rows = new LinkedList<>();

	public class RowBuilder {
		private final CSVBuilder builder;

		private final Map<String, @Nullable String> rowData = new HashMap<>();

		protected RowBuilder(final CSVBuilder builder) {
			this.builder = builder;
		}

		public RowBuilder addColumn(final String key, @Nullable final String value) {
			rowData.put(key, value);
			builder.addHeader(key);
			return this;
		}

		public CSVBuilder done() {
			builder.addRow(rowData);
			return builder;
		}

		public String build() {
			builder.addRow(rowData);
			return builder.build();
		}

		public RowBuilder makeRow() {
			builder.addRow(rowData);
			return builder.makeRow();
		}

	}

	public CSVBuilder() {

	}

	public CSVBuilder(@NonNull final String... predefinedHeaders) {
		for (final @NonNull String header : predefinedHeaders) {
			headers.add(header);
		}
	}

	public void addHeaders(@NonNull final String... predefinedHeaders) {
		for (final @NonNull String header : predefinedHeaders) {
			headers.add(header);
		}
	}

	public void addHeader(final String header) {
		headers.add(header);
	}

	public void addRow(final Map<String, @Nullable String> rowData) {
		rows.add(rowData);
	}

	public RowBuilder makeRow() {
		return new RowBuilder(this);
	}

	/**
	 * Create a whole row at once. Values should match the header count and order
	 * 
	 * @param values
	 * @return
	 */
	public CSVBuilder makeRow(@NonNull final String... values) {
		final RowBuilder rb = makeRow();
		int idx = 0;
		assert values.length == headers.size();
		for (final String header : headers) {
			rb.addColumn(header, values[idx++]);
		}
		return rb.done();
	}

	public String build() {
		final StringBuilder sb = new StringBuilder();
		// Do headers
		{
			boolean first = true;
			for (final String header : headers) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(header);
			}
			sb.append("\n");
		}
		// Rows
		for (final Map<@NonNull String, @Nullable String> rowData : rows) {
			boolean first = true;
			for (final String header : headers) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				@Nullable
				final String value = rowData.get(header);
				final String exportValue = value == null ? "" : value;
				sb.append(exportValue);
			}
			sb.append("\n");
		}

		return sb.toString();
	}

}
