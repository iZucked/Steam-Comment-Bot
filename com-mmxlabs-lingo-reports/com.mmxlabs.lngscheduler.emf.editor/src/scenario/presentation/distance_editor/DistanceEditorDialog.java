/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.distance_editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;

import scenario.port.DistanceModel;
import scenario.port.PortPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.importer.CSVReader;
import scenario.presentation.cargoeditor.importer.DeferredReference;
import scenario.presentation.cargoeditor.importer.EObjectImporter;
import scenario.presentation.cargoeditor.importer.EObjectImporterFactory;
import scenario.presentation.cargoeditor.importer.NamedObjectRegistry;

/**
 * A dialog for containing a {@link DistanceLineViewer}. It's easier to have it
 * in a dialog at the moment so it doesn't have to adapt the portmodel &c. to
 * update.
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceEditorDialog extends Dialog {
	private static final int IMPORT_CSV = 0x10;
	private DistanceLineViewer viewer;
	private DistanceModel distanceModel;
	private ScenarioEditor part;

	public DistanceEditorDialog(Shell parentShell) {
		super(parentShell);
	}

	public DistanceEditorDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);

		this.viewer = new DistanceLineViewer(parent, SWT.FULL_SELECTION
				| SWT.BORDER);

		viewer.init(part);

		final Table table = viewer.getTable();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer.setInput(distanceModel);

		viewer.refresh();

		return c;
	}

	@Override
	public void create() {
		super.create();
		getShell().setSize(800, 600);
		getShell().setText("Distance Matrix Editor");

		// center shell
		// center in parent window
		final Rectangle shellBounds = getParentShell().getBounds();
		final Point dialogSize = getShell().getSize();

		getShell().setLocation(
				shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
				shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

		final TableColumn[] columns = viewer.getTable().getColumns();
		for (final TableColumn c : columns) {
			c.pack();
		}
	}

	public DistanceModel getResult() {
		return distanceModel;
	}

	public int open(final ScenarioEditor part, final DistanceModel dm) {
		this.part = part;
		this.distanceModel = EcoreUtil.copy(dm);

		return super.open();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		createButton(parent, IMPORT_CSV, "Import CSV", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IMPORT_CSV) {
			final FileDialog openDialog = new FileDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					SWT.OPEN);

			openDialog.setFilterExtensions(new String[] { "*.csv" });
			openDialog
					.setText("Choose a CSV file from which to import a distance matrix");

			final String inputFileName = openDialog.open();
			if (inputFileName == null)
				return;

			final EObjectImporter importer = EObjectImporterFactory
					.getInstance().getImporter(
							PortPackage.eINSTANCE.getDistanceModel());
			final List<DeferredReference> deferments = new ArrayList<DeferredReference>();

			final NamedObjectRegistry registry = new NamedObjectRegistry();

			registry.addEObjects(part.getScenario());

			CSVReader reader;
			try {
				reader = new CSVReader(inputFileName);
				final Collection<EObject> importedObjects = importer
						.importObjects(reader, deferments, registry);

				// there should be a single distance model here
				final Map<Pair<EClass, String>, EObject> m = registry
						.getContents();
				for (final DeferredReference dr : deferments) {
					dr.setRegistry(m);
					dr.run();
				}
				final DistanceModel dm = (DistanceModel) importedObjects
						.iterator().next();
				this.distanceModel = dm;
				viewer.setInput(this.distanceModel);
				viewer.refresh();
			} catch (IOException e) {
			}
		} else {
			super.buttonPressed(buttonId);
		}
	}

}
