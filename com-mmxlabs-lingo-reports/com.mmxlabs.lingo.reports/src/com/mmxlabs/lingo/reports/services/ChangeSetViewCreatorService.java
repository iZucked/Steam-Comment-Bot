/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.List;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.views.changeset.OpenChangeSetHandler;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.rcp.common.RunnerHelper;

public class ChangeSetViewCreatorService {

	public static final String ChangeSetViewCreatorService_Topic = "create-change-set-view";

	private static final Logger log = LoggerFactory.getLogger(ChangeSetViewCreatorService.class);
	private final EventHandler eventHandler = new EventHandler() {

		@Override
		public void handleEvent(final Event event) {
			try {
				final AnalyticsSolution solution = (AnalyticsSolution) event.getProperty(IEventBroker.DATA);
				openView(solution);
			} catch (final Exception e) {
				log.error("Error handling create change set view event", e);
			}
		}

	};

	private IEventBroker eventBroker;

	public void start() {

		new Thread() {
			@Override
			public void run() {
				while (!PlatformUI.isWorkbenchRunning()) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Here the workbench has been marked as running. But it may not be really.
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=49316
				// This suggests running on the display thread to guarantee this.
				// Note: The previous code got to a point where the workbench service locator was null during an ITS run.
				RunnerHelper.asyncExec(() -> {
					eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
					eventBroker.subscribe(ChangeSetViewCreatorService_Topic, eventHandler);
				});
			};
		}.start();
	}

	public void stop() {
		if (eventBroker != null) {
			eventBroker.unsubscribe(eventHandler);
			eventBroker = null;
		}
	}

	private void openView(final AnalyticsSolution solution) {
		final EPartService partService = PlatformUI.getWorkbench().getService(EPartService.class);
		final EModelService modelService = PlatformUI.getWorkbench().getService(EModelService.class);
		final MApplication application = PlatformUI.getWorkbench().getService(MApplication.class);

		final String viewPartId = "com.mmxlabs.lingo.reports.views.changeset.CustomChangeSetView:" + solution.getID();

		RunnerHelper.asyncExec(() -> {
			boolean foundPerspective = false;
			final List<MPerspective> perspectives = modelService.findElements(application, null, MPerspective.class, null);
			for (final MPerspective p : perspectives) {
				if (p.getElementId().equals("com.mmxlabs.lingo.reports.diff.DiffPerspective")) {
					partService.switchPerspective(p);
					foundPerspective = true;
					break;
				}
			}
			if (!foundPerspective) {
				// Fallback to eclipse 3.x API to open perspective
				try {
					PlatformUI.getWorkbench().showPerspective("com.mmxlabs.lingo.reports.diff.DiffPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				} catch (final WorkbenchException e) {
					log.error("Unable to open compare perspective", e);
				}
			}
			MPart viewPart = partService.findPart(viewPartId);
			if (viewPart != null) {
				viewPart = partService.showPart(viewPart, PartState.ACTIVATE); // Show part
			} else {

				final MPartStack stack = (MPartStack) modelService.find("reportsArea", application);
				final MPart part = modelService.createModelElement(MPart.class);
				part.setElementId(viewPartId);
				part.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.ChangeSetView");
				part.setCloseable(true);
				part.getTags().add(EPartService.REMOVE_ON_HIDE_TAG);
				part.getTags().add("action-set");
				part.getTags().add("disable-event-handlers");
				part.setLabel(solution.getTitle());

				final MToolBar toolbar = modelService.createModelElement(MToolBar.class);
				toolbar.setElementId(viewPartId + ".toolbar");
				part.setToolbar(toolbar);

				{
					final MDirectToolItem item = modelService.createModelElement(MDirectToolItem.class);
					item.setElementId(viewPartId + ".directtoolitem.copy");
					item.setType(ItemType.CHECK);
					item.setTooltip("Copy to clipboard");
					item.setLabel("Filter Non Structural Changes");
					item.setIconURI("platform:/plugin/com.mmxlabs.rcp.common/icons/copy.gif");
					item.setContributionURI("bundleclass://com.mmxlabs.rcp.common/com.mmxlabs.rcp.common.handlers.CopyToHtmlClipboardHandler");
					item.setEnabled(true);
					item.setToBeRendered(true);
					item.setVisible(true);
					toolbar.getChildren().add(item);
				}
				if (solution.isCreateInsertionOptions()) {

					final MMenu menu = modelService.createModelElement(MMenu.class);
					menu.setEnabled(true);
					menu.setToBeRendered(true);
					menu.setVisible(true);

					{
						final MDirectToolItem item = modelService.createModelElement(MDirectToolItem.class);
						item.setElementId(viewPartId + ".directtoolitem.filter_menu");
						item.setType(ItemType.PUSH);
						item.setTooltip("Change filters");
						item.setLabel("Filter...");
						item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupByModeHandler");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setSelected(true);
						item.setMenu(menu);

						toolbar.getChildren().add(item);
					}

					{
						final MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
						item.setElementId(viewPartId + ".directtoolitem.toggle_insertion");
						item.setType(ItemType.CHECK);
						item.setTooltip("Toggle Insertion Plan Filter");
						item.setLabel("Toggle Insertion Plan Filter");
						item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.ToggleFilterInsertionPlansHandler");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setSelected(true);
						menu.getChildren().add(item);
					}
					{
						final MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
						item.setElementId(viewPartId + ".directtoolitem.filternonstructuralchanges");
						item.setType(ItemType.CHECK);
						item.setLabel("Filter Non Structural Changes");
						item.setTooltip("Toggling filtering of non structural changes");
						item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.ToggleShowStructuralChangesHandler");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setSelected(true);
						menu.getChildren().add(item);
					}
				} else {
					{
						final MDirectToolItem item = modelService.createModelElement(MDirectToolItem.class);
						item.setElementId(viewPartId + ".directtoolitem.filternonstructuralchanges");
						item.setType(ItemType.CHECK);
						item.setLabel("Filter Non Structural Changes");
						item.setTooltip("Toggling filtering of non structural changes");
						item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.ToggleShowStructuralChangesHandler");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						toolbar.getChildren().add(item);
					}
				}
				if (solution.isCreateDiffToBaseAction()) {
					final MDirectToolItem item = modelService.createModelElement(MDirectToolItem.class);
					item.setElementId(viewPartId + ".directtoolitem.comparetobase");
					item.setType(ItemType.CHECK);
					item.setLabel("Compare to Base");
					item.setTooltip("Toggle compare to base");
					item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/compare_to_base.gif");
					item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.ToggleDiffToBaseHandler");
					item.setEnabled(true);
					item.setToBeRendered(true);
					item.setVisible(true);
					item.setSelected(true);
					toolbar.getChildren().add(item);
				}

				if (solution.isCreateInsertionOptions()) {
					final MMenu menu = modelService.createModelElement(MMenu.class);
					menu.setEnabled(true);
					menu.setToBeRendered(true);
					menu.setVisible(true);

					{
						final MDirectToolItem item = modelService.createModelElement(MDirectToolItem.class);
						item.setElementId(viewPartId + ".directtoolitem.groupby_menu");
						item.setType(ItemType.PUSH);
						item.setTooltip("Change the grouping choice");
						item.setLabel("Group by");
						item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/group.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupByModeHandler");
						item.setMenu(menu);
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						toolbar.getChildren().add(item);
					}
					{
						final MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
						item.setElementId(viewPartId + ".directtoolitem.group_menu.groupby.target_and_complexity");
						item.setType(ItemType.PUSH);
						item.setTooltip("Groups first by source or destination, then the the change complexity");
						item.setLabel("Target and complexity");
						// item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupByModeHandler");
						item.getTags().add("groupby_target_and_complexity");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setType(ItemType.RADIO);

						menu.getChildren().add(item);
						// This is the default state
						item.setSelected(true);
					}
					{
						final MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
						item.setElementId(viewPartId + ".directtoolitem.group_menu.groupby.target");
						item.setType(ItemType.PUSH);
						item.setTooltip("Group by source or destination");
						item.setLabel("Target");
						// item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupByModeHandler");
						item.getTags().add("groupby_target");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setType(ItemType.RADIO);

						menu.getChildren().add(item);

					}
					{
						final MDirectMenuItem item = modelService.createModelElement(MDirectMenuItem.class);
						item.setElementId(viewPartId + ".directtoolitem.group_menu.groupby.complexity");
						item.setType(ItemType.PUSH);
						item.setTooltip("Group by change complexity");
						item.setLabel("Complexity");
						// item.setIconURI("platform:/plugin/com.mmxlabs.lingo.reports/icons/filter.gif");
						item.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SwitchGroupByModeHandler");
						item.getTags().add("groupby_complexity");
						item.setEnabled(true);
						item.setToBeRendered(true);
						item.setVisible(true);
						item.setType(ItemType.RADIO);

						menu.getChildren().add(item);

					}

					part.getTags().add("ScenarioUUID-" + solution.getScenarioInstance().getUuid());
					part.getTags().add("SolutionUUID-" + solution.getSolution().getUuid());
				}

				stack.getChildren().add(part); // Add part to stack
				viewPart = partService.showPart(part, PartState.ACTIVATE); // Show part
			}

			// Pass in the input data
			final Object viewObject = viewPart.getObject();
			final IEclipseContext componentContext = EclipseContextFactory.create();
			componentContext.set(AnalyticsSolution.class, solution);

			ContextInjectionFactory.invoke(viewObject, OpenChangeSetHandler.class, componentContext);

		});
	}

}
