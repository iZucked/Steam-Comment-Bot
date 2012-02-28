/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import com.mmxlabs.models.lng.fleet.FleetPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for VesselClass instances
 *
 * @generated
 */
public class VesselClassComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselClassComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselClassComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.AVESSEL_CLASS);
			if (helper != null) superClassesHelpers.add(helper);
		}
	}
	
	/**
	 * add editors to a composite, using VesselClass as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_CLASS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_inaccessiblePortsEditor(detailComposite, topClass);
		add_baseFuelEditor(detailComposite, topClass);
		add_capacityEditor(detailComposite, topClass);
		add_fillCapacityEditor(detailComposite, topClass);
		add_ladenAttributesEditor(detailComposite, topClass);
		add_ballastAttributesEditor(detailComposite, topClass);
		add_minSpeedEditor(detailComposite, topClass);
		add_maxSpeedEditor(detailComposite, topClass);
		add_minHeelEditor(detailComposite, topClass);
		add_warmingTimeEditor(detailComposite, topClass);
		add_coolingTimeEditor(detailComposite, topClass);
		add_coolingVolumeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the inaccessiblePorts feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_inaccessiblePortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_PORTS));
	}
	/**
	 * Create the editor for the baseFuel feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_baseFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__BASE_FUEL));
	}
	/**
	 * Create the editor for the capacity feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_capacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__CAPACITY));
	}
	/**
	 * Create the editor for the fillCapacity feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_fillCapacityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__FILL_CAPACITY));
	}
	/**
	 * Create the editor for the ladenAttributes feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_ladenAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES));
	}
	/**
	 * Create the editor for the ballastAttributes feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_ballastAttributesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES));
	}
	/**
	 * Create the editor for the minSpeed feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_minSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MIN_SPEED));
	}
	/**
	 * Create the editor for the maxSpeed feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_maxSpeedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MAX_SPEED));
	}
	/**
	 * Create the editor for the minHeel feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_minHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__MIN_HEEL));
	}
	/**
	 * Create the editor for the warmingTime feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_warmingTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__WARMING_TIME));
	}
	/**
	 * Create the editor for the coolingTime feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_coolingTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__COOLING_TIME));
	}
	/**
	 * Create the editor for the coolingVolume feature on VesselClass
	 *
	 * @generated
	 */
	protected void add_coolingVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_CLASS__COOLING_VOLUME));
	}
}