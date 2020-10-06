/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.presentation.editors.ShippingDaysRestrictionInlineEditorChangedListener;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SubContractProfile instances
 *
 * @generated
 */
public class SubContractProfileComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SubContractProfileComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SubContractProfileComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using SubContractProfile as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.SUB_CONTRACT_PROFILE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_nameEditor(detailComposite, topClass);
		add_contractTypeEditor(detailComposite, topClass);
		add_distributionModelEditor(detailComposite, topClass);
		add_slotTemplateIdEditor(detailComposite, topClass);
		add_nominatedVesselEditor(detailComposite, topClass);
		add_shippingDaysEditor(detailComposite, topClass);
		add_customAttribsEditor(detailComposite, topClass);
		add_constraintsEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
		add_windowSizeUnitsEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the name feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_nameEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__NAME));
	}

	/**
	 * Create the editor for the contractType feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_contractTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONTRACT_TYPE));
	}

	/**
	 * Create the editor for the distributionModel feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_distributionModelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__DISTRIBUTION_MODEL));
	}

	/**
	 * Create the editor for the slotTemplateId feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_slotTemplateIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID));
	}

	/**
	 * Create the editor for the customAttribs feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_customAttribsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CUSTOM_ATTRIBS));
	}

	/**
	 * Create the editor for the nominatedVessel feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_nominatedVesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL);
		editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the shippingDays feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_shippingDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// Hide it!
//		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__SHIPPING_DAYS);
//		editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
//		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the slots feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_slotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__SLOTS));
	}

	/**
	 * Create the editor for the constraints feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_constraintsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__CONSTRAINTS));
	}

	/**
	 * Create the editor for the windowSize feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE));
	}

	/**
	 * Create the editor for the windowSizeUnits feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_windowSizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__WINDOW_SIZE_UNITS));
	}

	/**
	 * Create the editor for the port feature on SubContractProfile
	 *
	 * @generated NOT using custom editor
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualPortReferenceInlineEditor(ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT));
	}
}