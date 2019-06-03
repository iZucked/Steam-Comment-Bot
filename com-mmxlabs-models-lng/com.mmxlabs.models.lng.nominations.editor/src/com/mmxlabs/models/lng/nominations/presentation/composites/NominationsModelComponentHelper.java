/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import com.mmxlabs.models.lng.nominations.NominationsPackage;

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
 * A component helper for NominationsModel instances
 *
 * @generated
 */
public class NominationsModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public NominationsModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public NominationsModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using NominationsModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, NominationsPackage.Literals.NOMINATIONS_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_slotNominationSpecsEditor(detailComposite, topClass);
		add_slotNominationsEditor(detailComposite, topClass);
		add_contractNominationSpecsEditor(detailComposite, topClass);
		add_contractNominationsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the slotNominationSpecs feature on NominationsModel
	 *
	 * @generated
	 */
	protected void add_slotNominationSpecsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.NOMINATIONS_MODEL__SLOT_NOMINATION_SPECS));
	}
	/**
	 * Create the editor for the slotNominations feature on NominationsModel
	 *
	 * @generated
	 */
	protected void add_slotNominationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.NOMINATIONS_MODEL__SLOT_NOMINATIONS));
	}

	/**
	 * Create the editor for the contractNominationSpecs feature on NominationsModel
	 *
	 * @generated
	 */
	protected void add_contractNominationSpecsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.NOMINATIONS_MODEL__CONTRACT_NOMINATION_SPECS));
	}

	/**
	 * Create the editor for the contractNominations feature on NominationsModel
	 *
	 * @generated
	 */
	protected void add_contractNominationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.NOMINATIONS_MODEL__CONTRACT_NOMINATIONS));
	}
}