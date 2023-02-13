/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class EndHeelOptionsDetailComposite extends DefaultDetailComposite {

	public EndHeelOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<EStructuralFeature> orderedFeatures = Lists.newArrayList( //
				CommercialPackage.Literals.END_HEEL_OPTIONS__MINIMUM_END_HEEL, //
				CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION,//
				CommercialPackage.Literals.END_HEEL_OPTIONS__MAXIMUM_END_HEEL,//
				CommercialPackage.Literals.END_HEEL_OPTIONS__TANK_STATE
		);
		// Reverse the list so that we can move the editors to the head of the list
		Collections.reverse(orderedFeatures);
		for (var feature : orderedFeatures) {
			for (var editor : editors) {
				if (editor.getFeature() == feature) {
					editors.remove(editor);
					editors.add(0, editor);
					break;
				}
			}
		}

	}
	
	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(CommercialPackage.Literals.END_HEEL_OPTIONS__MINIMUM_END_HEEL, 75) //
				.withFeature(CommercialPackage.Literals.END_HEEL_OPTIONS__MAXIMUM_END_HEEL, 75) //
				.makeRow()
				.withRow()
				.withFeature(CommercialPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION, "   Price", 130) //
				.withFeature(CommercialPackage.Literals.END_HEEL_OPTIONS__TANK_STATE, "   Tank State",  130) //
				.makeRow()
				.make();

	}

}