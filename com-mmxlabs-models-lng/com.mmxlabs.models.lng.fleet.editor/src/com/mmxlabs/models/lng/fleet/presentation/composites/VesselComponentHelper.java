/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.RouteExclusionMultiInlineEditor;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Vessel instances
 *
 * @generated
 */
public class VesselComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.AVESSEL_SET));
	}
	
	/**
	 * add editors to a composite, using Vessel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_shortNameEditor(detailComposite, topClass);
		add_vesselClassEditor(detailComposite, topClass);
		add_inaccessiblePortsEditor(detailComposite, topClass);
		add_capacityEditor(detailComposite, topClass);
		add_fillCapacityEditor(detailComposite, topClass);
		add_scntEditor(detailComposite, topClass);
		add_overrideInaccessibleRoutesEditor(detailComposite, topClass);
		add_inaccessibleRoutesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the shortName feature on Vessel
	 *
	 * @generated
	 */
	protected void add_shortNameEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__SHORT_NAME));
	}

	/**
	 * Create the editor for the vesselClass feature on Vessel
	 *
	 * @generated
	 */
	protected void add_vesselClassEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__VESSEL_CLASS));
	}
	/**
	 * Create the editor for the inaccessiblePorts feature on Vessel
	 *
	 * @generated
	 */
	protected void add_inaccessiblePortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS));
	}
	/**
	 * Create the editor for the capacity feature on Vessel
	 *
	 * @generated
	 */
	protected void add_capacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__CAPACITY));
	}

	/**
	 * Create the editor for the fillCapacity feature on Vessel
	 *
	 * @generated
	 */
	protected void add_fillCapacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__FILL_CAPACITY));
	}

	/**
	 * Create the editor for the scnt feature on Vessel
	 *
	 * @generated
	 */
	protected void add_scntEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__SCNT));
	}

	/**
	 * Create the editor for the overrideInaccessibleRoutes feature on Vessel
	 *
	 * @generated
	 */
	protected void add_overrideInaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__OVERRIDE_INACCESSIBLE_ROUTES));
	}

	/**
	 * Create the editor for the inaccessibleRoutes feature on Vessel
	 *
	 * @generated NOT
	 */
	protected void add_inaccessibleRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RouteExclusionMultiInlineEditor(FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES));
	}
}