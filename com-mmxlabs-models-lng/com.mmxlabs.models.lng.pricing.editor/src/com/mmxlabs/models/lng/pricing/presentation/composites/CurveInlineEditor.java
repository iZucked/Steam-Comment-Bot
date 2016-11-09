/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
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
import org.eclipse.swt.widgets.Table;

import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.AbstractTableInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 */
public class CurveInlineEditor extends AbstractTableInlineEditor {

	private Index<?> index = null;
	private EObject originalInput = null;
	private final EClassifier indexRawType;
	private EStructuralFeature indexPointsFeature = PricingPackage.Literals.DATA_INDEX__POINTS;

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

				return result;
			}
		};
	}

	abstract class ColumnEditingSupport extends EditingSupport {
		final EStructuralFeature feature;

		public ColumnEditingSupport(final GridTableViewer viewer, final EStructuralFeature feature) {
			super(viewer);
			this.feature = feature;
		}

		@Override
		protected void setValue(final Object element, final Object value) {
			final EditingDomain ed = commandHandler.getEditingDomain();
			final Command cmd = SetCommand.create(ed, element, feature, value);
			assert cmd.canExecute();
			commandHandler.handleCommand(cmd, (EObject) element, feature);
			assert ((EObject) element).eGet(feature) == value;
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

	protected TableViewer createTable(final Composite parent) {
		TableViewer viewer = new TableViewer(parent, SWT.FULL_SELECTION);

		Table table = viewer.getTable();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		layoutData.heightHint = 200;
		table.setLayoutData(layoutData);

		final TableViewerColumn dateColumn = createColumn(viewer, "Date");
		dateColumn.setEditingSupport(new EditingSupport(viewer) {
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table, SWT.BORDER);
				ed.setFormatter(new YearMonthTextFormatter());
				addCellEditorListener(ed);
				return ed;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				return ((EObject) element).eGet(PricingPackage.Literals.INDEX_POINT__DATE);
			}

			@Override
			protected void setValue(Object element, Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, PricingPackage.Literals.INDEX_POINT__DATE, value), (EObject) element, PricingPackage.Literals.INDEX_POINT__DATE);
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

		final TableViewerColumn valueColumn = createColumn(viewer, "Value");
		valueColumn.setEditingSupport(new EditingSupport(viewer) {
			@Override
			protected CellEditor getCellEditor(final Object element) {

				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);

				if (indexRawType.equals(EcorePackage.Literals.EINTEGER_OBJECT) || indexRawType.equals(EcorePackage.Literals.EINT)) {
					ed.setFormatter(new IntegerFormatter());
				} else {
					ed.setFormatter(new NumberFormatter());
				}
				addCellEditorListener(ed);
				return ed;
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected Object getValue(Object element) {
				return ((EObject) element).eGet(PricingPackage.Literals.INDEX_POINT__VALUE);
			}

			@Override
			protected void setValue(Object element, Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, PricingPackage.Literals.INDEX_POINT__VALUE, value), (EObject) element, PricingPackage.Literals.INDEX_POINT__VALUE);
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

		viewer.setContentProvider(createContentProvider());

		viewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				if (e1 instanceof IndexPoint<?> && e2 instanceof IndexPoint<?>) {
					final IndexPoint<?> arg0 = (IndexPoint<?>) e1;
					final IndexPoint<?> arg1 = (IndexPoint<?>) e2;
					final YearMonth date0 = arg0.getDate();
					if (date0 == null)
						return -1;
					final YearMonth date1 = arg1.getDate();
					if (date1 == null)
						return 1;
					return date0.compareTo(date1);
				}

				return super.compare(viewer, e1, e2);
			}

		});

		return viewer;
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
							final IndexPoint point = PricingFactory.eINSTANCE.createIndexPoint();
							point.setValue(null);
							newPoint = point;
						}
						// add a new Date with zero time
						final Calendar cal = Calendar.getInstance();

						final YearMonth mMonthYear = YearMonth.of(cal.get(Calendar.YEAR), 1 + cal.get(Calendar.MONTH));

						newPoint.setDate(mMonthYear);

						final Command cmd = AddCommand.create(commandHandler.getEditingDomain(), index, indexPointsFeature, newPoint);
						assert cmd.canExecute();
						commandHandler.handleCommand(cmd, index, indexPointsFeature);
						tableViewer.refresh();

						tableViewer.setSelection(new StructuredSelection(newPoint));
					}
				}
				tableViewer.refresh();
				repacker.repack();
			}
		};
	}

	public SelectionAdapter createDeleteAdapter() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty()) {
					return;
				}
				if (sel instanceof IStructuredSelection) {
					final IndexPoint<?> indexPoint = (IndexPoint<?>) ((IStructuredSelection) sel).getFirstElement();
					final Command cmd = RemoveCommand.create(commandHandler.getEditingDomain(), indexPoint.eContainer(), indexPoint.eContainingFeature(), indexPoint);
					assert cmd.canExecute();
					commandHandler.handleCommand(cmd, indexPoint.eContainer(), indexPoint.eContainingFeature());
					tableViewer.refresh();
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

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});

	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		originalInput = input;
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected @Nullable String getTableLabelText() {
		return null;
	}

}