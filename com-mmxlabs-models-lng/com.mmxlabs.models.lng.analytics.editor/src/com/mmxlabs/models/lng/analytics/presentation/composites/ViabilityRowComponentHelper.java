/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

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
 * A component helper for ViabilityRow instances
 *
 * @generated
 */
public class ViabilityRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ViabilityRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ViabilityRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using ViabilityRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.VIABILITY_ROW);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_buyOptionEditor(detailComposite, topClass);
		add_sellOptionEditor(detailComposite, topClass);
		add_shippingEditor(detailComposite, topClass);
		add_lhsResultsEditor(detailComposite, topClass);
		add_rhsResultsEditor(detailComposite, topClass);
		add_targetEditor(detailComposite, topClass);
		add_priceEditor(detailComposite, topClass);
		add_etaEditor(detailComposite, topClass);
		add_referencePriceEditor(detailComposite, topClass);
		add_startVolumeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the buyOption feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_buyOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__BUY_OPTION));
	}
	/**
	 * Create the editor for the sellOption feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_sellOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__SELL_OPTION));
	}
	/**
	 * Create the editor for the shipping feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_shippingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__SHIPPING));
	}
	/**
	 * Create the editor for the lhsResults feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_lhsResultsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__LHS_RESULTS));
	}
	/**
	 * Create the editor for the rhsResults feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_rhsResultsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__RHS_RESULTS));
	}
	/**
	 * Create the editor for the target feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_targetEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__TARGET));
	}
	/**
	 * Create the editor for the price feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_priceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__PRICE));
	}
	/**
	 * Create the editor for the eta feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_etaEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__ETA));
	}
	/**
	 * Create the editor for the referencePrice feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_referencePriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__REFERENCE_PRICE));
	}
	/**
	 * Create the editor for the startVolume feature on ViabilityRow
	 *
	 * @generated
	 */
	protected void add_startVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.VIABILITY_ROW__START_VOLUME));
	}
}