/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

public class DeselectAllCommandHandler extends AbstractHandler {
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		BusyIndicator.showWhile(HandlerUtil.getActiveShellChecked(event).getDisplay(), () -> ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, p -> p.deselectAll(true)));

		return null;
	}
}
