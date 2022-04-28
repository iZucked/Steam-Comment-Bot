/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.time.YearMonth;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.ui.editorpart.CurveTreeTransformer.DataType;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.WhatIfEvaluator;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
//import com.mmxlabs.rcp.common.CommonImages;
//import com.mmxlabs.rcp.common.CommonImages.IconMode;
//import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class SensitivityCurvesPane extends ScenarioTableViewerPane {

	private YearMonth minDisplayDate = null;
	private YearMonth maxDisplayDate = null;

	private final CurveTreeTransformer transformer = new CurveTreeTransformer();

	private PricingModel pricingModel;
	private SensitivityModel sensitivityModel;

	private GridViewerColumn nameViewerColumn;
	private GridViewerColumn unitViewerColumn;

	private Action addAction;

	public SensitivityCurvesPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void dispose() {
		this.pricingModel = null;
		transformer.dispose();
		super.dispose();
	}

	@Override
	protected Action createAddAction(EReference containment) {
		final Action addAction = new Action() {
			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof final IStructuredSelection structuredSelection) {
					if (structuredSelection.isEmpty()) {
						return;
					}
					final List<?> selections = structuredSelection.toList();
					if (selections.size() == 1) {
						if (selections.get(0) instanceof final CommodityCurveOverlay cco) {
							final YearMonthPointContainer ympc = PricingFactory.eINSTANCE.createYearMonthPointContainer();
							final CompoundCommand cmd = new CompoundCommand("Add alternative curve");
							cmd.append(AddCommand.create(getEditingDomain(), cco, AnalyticsPackage.eINSTANCE.getCommodityCurveOverlay_AlternativeCurves(), ympc));
							if (cco.getAlternativeCurves().isEmpty()) {
								cmd.append(AddCommand.create(getEditingDomain(), sensitivityModel.getSensitivityModel(), AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__COMMODITY_CURVES, cco));
							}
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}
				}
			}
		};
		this.addAction = addAction;
		addAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled));
		addAction.setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Disabled));
		return addAction;
	}

	@Override
	protected ScenarioTableViewer constructViewer(Composite parent) {
		final ScenarioTableViewer result = new SensitivityCurvesViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getScenarioEditingLocation());
		result.getGrid().setTreeLinesVisible(true);
		return result;
	}

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
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory, ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		getScenarioViewer().init(transformer.createContentProvider(), modelReference);

		getScenarioViewer().setCurrentContainerAndContainment(pricingModel, null);

		ColumnViewerToolTipSupport.enableFor((ColumnViewer) scenarioViewer, ToolTip.NO_RECREATE);

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(Object element) {
				if (element instanceof final AbstractYearMonthCurve abstractYearMonthCurve && abstractYearMonthCurve.isSetExpression()) {
					return abstractYearMonthCurve.getExpression();
				}
				return null;
			}
		});

		nameViewerColumn = addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getCommandHandler()) {
			@Override
			public @Nullable String render(Object object) {
				if (object instanceof final CommodityCurveOverlay cco) {
					return cco.getReferenceCurve().getName();
				}
				return super.render(object);
			}

			@Override
			public boolean canEdit(Object object) {
				if (object instanceof CommodityCurveOverlay) {
					return false;
				}
				return super.canEdit(object);
			}
		});
		nameViewerColumn.getColumn().setTree(true);

		unitViewerColumn = addTypicalColumn("Units", new NonEditableColumn() {

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof final CommodityCurveOverlay cco) {
					final CommodityCurve cc = cco.getReferenceCurve();
					if (!isEmpty(cc.getCurrencyUnit()) && !isEmpty(cc.getVolumeUnit())) {
						return String.format("%s/%s", cc.getCurrencyUnit(), cc.getVolumeUnit());
					}
				}
				return "";
			}
		});

		addTypicalColumn("Type", new NonEditableColumn() {

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof final AbstractYearMonthCurve abstractYearMonthCurve) {
					if (abstractYearMonthCurve.isSetExpression()) {
						return "Expression";
					} else {
						return "Data";
					}
				}
				return "";
			}
		});

		getScenarioViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				final IStructuredSelection selection = event.getStructuredSelection();
				if (!selection.isEmpty()) {
					final List<?> selections = selection.toList();
					if (selections.size() == 1) {
						final Object selectedRow = selections.get(0);
						if (selectedRow instanceof CommodityCurveOverlay) {
							addAction.setEnabled(true);
							return;
						}
					}
				}
				addAction.setEnabled(false);
			}
		});

		final Action runAction = createRunButton();
		getToolBarManager().insert(0, new ActionContributionItem(runAction));
		getToolBarManager().update(true);
	}

	private Action createRunButton() {
		final ImageDescriptor genDesc = CommonImages.getImageDescriptor(IconPaths.Play_16, IconMode.Enabled);
		final Action runSensitivityAction = new Action() {
			@Override
			public void run() {
				final OptionAnalysisModel m = sensitivityModel.getSensitivityModel();
				if (m != null) {
					WhatIfEvaluator.doPriceSensitivity(getScenarioEditingLocation(), sensitivityModel);
				}
			}
		};
		runSensitivityAction.setImageDescriptor(genDesc);
		runSensitivityAction.setToolTipText("Run sensitivity model");
		return runSensitivityAction;
	}

	private static boolean isEmpty(@Nullable final String str) {
		return str == null || str.trim().isEmpty();
	}

	private void updateDateRange(final PricingModel pricingModel) {
		for (final CommodityCurve cc : pricingModel.getCommodityCurves()) {
			if (cc.isSetExpression()) {
				continue;
			}
			for (final YearMonthPoint pt : cc.getPoints()) {
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

	protected class SensitivityCurvesViewer extends ScenarioTableViewer {

		public SensitivityCurvesViewer(Composite parent, int style, IScenarioEditingLocation part) {
			super(parent, style, part);
		}

		protected void redisplayDateRange(final YearMonth selected) {
			if (minDisplayDate != null && maxDisplayDate != null) {
				Grid grid = null;
				if (SensitivityCurvesPane.this.viewer instanceof final GridTreeViewer gridTreeViewer) {
					grid = gridTreeViewer.getGrid();
				} else if (SensitivityCurvesPane.this.viewer instanceof final GridTableViewer gridTableViewer) {
					grid = gridTableViewer.getGrid();
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
						localDate = localDate.plusMonths(1L);
					}
				}
			}
			viewer.refresh();
		}

		@Override
		protected void inputChanged(Object input, Object oldInput) {
			super.inputChanged(input, oldInput);
			updateDateRange(pricingModel);
			redisplayDateRange(null);
		}

		@Override
		protected void doCommandStackChanged() {
			transformer.update(pricingModel);
			super.doCommandStackChanged();
		}

		private void addColumn(final YearMonth cal, final boolean sortable) {
			final String date = String.format("%4d-%02d", cal.getYear(), cal.getMonthValue());
			final GridViewerColumn col = addSimpleColumn(date, sortable);
			col.getColumn().setData("date", cal);

			final ICellRenderer renderer = new ICellRenderer() {
				@Override
				public String render(Object object) {
					return object.toString();
				};

				@Override
				public boolean isValueUnset(Object object) {
					return false;
				}

				@Override
				public @Nullable Object getFilterValue(Object object) {
					return null;
				}

				@Override
				public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
					return null;
				}

				@Override
				public Comparable getComparable(Object element) {
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

				@Override
				public void setValue(Object element, Object value) {
					if (element instanceof final YearMonthPointContainer yearMonthPointContainer) {
						final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
						setIndexPoint((Double) value, yearMonthPointContainer, colDate);
					}

				}

				private void setIndexPoint(final Double value, final YearMonthPointContainer curve, final YearMonth colDate) {
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
				public void setParent(Object parent, Object object) {
				}

				@Override
				public void setExtraCommandsHook(IExtraCommandsHook extraCommandsHook) {
				}

				@Override
				public Object getValue(Object element) {
					final YearMonth colDate = (YearMonth) col.getColumn().getData("date");
					final Number number = getNumberForElement(element, colDate);
					if (number != null) {
						return number.doubleValue();
					}
					return null;
				}

				@Override
				public CellEditor getCellEditor(Composite parent, Object object) {
					if (object instanceof CommodityCurveOverlay || object instanceof YearMonthPointContainer) {
						// final DataType dt = getDataTypeForElement(object);
						// if (dt != null) {
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
				public boolean canEdit(Object element) {
					return element instanceof YearMonthPointContainer;
				}

			};
			col.getColumn().setData(EObjectTableViewer.COLUMN_MANIPULATOR, manipulator);
			col.setEditingSupport(new EditingSupport((ColumnViewer) viewer) {

				@Override
				protected CellEditor getCellEditor(Object element) {
					Composite grid = null;
					if (viewer instanceof final GridTreeViewer gridTreeViewer) {
						grid = ((GridTreeViewer) SensitivityCurvesPane.this.viewer).getGrid();
					} else if (viewer instanceof final GridTableViewer gridTableViewer) {
						grid = ((GridTableViewer) SensitivityCurvesPane.this.viewer).getGrid();
					} else if (viewer.getControl() instanceof final Composite composite) {
						grid = composite;
					}
					return manipulator.getCellEditor(grid, element);
				}

				@Override
				protected boolean canEdit(Object element) {
					return !lockedForEditing && manipulator != null && manipulator.canEdit(element);
				}

				@Override
				protected Object getValue(Object element) {
					return manipulator.getValue(element);
				}

				@Override
				protected void setValue(Object element, Object value) {
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
					if (element instanceof CommodityCurveOverlay) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
					return super.getForeground(element);
				}

				@Override
				public String getText(Object element) {
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

	private @Nullable DataType getDataTypeForElement(@Nullable final Object element) {
		if (element instanceof CommodityCurve) {
			return DataType.Commodity;
		}
		return null;
	}

	private @Nullable Number getNumberForElement(Object element, final YearMonth colDate) {
		if (element instanceof final CommodityCurveOverlay commodityCurveOverlay) {
			final CommodityCurve commodityCurve = commodityCurveOverlay.getReferenceCurve();
			if (!commodityCurve.isSetExpression()) {
				Optional<YearMonthPoint> pt = commodityCurve.getPoints().stream() //
						.filter(p -> colDate.equals(p.getDate())) //
						.findFirst();
				if (pt.isPresent()) {
					return pt.get().getValue();
				}
			}
		} else if (element instanceof final YearMonthPointContainer yearMonthPointContainer) {
			Optional<YearMonthPoint> pt = yearMonthPointContainer.getPoints().stream() //
					.filter(p -> colDate.equals(p.getDate())) //
					.findFirst();
			if (pt.isPresent()) {
				return pt.get().getValue();
			}
		}
		return null;
	}

	@Override
	protected Action createDeleteAction(@Nullable Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioTableViewerDeleteAction(null) {
			@Override
			protected boolean isApplicableToSelection(ISelection selection) {
				return super.isApplicableToSelection(selection) && ((List<Object>) (((IStructuredSelection) selection).toList())).stream() //
						.allMatch(YearMonthPointContainer.class::isInstance);
			}
		};
	}

	public void setInput(final SensitivityModel sensitivityModel, final PricingModel pricingModel) {
		this.pricingModel = pricingModel;
		this.sensitivityModel = sensitivityModel;
		getScenarioViewer().setCurrentContainerAndContainment(pricingModel, null);
		// final EObject root = transformer.getRoot();
		transformer.update(pricingModel);
		getScenarioViewer().setInput(Pair.of(sensitivityModel, pricingModel));
		// setInitialExpandedState();
	}

	private void setInitialExpandedState() {
		final EObject root = transformer.getRoot();
		final List<Object> expandedObjects = new LinkedList<>();
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
		if (scenarioViewer.getSelection() instanceof final IStructuredSelection iStructuredSelection) {
			if (!iStructuredSelection.isEmpty()) {
				if (iStructuredSelection.size() == 1) {
					if (transformer.getNodeClass().isInstance(iStructuredSelection.getFirstElement())) {
						return;
					}
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, iStructuredSelection);
				}
			}
		}
	}

	/**
	 */
	@Override
	protected void enableOpenListener() {
	}
}
