/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class OpenViewHandler {

	@Inject
	EPartService partService;

	@Execute
	public void execute(@Named("view.id") String viewId) {

		partService.showPart(viewId, PartState.ACTIVATE);
	}
}
