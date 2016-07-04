/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CargoModel instances
 *
 * @generated
 */
public class CargoModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CargoModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CARGO_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_loadSlotsEditor(detailComposite, topClass);
		add_dischargeSlotsEditor(detailComposite, topClass);
		add_cargoesEditor(detailComposite, topClass);
		add_cargoGroupsEditor(detailComposite, topClass);
		add_vesselAvailabilitiesEditor(detailComposite, topClass);
		add_vesselEventsEditor(detailComposite, topClass);
		add_vesselTypeGroupsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the loadSlots feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_loadSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS));
	}
	/**
	 * Create the editor for the dischargeSlots feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_dischargeSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS));
	}
	/**
	 * Create the editor for the cargoes feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGOES));
	}

	/**
	 * Create the editor for the cargoGroups feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGO_GROUPS));
	}

	/**
	 * Create the editor for the vesselAvailabilities feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES));
	}

	/**
	 * Create the editor for the vesselEvents feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselEventsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS));
	}

	/**
	 * Create the editor for the vesselTypeGroups feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselTypeGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_TYPE_GROUPS));
	}
}