/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;

import com.mmxlabs.demo.reports.ScheduleAdapter;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * A view which displays the cost breakdown as a hierarchy, thus
 * 
 * <ol>
 * <li>Fuel Cost - LNG : $11234
 * <ol>
 * <li>Fleet Vessels : $10000
 * <ol>
 * <li>Vessel 1 : $whatever</li>
 * <li>...</li>
 * </ol>
 * 
 * </li>
 * <li>Spot Charter Vessels : $1234</li>
 * </ol>
 * </li>
 * <li>etc</li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public class TotalsHierarchyView extends ViewPart implements ISelectionListener {
	public static final String ID = "com.mmxlabs.demo.reports.views.TotalsHierarchyView";

	protected static final String TREE_DATA_KEY = "THVTreeData";

	private TreeViewer viewer;

	@Override
	public void selectionChanged(final IWorkbenchPart part,
			final ISelection selection) {

		final List<Schedule> schedules = ScheduleAdapter.getSchedules(selection);
		if (!schedules.isEmpty()) {
			setSelectedSchedule(schedules.get(0));
		}
	}

	private class TreeData {
		final String name;
		final long cost;
		final List<TreeData> children = new ArrayList<TreeData>();
		private TreeData parent;

		public TreeData(final String name) {
			this.name = name;
			cost = 0;
		}

		public TreeData(final String name, final long cost) {
			this.name = name;
			this.cost = cost;
		}

		public String getName() {
			return name;
		}

		public long getCost() {
			long accumulator = cost;
			for (final TreeData d : children)
				accumulator += d.getCost();
			return accumulator;
		}

		public void addChild(final TreeData child) {
			children.add(child);
			child.setParent(this);
		}

		public int getChildCount() {
			return children.size();
		}

		public TreeData getChild(final int index) {
			return children.get(index);
		}

		public TreeData getParent() {
			return parent;
		}

		public void setParent(final TreeData d) {
			parent = d;
		}
	}

	/**
	 * Update the contents of the tree control for this schedule
	 * 
	 * @param schedule
	 */
	private void setSelectedSchedule(final Schedule schedule) {
		System.err.println("Set schedule " + schedule);
		final TreeData dummy = new TreeData("");
		dummy.addChild(createTreeData(schedule));
		viewer.setInput(dummy);
	}

	/**
	 * Extract cost information from the schedule and put it in the treedata
	 * 
	 * @param schedule
	 * @return
	 */
	private TreeData createTreeData(final Schedule schedule) {
		final TreeData top = new TreeData("Total");

		if (schedule == null)
			return top;

		final EnumMap<FuelType, TreeData> fleetFuelUsages = new EnumMap<FuelType, TreeData>(
				FuelType.class);
		final EnumMap<FuelType, TreeData> spotFuelUsages = new EnumMap<FuelType, TreeData>(
				FuelType.class);

		final TreeData fleetLNG = new TreeData("Fleet Vessels");
		final TreeData spotLNG = new TreeData("Spot Vessels");
		
		final TreeData lng = new TreeData("LNG");
		
		top.addChild(lng);
		lng.addChild(fleetLNG);
		lng.addChild(spotLNG);
		
		// First do fuel costs
		for (final FuelType t : FuelType.values()) {
			if (t.equals(FuelType.FBO) || t.equals(FuelType.NBO)) {
				final TreeData thisFuelFleet = new TreeData(t.getName());
				final TreeData thisFuelSpot = new TreeData(t.getName());
				
				fleetLNG.addChild(thisFuelFleet);
				spotLNG.addChild(thisFuelSpot);
				
				fleetFuelUsages.put(t, thisFuelFleet);
				spotFuelUsages.put(t, thisFuelSpot);
			} else {
				final TreeData thisFuel = new TreeData(t.getName());
				top.addChild(thisFuel);
				final TreeData fleetUsage = new TreeData("Fleet Vessels");
				final TreeData spotUsage = new TreeData("Spot Vessels");
				thisFuel.addChild(fleetUsage);
				thisFuel.addChild(spotUsage);

				fleetFuelUsages.put(t, fleetUsage);
				spotFuelUsages.put(t, spotUsage);
			}
		}

		for (final Sequence seq : schedule.getSequences()) {
			final AllocatedVessel av = seq.getVessel();
			final EnumMap<FuelType, TreeData> vesselFuelUsage = new EnumMap<FuelType, TreeData>(
					FuelType.class);

			final EnumMap<FuelType, TreeData> fuelUsages = (av instanceof FleetVessel) ? fleetFuelUsages
					: spotFuelUsages;

			for (final FuelType t : FuelType.values()) {
				final TreeData thisFuelAndVessel = new TreeData(av.getName());
				vesselFuelUsage.put(t, thisFuelAndVessel);
				fuelUsages.get(t).addChild(thisFuelAndVessel);
			}

			for (final ScheduledEvent evt : seq.getEvents()) {
				if (evt instanceof FuelMixture) {
					final String name;
					if (evt instanceof Journey) {
						final Journey j = (Journey) evt;
						name = j.getFromPort().getName() + " to "
								+ j.getToPort().getName();
					} else if (evt instanceof PortVisit) {
						name = ((PortVisit) evt).getDisplayTypeName() + " "
								+ ((PortVisit) evt).getId();
					} else {
						name = "Unknown";
					}
					final FuelMixture mix = (FuelMixture) evt;
					for (final FuelQuantity fq : mix.getFuelUsage()) {
						if (fq.getTotalPrice() == 0)
							continue;
						final TreeData eventUsage = new TreeData(name,
								fq.getTotalPrice());
						vesselFuelUsage.get(fq.getFuelType()).addChild(
								eventUsage);
					}
				}
			}
		}

		final TreeData canalCosts = new TreeData("Canal Fees");

		top.addChild(canalCosts);

		final TreeData fleetCanalCosts = new TreeData("Fleet Vessels");
		final TreeData spotCanalCosts = new TreeData("Spot Vessels");

		canalCosts.addChild(fleetCanalCosts);
		canalCosts.addChild(spotCanalCosts);

		// Now do canal costs
		for (final Sequence seq : schedule.getSequences()) {
			final AllocatedVessel av = seq.getVessel();
			final TreeData thisVessel = new TreeData(av.getName());
			(av instanceof FleetVessel ? fleetCanalCosts : spotCanalCosts)
					.addChild(thisVessel);

			for (final ScheduledEvent event : seq.getEvents()) {
				if (event instanceof Journey) {
					final Journey j = (Journey) event;
					if (j.getRouteCost() > 0) {
						final TreeData thisLeg = new TreeData(j
								.getVesselState().getName()
								+ " voyage from "
								+ j.getFromPort().getName()
								+ " to "
								+ j.getToPort().getName()
								+ " via "
								+ j.getRoute(), j.getRouteCost());
						thisVessel.addChild(thisLeg);
					}
				}
			}
		}

		// Finally do charter cost
		final TreeData charterCosts = new TreeData("Charter In");
		top.addChild(charterCosts);

		for (final Sequence seq : schedule.getSequences()) {
			final AllocatedVessel av = seq.getVessel();
			for (final ScheduleFitness sf : seq.getFitness()) {
				if (sf.getValue() > 0 && sf.getName()
						.equals(CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME)) {
					final TreeData thisVessel = new TreeData(av.getName(),
							sf.getValue());
					charterCosts.addChild(thisVessel);
				}
			}
		}

		return top;
	}

	@Override
	public void createPartControl(final Composite parent) {
		this.viewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);

		// autopack and set alternating colours - disabled because no longer
		// required
		// viewer.getTree().addListener(SWT.Expand, new Listener() {
		// @Override
		// public void handleEvent(final Event e) {
		// getSite().getShell().getDisplay().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// final Tree t = (Tree) e.widget;
		// for (final TreeColumn c : t.getColumns()) {
		// c.pack();
		// }
		//
		// // colour(t.getItem(0), true);
		// }
		// //
		// // private void colour(final TreeItem item, boolean oddness) {
		// // item.setBackground(oddness ?
		// Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_IN)
		// // }
		// });
		// }
		// });

		viewer.setContentProvider(new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public boolean hasChildren(final Object obj) {
				return ((TreeData) obj).getChildCount() > 0;
			}

			@Override
			public Object getParent(final Object obj) {
				return ((TreeData) obj).getParent();
			}

			@Override
			public Object[] getElements(final Object object) {

				if (object instanceof TreeData) {
					final TreeData data = (TreeData) object;
					// if (data.getParent() == null) {
					// return new Object[]{object};
					// } else {
					return getChildren(object); // argh.
					// }

				} else {
					return new Object[] {};
				}
			}

			@Override
			public Object[] getChildren(final Object element) {
				final TreeData data = (TreeData) element;
				return data.children.toArray();
			}
		});

		final TreeViewerColumn nameColumn = new TreeViewerColumn(viewer, 0);
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((TreeData) element).getName();
			}
		});

		nameColumn.getColumn().setText("Name");
		nameColumn.getColumn().pack();

		final TreeViewerColumn costColumn = new TreeViewerColumn(viewer, 0);
		costColumn.setLabelProvider(new ColumnLabelProvider() {
			final DecimalFormat myFormat = new DecimalFormat("$###,###,###");

			@Override
			public String getText(final Object element) {
				return myFormat.format(((TreeData) element).getCost());
			}
		});

		costColumn.getColumn().setText("Cost");
		costColumn.getColumn().pack();

		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);

		viewer.setInput(null);

		// add pack columns button
		getViewSite().getActionBars().getToolBarManager()
				.add(new PackTreeColumnsAction(viewer));

		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(this);
	}

	@Override
	public void setFocus() {

	}
}
