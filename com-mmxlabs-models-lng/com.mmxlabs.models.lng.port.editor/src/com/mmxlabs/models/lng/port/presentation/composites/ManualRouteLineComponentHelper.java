/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import com.mmxlabs.models.lng.port.PortPackage;

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
 * A component helper for ManualRouteLine instances
 *
 * @generated
 */
public class ManualRouteLineComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ManualRouteLineComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ManualRouteLineComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using ManualRouteLine as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PortPackage.Literals.MANUAL_ROUTE_LINE);	
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
		add_fromEditor(detailComposite, topClass);
		add_toEditor(detailComposite, topClass);
		add_distanceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the routeOption feature on ManualRouteLine
	 *
	 * @generated
	 */
	protected void add_routeOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.MANUAL_ROUTE_LINE__ROUTE_OPTION));
	}
	/**
	 * Create the editor for the from feature on ManualRouteLine
	 *
	 * @generated
	 */
	protected void add_fromEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.MANUAL_ROUTE_LINE__FROM));
	}
	/**
	 * Create the editor for the to feature on ManualRouteLine
	 *
	 * @generated
	 */
	protected void add_toEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.MANUAL_ROUTE_LINE__TO));
	}
	/**
	 * Create the editor for the distance feature on ManualRouteLine
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.MANUAL_ROUTE_LINE__DISTANCE));
	}
}