/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public abstract class ADPComposite extends Composite {

	public ADPComposite(Composite parent, int style) {
		super(parent, style);
	}

	public abstract void refresh();

	public abstract void updateRootModel(@Nullable LNGScenarioModel scenarioModel, ADPModel adpModel);

}
