/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Highlight_;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Show_Canals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.common.Equality;
import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.actions.PackAction;
import com.mmxlabs.ganttviewer.actions.SaveFullImageAction;
import com.mmxlabs.ganttviewer.actions.ZoomInAction;
import com.mmxlabs.ganttviewer.actions.ZoomOutAction;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScenarioViewerSynchronizer;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.properties.ScheduledEventPropertySourceProvider;
import com.mmxlabs.shiplingo.platform.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.shiplingo.platform.scheduleview.internal.Activator;
import com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ISchedulerViewColourSchemeExtension;

public class SchedulerView extends ViewPart implements ISelectionListener {

	private static final String SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION = "SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION";

	// public static final String = "";

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

	private boolean currentlyPinned = false;

	private int numberOfSchedules;

	private final Map<String, List<EObject>> allObjectsByKey = new LinkedHashMap<String, List<EObject>>();

	private final Set<EObject> pinnedObjects = new LinkedHashSet<EObject>();

	/**
	 * The constructor.
	 */
	public SchedulerView() {
	}

	@Override
	public void init(final IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;

		// check that it's the right memento - and non-null?...

		// set defaults from preference store for missing settings...
		// for (String key : memento.getAttributeKeys()) { // need list from Constants and then the right getString/Boolean call below...
		// if(memento.getString(key) == null){
		// memento.putString(key, Activator.getDefault().getPreferenceStore().getString(key));
		// }
		// }
		if (memento != null) {
			if (memento.getString(SCHEDULER_VIEW_COLOUR_SCHEME) == null) {
				memento.putString(SCHEDULER_VIEW_COLOUR_SCHEME, Activator.getDefault().getPreferenceStore().getString(SCHEDULER_VIEW_COLOUR_SCHEME));
			}
			if (memento.getBoolean(Show_Canals) == null) {
				memento.putBoolean(Show_Canals, Activator.getDefault().getPreferenceStore().getBoolean(Show_Canals));
			}
			if (memento.getChild(Highlight_) == null) {
				memento.createChild(Highlight_);
			}
			highlightMemento = memento.getChild(Highlight_);
		}
		super.init(site, memento);
	}

	@Override
	public void saveState(final IMemento memento) {
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
			@Override
			public boolean enableResizing() {
				return false;
			}

			@Override
			public Color getDefaultEventColor() {
				return ColorCache.getColor(221, 220, 221);
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
				return 5;
			}

			@Override
			public int getNumberOfDaysToAppendForEndOfDay() {
				return 0;
			}

			@Override
			public boolean allowBlankAreaVerticalDragAndDropToMoveChart() {
				return true;
			}

			@Override
			public boolean lockHeaderOnVerticalScroll() {
				return true;
			}

			@Override
			public boolean drawFillsToBottomWhenUsingGanttSections() {
				return true;
			}

			@Override
			public int getSectionBarDividerHeight() {
				return 0;
			}

			@Override
			public boolean showGradientEventBars() {
				return false;
			}

			@Override
			public boolean drawSectionsWithGradients() {
				return false;
			}

			@Override
			public boolean allowArrowKeysToScrollChart() {
				return true;
			}

			@Override
			public boolean showBarsIn3D() {
				return false;
			}

			@Override
			public int getEventsTopSpacer() {
				return 5;
			}

			@Override
			public int getEventsBottomSpacer() {
				return 5;
			}

			@Override
			public boolean showDeleteMenuOption() {
				return false;
			}

			@Override
			public boolean showMenuItemsOnRightClick() {
				return false;
			}

			@Override
			public int getSelectionLineWidth() {
				return 3;
			}
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

		final EMFScheduleContentProvider contentProvider = new EMFScheduleContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				super.inputChanged(viewer, oldInput, newInput);
				clearInputEquivalents();
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final Object[] result = super.getElements(inputElement);
				return result;
			}

			@Override
			public Object[] getChildren(final Object parent) {

				Object[] result = super.getChildren(parent);
				if (parent instanceof Sequence && numberOfSchedules > 1 && currentlyPinned) {
					final List<EObject> objects = new LinkedList<EObject>();
					for (final Map.Entry<String, List<EObject>> e : allObjectsByKey.entrySet()) {
						EObject ref = null;
						final LinkedHashSet<EObject> objectsToAdd = new LinkedHashSet<EObject>();

						// Find ref...
						for (final EObject ca : e.getValue()) {
							if (pinnedObjects.contains(ca)) {
								ref = ca;
								break;
							}
						}

						if (ref == null) {
							// No ref found, so add all
							objectsToAdd.addAll(e.getValue());
						} else {
							for (final EObject ca : e.getValue()) {
								if (ca == ref) {
									continue;
								}
								if (e.getValue().size() != numberOfSchedules) {
									// Different number of elements, so add all!
									// This means something has been removed/added
									objectsToAdd.addAll(e.getValue());
								} else if (isElementDifferent(ref, ca)) {
									// There is a data difference, so add
									objectsToAdd.addAll(e.getValue());
								}
							}
						}
						for (final EObject eObj : objectsToAdd) {
							if (eObj.eContainer() == parent) {
								objects.add(eObj);

							}
						}

					}
					result = objects.toArray();
				}
				if (result != null) {
					for (final Object event : result) {
						if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							setInputEquivalents(
									event,
									Arrays.asList(new Object[] { slotVisit.getSlotAllocation(), slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation().getCargoAllocation(),
											slotVisit.getSlotAllocation().getCargoAllocation().getInputCargo() }));

							// } else if (event instanceof Idle) {
							// setInputEquivalents(event, Arrays.asList(new Object[] { ((Idle) event).getSlotAllocation().getCargoAllocation() }));

						} else if (event instanceof CargoAllocation) {
							final CargoAllocation allocation = (CargoAllocation) event;

							final List<Object> equivalents = new ArrayList<Object>();
							for (final SlotAllocation sa : allocation.getSlotAllocations()) {
								equivalents.add(sa.getSlotVisit());
								equivalents.add(sa.getSlot());
							}
							equivalents.addAll(allocation.getEvents());
							equivalents.add(allocation.getInputCargo());

							setInputEquivalents(allocation, equivalents);

						} else if (event instanceof VesselEventVisit) {
							setInputEquivalents(event, Arrays.asList(new Object[] { ((VesselEventVisit) event).getVesselEvent() }));
						} else {
							setInputEquivalents(event, Collections.emptyList());
						}
					}
				}
				return result;
			}
		};
		viewer.setContentProvider(contentProvider);
		final EMFScheduleLabelProvider labelProvider = new EMFScheduleLabelProvider(viewer, memento);

		for (final ISchedulerViewColourSchemeExtension ext : this.colourSchemes) {
			final IScheduleViewColourScheme cs = ext.createInstance();
			final String ID = ext.getID();
			cs.setID(ID);
			if (ext.isHighlighter().equalsIgnoreCase("true")) {
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

		viewer.setComparer(new IElementComparer() {
			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(Object a, Object b) {
				if (!contents.contains(a) && equivalents.containsKey(a)) {
					a = equivalents.get(a);
				}
				if (!contents.contains(b) && equivalents.containsKey(b)) {
					b = equivalents.get(b);
				}
				return Equality.isEqual(a, b);
			}
		});

		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control. This is in the
		// format of pluginid.contextId
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.shiplingo.platform.scheduleview.SchedulerViewer");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().setSelectionProvider(viewer);
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(this);
		jobManagerListener = ScenarioViewerSynchronizer.registerView(viewer, getElementCollector());

		final String colourScheme = memento.getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME);
		// if (colourScheme != null) {
		labelProvider.setScheme(colourScheme);
		// }
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

	private class SortModeAction extends SchedulerViewAction {

		private final ScenarioViewerComparator comparator;

		public SortModeAction(final SchedulerView schedulerView, final GanttChartViewer viewer, final EMFScheduleLabelProvider lp, final ScenarioViewerComparator comparator) {
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

		@Override
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
		if (part == this) {
			return;
		}
		// Ignore property page activation - otherwise we loose the selection
		if (part instanceof PropertySheet) {
			return;
		}

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection sel = (IStructuredSelection) selection;
			List<Object> objects = new ArrayList<Object>(sel.toList().size());
			for (final Object o : sel.toList()) {
				if (o instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) o;
					objects.add(allocation.getInputCargo());
					objects.addAll(allocation.getEvents());
					for (SlotAllocation sa : allocation.getSlotAllocations()) {
						objects.add(sa.getSlotVisit());
					}
				} else if (o instanceof Cargo) {
					final Cargo cargo = (Cargo) o;
					objects.add(cargo);
					objects.addAll(cargo.getSlots());
				} else {
					objects.add(o);
				}
			}
			objects = expandSelection(objects);
			selection = new StructuredSelection(objects);
		}
		viewer.setSelection(selection);
	}

	private final HashMap<Object, Object> equivalents = new HashMap<Object, Object>();
	private final HashSet<Object> contents = new HashSet<Object>();

	protected void setInputEquivalents(final Object input, final Collection<Object> objectEquivalents) {
		for (final Object o : objectEquivalents) {
			equivalents.put(o, input);
		}
		contents.add(input);
	}

	protected void clearInputEquivalents() {
		equivalents.clear();
		contents.clear();
	}

	/**
	 * Helper method to expand cargo selections to include the whole set of events representing the cargo
	 * 
	 * @param selectedObjects
	 * @return
	 */
	private List<Object> expandSelection(final List<Object> selectedObjects) {
		final Set<Object> newSelection = new HashSet<Object>(selectedObjects.size());
		for (final Object o : selectedObjects) {
			newSelection.add(o);
			if (o instanceof Slot) {
				final Slot slot = (Slot) o;
				final Object object = equivalents.get(slot);
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					newSelection.add(slotVisit);
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					newSelection.add(slotAllocation);
					if (slotAllocation != null) {
						final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
						if (cargoAllocation != null) {
							newSelection.add(cargoAllocation);
							newSelection.addAll(cargoAllocation.getEvents());
						}
					}
				}
			} else {
				if (equivalents.containsKey(o)) {
					newSelection.add(equivalents.get(o));
				}
			}
		}
		newSelection.retainAll(contents);
		return new ArrayList<Object>(newSelection);

	}

	private boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {

		return ScheduleDiffUtils.isElementDifferent(pinnedObject, otherObject);
	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#beginCollecting()} to reset pin mode data
	 * 
	 */
	private void clearPinModeData() {
		clearInputEquivalents();
		currentlyPinned = false;
		allObjectsByKey.clear();
		pinnedObjects.clear();
		numberOfSchedules = 0;
	}

	private IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule) {
				return Collections.singleton(schedule);
			}

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				SchedulerView.this.clearPinModeData();
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<Event> interestingEvents = new LinkedList<Event>();
				for (final Sequence sequence : schedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof StartEvent) {
							interestingEvents.add(event);
						} else if (event instanceof VesselEventVisit) {
							interestingEvents.add(event);
						} else if (event instanceof GeneratedCharterOut) {
							interestingEvents.add(event);
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							// if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
							interestingEvents.add(event);
							// }
						}
					}
				}

				// SchedulerView.this.collectPinModeElements(interestingEvents, isPinned);

				return Collections.singleton(schedule);
			}
		};
	}

	private void collectPinModeElements(final List<? extends EObject> objects, final boolean isPinned) {
		// Temp disable diff mode
		// currentlyPinned |= isPinned;
		++numberOfSchedules;

		for (final EObject ca : objects) {
			final List<EObject> l;
			final String key = getElementKey(ca);
			if (allObjectsByKey.containsKey(key)) {
				l = allObjectsByKey.get(key);
			} else {
				l = new LinkedList<EObject>();
				allObjectsByKey.put(key, l);
			}

			l.add(ca);

			if (isPinned) {
				pinnedObjects.add(ca);
			}
		}
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	private String getElementKey(final EObject element) {
		if (element instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) element;
			return slotVisit.getSlotAllocation().getSlot().getName();
		} else if (element instanceof Event) {
			return ((Event) element).name();
		} else if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		}
		return element.toString();
	}

	private int getEventDuration(final Event event) {

		int duration = event.getDuration();
		Event next = event.getNextEvent();
		while (next != null && !isSegmentStart(next)) {
			duration += next.getDuration();
			next = next.getNextEvent();
		}
		return duration;

	}

	private boolean isSegmentStart(final Event event) {
		if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof SlotVisit) {
			if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}
}