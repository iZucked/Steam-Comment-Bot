/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

/**
 * Composite which is being shown on the dialog window
 * Allows editing the Settle Strategies instance
 * @author FM
 *
 */
public class SettleStrategyDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	public SettleStrategyDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<ETypedElement> orderedFeatures = Lists.newArrayList( //
				MMXCorePackage.Literals.NAMED_OBJECT__NAME, //
				
				PricingPackage.Literals.SETTLE_STRATEGY__DAY_OF_THE_MONTH,//
				PricingPackage.Literals.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH,//
				
				PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD,//
				PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT,//
				
				PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR	
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
				.withFeature(MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(PricingPackage.Literals.SETTLE_STRATEGY__DAY_OF_THE_MONTH, "Starts settling on") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(PricingPackage.Literals.SETTLE_STRATEGY__LAST_DAY_OF_THE_MONTH, "Last day") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD, "Period") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_PERIOD_UNIT, "Unit") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(PricingPackage.Literals.SETTLE_STRATEGY__SETTLE_START_MONTHS_PRIOR, "Starts") //
				.makeRow() //
				.make() //
		;

	}

}
