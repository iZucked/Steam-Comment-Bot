/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.dialogs;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.swtchart.Chart;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.Range;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.rcp.common.celleditors.SpinnerCellEditor;

public class VesselStateAttributesDialog extends Dialog {
	protected Shell shlFuelCurve;
	private Table fuelCurveTable;
	private VesselStateAttributes attributes;
	private Button btnRemoveLine;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public VesselStateAttributesDialog(final Shell parent, final int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public VesselStateAttributes open(final VesselStateAttributes attributes, String state) {
		return this.open(attributes, state, false);
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public VesselStateAttributes open(final VesselStateAttributes attributes, String state, final boolean hidingTopPart) {
		this.attributes = EcoreUtil.copy(attributes);
		createContents(state, hidingTopPart);
		shlFuelCurve.open();
		shlFuelCurve.layout();
		final Display display = getParent().getDisplay();

		// center in parent window
		final Rectangle shellBounds = getParent().getBounds();
		final Point dialogSize = shlFuelCurve.getSize();

		shlFuelCurve.setLocation(shellBounds.x + ((shellBounds.width - dialogSize.x) / 2), shellBounds.y + ((shellBounds.height - dialogSize.y) / 2));

		while (!shlFuelCurve.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return this.attributes;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param hidingTopPart
	 */
	private void createContents(final String state, final boolean hidingTopPart) {
		shlFuelCurve = new Shell(getParent(), getStyle());
		shlFuelCurve.setSize(382, 507);
		shlFuelCurve.setText(state + (hidingTopPart ? " Fuel Curve" : " Vessel State Attributes")

		);
		shlFuelCurve.setLayout(new GridLayout(1, false));

		final Group grpIdleBoiloff = new Group(shlFuelCurve, SWT.NONE);
		grpIdleBoiloff.setLayout(new GridLayout(3, false));
		grpIdleBoiloff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		if (hidingTopPart) {
			grpIdleBoiloff.setVisible(false);
			((GridData) grpIdleBoiloff.getLayoutData()).exclude = true;
		}
		grpIdleBoiloff.setText("Idle Consumption and NBO");

		final Label lblActiveNboRate = new Label(grpIdleBoiloff, SWT.NONE);
		lblActiveNboRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblActiveNboRate.setText("Active NBO Rate:");

		final Spinner nboRateSpinner = new Spinner(grpIdleBoiloff, SWT.BORDER);
		nboRateSpinner.setIncrement(0);
		nboRateSpinner.setMaximum(100000);
		nboRateSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				attributes.setNboRate((int) (nboRateSpinner.getSelection() * Math.pow(10, -nboRateSpinner.getDigits())));
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});
		nboRateSpinner.setDigits(2);

		final Label lblMday = new Label(grpIdleBoiloff, SWT.NONE);
		lblMday.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblMday.setText("M3/day");

		final Label lblIdleNboRate = new Label(grpIdleBoiloff, SWT.NONE);
		lblIdleNboRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIdleNboRate.setText("Idle NBO Rate:");

		final Spinner idleNboRatespinner = new Spinner(grpIdleBoiloff, SWT.BORDER);
		idleNboRatespinner.setIncrement(0);
		idleNboRatespinner.setMaximum(100000);
		idleNboRatespinner.setDigits(2);
		idleNboRatespinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				attributes.setIdleNBORate( (int)(idleNboRatespinner.getSelection() * Math.pow(10, -idleNboRatespinner.getDigits())));
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});

		final Label lblMday_1 = new Label(grpIdleBoiloff, SWT.NONE);
		lblMday_1.setText("M3/day");

		final Label lblIdleFuelConsumption = new Label(grpIdleBoiloff, SWT.NONE);
		lblIdleFuelConsumption.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIdleFuelConsumption.setText("Idle Fuel Consumption:");

		final Spinner idleConsumptionSpinner = new Spinner(grpIdleBoiloff, SWT.BORDER);
		idleConsumptionSpinner.setIncrement(0);
		idleConsumptionSpinner.setMaximum(100000);
		idleConsumptionSpinner.setDigits(2);
		idleConsumptionSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				attributes.setIdleBaseRate((int) (idleConsumptionSpinner.getSelection() * Math.pow(10, -idleConsumptionSpinner.getDigits())));
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});

		final Label lblMtday = new Label(grpIdleBoiloff, SWT.NONE);
		lblMtday.setText("MT/day");

		final Group grpSpeedconsumptionCurve = new Group(shlFuelCurve, SWT.NONE);
		grpSpeedconsumptionCurve.setLayout(new GridLayout(1, false));
		grpSpeedconsumptionCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSpeedconsumptionCurve.setText("Speed/Consumption Curve");

		final TableViewer tableViewer = new TableViewer(grpSpeedconsumptionCurve, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				btnRemoveLine.setEnabled(!event.getSelection().isEmpty());
			}
		});

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final FuelConsumption[] lines = ((VesselStateAttributes) inputElement).getFuelConsumption().toArray(new FuelConsumption[] {});

				Arrays.sort(lines, new Comparator<FuelConsumption>() {
					@Override
					public int compare(final FuelConsumption first, final FuelConsumption second) {
						return ((Double) first.getSpeed()).compareTo(second.getSpeed());
					}
				});

				return lines;
			}
		});

		fuelCurveTable = tableViewer.getTable();
		fuelCurveTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		fuelCurveTable.setLinesVisible(true);
		fuelCurveTable.setHeaderVisible(true);

		fuelCurveTable.setLayout(new TableLayout());

		final TableViewerColumn speedColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		speedColumn.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final SpinnerCellEditor editor = new SpinnerCellEditor(tableViewer.getTable());
				editor.setDigits(2);
				editor.setMinimumValue(0);
				editor.setMaximumValue(100000);
				return editor;
			}

			@Override
			protected Object getValue(final Object element) {
				return element == null ? null : (Double) ((FuelConsumption) element).getSpeed();
			}

			@Override
			protected void setValue(final Object element, final Object value) {
				((FuelConsumption) element).setSpeed((Double) value);
				tableViewer.refresh();
			}
		});
		speedColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				return element == null ? "" : ((FuelConsumption) element).getSpeed() + "";
			}
		});

		final TableColumn tblclmnSpeedknots = speedColumn.getColumn();
		tblclmnSpeedknots.setWidth(167);
		tblclmnSpeedknots.setText("Speed (knots)");

		final TableViewerColumn fuelConsumptionColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		fuelConsumptionColumn.setEditingSupport(new EditingSupport(tableViewer) {
			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final SpinnerCellEditor editor = new SpinnerCellEditor(tableViewer.getTable());
				editor.setDigits(2);
				editor.setMinimumValue(0);
				editor.setMaximumValue(100000);
				return editor;
			}

			@Override
			protected Object getValue(final Object element) {
				return element == null ? null : (Double) ((FuelConsumption) element).getConsumption();
			}

			@Override
			protected void setValue(final Object element, final Object value) {
				((FuelConsumption) element).setConsumption((Float) value);
				tableViewer.refresh();
			}
		});
		fuelConsumptionColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public Image getImage(final Object element) {
				return null;
			}

			@Override
			public String getText(final Object element) {
				return element == null ? "" : ((FuelConsumption) element).getConsumption() + "";
			}
		});
		final TableColumn tblclmnFuelConsumptionmt = fuelConsumptionColumn.getColumn();
		tblclmnFuelConsumptionmt.setWidth(180);
		tblclmnFuelConsumptionmt.setText("Fuel Consumption (MT)");

		final Composite composite = new Composite(grpSpeedconsumptionCurve, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));

		btnRemoveLine = new Button(composite, SWT.FLAT);
		btnRemoveLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty()) {
					return; // should never happen
				}
				if (sel instanceof IStructuredSelection) {
					final FuelConsumption line = (FuelConsumption) ((IStructuredSelection) sel).getFirstElement();

					attributes.getFuelConsumption().remove(line);
					tableViewer.refresh();
				}
			}
		});
		btnRemoveLine.setEnabled(false);
		btnRemoveLine.setText("-");

		final Button btnAddLine = new Button(composite, SWT.FLAT);
		btnAddLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final FuelConsumption line = FleetPackage.eINSTANCE.getFleetFactory().createFuelConsumption();
				line.setConsumption(0);
				line.setSpeed(0);
				attributes.getFuelConsumption().add(line);
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(line), true);
			}
		});
		btnAddLine.setText("+");

		final Composite graphComposite = new Composite(grpSpeedconsumptionCurve, SWT.NONE);
		graphComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 0, 1));
		graphComposite.setLayout(new FillLayout());
		final Chart chart = new Chart(graphComposite, SWT.NONE);

		chart.getTitle().setVisible(false);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(false);
		chart.getLegend().setVisible(false);

		final ISeries series = chart.getSeriesSet().createSeries(SeriesType.LINE, "Fuel Consumption");

		final EContentAdapter chartUpdater = new EContentAdapter() {
			{
				redraw();
			}

			@Override
			public void notifyChanged(final Notification msg) {
				super.notifyChanged(msg);// hack
				redraw();
			}

			public void redraw() {
				final EList<FuelConsumption> curve = attributes.getFuelConsumption();

				final FuelConsumption[] lines = curve.toArray(new FuelConsumption[] {});

				Arrays.sort(lines, new Comparator<FuelConsumption>() {
					@Override
					public int compare(final FuelConsumption first, final FuelConsumption second) {
						return ((Double) first.getSpeed()).compareTo(second.getSpeed());
					}
				});

				final double[] speedValues = new double[curve.size()];
				final double[] consumptionValues = new double[curve.size()];

				double minSpeed = Double.MAX_VALUE;
				double minConsumption = Double.MAX_VALUE;
				double maxSpeed = -Double.MAX_VALUE;
				double maxConsumption = -Double.MAX_VALUE;

				for (int i = 0; i < lines.length; i++) {
					speedValues[i] = lines[i].getSpeed();
					consumptionValues[i] = lines[i].getConsumption();

					minSpeed = Math.min(speedValues[i], minSpeed);
					maxSpeed = Math.max(speedValues[i], maxSpeed);
					minConsumption = Math.min(consumptionValues[i], minConsumption);
					maxConsumption = Math.max(consumptionValues[i], maxConsumption);

				}

				series.setXSeries(speedValues);
				series.setYSeries(consumptionValues);
				if (minSpeed == Double.MAX_VALUE) {
					minSpeed = 0;
				}
				if (maxSpeed == -Double.MAX_VALUE) {
					maxSpeed = 19.5;
				}
				if (minConsumption == Double.MAX_VALUE) {
					minConsumption = 0;
				}
				if (maxConsumption == -Double.MAX_VALUE) {
					maxConsumption = 100;
				}
				chart.getAxisSet().getXAxis(0).setRange(new Range(minSpeed, maxSpeed));
				chart.getAxisSet().getYAxis(0).setRange(new Range(minConsumption, maxConsumption));
				chart.redraw();
			}
		};

		attributes.eAdapters().add(chartUpdater);

		// remove updater when done
		shlFuelCurve.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				if (attributes != null) {
					attributes.eAdapters().remove(chartUpdater);
				}
			}
		});

		final Composite buttonsComposite = new Composite(shlFuelCurve, SWT.NONE);
		buttonsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		buttonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		final Button btnCancel = new Button(buttonsComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				attributes = null; // cancel by returning null.
				shlFuelCurve.close();
			}
		});
		btnCancel.setText("Cancel");

		final Button btnOk = new Button(buttonsComposite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				shlFuelCurve.close();
			}
		});
		btnOk.setText("OK");
		shlFuelCurve.setDefaultButton(btnOk);

		// set values
		tableViewer.setInput(attributes);

		idleConsumptionSpinner.setSelection((int) (attributes.getIdleBaseRate() * Math.pow(10, idleConsumptionSpinner.getDigits())));

		idleNboRatespinner.setSelection((int) (attributes.getIdleNBORate() * Math.pow(10, idleNboRatespinner.getDigits())));

		nboRateSpinner.setSelection((int) (attributes.getNboRate() * Math.pow(10, nboRateSpinner.getDigits())));
	}
}
