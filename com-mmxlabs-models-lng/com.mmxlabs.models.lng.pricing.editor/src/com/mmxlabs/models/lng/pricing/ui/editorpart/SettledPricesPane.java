/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.ui.actions.AddDateToIndexAction;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * The {@link SettledPricesPane} displays data for various indices in a tree format. The {@link IndexTreeTransformer} is used to combine the different indices into an internal dynamic EObject tree
 * datastructure.
 * 
 * Note - call {@link #setInput(PricingModel)} on {@link SettledPricesPane} rather than the {@link Viewer}.
 * 
 * @author Simon Goodall
 * 
 */
public class SettledPricesPane extends ScenarioTableViewerPane {

	private static final LocalDate dateZero = LocalDate.of(2000, 1, 1);

	private LocalDate minDisplayDate = null;
	private LocalDate maxDisplayDate = null;

	// private final IndexTreeTransformer transformer = new IndexTreeTransformer();
	// private final Map<DataType, SeriesParser> seriesParsers = new EnumMap<>(DataType.class);
	private PricingModel pricingModel;

	private GridViewerColumn nameViewerColumn;
	private GridViewerColumn unitViewerColumn;

	private Action unitAction;

	private ModelMarketCurveProvider marketCurveProvider;

	public SettledPricesPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.marketCurveProvider = location.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
	}

	@Override
	public void dispose() {
		this.pricingModel = null;
		this.marketCurveProvider = null;
		// seriesParsers.clear();
		// transformer.dispose();
		super.dispose();
	}

	/**
	 * Ensures that a given date is visible in the editor column range, as long as the editor is open.
	 * 
	 * @param date
	 */
	public void selectDateColumn(final LocalDate date) {
		if (date == null) {
			return;
		}

		if (minDisplayDate == null || minDisplayDate.isAfter(date)) {
			minDisplayDate = date;
		}

		if (maxDisplayDate == null || maxDisplayDate.isBefore(date)) {
			maxDisplayDate = date;
		}

		((IndexTableViewer) viewer).redisplayDateRange(date);
	}

	/**
	 * Hooks the {@link AddDateToIndexAction} to the Add menu
	 */
	@Override
	protected Action createAddAction(final EReference containment) {
		// final Action[] actions = new Action[] { new AddDateToIndexAction(this) };
		//
		// final List<Pair<EClass, IAddContext>> items = new LinkedList<>();
		// items.add(new Pair<>(PricingPackage.Literals.COMMODITY_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES)));
		// items.add(new Pair<>(PricingPackage.Literals.BASE_FUEL_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES)));
		// items.add(new Pair<>(PricingPackage.Literals.CHARTER_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES)));
		// items.add(new Pair<>(PricingPackage.Literals.CURRENCY_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES)));
		//
		// return AddModelAction.create(items, actions);
		//
		return null;
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		// Add in Check for Tree
		final ScenarioTableViewer result = new IndexTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart());
		result.getGrid().setTreeLinesVisible(true);
		return result;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		nameViewerColumn = addTypicalColumn("Name", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				// Skip tree model elements
				// if (transformer.getNodeClass().isInstance(object)) {
				return false;
				// }
				//
				// return super.canEdit(object);
			}
		}));
	}

	private void updateDateRange(final PricingModel pricingModel) {
		if (pricingModel == null) {
			return;
		}
		for (DatePointContainer indexObject : pricingModel.getSettledPrices()) {

			for (final DatePoint point : indexObject.getPoints()) {
				LocalDate d = point.getDate();
				if (d == null) {
					continue;
				}
				if (minDisplayDate == null || minDisplayDate.isAfter(d)) {
					minDisplayDate = d;
				}
				if (maxDisplayDate == null || maxDisplayDate.isBefore(d)) {
					maxDisplayDate = d;
				}
			}
		}
	}

	protected class IndexTableViewer extends ScenarioTableViewer {

		public IndexTableViewer(final Composite parent, final int style, final IScenarioEditingLocation part) {
			super(parent, style, part);
		}

		@Override
		protected void internalRefresh(final Object element) {
			// createSeriesParsers();
			super.internalRefresh(element);
		}

		@Override
		protected void internalRefresh(final Object element, final boolean updateLabels) {
			// createSeriesParsers();
			super.internalRefresh(element, updateLabels);
		}

		protected void redisplayDateRange(final LocalDate selected) {

			if (minDisplayDate != null && maxDisplayDate != null) {
				Grid grid = null;
				if (SettledPricesPane.this.viewer instanceof GridTreeViewer) {
					grid = ((GridTreeViewer) SettledPricesPane.this.viewer).getGrid();
				} else if (SettledPricesPane.this.viewer instanceof GridTableViewer) {
					grid = ((GridTableViewer) SettledPricesPane.this.viewer).getGrid();
				}

				if (grid != null) {
					final int columnCount = grid.getColumnCount();
					for (int i = columnCount - 1; i > 1; i--) {
						final GridColumn column = grid.getColumn(i);
						getSortingSupport().removeSortableColumn(column);
						column.dispose();
					}
					LocalDate localDate = minDisplayDate;
					while (!localDate.isAfter(maxDisplayDate)) {
						if (!(localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
							addColumn(localDate, true);
						}
						localDate = localDate.plusDays(1);

					}
				}
			}

			viewer.refresh();
		}

		@Override
		protected void inputChanged(final Object input, final Object oldInput) {
			super.inputChanged(input, oldInput);

			// createSeriesParsers();
			updateDateRange(pricingModel);
			redisplayDateRange(null);
		}

		@Override
		protected void doCommandStackChanged() {
			// transformer.update(pricingModel);
			super.doCommandStackChanged();
		}

		private void addColumn(final LocalDate cal, final boolean sortable) {

			final String date = String.format("%4d-%02d-%02d", cal.getYear(), cal.getMonthValue(), cal.getDayOfMonth());
			final GridViewerColumn col = addSimpleColumn(date, sortable);
			col.getColumn().setData("date", cal);

			final ICellRenderer renderer = new ICellRenderer() {

				@Override
				public String render(final Object object) {
					return object.toString();
				}

				@Override
				public boolean isValueUnset(final Object object) {
					return false;
				}

				@Override
				public Object getFilterValue(final Object object) {
					return null;
				}

				@Override
				public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
					return null;
				}

				@Override
				public Comparable<?> getComparable(final Object element) {

					final LocalDate colDate = (LocalDate) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {

						return number.doubleValue();
					}

					return null;
				}
			};

			col.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
			col.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);

			col.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {

				@Override
				public String getText(final Object element) {

					final LocalDate colDate = (LocalDate) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						return String.format("%01.3f", number.doubleValue());
					}

					return null;
				}
			});
		}

	}

	private @Nullable Number getNumberForElement(Object element, final LocalDate colDate) {

		if (element instanceof DatePointContainer) {
			DatePointContainer c = (DatePointContainer) element;
			return marketCurveProvider.getSettledPrices(c.getName()).get(colDate);
		}
		return null;
	}

	public void setInput(final PricingModel pricingModel) {
		this.pricingModel = pricingModel;

		getScenarioViewer().setInput(pricingModel);
	}

	@Override
	protected void enableOpenListener() {
	}
}
