/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;

public class NomineeDateInlineEditor extends LocalDateInlineEditor {

	public NomineeDateInlineEditor(final EStructuralFeature feature) {
		super(feature);
		Label label = this.getLabel();
		if (label != null) {
			label.setText("Date");
		}
		//Make readonly.
		this.setEditorEnabled(false);
	}
	
	@Override
	protected Object getValue() {
		if (this.input instanceof AbstractNomination) {
			return NominationsModelUtils.getDate((LNGScenarioModel)this.rootObject, (AbstractNomination)this.input);
		}
		else {
			return super.getValue();
		}
	}
}
