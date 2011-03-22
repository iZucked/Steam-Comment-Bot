/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduleview.views;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.ganttchart.AbstractSettings;
import org.eclipse.nebula.widgets.ganttchart.GanttFlags;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleModel;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.PackAction;
import com.mmxlabs.ganttviewer.ZoomInAction;
import com.mmxlabs.ganttviewer.ZoomOutAction;
import com.mmxlabs.scheduleview.Activator;
import com.mmxlabs.scheduleview.views.EMFScheduleLabelProvider.Mode;

public class SchedulerView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.scheduleview.views.SchedulerView";

	private GanttChartViewer viewer;
	private Action zoomInAction;
	private Action zoomOutAction;

	private Action toggleColourSchemeAction;

	private ISelectionListener selectionListener;

	private PackAction packAction;

	/**
	 * The constructor.
	 */
	public SchedulerView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// Gantt Chart settings object
		final ISettings settings = new AbstractSettings() {
			@Override
			public boolean enableResizing() {
				return false;
			}

			@Override
			public boolean showPlannedDates() {
				return false;
			}

			@Override
			public String getTextDisplayFormat() {
				return "#name#";
			}

			@Override
			public int getSectionTextSpacer() {
				return 0;
			}

			@Override
			public int getMinimumSectionHeight() {
				return 10;
			}
		};

		viewer = new GanttChartViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | GanttFlags.H_SCROLL_FIXED_RANGE, settings);
		// viewer.setContentProvider(new AnnotatedScheduleContentProvider());
		// viewer.setLabelProvider(new AnnotatedSequenceLabelProvider());

		viewer.setContentProvider(new EMFScheduleContentProvider());
		viewer.setLabelProvider(new EMFScheduleLabelProvider());

		// viewer.setSorter(new NameSorter());

		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(), "com.mmxlabs.scheduleview.viewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		/*
		 * Add selection listener. may need tidying up.
		 */

		selectionListener = new ISelectionListener() {
			private boolean trySelectObject(final Object o) {
				if (o instanceof Schedule) {
					setInput((Schedule) o);
					return true;
				} else if (o instanceof Scenario) {
					final Scenario scenario = (Scenario) o;
					final ScheduleModel scheduleModel = scenario
							.getScheduleModel();
					final int scheduleCount;
					if (scheduleModel != null
							&& (scheduleCount = scheduleModel.getSchedules()
									.size()) > 0) {
						setInput(scheduleModel.getSchedules().get(
								scheduleCount - 1));
						return true;
					}
				} else if (o instanceof Resource
						&& ((Resource) o).getContents().size() > 0) {
					return trySelectObject(((Resource) o).getContents().get(0));
				}
				return false;
			}

			@Override
			public void selectionChanged(final IWorkbenchPart part,
					final ISelection selection) {

				// Filter out non-editor selections - Unfortunately the
				// addSelectionLister part ID filters do not work with editors
				if (part instanceof IEditorPart
						|| (part!= null && part.getSite().getId()
								.equals("com.mmxlabs.rcp.navigator"))) {

					// if selection instanceof etc.
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection sel = (IStructuredSelection) selection;
						if (sel.isEmpty()) {
							setInput(null);
						} else {
							final Iterator<?> iter = sel.iterator();
							while (iter.hasNext()) {
								final Object o = iter.next();
								if (trySelectObject(o)) {
									return;
								} else if (o instanceof IAdaptable) {
									final Object adapter = ((IAdaptable) o)
											.getAdapter(Schedule.class);
									if (adapter != null) {
										setInput(((Schedule) adapter));
									}
								}
							}
						}
					}
				}
			}
		};
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(selectionListener);

		// getSite().getPage().addSelectionListener(JobManagerView.ID,
		// new ISelectionListener() {
		//
		// @Override
		// public void selectionChanged(IWorkbenchPart part,
		// ISelection selection) {
		//
		// selectedJob = null;
		// IStructuredSelection ss = (IStructuredSelection) selection;
		// if (ss.isEmpty() == false) {
		// selectedJob = (IManagedJob) ss.iterator().next();
		// setInput(selectedJob.getSchedule());
		// } else {
		// setInput(null);
		// }
		// refresh();
		// }
		// });

		// jobListener = new ManagedJobListenerNotifier() {
		//
		// @Override
		// public void jobNotified(IManagedJob job) {
		//
		// if (selectedJobs.contains(job)) {
		// setInput(job.getSchedule());
		// refresh();
		// }
		// }
		// };
		//
		// jobManagerListener = new IJobManagerListener() {
		//
		// @Override
		// public void jobRemoved(IJobManager jobManager, IManagedJob job) {
		// job.removeManagedJobListener(jobListener);
		// }
		//
		// @Override
		// public void jobAdded(IJobManager jobManager, IManagedJob job) {
		// job.addManagedJobListener(jobListener);
		// }
		//
		// @Override
		// public void jobSelected(IJobManager jobManager, IManagedJob job) {
		// selectedJobs.add(job);
		// if (selectedJobs.isEmpty()) {
		// setInput(null);
		// } else {
		// setInput(selectedJobs.get(0).getSchedule());
		// }
		// }
		//
		// @Override
		// public void jobDeselected(IJobManager jobManager, IManagedJob job) {
		// selectedJobs.remove(job);
		// if (selectedJobs.isEmpty()) {
		// setInput(null);
		// } else {
		// setInput(selectedJobs.get(0).getSchedule());
		// }
		//
		// }
		// };
		//
		// Activator.getDefault().getJobManager()
		// .addJobManagerListener(jobManagerListener);

		getSite().setSelectionProvider(viewer);

		// Update view from current selection
		final ISelectionProvider selectionProvider = getSite().getSelectionProvider();
		if (selectionProvider != null) {
			selectionListener.selectionChanged(null, selectionProvider.getSelection());
		} else {
			// No current provider? Look at the scenario navigator
			// TODO: Ensure this is kept in sync
			final ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection("com.mmxlabs.rcp.navigator");
			selectionListener.selectionChanged(null, selection);
		}
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(selectionListener);

		super.dispose();
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				SchedulerView.this.fillContextMenu(manager);
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
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
	}

	private void fillContextMenu(final IMenuManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		manager.add(toggleColourSchemeAction);
		manager.add(packAction);
	}

	private void makeActions() {

		zoomInAction = new ZoomInAction(viewer.getGanttChart());
		zoomInAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/clcl16/zoomin_nav.gif"));
		zoomInAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("icons/dlcl16/zoomin_nav.gif"));

		zoomOutAction = new ZoomOutAction(viewer.getGanttChart());
		zoomOutAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/clcl16/zoomout_nav.gif"));
		zoomOutAction.setDisabledImageDescriptor(Activator
				.getImageDescriptor("icons/dlcl16/zoomout_nav.gif"));

		toggleColourSchemeAction = new Action() {
			EMFScheduleLabelProvider.Mode mode = EMFScheduleLabelProvider.Mode.VesselState;

			public void run() {

				final EMFScheduleLabelProvider lp = (EMFScheduleLabelProvider) (viewer
						.getLabelProvider());

				final int nextMode = (mode.ordinal() + 1)
						% Mode.values().length;
				mode = EMFScheduleLabelProvider.Mode.values()[nextMode];
				lp.setMode(mode);

				viewer.setInput(viewer.getInput());

				redraw();
			};
		};
		toggleColourSchemeAction.setText("Switch Colour Scheme");
		
		packAction = new PackAction(viewer.getGanttChart());
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void redraw() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {

					viewer.setInput(viewer.getInput());
				}
			}
		});

	}

	public void refresh() {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {

					viewer.refresh();
				}
			}
		});
	}

	public void setInput(final Object input) {
		getSite().getShell().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!viewer.getControl().isDisposed()) {
					viewer.setInput(input);
				}
			}
		});
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") final Class key) {
		// Hook up our property sheet page
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else {
			return super.getAdapter(key);
		}
	}

	/**
	 * Create a new {@link PropertySheetPage} instance hooked up to the default
	 * EMF adapter factory.
	 * 
	 * @return
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final PropertySheetPage propertySheetPage = new PropertySheetPage();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory();

		// TODO: Add in ScenarioEMF Item Provider

		adapterFactory
				.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		propertySheetPage
				.setPropertySourceProvider(new AdapterFactoryContentProvider(
						adapterFactory));

		return propertySheetPage;
	}
}