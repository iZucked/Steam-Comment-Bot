/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
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
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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

	private SelectedScenariosService selectedScenariosService;

	protected static final String TREE_DATA_KEY = "THVTreeData";

	private TreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTreeAction;

	static final DecimalFormat myFormat = new DecimalFormat("$###,###,###");

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioInstance pinned, final Collection<ScenarioInstance> others, final boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					int numberOfSchedules = others.size() + (pinned == null ? 0 : 1);
					List<ScenarioInstance> instances = new LinkedList<>(others);
					if (pinned != null) {
						instances.add(0, pinned);
					}

					final TreeData dummy = new TreeData("");
					if (instances.size() == 1) {
						for (final ScenarioInstance other : instances) {
							LNGScenarioModel instance = (LNGScenarioModel) other.getInstance();
							if (instance != null) {
								final Schedule schedule = ScenarioModelUtil.findSchedule(instance);
								if (schedule != null) {
									dummy.addChild(createCostsTreeData(schedule));
									break;
								}
							}
						}
					} else {

						for (final ScenarioInstance other : instances) {
							LNGScenarioModel instance = (LNGScenarioModel) other.getInstance();
							if (instance != null) {
								final Schedule schedule = ScenarioModelUtil.findSchedule(instance);
								if (schedule != null) {
									final String scheduleName = other.getName();

									// final String scheduleName = schedule.getName();
									// don't sum costs and profits, because it's meaningless
									// (profits already include costs)
									final TreeData group = new TreeData(scheduleName, true);
									group.addChild(createCostsTreeData(schedule));
									// group.addChild(createProfitTreeData(schedule));
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
		final List<TreeData> children = new ArrayList<TreeData>();
		private TreeData parent;
		private String label = "";

		public TreeData(final String name, final boolean nonValued) {
			this.name = name;
			this.nonValued = nonValued;
			this.cost = 0;
		}

		public TreeData(final String name, final String misc) {
			this(name, true);
			this.label = misc;
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

	// private TreeData createTreeData(final Collection<Schedule> schedules) {
	// final TreeData dummy = new TreeData("");
	//
	// if (schedules.size() == 1) {
	// final Schedule schedule = schedules.iterator().next();
	// dummy.addChild(createCostsTreeData(schedule));
	// // dummy.addChild(createProfitTreeData(schedule));
	// } else {
	// for (final Schedule schedule : schedules) {
	// final String scheduleName = object.getScenarioInstance(schedule).getName();
	// // final String scheduleName = schedule.getName();
	// // don't sum costs and profits, because it's meaningless
	// // (profits already include costs)
	// final TreeData group = new TreeData(scheduleName, true);
	// group.addChild(createCostsTreeData(schedule));
	// // group.addChild(createProfitTreeData(schedule));
	// dummy.addChild(group);
	// }
	// }
	// return dummy;
	// }

	// private TreeData createProfitTreeData(final Schedule schedule) {
	// final TreeData top = new TreeData("Total Profit");
	// final Map<Entity, TreeData> byEntity = new HashMap<Entity, TreeData>();
	// if (schedule != null) {
	// for (final BookedRevenue revenue : schedule.getRevenue()) {
	// final Entity e = revenue.getEntity();
	// if ((e == null) || !(e instanceof GroupEntity)) {
	// continue;
	// }
	//
	// TreeData td = byEntity.get(e);
	// if (td == null) {
	// td = new TreeData(e.getName());
	// top.addChild(td);
	// byEntity.put(e, td);
	// }
	//
	// final TreeData rd = new TreeData(revenue.getName(), revenue.getValue());
	// td.addChild(rd);
	// for (final Detail d : revenue.getDetails().getChildren()) {
	// rd.addChild(createDetailTreeData(d));
	// }
	//
	// // for (final LineItem item : revenue.getLineItems()) {
	// // final TreeData li = new TreeData(item.getName(),
	// // item.getValue());
	// // rd.addChild(li);
	// // }
	//
	// // rd.addChild(new TreeData("Tax", -revenue.getTaxCost()));
	// // assert (rd.getCost() == revenue.getTaxedValue());
	// // TODO this does not take account of ownership proportion
	// }
	// }
	// return top;
	// }

	// /**
	// * @param details
	// * @return
	// */
	// private TreeData createDetailTreeData(final Detail details) {
	// final TreeData top = new TreeData(details.getName(), details.getValue());
	// for (final Detail d : details.getChildren()) {
	// top.addChild(createDetailTreeData(d));
	// }
	// return top;
	// }

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

		final Map<Fuel, TreeData> fleetFuelUsages = new HashMap<Fuel, TreeData>();
		final Map<Fuel, TreeData> spotFuelUsages = new HashMap<Fuel, TreeData>();

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
			// final AllocatedVessel av = seq.getVessel();
			final Map<Fuel, TreeData> vesselFuelUsage = new HashMap<Fuel, TreeData>();

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
						final TreeData eventUsage = new TreeData(name, fq.getCost());
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
			// final AllocatedVessel av = seq.getVessel();
			final TreeData thisVessel = new TreeData(seq.getName());
			(!seq.isSpotVessel() ? fleetCanalCosts : spotCanalCosts).addChild(thisVessel);

			for (final Event event : seq.getEvents()) {
				if (event instanceof Journey) {
					final Journey j = (Journey) event;
					if (j.getToll() > 0) {
						final TreeData thisLeg = new TreeData(
								(j.isLaden() ? "Laden" : "Ballast") + " voyage from " + j.getPort().getName() + " to " + j.getDestination().getName() + " via " + j.getRoute(), j.getToll());
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

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);

		this.viewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

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
		//
		// getSite().getPage().addSelectionListener("com.mmxlabs.rcp.navigator",
		// this);
		//
		// final ISelection selection = getSite().getWorkbenchWindow()
		// .getSelectionService()
		// .getSelection("com.mmxlabs.rcp.navigator");
		//
		// selectionChanged(null, selection);
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
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				TotalsHierarchyView.this.fillContextMenu(manager);
			}
		});
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
