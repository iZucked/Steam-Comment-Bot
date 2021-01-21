/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.VesselAvailabilityTopLevelComposite;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link VesselAvailabilityTopLevelComposite} application.
 * 
 * @author Simon Goodall
 * 
 */
public class FullVesselCharterCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new VesselAvailabilityTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit) {
			@Override
			public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject object, Collection<EObject> range, EMFDataBindingContext dbc) {
				// Unwrap child object
				if (object instanceof FullVesselCharterOption) {
					object = ((FullVesselCharterOption) object).getVesselCharter();
				}
				super.display(dialogContext, root, object, range, dbc);
			}
		};
	}
}
