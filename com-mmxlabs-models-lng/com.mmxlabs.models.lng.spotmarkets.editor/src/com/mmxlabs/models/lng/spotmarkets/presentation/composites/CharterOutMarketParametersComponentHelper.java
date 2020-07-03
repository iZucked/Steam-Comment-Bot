/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for CharterOutMarketParameters instances
 *
 * @generated
 */
public class CharterOutMarketParametersComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterOutMarketParametersComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterOutMarketParametersComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using CharterOutMarketParameters as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_charterOutStartDateEditor(detailComposite, topClass);
		add_charterOutEndDateEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the charterOutStartDate feature on CharterOutMarketParameters
	 *
	 * @generated
	 */
	protected void add_charterOutStartDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE));
	}
	/**
	 * Create the editor for the charterOutEndDate feature on CharterOutMarketParameters
	 *
	 * @generated
	 */
	protected void add_charterOutEndDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE));
	}
}