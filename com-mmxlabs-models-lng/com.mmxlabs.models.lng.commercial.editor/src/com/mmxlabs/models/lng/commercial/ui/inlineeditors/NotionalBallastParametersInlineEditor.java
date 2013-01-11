/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.inlineeditors;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class NotionalBallastParametersInlineEditor extends DialogInlineEditor {
	private MMXRootObject rootObject;

	private IScenarioEditingLocation location;

	public NotionalBallastParametersInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IScenarioEditingLocation location, MMXRootObject context, EObject input, Collection<EObject> range) {
		this.rootObject = context;
		this.location = location;
		super.display(location, context, input, range);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {

		final DetailCompositeDialog dcd = new DetailCompositeDialog(getShell(), commandHandler);

		dcd.setReturnDuplicates(false);

		dcd.open(location, rootObject, input, CommercialPackage.eINSTANCE.getNetbackPurchaseContract_NotionalBallastParameters());

		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}
}
