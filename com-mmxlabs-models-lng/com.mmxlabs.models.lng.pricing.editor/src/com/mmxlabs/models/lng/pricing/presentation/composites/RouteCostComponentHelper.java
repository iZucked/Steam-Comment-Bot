/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for RouteCost instances
 *
 * @generated
 */
public class RouteCostComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public RouteCostComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RouteCostComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using RouteCost as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.ROUTE_COST);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_routeOptionEditor(detailComposite, topClass);
		add_vesselsEditor(detailComposite, topClass);
		add_ladenCostEditor(detailComposite, topClass);
		add_ballastCostEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the routeOption feature on RouteCost
	 *
	 * @generated NO
	 */
	protected void add_routeOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ROUTE_COST__ROUTE_OPTION);
		editor.setEditorEnabled(false); // NOTE: this causes an exception because it is not expected before the control is created
		detailComposite.addInlineEditor(editor);
	}


	/**
	 * Create the editor for the vessels feature on RouteCost
	 *
	 * @generated
	 */
	protected void add_vesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ROUTE_COST__VESSELS));
	}

	/**
	 * Create the editor for the ladenCost feature on RouteCost
	 *
	 * @generated
	 */
	protected void add_ladenCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ROUTE_COST__LADEN_COST));
	}

	/**
	 * Create the editor for the ballastCost feature on RouteCost
	 *
	 * @generated
	 */
	protected void add_ballastCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.ROUTE_COST__BALLAST_COST));
	}
}