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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.actions.AddDateToIndexAction;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DialogFeatureManipulator;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;

public class IndexPane extends ScenarioTableViewerPane {

	private static final Date dateZero = new Date(0);
	private List<EReference> path = null;
	private Date minDisplayDate = null;
	private Date maxDisplayDate = null;	

	private boolean useIntegers;

	private final EReference indexFeature;

	public IndexPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars, final EReference indexFeature) {
		super(page, part, location, actionBars);
		this.indexFeature = indexFeature;

	}

	private SeriesParser seriesParser = null;

	/**
	 * Ensures that a given date is visible in the editor column range, as long as the editor is open.
	 * 
	 * @param date
	 */
	public void selectDateColumn(Date date) {
		if (date == null) {
			return;
		}
		
		if (minDisplayDate == null || minDisplayDate.after(date)) {
			minDisplayDate = date;
		}
		
		if (maxDisplayDate == null || maxDisplayDate.before(date)) {
			maxDisplayDate = date;
		}				
		
		((IndexTableViewer) viewer).redisplayDateRange(date);
	}
	
	@Override
	protected Action createAddAction(final EReference containment) {
		Action [] actions = new Action [] { new AddDateToIndexAction(this) }; 
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), actions);		
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
		}, indexFeature);
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
		public Comparable<?> getComparable(final Object object) {
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

	private SeriesParser createSeriesParser(final PricingModel pricingModel) {
		final SeriesParser seriesParser = new SeriesParser();

		Object obj = pricingModel;
		for (final EReference ref : path) {
			obj = ((EObject) obj).eGet(ref);
		}

		if (obj instanceof List) {
			final List<EObject> indexObjects = (List<EObject>) obj;

			for (final EObject indexObject : indexObjects) {

				if (!indexObject.eIsSet(indexFeature)) {
					continue;
				}

				String name = "Unknown";
				if (indexObject instanceof NamedObject) {
					final NamedObject namedObject = (NamedObject) indexObject;
					name = namedObject.getName();
				}

				final Index<?> idx = (Index<?>) indexObject.eGet(indexFeature);

				if (idx instanceof DataIndex) {
					PriceIndexUtils.addSeriesDataFromDataIndex(seriesParser, name, dateZero, (DataIndex<? extends Number>) idx);
				} else if (idx instanceof DerivedIndex) {
					seriesParser.addSeriesExpression(name, ((DerivedIndex) idx).getExpression());
				}
			}
		}
		return seriesParser;

	}
	
	protected class IndexTableViewer extends ScenarioTableViewer {

		public IndexTableViewer(Composite parent, int style,
				IScenarioEditingLocation part) {
			super(parent, style, part);
		}
		
		@Override
		protected void internalRefresh(final Object element) {
			final Object input = getInput();
			if (input instanceof PricingModel) {
				final PricingModel pricingModel = (PricingModel) input;
				seriesParser = createSeriesParser(pricingModel);
			}
			super.internalRefresh(element);
		}

		@Override
		protected void internalRefresh(final Object element, final boolean updateLabels) {
			final Object input = getInput();
			if (input instanceof PricingModel) {
				final PricingModel pricingModel = (PricingModel) input;
				seriesParser = createSeriesParser(pricingModel);
			}
			super.internalRefresh(element, updateLabels);
		}
		
		protected void redisplayDateRange(Date selected) {
			if (minDisplayDate != null && maxDisplayDate != null) {
				final Grid grid = ((GridTableViewer) IndexPane.this.viewer).getGrid();
				final int columnCount = grid.getColumnCount();
				for (int i = columnCount - 1; i > 1; i--) {
					final GridColumn column = grid.getColumn(i);
					column.dispose();
				}
				final Calendar c = Calendar.getInstance();
				c.setTime(minDisplayDate);
				c.set(Calendar.MILLISECOND, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.DAY_OF_MONTH, 1);

				while (!c.getTime().after(maxDisplayDate)) {
					addColumn(c, true, useIntegers);
					c.add(Calendar.MONTH, 1);
				}
			}

			viewer.refresh();			
		}

		@Override
		protected void inputChanged(final Object input, final Object oldInput) {
			super.inputChanged(input, oldInput);

			if (input instanceof PricingModel) {
				final PricingModel pricingModel = (PricingModel) input;
				seriesParser = createSeriesParser(pricingModel);

				Object obj = pricingModel;
				for (final EReference ref : path) {
					obj = ((EObject) obj).eGet(ref);
				}

				if (obj instanceof List) {
					final List<EObject> indexObjects = (List<EObject>) obj;

					for (final EObject indexObject : indexObjects) {

						if (!indexObject.eIsSet(indexFeature)) {
							continue;
						}

						final Index<?> idx = (Index<?>) indexObject.eGet(indexFeature);
						if (!(idx instanceof DataIndex<?>)) {
							continue;
						}

						for (final Date d : idx.getDates()) {
							if (minDisplayDate == null || minDisplayDate.after(d)) {
								minDisplayDate = d;
							}
							if (maxDisplayDate == null || maxDisplayDate.before(d)) {
								maxDisplayDate = d;
							}
						}
					}
					
					redisplayDateRange(null);
								getSortingSupport().removeSortableColumn(column);

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
				public Comparable getComparable(Object element) {

					// Unwrap index from owner
					String name = null;
					if (element instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) element;
						name = namedObject.getName();
					}

					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
						}
					}

					if (element instanceof DataIndex) {
						final DataIndex<?> idx = (DataIndex<?>) element;
						final Date colDate = (Date) col.getColumn().getData("date");
						final Object valueAfter = idx.getValueForMonth(colDate);
						if (valueAfter instanceof Integer) {
							//return (Integer) valueAfter;
							return new Double((Integer) valueAfter);
						} else if (valueAfter instanceof Double) {
							return (Double) valueAfter;
						}
					} else if (element instanceof DerivedIndex) {
						final Date colDate = (Date) col.getColumn().getData("date");
						try {
							final ISeries series = seriesParser.getSeries(name);
							final Number valueAfter = series.evaluate(PriceIndexUtils.convertTime(dateZero, colDate));
							// final Object valueAfter = idx.getValueForMonth(colDate);
							if (valueAfter instanceof Integer) {
								//return (Integer) valueAfter;
								return new Double((Integer) valueAfter);
							} else if (valueAfter instanceof Double) {
								return (Double) valueAfter;
							}
						} catch (Exception e) {

						}
					}

					return null;
				}
			};

			col.getColumn().setData(EObjectTableViewer.COLUMN_RENDERER, renderer);
			final ICellManipulator manipulator = new ICellManipulator() {

				@SuppressWarnings("unchecked")
				@Override
				public void setValue(Object element, final Object value) {
					// Unwrap index from owner
					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
						}
					}
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
				public Object getValue(Object element) {
					String name = null;
					if (element instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) element;
						name = namedObject.getName();
					}
					// Unwrap index from owner
					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
						}
					}
					if (element instanceof DataIndex) {
						final DataIndex<?> idx = (DataIndex<?>) element;
						final Date colDate = (Date) col.getColumn().getData("date");
						final Object valueAfter = idx.getValueForMonth(colDate);
						if (valueAfter instanceof Integer) {
							return (Integer) valueAfter;
						} else if (valueAfter instanceof Double) {
							return (Double) valueAfter;
						}
					} else if (element instanceof DerivedIndex) {
						final DerivedIndex<?> idx = (DerivedIndex<?>) element;
						final Date colDate = (Date) col.getColumn().getData("date");

						try {
							final ISeries series = seriesParser.getSeries(name);
							final Number valueAfter = series.evaluate(PriceIndexUtils.convertTime(dateZero, colDate));
							// final Object valueAfter = idx.getValueForMonth(colDate);
							if (valueAfter instanceof Integer) {
								return (Integer) valueAfter;
							} else if (valueAfter instanceof Double) {
								return (Double) valueAfter;
							}
						} catch (Exception e) {
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
				public boolean canEdit(Object element) {

					// Unwrap index from owner
					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
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
				public Color getForeground(Object element) {
					// Unwrap index from owner
					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
						}
					}
					if (element instanceof DerivedIndex<?>) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
					return super.getForeground(element);
				}

				@Override
				public String getText(Object element) {

					String name = null;
					if (element instanceof NamedObject) {
						final NamedObject namedObject = (NamedObject) element;
						name = namedObject.getName();
					}

					// Unwrap index from owner
					if (element instanceof EObject) {
						final EObject eObject = (EObject) element;
						if (eObject.eIsSet(indexFeature)) {
							element = eObject.eGet(indexFeature);
						}
					}
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
					} else if (element instanceof DerivedIndex) {
						final Date colDate = (Date) col.getColumn().getData("date");
						if (name != null && !name.isEmpty()) {
							try {
								final ISeries series = seriesParser.getSeries(name);
								final Number valueAfter = series.evaluate(PriceIndexUtils.convertTime(dateZero, colDate));
								// final Object valueAfter = idx.getValueForMonth(colDate);
								if (valueAfter != null) {
									if (valueAfter instanceof Integer) {
										return String.format("%d", valueAfter);
									} else {
										return String.format("%01.3f", valueAfter);
									}
								}
							} catch (Exception e) {
								// Ignore
							}
						}
					}
					return null;
				}
			});
		}
	}
	

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer result = new IndexTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart());
		return result;
	}

}
