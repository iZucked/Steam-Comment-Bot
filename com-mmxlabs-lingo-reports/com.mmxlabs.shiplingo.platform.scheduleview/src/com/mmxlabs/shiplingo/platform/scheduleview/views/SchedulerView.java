/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Highlight_;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Show_Canals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.ganttchart.AbstractSettings;
import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.nebula.widgets.ganttchart.DefaultColorManager;
import org.eclipse.nebula.widgets.ganttchart.GanttFlags;
import org.eclipse.nebula.widgets.ganttchart.IColorManager;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.util.ConfigurationElementMemento;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.PackAction;
import com.mmxlabs.ganttviewer.SaveFullImageAction;
import com.mmxlabs.ganttviewer.ZoomInAction;
import com.mmxlabs.ganttviewer.ZoomOutAction;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.scheduleview.internal.Activator;
import com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ISchedulerViewColourSchemeExtension;

public class SchedulerView extends ViewPart implements ISelectionListener {

	private static final String SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION = "SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION";
	
//	public static final String = "";
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.scheduleview.views.SchedulerView";

	private GanttChartViewer viewer;
	private Action zoomInAction;
	private Action zoomOutAction;

	private Action colourSchemeAction;

	// private ISelectionListener selectionListener;

	private PackAction packAction;

	private SaveFullImageAction saveFullImageAction;

	private Action sortModeAction;

	private final ScenarioViewerComparator viewerComparator = new ScenarioViewerComparator();

	private ScenarioViewerSynchronizer jobManagerListener;

	private IMemento memento;
	private IMemento highlightMemento;
	
	@Inject
	private Iterable<ISchedulerViewColourSchemeExtension> colourSchemes;

	private HighlightAction highlightAction;


	/**
	 * The constructor.
	 */
	public SchedulerView() {
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;
		
		// check that it's the right memento - and non-null?...
		
		// set defaults from preference store for missing settings...
//		for (String key : memento.getAttributeKeys()) { // need list from Constants and then the right getString/Boolean call below...
//			if(memento.getString(key) == null){
//				memento.putString(key, Activator.getDefault().getPreferenceStore().getString(key));
//			}					
//		}
		if (memento != null) {
			if(memento.getString(SCHEDULER_VIEW_COLOUR_SCHEME) == null){
				memento.putString(SCHEDULER_VIEW_COLOUR_SCHEME, Activator.getDefault().getPreferenceStore().getString(SCHEDULER_VIEW_COLOUR_SCHEME));
			}		
			if(memento.getBoolean(Show_Canals) == null){
				memento.putBoolean(Show_Canals, Activator.getDefault().getPreferenceStore().getBoolean(Show_Canals));
			}	
			if(memento.getChild(Highlight_) == null){
				memento.createChild(Highlight_);
			}	
			highlightMemento = memento.getChild(Highlight_);
		}
		super.init(site, memento);		
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		memento.putMemento(this.memento);
	}
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		// Inject the extension points
		Activator.getDefault().getInjector().injectMembers(this);

		// Gantt Chart settings object
		final ISettings settings = new AbstractSettings() {
			@Override public boolean enableResizing() { return false; }
			@Override public Color getDefaultEventColor() { return ColorCache.getColor(221, 220, 221); }
			@Override public boolean showPlannedDates() { return false; }
			@Override public String getTextDisplayFormat() { return "#name#";}
			@Override public int getSectionTextSpacer() { return 0; }
			@Override public int getMinimumSectionHeight() { return 5; }
			@Override public int getNumberOfDaysToAppendForEndOfDay() { return 0; }
			@Override public boolean allowBlankAreaVerticalDragAndDropToMoveChart() { return true; }
			@Override public boolean lockHeaderOnVerticalScroll() { return true; }
			@Override public boolean drawFillsToBottomWhenUsingGanttSections() { return true; }
			@Override public int getSectionBarDividerHeight() { return 0; }
			@Override public boolean showGradientEventBars() { return false; }
			@Override public boolean drawSectionsWithGradients() { return false; }
			@Override public boolean allowArrowKeysToScrollChart() {
				return true;
			}
			@Override public boolean showBarsIn3D() {
				return false;
			}
			public int getEventsTopSpacer() { return 5; }
			public int getEventsBottomSpacer() { return 5;
			}
			@Override public boolean showDeleteMenuOption() { return false; }
			@Override public boolean showMenuItemsOnRightClick() { return false; }
			@Override public int getSelectionLineWidth() { return 3; }
		};

		final IColorManager colourManager = new DefaultColorManager() {
			
			@Override
			public boolean useAlphaDrawing() {
				return true;
			};
			
			@Override
			public Color getTextColor() {
				return ColorCache.getWhite();
			}
		};

		viewer = new GanttChartViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | GanttFlags.H_SCROLL_FIXED_RANGE, settings, colourManager) {
			@Override
			protected synchronized void inputChanged(final Object input, final Object oldInput) {
				super.inputChanged(input, oldInput);

				final boolean inputEmpty = (input == null) || ((input instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) input).getCollectedElements().isEmpty());
				final boolean oldInputEmpty = (oldInput == null)
						|| ((oldInput instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) oldInput).getCollectedElements().isEmpty());

				if (inputEmpty != oldInputEmpty) {

					if (packAction != null) {
						packAction.run();
					}
				}
			}
		};
		// viewer.setContentProvider(new AnnotatedScheduleContentProvider());
		// viewer.setLabelProvider(new AnnotatedSequenceLabelProvider());

		final EMFScheduleContentProvider contentProvider = new EMFScheduleContentProvider();
		viewer.setContentProvider(contentProvider);
		final EMFScheduleLabelProvider labelProvider = new EMFScheduleLabelProvider(viewer, memento);

		for (final ISchedulerViewColourSchemeExtension ext : this.colourSchemes) {
			IScheduleViewColourScheme cs = ext.createInstance();
			String ID = ext.getID();
			cs.setID(ID);
			if(ext.isHighlighter().equalsIgnoreCase("true")){
				labelProvider.addHighlighter(ID, cs);
			} else {
				labelProvider.addColourScheme(ID, cs);
			}
		}

		viewer.setLabelProvider(labelProvider);
		// TODO: Hook up action to alter sort behaviour
		// Then refresh
		// E.g. mode?
		// Move into separate class
		viewer.setComparator(viewerComparator);

		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control. This is in the
		// format of pluginid.contextId
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.scheduleview.SchedulerViewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		// /*
		// * Add selection listener. may need tidying up.
		// */
		//
		// selectionListener = new ISelectionListener() {
		//
		// @Override
		// public void selectionChanged(final IWorkbenchPart part,
		// final ISelection selection) {
		//
		// final List<Schedule> schedules = ScheduleAdapter
		// .getSchedules(selection);
		// if (!schedules.isEmpty()) {
		// final boolean needFit = viewer.getInput() == null;
		// setInput(schedules);
		// if (needFit) {
		// Display.getDefault().asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		// packAction.run();
		// }
		// });
		// }
		// } else {
		// setInput(null);
		// }
		// }
		// };

		getSite().setSelectionProvider(viewer);
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		jobManagerListener = ScenarioViewerSynchronizer.registerView(viewer, new ScheduleElementCollector() {
			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule) {
				return Collections.singleton(schedule);
			}
		});

		// getSite().getPage().addSelectionListener("com.mmxlabs.rcp.navigator",
		// selectionListener);

		// // Update view from current selection
		// final ISelection selection = getSite().getWorkbenchWindow()
		// .getSelectionService()
		// .getSelection("com.mmxlabs.rcp.navigator");
		// selectionListener.selectionChanged(null, selection);

		final String colourScheme = memento.getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME);
//		if (colourScheme != null) {
			labelProvider.setScheme(colourScheme);
//		}
	}

	@Override
	public void dispose() {

		ScenarioViewerSynchronizer.deregisterView(jobManagerListener);
		// getSite().getPage().removeSelectionListener(
		// "com.mmxlabs.rcp.navigator", selectionListener);

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
		manager.add(saveFullImageAction);

	}

	private void fillContextMenu(final IMenuManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		manager.add(saveFullImageAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		manager.add(highlightAction);
		if (colourSchemeAction != null) {
			manager.add(colourSchemeAction);
		}
		manager.add(sortModeAction);
		manager.add(packAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {

		zoomInAction = new ZoomInAction(viewer.getGanttChart());
		zoomOutAction = new ZoomOutAction(viewer.getGanttChart());
		
		highlightAction = new HighlightAction(this, viewer, (EMFScheduleLabelProvider) (viewer.getLabelProvider()));
		
		if (!Activator.getDefault().getPreferenceStore().getBoolean(SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION)) {

			colourSchemeAction = new ColourSchemeAction(this, (EMFScheduleLabelProvider) (viewer.getLabelProvider()), viewer);
		}

		sortModeAction = new SortModeAction(this, viewer, (EMFScheduleLabelProvider) viewer.getLabelProvider(), viewerComparator);

		packAction = new PackAction(viewer.getGanttChart());

		saveFullImageAction = new SaveFullImageAction(viewer.getGanttChart());

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.SAVE_AS.getId(), saveFullImageAction);
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

					final boolean needFit = viewer.getInput() == null;

					viewer.setInput(input);

					if ((input != null) && needFit) {
						packAction.run();
					}
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
	 * Create a new {@link PropertySheetPage} instance hooked up to the default EMF adapter factory.
	 * 
	 * @return
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final PropertySheetPage propertySheetPage = new PropertySheetPage();

		propertySheetPage.setPropertySourceProvider(new ScheduledEventPropertySourceProvider());

		return propertySheetPage;
	}
//
//	public class HighlightAction extends SchedulerViewAction {
//
//		public HighlightAction(SchedulerView schedulerView, GanttChartViewer viewer, EMFScheduleLabelProvider emfScheduleLabelProvider) {
//			super("Highlight", IAction.AS_RADIO_BUTTON, schedulerView, viewer, emfScheduleLabelProvider);
//			setImageDescriptor(Activator.getImageDescriptor("/icons/highlight.gif"));			
//		}
//
//		@Override
//		protected void createMenuItems(Menu menu) {
//			
//			final Action highlightTightJourneysAction = new Action("Tight Journeys", IAction.AS_CHECK_BOX) {
//				@Override
//				public void run() {
//					lp.toggleHighlightTightJourneys();
//					setChecked(lp.showCanals());
//					viewer.setInput(viewer.getInput());
//					schedulerView.redraw();
//				}
//			};			
//			highlightTightJourneysAction.setChecked(lp.setHighlighter());
//			final ActionContributionItem aci = new ActionContributionItem(highlightTightJourneysAction);
//			aci.fill(menu, -1);
//		}
//	}


	class SortModeAction extends SchedulerViewAction {

		private final ScenarioViewerComparator comparator;

		public SortModeAction(SchedulerView schedulerView, GanttChartViewer viewer, EMFScheduleLabelProvider lp, final ScenarioViewerComparator comparator) {
			super("Sort", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
			this.comparator = comparator;
			setImageDescriptor(Activator.getImageDescriptor("/icons/alphab_sort_co.gif"));
		}

		@Override
		public void run() {

			// Step through modes
			ScenarioViewerComparator.Mode mode = comparator.getMode();
			final int nextMode = (mode.ordinal() + 1) % ScenarioViewerComparator.Mode.values().length;
			mode = ScenarioViewerComparator.Mode.values()[nextMode];
			comparator.setMode(mode);

			viewer.setInput(viewer.getInput());
		};

		protected void createMenuItems(final Menu menu) {

			for (final ScenarioViewerComparator.Mode mode : ScenarioViewerComparator.Mode.values()) {

				final Action a = new Action(mode.getDisplayName(), IAction.AS_RADIO_BUTTON) {
					@Override
					public void run() {
						comparator.setMode(mode);
						viewer.setInput(viewer.getInput());
					}
				};

				// a.setActionDefinitionId(mode.toString());
				final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
				actionContributionItem.fill(menu, -1);

				// Set initially checked item.
				if (comparator.getMode() == mode) {
					a.setChecked(true);
				}
			}
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, ISelection selection) {
		// TODO make selection more obvious - the gantt selection box is small
		// TODO this seems hard, having taken a look.
		if (part == this) {
			return;
		}
		// Ignore property page activation - otherwise we loose the selecion
		if (part instanceof PropertySheet) {
			return;
		}

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection sel = (IStructuredSelection) selection;
			final List<Object> objects = new ArrayList<Object>(sel.toList().size());
			for (final Object o : sel.toList()) {
				if (o instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) o;
					objects.add(allocation.getLoadAllocation().getSlotVisit());
					objects.add(allocation.getLadenLeg());
					objects.add(allocation.getLadenIdle());
					objects.add(allocation.getDischargeAllocation().getSlotVisit());
					objects.add(allocation.getBallastLeg());
					objects.add(allocation.getBallastIdle());
				} else {
					objects.add(o);
				}
			}
			selection = new StructuredSelection(objects);
		}
		viewer.setSelection(selection);
	}
}