/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MItem;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;

public class SelectSlotHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute(final MPart activePart, final MItem activeMenu) {

		final SwitchSlotEvent event = new SwitchSlotEvent();
		event.activePart = activePart;

		for (final String tag : activeMenu.getTags()) {
			if (tag.startsWith("slot-")) {
				event.slotId = tag.replaceFirst("slot-", "");
				eventBroker.post(ChangeSetViewEventConstants.EVENT_SWITCH_TARGET_SLOT, event);
				break;
			}
		}
	}

}