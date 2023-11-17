/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.preferences.PreferenceConstants;
import com.mmxlabs.lingo.reports.services.EquivalentsManager;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceViewWithUndoSupport;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.ENinteyDayNonShippedRotationSelection;
import com.mmxlabs.widgets.schedulechart.EventSize;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.providers.ILegendItem;
import com.mmxlabs.widgets.schedulechart.providers.ScheduleChartProviders;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BalastIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BalastJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.DryDockEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LateLoadEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LoadEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.MaintenanceEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.BuySellDrawableScheduleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionStateType;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionType;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceClassification;


public class NinetyDayScheduleReport extends ScenarioInstanceViewWithUndoSupport implements IPreferenceChangeListener {

	private Action zoomInAction;
	private Action zoomOutAction;
	private Action packAction;
	private NinetyDayScheduleLabelMenuAction labelMenuAction;
	private NinetyDayScheduleFilterMenuAction filterMenuAction;
	private NinetyDayScheduleSortMenuAction sortMenuAction;
	private Action showAnnotationsAction;
	private Action showLegendAction;
	private RunnableAction gotoPreferences;
	
	private static final Set<String> IGNORED_PREFERENCES = new HashSet<>();

	static {
		IGNORED_PREFERENCES.add(PreferenceConstants.P_REPORT_DURATION_FORMAT);
	}

	
	private ScheduleChartViewer<NinetyDayScheduleInput> viewer;
	private IMemento memento;
	private NinetyDayScheduleChartSettings settings;
	
	private final EquivalentsManager equivalentsManager = new EquivalentsManager();
	private NinetyDayScheduleEventProvider eventProvider;
	private NinetyDayScheduleChartSortingProvider sortingProvider;
	private NinetyDayDrawableEventProvider drawableEventProvider;
	private NinetyDayDrawableEventTooltipProvider drawableEventTooltipProvider;
	private NinetyDayScheduleEventStylingProvider eventStylingProvider;
	private NinetyDayDrawableEventLabelProvider eventLabelProvider;
	private NinetyDayDrawableLegendProvider drawableLegendProvider;
	private NinetyDayScheduleChartRowsDataProvider scheduleChartRowsDataProvider;
	
	private NinetyDayScheduleModelUpdater modelUpdater;
	
	private @NonNull ENinteyDayNonShippedRotationSelection rotationSelection = ENinteyDayNonShippedRotationSelection.OFF;
	private final @NonNull Set<Contract> fobRotationSelectedContracts = new HashSet<>();
	private final @NonNull Set<Predicate<NonShippedSequence>> fobRotationsToShow = new HashSet<>();
	
	private @Nullable ScenarioComparisonService scenarioComparisonService;
	private ReentrantSelectionManager selectionManager;
	protected final ISelectedScenariosServiceListener scenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
			ViewerHelper.runIfViewerValid(viewer, block, () -> {
				if (selectedDataProvider != null) {
					ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();
					List<ScenarioResult> other = selectedDataProvider.getAllScenarioResults().stream().filter(f -> f != null && !f.equals(pinned)).toList();
					settings.sethasMultipleScenarios(pinned != null ? !other.isEmpty() : other.size() > 1);
					
					clearFobRotations();
					viewer.typedSetInput(new NinetyDayScheduleInput(pinned, other));
					viewer.getCanvas().getTimeScale().pack();
				}
			});
		}
		
		@Override
		public void selectedObjectChanged(@Nullable MPart source, ISelection selection) {
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
				objects = equivalentsManager.expandSelection(objects);
				selection = new StructuredSelection(objects);
			}
			ViewerHelper.setSelection(viewer, true, selection);
		}
		
	};

	@Override
	public void init(IViewSite viewSite, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}

		this.memento = memento;
		this.settings = new NinetyDayScheduleChartSettings();
		
		setupPreferenceListener();

		this.scheduleChartRowsDataProvider = new NinetyDayScheduleChartRowsDataProvider();
		this.eventProvider = new NinetyDayScheduleEventProvider(equivalentsManager, settings, this.scheduleChartRowsDataProvider, this);
		this.sortingProvider = new NinetyDayScheduleChartSortingProvider();
		this.drawableEventProvider = new NinetyDayDrawableEventProvider();
		this.drawableEventTooltipProvider = new NinetyDayDrawableEventTooltipProvider();
		this.eventStylingProvider = new NinetyDayScheduleEventStylingProvider();
		this.eventLabelProvider = new NinetyDayDrawableEventLabelProvider(memento, settings);
		this.modelUpdater = new NinetyDayScheduleModelUpdater(this::getDefaultCommandHandler);
		this.drawableLegendProvider = new NinetyDayDrawableLegendProvider(getLegendItems());

		super.init(viewSite, memento);
	}

	@Override
	public void saveState(final IMemento memento) {
		memento.putMemento(this.memento);
		super.saveState(memento);
	}

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new ScheduleChartViewer<>(parent, new ScheduleChartProviders(eventLabelProvider, drawableEventProvider, drawableEventTooltipProvider, sortingProvider, scheduleChartRowsDataProvider, drawableLegendProvider), eventProvider, modelUpdater, settings);
		this.scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);
		selectionManager = new ReentrantSelectionManager(viewer, scenariosServiceListener, scenarioComparisonService);
		
		
		makeActions();
		contributeToActionBars();
		makeUndoActions();
		
		try {
			scenarioComparisonService.triggerListener(scenariosServiceListener, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		listenToScenarioSelection();
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(zoomInAction);
		manager.add(zoomOutAction);
		manager.add(labelMenuAction);
		manager.add(filterMenuAction);
		manager.add(sortMenuAction);
		manager.add(showAnnotationsAction);
		manager.add(packAction);
		
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
	}
	
	private void fillLocalPullDown(final IMenuManager manager) {
		manager.add(gotoPreferences);
		manager.add(showLegendAction);
	}

	private void makeActions() {
		zoomInAction = new ZoomAction(true, viewer.getCanvas());
		zoomOutAction = new ZoomAction(false, viewer.getCanvas());
		packAction = new PackAction(viewer.getCanvas());
		labelMenuAction = new NinetyDayScheduleLabelMenuAction(this, eventLabelProvider);
		filterMenuAction = new NinetyDayScheduleFilterMenuAction(this, viewer.getCanvas(), settings);
		sortMenuAction = new NinetyDayScheduleSortMenuAction(this, sortingProvider);
		showAnnotationsAction = new ShowAnnotationsAction(viewer.getCanvas(), settings);
		showLegendAction = new ShowLegendAction(viewer.getCanvas(), settings);
		gotoPreferences = new RunnableAction("Preferences", () -> {
			PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, "com.mmxlabs.lingo.reports.preferences.ReportsPreferencesPage",
					new String[] { "com.mmxlabs.lingo.reports.preferences.ReportsPreferencesPage" }, null);
			dialog.open();
		});
	}

	@Override
	public void dispose() {
		if (scenarioComparisonService != null) {
			scenarioComparisonService.removeListener(scenariosServiceListener);
		}
		super.dispose();
	}
	
	public void redraw() {
		viewer.getCanvas().redraw();
	}
	
	private static class ZoomAction extends Action {
		
		private final boolean zoomIn;
		private final ScheduleCanvas canvas;
		
		public ZoomAction(final boolean zoomIn, final ScheduleCanvas canvas) {
			super();
			this.zoomIn = zoomIn;
			this.canvas = canvas;

			setText(zoomIn ? "Zoom In" : "Zoom Out");
			CommonImages.setImageDescriptors(this, zoomIn ? IconPaths.ZoomIn : IconPaths.ZoomOut);
		}
		
		@Override
		public void run() {
			if (zoomIn) {
				canvas.getTimeScale().zoomIn();
			} else {
				canvas.getTimeScale().zoomOut();
			}
			canvas.redraw();
		}
	}
	
	private static class PackAction extends Action {
		private final ScheduleCanvas canvas;
		
		public PackAction(final ScheduleCanvas canvas) {
			super();
			this.canvas = canvas;

			setText("Fit");
			CommonImages.setImageDescriptors(this, IconPaths.Pack);
		}
		
		@Override
		public void run() {
			canvas.getTimeScale().pack();
			canvas.redraw();
		}
		
	}
	
	private static class ShowAnnotationsAction extends Action {
		private final ScheduleCanvas canvas;
		private final NinetyDayScheduleChartSettings settings;
		
		public ShowAnnotationsAction(final ScheduleCanvas canvas, final NinetyDayScheduleChartSettings settings) {
			super();
			this.canvas = canvas;
			this.settings = settings;

			setText("Show Annotations");
			CommonImages.setImageDescriptors(this, IconPaths.Lateness);
		}
		
		@Override
		public void run() {
			settings.setShowAnnotations(!settings.showAnnotations());
			canvas.redraw();
		}
		
	}
	
	private static class ShowLegendAction extends Action {
		private final ScheduleCanvas canvas;
		private final NinetyDayScheduleChartSettings settings;
		
		public ShowLegendAction(final ScheduleCanvas canvas, final NinetyDayScheduleChartSettings settings) {
			super();
			this.canvas = canvas;
			this.settings = settings;

			setText("Show Legend");
		}
		
		@Override
		public void run() {
			settings.setShowLegend(!settings.showLegend());
			canvas.redraw();
		}
		
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		final String preferenceKey = event.getKey();
		if (preferenceKey != null) {
			if (preferenceKey.equals(PreferenceConstants.P_SCHEDULE_CHART_EVENT_LABEL_FONT_SIZE)) {
				final Object newValueObj = event.getNewValue();
				if (newValueObj == null) {
					// default case - small
					settings.setEventSizing(EventSize.SMALL);
					eventLabelProvider.reToggleLabel();
					viewer.typedSetInput(viewer.getInput());
				} else if (newValueObj instanceof String newValue) {
					final EventSize fontSize = switch (newValue) {
					case "MEDIUM" -> EventSize.MEDIUM;
					case "LARGE" -> EventSize.LARGE;
					default -> EventSize.SMALL;
					};
					settings.setEventSizing(fontSize);
					eventLabelProvider.reToggleLabel();
					viewer.typedSetInput(viewer.getInput());
				}
			} else if (preferenceKey.equals(PreferenceConstants.P_SCHEDULE_CHART_NUM_DAY_OVERRIDE_FORMAT)) {
				// viewer.getGanttChart().getGanttComposite().resetEventLabels();
				viewer.typedSetInput(viewer.getInput());
			} else if (!IGNORED_PREFERENCES.contains(preferenceKey)){
				viewer.typedSetInput(viewer.getInput());
			}
		}
	}
	
	private void setupPreferenceListener() {
		// make sure this viewer is listening to preference changes
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.mmxlabs.lingo.reports");
		prefs.addPreferenceChangeListener(this);
				
		// Get current font size from preferences
		final EventSize fontSize = switch (prefs.get(PreferenceConstants.P_SCHEDULE_CHART_EVENT_LABEL_FONT_SIZE, "")) {
		case "MEDIUM" -> EventSize.MEDIUM;
		case "LARGE" -> EventSize.LARGE;
		default -> EventSize.SMALL;
		};
		settings.setEventSizing(fontSize );
	}
	
	private List<ILegendItem> getLegendItems(){
		List<ILegendItem> legendItems = new ArrayList<>();
		legendItems.add(new DefaultLegendItem("Laden travel/idle", (se, b) -> List.of(
				new LadenJourneyEvent(se, b, true),
				new LadenIdleEvent(se, b, true)
				)));
		legendItems.add(new DefaultLegendItem("Balast travel/idle", (se, b) -> List.of(
				new BalastJourneyEvent(se, b, true), 
				new BalastIdleEvent(se, b, true)
				)));
		legendItems.add(new DefaultLegendItem("Port visit, late", (se, b) -> List.of(
				new LoadEvent(se, b, true),
				new LateLoadEvent(se, b, true)
				)));
		legendItems.add(new DefaultLegendItem("Charter out real/generated", (se, b) -> List.of(
				new CharterOutEvent(se, b, true), 
				new GeneratedCharterOutEvent(se, b, true)
				)));
		legendItems.add(new DefaultLegendItem("Dry-dock/Maintenance", (se, b) -> List.of(
				new DryDockEvent(se, b, true),
				new MaintenanceEvent(se, b, true)
				)));
		legendItems.add(new DefaultLegendItem("Charter Length real/generated", (se, b) -> List.of(
				new CharterLengthEvent(se, b, true),
				new GeneratedCharterLengthEvent(se, b, true)
				)));
		legendItems.add(new BuySellLegendItem("FOB, open, multi", (se, b) -> List.of(
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPTIONAL, PositionType.FOB, false),
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPEN, PositionType.FOB, false),
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPTIONAL, PositionType.FOB, true)
				)));
		legendItems.add(new BuySellLegendItem("DES, open, multi", (se, b) -> List.of(
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPTIONAL, PositionType.DES, false),
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPEN, PositionType.DES, false),
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.OPTIONAL, PositionType.DES, true)
				)));
		legendItems.add(new BuySellLegendItem("Mixed FOB/DES", (se, b) -> List.of(
				new BuySellDrawableScheduleEvent(se, b, true, true, PositionStateType.PAIRED, PositionType.MIXED, true)
				)));
		
		return legendItems;
	}
	
	public ScheduleChartViewer<NinetyDayScheduleInput> getViewer() {
		return viewer;
	}
	
	public boolean toggleSelectedContract(final Contract selectedContract) {
		if (fobRotationSelectedContracts.contains(selectedContract)) {
			fobRotationSelectedContracts.remove(selectedContract);
			return false;
		} else {
			fobRotationSelectedContracts.add(selectedContract);
			return true;
		}
	}

	public Set<Contract> getSelectedContracts() {
		return fobRotationSelectedContracts;
	}

	public ENinteyDayNonShippedRotationSelection getFobRotationOptionSelection() {
		return rotationSelection;
	}

	public void setFobRotationOptionSelection(final @NonNull ENinteyDayNonShippedRotationSelection rotationSelection) {
		this.rotationSelection = rotationSelection;
	}
	
	public Set<Predicate<NonShippedSequence>> getFobRotationsToShow(){
		return fobRotationsToShow;
	}

	public void clearFobRotations() {
		setFobRotationOptionSelection(ENinteyDayNonShippedRotationSelection.OFF);
		fobRotationsToShow.clear();
		fobRotationSelectedContracts.clear();
	}

	public void replaceFobRotations(final @NonNull Collection<@NonNull Predicate<NonShippedSequence>> predicates) {
		fobRotationsToShow.clear();
		fobRotationsToShow.addAll(predicates);
	}

}
