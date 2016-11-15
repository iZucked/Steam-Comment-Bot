/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for BasicSlotPNLDetails instances
 *
 * @generated
 */
public class BasicSlotPNLDetailsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BasicSlotPNLDetailsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BasicSlotPNLDetailsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.GENERAL_PNL_DETAILS));
	}
	
	/**
	 * add editors to a composite, using BasicSlotPNLDetails as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_extraShippingPNLEditor(detailComposite, topClass);
		add_additionalPNLEditor(detailComposite, topClass);
		add_cancellationFeesEditor(detailComposite, topClass);
		add_hedgingValueEditor(detailComposite, topClass);
		add_miscCostsValueEditor(detailComposite, topClass);
		add_extraUpsidePNLEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the extraShippingPNL feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_extraShippingPNLEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__EXTRA_SHIPPING_PNL));
	}

	/**
	 * Create the editor for the additionalPNL feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_additionalPNLEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__ADDITIONAL_PNL));
	}

	/**
	 * Create the editor for the cancellationFees feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_cancellationFeesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__CANCELLATION_FEES));
	}
	/**
	 * Create the editor for the hedgingValue feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_hedgingValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__HEDGING_VALUE));
	}

	/**
	 * Create the editor for the miscCostsValue feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_miscCostsValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__MISC_COSTS_VALUE));
	}

	/**
	 * Create the editor for the extraUpsidePNL feature on BasicSlotPNLDetails
	 *
	 * @generated
	 */
	protected void add_extraUpsidePNLEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.BASIC_SLOT_PNL_DETAILS__EXTRA_UPSIDE_PNL));
	}
}