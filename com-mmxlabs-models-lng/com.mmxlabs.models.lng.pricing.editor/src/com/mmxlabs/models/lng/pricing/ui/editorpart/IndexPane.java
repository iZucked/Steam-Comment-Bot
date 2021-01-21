/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.time.YearMonth;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
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
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
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
import com.mmxlabs.scenario.service.model.manager.ModelReference;

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

	private YearMonth minDisplayDate = null;
	private YearMonth maxDisplayDate = null;

	private final IndexTreeTransformer transformer = new IndexTreeTransformer();
	private final Map<DataType, SeriesParser> seriesParsers = new EnumMap<>(DataType.class);
	private PricingModel pricingModel;

	private GridViewerColumn nameViewerColumn;
	private GridViewerColumn unitViewerColumn;

	private Action unitAction;

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
		items.add(new Pair<>(PricingPackage.Literals.COMMODITY_CURVE, getAddContext(PricingPackage.Literals.PRICING_MODEL__COMMODITY_CURVES)));
		items.add(new Pair<>(PricingPackage.Literals.BUNKER_FUEL_CURVE, getAddContext(PricingPackage.Literals.PRICING_MODEL__BUNKER_FUEL_CURVES)));
		items.add(new Pair<>(PricingPackage.Literals.CHARTER_CURVE, getAddContext(PricingPackage.Literals.PRICING_MODEL__CHARTER_CURVES)));
		items.add(new Pair<>(PricingPackage.Literals.CURRENCY_CURVE, getAddContext(PricingPackage.Literals.PRICING_MODEL__CURRENCY_CURVES)));

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
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		// Override default content provider
		getScenarioViewer().init(transformer.createContentProvider(), modelReference);

		// TODO: API subject to change!
		getScenarioViewer().setCurrentContainerAndContainment(pricingModel, null);

		ColumnViewerToolTipSupport.enableFor((ColumnViewer) scenarioViewer, ToolTip.NO_RECREATE);

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(final Object element) {
				if (element instanceof AbstractYearMonthCurve) {
					final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) element;
					if (curve.isSetExpression()) {
						return curve.getExpression();
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

		unitViewerColumn = addTypicalColumn("Units", new NonEditableColumn() {
			@Override
			public String render(final Object object) {

				if (object instanceof AbstractYearMonthCurve) {
					final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) object;

					if (!isEmpty(curve.getCurrencyUnit()) && !isEmpty(curve.getVolumeUnit())) {
						return String.format("%s/%s", curve.getCurrencyUnit(), curve.getVolumeUnit());
					} else if (!isEmpty(curve.getCurrencyUnit())) {
						return curve.getCurrencyUnit();
					} else if (!isEmpty(curve.getVolumeUnit())) {
						return curve.getVolumeUnit();
					}
				}
				return "";
			}
		});
		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {

				if (object instanceof AbstractYearMonthCurve) {
					final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) object;
					if (curve.isSetExpression()) {
						return "Expression";
					} else {
						return "Data";
					}
				}
				return "";
			}
		});

		unitAction = new Action("Edit unit conversion factors") {
			@Override
			public void run() {
				final UnitConversionEditor dialog = new UnitConversionEditor(scenarioEditingLocation.getShell(), scenarioEditingLocation);
				dialog.setBlockOnOpen(true);

				DetailCompositeDialogUtil.editInlock(scenarioEditingLocation, () -> {
					final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
					try {
						scenarioEditingLocation.setDisableUpdates(true);
						try {
							scenarioEditingLocation.setDisableCommandProviders(true);
							// Record current command so we can undo back to here when cancelling
							final Command currentCommand = commandStack.getUndoCommand();
							if (dialog.open() != Window.OK) {
								while (commandStack.getUndoCommand() != currentCommand) {
									commandStack.undo();
									// This avoids infinite loop... but should only be valid if currentCommand is also null.
									if (commandStack.getUndoCommand() == null) {
										break;
									}
								}
							}
						} finally {
							scenarioEditingLocation.setDisableCommandProviders(false);
						}
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
					}
					return Window.OK;
				});
			}
		};
		getMenuManager().add(unitAction);
		getMenuManager().update(true);
	}

	private void createSeriesParsers() {
		// Remove existing parsers
		seriesParsers.clear();

		for (final DataType dt : DataType.values()) {

			if (pricingModel != null) {
				seriesParsers.put(dt, PriceIndexUtils.getParserFor(pricingModel, dt.getPriceIndexType()));
			}
		}
	}

	private void updateDateRange(final PricingModel pricingModel) {

		for (final DataType dt : DataType.values()) {

			@SuppressWarnings("unchecked")
			final List<EObject> indexObjects = (List<EObject>) pricingModel.eGet(dt.getContainerFeature());

			for (final EObject indexObject : indexObjects) {

				final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) indexObject;
				if (curve.isSetExpression()) {
					continue;
				}

				for (final YearMonthPoint pt : curve.getPoints()) {
					final YearMonth d = pt.getDate();
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

					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						return number.doubleValue();
					}

					return null;
				}
			};

			col.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
			col.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);

			final ICellManipulator manipulator = new ICellManipulator() {

				@SuppressWarnings("unchecked")
				@Override
				public void setValue(final Object element, final Object value) {
					final DataType dt = getDataTypeForElement(element);
					if (dt != null) {
						if (element instanceof AbstractYearMonthCurve) {
							final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
							setIndexPoint((Double) value, (AbstractYearMonthCurve) element, colDate);
						}
					}
				}

				private void setIndexPoint(final Double value, final AbstractYearMonthCurve curve, final YearMonth colDate) {
					for (final YearMonthPoint p : curve.getPoints()) {
						if (p.getDate().getYear() == colDate.getYear() && p.getDate().getMonthValue() == colDate.getMonthValue()) {

							final Command cmd;
							if (value == null) {
								cmd = RemoveCommand.create(getEditingDomain(), p);
							} else {
								cmd = SetCommand.create(getEditingDomain(), p, PricingPackage.eINSTANCE.getYearMonthPoint_Value(), value);
							}
							if (!cmd.canExecute()) {
								throw new RuntimeException("Unable to execute index set command");
							}
							getEditingDomain().getCommandStack().execute(cmd);

							return;
						}
					}
					if (value != null) {
						final YearMonthPoint p = PricingFactory.eINSTANCE.createYearMonthPoint();
						p.setDate(colDate);
						p.setValue(value);
						final Command cmd = AddCommand.create(getEditingDomain(), curve, PricingPackage.eINSTANCE.getYearMonthPointContainer_Points(), p);
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
						return number.doubleValue();
					}

					return null;
				}

				@Override
				public CellEditor getCellEditor(final Composite parent, final Object object) {

					final DataType dt = getDataTypeForElement(object);
					if (dt != null) {
						final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
						final NumberFormatter formatter;

						formatter = new DoubleFormatter("#,###.###");

						formatter.setFixedLengths(false, false);

						result.setFormatter(formatter);

						return result;
					}
					return null;
				}

				@Override
				public boolean canEdit(final Object element) {
					if (element instanceof AbstractYearMonthCurve) {
						final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) element;
						return !curve.isSetExpression();

					}

					return false;
				}

				@Override
				public void setParent(final Object parent, final Object object) {
					// TODO Auto-generated method stub

				}

				@Override
				public void setExtraCommandsHook(final IExtraCommandsHook extraCommandsHook) {
					// TODO Auto-generated method stub

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
					Composite grid = null;
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
				public Color getForeground(final Object element) {

					if (element instanceof AbstractYearMonthCurve) {
						final AbstractYearMonthCurve curve = (AbstractYearMonthCurve) element;
						if (curve.isSetExpression()) {
							return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
						}
					}
					return super.getForeground(element);
				}

				@Override
				public String getText(final Object element) {

					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						return String.format("%01.3f", number.doubleValue());
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
			public void mouseDoubleClick(final MouseEvent e) {

				final Point p = new Point(e.x, e.y);
				final GridColumn column = scenarioViewer.getGrid().getColumn(p);
				if (column == nameViewerColumn.getColumn()) {
					editIndex();
				} else if (column == unitViewerColumn.getColumn()) {
					editIndex();
				}
			}

		});
	}

	// private @Nullable Index<?> getIndex(@Nullable final Object o) {
	// if (o instanceof CommodityCurve) {
	// return ((CommodityCurve) o).getData();
	// } else if (o instanceof CharterCurve) {
	// return ((CharterCurve) o).getData();
	// } else if (o instanceof BunkerFuelCurve) {
	// return ((BunkerFuelCurve) o).getData();
	// } else if (o instanceof NamedIndexContainer<?>) {
	// return ((NamedIndexContainer<?>) o).getData();
	// }
	// return null;
	// }

	private @Nullable DataType getDataTypeForElement(@Nullable final Object element) {
		if (element instanceof CommodityCurve) {
			return DataType.Commodity;
		} else if (element instanceof BunkerFuelCurve) {
			return DataType.BaseFuel;
		} else if (element instanceof CharterCurve) {
			return DataType.Charter;
		} else if (element instanceof CurrencyCurve) {
			return DataType.Currency;
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

		DataType dt = getDataTypeForElement(element);

		if (element instanceof AbstractYearMonthCurve) {
			AbstractYearMonthCurve curve = (AbstractYearMonthCurve) element;
			if (curve.isSetExpression()) {
				final SeriesParser seriesParser = seriesParsers.get(dt);
				if (seriesParser != null) {
					if (name != null && !name.isEmpty()) {
						try {
							final ISeries series = seriesParser.getSeries(name);
							if (series != null) {
								return series.evaluate(PriceIndexUtils.convertTime(PriceIndexUtils.dateZero, colDate));
							}
						} catch (final Exception e) {
							// Ignore, anything from series parser should be picked up via validation
						}
					}
				}
			} else {
				Optional<YearMonthPoint> pt = curve.getPoints().stream() //
						.filter(p -> colDate.equals(p.getDate())) //
						.findFirst();
				if (pt.isPresent()) {
					return pt.get().getValue();
				}
				return null;
			}
		}

		return null;
	}

	public void setInput(final PricingModel pricingModel) {
		this.pricingModel = pricingModel;

		 getScenarioViewer().setCurrentContainerAndContainment(pricingModel, null);

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

	private static boolean isEmpty(@Nullable final String str) {
		return str == null || str.trim().isEmpty();
	}

	public void openUnitsEditor() {
		unitAction.run();
	}

	@Override
	protected void filterObjectsToDelete(final Set<Object> uniqueObjects) {
		// Do not delete tree model nodes
		final Iterator<Object> itr = uniqueObjects.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			if (transformer.isNode(obj)) {
				itr.remove();
			}
		}
	}
}
