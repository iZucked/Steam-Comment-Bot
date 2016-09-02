/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.time.YearMonth;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.actions.AddDateToIndexAction;
import com.mmxlabs.models.lng.pricing.ui.editorpart.IndexTreeTransformer.DataType;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * The {@link IndexPane} displays data for various indices in a tree format. The {@link IndexTreeTransformer} is used to combine the different indices into an internal dynamic EObject tree
 * datastructure.
 * 
 * Note - call {@link #setInput(PricingModel)} on {@link IndexPane} rather than the {@link Viewer}.
 * 
 * @author Simon Goodall
 * 
 */
public class IndexPane extends ScenarioTableViewerPane {

	private static final YearMonth dateZero = YearMonth.now();

	private YearMonth minDisplayDate = null;
	private YearMonth maxDisplayDate = null;

	private final IndexTreeTransformer transformer = new IndexTreeTransformer();
	private final Map<DataType, SeriesParser> seriesParsers = new EnumMap<>(DataType.class);
	private PricingModel pricingModel;

	private GridViewerColumn nameViewerColumn;

	public IndexPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);

	}

	@Override
	public void dispose() {
		this.pricingModel = null;
		seriesParsers.clear();
		transformer.dispose();
		super.dispose();
	}

	/**
	 * Ensures that a given date is visible in the editor column range, as long as the editor is open.
	 * 
	 * @param date
	 */
	public void selectDateColumn(final YearMonth date) {
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
		final Action[] actions = new Action[] { new AddDateToIndexAction(this) };

		final List<Pair<EClass, IAddContext>> items = new LinkedList<>();
		items.add(new Pair<>(PricingPackage.Literals.COMMODITY_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES)));
		items.add(new Pair<>(PricingPackage.Literals.BASE_FUEL_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES)));
		items.add(new Pair<>(PricingPackage.Literals.CHARTER_INDEX, getAddContext(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES)));

		return AddModelAction.create(items, actions);
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		// Add in Check for Tree
		final ScenarioTableViewer result = new IndexTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart());
		result.getGrid().setTreeLinesVisible(true);
		return result;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		// Override default content provider
		getScenarioViewer().init(transformer.createContentProvider(), commandStack);

		// TODO: API subject to change!
		getScenarioViewer().setCurrentContainerAndContainment(pricingModel, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA);

		ColumnViewerToolTipSupport.enableFor((ColumnViewer) scenarioViewer, ToolTip.NO_RECREATE);

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof NamedIndexContainer<?>) {
					final Index<?> index = ((NamedIndexContainer<?>) element).getData();
					if (index instanceof DerivedIndex<?>) {
						return ((DerivedIndex<?>) index).getExpression();
					}
				}
				return null;
			}
		});

		nameViewerColumn = addTypicalColumn("Name", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				// Skip tree model elements
				if (transformer.getNodeClass().isInstance(object)) {
					return false;
				}

				return super.canEdit(object);
			}
		}));
		// Enable the tree controls on this column
		nameViewerColumn.getColumn().setTree(true);

		addTypicalColumn("Units", new BasicAttributeManipulator(PricingPackage.Literals.NAMED_INDEX_CONTAINER__UNITS, getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {
				// Skip tree model elements
				if (transformer.getNodeClass().isInstance(object)) {
					return false;
				}

				return super.canEdit(object);
			}
		});

		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(Object object) {

				object = getIndex(object);

				if (object instanceof DerivedIndex) {
					return "Expression";
				} else if (object instanceof DataIndex) {
					return "Data";
				}
				return "";
			}
		});
	}

	private void createSeriesParsers() {
		// Remove existing parsers
		seriesParsers.clear();

		for (final DataType dt : DataType.values()) {

			final SeriesParser seriesParser = new SeriesParser();
			if (pricingModel != null) {
				@SuppressWarnings("unchecked")
				final List<EObject> indexObjects = (List<EObject>) pricingModel.eGet(dt.getContainerFeature());

				for (final EObject indexObject : indexObjects) {

					if (!indexObject.eIsSet(dt.getIndexFeature())) {
						continue;
					}

					String name = "Unknown";
					if (indexObject instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) indexObject;
						name = namedObject.getName();
					}

					final Index<?> idx = (Index<?>) indexObject.eGet(dt.getIndexFeature());
					if (idx instanceof DataIndex) {
						PriceIndexUtils.addSeriesDataFromDataIndex(seriesParser, name, dateZero, (DataIndex<? extends Number>) idx);
					} else if (idx instanceof DerivedIndex) {
						seriesParser.addSeriesExpression(name, ((DerivedIndex) idx).getExpression());
					}
				}
			}
			seriesParsers.put(dt, seriesParser);
		}
	}

	private void updateDateRange(final PricingModel pricingModel) {

		for (final DataType dt : DataType.values()) {

			@SuppressWarnings("unchecked")
			final List<EObject> indexObjects = (List<EObject>) pricingModel.eGet(dt.getContainerFeature());

			for (final EObject indexObject : indexObjects) {

				if (!indexObject.eIsSet(dt.getIndexFeature())) {
					continue;
				}

				final Index<?> idx = (Index<?>) indexObject.eGet(dt.getIndexFeature());
				if (!(idx instanceof DataIndex<?>)) {
					continue;
				}

				for (final YearMonth d : idx.getDates()) {
					if (minDisplayDate == null || minDisplayDate.isAfter(d)) {
						minDisplayDate = d;
					}
					if (maxDisplayDate == null || maxDisplayDate.isBefore(d)) {
						maxDisplayDate = d;
					}
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
			createSeriesParsers();
			super.internalRefresh(element);
		}

		@Override
		protected void internalRefresh(final Object element, final boolean updateLabels) {
			createSeriesParsers();
			super.internalRefresh(element, updateLabels);
		}

		protected void redisplayDateRange(final YearMonth selected) {

			if (minDisplayDate != null && maxDisplayDate != null) {
				Grid grid = null;
				if (IndexPane.this.viewer instanceof GridTreeViewer) {
					grid = ((GridTreeViewer) IndexPane.this.viewer).getGrid();
				} else if (IndexPane.this.viewer instanceof GridTableViewer) {
					grid = ((GridTableViewer) IndexPane.this.viewer).getGrid();
				}

				if (grid != null) {
					final int columnCount = grid.getColumnCount();
					for (int i = columnCount - 1; i > 1; i--) {
						final GridColumn column = grid.getColumn(i);
						getSortingSupport().removeSortableColumn(column);
						column.dispose();
					}
					YearMonth localDate = minDisplayDate;
					while (!localDate.isAfter(maxDisplayDate)) {
						addColumn(localDate, true);
						localDate = localDate.plusMonths(1);
					}
				}
			}

			viewer.refresh();
		}

		@Override
		protected void inputChanged(final Object input, final Object oldInput) {
			super.inputChanged(input, oldInput);

			createSeriesParsers();
			updateDateRange(pricingModel);
			redisplayDateRange(null);
		}

		@Override
		protected void doCommandStackChanged() {
			transformer.update(pricingModel);
			super.doCommandStackChanged();
		}

		private void addColumn(final YearMonth cal, final boolean sortable) {

			final String date = String.format("%4d-%02d", cal.getYear(), (cal.getMonthValue()));
			final GridViewerColumn col = addSimpleColumn(date, sortable);
			col.getColumn().setData("date", cal);

			final ICellRenderer renderer = new ICellRenderer() {

				@Override
				public String render(final Object object) {
					return object.toString();
				}

				@Override
				public boolean isValueUnset(Object object) {
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

					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						final DataType dt = getDataTypeForElement(element);
						if (dt.useIntegers()) {
							return number.intValue();
						} else {
							return number.doubleValue();
						}
					}

					return null;
				}
			};

			col.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
			col.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);
			final ICellManipulator manipulator = new ICellManipulator() {

				@SuppressWarnings("unchecked")
				@Override
				public void setValue(Object element, final Object value) {
					final DataType dt = getDataTypeForElement(element);
					if (dt != null) {

						// Unwrap index from owner
						if (element instanceof EObject) {
							final EObject eObject = (EObject) element;
							if (eObject.eIsSet(dt.getIndexFeature())) {
								element = eObject.eGet(dt.getIndexFeature());
							}
						}
						if (element instanceof DataIndex) {
							final YearMonth colDate = (YearMonth) col.getColumn().getData("date");

							if (dt.useIntegers()) {
								setIndexPoint((Integer) value, (DataIndex<Integer>) element, colDate);
							} else {
								setIndexPoint((Double) value, (DataIndex<Double>) element, colDate);
							}
						}
					}
				}

				private <T extends Number> void setIndexPoint(final T value, final DataIndex<T> di, final YearMonth colDate) {

					for (final IndexPoint<T> p : di.getPoints()) {
						if (p.getDate().getYear() == colDate.getYear() && p.getDate().getMonthValue() == colDate.getMonthValue()) {

							final Command cmd;
							if (value == null) {
								cmd = RemoveCommand.create(getEditingDomain(), p);
							} else {
								cmd = SetCommand.create(getEditingDomain(), p, PricingPackage.eINSTANCE.getIndexPoint_Value(), value);
							}
							if (!cmd.canExecute()) {
								throw new RuntimeException("Unable to execute index set command");
							}
							getEditingDomain().getCommandStack().execute(cmd);

							return;
						}
					}
					if (value != null) {
						final IndexPoint<T> p = PricingFactory.eINSTANCE.createIndexPoint();
						p.setDate(colDate);
						p.setValue(value);
						final Command cmd = AddCommand.create(getEditingDomain(), di, PricingPackage.eINSTANCE.getDataIndex_Points(), p);
						if (!cmd.canExecute()) {
							throw new RuntimeException("Unable to execute index add command");
						}
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

				@Override
				public Object getValue(final Object element) {

					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						final DataType dt = getDataTypeForElement(element);
						if (dt.useIntegers()) {
							return number.intValue();
						} else {
							return number.doubleValue();
						}
					}

					return null;
				}

				@Override
				public CellEditor getCellEditor(final Composite parent, final Object object) {

					final DataType dt = getDataTypeForElement(object);
					if (dt != null) {
						final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
						final NumberFormatter formatter;

						if (dt.useIntegers()) {
							formatter = new IntegerFormatter("#,###.###");
						} else {
							formatter = new DoubleFormatter("#,###.###");
						}

						formatter.setFixedLengths(false, false);

						result.setFormatter(formatter);

						return result;
					}
					return null;
				}

				@Override
				public boolean canEdit(Object element) {
					final DataType dt = getDataTypeForElement(element);
					if (dt != null) {

						// Unwrap index from owner
						if (element instanceof EObject) {
							final EObject eObject = (EObject) element;
							if (eObject.eIsSet(dt.getIndexFeature())) {
								element = eObject.eGet(dt.getIndexFeature());
							}
						}
					}
					return (element instanceof DataIndex<?>);
				}
			};
			col.getColumn().setData(EObjectTableViewer.COLUMN_MANIPULATOR, manipulator);

			col.setEditingSupport(new EditingSupport((ColumnViewer) viewer) {
				@Override
				protected boolean canEdit(final Object element) {
					return (lockedForEditing == false) && (manipulator != null) && manipulator.canEdit(element);
				}

				@Override
				protected CellEditor getCellEditor(final Object element) {
					Composite grid = null;// = ((GridTableViewer) IndexPane.this.viewer).getGrid();
					if (viewer instanceof GridTreeViewer) {
						grid = ((GridTreeViewer) IndexPane.this.viewer).getGrid();
					} else if (viewer instanceof GridTableViewer) {
						grid = ((GridTableViewer) IndexPane.this.viewer).getGrid();
					} else if (viewer.getControl() instanceof Composite) {
						grid = (Composite) viewer.getControl();
					}
					return manipulator.getCellEditor(grid, element);
				}

				@Override
				protected Object getValue(final Object element) {
					return manipulator.getValue(element);
				}

				@Override
				protected void setValue(final Object element, final Object value) {
					// a value has come out of the celleditor and is being set on
					// the element.
					if (lockedForEditing) {
						return;
					}
					manipulator.setValue(element, value);
					refresh();
				}
			});

			ColumnViewerToolTipSupport.enableFor((ColumnViewer) viewer, ToolTip.NO_RECREATE);

			col.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {

				@Override
				public Color getForeground(Object element) {

					final DataType dt = getDataTypeForElement(element);
					if (dt != null) {
						// Unwrap index from owner
						if (element instanceof EObject) {
							final EObject eObject = (EObject) element;
							if (eObject.eClass().getEAllStructuralFeatures().contains(dt.getIndexFeature()) && eObject.eIsSet(dt.getIndexFeature())) {
								element = eObject.eGet(dt.getIndexFeature());
							}
						}
					}
					if (element instanceof DerivedIndex<?>) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
					return super.getForeground(element);
				}

				@Override
				public String getText(final Object element) {

					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						final DataType dt = getDataTypeForElement(element);
						if (dt.useIntegers()) {
							return String.format("%d", number.intValue());
						} else {
							return String.format("%01.3f", number.doubleValue());
						}
					}

					return null;
				}
			});
		}
	}

	/**
	 */
	@Override
	protected void enableOpenListener() {

		// Enable the tree controls on this column
		scenarioViewer.getGrid().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {

				Point p = new Point(e.x, e.y);
				GridColumn column = scenarioViewer.getGrid().getColumn(p);
				if (column == nameViewerColumn.getColumn()) {
					editIndex();
				}
			}

		});

		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
			}
		});
	}

	private @Nullable Index<?> getIndex(@Nullable final Object o) {
		if (o instanceof CommodityIndex) {
			return ((CommodityIndex) o).getData();
		} else if (o instanceof CharterIndex) {
			return ((CharterIndex) o).getData();
		} else if (o instanceof BaseFuelIndex) {
			return ((BaseFuelIndex) o).getData();
		} else if (o instanceof NamedIndexContainer<?>) {
			return ((NamedIndexContainer<?>) o).getData();
		}
		return null;
	}

	private @Nullable DataType getDataTypeForElement(@Nullable final Object element) {
		if (element instanceof CommodityIndex) {
			return DataType.Commodity;
		} else if (element instanceof BaseFuelIndex) {
			return DataType.BaseFuel;
		} else if (element instanceof CharterIndex) {
			return DataType.Charter;
		}
		return null;

	}

	private @Nullable Number getNumberForElement(Object element, final YearMonth colDate) {

		if (transformer.getNodeClass().isInstance(element)) {
			return null;
		}

		String name = null;
		if (element instanceof NamedObject) {
			final NamedObject namedObject = (NamedObject) element;
			name = namedObject.getName();
		}

		final DataType dt = getDataTypeForElement(element);
		element = getIndex(element);

		if (element instanceof DataIndex) {
			final DataIndex<?> idx = (DataIndex<?>) element;
			return (Number) idx.getValueForMonth(colDate);
		} else if (element instanceof DerivedIndex) {

			final SeriesParser seriesParser = seriesParsers.get(dt);
			if (seriesParser != null) {
				if (name != null && !name.isEmpty()) {
					try {
						final ISeries series = seriesParser.getSeries(name);
						if (series != null) {
							return series.evaluate(PriceIndexUtils.convertTime(dateZero, colDate));
						}
					} catch (final Exception e) {
						// Ignore, anything from seried parser should be picked up via validation
					}
				}
			}
		}
		return null;
	}

	public void setInput(final PricingModel pricingModel) {
		this.pricingModel = pricingModel;

		getScenarioViewer().setCurrentContainerAndContainment(pricingModel, PricingPackage.Literals.NAMED_INDEX_CONTAINER__DATA);

		final EObject root = transformer.getRootObject();
		transformer.update(pricingModel);

		getScenarioViewer().setInput(root);

		setInitialExpandedState();
	}

	private void setInitialExpandedState() {

		final EObject root = transformer.getRootObject();
		final List<Object> expandedObjects = new LinkedList<>();
		// Set initial expanded state
		final List<?> nodes = (List<?>) root.eGet(transformer.getDataReference());
		for (final Object obj : nodes) {
			if (transformer.getNodeClass().isInstance(obj)) {
				if (Boolean.TRUE.equals(((EObject) obj).eGet(transformer.getExpandAttribute()))) {
					expandedObjects.add(obj);
				}
			}
		}
		getScenarioViewer().setExpandedElements(expandedObjects.toArray());
	}

	private void editIndex() {
		if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
			if (structuredSelection.isEmpty() == false) {
				final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
				if (structuredSelection.size() == 1) {

					// Skip tree model elements
					if (transformer.getNodeClass().isInstance(structuredSelection.getFirstElement())) {
						return;
					}

					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);
				} else {
					// No multi-edit for indices
				}
			}
		}
	}
}
