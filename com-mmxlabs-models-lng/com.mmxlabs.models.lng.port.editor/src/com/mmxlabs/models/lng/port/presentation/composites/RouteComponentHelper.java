/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Route instances
 *
 * @generated
 */
public class RouteComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public RouteComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RouteComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Route as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PortPackage.Literals.ROUTE);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_linesEditor(detailComposite, topClass);
		add_routeOptionEditor(detailComposite, topClass);
		add_northEntranceEditor(detailComposite, topClass);
		add_southEntranceEditor(detailComposite, topClass);
		add_distanceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the lines feature on Route
	 *
	 * @generated
	 */
	protected void add_linesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__LINES));
	}

	/**
	 * Create the editor for the routeOption feature on Route
	 *
	 * @generated
	 */
	protected void add_routeOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__ROUTE_OPTION));
	}

	/**
	 * Create the editor for the virtualPort feature on Route
	 *
	 * @generated NOT
	 */
	protected void add_virtualPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__VIRTUAL_PORT));
	}

	/**
	 * Create the editor for the northEntrance feature on Route
	 *
	 * @generated
	 */
	protected void add_northEntranceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__NORTH_ENTRANCE));
	}

	/**
	 * Create the editor for the southEntrance feature on Route
	 *
	 * @generated
	 */
	protected void add_southEntranceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE));
	}

	/**
	 * Create the editor for the distance feature on Route
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.ROUTE__DISTANCE));
	}
}