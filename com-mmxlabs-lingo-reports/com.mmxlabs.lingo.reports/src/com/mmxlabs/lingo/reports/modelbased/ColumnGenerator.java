/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;

import com.google.common.collect.Sets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnSortFunction;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnSortOrder;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerFilterSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.IComparableProvider;
import com.mmxlabs.models.ui.tabular.IFilterProvider;

public class ColumnGenerator {

	private static Set<Class<?>> numericTypes = Sets.newHashSet(short.class, int.class, long.class, float.class, double.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
			Number.class);
	private static Set<Class<?>> dateTypes = Sets.newHashSet(LocalDate.class, LocalDateTime.class);

	public static class ColumnInfo{
		final public Map<Integer, Field> mapOfFields;
		final public Map<Field, GridViewerColumn> mapOfFieldColumns;
		
		public ColumnInfo(final Map<Integer, Field> mapOfFields, final Map<Field, GridViewerColumn> mapOfFieldColumns) {
			this.mapOfFields = mapOfFields;
			this.mapOfFieldColumns = mapOfFieldColumns;
		}
	}
	
	public static ColumnInfo createColumns(final GridTableViewer viewer, @Nullable final EObjectTableViewerSortingSupport sortingSupport, @Nullable final EObjectTableViewerFilterSupport filterSupport,
			final Class<?> cls, final BiConsumer<ViewerCell, Field> styler) {

		final List<Triple<Integer, GridViewerColumn, Boolean>> defaultSortColumns = new LinkedList<>();

		final Map<Integer, Field> mapOfFields = new HashMap<>();
		final Map<Field, GridViewerColumn> mapOfFieldColumns = new HashMap<>();
		int counter = -1;
		
		for (final Field f : cls.getFields()) {
			if (f.getAnnotation(LingoIgnore.class) != null) {
				continue;
			}

			final ColumnName columnName = f.getAnnotation(ColumnName.class);
			final GridViewerColumn col;
			
			if (columnName != null) {
				col = new GridViewerColumn(viewer, SWT.NONE);
				if (!columnName.lingo().isEmpty()) {
					col.getColumn().setText(columnName.lingo());
				} else {
					col.getColumn().setText(columnName.value());
				}
			} else {
				continue;
			}
			GridViewerHelper.configureLookAndFeel(col);
			mapOfFields.put(++counter, f);
			mapOfFieldColumns.put(f, col);

			final Function<Object, String> formatter;
			Function<Object, Comparable> sortFunction;
			if (dateTypes.contains(f.getType())) {
				DateTimeFormatter dtf;
				final LingoFormat format = f.getAnnotation(LingoFormat.class);
				if (format != null) {
					dtf = DateTimeFormatter.ofPattern(format.value());
				} else {
					dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				}

				formatter = o -> {
					if (o == null) {
						return null;
					} else {
						return dtf.format((TemporalAccessor) o);
					}
				};
				sortFunction = o -> (Comparable<?>) o;

			} else if (numericTypes.contains(f.getType())) {
				DecimalFormat df;
				final LingoFormat format = f.getAnnotation(LingoFormat.class);
				if (format != null) {
					df = new DecimalFormat(format.value());
				} else {
					df = new DecimalFormat();
				}

				formatter = o -> {
					if (o == null) {
						return null;
					} else {
						return df.format(o);
					}
				};
				sortFunction = o -> (Comparable<?>) o;

			} else {
				formatter = o -> {
					if (o == null) {
						return "";
					} else {
						return o.toString();
					}
				};
				sortFunction = o -> {
					if (o == null) {
						return null;
					} else {
						return o.toString();
					}
				};
			}

			final Function<Object, String> rendererFunction = data -> {
				if (cls.isInstance(data)) {
					try {
						return (formatter.apply(f.get(data)));
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			};

			if (sortingSupport != null && sortFunction != null) {
				sortingSupport.addSortableColumn(viewer, col, col.getColumn());

				ColumnSortFunction columnSortFunction = f.getAnnotation(ColumnSortFunction.class);
				Method altSortMethod = null;
				if (columnSortFunction != null) {
					try {
						altSortMethod = cls.getMethod(columnSortFunction.value());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				final Method pAltSortMethod = altSortMethod;

				final IComparableProvider provider = (m) -> {
					try {
						if (pAltSortMethod != null) {
							return sortFunction.apply(pAltSortMethod.invoke(m));
						} else {
							return sortFunction.apply(f.get(m));
						}
					} catch (final Exception e1) {
						e1.printStackTrace();
					}
					return null;
				};

				col.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, provider);

				ColumnSortOrder sortOrder = f.getAnnotation(ColumnSortOrder.class);
				if (sortOrder != null) {
					defaultSortColumns.add(new Triple<>(sortOrder.value(), col, sortOrder.ascending()));
				}
			}
			if (filterSupport != null) {
				filterSupport.createColumnMnemonics(col.getColumn(), col.getColumn().getText());

				final IFilterProvider filterProvider = new IFilterProvider() {
					@Override
					public @Nullable String render(final Object object) {
						return rendererFunction.apply(object);
					}

					@Override
					public Object getFilterValue(final Object object) {
						return rendererFunction.apply(object);
					}
				};

				col.getColumn().setData(EObjectTableViewer.COLUMN_FILTER, filterProvider);
			}
			col.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {
					final Widget item = cell.getItem();
					cell.setText(rendererFunction.apply(item.getData()));
					styler.accept(cell, f);
				}
			});
		}

		if (sortingSupport != null) {
			if (!defaultSortColumns.isEmpty()) {
				// Reverse sort.
				Collections.sort(defaultSortColumns, (a, b) -> b.getFirst() - a.getFirst());
				// Set initial sort order
				defaultSortColumns.forEach(p -> {
					sortingSupport.setSortDescending(!p.getThird());
					sortingSupport.sortColumnsBy(p.getSecond().getColumn());
				});
			}
		}
		
		return new ColumnInfo(mapOfFields, mapOfFieldColumns);
	}
}
