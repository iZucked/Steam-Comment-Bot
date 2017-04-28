/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;

public class ToggleFilterInsertionPlansHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute(MPart activePart) {
		eventBroker.post(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_INSERTION_CHANGES, activePart);
	}

}