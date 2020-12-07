/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for InventoryProfile instances
 *
 * @generated
 */
public class InventoryProfileComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public InventoryProfileComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public InventoryProfileComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using InventoryProfile as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.INVENTORY_PROFILE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_inventoryEditor(detailComposite, topClass);
		add_generatedSlotsEditor(detailComposite, topClass);
		add_volumeEditor(detailComposite, topClass);
		add_entityTableEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the relativeEntitlements feature on InventoryProfile
	 *
	 * @generated NOT
	 */
	protected void add_relativeEntitlementsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__RELATIVE_ENTITLEMENTS));
	}

	/**
	 * Create the editor for the inventory feature on InventoryProfile
	 *
	 * @generated NOT
	 */
	protected void add_inventoryEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__INVENTORY);
		editor.setEditorLocked(true);
		editor.setEditorEnabled(false);
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the generatedSlots feature on InventoryProfile
	 *
	 * @generated NOT
	 */
	protected void add_generatedSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__GENERATED_SLOTS));
	}

	/**
	 * Create the editor for the volume feature on InventoryProfile
	 *
	 * @generated
	 */
	protected void add_volumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__VOLUME));
	}

	/**
	 * Create the editor for the initialAllocations feature on InventoryProfile
	 *
	 * @generated NOT
	 */
	protected void add_initialAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__INITIAL_ALLOCATIONS));
	}

	/**
	 * Create the editor for the entityTable feature on InventoryProfile
	 *
	 * @generated NOT
	 */
	protected void add_entityTableEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE));
	}

	/**
	 * Create the editor for the windowSize feature on InventoryProfile
	 *
	 * @generated
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.INVENTORY_PROFILE__WINDOW_SIZE));
	}
}