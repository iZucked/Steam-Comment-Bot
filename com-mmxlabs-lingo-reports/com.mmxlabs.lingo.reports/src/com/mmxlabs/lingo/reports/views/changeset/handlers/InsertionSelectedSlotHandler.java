/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;
import com.mmxlabs.lingo.reports.views.changeset.GetCurrentAnalyticsSolution;
import com.mmxlabs.lingo.reports.views.changeset.GetCurrentTargetObject;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.NamedObject;

public class InsertionSelectedSlotHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute(final MPart activePart) {
		eventBroker.post(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_INSERTION_CHANGES, activePart);
	}

	@AboutToShow
	public void aboutToShow(final List<MMenuElement> items, final EModelService modelService, final MPart activePart) {

		final AnalyticsSolution solution = (AnalyticsSolution) ContextInjectionFactory.invoke(activePart.getObject(), GetCurrentAnalyticsSolution.class, null);
		final NamedObject lastSlot = (NamedObject) ContextInjectionFactory.invoke(activePart.getObject(), GetCurrentTargetObject.class, null);
		if (solution != null && solution.getSolution() instanceof SlotInsertionOptions) {
			final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) solution.getSolution();
			if (slotInsertionOptions != null && slotInsertionOptions.getSlotsInserted().size() > 1) {
				MMenu parent;
				{
					final MMenu dynamicItem = modelService.createModelElement(MMenu.class);
					dynamicItem.setElementId("focus.on");
					dynamicItem.setLabel("Focus on...");
					items.add(dynamicItem);
					parent = dynamicItem;
				}
				for (final Slot slot : slotInsertionOptions.getSlotsInserted()) {
					final MDirectMenuItem dynamicItem = modelService.createModelElement(MDirectMenuItem.class);
					dynamicItem.setElementId("focus.on." + slot.getName());
					dynamicItem.setLabel(slot.getName());
					dynamicItem.setContributionURI("bundleclass://com.mmxlabs.lingo.reports/com.mmxlabs.lingo.reports.views.changeset.handlers.SelectSlotHandler");
					dynamicItem.getTags().add("slot-" + slot.getName());
					if (lastSlot == slot) {
						dynamicItem.setSelected(true);
					}
					dynamicItem.setType(ItemType.RADIO);
					parent.getChildren().add(dynamicItem);
				}
			}
		}

	}

}