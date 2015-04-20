/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.joda.time.YearMonth;

import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ILabelLayoutDataProvidingEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;

/**
 */
public class CurveInlineEditor extends BasicAttributeInlineEditor implements ILabelLayoutDataProvidingEditor {

	Control control;
	Table table;
	TableViewer viewer;
	Index<?> index = null;
	EObject originalInput = null;
	final Repacker repacker = new Repacker();
	final EClassifier indexRawType;
	EStructuralFeature indexPointsFeature = PricingPackage.Literals.DATA_INDEX__POINTS;

	/**
	 * Creates a curve inline editor for Index<clazz> objects. We need to know what class it was instantiated for, and Java generics don't provide enough introspection to do so.
	 * 
	 * @param feature
	 * @param clazz
	 */
	public CurveInlineEditor(final EStructuralFeature feature) {
		super(feature);
		// infer the class from the EMF feature object, whose etype should be a parameterised DataIndex
		indexRawType = feature.getEGenericType().getETypeArguments().get(0).getERawType();
	}

	public IStructuredContentProvider createContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final DataIndex<?> index = (DataIndex<?>) inputElement;
				final EList<?> points = index.getPoints();
				final IndexPoint<?>[] result = points.toArray(new IndexPoint[0]);

				Arrays.sort(result, new Comparator<IndexPoint<?>>() {

					@Override
					public int compare(final IndexPoint<?> arg0, final IndexPoint<?> arg1) {
						final YearMonth date0 = arg0.getDate();
						if (date0 == null)
							return -1;
						final YearMonth date1 = arg1.getDate();
						if (date1 == null)
							return 1;
						return date0.compareTo(date1);
					}

				});

				return result;
			}
		};
	}

	abstract class ColumnEditingSupport extends EditingSupport {
		final EStructuralFeature feature;

		public ColumnEditingSupport(final ColumnViewer viewer, final EStructuralFeature feature) {
			super(viewer);
			this.feature = feature;
		}

		@Override
		protected void setValue(final Object element, final Object value) {
			final EditingDomain ed = commandHandler.getEditingDomain();
			commandHandler.handleCommand(SetCommand.create(ed, element, feature, value), (EObject) element, feature);
			viewer.refresh();
		}

		@Override
		protected Object getValue(final Object element) {
			return ((EObject) element).eGet(feature);
		}

		@Override
		protected boolean canEdit(final Object element) {
			return true;
		}

	}

	public TableViewerColumn createColumn(final TableViewer viewer, final String title) {
		final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(title);
		return column;
	}

	class Repacker {
		boolean resizing = false;

		public void repack() {
			if (resizing)
				return;
			resizing = true;
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(i).pack();
			}
			resizing = false;
		}
	}

	void createTable(final Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		table = viewer.getTable();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		layoutData.heightHint = 200;
		table.setLayoutData(layoutData);

		final TableViewerColumn dateColumn = createColumn(viewer, "Date");
		dateColumn.setEditingSupport(new ColumnEditingSupport(viewer, PricingPackage.Literals.INDEX_POINT__DATE) {
			@Override
			protected CellEditor getCellEditor(final Object element) {

				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				ed.setFormatter(new YearMonthTextFormatter());
				return ed;
			}

		});
		dateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element != null && element instanceof IndexPoint) {
					final YearMonth date = ((IndexPoint<?>) element).getDate();
					if (date != null) {
						final YearMonthTextFormatter sdf = new YearMonthTextFormatter();
						sdf.setValue(date);
						return sdf.getDisplayString();
					}
				}
				return "";
			}
		});

		// layout.setColumnData(dateColumn.getColumn(), new ColumnWeightData(70, 100));

		final TableViewerColumn valueColumn = createColumn(viewer, "Value");
		valueColumn.setEditingSupport(new ColumnEditingSupport(viewer, PricingPackage.Literals.INDEX_POINT__VALUE) {
			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table) {
				};

				if (indexRawType.equals(EcorePackage.Literals.EINTEGER_OBJECT) || indexRawType.equals(EcorePackage.Literals.EINT)) {
					ed.setFormatter(new IntegerFormatter());
				} else {
					ed.setFormatter(new NumberFormatter());
				}
				return ed;
			}

		});

		valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element != null && element instanceof IndexPoint) {
					final Object value = ((IndexPoint<?>) element).getValue();
					if (value != null)
						return value.toString();
				}
				return "";
			}
		});
		// layout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(30));

		table.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(final Event event) {
				repacker.repack();
			}
		});

		viewer.setContentProvider(createContentProvider());
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		final Composite composite = new Composite(parent, SWT.FULL_SELECTION);
		composite.setLayout(new GridLayout(1, false));
		createTable(composite);
		createButtons(composite);

		control = composite;
		return super.wrapControl(control);
	}

	public SelectionAdapter createAddAdapter() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (originalInput != null) {
					final Object field = originalInput.eGet(feature);
					if (field instanceof DataIndex) {
						final DataIndex<?> index = (DataIndex<?>) field;
						final IndexPoint<?> newPoint;
						if (indexRawType.equals(EcorePackage.Literals.EINTEGER_OBJECT) || indexRawType.equals(EcorePackage.Literals.EINT)) {
							final IndexPoint<Integer> point = PricingFactory.eINSTANCE.createIndexPoint();
							point.setValue(0);
							newPoint = point;
						} else {
							final IndexPoint<?> point = PricingFactory.eINSTANCE.createIndexPoint();
							point.setValue(null);
							newPoint = point;
						}
						// add a new Date with zero time
						final Calendar cal = Calendar.getInstance();

						final YearMonth mMonthYear = new YearMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));

						newPoint.setDate(mMonthYear);

						commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), index, indexPointsFeature, newPoint), index, indexPointsFeature);
						viewer.setSelection(new StructuredSelection(newPoint));
					}
				}
				viewer.refresh();
				repacker.repack();
			}
		};
	}

	public SelectionAdapter createDeleteAdapter() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = viewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final IndexPoint<?> indexPoint = (IndexPoint<?>) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), indexPoint.eContainer(), indexPoint.eContainingFeature(), indexPoint),
							indexPoint.eContainer(), indexPoint.eContainingFeature());
					viewer.refresh();
				}
			}
		};

	}

	public void createButtons(final Composite parent) {
		final Composite buttonComposite = new Composite(parent, SWT.NONE);

		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));

		final GridLayout buttonLayout = new GridLayout(2, true);
		buttonComposite.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;

		final Button remove = new Button(buttonComposite, SWT.NONE);
		remove.setText("Remove");
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		remove.addSelectionListener(createDeleteAdapter());

		final Button add = new Button(buttonComposite, SWT.NONE);
		add.setText("Add");
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		add.addSelectionListener(createAddAdapter());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});

	}

	@Override
	protected void updateDisplay(final Object value) {
		viewer.setInput(value);
		repacker.repack();

	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		originalInput = input;
		super.display(dialogContext, context, input, range);
	}

	@Override
	public Object createLabelLayoutData(final MMXRootObject root, final EObject value, final Control control, final Label label) {
		// TODO Auto-generated method stub
		return new GridData(SWT.RIGHT, SWT.TOP, false, false);
	}

	@Override
	public boolean hasLabel() {
		return false;
	}

	/**
	 * Overrides the default layout data for the editing control, forcing it to span two columns instead of one.
	 */
	@Override
	public Object createLayoutData(final MMXRootObject root, final EObject value, final Control control) {
		final GridData result = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		result.heightHint = 200;
		return result;
	}

}