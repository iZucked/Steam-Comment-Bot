/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
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
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.AVESSEL));
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
		add_vesselClassEditor(detailComposite, topClass);
		add_inaccessiblePortsEditor(detailComposite, topClass);
		add_availabilityEditor(detailComposite, topClass);
		add_startHeelEditor(detailComposite, topClass);
		add_timeCharterRateEditor(detailComposite, topClass);
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
	 * Create the editor for the startHeel feature on Vessel
	 *
	 * @generated
	 */
	protected void add_startHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__START_HEEL));
	}
	/**
	 * Create the editor for the availability feature on Vessel
	 *
	 * @generated
	 */
	protected void add_availabilityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__AVAILABILITY));
	}
	/**
	 * Create the editor for the timeCharterRate feature on Vessel
	 *
	 * @generated
	 */
	protected void add_timeCharterRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__TIME_CHARTER_RATE));
	}
}