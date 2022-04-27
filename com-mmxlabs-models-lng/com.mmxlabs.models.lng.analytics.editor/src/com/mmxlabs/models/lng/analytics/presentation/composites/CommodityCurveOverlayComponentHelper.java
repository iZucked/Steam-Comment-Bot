/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.time.YearMonth;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;

/**
 * A component helper for CommodityCurveOverlay instances
 *
 * @generated NOT
 */
public class CommodityCurveOverlayComponentHelper extends DefaultComponentHelper {

	public CommodityCurveOverlayComponentHelper() {
		super(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY);

		addDefaultReadonlyEditor(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE);
		addEditor(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				createCurvesEditor(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES, "Alternative Curves"));
	}

	protected Function<EClass, IInlineEditor> createCurvesEditor(final EStructuralFeature feature, String label) {
		return topClass -> {
			TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder() {
				@Override
				public TabularDataInlineEditor build(final EStructuralFeature f) {
					return new TabularDataInlineEditor(feature, this) {
						private boolean tableBuilt = false;

						private GridViewerColumn createDateColumn(final YearMonth ym, final GridTableViewer tableViewer) {

							final GridViewerColumn column = new GridViewerColumn(tableViewer, SWT.NONE);
							GridViewerHelper.configureLookAndFeel(column);
							column.getColumn().setText(ym.toString());
							column.getColumn().setData("date", ym);

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

									final YearMonth colDate = (YearMonth) column.getColumn().getData("date");
									if (element instanceof YearMonthPointContainer curve) {
										Optional<YearMonthPoint> pt = curve.getPoints().stream() //
												.filter(p -> colDate.equals(p.getDate())) //
												.findFirst();
										if (pt.isPresent()) {
											return pt.get().getValue();
										}
									}
									return null;
								}
							};

							column.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
							column.getColumn().setData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER, renderer);

							final ICellManipulator manipulator2 = new ICellManipulator() {
								@SuppressWarnings("unchecked")
								@Override
								public void setValue(final Object element, final Object value) {
									if (element instanceof YearMonthPointContainer yearMonthContainer) {
										final YearMonth colDate = (YearMonth) column.getColumn().getData("date");
										setIndexPoint((Double) value, yearMonthContainer, colDate);
									}
								}

								private void setIndexPoint(final Double value, final YearMonthPointContainer curve, final YearMonth colDate) {
									for (final YearMonthPoint p : curve.getPoints()) {
										if (p.getDate().getYear() == colDate.getYear() && p.getDate().getMonthValue() == colDate.getMonthValue()) {

											final Command cmd;
											final EditingDomain ed = commandHandler.getEditingDomain();
											if (value == null) {
												cmd = RemoveCommand.create(ed, p);
											} else {
												cmd = SetCommand.create(ed, p, PricingPackage.eINSTANCE.getYearMonthPoint_Value(), value);
											}
											if (!cmd.canExecute()) {
												throw new RuntimeException("Unable to execute index set command");
											}
											ed.getCommandStack().execute(cmd);

											return;
										}
									}
									if (value != null) {
										final YearMonthPoint p = PricingFactory.eINSTANCE.createYearMonthPoint();
										p.setDate(colDate);
										p.setValue(value);
										final EditingDomain ed = commandHandler.getEditingDomain();
										final Command cmd = AddCommand.create(ed, curve, PricingPackage.eINSTANCE.getYearMonthPointContainer_Points(), p);
										if (!cmd.canExecute()) {
											throw new RuntimeException("Unable to execute index add command");
										}
										ed.getCommandStack().execute(cmd);
									}
								}

								@Override
								public Object getValue(final Object element) {

									final YearMonth colDate = (YearMonth) column.getColumn().getData("date");
									if (element instanceof YearMonthPointContainer curve) {
										Optional<YearMonthPoint> pt = curve.getPoints().stream() //
												.filter(p -> colDate.equals(p.getDate())) //
												.findFirst();
										if (pt.isPresent()) {
											return pt.get().getValue();
										}
									}
									return null;
								}

								@Override
								public CellEditor getCellEditor(final Composite parent, final Object object) {
									final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
									final NumberFormatter formatter;

									formatter = new DoubleFormatter("#,###.###");

									formatter.setFixedLengths(false, false);

									result.setFormatter(formatter);

									return result;
								}

								@Override
								public boolean canEdit(final Object element) {
									return true;
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
							column.getColumn().setData(EObjectTableViewer.COLUMN_MANIPULATOR, manipulator2);

							column.setEditingSupport(new EditingSupport((ColumnViewer) tableViewer) {
								@Override
								protected boolean canEdit(final Object element) {
									return (manipulator2 != null) && manipulator2.canEdit(element);
								}

								@Override
								protected CellEditor getCellEditor(final Object element) {
									Composite grid = tableViewer.getGrid();
									// if (viewer instanceof GridTreeViewer) {
									// grid = ((GridTreeViewer) IndexPane.this.viewer).getGrid();
									// } else if (viewer instanceof GridTableViewer) {
									// grid = ((GridTableViewer) IndexPane.this.viewer).getGrid();
									// } else if (viewer.getControl() instanceof Composite) {
									// grid = (Composite) viewer.getControl();
									// }
									return manipulator2.getCellEditor(grid, element);
								}

								@Override
								protected Object getValue(final Object element) {
									return manipulator2.getValue(element);
								}

								@Override
								protected void setValue(final Object element, final Object value) {
									// a value has come out of the celleditor and is being set on
									// the element.
									manipulator2.setValue(element, value);
									tableViewer.refresh();
								}
							});

							column.setLabelProvider(new ColumnLabelProvider() {

								@Override
								public Color getForeground(final Object element) {
									return super.getForeground(element);
								}

								@Override
								public String getText(final Object element) {

									final YearMonth colDate = (YearMonth) column.getColumn().getData("date");
									if (element instanceof YearMonthPointContainer curve) {
										Optional<YearMonthPoint> pt = curve.getPoints().stream() //
												.filter(p -> colDate.equals(p.getDate())) //
												.findFirst();
										if (pt.isPresent()) {
											return String.format("%01.3f", pt.get().getValue());
										}
									}
									return null;
								}
							});
							return column;
						}

						@Override
						protected void updateDisplay(final Object value) {
							if (input != null) {
								input.eAdapters().remove(adapter);
							}

							if (!tableViewer.getControl().isDisposed()) {
								if (input instanceof CommodityCurveOverlay commodityCurveOverlay) {
									final CommodityCurve referenceCurve = commodityCurveOverlay.getReferenceCurve();
									if (!tableBuilt && !referenceCurve.getPoints().isEmpty()) {
										tableBuilt = true;
										final Iterator<YearMonth> iterYmPoints = referenceCurve.getPoints().stream().map(YearMonthPoint::getDate).iterator();
										YearMonth earliestYm = iterYmPoints.next();
										YearMonth latestYm = earliestYm;
										while (iterYmPoints.hasNext()) {
											final YearMonth nextYmPoint = iterYmPoints.next();
											if (nextYmPoint.isBefore(earliestYm)) {
												earliestYm = nextYmPoint;
											}
											if (nextYmPoint.isAfter(latestYm)) {
												latestYm = nextYmPoint;
											}
										}
										for (YearMonth ym = earliestYm; !ym.isAfter(latestYm); ym = ym.plusMonths(1)) {
											createDateColumn(ym, tableViewer);
										}
									}

									tableViewer.setInput(value);

									if (input instanceof EObject) {
										input.eAdapters().add(adapter);
									}
								}
							}
						}
					};

				}
			};
			b.withShowHeaders(true);
			b.withLabel(label);
			b.withHeightHint(100);
			b.withContentProvider(new ArrayContentProvider());
			// b.withComparator(new ViewerComparator());
			b.buildColumn("Name", feature).withWidth(100) //
					.withRMMaker((ch, rvp) -> new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), ch)).build();

			b.withAction("Add", (input, ch, sel) -> {
				CommodityCurveOverlay overlay = (CommodityCurveOverlay) input;
				YearMonthPointContainer ympc = PricingFactory.eINSTANCE.createYearMonthPointContainer();
				Command c = AddCommand.create(ch.getEditingDomain(), overlay, feature, ympc);
				ch.handleCommand(c, overlay, feature);

			});
			b.withAction("Delete", (input, ch, sel) -> {

				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					CommodityCurveOverlay overlay = (CommodityCurveOverlay) input;
					Command c = RemoveCommand.create(ch.getEditingDomain(), overlay, feature, ss.toList());
					ch.handleCommand(c, overlay, feature);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(feature);
		};
	}

}