/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */

package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;

import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.IFeatureEditor.IFeatureManipulator;

/**
 * A viewerpane which displays a list of EObjects in a table and lets you edit
 * them
 * 
 * @author hinton
 * 
 */
public class EObjectEditorViewerPane extends ViewerPane {
	private static final String FEATURE_MANIPULATOR_KEY = "feature-formatter";
	private final ScenarioEditor part;
	private TableViewer viewer;

	private final List<CellEditor> cellEditors = new ArrayList<CellEditor>();
	private final Map<String, TableColumn> tableColumns = new HashMap<String, TableColumn>();

	private final Map<EClass, IFeatureEditor> featureEditorsByRefType = new HashMap<EClass, IFeatureEditor>();

	private final Map<EStructuralFeature, IFeatureEditor> featureEditorsByFeature = new HashMap<EStructuralFeature, IFeatureEditor>();

	private final Map<EDataType, IFeatureEditor> featureEditorsByAttributeType = new HashMap<EDataType, IFeatureEditor>();
	private final IFeatureEditor defaultFeatureEditor;

	public EObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.part = part;
		defaultFeatureEditor = new DefaultAttributeEditor(
				part.getEditingDomain());
	}

	@Override
	public Viewer createViewer(final Composite parent) {
		viewer = new TableViewer(parent);
		return viewer;
	}

	public void init(final List<EReference> path,
			final AdapterFactory adapterFactory) {

		// TODO figure out why this doesn't work
		getToolBarManager().add(new Action("Pack Columns") {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					final TableColumn[] columns = viewer.getTable()
							.getColumns();
					for (TableColumn c : columns) {
						c.pack();
					}
				}
			}
		});

		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		cellEditors.clear();
		tableColumns.clear();
		createTableColumns(table, path.get(path.size() - 1).getEReferenceType());

		viewer.setUseHashlookup(true);
		final String[] columnNames = new String[table.getColumnCount()];
		for (int i = 0; i < table.getColumnCount(); i++) {
			columnNames[i] = table.getColumn(i).getText();
		}
		viewer.setColumnProperties(columnNames);

		viewer.setCellEditors(cellEditors.toArray(new CellEditor[] {}));
		viewer.setCellModifier(new ICellModifier() {
			@Override
			public boolean canModify(final Object row, final String columnName) {
				final IFeatureManipulator manipulator = (IFeatureManipulator) tableColumns
						.get(columnName).getData(FEATURE_MANIPULATOR_KEY);

				return manipulator.canModify((EObject) row);
			}

			@Override
			public Object getValue(final Object row, final String columnName) {
				final TableColumn col = tableColumns.get(columnName);
				final IFeatureManipulator manipulator = (IFeatureManipulator) col
						.getData(FEATURE_MANIPULATOR_KEY);
				return manipulator.getEditorValue((EObject) row);
			}

			@Override
			public void modify(Object row, final String columnName,
					final Object newValue) {
				final TableColumn col = tableColumns.get(columnName);
				final IFeatureManipulator formatter = (IFeatureManipulator) col
						.getData(FEATURE_MANIPULATOR_KEY);

				final EObject modelObject = row instanceof TableItem ? ((EObject) ((TableItem) row)
						.getData()) : (row instanceof EObject ? (EObject) row
						: null);

				if (modelObject != null) {
					formatter.setFromEditorValue(modelObject, newValue);
					viewer.refresh(modelObject, true, true);

					// TODO this doesn't work, no idea why not.
					viewer.setSelection(new StructuredSelection(modelObject));
				}
			}
		});
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {

			@SuppressWarnings("rawtypes")
			@Override
			public Object[] getElements(Object object) {
				if (object instanceof EObject) {
					EObject o = (EObject) object;
					for (final EReference ref : path) {
						object = o.eGet(ref);
						if (object instanceof EList) {
							return ((EList) object).toArray();
						}
						if (object instanceof EObject) {
							o = (EObject) object;
						}
					}

					return new Object[] { o };
				}
				return new Object[] {};
			}
		});

		viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory) {
			@Override
			public String getColumnText(final Object object,
					final int columnIndex) {
				if (object instanceof EObject) {
					final EObject e = (EObject) object;
					final IFeatureManipulator formatter = (IFeatureManipulator) table
							.getColumn(columnIndex).getData(
									FEATURE_MANIPULATOR_KEY);
					return formatter.getStringValue(e);
				}
				return "";
			}
		});
	}

	@Override
	protected void requestActivation() {
		super.requestActivation();
		part.setCurrentViewerPane(this);
	}

	private void createTableColumns(final Table table, final EClass rowClass) {
		createTableColumnsForEClassAtPath(table, rowClass,
				new LinkedList<EReference>());
	}

	private void createTableColumnsForEClassAtPath(final Table table,
			final EClass eClass, final LinkedList<EReference> path) {

		String suffix = "";
		for (final EReference ref : path) {
			suffix = " of " + unmangle(ref.getName()) + suffix;
		}

		// Add columns for all attributes
		for (final EAttribute feature : eClass.getEAllAttributes()) {
			IFeatureEditor editor = featureEditorsByFeature.get(feature);

			if (editor == null)
				editor = featureEditorsByAttributeType.get(feature
						.getEAttributeType());

			if (editor == null)
				editor = defaultFeatureEditor;

			if (editor != null) {
				createColumn(table, path, feature, editor, suffix);
			}
		}

		// Now handle contained objects
		for (final EReference reference : eClass.getEAllReferences()) {
			if (reference.isMany())
				continue; // TODO handle multiple references.
			if (reference.isContainment()) {
				// contained objects get flattened out
				final LinkedList<EReference> subPath = new LinkedList<EReference>(
						path);
				subPath.add(reference);
				createTableColumnsForEClassAtPath(table,
						reference.getEReferenceType(), subPath);
			} else {
				// non-contained objects get an editor
				IFeatureEditor editor = featureEditorsByFeature.get(reference);
				if (editor == null) {
					editor = featureEditorsByRefType.get(reference
							.getEReferenceType());
				}
				if (editor != null) {
					createColumn(table, path, reference, editor, suffix);
				}
			}
		}
	}

	private String unmangle(final String name) {
		final StringBuilder sb = new StringBuilder();
		boolean lastWasLower = true;
		boolean firstChar = true;
		for (final char c : name.toCharArray()) {
			if (firstChar) {
				sb.append(Character.toUpperCase(c));
				firstChar = false;
			} else {
				if (lastWasLower && Character.isUpperCase(c))
					sb.append(" ");
				lastWasLower = Character.isLowerCase(c);
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private void createColumn(final Table table,
			final LinkedList<EReference> path, final EStructuralFeature field,
			final IFeatureEditor editor, final String suffix) {
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setResizable(true);
		
		column.setText(unmangle(field.getName()) + suffix);

		tableColumns.put(column.getText(), column);

		final IFeatureManipulator manipulator = editor.getFeatureManipulator(
				path, field);
		column.pack();
		column.setData(FEATURE_MANIPULATOR_KEY, manipulator);

		// create cell editor

		final CellEditor cellEditor = manipulator.createCellEditor(table);

		cellEditors.add(cellEditor);
	}

	public void setFeatureEditorForReferenceType(final EClass referenceType,
			final IFeatureEditor editor) {
		featureEditorsByRefType.put(referenceType, editor);
	}

	public void setFeatureEditorForAttributeType(final EDataType attributeType,
			final IFeatureEditor editor) {
		featureEditorsByAttributeType.put(attributeType, editor);
	}

	public void setFeatureEditorForFeature(final EStructuralFeature feature,
			final IFeatureEditor editor) {
		featureEditorsByFeature.put(feature, editor);
	}

	public TableViewer getViewer() {
		return viewer;
	}
}
