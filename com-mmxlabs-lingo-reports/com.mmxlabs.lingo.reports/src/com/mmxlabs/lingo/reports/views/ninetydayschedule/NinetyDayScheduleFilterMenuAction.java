/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.Optional;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.EnabledPositionsSequenceProviderTracker;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.ISchedulePositionsSequenceProvider;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.ISchedulePositionsSequenceProviderExtension;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.positionsequences.PositionsSequenceProviderException;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartMode;
import com.mmxlabs.widgets.schedulechart.ScheduleFilterSupport;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class NinetyDayScheduleFilterMenuAction extends NinetyDayScheduleAction {
	
	private static final String NOMINALS_FILTER = "NOMINALS";
	
	private final ScheduleChartViewer<?> viewer;
	private final NinetyDayScheduleChartSettings settings;
	private ScheduleChartMode preToggleMode;
	private Iterable<ISchedulePositionsSequenceProviderExtension> schedulePositionExtensionPoints;
	private final EnabledPositionsSequenceProviderTracker tracker;

	protected NinetyDayScheduleFilterMenuAction(NinetyDayScheduleReport parent, ScheduleChartViewer<?> viewer, NinetyDayScheduleChartSettings settings, Iterable<ISchedulePositionsSequenceProviderExtension> extensions, EnabledPositionsSequenceProviderTracker positionSequenceTracker) {
		super("Filter", AS_DROP_DOWN_MENU, parent);
		this.viewer = viewer;
		this.settings = settings;
		this.tracker = positionSequenceTracker;
		this.preToggleMode = viewer.getCanvas().getScheduleChartMode();
		this.schedulePositionExtensionPoints = extensions;
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
	}
	
	@Override
	public void run() {
		ScheduleChartMode temp = viewer.getCanvas().getScheduleChartMode();
		viewer.getCanvas().setScheduleChartMode(viewer.getCanvas().getScheduleChartMode() == ScheduleChartMode.FILTER ? preToggleMode : ScheduleChartMode.FILTER);
		if (temp != ScheduleChartMode.FILTER) {
			preToggleMode = temp;
			setChecked(true);
		} else {
			setChecked(false);
		}
		viewer.getCanvas().redraw();
	}

	@Override
	protected void createMenuItems(Menu menu) {
		
		for (ISchedulePositionsSequenceProviderExtension ext : schedulePositionExtensionPoints) {
			if (ext.showMenuItem().equals("true")) {
				ISchedulePositionsSequenceProvider provider = ext.createInstance();
				final Action toggleShowPartition = new Action(ext.getName(), SWT.CHECK) {
					@Override
					public void run() {
						Optional<PositionsSequenceProviderException> optError = tracker.toggleVisibilityOrGetError(provider.getId());
						if (optError.isPresent()) {
							PositionsSequenceProviderException e = optError.get();
							MessageDialog dialog = new MessageDialog(menu.getShell(), e.getTitle(), null, e.getDescription(), 0, 0, "OK");
							dialog.create();
							dialog.open();
							return;
						}
						setChecked(tracker.isEnabledWithNoError(provider.getId()));
						viewer.hardRefresh();
					}
				};
				toggleShowPartition.setToolTipText("Partitions unshipped cargoes based on the selected split");
				toggleShowPartition.setChecked(tracker.isEnabledWithNoError(provider.getId()));
				final ActionContributionItem actionContributionItem = new ActionContributionItem(toggleShowPartition);
				actionContributionItem.fill(menu, -1);
			}
		}
		Action showNominals = new Action("Show Nominals", AS_CHECK_BOX) {
			@Override
			public void run() {
				ScheduleFilterSupport fs = viewer.getCanvas().getFilterSupport();
				boolean isShowingNominals = !fs.isFilterActive(NOMINALS_FILTER);
				if (isShowingNominals) {
					fs.applyFilter(NOMINALS_FILTER, key -> key.getData() instanceof Sequence s && s.getSequenceType() == SequenceType.ROUND_TRIP);
				} else {
					fs.removeFilter(NOMINALS_FILTER);
				}
				setChecked(!fs.isFilterActive(NOMINALS_FILTER));
				viewer.getCanvas().redraw();
			}
		};
		showNominals.setChecked(!viewer.getCanvas().getFilterSupport().isFilterActive(NOMINALS_FILTER));
		ActionContributionItem showNominalsACI = new ActionContributionItem(showNominals);
		showNominalsACI.fill(menu, -1);

		final Separator separator = new Separator();
		separator.fill(menu, -1);

		Action showHiddenRows = new Action("Show Hidden Rows", AS_PUSH_BUTTON) {
			@Override
			public void run() {
				viewer.getCanvas().getFilterSupport().showHiddenRows();
				setEnabled(viewer.getCanvas().getFilterSupport().isFiltered());
				viewer.getCanvas().redraw();
			}
		};
		showHiddenRows.setEnabled(viewer.getCanvas().getFilterSupport().isFiltered());
		ActionContributionItem showHiddenRowsACI = new ActionContributionItem(showHiddenRows);
		showHiddenRowsACI.fill(menu, -1);

	}
	
	public void applyDefaultFilters() {
		if (!settings.showNominalsByDefault()) {
			viewer.getCanvas().getFilterSupport().addDefaultFilter(NOMINALS_FILTER, key -> key.getData() instanceof Sequence s && s.getSequenceType() == SequenceType.ROUND_TRIP);
		}
	}

}
