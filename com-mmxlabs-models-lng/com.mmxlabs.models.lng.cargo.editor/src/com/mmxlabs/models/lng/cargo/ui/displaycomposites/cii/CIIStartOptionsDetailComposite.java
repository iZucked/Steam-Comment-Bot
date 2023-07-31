/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CIIStartOptionsDetailComposite extends DefaultDetailComposite {

		public CIIStartOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
			super(parent, style, toolkit);
		}
		
		@Override
		public void createControls(IDialogEditingContext dialogContext, MMXRootObject root, EObject object, EMFDataBindingContext dbc) {
			super.createControls(dialogContext, root, object, dbc);
		}

		@Override
		protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
			return new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE, "YTD Distance", 75) //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS, "YTD Emissions", 130) //
					.makeRow() //
					.make() //
			;
		}
}
