/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;

import com.mmxlabs.models.lng.analytics.ui.liveeval.ILiveEvaluatorService;
import com.mmxlabs.rcp.common.ServiceHelper;

public class LiveEvaluatorStateEditorActionDelegate extends ActionDelegate implements IEditorActionDelegate {

	private static final String TOOLTIP_ACTIVE = "Auto evaluate is on";
	private static final String TOOLTIP_INACTIVE = "Auto evaluate is off";
	
	@Override
	public void run(final IAction action) {

		ServiceHelper.withOptionalServiceConsumer(ILiveEvaluatorService.class, service -> {
			if (service != null) {
				final boolean oldValue = service.isLiveEvaluatorEnabled();
				service.setLiveEvaluatorEnabled(!oldValue);
				action.setChecked(!oldValue);
				if (action.isChecked()) {
					action.setToolTipText(TOOLTIP_ACTIVE);
				} else {
					action.setToolTipText(TOOLTIP_INACTIVE);
				}
			}
		});
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		ServiceHelper.withOptionalServiceConsumer(ILiveEvaluatorService.class, service -> {
			if (service != null) {
				final boolean oldValue = service.isLiveEvaluatorEnabled();
				action.setChecked(oldValue);
				if (action.isChecked()) {
					action.setToolTipText(TOOLTIP_ACTIVE);
				} else {
					action.setToolTipText(TOOLTIP_INACTIVE);
				}
			}
		});
	}

}
