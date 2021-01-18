/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CharterOutEvent instances
 *
 * @generated
 */
public class CharterOutEventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterOutEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterOutEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CargoPackage.Literals.VESSEL_EVENT));
	}
	
	/**
	 * add editors to a composite, using CharterOutEvent as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CHARTER_OUT_EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_optionalEditor(detailComposite, topClass);
		add_relocateToEditor(detailComposite, topClass);
		add_hireRateEditor(detailComposite, topClass);
		add_ballastBonusEditor(detailComposite, topClass);
		add_repositioningFeeEditor(detailComposite, topClass);
		add_requiredHeelEditor(detailComposite, topClass);
		add_availableHeelEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the optional feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_optionalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__OPTIONAL));
	}

	/**
	 * Create the editor for the relocateTo feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_relocateToEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__RELOCATE_TO));
	}
	/**
	 * Create the editor for the repositioningFee feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_repositioningFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__REPOSITIONING_FEE));
	}
	/**
	 * Create the editor for the requiredHeel feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_requiredHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__REQUIRED_HEEL));
	}

	/**
	 * Create the editor for the availableHeel feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_availableHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__AVAILABLE_HEEL));
	}

	/**
	 * Create the editor for the hireRate feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_hireRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__HIRE_RATE));
	}

	/**
	 * Create the editor for the ballastBonus feature on CharterOutEvent
	 *
	 * @generated
	 */
	protected void add_ballastBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CHARTER_OUT_EVENT__BALLAST_BONUS));
	}
}