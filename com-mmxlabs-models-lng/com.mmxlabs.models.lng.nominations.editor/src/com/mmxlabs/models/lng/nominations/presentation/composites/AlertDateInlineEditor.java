/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;

public class AlertDateInlineEditor extends LocalDateInlineEditor {

	public AlertDateInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}
	
	@Override
	protected Object getValue() {
		if (this.input instanceof AbstractNomination) {
			return NominationsModelUtils.getAlertDate((LNGScenarioModel)this.rootObject, (AbstractNomination)this.input);
		}
		else {
			return super.getValue();
		}
	}
}
