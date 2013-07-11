/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
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

import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ILabelLayoutDataProvidingEditor;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;

/**
 * @since 2.0
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
	
	// we need to override the layout set by the default display composite
	final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Creates a curve inline editor for Index<clazz> objects. We need to know
	 * what class it was instantiated for, and Java generics don't provide enough
	 * introspection to do so.  
	 * @param feature
	 * @param clazz
	 */
	public CurveInlineEditor(EStructuralFeature feature) {
		super(feature);
		// infer the class from the EMF feature object, whose etype should be a parameterised DataIndex
		indexRawType = feature.getEGenericType().getETypeArguments().get(0).getERawType();
	}
	
	public IStructuredContentProvider createContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				final DataIndex<?> index = (DataIndex<?>) inputElement;
				EList<?> points = index.getPoints();
				IndexPoint<?>[] result = points.toArray(new IndexPoint[0]);

				Arrays.sort(result, new Comparator<IndexPoint<?>>() {

					@Override
					public int compare(IndexPoint<?> arg0, IndexPoint<?> arg1) {
						Date date0 = arg0.getDate(); 
						if (date0 == null) return -1;
						Date date1 = arg1.getDate(); 
						if (date1 == null) return 1;
						return date0.compareTo(date1);
					}

				});
				
				return result;
			}
		};
	}
	
	abstract class ColumnEditingSupport extends EditingSupport {
		final EAttribute attr;
		
		public ColumnEditingSupport(ColumnViewer viewer, EAttribute attr) {
			super(viewer);
			this.attr = attr;
		}

		@Override
		protected void setValue(Object element, Object value) {
			final EditingDomain ed = commandHandler.getEditingDomain();
			commandHandler.handleCommand(
					SetCommand.create(ed, element, 
							attr
							, value)
					, (EObject) element, attr);
			viewer.refresh();
		}
		
		@Override
		protected Object getValue(Object element) {
			return ((EObject)element).eGet(attr);
		}
		
		@Override
		protected boolean canEdit(Object element) {
			return true;
		}
		
	}
	
	public TableViewerColumn createColumn(final TableViewer viewer, final String title, final EAttribute attr) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText(title);
		return column;		
	}

	class Repacker {
		boolean resizing = false;
		public void repack() {
			if (resizing) return;
			resizing = true;
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(i).pack();
			}
			resizing = false;
		}
	}
	
	void createTable(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		table = viewer.getTable();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		layoutData.heightHint = 200;
		table.setLayoutData(layoutData);
		
		
		final TableViewerColumn dateColumn = createColumn(viewer, "Date", PricingPackage.Literals.INDEX_POINT__DATE);
		dateColumn.setEditingSupport(new ColumnEditingSupport(viewer, PricingPackage.Literals.INDEX_POINT__DATE) {
			@Override
			protected CellEditor getCellEditor(Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				return ed;
			}
						
		});
		dateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element != null && element instanceof IndexPoint) {
					Date date = ((IndexPoint<?>) element).getDate();
					if (date != null)
						return sdf.format(date);
				}
				return "";				
			}
		});		

		// layout.setColumnData(dateColumn.getColumn(), new ColumnWeightData(70, 100));
		
		final TableViewerColumn valueColumn = createColumn(viewer, "Value", PricingPackage.Literals.INDEX_POINT__VALUE);
		valueColumn.setEditingSupport(new ColumnEditingSupport(viewer, PricingPackage.Literals.INDEX_POINT__VALUE) {
			@Override
			protected CellEditor getCellEditor(Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				
				if (indexRawType.equals(EcorePackage.Literals.EINTEGER_OBJECT) || indexRawType.equals(EcorePackage.Literals.EINT)) {
					ed.setFormatter(new IntegerFormatter());
				}
				else {
					ed.setFormatter(new NumberFormatter());
				}
				return ed;
			}
						
		});
		
		valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element != null && element instanceof IndexPoint) {
					Object value = ((IndexPoint<?>) element).getValue();
					if (value != null)
						return value.toString();
				}
				return "";				
			}
		});
		//layout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(30));
		

		
		table.addListener(SWT.Resize, 
				new Listener() {
					@Override
					public void handleEvent(Event event) {
						repacker.repack();
					}
				});

		viewer.setContentProvider(createContentProvider());		
	}
	
	@Override
	public Control createControl(Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		final Composite composite = new Composite(parent, SWT.FULL_SELECTION);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(layoutData);
		
		/* 
		// failed attempt to use TableLayoutColumn layout to manage table column width:
		// it reduced the table height to zero!
		final Composite tableComposite = new Composite(composite, SWT.FULL_SELECTION);
		final TableColumnLayout layout = new TableColumnLayout() {
			@Override
			protected Point computeSize(Composite comp, int wHint, int hHint, boolean flushCache) {
				Point result = super.computeSize(comp, hHint, hHint, flushCache);
				result.y = 200;
				return result;
			}
		};
		tableComposite.setLayout(layout);
		//layout.setLayoutData(layoutData);
		createTable(tableComposite, layout);
		*/
		createTable(composite);
		createButtons(composite);		
		
		control = composite;
		return super.wrapControl(control);
	}

	public SelectionAdapter createAddAdapter() {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (originalInput != null) {
					Object field = originalInput.eGet(feature);
					if (field instanceof DataIndex) {
						DataIndex<?> index = (DataIndex<?>) field;
						final IndexPoint<?> newPoint;
						if (indexRawType.equals(EcorePackage.Literals.EINTEGER_OBJECT) || indexRawType.equals(EcorePackage.Literals.EINT)) {
							IndexPoint<Integer> point = PricingFactory.eINSTANCE.createIndexPoint();
							point.setValue(0);
							newPoint = point;
						}
						else {
							IndexPoint<?> point = PricingFactory.eINSTANCE.createIndexPoint();
							point.setValue(null);
							newPoint = point;
						}
						newPoint.setDate(new Date());
						
						commandHandler.handleCommand(
								AddCommand.create(commandHandler.getEditingDomain(), index, indexPointsFeature, newPoint),
								index, indexPointsFeature);
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
			public void widgetSelected(SelectionEvent e) {
				final ISelection sel = viewer.getSelection();
				if (sel.isEmpty()) return;
				if (sel instanceof IStructuredSelection) {
					final IndexPoint<?> indexPoint = (IndexPoint<?>) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(
							RemoveCommand.create(commandHandler.getEditingDomain(), indexPoint.eContainer(), indexPoint.eContainingFeature(), indexPoint),
							indexPoint.eContainer(), indexPoint.eContainingFeature());
					viewer.refresh();
				}
			}
		};
		
	}
	
	public void createButtons(Composite parent) {
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
			public void selectionChanged(SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});
		
		
	}

	@Override
	protected void updateDisplay(Object value) {
		viewer.setInput(value);
		repacker.repack();
		
	}
	
	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		// override the layout data set by default
		control.setLayoutData(layoutData);
		originalInput = input;
		super.display(location, context, input, range);
	}

	@Override
	public Object createLabelLayoutData(MMXRootObject root, EObject value,
			Control control, Label label) {
		// TODO Auto-generated method stub
		return new GridData(SWT.RIGHT, SWT.TOP, false, false);
	}

}