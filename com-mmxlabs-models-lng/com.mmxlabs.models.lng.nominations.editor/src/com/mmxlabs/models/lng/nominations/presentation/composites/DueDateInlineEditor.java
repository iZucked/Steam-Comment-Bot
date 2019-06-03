package com.mmxlabs.models.lng.nominations.presentation.composites;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;

public class DueDateInlineEditor extends LocalDateInlineEditor {

	public DueDateInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}
	
	@Override
	protected Object getValue() {
		if (this.input instanceof AbstractNomination) {
			return NominationsModelUtils.getDueDate((LNGScenarioModel)this.rootObject, (AbstractNomination)this.input);
		}
		else {
			return super.getValue();
		}
	}
}
