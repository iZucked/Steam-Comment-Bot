/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.PlaceholderInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CharterInMarket instances
 *
 * @generated
 */
public class CharterInMarketComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterInMarketComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterInMarketComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET));
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.VESSEL_ASSIGNMENT_TYPE));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CharterInMarket as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SpotMarketsPackage.Literals.CHARTER_IN_MARKET);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_charterInRateEditor(detailComposite, topClass);
		add_spotCharterCountEditor(detailComposite, topClass);
		add_overrideInaccessibleRoutesEditor(detailComposite, topClass);
		add_inaccessibleRoutesEditor(detailComposite, topClass);
		add_genericCharterContractEditor(detailComposite, topClass);
		add_nominalEditor(detailComposite, topClass);
		add_minDurationEditor(detailComposite, topClass);
		add_maxDurationEditor(detailComposite, topClass);
		add_mtmEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_startAtEditor(detailComposite, topClass);
		add_endAtEditor(detailComposite, topClass);
		add_startHeelCVEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vessel feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL));
	}

	/**
	 * Create the editor for the nominal feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_nominalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__NOMINAL));
	}

	/**
	 * Create the editor for the minDuration feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_minDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MIN_DURATION));
	}

	/**
	 * Create the editor for the maxDuration feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_maxDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MAX_DURATION));
	}

	/**
	 * Create the editor for the mtm feature on CharterInMarket
	 *
	 * @generated NOT
	 */
	protected void add_mtmEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MTM)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM));
		} else {
			detailComposite.addInlineEditor(new PlaceholderInlineEditor(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM));
		}
	}

	/**
	 * Create the editor for the entity feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__ENTITY));
	}

	/**
	 * Create the editor for the startAt feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_startAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_AT));
	}

	/**
	 * Create the editor for the endAt feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_endAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__END_AT));
	}

	/**
	 * Create the editor for the startHeelCV feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_startHeelCVEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__START_HEEL_CV));
	}

	/**
	 * Create the editor for the spotCharterCount feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_spotCharterCountEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT));
	}

	/**
	 * Create the editor for the overrideInaccessibleRoutes feature on CharterInMarket
	 *
	 * @generated NOT
	 */
	protected void add_overrideInaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES));
	}

	/**
	 * Create the editor for the inaccessibleRoutes feature on CharterInMarket
	 *
	 * @generated NOT
	 */
	protected void add_inaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(new RouteExclusionMultiInlineEditor(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES));
	}

	/**
	 * Create the editor for the genericCharterContract feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_genericCharterContractEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__GENERIC_CHARTER_CONTRACT));
	}

	/**
	 * Create the editor for the charterInRate feature on CharterInMarket
	 *
	 * @generated
	 */
	protected void add_charterInRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE));
	}
}