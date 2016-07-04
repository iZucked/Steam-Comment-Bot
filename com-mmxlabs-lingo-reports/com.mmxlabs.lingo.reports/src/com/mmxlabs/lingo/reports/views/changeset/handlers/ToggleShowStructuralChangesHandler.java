/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;

public class ToggleShowStructuralChangesHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute(MPart activePart) {
		eventBroker.post(ChangeSetViewEventConstants.EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES, activePart);
	}

}