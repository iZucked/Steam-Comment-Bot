/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DialogFeatureManipulator;

public class IndexPane extends ScenarioTableViewerPane {

	private List<EReference> path = null;

	private boolean useIntegers;

	public IndexPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);

	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		this.path = path;

		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof DerivedIndex) {
					return "Expression";
				} else {
					return "Data";
				}
			}
		});
		addNameManipulator("Name");

		// addTypicalColumn("Content", new IndexValueManipulator());

		defaultSetTitle("Indices");
	}

	public void setUseIntegers(final boolean b) {
		this.useIntegers = b;
	}

	private class IndexValueManipulator implements ICellRenderer, ICellManipulator {
		private final BasicAttributeManipulator expressionManipulator = new BasicAttributeManipulator(PricingPackage.eINSTANCE.getDerivedIndex_Expression(), getEditingDomain());

		private final BasicAttributeManipulator dataManipulator = new DialogFeatureManipulator(PricingPackage.eINSTANCE.getDataIndex_Points(), getEditingDomain()) {

			@Override
			protected String renderValue(final Object value) {
				if (value == null)
					return "";
				return ((List) value).size() + " points";
			}

			@Override
			protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
				return null;
			}
		};

		private BasicAttributeManipulator pick(final Object object) {
			if (object instanceof DataIndex)
				return dataManipulator;
			else
				return expressionManipulator;
		}

		@Override
		public void setValue(final Object object, final Object value) {
			pick(object).setValue(object, value);
		}

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			return pick(object).getCellEditor(parent, object);
		}

		@Override
		public Object getValue(final Object object) {
			return pick(object).getValue(object);
		}

		@Override
		public boolean canEdit(final Object object) {
			return pick(object).canEdit(object);
		}

		@Override
		public String render(final Object object) {
			return pick(object).render(object);
		}

		@Override
		public Comparable getComparable(final Object object) {
			return pick(object).getComparable(object);
		}

		@Override
		public Object getFilterValue(final Object object) {
			return pick(object).getFilterValue(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
			return pick(object).getExternalNotifiers(object);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#defaultSetTitle(java.lang.String)
	 */
	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}

	public void removeColumn(final String title) {
		// TODO: Implement?
		// for (final ColumnHandler h : handlers) {
		// if (h.title.equals(title)) {
		// viewer.removeColumn(h.column);
		// handlers.remove(h);
		// handlersInOrder.remove(h);
		// break;
		// }
		// }
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		ScenarioTableViewer result = new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {
			@Override
			protected void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				if (input instanceof PricingModel) {
					final PricingModel pricingModel = (PricingModel) input;

					Object obj = pricingModel;
					for (final EReference ref : path) {
						obj = ((EObject) obj).eGet(ref);
					}

					if (obj instanceof List) {
						final List<Index<?>> indexCurve = (List<Index<?>>) obj;

						Date minDate = null;
						Date maxDate = null;
						for (final Index<?> idx : indexCurve) {
							if (!(idx instanceof DataIndex<?>)) {
								continue;
							}

							for (final Date d : idx.getDates()) {
								if (minDate == null || minDate.after(d)) {
									minDate = d;
								}
								if (maxDate == null || maxDate.before(d)) {
									maxDate = d;
								}
							}
						}

						if (minDate != null && maxDate != null) {
							Grid grid = ((GridTableViewer) IndexPane.this.viewer).getGrid();
							int columnCount = grid.getColumnCount();
							for (int i = columnCount - 1; i > 1; i--) {
								GridColumn column = grid.getColumn(i);
								column.dispose();
							}
							final Calendar c = Calendar.getInstance();
							c.setTime(minDate);
							c.set(Calendar.MILLISECOND, 0);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MINUTE, 0);
							c.set(Calendar.HOUR, 0);
							c.set(Calendar.DAY_OF_MONTH, 1);

							while (!c.getTime().after(maxDate)) {
								addColumn(c, true, useIntegers);
								c.add(Calendar.MONTH, 1);
							}
						}

						viewer.refresh();
					}
				}

			}

			@Override
			protected void doCommandStackChanged() {
				inputChanged(getInput(), getInput());
				super.doCommandStackChanged();
			}

			private void addColumn(final Calendar cal, final boolean sortable, final boolean isIntegerBased) {

				final String date = String.format("%4d-%02d", cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1));
				final GridViewerColumn col = addSimpleColumn(date, sortable);
				col.getColumn().setData("date", cal.getTime());

				final ICellRenderer renderer = new ICellRenderer() {

					@Override
					public String render(final Object object) {
						return object.toString();
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
					public Comparable getComparable(final Object element) {
						if (element instanceof DataIndex) {
							final DataIndex<?> idx = (DataIndex<?>) element;
							final Date colDate = (Date) col.getColumn().getData("date");
							final Object valueAfter = idx.getValueForMonth(colDate);
							if (valueAfter instanceof Integer) {
								return (Integer) valueAfter;
							} else if (valueAfter instanceof Double) {
								return (Double) valueAfter;
							}
						}

						return null;
					}
				};

				col.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
				final ICellManipulator manipulator = new ICellManipulator() {

					@SuppressWarnings("unchecked")
					@Override
					public void setValue(final Object element, final Object value) {

						if (element instanceof DataIndex) {
							final Date colDate = (Date) col.getColumn().getData("date");

							if (isIntegerBased) {
								setIndexPoint((Integer) value, (DataIndex<Integer>) element, colDate);

							} else {

								setIndexPoint((Double) value, (DataIndex<Double>) element, colDate);
							}
						}
					}

					@SuppressWarnings({ "deprecation" })
					private <T> void setIndexPoint(final T value, final DataIndex<T> di, final Date colDate) {

						for (final IndexPoint<T> p : di.getPoints()) {
							if (p.getDate().getYear() == colDate.getYear() && p.getDate().getMonth() == colDate.getMonth()) {

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
						if (element instanceof DataIndex) {
							final DataIndex<?> idx = (DataIndex<?>) element;
							final Date colDate = (Date) col.getColumn().getData("date");
							final Object valueAfter = idx.getValueForMonth(colDate);
							if (valueAfter instanceof Integer) {
								return (Integer) valueAfter;
							} else if (valueAfter instanceof Double) {
								return (Double) valueAfter;
							}
						}

						return null;
					}

					@Override
					public CellEditor getCellEditor(final Composite parent, final Object object) {

						final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
						final NumberFormatter formatter;
						if (isIntegerBased) {
							formatter = new IntegerFormatter("#,###.###");
						} else {
							formatter = new DoubleFormatter("#,###.###");
						}

						formatter.setFixedLengths(false, false);

						result.setFormatter(formatter);

						return result;
					}

					@Override
					public boolean canEdit(final Object element) {
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
						return manipulator.getCellEditor(((GridTableViewer) viewer).getGrid(), element);
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
				col.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {

					@Override
					public String getText(final Object element) {

						if (element instanceof DataIndex) {
							final DataIndex<?> idx = (DataIndex<?>) element;
							final Date colDate = (Date) col.getColumn().getData("date");
							final Object valueAfter = idx.getValueForMonth(colDate);
							if (valueAfter != null) {
								if (valueAfter instanceof Integer) {
									return String.format("%d", valueAfter);
								} else {
									return String.format("%01.3f", valueAfter);
								}
							}
						}
						return null;
					}
				});
			}
		};
		return result;
	}

}
