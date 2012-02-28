/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PricingPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for CharterCostModel instances
 *
 * @generated
 */
public class CharterCostModelComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterCostModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterCostModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using CharterCostModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.CHARTER_COST_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselsEditor(detailComposite, topClass);
		add_charterInPriceEditor(detailComposite, topClass);
		add_charterOutPriceEditor(detailComposite, topClass);
		add_spotCharterCountEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vessels feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_vesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.CHARTER_COST_MODEL__VESSELS));
	}
	/**
	 * Create the editor for the charterInPrice feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_charterInPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.CHARTER_COST_MODEL__CHARTER_IN_PRICE));
	}
	/**
	 * Create the editor for the charterOutPrice feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_charterOutPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.CHARTER_COST_MODEL__CHARTER_OUT_PRICE));
	}
	/**
	 * Create the editor for the spotCharterCount feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_spotCharterCountEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT));
	}
}