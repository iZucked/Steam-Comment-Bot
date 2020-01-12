/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import com.mmxlabs.models.lng.schedule.SchedulePackage;

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
 * A component helper for InventoryChangeEvent instances
 *
 * @generated
 */
public class InventoryChangeEventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public InventoryChangeEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public InventoryChangeEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using InventoryChangeEvent as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_dateEditor(detailComposite, topClass);
		add_changeQuantityEditor(detailComposite, topClass);
		add_currentLevelEditor(detailComposite, topClass);
		add_currentMinEditor(detailComposite, topClass);
		add_currentMaxEditor(detailComposite, topClass);
		add_eventEditor(detailComposite, topClass);
		add_slotAllocationEditor(detailComposite, topClass);
		add_openSlotAllocationEditor(detailComposite, topClass);
		add_breachedMinEditor(detailComposite, topClass);
		add_breachedMaxEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the date feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_dateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__DATE));
	}
	/**
	 * Create the editor for the changeQuantity feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_changeQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CHANGE_QUANTITY));
	}
	/**
	 * Create the editor for the currentLevel feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_currentLevelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_LEVEL));
	}
	/**
	 * Create the editor for the currentMin feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_currentMinEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_MIN));
	}
	/**
	 * Create the editor for the currentMax feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_currentMaxEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__CURRENT_MAX));
	}
	/**
	 * Create the editor for the event feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_eventEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__EVENT));
	}
	/**
	 * Create the editor for the slotAllocation feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_slotAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__SLOT_ALLOCATION));
	}
	/**
	 * Create the editor for the openSlotAllocation feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_openSlotAllocationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__OPEN_SLOT_ALLOCATION));
	}

	/**
	 * Create the editor for the breachedMin feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_breachedMinEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__BREACHED_MIN));
	}

	/**
	 * Create the editor for the breachedMax feature on InventoryChangeEvent
	 *
	 * @generated
	 */
	protected void add_breachedMaxEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.INVENTORY_CHANGE_EVENT__BREACHED_MAX));
	}
}