/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetViewEventConstants;

public class ExpandAllHandler {

	@Inject
	private IEventBroker eventBroker;

	@Execute
	public void execute() {
		eventBroker.post(ChangeSetViewEventConstants.EVENT_EXPAND_ALL, null);
	}

}