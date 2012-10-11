/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTableColumnsAction;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;

public class PerVesselReportView extends ViewPart {

	private TableViewer tableViewer;
	private ScenarioViewerSynchronizer synchronizer;
	private Action packColumnsAction;
	private Action copyTableAction;

	private IScenarioViewerSynchronizerOutput lastInput;
	private TableViewerColumn scheduleColumnViewer;

	@Override
	public void createPartControl(final Composite parent) {
		tableViewer = new TableViewer(parent);

		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		scheduleColumnViewer = new TableViewerColumn(tableViewer, SWT.NONE);
		scheduleColumnViewer.getColumn().setText("Scenario");
		scheduleColumnViewer.getColumn().pack();
		scheduleColumnViewer.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return lastInput.getScenarioInstance(element).getName();
			}
		});

		final TableViewerColumn name = new TableViewerColumn(tableViewer, SWT.NONE);
		name.getColumn().setText("Vessel");
		name.getColumn().pack();
		name.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((VesselCosts) element).name;
			}
		});

		final TableViewerColumn hire = new TableViewerColumn(tableViewer, SWT.NONE);
		hire.getColumn().setText("Hire Cost");
		hire.getColumn().pack();
		hire.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return String.format("%,d", ((VesselCosts) element).hireCost);
			}
		});

		final TableViewerColumn canal = new TableViewerColumn(tableViewer, SWT.NONE);
		canal.getColumn().setText("Canals");
		canal.getColumn().pack();
		canal.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return String.format("%,d", ((VesselCosts) element).canalCost);
			}
		});

		final TableViewerColumn port = new TableViewerColumn(tableViewer, SWT.NONE);
		port.getColumn().setText("Port Costs");
		port.getColumn().pack();
		port.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return String.format("%,d", ((VesselCosts) element).portCost);
			}
		});

		for (final Fuel fuel : Fuel.values()) {
			final TableViewerColumn f = new TableViewerColumn(tableViewer, SWT.NONE);
			f.getColumn().setText(fuel.name());
			f.getColumn().pack();
			f.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					final Integer i = ((VesselCosts) element).fuelCosts.get(fuel);
					if (i == null)
						return "";
					return String.format("%,d", i);
				}
			});
		}

		final TableViewerColumn utilisation = new TableViewerColumn(tableViewer, SWT.NONE);
		utilisation.getColumn().setText("Utilisation");
		utilisation.getColumn().pack();
		utilisation.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return String.format("%.2f%%", ((VesselCosts) element).utilisation * 100);
			}
		});

		makeActions();
		fillLocalToolBar(getViewSite().getActionBars().getToolBarManager());

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				if (newInput instanceof IScenarioViewerSynchronizerOutput) {
					final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
					if (scheduleColumnViewer != null) {
						if (synchronizerOutput.getRootObjects().size() > 1) {

							scheduleColumnViewer.getColumn().setResizable(true);
							scheduleColumnViewer.getColumn().pack();
						} else {
							scheduleColumnViewer.getColumn().setWidth(0);
							scheduleColumnViewer.getColumn().setResizable(false);
						}
					}
				}
			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				lastInput = null;
				if (inputElement instanceof IScenarioViewerSynchronizerOutput) {
					lastInput = (IScenarioViewerSynchronizerOutput) inputElement;
					return ((IScenarioViewerSynchronizerOutput) inputElement).getCollectedElements().toArray();
				}
				return new Object[0];
			}
		});

		synchronizer = ScenarioViewerSynchronizer.registerView(tableViewer, new ScheduleElementCollector() {

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule) {
				final List<VesselCosts> r = new ArrayList<VesselCosts>();
				for (final Sequence seq : schedule.getSequences()) {
					r.add(new VesselCosts(seq));
				}
				return r;
			}
		});
	}

	@Override
	public void dispose() {
		ScenarioViewerSynchronizer.deregisterView(synchronizer);
		super.dispose();
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	private class VesselCosts {
		public String name;
		public int canalCost;
		public int hireCost;
		public int portCost;
		public double utilisation;
		public Map<Fuel, Integer> fuelCosts = new HashMap<Fuel, Integer>();

		public VesselCosts(final Sequence sequence) {
			name = sequence.getName();
			int activeDuration = 0;
			int idleDuration = 0;
			for (final Event event : sequence.getEvents()) {
				hireCost += event.getHireCost();
				if (event instanceof FuelUsage) {
					for (final FuelQuantity quantity : ((FuelUsage) event).getFuels()) {
						final Integer i = fuelCosts.get(quantity.getFuel());
						fuelCosts.put(quantity.getFuel(), (i == null ? 0 : i.intValue()) + quantity.getCost());
					}
				}
				if (event instanceof Journey) {
					canalCost += ((Journey) event).getToll();
				}
				if (event instanceof PortVisit) {
					portCost += ((PortVisit) event).getPortCost();
				}

				if (event instanceof Idle) {
					idleDuration += event.getDuration();
				} else {
					activeDuration += event.getDuration();
				}
			}
			final int totalDuration = idleDuration + activeDuration;
			utilisation = totalDuration == 0 ? 0 : ((activeDuration) / (double) totalDuration);
		}
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		// Other plug-ins can contribute there actions here
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTableColumnsAction(tableViewer, true);
		copyTableAction = new CopyTableToClipboardAction(tableViewer.getTable());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
	}
}
