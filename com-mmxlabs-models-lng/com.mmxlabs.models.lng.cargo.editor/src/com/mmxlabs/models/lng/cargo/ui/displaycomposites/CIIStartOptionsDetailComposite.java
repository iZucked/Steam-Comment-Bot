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

//		@Override
//		protected void sortEditors(List<IInlineEditor> editors) {
//			// Sub classes can sort the editor list prior to rendering
//			List<EStructuralFeature> orderedFeatures = Lists.newArrayList( //
//					CommercialPackage.Literals.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE, //
//					CommercialPackage.Literals.START_HEEL_OPTIONS__PRICE_EXPRESSION,//
//					CommercialPackage.Literals.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE,//
//					CommercialPackage.Literals.START_HEEL_OPTIONS__CV_VALUE
//			);
//			// Reverse the list so that we can move the editors to the head of the list
//			Collections.reverse(orderedFeatures);
//			for (var feature : orderedFeatures) {
//				for (var editor : editors) {
//					if (editor.getFeature() == feature) {
//						editors.remove(editor);
//						editors.add(0, editor);
//						break;
//					}
//				}
//			}
//
//		}
		
		@Override
		protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

			return new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TODAY_DISTANCE, 75) //
					.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS, "   Emissions", 130) //
					.makeRow() //
					.make() //
			;

		}
}
