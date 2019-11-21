/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for RoundTripShippingOption instances
 *
 * @generated
 */
public class RoundTripShippingOptionComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public RoundTripShippingOptionComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RoundTripShippingOptionComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(AnalyticsPackage.Literals.SHIPPING_OPTION));
	}
	
	/**
	 * add editors to a composite, using RoundTripShippingOption as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_hireCostEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vessel feature on RoundTripShippingOption
	 *
	 * @generated NOT
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__VESSEL));
		detailComposite.addInlineEditor(new TextualVesselReferenceInlineEditor(AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__VESSEL));
	}

	/**
	 * Create the editor for the hireCost feature on RoundTripShippingOption
	 *
	 * @generated
	 */
	protected void add_hireCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__HIRE_COST));
	}

	/**
	 * Create the editor for the entity feature on RoundTripShippingOption
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__ENTITY));
	}
}