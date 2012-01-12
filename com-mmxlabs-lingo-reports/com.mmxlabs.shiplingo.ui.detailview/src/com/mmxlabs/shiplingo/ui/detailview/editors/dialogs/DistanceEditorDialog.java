/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors.dialogs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import scenario.Scenario;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortPackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.shiplingo.importer.importers.ExportCSVAction;
import com.mmxlabs.shiplingo.importer.importers.ImportCSVAction;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;

/**
 * A dialog for containing a {@link DistanceLineViewer}. It's easier to have it in a dialog at the moment so it doesn't have to adapt the portmodel &c. to update.
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceEditorDialog extends Dialog {
	private static final int IMPORT_CSV = 0x10;
	private DistanceLineViewer viewer;
	private DistanceModel distanceModel;
	private EditingDomain editingDomain;
	private IValueProviderProvider valueProviderProvider;

	public DistanceEditorDialog(Shell parentShell) {
		super(parentShell);
	}

	public DistanceEditorDialog(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
		final Composite c2 = new Composite(c, SWT.NONE);
		final GridLayout c2Layout = new GridLayout(1, false);
		c2Layout.marginHeight = c2Layout.marginWidth = c2Layout.verticalSpacing = c2Layout.horizontalSpacing = 0;

		c2.setLayout(c2Layout);

		final ToolBarManager barManager = new ToolBarManager(SWT.BORDER | SWT.RIGHT);

		barManager.add(new ImportCSVAction() {
			@Override
			protected EObject getToplevelObject() {
				return valueProviderProvider.getModel();
			}

			@Override
			protected EClass getImportClass() {
				return PortPackage.eINSTANCE.getDistanceModel();
			}

			@Override
			public void addObjects(Collection<EObject> newObjects) {
				assert newObjects.size() == 1;
				final DistanceModel newModel = (DistanceModel) newObjects.iterator().next();
				DistanceEditorDialog.this.distanceModel = newModel;
				DistanceEditorDialog.this.viewer.setInput(DistanceEditorDialog.this.distanceModel);
				DistanceEditorDialog.this.viewer.refresh();
			}
		});

		barManager.add(new ExportCSVAction() {
			@Override
			public List<EObject> getObjectsToExport() {
				return Collections.singletonList((EObject) distanceModel);
			}

			@Override
			public EClass getExportEClass() {
				return distanceModel.eClass();
			}
		});

		final Composite barComposite = new Composite(c2, SWT.NONE);
		final GridLayout barLayout = new GridLayout(5, false);
		barLayout.marginWidth = 2;
		barLayout.marginTop = 0;
		barComposite.setLayout(barLayout);

		this.viewer = new DistanceLineViewer(c2, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		viewer.init(editingDomain, ((Scenario) valueProviderProvider.getModel()));

		final Grid grid = viewer.getGrid();

		final Label rowLabel = new Label(barComposite, SWT.NONE);
		rowLabel.setText("Row:");
		final Text rowFilter = new Text(barComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		rowFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Label columnLabel = new Label(barComposite, SWT.NONE);
		columnLabel.setText("Column:");
		final Text columnFilter = new Text(barComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		columnFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		columnFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				final String columnFilterText = columnFilter.getText().trim().toLowerCase();

				if (columnFilterText.isEmpty()) {
					for (final GridColumn column : grid.getColumns()) {
						column.setVisible(true);
					}
				} else {

					for (final GridColumn column : grid.getColumns()) {
						final String name = column.getText();
						column.setVisible(name.toLowerCase().contains(columnFilterText));
					}
				}
			}
		});
		getShell().addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.keyCode == SWT.ESC && (columnFilter.isFocusControl() || rowFilter.isFocusControl()))
					e.doit = false;
			}
		});

		columnFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					if (columnFilter.getText().isEmpty())
						getButton(CANCEL).notifyListeners(SWT.Selection, new Event());
					else
						columnFilter.setText("");
				}
			}
		});

		rowFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					if (rowFilter.getText().isEmpty())
						getButton(CANCEL).notifyListeners(SWT.Selection, new Event());
					else
						rowFilter.setText("");
				}
			}
		});
		
		rowFilter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				final String text = rowFilter.getText().trim().toLowerCase();
				if (text.isEmpty()) {
					viewer.setFilters(new ViewerFilter[0]);
				} else {
					viewer.setFilters(new ViewerFilter[] {
						new ViewerFilter() {							
							@Override
							public boolean select(Viewer viewer, Object parentElement, Object element) {
								return ((Pair<Port, ?>) element).getFirst().getName().toLowerCase().contains(text);
							}
						}
					});
				}
			}
		});

		barComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		barManager.createControl(barComposite).setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final CopyGridToClipboardAction copyAction = new CopyGridToClipboardAction(viewer.getGrid());
		copyAction.setRowHeadersIncluded(true);
		barManager.add(copyAction);
		barManager.update(true);

		final Grid table = viewer.getGrid();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);
		table.setCellSelectionEnabled(true);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer.setInput(distanceModel);

		viewer.refresh();

		c2.setLayoutData(new GridData(GridData.FILL_BOTH));

		final GridLayout cLayout = (GridLayout) c.getLayout();

		cLayout.marginWidth = cLayout.marginHeight = 0;

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

		getShell().setLocation(shellBounds.x + (shellBounds.width - dialogSize.x) / 2, shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

		final GridColumn[] columns = viewer.getGrid().getColumns();
		for (final GridColumn c : columns) {
			c.pack();
		}
	}

	public DistanceModel getResult() {
		return distanceModel;
	}

	public int open(final IValueProviderProvider valueProviderProvider, final EditingDomain editingDomain, final DistanceModel dm) {
		this.valueProviderProvider = valueProviderProvider;
		this.editingDomain = editingDomain;
		this.distanceModel = EcoreUtil.copy(dm);

		return super.open();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
