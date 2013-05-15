package com.mmxlabs.models.ui.tabular;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.tabular.filter.FilterUtils;
import com.mmxlabs.models.ui.tabular.filter.IFilter;
import com.mmxlabs.models.util.emfpath.EMFPath;

/**
 * Filtering related code for the {@link EObjectTableViewer}
 * 
 * @author Simon Goodall
 * @since 4.0
 * 
 */
public class EObjectTableViewerFilterSupport {

	private final EObjectTableViewer viewer;
	private IFilter filter = null;

	public EObjectTableViewerFilterSupport(EObjectTableViewer viewer) {
		this.viewer = viewer;
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

		for (final GridColumn column : viewer.getGrid().getColumns()) {
			if (column.getText().equals(columnName)) {
				final ICellRenderer renderer = (ICellRenderer) column.getData(EObjectTableViewer.COLUMN_RENDERER);
				final EMFPath path = (EMFPath) column.getData(EObjectTableViewer.COLUMN_PATH);
				for (final EObject element : viewer.getCurrentElements()) {
					result.add(renderer.render(path.get(element)));
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

		for (final GridColumn column : viewer.getGrid().getColumns()) {
			final List<String> mnemonics = (List<String>) column.getData(EObjectTableViewer.COLUMN_MNEMONICS);
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
				for (final GridColumn column : viewer.getGrid().getColumns()) {
					final ICellRenderer renderer = (ICellRenderer) column.getData(EObjectTableViewer.COLUMN_RENDERER);
					final EMFPath path = (EMFPath) column.getData(EObjectTableViewer.COLUMN_PATH);
					if (path == null)
						continue;
					final Object fieldValue = path.get((EObject) element);
					final Object filterValue = renderer.getFilterValue(fieldValue);
					final Object renderValue = renderer.render(fieldValue);

					final List<String> mnemonics = (List<String>) column.getData(EObjectTableViewer.COLUMN_MNEMONICS);
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
}
