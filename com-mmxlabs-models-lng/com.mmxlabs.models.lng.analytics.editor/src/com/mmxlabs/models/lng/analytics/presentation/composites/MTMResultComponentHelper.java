/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
 * A component helper for MTMResult instances
 *
 * @generated
 */
public class MTMResultComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public MTMResultComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public MTMResultComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using MTMResult as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.MTM_RESULT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_targetEditor(detailComposite, topClass);
		add_earliestETAEditor(detailComposite, topClass);
		add_earliestVolumeEditor(detailComposite, topClass);
		add_earliestPriceEditor(detailComposite, topClass);
		add_shippingEditor(detailComposite, topClass);
		add_shippingCostEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the target feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_targetEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__TARGET));
	}
	/**
	 * Create the editor for the earliestETA feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_earliestETAEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_ETA));
	}
	/**
	 * Create the editor for the earliestVolume feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_earliestVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_VOLUME));
	}
	/**
	 * Create the editor for the earliestPrice feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_earliestPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_PRICE));
	}
	/**
	 * Create the editor for the shipping feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_shippingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__SHIPPING));
	}

	/**
	 * Create the editor for the shippingCost feature on MTMResult
	 *
	 * @generated
	 */
	protected void add_shippingCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.MTM_RESULT__SHIPPING_COST));
	}
}