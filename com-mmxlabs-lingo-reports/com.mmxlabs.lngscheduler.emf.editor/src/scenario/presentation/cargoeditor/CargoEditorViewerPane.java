/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPage;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.presentation.ScenarioEditor;

/**
 * @author hinton
 * 
 */
public class CargoEditorViewerPane extends ViewerPane {
	private static final String VALUE_GETTER_KEY = "CargoEditorViewerPaneValueGetter";
	private TableViewer viewer;
	private ScenarioEditor editor;

	public CargoEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
		this.editor = part;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.common.ui.ViewerPane#createViewer(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public Viewer createViewer(final Composite parent) {
		viewer = new TableViewer(parent);
		return viewer;
	}

	@Override
	protected void requestActivation() {
		super.requestActivation();
		editor.setCurrentViewerPane(this);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void init(final AdapterFactory adapterFactory) {
		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createTableColumns(CargoPackage.eINSTANCE.getCargo(), table,
				new LinkedList<EReference>(), true);

		viewer.setColumnProperties(new String[] { "a", "b" });
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory) {
			@Override
			public Object[] getElements(Object object) {
				System.err.println("getElements " + object);
				while (object instanceof EObject) {
					if (object instanceof Scenario) {
						final Scenario scenario = (Scenario) object;
						final EList<Cargo> cargoes = scenario.getCargoModel()
								.getCargoes();
						return cargoes.toArray();
					} else {
						object = ((EObject) object).eContainer();
					}
				}
				return new Object[] {};
			}
		});
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory) {
			@Override
			public String getColumnText(final Object object,
					final int columnIndex) {
				if (object instanceof Cargo) {
					final ValueGetter getter = (ValueGetter) table.getColumn(
							columnIndex).getData(VALUE_GETTER_KEY);
					return getter.getValue((EObject) object).toString();
				} else {
					return "ERROR";
				}
			}
		});
	}

	/**
	 * Recursively reflexively create columns to display an EMF object and all
	 * its contained objects
	 * 
	 * @param eClass
	 * @param table
	 */
	private void createTableColumns(final EClass eClass, final Table table,
			final List<EReference> references, final boolean containment) {
		String suffix = "";
		for (final EReference ref : references) {
			suffix = " of " + ref.getName() + suffix;
		}

		if (containment == false) {
			if (eClass.getEIDAttribute() != null) {
				createTableColumn(table, eClass.getEIDAttribute(), references, suffix);
				return;
			}
		}
		
		for (final EAttribute attribute : eClass.getEAllAttributes()) {
			if (containment == false) {
				// hack - we only display name or id fields 
				if (attribute.getName().equalsIgnoreCase("name") == false)
					continue;
			}
			createTableColumn(table, attribute, references, suffix);
			if (!containment) return;
		}
		if (!containment) // don't chase any more references
			return;
		for (final EReference reference : eClass.getEAllReferences()) {
			if (reference.isMany())
				continue;
			final List<EReference> path = new LinkedList<EReference>(references);
			path.add(reference);
			createTableColumns(reference.getEReferenceType(), table, path,
					reference.isContainment());
		}
	}

	private void createTableColumn(final Table table,
			final EAttribute attribute, final List<EReference> path,
			final String suffix) {
		final TableColumn column = new TableColumn(table, SWT.NONE);
		column.setResizable(true);
		column.setText(attribute.getName() + suffix);
		column.setData(VALUE_GETTER_KEY, new ValueGetter(path, attribute));
		column.pack();
	}

	/**
	 * Gets a value from a path into an EObject composed of some references
	 * followed by an attribute.
	 * 
	 * @author hinton
	 * 
	 */
	private class ValueGetter {
		private final List<EReference> references;
		private final EAttribute attribute;

		public ValueGetter(final List<EReference> references,
				final EAttribute attribute) {
			this.references = references;
			this.attribute = attribute;
		}

		public Object getValue(EObject from) {
			for (final EReference reference : references) {
				from = (EObject) from.eGet(reference);
			}
			return from.eGet(attribute);
		}
	}
}
