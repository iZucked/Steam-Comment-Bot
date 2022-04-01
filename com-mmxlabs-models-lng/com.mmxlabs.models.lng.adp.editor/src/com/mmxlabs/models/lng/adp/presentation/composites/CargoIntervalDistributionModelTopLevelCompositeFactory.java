/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CargoIntervalDistributionModelTopLevelCompositeFactory extends DefaultDisplayCompositeFactory {
 
	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {

		final DefaultDetailComposite s = (DefaultDetailComposite) super.createSublevelComposite(parent, eClass, dialogContext, toolkit);
		
		s.setLayoutProvider(new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL__QUANTITY, 32) //
				.withFeature(ADPPackage.Literals.CARGO_INTERVAL_DISTRIBUTION_MODEL__INTERVAL_TYPE) //
				.makeRow() //
				.make() //
				);

		return s;
	}

}
