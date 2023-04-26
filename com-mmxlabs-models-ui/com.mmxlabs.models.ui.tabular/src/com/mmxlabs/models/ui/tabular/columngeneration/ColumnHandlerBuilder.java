package com.mmxlabs.models.ui.tabular.columngeneration;

import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;

public class ColumnHandlerBuilder {

	private final @NonNull ColumnBlock block;
	private final @NonNull String title;
	private final TriFunction<ColumnBlock, ColumnHandler, Boolean, ColumnHandler> configureAction;
	private final @NonNull IColumnFactory columnFactory;

	private ICellRenderer renderer;
	private ICellManipulator manipulator;
	private Predicate<Object> rowFilter;
	private ETypedElement[] singleElementPath;
	private ETypedElement[][] multiElementPath;

	private boolean createImmediately = true;

	private boolean detail = true;
	private boolean summary = true;
	private String tooltip = null;

	private EMFMultiPath multiPathForSorting;
	private Function<GridColumnGroup, TreeListener> treeListener;

	public ColumnHandlerBuilder(final @NonNull ColumnBlock block, final @NonNull String title, final TriFunction<ColumnBlock, ColumnHandler, Boolean, ColumnHandler> configureAction,
			@NonNull final IColumnFactory columnFactory) {
		this.block = block;
		this.title = title;
		this.configureAction = configureAction;
		this.columnFactory = columnFactory;
	}

	public ColumnHandler build() {
		final ColumnHandler handler;
		if (multiElementPath != null) {
			handler = new ColumnHandler(block, rowFilter, renderer, manipulator, multiElementPath, title, columnFactory);
		} else if (singleElementPath != null) {
			handler = new ColumnHandler(block, rowFilter, renderer, manipulator, singleElementPath, title, columnFactory);
		} else {
			handler = new ColumnHandler(block, rowFilter, renderer, manipulator, new ETypedElement[0], title, columnFactory);
		}
		
		handler.detailColumn = detail;
		handler.summaryColumn = summary;
		handler.multiPathForSorting = multiPathForSorting;
		
		if (tooltip != null) {
			handler.setTooltip(tooltip);
		}
		handler.treeListener = treeListener;

		return configureAction.apply(block, handler, createImmediately);

	}

	public ColumnHandlerBuilder withElementPath(final ETypedElement... singleElementPath) {
		this.singleElementPath = singleElementPath;
		this.multiElementPath = null;
		return this;
	}

	public ColumnHandlerBuilder withMultiElementPath(final ETypedElement[][] multiElementPath) {
		this.multiElementPath = multiElementPath;
		this.singleElementPath = null;
		return this;
	}

	public ColumnHandlerBuilder withMultiPathForSorting(final EMFMultiPath multiPathForSorting) {
		this.multiPathForSorting = multiPathForSorting;
		return this;
	}

	public ColumnHandlerBuilder withCellRenderer(final ICellRenderer renderer) {
		this.renderer = renderer;
		return this;
	}

	public ColumnHandlerBuilder withCellManipulator(final ICellManipulator manipulator) {
		this.manipulator = manipulator;
		return this;
	}

	public <T extends ICellRenderer & ICellManipulator> ColumnHandlerBuilder withCellEditor(final T editor) {
		this.renderer = editor;
		this.manipulator = editor;
		return this;
	}

	/**
	 * Returns true to process the row, false to ignore the row
	 * 
	 * @param rowFilter
	 * @return
	 */
	public ColumnHandlerBuilder withRowFilter(@Nullable final Predicate<Object> rowFilter) {
		this.rowFilter = rowFilter;
		return this;
	}

	public ColumnHandlerBuilder withCreateImmediately(final boolean createImmediately) {
		this.createImmediately = createImmediately;
		return this;
	}

	public ColumnHandlerBuilder asDetailOnly() {
		this.detail = true;
		this.summary = false;
		return this;
	}

	public ColumnHandlerBuilder asSummaryOnly() {
		this.detail = false;
		this.summary = true;
		return this;
	}

	public ColumnHandlerBuilder withTooltip(final String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public ColumnHandlerBuilder withGroupListener(final Function<GridColumnGroup, TreeListener> treeListener) {
		this.treeListener = treeListener;
		return this;
	}

	/**
	 * Sets the label of the column group when expanded, blank when collapsed.
	 * 
	 * @param expandedLabel
	 * @return
	 */
	public ColumnHandlerBuilder withGroupExpandedLabel(final @NonNull String expandedLabel) {
		this.treeListener = group -> new TreeListener() {

			@Override
			public void treeExpanded(final TreeEvent e) {
				group.setText(expandedLabel);

			}

			@Override
			public void treeCollapsed(final TreeEvent e) {
				group.setText("");

			}
		};
		return this;
	}
}
