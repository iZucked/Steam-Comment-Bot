/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.Highlight_;
import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME;
import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.Show_Canals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.ganttchart.AbstractSettings;
import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.nebula.widgets.ganttchart.DefaultColorManager;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.GanttFlags;
import org.eclipse.nebula.widgets.ganttchart.GanttGroup;
import org.eclipse.nebula.widgets.ganttchart.GanttSection;
import org.eclipse.nebula.widgets.ganttchart.IColorManager;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
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
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.diff.DiffSelectionAdapter;
import com.mmxlabs.lingo.reports.properties.ScheduledEventPropertySourceProvider;
import com.mmxlabs.lingo.reports.scheduleview.internal.Activator;
import com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ISchedulerViewColourSchemeExtension;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioChangeSetListener;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioChangeSetService;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class SchedulerView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener, IPreferenceChangeListener {

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

	private ScenarioViewerComparator viewerComparator;

	private IMemento memento;
	private IMemento highlightMemento;

	@Inject
	private Iterable<ISchedulerViewColourSchemeExtension> colourSchemeExtensions;

	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<>();

	private HighlightAction highlightAction;

	private boolean currentlyPinned = false;

	private int numberOfSchedules;

	private final Map<String, List<EObject>> allObjectsByKey = new LinkedHashMap<String, List<EObject>>();

	private final Set<EObject> pinnedObjects = new LinkedHashSet<EObject>();

	private IEclipseContext e4Context;
	private ScenarioComparisonService scenarioComparisonService;
	private SelectedScenariosService selectedScenariosService;
	private ScenarioChangeSetService scenarioChangeSetService;

	// New diff stuff
	private Table table;

	// DEMO
	private final boolean showConnections = System.getProperty("schedulechart.showConnections") != null;

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

		e4Context = (IEclipseContext) getSite().getService(IEclipseContext.class);
		this.scenarioComparisonService = e4Context.getActive(ScenarioComparisonService.class);
		this.selectedScenariosService = e4Context.getActive(SelectedScenariosService.class);
		this.scenarioChangeSetService = e4Context.getActive(ScenarioChangeSetService.class);

		// Inject the extension points
		Activator.getDefault().getInjector().injectMembers(this);

		// Gantt Chart settings object
		final ISettings settings = new AbstractSettings() {
			@Override
			public boolean enableResizing() {
				return false;
			}

			@Override
			public boolean useSplitArrowConnections() {
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
				return 1;
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
				return 3;
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

			@Override
			public int getSelectionLineStyle() {
				return SWT.LINE_SOLID;
			}

			@Override
			public int getHeaderMonthHeight() {
				return 22;
			}

			@Override
			public int getHeaderDayHeight() {
				return 22;
			}
		};

		final IColorManager colourManager = new DefaultColorManager() {

			@Override
			public boolean useAlphaDrawing() {
				return true;
			};

			@Override
			public Color getTextColor() {
				return ColorCache.getBlack();
			}

			@Override
			public Color getActiveSessionBarColorLeft() {
				return getHeaderColour();

			}

			@Override
			public Color getActiveSessionBarColorRight() {
				return getHeaderColour();
			}

			@Override
			public Color getNonActiveSessionBarColorLeft() {
				return getHeaderColour();
			}

			@Override
			public Color getNonActiveSessionBarColorRight() {
				return getHeaderColour();
			}

			@Override
			public Color getTextHeaderBackgroundColorTop() {
				return getHeaderColour();
			}

			@Override
			public Color getTopHorizontalLinesColor() {
				return getDividerColour();
			}

			@Override
			public Color getTextHeaderBackgroundColorBottom() {
				return getHeaderColour();
			}

			@Override
			public Color getSaturdayTextColor() {
				return getHeaderColour();
			}

			@Override
			public Color getTimeHeaderBackgroundColorBottom() {
				return getHeaderColour();
			}

			@Override
			public Color getTimeHeaderBackgroundColorTop() {

				return getHeaderColour();
			}

			@Override
			public Color getWeekTimeDividerColor() {
				return getDividerColour();
			}

			@Override
			public Color getYearTimeDividerColor() {
				return getDividerColour();
			}

			@Override
			public Color getWeekDividerLineColor() {
				return getDividerColour();
			}

			@Override
			public Color getMonthTimeDividerColor() {
				return getDividerColour();
			}

			@Override
			public Color getHourTimeDividerColor() {
				return getDividerColour();
			}

			private Color getHeaderColour() {
				return TableColourPalette.getInstance().getColourFor(TableItems.ColumnHeaders, ColourElements.Background);
			}

			private Color getDividerColour() {
				return TableColourPalette.getInstance().getColourFor(TableItems.LineBorders, ColourElements.Background);
			}

		};

		viewer = new GanttChartViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | GanttFlags.H_SCROLL_FIXED_RANGE, settings, colourManager) {
			// @Override
			// protected synchronized void inputChanged(final Object input, final Object oldInput) {
			// super.inputChanged(input, oldInput);
			//
			// final boolean inputEmpty = (input == null) || ((input instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) input).getCollectedElements().isEmpty());
			// final boolean oldInputEmpty = (oldInput == null)
			// || ((oldInput instanceof IScenarioViewerSynchronizerOutput) && ((IScenarioViewerSynchronizerOutput) oldInput).getCollectedElements().isEmpty());
			//
			// if (inputEmpty != oldInputEmpty) {
			//
			// if (packAction != null) {
			// packAction.run();
			// }
			// }
			// }

			@Override
			protected void setSelectionToWidget(@SuppressWarnings("rawtypes") final List l, final boolean reveal) {

				if (showConnections) {

					ganttChart.getGanttComposite().getGanttConnections().clear();

					final BiFunction<SlotAllocation, SlotAllocation, Boolean> differentSequenceChecker = (r1, r2) -> {
						final SlotVisit v1 = r1.getSlotVisit();
						final SlotVisit v2 = r2.getSlotVisit();
						if (v1 == null || v2 == null) {
							return false;
						}
						final Sequence s1 = v1.getSequence();
						final Sequence s2 = v2.getSequence();
						if (s1 == null && s2 == null) {
							return true;
						}
						return !s1.getName().equals(s2.getName());
					};

					final boolean isDiffToBase = scenarioChangeSetService.isDiffToBase();
					// final ChangeSet changeSet = scenarioChangeSetService.getChangeSet();
					final Collection<ChangeSetRow> csRows = scenarioChangeSetService.getSelectedChangeSetRows();
					{
						if (csRows != null) {
							// final List<ChangeSetRow> csRows;
							// if (isDiffToBase) {
							// csRows = changeSet.getChangeSetRowsToBase();
							// } else {
							// csRows = changeSet.getChangeSetRowsToPrevious();
							// }
							final Color lineColour = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

							for (final ChangeSetRow csRow : csRows) {
								{
									final SlotAllocation oldAllocation = csRow.getOriginalLoadAllocation();
									final SlotAllocation newAllocation = csRow.getNewLoadAllocation();

									if (oldAllocation != null && newAllocation != null) {

										if (differentSequenceChecker.apply(oldAllocation, newAllocation)) {
											final GanttEvent oldEvent = internalMap.get(oldAllocation.getSlotVisit());
											final GanttEvent newEvent = internalMap.get(newAllocation.getSlotVisit());

											if (oldEvent != null && newEvent != null) {
												ganttChart.getGanttComposite().addConnection(oldEvent, newEvent, lineColour);
											}
										}
									}
								}
								{
									ChangeSetRow rhsWiringLink = csRow.getRhsWiringLink();
									if (rhsWiringLink != null) {
										final SlotAllocation oldAllocation = rhsWiringLink.getOriginalDischargeAllocation();
										final SlotAllocation newAllocation = csRow.getNewDischargeAllocation();

										if (oldAllocation != null && newAllocation != null) {
											if (differentSequenceChecker.apply(oldAllocation, newAllocation)) {

												final GanttEvent oldEvent = internalMap.get(oldAllocation.getSlotVisit());
												final GanttEvent newEvent = internalMap.get(newAllocation.getSlotVisit());

												if (oldEvent != null && newEvent != null) {
													ganttChart.getGanttComposite().addConnection(oldEvent, newEvent, lineColour);
												}
											}
										}
									}
									ChangeSetRow lhsWiringLink = csRow.getLhsWiringLink();
									if (lhsWiringLink != null) {
										final SlotAllocation newAllocation = lhsWiringLink.getNewDischargeAllocation();
										final SlotAllocation oldAllocation = csRow.getOriginalDischargeAllocation();

										if (oldAllocation != null && newAllocation != null) {
											if (differentSequenceChecker.apply(oldAllocation, newAllocation)) {

												final GanttEvent oldEvent = internalMap.get(oldAllocation.getSlotVisit());
												final GanttEvent newEvent = internalMap.get(newAllocation.getSlotVisit());

												if (oldEvent != null && newEvent != null) {
													ganttChart.getGanttComposite().addConnection(oldEvent, newEvent, lineColour);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				final ArrayList<GanttEvent> selectedEvents;
				final Set<GanttSection> selectedSections = new HashSet<>();
				if (l != null) {
					// Use the internalMap to obtain the list of events we are selecting
					selectedEvents = new ArrayList<GanttEvent>(l.size());
					if (!l.isEmpty()) {
						for (final Object ge : ganttChart.getGanttComposite().getEvents()) {
							final GanttEvent ganttEvent = (GanttEvent) ge;
							ganttEvent.setStatusAlpha(130);
							final Event evt = (Event) ganttEvent.getData();
							if (table != null) {
								// Change alpha for pinned elements
								final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
								if (selectedDataProvider != null) {
									if (selectedDataProvider.isPinnedObject(evt)) {
										ganttEvent.setStatusAlpha(50);
									}
								}
							}
						}
					}
					for (final Object obj : l) {
						if (obj != null) {
							if (internalMap.containsKey(obj)) {
								final GanttEvent ge = internalMap.get(obj);
								selectedEvents.add(ge);
								if (ge.getGanttSection() != null) {
									selectedSections.add(ge.getGanttSection());
								}
								ge.setStatusAlpha(getLabelProviderAlpha((ILabelProvider) getLabelProvider(), obj));

							} else if (getComparer() != null) {
								for (final Map.Entry<Object, GanttEvent> e : internalMap.entrySet()) {
									if (getComparer().equals(e.getKey(), obj)) {
										final GanttEvent ge = internalMap.get(e.getKey());
										selectedEvents.add(ge);
										if (ge.getGanttSection() != null) {
											selectedSections.add(ge.getGanttSection());
										}
										e.getValue().setStatusAlpha(getLabelProviderAlpha((ILabelProvider) getLabelProvider(), e.getKey()));
										ge.setStatusAlpha(getLabelProviderAlpha((ILabelProvider) getLabelProvider(), e.getKey()));
									}
								}
							}
						}
					}
				} else {
					// Clear selection
					selectedEvents = new ArrayList<GanttEvent>(0);
				}
				if (scenarioComparisonService != null && scenarioComparisonService.getDiffOptions().isFilterSelectedSequences()) {
					// final Set<GanttSection> selectedSections = new HashSet<>();
					// for (final GanttEvent event : selectedEvents) {
					// selectedSections.add(event.getGanttSection());
					// }
					final Iterator<GanttSection> itr = new ArrayList<GanttSection>(ganttChart.getGanttComposite().getGanttSections()).iterator();// .iterator();
					while (itr.hasNext()) {
						final GanttSection ganttSection = itr.next();
						final Set<GanttEvent> events = new HashSet<>(ganttSection.getEvents());
						for (final Object o : ganttSection.getEvents()) {
							if (o instanceof GanttGroup) {
								final GanttGroup ganttGroup = (GanttGroup) o;
								events.addAll(ganttGroup.getEventMembers());
							}
						}

						events.retainAll(selectedEvents);
						if (events.isEmpty()) {
							ganttSection.setVisible(false);
						} else {
							boolean visible = true;
							final Object d = ganttSection.getData();
							if (d instanceof Sequence) {
								final Sequence sequence = (Sequence) d;
								if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
									visible = false;
									if (selectedSections.contains(ganttSection)) {
										visible = true;
									}
								}
							}

							ganttSection.setVisible(visible);
						}
					}
				} else {
					final Iterator<GanttSection> itr = new ArrayList<GanttSection>(ganttChart.getGanttComposite().getGanttSections()).iterator();
					while (itr.hasNext()) {
						final GanttSection ganttSection = itr.next();
						boolean visible = true;

						final Object d = ganttSection.getData();
						if (d instanceof Sequence) {
							final Sequence sequence = (Sequence) d;
							if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
								visible = false;
								if (selectedSections.contains(ganttSection)) {
									visible = true;
								}
							}
						}
						ganttSection.setVisible(visible);
					}
				}
				ganttChart.getGanttComposite().setSelection(selectedEvents);
				if (selectedEvents.isEmpty() == false) {
					final GanttEvent sel = selectedEvents.get(0);
					if (!ganttChart.getGanttComposite().isEventVisible(sel, ganttChart.getGanttComposite().getBounds())) {
						ganttChart.getGanttComposite().showEvent(sel, SWT.CENTER);
					}
				}
				ganttChart.getGanttComposite().heavyRedraw();
			}
		};

		// make sure this viewer is listening to preference changes
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		prefs.addPreferenceChangeListener(this);

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
							setInputEquivalents(event, Arrays.asList(new Object[] { slotVisit.getSlotAllocation(), slotVisit.getSlotAllocation().getSlot(),
									slotVisit.getSlotAllocation().getCargoAllocation() /* , slotVisit.getSlotAllocation().getCargoAllocation().getInputCargo() */ }));

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
							// equivalents.add(allocation.getInputCargo());

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
		final EMFScheduleLabelProvider labelProvider = new EMFScheduleLabelProvider(viewer, memento, selectedScenariosService);

		for (final ISchedulerViewColourSchemeExtension ext : this.colourSchemeExtensions) {
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
		viewerComparator = new ScenarioViewerComparator(selectedScenariosService);
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

		selectedScenariosService.addListener(selectedScenariosServiceListener);
		scenarioComparisonService.addListener(scenarioComparisonServiceListener);
		scenarioChangeSetService.addListener(scenarioChangeSetListener);
		// Create the help context id for the viewer's control. This is in the
		// format of pluginid.contextId
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_ScheduleChart");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		getSite().setSelectionProvider(viewer);
		// Get e4 selection service!
		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);

		final String colourScheme = memento.getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME);
		// if (colourScheme != null) {
		labelProvider.setScheme(colourScheme);
		// }

		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
		scenarioComparisonService.triggerListener(scenarioComparisonServiceListener);
		scenarioChangeSetService.triggerListener(scenarioChangeSetListener);
	}

	@Override
	public void dispose() {

		final ESelectionService service = getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		// stop this view from listening to preference changes
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		prefs.removePreferenceChangeListener(this);

		scenarioComparisonService.removeListener(scenarioComparisonServiceListener);
		selectedScenariosService.removeListener(selectedScenariosServiceListener);
		scenarioChangeSetService.removeListener(scenarioChangeSetListener);

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
		RunnerHelper.asyncExec(() -> {
			if (!viewer.getControl().isDisposed()) {
				viewer.setInput(viewer.getInput());
			}
		});
	}

	public void refresh() {
		ViewerHelper.refresh(viewer, false);
	}

	public void setInput(final Object input) {
		RunnerHelper.asyncExec(() -> {
			if (!viewer.getControl().isDisposed()) {

				final boolean needFit = viewer.getInput() == null;

				viewer.setInput(input);

				if ((input != null) && needFit) {
					packAction.run();
				}
			}
		});
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		// Hook up our property sheet page
		if (key.equals(IPropertySheetPage.class)) {
			return (T) getPropertySheetPage();
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
	public void selectionChanged(final MPart part, final Object selectedObject) {
		{
			final IWorkbenchPart view = SelectionHelper.getE3Part(part);

			if (view == this) {
				return;
			}
			if (view instanceof PropertySheet) {
				return;
			}
		}

		ISelection selection = SelectionHelper.adaptSelection(selectedObject);

		if (table == null) {
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection sel = (IStructuredSelection) selection;
				List<Object> objects = new ArrayList<Object>(sel.toList().size());
				for (final Object o : sel.toList()) {
					if (o instanceof CargoAllocation) {
						final CargoAllocation allocation = (CargoAllocation) o;
						// objects.add(allocation.getInputCargo());
						objects.addAll(allocation.getEvents());
						for (final SlotAllocation sa : allocation.getSlotAllocations()) {
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
			ViewerHelper.setSelection(viewer, true, selection);
		} else {
			ViewerHelper.setSelection(viewer, true, DiffSelectionAdapter.expandDown(selection, table));
		}
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

		return new ScheduleDiffUtils().isElementDifferent(pinnedObject, otherObject);
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
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule) {
				return Collections.singleton(schedule);
			}

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);
				SchedulerView.this.clearPinModeData();
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean isPinned) {

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

	@Override
	public void preferenceChange(final PreferenceChangeEvent event) {
		viewer.setInput(viewer.getInput());
	}

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {
					final List<Object> rowElements = new LinkedList<>();
					final IScenarioInstanceElementCollector elementCollector = getElementCollector();
					elementCollector.beginCollecting(pinned != null);
					if (pinned != null) {
						rowElements.addAll(elementCollector.collectElements(pinned, true));
					}
					for (final ScenarioResult other : others) {
						rowElements.addAll(elementCollector.collectElements(other, false));
					}
					elementCollector.endCollecting();
					ViewerHelper.setInput(viewer, true, rowElements);

					// setInput(rowElements);
					// packAction.run();
				}
			};
			RunnerHelper.exec(r, block);
		}
	};

	private final IScenarioComparisonServiceListener scenarioComparisonServiceListener = new IScenarioComparisonServiceListener() {

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			if (d == EDiffOption.FILTER_SCHEDULE_CHART_BY_SELECTION) {
				viewer.setSelection(viewer.getSelection());
			}
		}

		@Override
		public void compareDataUpdate(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pin, final ScenarioResult other, final Table table,
				final List<LNGScenarioModel> rootObjects, final Map<EObject, Set<EObject>> equivalancesMap) {
			// Do Nothing
			SchedulerView.this.table = table;
		}

		@Override
		public void multiDataUpdate(final ISelectedDataProvider selectedDataProvider, final Collection<ScenarioResult> others, final Table table, final List<LNGScenarioModel> rootObjects) {
			// Do Nothing
			SchedulerView.this.table = table;
		}
	};

	private final IScenarioChangeSetListener scenarioChangeSetListener = new IScenarioChangeSetListener() {
		@Override
		public void changeSetChanged(@Nullable final ChangeSetRoot changeSetRoot, @Nullable final ChangeSet changeSet, @Nullable final Collection<ChangeSetRow> changeSetRows, final boolean diffToBase) {
			ViewerHelper.refresh(viewer, true);
		}
	};
}