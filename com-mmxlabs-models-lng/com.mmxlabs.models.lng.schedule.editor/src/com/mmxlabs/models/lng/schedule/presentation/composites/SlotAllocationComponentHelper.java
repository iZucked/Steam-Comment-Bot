/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SlotAllocation instances
 *
 * @generated
 */
public class SlotAllocationComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SlotAllocationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SlotAllocationComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using SlotAllocation as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.SLOT_ALLOCATION);	
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
		add_spotMarketEditor(detailComposite, topClass);
		add_cargoAllocationEditor(detailComposite, topClass);
		add_marketAllocationEditor(detailComposite, topClass);
		add_slotVisitEditor(detailComposite, topClass);
		add_priceEditor(detailComposite, topClass);
		add_volumeTransferredEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the slot feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_slotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__SLOT));
	}
	/**
	 * Create the editor for the spotMarket feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_spotMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__SPOT_MARKET));
	}

	/**
	 * Create the editor for the cargoAllocation feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_cargoAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__CARGO_ALLOCATION));
	}

	/**
	 * Create the editor for the marketAllocation feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_marketAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__MARKET_ALLOCATION));
	}

	/**
	 * Create the editor for the slotVisit feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_slotVisitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__SLOT_VISIT));
	}

	/**
	 * Create the editor for the price feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_priceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__PRICE));
	}

	/**
	 * Create the editor for the volumeTransferred feature on SlotAllocation
	 *
	 * @generated
	 */
	protected void add_volumeTransferredEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SLOT_ALLOCATION__VOLUME_TRANSFERRED));
	}
}