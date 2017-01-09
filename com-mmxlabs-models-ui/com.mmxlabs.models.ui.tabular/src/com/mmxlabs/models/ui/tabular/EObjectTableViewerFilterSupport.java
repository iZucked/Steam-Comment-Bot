/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.filter.FilterUtils;
import com.mmxlabs.models.ui.tabular.filter.IFilter;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * Filtering related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * 
 */
public class EObjectTableViewerFilterSupport {

	public static final String COLUMN_MNEMONICS = "COLUMN_MNEMONICS";

	private final ColumnViewer viewer;
	private final Grid grid;
	private IFilter filter = null;

	/**
	 */
	protected final Set<String> allMnemonics = new HashSet<String>();

	public EObjectTableViewerFilterSupport(final ColumnViewer viewer, final Grid grid) {
		this.viewer = viewer;
		this.grid = grid;
	}

	public void setFilterString(final String filterString) {
		if (filterString.isEmpty()) {
			filter = null;
		}
		final FilterUtils utils = new FilterUtils();
		filter = utils.parseFilterString(filterString);
		viewer.refresh(false);
	}

	/**
	 * Get possible (unfiltered) values for the column with the given name
	 * 
	 * @param columnName
	 * @return
	 */
	public Set<String> getDistinctValues(final String columnName) {
		final TreeSet<String> result = new TreeSet<String>();

		for (final GridColumn column : grid.getColumns()) {
			if (column.getText().equals(columnName)) {
				final ICellRenderer renderer = (ICellRenderer) column.getData(EObjectTableViewer.COLUMN_RENDERER);
				final EMFPath path = (EMFPath) column.getData(EObjectTableViewer.COLUMN_PATH);
				final Object[] elements = ((IStructuredContentProvider) viewer.getContentProvider()).getElements(viewer.getInput());
				for (final Object element : elements) { // viewer.getCurrentElements()) {
					if (element instanceof EObject) {
						result.add(renderer.render(path.get((EObject) element)));
					}
				}
				return result;
			}
		}

		return result;
	}

	/**
	 * @return names that can be used in the filter (see {@link #setFilterString(String)})
	 */
	public Map<String, List<String>> getColumnMnemonics() {
		final Map<String, List<String>> ms = new TreeMap<String, List<String>>();

		for (final GridColumn column : grid.getColumns()) {
			final List<String> mnemonics = (List<String>) column.getData(COLUMN_MNEMONICS);
			ms.put(column.getText(), mnemonics);
		}

		return ms;
	}

	public ViewerFilter createViewerFilter() {
		return new ViewerFilter() {
			@Override
			public boolean select(final Viewer _viewer, final Object parentElement, final Object element) {
				if (filter == null) {
					return true;
				}

				/**
				 * This map contains representations of each column for this object, both the real value and the display value.
				 */
				final Map<String, Pair<?, ?>> attributes = new HashMap<String, Pair<?, ?>>();
				// this could probably be much faster
				for (final GridColumn column : grid.getColumns()) {
					final ICellRenderer renderer = (ICellRenderer) column.getData(EObjectTableViewer.COLUMN_RENDERER);
					final Object columnPath = column.getData(EObjectTableViewer.COLUMN_PATH);

					Object fieldValue = element;
					if (columnPath instanceof EMFPath[]) {
						final EMFPath[] paths = (EMFPath[]) columnPath;
						if (fieldValue != null) {
							boolean found = false;
							for (final EMFPath p : paths) {
								final Object x = p.get((EObject) fieldValue);
								if (x != null) {
									fieldValue = x;
									found = true;
									break;
								}
							}
							if (!found) {
								fieldValue = null;
							}
						}
					} else if (columnPath instanceof EMFPath) {
						final EMFPath p = (EMFPath) columnPath;
						if (fieldValue != null) {
							final Object x = p.get((EObject) fieldValue);
							fieldValue = x;
						}
					}

					if (fieldValue == null)
						continue;

					final Object filterValue = renderer.getFilterValue(fieldValue);
					final Object renderValue = renderer.render(fieldValue);

					final List<String> mnemonics = (List<String>) column.getData(COLUMN_MNEMONICS);
					for (final String m : mnemonics) {
						// make sure we add the attribute with a unique key
						String key = m;
						int suffix = 2;
						while (attributes.containsKey(key)) {
							key = m + suffix;
							suffix += 1;
						}
						attributes.put(key, new Pair<Object, Object>(filterValue, renderValue));
					}
				}

				return filter.matches(attributes);

			}
		};
	}

	public void createColumnMnemonics(final GridColumn column, final String columnName) {
		setColumnMnemonics(column, makeMnemonics(columnName));
	}

	/**
	 */
	public void setColumnMnemonics(final GridColumn column, final List<String> mnemonics) {
		column.setData(COLUMN_MNEMONICS, mnemonics);
		for (final String string : mnemonics) {
			allMnemonics.add(string);
		}
	}

	/**
	 */
	protected String uniqueMnemonic(final String mnemonic) {
		String result = mnemonic;
		int suffix = 2;
		while (allMnemonics.contains(result)) {
			result = mnemonic + suffix++;
		}
		return result;
	}

	/**
	 */
	protected List<String> makeMnemonics(final String columnName) {
		final LinkedList<String> result = new LinkedList<String>();

		result.add(uniqueMnemonic(columnName.toLowerCase().replace(" ", "")));
		String initials = "";
		boolean ws = true;
		for (int i = 0; i < columnName.length(); i++) {
			final char c = columnName.charAt(i);
			if (Character.isWhitespace(c)) {
				ws = true;
			} else {
				if (ws) {
					initials += c;
				}
				ws = false;
			}
		}
		result.add(uniqueMnemonic(initials.toLowerCase()));

		return result;
	}
}
