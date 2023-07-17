/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * A report which displays the cooldowns in the selected schedules.
 * 
 * @author hinton
 * 
 */
public class CooldownReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CooldownReportView";

	private static final IconPaths CELL_IMAGE_GREEN_ARROW_DOWN = IconPaths.GreenArrowDown;
	private static final IconPaths CELL_IMAGE_RED_ARROW_UP = IconPaths.RedArrowUp;
	private static final IconPaths CELL_IMAGE_DARK_ARROW_DOWN = IconPaths.DarkArrowDown;
	private static final IconPaths CELL_IMAGE_DARK_ARROW_UP = IconPaths.DarkArrowUp;
	private static final IconPaths RED_PLUS = IconPaths.RedPlus;
	private static final IconPaths GREEN_LINE = IconPaths.GreenLine;

	private static final String CHANGE_MARKER = "• ";

	private Action toggleDeltaAction;
	private ActionContributionItem toggleDeltaAci;
	private Boolean multipleScenarios;

	private Set<Cooldown> pinnedCooldownsSet = new HashSet<>();
	private Set<String> otherCooldownsIDSet = new HashSet<>();
	private Map<String, Cooldown> pinnedCooldownsMap = new HashMap<>();

	public CooldownReportView() {
		super("com.mmxlabs.lingo.doc.Reports_Cooldown");
		multipleScenarios = false;
		addColumn("scheduleNONDELTA", "Schedule", ColumnType.NONDELTAMULTIPLE, containingScheduleFormatter);
		addColumn("vessel", "Vessel", ColumnType.NORMAL, new DefaultTooltipFormatter() {
			public String render(final Object object) {
				if (object instanceof Cooldown cooldown) {
					StringBuilder sb = new StringBuilder();
					String vesselName = ((Sequence) cooldown.eContainer()).getName();
					if (pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
						if (!((Sequence) pinnedCooldownsMap.get(idFromCooldown(cooldown)).eContainer()).getName().equals(vesselName)) {
							sb.append(CHANGE_MARKER);
						}
					}
					sb.append(vesselName);
					return sb.toString();
				}
				return "";
			}

			@Override
			public String getToolTipText(Object object) {
				if (object instanceof Cooldown cooldown && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
					Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
					String pinnedCooldownVesselName = ((Sequence) pinnedCooldown.eContainer()).getName();
					String cooldownVesselName = ((Sequence) cooldown.eContainer()).getName();
					if (!Objects.equals(pinnedCooldownVesselName, cooldownVesselName)) {
						StringBuilder sb = new StringBuilder();
						sb.append("Before: ");
						sb.append(pinnedCooldownVesselName);
						sb.append("\nAfter: ");
						sb.append(cooldownVesselName);
						return sb.toString();
					}
				}
				return super.getToolTipText(object);
			}
		});
		addColumn("causeid", "Cause ID", ColumnType.NORMAL, new DefaultTooltipFormatter() {
			@Override
			public String render(final Object object) {
				if (object instanceof Cooldown cooldown) {
					StringBuilder sb = new StringBuilder();
					String causeID = causeIDFromCooldown(cooldown);
					if (getDeltaMode() && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && !pinnedCooldownsSet.contains(cooldown)) {
						final Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
						String pinnedCooldownCauseID = causeIDFromCooldown(pinnedCooldown);
						if (!(causeID == null || pinnedCooldownCauseID == null || pinnedCooldownCauseID.equals(causeID))) {
							sb.append("• ");
						}
					}
					sb.append(causeID);
					return sb.toString();
				}

				return "";
			}

			@Override
			public String getToolTipText(Object object) {
				if (object instanceof Cooldown cooldown && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
					Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
					String pinnedCooldownCauseID = causeIDFromCooldown(pinnedCooldown);
					String causeID = causeIDFromCooldown(cooldown);
					if (!causeID.equals(pinnedCooldownCauseID)) {
						StringBuilder sb = new StringBuilder();
						sb.append("Before: ");
						sb.append(pinnedCooldownCauseID);
						sb.append("\nAfter: ");
						sb.append(causeID);
						return sb.toString();
					}
				}
				return super.getToolTipText(object);
			}
		});

		addColumn("id", "ID", ColumnType.NORMAL, new AdaptiveIDFormatter());

		addColumn("date", "Date", ColumnType.NORMAL, new AdaptiveDateFormatter());
		addColumn("port", "Port", ColumnType.NORMAL, new DefaultTooltipFormatter() {
			public String render(final Object object) {
				if (object instanceof Cooldown cooldown) {
					StringBuilder sb = new StringBuilder();
					final Port port = cooldown.getPort();
					final String portName = port.getName();
					if (getDeltaMode() && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && !pinnedCooldownsSet.contains(cooldown)) {
						final Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
						final Port pinnedCooldownPort = pinnedCooldown.getPort();
						final String pinnedCooldownPortName = pinnedCooldownPort.getName();
						if (!pinnedCooldownPortName.equals(portName)) {
							sb.append("• ");
						}
					}
					sb.append(portName);
					return sb.toString();
				}
				return "";
			}

			public String getToolTipText(Object object) {
				if (object instanceof Cooldown cooldown && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
					Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
					Port pinnedCooldownPort = pinnedCooldown.getPort();
					Port cooldownPort = cooldown.getPort();
					if (!cooldownPort.getName().equals(pinnedCooldownPort.getName())) {
						StringBuilder sb = new StringBuilder();
						sb.append("Before: ");
						sb.append(pinnedCooldownPort.getName());
						sb.append("\nAfter: ");
						sb.append(cooldownPort.getName());
						return sb.toString();
					}
				}
				return super.getToolTipText(object);
			}
		});
		addColumn("cost", "Cost", ColumnType.NORMAL, new AdaptiveCostFormatter());

		getBlockManager().makeAllBlocksVisible();
		renderColumns();

	}

	private class DefaultTooltipFormatter extends DefaultToolTipProvider implements ICellRenderer {
		@Override
		public boolean isValueUnset(Object object) {
			return false;
		}

		@Override
		public Comparable getComparable(final Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(final Object object) {
			return getComparable(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
			return null;
		}

		@Override
		public String getToolTipText(Object element) {
			return null;
		}

		@Override
		public @Nullable String render(Object object) {
			return null;
		}
	}

	private class AdaptiveIDFormatter extends DefaultTooltipFormatter implements IImageProvider {
		@Override
		public String render(Object obj) {
			if (obj instanceof Cooldown cooldown) {
				return idFromCooldown(cooldown);
			}
			return "";
		}

		@Override
		public Image getImage(Object obj) {
			if (obj instanceof Cooldown cooldown && getDeltaMode()) {
				if (pinnedCooldownsSet.contains(cooldown)) {
					return CommonImages.getImage(GREEN_LINE, IconMode.Enabled);
				}
				return pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) ? null : CommonImages.getImage(RED_PLUS, IconMode.Enabled);
			}
			return null;
		}
	}

	private class AdaptiveDateFormatter extends DefaultTooltipFormatter implements IImageProvider {

		public String render(final Object object) {
			if (object instanceof Cooldown cooldown) {
				return Formatters.asDateTimeFormatterNoTz.render(cooldown.getStart());
			}
			return "";
		}

		@Override
		public Image getImage(Object object) {
			if (object instanceof Cooldown cooldown) {
				ZonedDateTime date = cooldown.getStart();
				if (getDeltaMode() && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && !pinnedCooldownsSet.contains(cooldown)) {
					final Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
					final ZonedDateTime pinnedCooldownDate = pinnedCooldown.getStart();
					if (date.compareTo(pinnedCooldownDate) > 0) {
						return CommonImages.getImage(CELL_IMAGE_DARK_ARROW_UP, IconMode.Enabled);
					} else if (date.compareTo(pinnedCooldownDate) < 0) {
						return CommonImages.getImage(CELL_IMAGE_DARK_ARROW_DOWN, IconMode.Enabled);
					}
				}
			}
			return null;
		}

		public String getToolTipText(Object object) {
			if (object instanceof Cooldown cooldown && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
				Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
				ZonedDateTime cooldownDate = cooldown.getStart();
				ZonedDateTime pinnedCooldownDate = pinnedCooldown.getStart();
				if (!cooldownDate.equals(pinnedCooldownDate)) {
					StringBuilder sb = new StringBuilder();
					sb.append("Before: ");
					sb.append(Formatters.asDateTimeFormatterNoTz.render(pinnedCooldownDate));
					sb.append("\nAfter: ");
					sb.append(Formatters.asDateTimeFormatterNoTz.render(cooldownDate));
					return sb.toString();
				}
			}
			return super.getToolTipText(object);
		}
	}

	private class AdaptiveCostFormatter extends DefaultTooltipFormatter implements IImageProvider {

		public Integer getIntValue(Object object) {

			if (object instanceof Cooldown cooldown) {
				if (getDeltaMode() && !pinnedCooldownsSet.contains(cooldown) && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown))) {
					final Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
					return cooldown.getCost() - pinnedCooldown.getCost();
				} else {
					return cooldown.getCost();
				}
			}
			return null;
		}

		@Override
		public Image getImage(Object object) {
			if (object instanceof Cooldown cooldown && getDeltaMode()) {
				if (pinnedCooldownsSet.contains(cooldown)) {
					return CommonImages.getImage(CELL_IMAGE_GREEN_ARROW_DOWN, IconMode.Enabled);
				} else if (pinnedCooldownsMap.containsKey(idFromCooldown(cooldown))) {
					if (getIntValue(object) > 0) {
						return CommonImages.getImage(CELL_IMAGE_RED_ARROW_UP, IconMode.Enabled);
					} else if (getIntValue(object) < 0) {
						return CommonImages.getImage(CELL_IMAGE_GREEN_ARROW_DOWN, IconMode.Enabled);
					}
				} else {
					return CommonImages.getImage(CELL_IMAGE_RED_ARROW_UP, IconMode.Enabled);
				}
			}
			return null;
		}

		@Override
		public String render(final Object object) {
			if (object == null) {
				return "";
			}
			final Integer x = getIntValue(object);
			if (x == null) {
				return "";
			}
			if (x != 0 || !getDeltaMode()) {
				return String.format("%,d", Integer.max(x, -x));
			} else {
				return "";
			}
		}

		public String getToolTipText(Object object) {
			if (object instanceof Cooldown cooldown && pinnedCooldownsMap.containsKey(idFromCooldown(cooldown)) && getDeltaMode() && !pinnedCooldownsSet.contains(cooldown)) {
				Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(cooldown));
				int cooldownCost = cooldown.getCost();
				int pinnedCooldownCost = pinnedCooldown.getCost();
				if (cooldownCost != pinnedCooldownCost) {
					StringBuilder sb = new StringBuilder();
					sb.append("Before: $");
					sb.append(String.format("%,d", pinnedCooldownCost));
					sb.append("\nAfter: $");
					sb.append(String.format("%,d", cooldownCost));
					return sb.toString();
				} else {
					return "$".concat(String.format("%,d", cooldownCost));
				}
			} else if (object instanceof Cooldown cooldown) {
				return "$".concat(String.format("%,d", cooldown.getCost()));
			}
			return super.getToolTipText(object);
		}
	}

	private void renderColumns() {
		ColumnBlockManager blockManager = getBlockManager();
		for (ColumnBlock block : blockManager.getBlocksInVisibleOrder()) {
			block.setViewState(multipleScenarios, multipleScenarios, getDeltaMode());
		}
	}

	@Override
	protected void renderColumns(boolean isMultiple, boolean pinDiffMode) {
		for (final ColumnBlock handler : getBlockManager().getBlocksInVisibleOrder()) {
			if (handler != null) {
				handler.setViewState(isMultiple, pinDiffMode, getDeltaMode());
			}
		}
	}

	private String idFromCooldown(Cooldown cooldown) {
		final Sequence sequence = (Sequence) cooldown.eContainer();
		final int index = sequence.getEvents().indexOf(cooldown) + 1;
		final Event after = sequence.getEvents().get(index);

		return after.name();
	}

	private String causeIDFromCooldown(Cooldown cooldown) {
		String causeID = null;

		final Sequence sequence = (Sequence) cooldown.eContainer();
		int index = sequence.getEvents().indexOf(cooldown) - 1;

		while (index >= 0) {
			final Event before = sequence.getEvents().get(index);

			if ((before instanceof SlotVisit) || (before instanceof VesselEventVisit) || (before instanceof StartEvent)) {
				return before.name();
			}

			index--;
		}
		return null;
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(final Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(final Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				clearInputEquivalents();
				return superProvider.getElements(inputElement);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	private boolean areCooldownsIdentical(Cooldown c1, Cooldown c2) {
		if (c1 == c2) {
			return true;
		}
		String c1ID = idFromCooldown(c1);
		String c2ID = idFromCooldown(c2);
		if (!c1ID.equals(c2ID)) {
			return false;
		}
		int c1Cost = c1.getCost();
		int c2Cost = c2.getCost();
		if (c1Cost != c2Cost) {
			return false;
		}
		ZonedDateTime c1DateTime = c1.getStart();
		ZonedDateTime c2DateTime = c2.getStart();
		if (!c1DateTime.equals(c2DateTime)) {
			return false;
		}
		String c1CauseID = causeIDFromCooldown(c1);
		String c2CauseID = causeIDFromCooldown(c2);
		if (!c1CauseID.equals(c2CauseID)) {
			return false;
		}
		String c1VesselName = ((Sequence) c1.eContainer()).getName();
		String c2VesselName = ((Sequence) c1.eContainer()).getName();
		if (!c1VesselName.equals(c2VesselName)) {
			return false;
		}
		String c1PortName = c1.getPort().getName();
		String c2PortName = c2.getPort().getName();
		if (!c1PortName.equals(c2PortName)) {
			return false;
		}
		return true;
	}

	@Override
	protected List<Object> getRowElements(ISelectedDataProvider selectedDataProvider) {
		final List<Object> rowElements = new LinkedList<>();
		final IScenarioInstanceElementCollector elementCollector = getElementCollector();
		pinnedCooldownsMap = new HashMap<>();
		pinnedCooldownsSet = new HashSet<>();
		otherCooldownsIDSet = new HashSet<>();
		ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();

		Collection<ScenarioResult> others = selectedDataProvider.getOtherScenarioResults();
		if (pinned == null || others.isEmpty()) {
			if (getDeltaMode()) {
				setDeltaMode(false);
			}
			toggleDeltaAction.setEnabled(false);
			toggleDeltaAci.setVisible(false);
			getViewSite().getActionBars().getToolBarManager().update(true);
		} else {
			toggleDeltaAction.setEnabled(true);
			toggleDeltaAci.setVisible(true);
			getViewSite().getActionBars().getToolBarManager().update(true);
		}
		if (!getDeltaMode()) {
			elementCollector.beginCollecting(pinned != null);
			if (pinned != null) {
				final Collection<? extends Object> elements = elementCollector.collectElements(pinned, true);
				rowElements.addAll(elements);
			}
			for (final ScenarioResult other : others) {
				final Collection<? extends Object> elements = elementCollector.collectElements(other, false);
				rowElements.addAll(elements);
			}
			multipleScenarios = (pinned != null && !others.isEmpty()) || others.size() > 1;
			elementCollector.endCollecting();
		} else {
			elementCollector.beginCollecting(pinned != null);
			Collection<? extends Object> elements = new ArrayList<>();
			if (pinned != null) {
				elements = elementCollector.collectElements(pinned, true);
				pinnedCooldownsMap = elements.stream()//
						.filter(Cooldown.class::isInstance)//
						.map(Cooldown.class::cast)//
						.collect(Collectors.toMap(this::idFromCooldown, c -> c));
				pinnedCooldownsSet = elements.stream()//
						.filter(Cooldown.class::isInstance)//
						.map(Cooldown.class::cast)//
						.collect(Collectors.toSet());
			}
			if (!others.isEmpty()) {
				for (final ScenarioResult other : others) {
					final Collection<? extends Object> otherElements = elementCollector.collectElements(other, false);
					final List<@NonNull Cooldown> otherCooldowns = otherElements.stream()//
							.filter(Cooldown.class::isInstance)//
							.map(Cooldown.class::cast)//
							.filter((Cooldown t) -> {
								if (pinnedCooldownsMap.containsKey(idFromCooldown(t))) {
									Cooldown pinnedCooldown = pinnedCooldownsMap.get(idFromCooldown(t));
									return !areCooldownsIdentical(t, pinnedCooldown);
								} else {
									return true;
								}
							}).toList();

					otherCooldownsIDSet = (otherElements.stream()//
							.filter(Cooldown.class::isInstance)//
							.map(Cooldown.class::cast)//
							.map(this::idFromCooldown)//
							.collect(Collectors.toSet()));
					rowElements.addAll(otherCooldowns);
				}
			}
			if (pinned != null) {
				rowElements.addAll(elements.stream().filter(Cooldown.class::isInstance).map(Cooldown.class::cast).filter((Cooldown c) -> !otherCooldownsIDSet.contains(idFromCooldown(c))).toList());
			}
			multipleScenarios = (pinned != null && !others.isEmpty()) || others.size() > 1;
		}
		return rowElements;
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector() {

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				clearPinModeData();
				super.beginCollecting(pinDiffMode);
			}

			@Override
			protected Collection<EObject> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {

				final Collection<EObject> collectedElements = super.collectElements(scenarioResult, scenarioModel, schedule, pinned);
				final List<EObject> elements = new ArrayList<>(collectedElements);
				if (!toggleDeltaAction.isChecked()) {
					collectPinModeElements(elements, pinned);
				}
				return collectedElements;
			}

			@Override
			protected boolean filter(final Event event) {
				return (event instanceof Cooldown);
			}

			@Override
			protected boolean filter() {
				return true;
			}
		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	public ColumnHandler addColumn(final String blockID, final String title, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		final ColumnBlock block = getBlockManager().createBlock(blockID, title, columnType);
		return getBlockManager().createColumn(block, title).withCellRenderer(formatter).withElementPath(path).build();
	}

	public void clearColumns() {
		ColumnBlockManager columnBlockManager = getBlockManager();
		for (ColumnHandler columnHandler : columnBlockManager.getHandlersInOrder()) {
			columnHandler.block.setUserVisible(false);
		}
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}
		return super.getAdapter(adapter);
	}

	@Override
	protected void processInputs(final Object[] result) {

		for (final Object obj : result) {
			if (obj instanceof Cooldown) {
				// Not managed to get this working correctly. Probably need to find the previous
				// slot?
				// setInputEquivalents(row, CollectionsUtil.makeArrayList(row,
				// row.getPreviousEvent(), row.getNextEvent()));
			}
		}
	}

	@Override
	protected void fillLocalToolBar(final IToolBarManager manager) {
		super.fillLocalToolBar(manager);

		manager.add(new GroupMarker("delta"));

		manager.appendToGroup("delta", toggleDeltaAci);

	}

	public boolean toggleDeltaMode() {
		setDeltaMode(!getDeltaMode());
		return getDeltaMode();
	}

	public void setDeltaMode(boolean aDeltaMode) {
		toggleDeltaAction.setChecked(aDeltaMode);
		updateElements();
		viewer.refresh();

	}

	public boolean getDeltaMode() {
		if (toggleDeltaAction != null) {
			return toggleDeltaAction.isChecked();
		}
		return false;
	}

	@Override
	protected void makeActions() {
		super.makeActions();
		toggleDeltaAction = new CooldownReportViewToggleDeltaAction(this, false, viewer);
		toggleDeltaAci = new ActionContributionItem(toggleDeltaAction);
	}
}
