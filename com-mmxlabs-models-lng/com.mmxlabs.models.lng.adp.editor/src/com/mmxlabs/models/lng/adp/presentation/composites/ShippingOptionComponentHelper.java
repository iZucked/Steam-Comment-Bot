/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.presentation.editors.SpotIndexInlineEditor;
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
 * A component helper for ShippingOption instances
 *
 * @generated
 */
public class ShippingOptionComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ShippingOptionComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ShippingOptionComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using ShippingOption as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.SHIPPING_OPTION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselAssignmentTypeEditor(detailComposite, topClass);
		add_spotIndexEditor(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_maxLadenIdleDaysEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vesselAssignmentType feature on ShippingOption
	 *
	 * @generated
	 */
	protected void add_vesselAssignmentTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE));
	}
	/**
	 * Create the editor for the spotIndex feature on ShippingOption
	 *
	 * @generated NOT
	 */
	protected void add_spotIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SHIPPING_OPTION__SPOT_INDEX));
		detailComposite.addInlineEditor(new SpotIndexInlineEditor(ADPPackage.Literals.SHIPPING_OPTION__SPOT_INDEX));
	}
	/**
	 * Create the editor for the vessel feature on ShippingOption
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SHIPPING_OPTION__VESSEL));
	}
	/**
	 * Create the editor for the maxLadenIdleDays feature on ShippingOption
	 *
	 * @generated
	 */
	protected void add_maxLadenIdleDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SHIPPING_OPTION__MAX_LADEN_IDLE_DAYS));
	}
}