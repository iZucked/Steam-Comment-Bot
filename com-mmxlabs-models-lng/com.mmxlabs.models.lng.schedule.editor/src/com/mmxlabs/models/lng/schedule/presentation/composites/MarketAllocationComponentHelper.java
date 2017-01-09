/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for MarketAllocation instances
 *
 * @generated
 */
public class MarketAllocationComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public MarketAllocationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public MarketAllocationComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER));
	}
	
	/**
	 * add editors to a composite, using MarketAllocation as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.MARKET_ALLOCATION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_slotEditor(detailComposite, topClass);
		add_marketEditor(detailComposite, topClass);
		add_slotAllocationEditor(detailComposite, topClass);
		add_priceEditor(detailComposite, topClass);
		add_slotVisitEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the slot feature on MarketAllocation
	 *
	 * @generated
	 */
	protected void add_slotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.MARKET_ALLOCATION__SLOT));
	}
	/**
	 * Create the editor for the market feature on MarketAllocation
	 *
	 * @generated
	 */
	protected void add_marketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.MARKET_ALLOCATION__MARKET));
	}

	/**
	 * Create the editor for the slotAllocation feature on MarketAllocation
	 *
	 * @generated
	 */
	protected void add_slotAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.MARKET_ALLOCATION__SLOT_ALLOCATION));
	}

	/**
	 * Create the editor for the price feature on MarketAllocation
	 *
	 * @generated
	 */
	protected void add_priceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.MARKET_ALLOCATION__PRICE));
	}

	/**
	 * Create the editor for the slotVisit feature on MarketAllocation
	 *
	 * @generated
	 */
	protected void add_slotVisitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.MARKET_ALLOCATION__SLOT_VISIT));
	}
}