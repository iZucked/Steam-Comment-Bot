/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.nebula.widgets.ganttchart.IGanttChartItem;
import org.eclipse.nebula.widgets.ganttchart.ILegendItem;
import org.eclipse.nebula.widgets.ganttchart.ISettings;
import org.eclipse.nebula.widgets.ganttchart.LegendItemImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;
import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.actions.PackAction;
import com.mmxlabs.ganttviewer.actions.SaveFullImageAction;
import com.mmxlabs.ganttviewer.actions.ZoomInAction;
import com.mmxlabs.ganttviewer.actions.ZoomOutAction;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.scheduleview.internal.Activator;
import com.mmxlabs.lingo.reports.scheduleview.rendering.DefaultRenderOrderComparator;
import com.mmxlabs.lingo.reports.scheduleview.views.ScenarioViewerComparator.Category;
import com.mmxlabs.lingo.reports.scheduleview.views.ScenarioViewerComparator.Mode;
import com.mmxlabs.lingo.reports.scheduleview.views.colourschemes.ISchedulerViewColourSchemeExtension;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.BuySellSplit;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProvider;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProviderExtension;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.PositionsSequenceProviderException;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.SelectionToSandboxUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.MultiEvent;
import com.mmxlabs.models.lng.schedule.util.PositionsSequence;
import com.mmxlabs.models.ui.tabular.TableColourPalette;
import com.mmxlabs.models.ui.tabular.TableColourPalette.ColourElements;
import com.mmxlabs.models.ui.tabular.TableColourPalette.TableItems;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SchedulerView extends ViewPart implements IPreferenceChangeListener {

	private static final String SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION = "SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION";

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.scheduleview.views.SchedulerView";

	private GanttChartViewer viewer;
	private Action zoomInAction;
	private Action zoomOutAction;

	private Action colourSchemeAction;

	private PackAction packAction;

	private SaveFullImageAction saveFullImageAction;

	private Action sortModeAction;

	private RunnableAction toggleLegend;
	protected EMFScheduleContentProvider contentProvider;
	private ScenarioViewerComparator viewerComparator;

	private IMemento memento;

	@Inject
	private Iterable<ISchedulerViewColourSchemeExtension> colourSchemeExtensions;

	@Inject
	protected Iterable<ISchedulePositionsSequenceProviderExtension> positionsSequenceProviderExtensions;
	
	private HighlightAction highlightAction;

	private int numberOfSchedules;

	private ScenarioComparisonService scenarioComparisonService;

	private ReentrantSelectionManager selectionManager;

	boolean showNominalsByDefault = false;
	
	@Nullable
	private ISelectedDataProvider currentSelectedDataProvider = new TransformedSelectedDataProvider(null);

	/**
	 * Basic colour only items. Other are rendered directly once the ganttchart has been created
	 */
	private static final List<ILegendItem> basiclegendItems = Lists.newArrayList( //
			new LegendItemImpl("Laden travel/idle", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Journey, ColourPalette.ColourElements.Background),
					ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Laden_Idle, ColourPalette.ColourElements.Background)),
			new LegendItemImpl("Ballast travel/idle", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, ColourPalette.ColourElements.Background),
					ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Ballast_Idle, ColourPalette.ColourElements.Background)),
			new LegendItemImpl("Port visit, late", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_Load, ColourPalette.ColourElements.Background),
					ColourPalette.getInstance().getColourFor(ColourPaletteItems.Late_Load, ColourPalette.ColourElements.Background)),
			new LegendItemImpl("Charter out real/generated", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_CharterOut, ColourPalette.ColourElements.Background),
					ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourPalette.ColourElements.Background)),
			new LegendItemImpl("Dry-dock/Maintenance", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_DryDock, ColourPalette.ColourElements.Background),
					ColourPalette.getInstance().getColourFor(ColourPaletteItems.Event_Maintenance, ColourPalette.ColourElements.Background)),
			new LegendItemImpl("Charter Length", ColourPalette.getInstance().getColourFor(ColourPaletteItems.Voyage_CharterLength, ColourPalette.ColourElements.Background)));
	private final List<ILegendItem> legendItems = new LinkedList<>();

	@Override
	public void init(final IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;

		if (memento != null) {
			if (memento.getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME) == null) {
				memento.putString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME, Activator.getDefault().getPreferenceStore().getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME));
			}
			if (memento.getBoolean(SchedulerViewConstants.Show_Canals) == null) {
				memento.putBoolean(SchedulerViewConstants.Show_Canals, Activator.getDefault().getPreferenceStore().getBoolean(SchedulerViewConstants.Show_Canals));
			}
			if (memento.getBoolean(SchedulerViewConstants.Show_Nominals) == null) {
				memento.putBoolean(SchedulerViewConstants.Show_Nominals, Activator.getDefault().getPreferenceStore().getBoolean(SchedulerViewConstants.Show_Nominals));
			}
			if (memento.getChild(SchedulerViewConstants.Highlight_) == null) {
				memento.createChild(SchedulerViewConstants.Highlight_);
			}

			this.showNominalsByDefault = memento.getBoolean(SchedulerViewConstants.Show_Nominals);
		}
		super.init(site, memento);
	}

	@Override
	public void saveState(final IMemento memento) {

		// Copy existing attributes
		memento.putMemento(this.memento);

		// Set anything that hasn't been updated in the shared memento
		memento.putBoolean(SchedulerViewConstants.Show_Days, viewer.getGanttChart().getGanttComposite().isShowingDaysOnEvents());
		memento.putBoolean(SchedulerViewConstants.Show_Nominals, this.showNominalsByDefault);
		memento.putString(SchedulerViewConstants.SortMode, viewerComparator.getMode().toString());
		memento.putString(SchedulerViewConstants.SortCategory, viewerComparator.getCategory().toString());

		super.saveState(memento);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		final IEclipseContext e4Context = getSite().getService(IEclipseContext.class);
		this.scenarioComparisonService = e4Context.getActive(ScenarioComparisonService.class);

		// Inject the extension points
		Activator.getDefault().getInjector().injectMembers(this);

		// Gantt Chart settings object
		final ISettings settings = createGanttSettings();
		final IColorManager colourManager = createGanttColourManager();

		viewer = new GanttChartViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | GanttFlags.H_SCROLL_FIXED_RANGE, settings, colourManager) {

			@Override
			protected List<Object> getSelectionFromObjects(final List<GanttEvent> allSelectedEvents) {

				// Use reverse map to get underlying objects
				final List<Object> selectedObjects = new ArrayList<>(allSelectedEvents.size());

				for (final GanttEvent ge : allSelectedEvents) {

					final Object obj = ge.getData();

					if (obj != null) {
						if (obj instanceof final MultiEvent me) {
							selectedObjects.addAll(me.getElements());
						} else {
							selectedObjects.add(obj);
						}

						// selectedObjects.add(obj);
					}

				}

				return selectedObjects;
			}

			@Override
			public void setSelection(final ISelection selection) {

				if (selection instanceof final IStructuredSelection ss) {

					final List<Object> n = new LinkedList<>();
					for (final var e : ss.toList()) {
						if (e instanceof final MultiEvent me) {
							n.addAll(me.getElements());
						} else {
							n.add(e);
						}
					}
					super.setSelection(new StructuredSelection(n));
					return;
				}

				super.setSelection(selection);
			}

			@Override
			protected void setSelectionToWidget(@SuppressWarnings("rawtypes") final List providedSelection, final boolean reveal) {
				// Take a copy of the array so we can modify it later if it was empty
				final List<Object> l = new LinkedList<>(providedSelection);
				final boolean emptyInput = providedSelection.isEmpty();

				// Render the connections between events
				{
					ganttChart.getGanttComposite().getGanttConnections().clear();

					final BiPredicate<SlotAllocation, SlotAllocation> differentSequenceChecker = (r1, r2) -> {
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

					final Collection<ChangeSetTableRow> csRows = currentSelectedDataProvider.getSelectedChangeSetRows();
					{
						if (csRows != null) {

							final Color lineColour = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

							for (final ChangeSetTableRow csRow : csRows) {
								{
									final SlotAllocation oldAllocation = csRow.getLhsBefore() != null ? csRow.getLhsBefore().getLoadAllocation() : null;
									final SlotAllocation newAllocation = csRow.getLhsAfter() != null ? csRow.getLhsAfter().getLoadAllocation() : null;

									if (oldAllocation != null && newAllocation != null) {

										if (differentSequenceChecker.test(oldAllocation, newAllocation)) {
											final GanttEvent oldEvent = internalMap.get(oldAllocation.getSlotVisit());
											final GanttEvent newEvent = internalMap.get(newAllocation.getSlotVisit());

											if (oldEvent != null && newEvent != null) {
												ganttChart.getGanttComposite().addConnection(oldEvent, newEvent, lineColour);
											}

											// If the selection was initially empty, then add in all objects from the
											// ChangeSetTableRow
											if (emptyInput) {
												l.add(oldAllocation.getSlotVisit());
												l.add(newAllocation.getSlotVisit());
											}
										}
									}
								}
								{
									final SlotAllocation oldAllocation = csRow.getCurrentRhsBefore() != null ? csRow.getCurrentRhsBefore().getDischargeAllocation() : null;
									final SlotAllocation newAllocation = csRow.getCurrentRhsAfter() != null ? csRow.getCurrentRhsAfter().getDischargeAllocation() : null;

									if (oldAllocation != null && newAllocation != null) {
										if (differentSequenceChecker.test(oldAllocation, newAllocation)) {

											final GanttEvent oldEvent = internalMap.get(oldAllocation.getSlotVisit());
											final GanttEvent newEvent = internalMap.get(newAllocation.getSlotVisit());

											if (oldEvent != null && newEvent != null) {
												ganttChart.getGanttComposite().addConnection(oldEvent, newEvent, lineColour);
											}

											// If the selection was initially empty, then add in all objects from the
											// ChangeSetTableRow
											if (emptyInput) {
												l.add(oldAllocation.getSlotVisit());
												l.add(newAllocation.getSlotVisit());
											}
										}
									}
								}
							}
						}
					}
				}
				// Canal events are always selected with journeys 
				final List<Object> additionalElements = l.stream() //
						.filter(Journey.class::isInstance) //
						.map(Journey.class::cast) //
						.map(Journey::getCanalJourneyEvent) //
						.filter(Objects::nonNull) //
						.map(Object.class::cast) //
						.toList();

				l.addAll(additionalElements);
				// Apply alpha.
				// - Fade out objects which are not selected.
				// - Fade out pinned scenario objects more.

				final ArrayList<GanttEvent> selectedEvents;
				final Set<GanttSection> selectedSections = new HashSet<>();
				if (l != null) {
					// Use the internalMap to obtain the list of events we are selecting
					selectedEvents = new ArrayList<>(l.size());

					if (!l.isEmpty() && currentSelectedDataProvider != null && (currentSelectedDataProvider.getSelectedChangeSetRows() != null || !currentSelectedDataProvider.inPinDiffMode())) {
						for (final GanttEvent ganttEvent : ganttChart.getGanttComposite().getEvents()) {
							// Render CanalJourneyEvent without change to alpha
							if (!(ganttEvent.getData() instanceof CanalJourneyEvent)) {
								ganttEvent.setStatusAlpha(130);
							} else {
								ganttEvent.setStatusAlpha(255);
							}
							if (ganttEvent.getData() instanceof final EObject evt) {
								// Change alpha for pinned elements
								if (currentSelectedDataProvider.isPinnedObject(evt)) {
									ganttEvent.setStatusAlpha(50);
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
					final Collection<Object> changeSetSelection = currentSelectedDataProvider.getChangeSetSelection();
					if (changeSetSelection != null) {
						for (final Object obj : changeSetSelection) {
							if (obj != null) {
								if (internalMap.containsKey(obj)) {
									final GanttEvent ge = internalMap.get(obj);
									// selectedEvents.add(ge);
									if (ge.getGanttSection() != null) {
										selectedSections.add(ge.getGanttSection());
									}
									// ge.setStatusAlpha(getLabelProviderAlpha((ILabelProvider) getLabelProvider(),
									// obj));

								} else if (getComparer() != null) {
									for (final Map.Entry<Object, GanttEvent> e : internalMap.entrySet()) {
										if (getComparer().equals(e.getKey(), obj)) {
											final GanttEvent ge = internalMap.get(e.getKey());
											// selectedEvents.add(ge);
											if (ge.getGanttSection() != null) {
												selectedSections.add(ge.getGanttSection());
											}
											// e.getValue().setStatusAlpha(getLabelProviderAlpha((ILabelProvider)
											// getLabelProvider(), e.getKey()));
											// ge.setStatusAlpha(getLabelProviderAlpha((ILabelProvider) getLabelProvider(),
											// e.getKey()));
										}
									}
								}
							}
						}
					}
				} else {
					// Clear selection
					selectedEvents = new ArrayList<>(0);
				}
				// Work out which rows to show when filtering
				if (scenarioComparisonService != null && scenarioComparisonService.getDiffOptions().isFilterSelectedSequences()) {
					final Iterator<GanttSection> itr = new ArrayList<>(ganttChart.getGanttComposite().getGanttSections()).iterator();
					while (itr.hasNext()) {
						final GanttSection ganttSection = itr.next();
						final Set<IGanttChartItem> events = new HashSet<>(ganttSection.getEvents());
						for (final IGanttChartItem o : ganttSection.getEvents()) {
							if (o instanceof final GanttGroup ganttGroup) {
								events.addAll(ganttGroup.getEventMembers());
							}
						}

						events.retainAll(selectedEvents);
						if (events.isEmpty()) {
							boolean visible = true;
							visible = false;
							if (selectedSections.contains(ganttSection)) {
								visible = true;
							}
							ganttSection.setVisible(visible);

						} else {
							boolean visible = true;
							final Object d = ganttSection.getData();
							if (d instanceof final Sequence sequence) {
								if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
									visible = showNominalsByDefault;
									if (selectedSections.contains(ganttSection)) {
										visible = true;
									}
								}
							} else if (d instanceof PositionsSequence ps) {
								var cp = (EMFScheduleContentProvider) getContentProvider();
								visible = cp.isProviderEnabledWithNoError(ps.getProviderId())
										|| cp.enabledPositionsSequenceProviders.values().stream().noneMatch(Optional::isEmpty) && !ps.isPartition();
							}

							ganttSection.setVisible(visible);
						}
					}
				} else {
					final Iterator<GanttSection> itr = new ArrayList<>(ganttChart.getGanttComposite().getGanttSections()).iterator();
					while (itr.hasNext()) {
						final GanttSection ganttSection = itr.next();
						boolean visible = true;

						final Object d = ganttSection.getData();
						if (d instanceof final Sequence sequence) {
							if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
								visible = showNominalsByDefault;
								if (selectedSections.contains(ganttSection)) {
									visible = true;
								}
							}
						} else if (d instanceof final PositionsSequence ps) { 
							var cp = (EMFScheduleContentProvider) getContentProvider();
							visible = cp.isProviderEnabledWithNoError(ps.getProviderId()) || cp.enabledPositionsSequenceProviders.values().stream().noneMatch(Optional::isEmpty) && !ps.isPartition();
						}

						ganttSection.setVisible(visible);
					}
				}
				ganttChart.getGanttComposite().setSelection(selectedEvents);
				if (!selectedEvents.isEmpty()) {
					final GanttEvent sel = selectedEvents.get(0);
					if (!ganttChart.getGanttComposite().isEventVisible(sel, ganttChart.getGanttComposite().getBounds())) {
						ganttChart.getGanttComposite().showEvent(sel, SWT.CENTER);
					}
				}
				ganttChart.getGanttComposite().heavyRedraw();
			}

		};
		viewer.getGanttChart().getGanttComposite().setRenderOrderComparator(new DefaultRenderOrderComparator());

		viewer.getGanttChart().getGanttComposite().setMenuAction((menu, event) -> {

			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SANDBOX)) {
				if (numberOfSchedules == 1) {

					final IStructuredSelection structuredSelection = viewer.getStructuredSelection();
					if (SelectionToSandboxUtil.canSelectionBeUsed(structuredSelection)) {
						final ScenarioResult result = currentSelectedDataProvider.getScenarioResult((EObject) structuredSelection.getFirstElement());

						if (result != null) {

							final ADPModel adpModel = ScenarioModelUtil.getADPModel(result.getScenarioDataProvider());
							if (adpModel != null) {
								// Cannot use sandbox with ADP
								return;
							}

							final MenuItem item = new MenuItem(menu, SWT.PUSH);
							item.setText("Create sandbox");

							item.addListener(SWT.Selection, e -> {
								final IScenarioDataProvider sdp = result.getScenarioDataProvider();
								SelectionToSandboxUtil.selectionToSandbox(structuredSelection, true, sdp);
							});
						}
					}
				}
			}
		});

		viewer.getGanttChart().getGanttComposite().setLegendProvider(() -> legendItems);

		// Restore show days setting
		{
			final Boolean v = memento.getBoolean(SchedulerViewConstants.Show_Days);
			if (v != null) {
				viewer.getGanttChart().getGanttComposite().setShowDaysOnEvents(v);
			}
		}
		// make sure this viewer is listening to preference changes
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		prefs.addPreferenceChangeListener(this);

		contentProvider = new EMFScheduleContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				super.inputChanged(viewer, oldInput, newInput);
				clearInputEquivalents();
			}

			@Override
			public Object[] getChildren(final Object parent) {

				final Object[] result = super.getChildren(parent);
				if (result != null) {

					final Function<Object, List<Object>> generator = event -> {
						if (event instanceof final SlotVisit slotVisit) {
							return Lists.newArrayList(slotVisit.getSlotAllocation(), slotVisit.getSlotAllocation().getSlot(), slotVisit.getSlotAllocation().getCargoAllocation());
						} else if (event instanceof final CargoAllocation allocation) {

							final List<Object> localEquivalents = new ArrayList<>();
							for (final SlotAllocation sa : allocation.getSlotAllocations()) {
								localEquivalents.add(sa.getSlotVisit());
								localEquivalents.add(sa.getSlot());
							}
							localEquivalents.addAll(allocation.getEvents());

							return localEquivalents;

						} else if (event instanceof final VesselEventVisit vev) {
							return Collections.singletonList(vev.getVesselEvent());
						} else if (event instanceof final OpenSlotAllocation sa) {
							return Collections.singletonList(sa.getSlot());
						} else if (event instanceof StartEvent se) {
							if (se.getSequence() != null && se.getSequence().getVesselCharter() != null) {
								return Collections.singletonList(se.getSequence().getVesselCharter());
							}
						} else if (event instanceof EndEvent ee) {
							if (ee.getSequence() != null && ee.getSequence().getVesselCharter() != null) {
								return Collections.singletonList(ee.getSequence().getVesselCharter());
							}
						}

						return Collections.emptyList();

					};

					for (final Object event : result) {
						if (event instanceof final MultiEvent me) {
							final List<Object> localEquivalents = new ArrayList<>();
							for (final var e : me.getElements()) {
								localEquivalents.addAll(generator.apply(e));
							}
							localEquivalents.addAll(me.getElements());
							setInputEquivalents(event, localEquivalents);
						} else {
							setInputEquivalents(event, generator.apply(event));
						}
					}
				}
				return result;
			}

			@Override
			public IScenarioDataProvider getScenarioDataProviderFor(final Object obj) {
				if (obj instanceof final EObject eObject) {

					if (currentSelectedDataProvider != null) {
						final @Nullable ScenarioResult scenarioResult = currentSelectedDataProvider.getScenarioResult(eObject);
						if (scenarioResult != null) {
							return scenarioResult.getScenarioDataProvider();
						}
					}

				}
				return null;
			}
			
			@Override
			public final List<@NonNull PositionsSequence> getPositionsSequences(Schedule schedule) {
				List<@NonNull PositionsSequence> result = new ArrayList<>();
				
				try {
					result.addAll(new BuySellSplit().provide(schedule));
				} catch (PositionsSequenceProviderException e) {
					// BuySellSplit should never throw this
				}

				if (positionsSequenceProviderExtensions.iterator().hasNext()) {
					for (var ext: positionsSequenceProviderExtensions) {
						ISchedulePositionsSequenceProvider provider = ext.createInstance();
						try {
							result.addAll(provider.provide(schedule));
						} catch (PositionsSequenceProviderException e1) {
							enabledPositionsSequenceProviders.put(provider.getId(), Optional.of(e1));
						}
					}
				}
				return result;
			}
			
		};

		viewer.setContentProvider(contentProvider);
		final EMFScheduleLabelProvider labelProvider = new EMFScheduleLabelProvider(viewer, memento, scenarioComparisonService);

		for (final ISchedulerViewColourSchemeExtension ext : this.colourSchemeExtensions) {
			final IScheduleViewColourScheme cs = ext.createInstance();
			final String extID = ext.getID();
			cs.setID(extID);
			if (ext.isHighlighter().equalsIgnoreCase("true")) {
				labelProvider.addHighlighter(extID, cs);
			} else {
				labelProvider.addColourScheme(extID, cs);
			}
		}
		// Restore highlighter settings
		{
			final IMemento highlightSettings = memento.getChild(SchedulerViewConstants.Highlight_);
			if (highlightSettings != null) {
				for (final var hi : labelProvider.getHighlighters()) {
					if (highlightSettings.getBoolean(hi.getID()) == Boolean.TRUE) {
						labelProvider.toggleHighlighter(hi);
					}
				}
			}
		}

		viewer.setLabelProvider(labelProvider);
		viewerComparator = new ScenarioViewerComparator(scenarioComparisonService);

		// Legend (after label provider has been set)
		legendItems.addAll(basiclegendItems);
		legendItems.add(make("FOB, open, multi", () -> {

			final List<Object> l = new LinkedList<>();
			// FOB Purchase,
			{
				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
				s.setDESPurchase(false);
				s.setOptional(true);
				sa.setSlot(s);
				l.add(sa);
			}
			// FOB Purchase open, non-optional
			{
				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
				s.setDESPurchase(false);
				s.setOptional(false);
				sa.setSlot(s);
				l.add(sa);
			}
			// Multiple FOB's
			{

				final List<Object> l2 = new LinkedList<>();
				{

					final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
					final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
					s.setDESPurchase(false);
					s.setOptional(true);
					sa.setSlot(s);
					l2.add(sa);
				}
				{

					final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
					final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
					s.setDESPurchase(false);
					s.setOptional(true);
					sa.setSlot(s);
					l2.add(sa);
				}
				l.add(new MultiEvent(l2));
			}
			return l;
		}));
		legendItems.add(make("DES, open, multi", () -> {

			final List<Object> l = new LinkedList<>();
			{
				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final DischargeSlot s = CargoFactory.eINSTANCE.createDischargeSlot();
				s.setFOBSale(false);
				s.setOptional(true);
				sa.setSlot(s);
				l.add(sa);
			}
			{
				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final DischargeSlot s = CargoFactory.eINSTANCE.createDischargeSlot();
				s.setFOBSale(false);
				s.setOptional(false);
				sa.setSlot(s);
				l.add(sa);
			}
			// Multiple DES's
			{

				final List<Object> l2 = new LinkedList<>();
				{

					final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
					final DischargeSlot s = CargoFactory.eINSTANCE.createDischargeSlot();
					s.setFOBSale(false);
					s.setOptional(true);
					sa.setSlot(s);
					l2.add(sa);
				}
				{

					final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
					final DischargeSlot s = CargoFactory.eINSTANCE.createDischargeSlot();
					s.setFOBSale(false);
					s.setOptional(true);
					sa.setSlot(s);
					l2.add(sa);
				}
				l.add(new MultiEvent(l2));
			}
			return l;
		}));
		legendItems.add(make("Mixed FOB/DES", () -> {

			final List<Object> l = new LinkedList<>();
			{

				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
				s.setDESPurchase(false);
				s.setOptional(true);
				sa.setSlot(s);
				l.add(sa);
			}
			{

				final OpenSlotAllocation sa = ScheduleFactory.eINSTANCE.createOpenSlotAllocation();
				final LoadSlot s = CargoFactory.eINSTANCE.createLoadSlot();
				s.setDESPurchase(true);
				s.setOptional(true);
				sa.setSlot(s);
				l.add(sa);
			}
			return new MultiEvent(l);
		}));

		// Restore sort mode settings
		{
			final String v = memento.getString(SchedulerViewConstants.SortMode);
			if (v != null) {
				viewerComparator.setMode(Mode.valueOf(v));
			}
		}
		{
			final String v = memento.getString(SchedulerViewConstants.SortCategory);
			if (v != null) {
				viewerComparator.setCategory(Category.valueOf(v));
			}
		}

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
				return Objects.equals(a, b);
			}
		});

		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control. This is in the
		// format of pluginid.contextId
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_ScheduleChart");

		makeActions();
		hookContextMenu();
		contributeToActionBars();

		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, scenarioComparisonService);

		final String colourScheme = memento.getString(SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME);
		labelProvider.setScheme(colourScheme);

		scenarioComparisonService.triggerListener(selectedScenariosServiceListener, false);
	}

	private IColorManager createGanttColourManager() {
		return new DefaultColorManager() {

			@Override
			public boolean useAlphaDrawing() {
				return true;
			}

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
	}

	private ISettings createGanttSettings() {
		return new AbstractSettings() {
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
				return 0;
			}

			@Override
			public int getEventsBottomSpacer() {
				return 0;
			}

			@Override
			public boolean showDeleteMenuOption() {
				return false;
			}

			@Override
			public boolean showMenuItemsOnRightClick() {
				return true;
			}

			@Override
			public boolean showDefaultMenuItemsOnEventRightClick() {
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
	}

	@Override
	public void dispose() {

		// final ESelectionService service =
		// getSite().getService(ESelectionService.class);
		// service.removePostSelectionListener(this);

		// stop this view from listening to preference changes
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		prefs.removePreferenceChangeListener(this);

		super.dispose();
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(SchedulerView.this::fillContextMenu);
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
		manager.add(toggleLegend);
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
		packAction = new PackAction(viewer.getGanttChart());

		if (!Activator.getDefault().getPreferenceStore().getBoolean(SCHEDULER_VIEW_HIDE_COLOUR_SCHEME_ACTION)) {

			colourSchemeAction = new ColourSchemeAction(this, (EMFScheduleLabelProvider) (viewer.getLabelProvider()), viewer);
		}

		highlightAction = new HighlightAction(this, viewer, (EMFScheduleLabelProvider) (viewer.getLabelProvider()));

		toggleLegend = new RunnableAction("Legend", SWT.CHECK, () -> {
			final boolean b = viewer.getGanttChart().getGanttComposite().isShowLegend();
			viewer.getGanttChart().getGanttComposite().setShowLegend(!b);
			viewer.getGanttChart().getGanttComposite().redraw();
		});
		toggleLegend.setChecked(viewer.getGanttChart().getGanttComposite().isShowLegend());

		sortModeAction = new SortModeAction(this, viewer, (EMFScheduleLabelProvider) viewer.getLabelProvider(), viewerComparator);

		saveFullImageAction = new SaveFullImageAction(viewer.getGanttChart());

		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.SAVE_AS.getId(), saveFullImageAction);

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
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

	private class SortModeAction extends SchedulerViewAction {

		private final ScenarioViewerComparator comparator;

		public SortModeAction(final SchedulerView schedulerView, final GanttChartViewer viewer, final EMFScheduleLabelProvider lp, final ScenarioViewerComparator comparator) {
			super("Sort", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
			this.comparator = comparator;
			setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Sort, IconMode.Enabled));
		}

		@Override
		public void run() {

			// Step through modes
			ScenarioViewerComparator.Mode mode = comparator.getMode();
			final int nextMode = (mode.ordinal() + 1) % ScenarioViewerComparator.Mode.values().length;
			mode = ScenarioViewerComparator.Mode.values()[nextMode];
			comparator.setMode(mode);

			viewer.setInput(viewer.getInput());
		}

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
				a.setToolTipText(mode.getTooltip());

				final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
				actionContributionItem.fill(menu, -1);

				// Set initially checked item.
				if (comparator.getMode() == mode) {
					a.setChecked(true);
				}
			}

			{
				final Separator actionContributionItem = new Separator();
				actionContributionItem.fill(menu, -1);
			}
			for (final ScenarioViewerComparator.Category category : ScenarioViewerComparator.Category.values()) {

				final Action a = new Action(category.getDisplayName(), IAction.AS_RADIO_BUTTON) {
					@Override
					public void run() {
						comparator.setCategory(category);
						viewer.setInput(viewer.getInput());
					}
				};
				a.setToolTipText(category.getTooltip());

				final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
				actionContributionItem.fill(menu, -1);

				// Set initially checked item.
				if (comparator.getCategory() == category) {
					a.setChecked(true);
				}
			}
		}
	}

	private final HashMap<Object, Object> equivalents = new HashMap<>();
	private final HashSet<Object> contents = new HashSet<>();

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
	private List<Object> expandSelection(final Collection<Object> selectedObjects) {
		final Set<Object> newSelection = new HashSet<>(selectedObjects.size());
		for (final Object o : selectedObjects) {
			newSelection.add(o);
			if (o instanceof final Slot<?> slot) {
				final Object object = equivalents.get(slot);
				if (object instanceof final SlotVisit slotVisit) {
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
		return new ArrayList<>(newSelection);

	}

	/**
	 * Call from {@link IScenarioInstanceElementCollector#beginCollecting()} to reset pin mode data
	 * 
	 */
	private void clearPinModeData() {
		clearInputEquivalents();
		numberOfSchedules = 0;
	}

	private IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			protected Collection<EObject> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule) {

				numberOfSchedules++;
				return Collections.singleton(schedule);
			}

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);
				SchedulerView.this.clearPinModeData();
			}

			@Override
			protected Collection<EObject> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean isPinned) {
				numberOfSchedules++;
				return Collections.singleton(schedule);
			}
		};
	}

	@Override
	public void preferenceChange(final PreferenceChangeEvent event) {
		viewer.setInput(viewer.getInput());
	}

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		public void selectedDataProviderChanged(final ISelectedDataProvider selectedDataProvider, final boolean block) {
			ViewerHelper.runIfViewerValid(viewer, block, () -> {
				SchedulerView.this.currentSelectedDataProvider = selectedDataProvider;

				final ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();

				final List<Object> rowElements = new LinkedList<>();
				final IScenarioInstanceElementCollector elementCollector = getElementCollector();
				elementCollector.beginCollecting(pinned != null);
				if (pinned != null) {
					rowElements.addAll(elementCollector.collectElements(pinned, true));

				}
				for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {
					rowElements.addAll(elementCollector.collectElements(other, false));
				}
				elementCollector.endCollecting();
				ViewerHelper.setInput(viewer, true, rowElements);

				selectedObjectChanged(null, new StructuredSelection(expandSelection(selectedDataProvider.getSelectedObjects())));

				viewer.getGanttChart().getGanttComposite().setTodaySupplier(null);
				if (pinned != null) {
					final LNGScenarioModel scenarioModel = pinned.getTypedRoot(LNGScenarioModel.class);
					if (scenarioModel != null) {
						final LocalDate today = scenarioModel.getPromptPeriodStart();
						if (today != null) {
							viewer.getGanttChart().getGanttComposite().setTodaySupplier(() -> {
								final Calendar cal = Calendar.getInstance();
								cal.setTimeInMillis(today.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000L);
								return cal;
							});
						}
					}
				} else {
					for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {

						final LNGScenarioModel scenarioModel = other.getTypedRoot(LNGScenarioModel.class);
						if (scenarioModel != null) {
							final LocalDate today = scenarioModel.getPromptPeriodStart();
							if (today != null) {
								viewer.getGanttChart().getGanttComposite().setTodaySupplier(() -> {
									final Calendar cal = Calendar.getInstance();
									cal.setTimeInMillis(today.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000L);
									return cal;
								});
								break;
							}
						}
					}
				}

			});
		}

		@Override
		public void diffOptionChanged(final EDiffOption d, final Object oldValue, final Object newValue) {
			if (d == EDiffOption.FILTER_SCHEDULE_CHART_BY_SELECTION) {
				selectionManager.setViewerSelection(viewer.getSelection(), false);
			}
		}

		@Override
		public void selectedObjectChanged(@Nullable final MPart source, @NonNull ISelection selection) {

			if (selection instanceof final IStructuredSelection sel) {
				List<Object> objects = new ArrayList<>(sel.toList().size());
				for (final Object o : sel.toList()) {
					if (o instanceof final CargoAllocation allocation) {
						objects.addAll(allocation.getEvents());
						for (final SlotAllocation sa : allocation.getSlotAllocations()) {
							objects.add(sa.getSlotVisit());
						}
					} else if (o instanceof final Cargo cargo) {
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
		}
	};

	private ILegendItem make(final String lbl, final Supplier<Object> object) {
		return make(lbl, object.get());
	}

	private ILegendItem make(final String lbl, final Object object) {

		if (object instanceof final List<?> l) {
			final List<Object> l2 = new LinkedList<>();
			for (final var e : l) {
				final GanttEvent ge = new GanttEvent(SchedulerView.this.viewer.getGanttChart(), "");
				final Rectangle b = new Rectangle(0, 0, 1, 12);
				ge.setBounds(b);

				ge.setSpecialDrawMode(viewer.getSpecialDrawMode((ILabelProvider) viewer.getLabelProvider(), e));
				ge.setStatusBorderColor(viewer.getLabelProviderBorderColor((ILabelProvider) viewer.getLabelProvider(), e));
				ge.setStatusColor(viewer.getLabelProviderColor((ILabelProvider) viewer.getLabelProvider(), e));
				ge.setStatusForegroundColour(viewer.getLabelProviderForegroundColour((ILabelProvider) viewer.getLabelProvider(), e));
				l2.add(ge);
			}

			return new ILegendItem() {

				@Override
				public Object getItem() {
					return l2;
				}

				@Override
				public String getDescription() {
					return lbl;
				}

				@Override
				public Color[] getColours() {
					return new Color[0];
				}
			};
		}

		final GanttEvent ge = new GanttEvent(SchedulerView.this.viewer.getGanttChart(), "");
		final Rectangle b = new Rectangle(0, 0, 1, 12);
		ge.setBounds(b);

		ge.setSpecialDrawMode(viewer.getSpecialDrawMode((ILabelProvider) viewer.getLabelProvider(), object));
		ge.setStatusBorderColor(viewer.getLabelProviderBorderColor((ILabelProvider) viewer.getLabelProvider(), object));
		ge.setStatusColor(viewer.getLabelProviderColor((ILabelProvider) viewer.getLabelProvider(), object));
		ge.setStatusForegroundColour(viewer.getLabelProviderForegroundColour((ILabelProvider) viewer.getLabelProvider(), object));

		return new ILegendItem() {

			@Override
			public Object getItem() {
				return ge;
			}

			@Override
			public String getDescription() {
				return lbl;
			}

			@Override
			public Color[] getColours() {
				return new Color[0];
			}
		};
	}

}