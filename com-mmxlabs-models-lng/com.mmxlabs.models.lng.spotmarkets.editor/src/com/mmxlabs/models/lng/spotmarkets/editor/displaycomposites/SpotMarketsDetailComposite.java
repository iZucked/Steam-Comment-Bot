/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.displaycomposites;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

/**
 * @author Simon Goodall
 * 
 */
public class SpotMarketsDetailComposite extends DefaultDetailComposite {

	private final DetailGroup contractDetailGroup;

	public enum DetailGroup {
		GENERAL, RESTRICTIONS, AVAILABILITY, PRICING
	}

	public SpotMarketsDetailComposite(final Composite parent, final int style, final DetailGroup contractDetailGroup, final FormToolkit toolkit) {
		super(parent, style, toolkit);
		this.contractDetailGroup = contractDetailGroup;
		layoutProvider = createLayoutProvider();
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {
		if (editor == null) {
			return null;
		}
		// By default all elements are in the main tab
		DetailGroup cdg = DetailGroup.GENERAL;

		// Here the exceptions are listed for the elements which should go into the
		// bottom
		if (editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedContractsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedPortsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedVesselsArePermissive() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedContracts() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedPorts() //
				|| editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_RestrictedVessels() //
		) {
			cdg = DetailGroup.RESTRICTIONS;
		}
		if (editor.getFeature() == SpotMarketsPackage.eINSTANCE.getSpotMarket_Availability() //
		) {
			cdg = DetailGroup.AVAILABILITY;
		}

		// Do not add elements if they are for the wrong section.
		if (contractDetailGroup != cdg) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		if (contractDetailGroup == DetailGroup.GENERAL) {

			return new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withLabel("Volume") //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY, 80) //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__MAX_QUANTITY, 80) //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__VOLUME_LIMITS_UNIT) //
					.makeRow() //
					//
					.make();
		}
		if (contractDetailGroup == DetailGroup.RESTRICTIONS) {

			return new RowGroupDisplayCompositeLayoutProviderBuilder() //
					.withRow() //
					.withLabel("Contracts") //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS) //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE) //
					.makeRow() //
					//
					.withRow() //
					.withLabel("Ports") //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS) //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE) //
					.makeRow() //
					//
					//
					.withRow() //
					.withLabel("Vessels") //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS) //
					.withFeature(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE) //
					.makeRow() //
					//
					.make();
		}
		return super.createLayoutProvider();
	}
}
