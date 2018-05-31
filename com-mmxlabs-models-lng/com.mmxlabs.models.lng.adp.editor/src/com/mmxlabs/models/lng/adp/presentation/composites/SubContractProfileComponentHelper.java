/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.models.lng.adp.presentation.editors.NominatedVesselEditorWrapper;
import com.mmxlabs.models.lng.adp.presentation.editors.ShippingDaysRestrictionInlineEditorChangedListener;
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
		add_totalVolumeEditor(detailComposite, topClass);
		add_volumeUnitEditor(detailComposite, topClass);
		add_distributionModelEditor(detailComposite, topClass);
		add_slotTemplateIdEditor(detailComposite, topClass);
		add_nominatedVesselEditor(detailComposite, topClass);
		add_shippingDaysEditor(detailComposite, topClass);
		add_customAttribsEditor(detailComposite, topClass);
		add_slotsEditor(detailComposite, topClass);
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
	 * Create the editor for the totalVolume feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_totalVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__TOTAL_VOLUME));
	}

	/**
	 * Create the editor for the volumeUnit feature on SubContractProfile
	 *
	 * @generated
	 */
	protected void add_volumeUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__VOLUME_UNIT));
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
		detailComposite.addInlineEditor(new NominatedVesselEditorWrapper(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL)));
	}

	/**
	 * Create the editor for the shippingDays feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_shippingDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__SHIPPING_DAYS);
		editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the slots feature on SubContractProfile
	 *
	 * @generated NOT
	 */
	protected void add_slotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__SLOTS));
	}
}