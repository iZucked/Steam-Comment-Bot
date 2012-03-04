/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

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
 * A component helper for Cargo instances
 *
 * @generated
 */
public class CargoComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.ACARGO);
			if (helper != null) superClassesHelpers.add(helper);
		}
	}
	
	/**
	 * add editors to a composite, using Cargo as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CARGO);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_loadSlotEditor(detailComposite, topClass);
		add_dischargeSlotEditor(detailComposite, topClass);
		add_cargoTypeEditor(detailComposite, topClass);
		add_allowRewiringEditor(detailComposite, topClass);
		add_allowedVesselsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the loadSlot feature on Cargo
	 *
	 * @generated NO
	 */
	protected void add_loadSlotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__LOAD_SLOT));
	}
	/**
	 * Create the editor for the dischargeSlot feature on Cargo
	 *
	 * @generated NO
	 */
	protected void add_dischargeSlotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__DISCHARGE_SLOT));
	}

	/**
	 * Create the editor for the cargoType feature on Cargo
	 *
	 * @generated
	 */
	protected void add_cargoTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__CARGO_TYPE));
	}

	/**
	 * Create the editor for the allowRewiring feature on Cargo
	 *
	 * @generated
	 */
	protected void add_allowRewiringEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__ALLOW_REWIRING));
	}

	/**
	 * Create the editor for the allowedVessels feature on Cargo
	 *
	 * @generated
	 */
	protected void add_allowedVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO__ALLOWED_VESSELS));
	}
}