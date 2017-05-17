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
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;
import com.mmxlabs.lingo.reports.views.changeset.InsertionPlanGrouperAndFilter.GroupMode;

public class SwitchGroupByModeHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute(MPart activePart, MItem  activeMenu) {

		SwitchGroupModeEvent event = new SwitchGroupModeEvent();
		event.activePart = activePart;

		for (String tag : activeMenu.getTags()) {
			if ("groupby_target".contentEquals(tag)) {
				event.mode = GroupMode.Target;
				break;
			}
			if ("groupby_target_and_complexity".contentEquals(tag)) {
				event.mode = GroupMode.TargetAndComplexity;
				break;
			}
			if ("groupby_complexity".contentEquals(tag)) {
				event.mode = GroupMode.Complexity;
				break;
			}
		}
		if (event.mode != null) {
			eventBroker.post(ChangeSetViewEventConstants.EVENT_SWITCH_GROUP_BY_MODE, event);
		}
	}

}