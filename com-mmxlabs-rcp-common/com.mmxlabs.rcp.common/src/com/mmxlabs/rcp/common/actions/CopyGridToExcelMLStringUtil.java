/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToExcelMLStringUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CopyGridToExcelMLStringUtil.class);
	private final Grid table;

	private boolean rowHeadersIncluded = false;
	// Set border around everything?
	private final boolean includeBorder = false;
	private boolean showBackgroundColours = false;
	private boolean includeAllColumns = false;
	private IAdditionalAttributeProvider additionalAttributeProvider = null;

	public CopyGridToExcelMLStringUtil(final Grid table, final boolean includeRowHeaders, final boolean includeAllColumns) {

		this.table = table;
		this.rowHeadersIncluded = includeRowHeaders;
		this.includeAllColumns = includeAllColumns;
	}

	private static class Style {
		int border;
		RGB foreground;
		private RGB background;

		public Style(RGB foreground, RGB background, int border) {
			this.background = background;
			this.border = border;
			this.foreground = foreground;
		}

		@Override
		public int hashCode() {
			return (border * 31 + foreground.hashCode()) * 31 + background.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Style) {
				Style other = (Style) obj;

				return border == other.border && foreground.equals(other.foreground) && background.equals(other.background);

			}
			return false;
		}
	}

	private class StyleManager {
		private AtomicInteger nextIndex = new AtomicInteger(1);
		private Map<Style, String> stlyeCache = new LinkedHashMap<>();

		public String getStyleID(RGB foreground, RGB background, int border) {
			return stlyeCache.computeIfAbsent(new Style(foreground, background, border), (style) -> {
				return String.format("Style%d", nextIndex.getAndIncrement());
			});
		}

		public static final int NONE = 0;
		public static final int LEFT = 1;
		public static final int RIGHT = 2;
		public static final int TOP = 4;
		public static final int BOTTOM = 8;

		public @NonNull String getStyles() {
			StringWriter sw = new StringWriter();
			sw.append("<Styles>");

			for (Map.Entry<Style, String> e : stlyeCache.entrySet()) {

				RGB f = e.getKey().foreground;
				RGB b = e.getKey().background;
				String border = "";
				if (e.getKey().border != 0) {
					List<String> borderElements = new LinkedList<>();
					if ((e.getKey().border & LEFT) == LEFT) {
						borderElements.add("<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
					}
					if ((e.getKey().border & RIGHT) == RIGHT) {
						borderElements.add("<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
					}
					if ((e.getKey().border & TOP) == TOP) {
						borderElements.add("<Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
					}
					if ((e.getKey().border & BOTTOM) == BOTTOM) {
						borderElements.add("<Border ss:Position=\"Bottom\"  ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
					}

					border = String.format("<Borders>%s</Borders>", Joiner.on("").join(borderElements));
				}
				sw.append(String.format(
						"<Style  ss:ID=\"%s\"><Interior  ss:Pattern=\"Solid\" ss:Color=\"#%02X%02X%02X\"></Interior><NumberFormat   ss:Format=\"yy\\-mmm\\-dd\" /><Font ss:Color=\"#%02X%02X%02X\"></Font>%s</Style>",
						e.getValue(), b.red, b.green, b.blue, f.red, f.green, f.blue, border));
			}

			sw.append("</Styles>");

			return sw.toString();
		}

	}

	public String convert() {
		if (additionalAttributeProvider != null) {
			additionalAttributeProvider.begin();
		}
		try {
			final StringWriter header_sw = new StringWriter();
			// Dump header info
			header_sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			header_sw.append("<?mso-application progid=\"Excel.Sheet\"?>");
			header_sw.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
			header_sw.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
			header_sw.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
			header_sw.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
			header_sw.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");

			final StringWriter sw = new StringWriter();

			// sw.append(" <Names>");
			// sw.append(" <NamedRange ss:Name=\"Print_Titles\" ss:Hidden=\"0\" ss:RefersTo=\"=Schedule!R1:R1,Schedule!R2:R2\"/>");
			// sw.append(" </Names>");

			// TODO: Get from input / date?
			sw.append(" <Worksheet ss:Name=\"Schedule\">");
			// =Sheet1!$1:$1,Sheet1!$2:$2
			sw.append(" <Names>");
			sw.append(" <NamedRange ss:Name=\"Print_Titles\" ss:Hidden=\"0\"  ss:RefersTo=\"=Schedule!R1:R1,Schedule!R2:R2\"/>");
			sw.append(" </Names>");

			StyleManager styleManger = new StyleManager();

			// Note this may be zero if no columns have been defined. However an
			// implicit column will be created in such cases
			final int numColumns = table.getColumnCount();
			sw.write(String.format("<Table ss:ExpandedColumnCount=\"%d\" ss:ExpandedRowCount=\"%d\" x:FullColumns=\"1\" x:FullRows=\"1\" ss:DefaultRowHeight=\"13.2\" >\n", numColumns,
					table.getRootItems().length + 2));

			try {
				// addPreTableRows(sw);
				addHeader(sw);
				// Ensure at least 1 column to grab data
				final int numberOfColumns = Math.max(5, numColumns);
				final int[] rowOffsets = new int[numberOfColumns];

				for (final GridItem item : table.getItems()) {
					processTableRow(sw, styleManger, numberOfColumns, item, rowOffsets);
				}
			} catch (final IOException e) {
				// should not occur, since we use a StringWriter
				LOG.error(e.getMessage(), e);
			}
			sw.write("</Table>");

			sw.append("</Worksheet>");
			sw.append("</Workbook>");

			return header_sw.toString() + styleManger.getStyles() + sw.toString();
		} finally {
			if (additionalAttributeProvider != null) {
				additionalAttributeProvider.done();
			}
		}
	}

	private void addPreTableRows(@NonNull final StringWriter sw) {
		final String[] additionalPreRows = getAdditionalPreRows();
		if (additionalPreRows != null) {
			for (final String r : additionalPreRows) {
				sw.write(r);
			}
		}
	}

	private void addHeader(@NonNull final StringWriter sw) {

		// final int numColumns = table.getColumnCount();
		// write the head
		// sw.write("<Row>\n");

		// Create temporary writers to create both header rows concurrently.
		final StringWriter topRow = new StringWriter();
		final StringWriter bottomRow = new StringWriter();
		final StringWriter singleRow = new StringWriter();

		// top left blank cell
		if (rowHeadersIncluded) {
			// addCell(topRow, "Cell", getTopLeftCellUpperText(), 0, 0, new String[] {}, 1);
			// addCell(bottomRow, "Cell", getTopLeftCellLowerText(), 0, 0, new String[] {}, 1);
			// addCell(singleRow, "Cell", getTopLeftCellText(), 0, 0, new String[] {}, 1);
		}
		// Set of column groups already seen. This assumes all columns within a group are next to each other
		final Set<GridColumnGroup> seenGroups = new HashSet<>();
		final int[] columnOrder = includeAllColumns ? getAllColumns(table) : table.getColumnOrder();
		// int idx = 0;
		// for (final int i : columnOrder) {
		for (int idx = 0; idx < columnOrder.length; ++idx) {
			final int i = columnOrder[idx];

			final GridColumn column = table.getColumn(i);
			if (!includeAllColumns && !column.isVisible()) {
				continue;
			}
			// ++idx;
			// Get the column group
			final GridColumnGroup columnGroup = column.getColumnGroup();
			final String colourString;
			if (showBackgroundColours) {
				// colourString = "bgcolor='grey'";
				colourString = "";
			} else {
				colourString = "";
			}
			String typeString = " ss:Type=\"String\"";
			if (columnGroup == null) {
				// No group? Then cell bottom row cell should fill both header rows
				addCell(topRow, "<NamedCell ss:Name=\"Print_Titles\"/>", column.getText(), table.getColumnGroupCount() > 0 ? 1 : 0, 0,
						combineAttributes(new String[] { typeString }, getAdditionalHeaderAttributes(column)), idx + 1, colourString);
				// //
				addCell(singleRow, "<NamedCell ss:Name=\"Print_Titles\"/>", column.getText(), 0, 0, combineAttributes(new String[] { typeString }, getAdditionalHeaderAttributes(column)), idx + 1,
						colourString);

			} else {
				// Add in the bottom row info.
				addCell(bottomRow, "<NamedCell ss:Name=\"Print_Titles\"/>", column.getText(), 0, 0, combineAttributes(new String[] { typeString }, getAdditionalHeaderAttributes(column)), idx + 1,
						colourString);
				// Part of column group. Only add group if we have not previously seen it.
				if (seenGroups.add(columnGroup)) {
					int groupCount = 0;
					for (GridColumn c : columnGroup.getColumns()) {
						if (c.isVisible()) {
							groupCount++;
						}
					}
					addCell(topRow, "<NamedCell ss:Name=\"Print_Titles\"/>", columnGroup.getText(), 0, groupCount - 1,
							combineAttributes(new String[] { typeString }, getAdditionalHeaderAttributes(column)), idx + 1, colourString);
					// idx += groupCount;
				}
			}

		}
		// If we have column groups, then write the top header row, otherwise skip it.
		if (!seenGroups.isEmpty()) {
			sw.write("<Row>");
			sw.write(topRow.toString());
			sw.write("</Row>\n");
			sw.write("<Row>");
			sw.write(bottomRow.toString());
			sw.write("</Row>\n");
		} else
		//
		{
			sw.write("<Row>");
			sw.write(singleRow.toString());
			sw.write("</Row>\n");

		}
	}

	@NonNull
	protected String getTopLeftCellText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellText();
		}
		return "";
	}

	@NonNull
	protected String getTopLeftCellUpperText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellUpperText();
		}
		return "";
	}

	@NonNull
	protected String getTopLeftCellLowerText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellLowerText();
		}
		return "";
	}

	private int @NonNull [] getAllColumns(final @NonNull Grid table) {
		final int[] indicies = new int[table.getColumnCount()];
		for (int i = 0; i < indicies.length; ++i) {
			indicies[i] = i;
		}
		return indicies;
	}

	private void processTableRow(final StringWriter sw, StyleManager styleManager, final int numColumns, final GridItem item, final int[] rowOffsets) throws IOException {
		// if (true) return;
		// start a row
		sw.write("<Row>");

		if (rowHeadersIncluded) {
			// addCell(sw, item.getHeaderText(), 0, 0, getAdditionalRowHeaderAttributes(item));
		}

		final int[] columnOrder = includeAllColumns ? getAllColumns(table) : table.getColumnOrder();
		for (int i = 0; i < columnOrder.length; ++i) {
			final int colIdx = columnOrder[i];
			final GridColumn column = table.getColumn(colIdx);
			if (!includeAllColumns && !column.isVisible()) {
				continue;
			}
			// If offset is greater than zero, skip this row
			if (rowOffsets[colIdx] == 0) {

				Color fc = item.getForeground(colIdx);
				RGB f = new RGB(0, 0, 0);
				if (fc != null) {
					f = fc.getRGB();
				}

				Color fb = item.getBackground(colIdx);
				RGB b = new RGB(0, 255, 255);
				if (fb != null) {
					b = fb.getRGB();
				}
				// String colourString = "";
				String extra = "";
				// if (showBackgroundColours) {
				{
					// if (c == null) {
					// colourString = "bgcolor='white'";
					// } else {
					// colourString = String.format(" <Font x:Color=\"FF0000\"> ", c.getRed(), c.getGreen(), c.getBlue());
					extra = String.format(" ss:StyleID=\"%s\" ", styleManager.getStyleID(f, b, getBorders(item, colIdx)));
					// }
					// colourString = "";
				}

				String typeString = String.format(" ss:Type=\"String\" ");
				String text = item.getText(colIdx);
				Object v = getTypedValue(item, colIdx);
				if (v != null) {
					if (v instanceof LocalDate) {
						typeString = String.format(" ss:Type=\"DateTime\" ");
						LocalDate dt = (LocalDate) v;
						text = Long.toString(dt.atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0)));

					}
				}
				addCell(sw, text, item.getRowSpan(colIdx), item.getColumnSpan(colIdx), combineAttributes(new String[] { typeString }, getAdditionalAttributes(item, colIdx)), i + 1, extra);
				// addCell(sw,item.getText(colIdx), 0,0,
				// new String[] { typeString});
				// Increment col idx.
				// FIXME: Does this work correctly for sortable columns? No existing reports to test against.
				i += item.getColumnSpan(colIdx);
				// i+= 1;
				rowOffsets[colIdx] = item.getRowSpan(colIdx);
			} else {
				// sw.write(String.format("<Cell ss:Index=\"%d\"/>\n", 1+ i));
				rowOffsets[colIdx] = rowOffsets[colIdx] - 1;
			}
			// end row
			if ((i + 1) >= numColumns) {
				sw.write("</Row>\n");
			}
		}
	}

	private String[] combineAttributes(final String[] strings, final String[] additionalAttributes) {
		if (strings == null && additionalAttributes == null) {
			return new String[0];
		} else if (strings == null) {
			return additionalAttributes;
		} else if (additionalAttributes == null) {
			return strings;
		}

		final String[] combined = new String[strings.length + additionalAttributes.length];
		System.arraycopy(strings, 0, combined, 0, strings.length);
		System.arraycopy(additionalAttributes, 0, combined, strings.length, additionalAttributes.length);

		return combined;
	}

	private String[] getAdditionalRowHeaderAttributes(final GridItem item) {
		if (additionalAttributeProvider != null) {
			// return additionalAttributeProvider.getAdditionalRowHeaderAttributes(item);
		}
		return null;
	}

	private @NonNull String @Nullable [] getAdditionalPreRows() {
		if (additionalAttributeProvider != null) {
			// return additionalAttributeProvider.getAdditionalPreRows();
		}
		return null;
	}

	private String[] getAdditionalHeaderAttributes(final GridColumn column) {
		if (additionalAttributeProvider != null) {
			// return additionalAttributeProvider.getAdditionalHeaderAttributes(column);
		}
		return null;
	}

	private String[] getAdditionalAttributes(final GridItem item, final int i) {
		if (additionalAttributeProvider != null) {
			// return additionalAttributeProvider.getAdditionalAttributes(item, i);
		}
		return null;
	}

	private int getBorders(final GridItem item, final int i) {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getBorders(item, i);
		}
		return 0;
	}

	private @Nullable Object getTypedValue(final GridItem item, final int i) {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTypedValue(item, i);
		}
		return null;
	}

	public final void addCell(final StringWriter sw, final String text, int rowSpan, int colSpan, final String[] attributes, int colIdx, String extra) {
		addCell(sw, "Cell", text, rowSpan, colSpan, attributes, colIdx, extra);
	}

	public final void addCell(final StringWriter sw, final String tag, String text, int rowSpan, int colSpan, final String[] attributes, int colIdx, String style) {

		String range = "";
		if (!"Cell".equals(tag)) {
			// range = tag;
		}
		if (rowSpan != 0 || colSpan != 0) {
			sw.write(String.format("<Cell ss:MergeAcross=\"%d\" ss:MergeDown=\"%d\" ss:Index=\"%d\" %s %s>", colSpan, rowSpan, colIdx, style, range));
		} else {
			sw.write(String.format("<Cell ss:Index=\"%d\" %s %s>", colIdx, style, range));

		}

		if (!"Cell".equalsIgnoreCase(tag)) {
			sw.write(" " + tag + " ");
		}

//		sw.write("<Data ss:Type=\"String\">");
		 if (attributes == null) {
		 sw.write("<Data ss:Type=\"String\">");
		 } else {
		 sw.write("<Data");
		 for (final String attribute : attributes) {
		 sw.write(" " + attribute);
		 }
		 sw.write(">");
		 }

		// if (extra != null) {
		// }
		text = htmlEscape(text);

		if (text == null || text.equals("")) {
			text = "";
		}

		sw.write(text); // TODO: escape this for HTML
		sw.write("</Data>");
		sw.write("</Cell>");
	}

	/**
	 * Does rough HTML escaping on a piece of text.
	 * 
	 * @param text
	 * @return
	 */
	public final String htmlEscape(final String text) {
		if (text == null) {
			return null;
		}
		return text //
				.replace("&", "&amp;") //
				.replace(">", "&gt;") //
				.replace("<", "&gt;") //
				.replace("\"", "&quot;") //
				.replace("'", "&#39;") //
		;
	}

	public final void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = b;
	}

	public final boolean isShowBackgroundColours() {
		return showBackgroundColours;
	}

	public final void setShowBackgroundColours(final boolean showBackgroundColours) {
		this.showBackgroundColours = showBackgroundColours;
	}

	public void setAdditionalAttributeProvider(final IAdditionalAttributeProvider additionalAttributeProvider) {
		this.additionalAttributeProvider = additionalAttributeProvider;
	}

}
