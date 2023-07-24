/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CIIStartOptionsDetailComposite extends DefaultDetailComposite {

		public CIIStartOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
			super(parent, style, toolkit);
		}

		@Override
		protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

			return new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TODAY_DISTANCE, "YTD Distance", 75) //
					.makeRow() //
					.withRow() //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS, "YTD Emissions", 130) //
					.makeRow() //
					.make() //
			;

		}
}
