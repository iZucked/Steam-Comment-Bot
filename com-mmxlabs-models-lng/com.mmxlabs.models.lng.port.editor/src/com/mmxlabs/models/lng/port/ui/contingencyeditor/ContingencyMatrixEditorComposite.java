/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.contingencyeditor;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;

public class ContingencyMatrixEditorComposite extends DefaultDetailComposite {

	private static final String CONTINGENCY_MATRIX_EDITOR_TOOLBAR_ID = "toolbar:com.mmxlabs.models.lng.port.ui.contingencyeditor.toolbar";
	private ContingencyMatrixViewer viewer;
	private ContingencyMatrix contingencyMatrix;
	private EditingDomain editingDomain;
	private LNGScenarioModel rootObject;
	private static WeakReference<PortModel> currentPortModel;

	private static WeakReference<EditingDomain> currentEditingDomain;

	public ContingencyMatrixEditorComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	public void createControls(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject dm, final EMFDataBindingContext dbc) {

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		sel.getReferenceValueProviderCache();
		this.editingDomain = sel.getEditingDomain();
		currentEditingDomain = new WeakReference<>(editingDomain);
		this.contingencyMatrix = (ContingencyMatrix) dm;
		this.rootObject = (LNGScenarioModel) sel.getRootObject();
		currentPortModel = new WeakReference<>(rootObject.getReferenceModel().getPortModel());

		final Composite c2 = new Composite(this, SWT.NONE) {

			@Override
			protected void checkSubclass() {
			}

			@Override
			public Point computeSize(final int wHint, final int hHint, final boolean changed) {
				final Point p = super.computeSize(wHint, hHint, changed);

				// Special handling to open dialog with an initial size but allow the control to grow after.
				if (hHint == -1) {
					p.y = 600;
				} else {
					p.y = Math.max(p.y, 600);
				}
				if (wHint == -1) {
					p.x = 800;
				} else {
					p.x = Math.max(p.x, 800);
				}
				return p;
			}
		};
		final GridLayout c2Layout = new GridLayout(1, false);
		c2Layout.marginHeight = c2Layout.marginWidth = c2Layout.verticalSpacing = c2Layout.horizontalSpacing = 0;
		c2.setLayout(c2Layout);

		final Composite barComposite = new Composite(c2, SWT.NONE);
		final GridLayout barLayout = new GridLayout(5, false);
		barLayout.marginWidth = 2;
		barLayout.marginTop = 0;
		barComposite.setLayout(barLayout);

		this.viewer = new ContingencyMatrixViewer(c2, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.init(editingDomain, rootObject);

		final Grid grid = viewer.getGrid();
		final Label rowLabel = new Label(barComposite, SWT.NONE);
		rowLabel.setText("From:");
		final Text rowFilter = new Text(barComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		rowFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		rowFilter.setToolTipText("Filter from rows to show source ports with names containing this text");

		final Label columnLabel = new Label(barComposite, SWT.NONE);
		columnLabel.setText("To:");
		final Text columnFilter = new Text(barComposite, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		columnFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		columnFilter.setToolTipText("Filter to columns to show destination ports with names containing this text");

		columnFilter.addModifyListener(e -> {
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
		});

		getShell().addTraverseListener(e -> {
			if ((e.keyCode == SWT.ESC) && (columnFilter.isFocusControl() || rowFilter.isFocusControl())) {
				e.doit = false;
			}
		});

		rowFilter.addModifyListener(e -> {
			final String text = rowFilter.getText().trim().toLowerCase();
			if (text.isEmpty()) {
				viewer.setFilters();
			} else {
				viewer.setFilters(new ViewerFilter() {
					@Override
					public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
						return ((Pair<Port, ?>) element).getFirst().getName().toLowerCase().contains(text);
					}
				});
			}
		});

		barComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Grid table = viewer.getGrid();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);
		table.setCellSelectionEnabled(true);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		viewer.setInput(contingencyMatrix);

		viewer.refresh();
		c2.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		final GridLayout cLayout = (GridLayout) this.getLayout();

		cLayout.marginWidth = cLayout.marginHeight = 0;

		final Button btn = new Button(this, SWT.PUSH);
		btn.setText("Set all values to...");
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final InputDialog input = new InputDialog(sel.getShell(), "Set voyage delays", "Enter number of days delay for all voyages. Zero or empty will remove all delays", "0", newText -> {
					if (newText.trim().isEmpty()) {
						return null;
					}
					try {
						Integer.parseInt(newText);
					} catch (final NumberFormatException ee) {
						return "Invalid number";
					}
					return null;
				});
				if (input.open() == Window.OK) {
					final String value = input.getValue();
					if (value == null || value.isEmpty() || value.trim().equals("0")) {
						if (!contingencyMatrix.getEntries().isEmpty()) {
							sel.getEditingDomain().getCommandStack()
									.execute(RemoveCommand.create(sel.getEditingDomain(), contingencyMatrix, PortPackage.Literals.CONTINGENCY_MATRIX__ENTRIES, contingencyMatrix.getEntries()));
						}
					} else {
						final int days = Integer.parseInt(value.trim());
						final CompoundCommand cmd = new CompoundCommand();
						cmd.append(RemoveCommand.create(sel.getEditingDomain(), contingencyMatrix, PortPackage.Literals.CONTINGENCY_MATRIX__ENTRIES, contingencyMatrix.getEntries()));
						final PortModel portModel = ScenarioModelUtil.getPortModel(sel.getScenarioDataProvider());
						final List<ContingencyMatrixEntry> entries = new LinkedList<>();
						for (final Port from : portModel.getPorts()) {
							for (final Port to : portModel.getPorts()) {
								if (from != to) {
									final ContingencyMatrixEntry ee = PortFactory.eINSTANCE.createContingencyMatrixEntry();
									ee.setDuration(days);
									ee.setFromPort(from);
									ee.setToPort(to);
									entries.add(ee);
								}
							}
						}
						if (!entries.isEmpty()) {
							cmd.append(AddCommand.create(sel.getEditingDomain(), contingencyMatrix, PortPackage.Literals.CONTINGENCY_MATRIX__ENTRIES, entries));
						}
						if (!cmd.isEmpty()) {
							sel.getEditingDomain().getCommandStack().execute(cmd);
						}
					}
					viewer.refresh();

				}

				super.widgetSelected(e);
			}
		});
	}

	public ContingencyMatrix getResult() {
		return contingencyMatrix;
	}

	public static PortModel getCurrentPortModel() {
		return currentPortModel.get();
	}

	/**
	 * Return the current {@link EditingDomain} or null if not available.
	 * 
	 */
	@Nullable
	public static EditingDomain getEditingDomain() {
		return currentEditingDomain.get();
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		this.object = object;
		setLayout(layoutProvider.createDetailLayout(root, object));

		createControls(dialogContext, root, object, dbc);
		checkVisibility(dialogContext);

		viewer.setInput(contingencyMatrix);

		viewer.refresh();

	}
}
