/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CharterInMarketDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	public CharterInMarketDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<EStructuralFeature> orderedFeatures = Lists.newArrayList( //
				MMXCorePackage.Literals.NAMED_OBJECT__NAME, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__ENTITY, //
				SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__NOMINAL, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__GENERIC_CHARTER_CONTRACT, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MIN_DURATION, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MAX_DURATION, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_AT, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_HEEL_CV, //
				SpotMarketsPackage.Literals.CHARTER_IN_MARKET__END_AT //

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
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET__ENABLED, "Active") //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE) //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__ENTITY) //
				.makeRow() //
				//
				.withRow() //
				.withLabel("Duration") //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MIN_DURATION, 32) //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MAX_DURATION) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__NOMINAL) //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM) //
				.makeRow() //
				 //
				.withRow() //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_AT) //
				.withFeature(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_HEEL_CV, "Heel CV") //
				.makeRow() //
				.make() //
		;

	}

}
