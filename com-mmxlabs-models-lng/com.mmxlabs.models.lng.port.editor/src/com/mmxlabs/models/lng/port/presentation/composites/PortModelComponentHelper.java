/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * A component helper for PortModel instances
 *
 * @generated
 */
public class PortModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PortModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using PortModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PortPackage.Literals.PORT_MODEL);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_portsEditor(detailComposite, topClass);
		add_portGroupsEditor(detailComposite, topClass);
		add_routesEditor(detailComposite, topClass);
		add_specialPortGroupsEditor(detailComposite, topClass);
		add_portCountryGroupsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the ports feature on PortModel
	 *
	 * @generated
	 */
	protected void add_portsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT_MODEL__PORTS));
	}
	/**
	 * Create the editor for the portGroups feature on PortModel
	 *
	 * @generated
	 */
	protected void add_portGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT_MODEL__PORT_GROUPS));
	}

	/**
	 * Create the editor for the routes feature on PortModel
	 *
	 * @generated
	 */
	protected void add_routesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT_MODEL__ROUTES));
	}

	/**
	 * Create the editor for the specialPortGroups feature on PortModel
	 *
	 * @generated
	 */
	protected void add_specialPortGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT_MODEL__SPECIAL_PORT_GROUPS));
	}

	/**
	 * Create the editor for the portCountryGroups feature on PortModel
	 *
	 * @generated
	 */
	protected void add_portCountryGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT_MODEL__PORT_COUNTRY_GROUPS));
	}
}