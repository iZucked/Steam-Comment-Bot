/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import com.google.common.base.Joiner;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class PreDefinedDatesInlineEditorDialog extends Dialog {
	private EObjectTableViewer tableViewer;

	private EObject container;
	private EReference containment;

	private AdapterFactory adapterFactory;
	private EditingDomain editingDomain;
	private ModelReference modelReference;

	public PreDefinedDatesInlineEditorDialog(final Shell parentShell) {
		super(parentShell);
	}

	public PreDefinedDatesInlineEditorDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite content = (Composite) super.createDialogArea(parent);
		tableViewer = new EObjectTableViewer(content, SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL);

		tableViewer.init(adapterFactory, modelReference, containment);

		final Grid table = tableViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// table.setRowHeaderVisible(true);

		final GridViewerColumn dateCol = tableViewer.addTypicalColumn("Date", new LocalDateAttributeManipulator(ADPPackage.Literals.PRE_DEFINED_DATE__DATE, editingDomain));

		tableViewer.setInput(container);

		tableViewer.refresh();

		// Keep column width equals to the table width
		table.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(final ControlEvent e) {
				dateCol.getColumn().setWidth(table.getBounds().width);

			}
		});

		table.setLayoutData(GridDataFactory.fillDefaults().hint(100, 300).minSize(100, 300).create());

		final Composite buttonBar = new Composite(content, SWT.NONE);
		buttonBar.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		buttonBar.setLayout(GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(true).create());
		{
			final Button btn = new Button(buttonBar, SWT.PUSH);
			btn.setText("+");
			btn.setLayoutData(GridDataFactory.fillDefaults().create());
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final PreDefinedDate newDate = ADPFactory.eINSTANCE.createPreDefinedDate();
					newDate.setDate(LocalDate.now());
					((PreDefinedDistributionModel) container).getDates().add(newDate);
					tableViewer.refresh();
					getShell().pack();
				}
			});
		}
		{
			final Button btn = new Button(buttonBar, SWT.PUSH);
			btn.setText("-");
			btn.setLayoutData(GridDataFactory.fillDefaults().create());

			btn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {

					final ISelection selection = tableViewer.getSelection();
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ss = (IStructuredSelection) selection;
						final Iterator<?> itr = ss.iterator();
						while (itr.hasNext()) {
							((PreDefinedDistributionModel) container).getDates().remove(itr.next());
						}
						tableViewer.refresh();
						getShell().pack();
					}
				}
			});
		}
		{
			final Button btn = new Button(buttonBar, SWT.PUSH);
			btn.setText("Import");
			btn.setLayoutData(GridDataFactory.fillDefaults().create());
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					// ((PreDefinedDistributionModel) container).getDates().remove(newDate);
					// tableViewer.refresh();
					// getShell().pack();

					final FileDialog dialog = new FileDialog(getShell());
					dialog.setFilterExtensions(new String[] { "*.csv" });
					dialog.setText("Import pre-defined slots from csv");
					final String result = dialog.open();
					if (result != null) {
						final List<LocalDate> dates = new LinkedList<>();
						final List<String> errors = new LinkedList<>();
						try (CSVReader reader = new CSVReader(',', new FileInputStream(result))) {
							final LocalDateAttributeImporter dateImporter = new LocalDateAttributeImporter();
							for (IFieldMap row = reader.readRow(true); row != null; row = reader.readRow(true)) {
								for (final String v : row.values()) {
									if (v.isEmpty()) {
										continue;
									}
									try {
										dates.add(dateImporter.parseLocalDate(v));
									} catch (final ParseException e1) {
										errors.add(v);
									}
								}
							}
							((PreDefinedDistributionModel) container).getDates().clear();
							for (final LocalDate d : dates) {
								final PreDefinedDate newDate = ADPFactory.eINSTANCE.createPreDefinedDate();
								newDate.setDate(d);
								((PreDefinedDistributionModel) container).getDates().add(newDate);
							}
							tableViewer.refresh();
							getShell().pack();

						} catch (final FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							MessageDialog.openError(getShell(), "Error importing dates", e1.getMessage());
						} catch (final IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							MessageDialog.openError(getShell(), "Error importing dates", e1.getMessage());
						}
						if (!errors.isEmpty()) {
							MessageDialog.openError(getShell(), "Error importing dates", "Unable to parse " + Joiner.on(", ").join(errors));
						}
					}
				}
			});
		}
		return content;
	}

	public int open(final AdapterFactory adapterFactory, final ModelReference modelReference, final MMXRootObject rootObject, final EObject container, final EReference containment) {
		this.container = EcoreUtil.copy(container);
		this.containment = containment;

		this.modelReference = modelReference;
		this.editingDomain = modelReference.getEditingDomain();
		this.adapterFactory = adapterFactory;

		return super.open();
	}

	public EList<LocalDate> getResult() {
		return (EList<LocalDate>) this.container.eGet(containment);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Pre defined dates");
	}
}
