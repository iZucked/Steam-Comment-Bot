/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
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
 * A component helper for CharterInMarketOverride instances
 *
 * @generated
 */
public class CharterInMarketOverrideComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterInMarketOverrideComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterInMarketOverrideComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.VESSEL_ASSIGNMENT_TYPE));
	}
	
	/**
	 * add editors to a composite, using CharterInMarketOverride as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_charterInMarketEditor(detailComposite, topClass);
		add_spotIndexEditor(detailComposite, topClass);
		add_startHeelEditor(detailComposite, topClass);
		add_startDateEditor(detailComposite, topClass);
		add_endPortEditor(detailComposite, topClass);
		add_endDateEditor(detailComposite, topClass);
		add_endHeelEditor(detailComposite, topClass);
		add_includeBallastBonusEditor(detailComposite, topClass);
		add_minDurationEditor(detailComposite, topClass);
		add_maxDurationEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the charterInMarket feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_charterInMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET));
	}
	/**
	 * Create the editor for the spotIndex feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_spotIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX));
	}

	/**
	 * Create the editor for the startHeel feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_startHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__START_HEEL));
	}
	/**
	 * Create the editor for the startDate feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_startDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__START_DATE));
	}
	/**
	 * Create the editor for the endPort feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_endPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_PORT));
	}
	/**
	 * Create the editor for the endDate feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_endDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_DATE));
	}
	/**
	 * Create the editor for the endHeel feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_endHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_HEEL));
	}
	/**
	 * Create the editor for the includeBallastBonus feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_includeBallastBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS));
	}
	/**
	 * Create the editor for the minDuration feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_minDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION));
	}
	/**
	 * Create the editor for the maxDuration feature on CharterInMarketOverride
	 *
	 * @generated
	 */
	protected void add_maxDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION));
	}
}