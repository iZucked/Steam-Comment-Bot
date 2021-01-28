/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;
import com.mmxlabs.scenario.service.ScenarioResult;

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
public class TotalsHierarchyView extends ViewPart {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.TotalsHierarchyView";

	private ScenarioComparisonService selectedScenariosService;

	protected static final String TREE_DATA_KEY = "THVTreeData";

	private TreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTreeAction;

	static final DecimalFormat myFormat = new DecimalFormat("$###,###,###");

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final Runnable r = new Runnable() {
				@Override
				public void run() {
					List<ScenarioResult> instances = selectedDataProvider.getAllScenarioResults();

					final TreeData dummy = new TreeData("");
					if (instances.size() == 1) {
						for (final ScenarioResult other : instances) {
							ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
							if (scheduleModel != null) {
								final Schedule schedule = scheduleModel.getSchedule();
								if (schedule != null) {
									dummy.addChild(createCostsTreeData(schedule));
									break;
								}
							}
						}
					} else {

						for (final ScenarioResult other : instances) {
							ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
							if (scheduleModel != null) {
								final Schedule schedule = scheduleModel.getSchedule();
								if (schedule != null) {
									final String scheduleName = other.getModelRecord().getName();

									// don't sum costs and profits, because it's meaningless
									// (profits already include costs)
									final TreeData group = new TreeData(scheduleName, true);
									group.addChild(createCostsTreeData(schedule));
									dummy.addChild(group);

								}
							}
						}
					}
					ViewerHelper.setInput(viewer, true, dummy);

					if (!dummy.children.isEmpty()) {
						if (packColumnsAction != null) {
							packColumnsAction.run();
						}
					}
				}
			};
			RunnerHelper.exec(r, block);
		}
	};

	private static class TreeData {

		final String name;
		final boolean nonValued;
		final long cost;
		final List<TreeData> children = new ArrayList<>();
		private TreeData parent;
		private String label = "";

		public TreeData(final String name, final boolean nonValued) {
			this.name = name;
			this.nonValued = nonValued;
			this.cost = 0;
		}

		public TreeData(final String name) {
			this.name = name;
			nonValued = false;
			cost = 0;
		}

		public TreeData(final String name, final long cost) {
			this.name = name;
			this.cost = cost;
			nonValued = false;
		}

		public String getName() {
			return name;
		}

		public long getCost() {
			if (nonValued) {
				return 0;
			}
			long accumulator = cost;
			for (final TreeData d : children) {
				accumulator += d.getCost();
			}
			return accumulator;
		}

		public void addChild(final TreeData child) {
			children.add(child);
			child.setParent(this);
		}

		public int getChildCount() {
			return children.size();
		}

		public TreeData getParent() {
			return parent;
		}

		public void setParent(final TreeData d) {
			parent = d;
		}

		public String renderCost() {
			if (nonValued) {
				return label;
			}
			return myFormat.format(getCost());
		}
	}

	/**
	 * Extract cost information from the schedule and put it in the treedata
	 * 
	 * @param schedule
	 * @return
	 */
	private TreeData createCostsTreeData(final Schedule schedule) {
		final TreeData top = new TreeData("Total Costs");

		if (schedule == null) {
			return top;
		}

		final Map<Fuel, TreeData> fleetFuelUsages = new EnumMap<>(Fuel.class);
		final Map<Fuel, TreeData> spotFuelUsages = new EnumMap<>(Fuel.class);

		final TreeData fleetLNG = new TreeData("Fleet Vessels");
		final TreeData spotLNG = new TreeData("Spot Vessels");

		final TreeData lng = new TreeData("LNG");

		top.addChild(lng);
		lng.addChild(fleetLNG);
		lng.addChild(spotLNG);

		// First do fuel costs
		for (final Fuel t : Fuel.values()) {
			if (t.equals(Fuel.FBO) || t.equals(Fuel.NBO)) {
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
			final Map<Fuel, TreeData> vesselFuelUsage = new EnumMap<>(Fuel.class);

			final Map<Fuel, TreeData> fuelUsages = seq.isSpotVessel() ? spotFuelUsages : fleetFuelUsages;

			for (final Fuel t : Fuel.values()) {
				final TreeData thisFuelAndVessel = new TreeData(seq.getName());
				vesselFuelUsage.put(t, thisFuelAndVessel);
				fuelUsages.get(t).addChild(thisFuelAndVessel);
			}

			for (final Event evt : seq.getEvents()) {
				if (evt instanceof FuelUsage) {
					final String name;
					if (evt instanceof Journey) {
						final Journey j = (Journey) evt;
						name = j.getPort().getName() + " to " + j.getDestination().getName();
					} else {
						name = evt.type() + " - " + evt.name();
					}
					final FuelUsage mix = (FuelUsage) evt;
					for (final FuelQuantity fq : mix.getFuels()) {
						if (fq.getCost() == 0) {
							continue;
						}
						final TreeData eventUsage = new TreeData(name, Math.round(fq.getCost()));
						vesselFuelUsage.get(fq.getFuel()).addChild(eventUsage);
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
			final TreeData thisVessel = new TreeData(seq.getName());
			(!seq.isSpotVessel() ? fleetCanalCosts : spotCanalCosts).addChild(thisVessel);

			for (final Event event : seq.getEvents()) {
				if (event instanceof Journey) {
					final Journey j = (Journey) event;
					if (j.getToll() > 0) {
						final TreeData thisLeg = new TreeData((j.isLaden() ? "Laden" : "Ballast") + " voyage from " + j.getPort().getName() + " to " + j.getDestination().getName() + " via "
								+ PortModelLabeller.getName(j.getRouteOption()), j.getToll());
						thisVessel.addChild(thisLeg);
					}
				}
			}
		}

		final TreeData portCosts = new TreeData("Port Costs");
		for (final Sequence seq : schedule.getSequences()) {
			for (final Event event : seq.getEvents()) {
				if (event instanceof PortVisit) {
					int cost = ((PortVisit) event).getPortCost();
					if (cost > 0) {
						final TreeData record = new TreeData(event.name(), cost);
						portCosts.addChild(record);
					}
				}
			}
		}
		if (portCosts.getChildCount() > 0) {
			top.addChild(portCosts);
		}

		// Finally do charter cost
		final TreeData charterCosts = new TreeData("Charter In");
		top.addChild(charterCosts);

		for (final Sequence seq : schedule.getSequences()) {
			if (seq.isFleetVessel())
				continue;
			long acc = 0;
			for (final Event e : seq.getEvents()) {
				acc += e.getCharterCost();
			}
			charterCosts.addChild(new TreeData(seq.getName(), acc));
		}

		return top;
	}

	@Override
	public void createPartControl(final Composite parent) {

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);

		this.viewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		viewer.setContentProvider(new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {
				// Nothing to do
			}

			@Override
			public void dispose() {
				// Nothing to do
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
					return getChildren(object); // argh.
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

			@Override
			public String getText(final Object element) {
				return ((TreeData) element).renderCost();

			}
		});

		costColumn.getColumn().setText("Cost");
		costColumn.getColumn().pack();

		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);

		viewer.setInput(null);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_CostBreakdown");
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(TotalsHierarchyView.this::fillContextMenu);
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
		manager.add(new Separator());
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTreeAction);
	}

	private void makeActions() {
		packColumnsAction = new PackTreeColumnsAction(viewer);
		copyTreeAction = new CopyTreeToClipboardAction(viewer.getTree());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTreeAction);
	}

	@Override
	public void dispose() {

		selectedScenariosService.removeListener(selectedScenariosServiceListener);

		super.dispose();
	}
}
