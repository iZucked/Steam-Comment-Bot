/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.VesselAvailabilityDetailWrapper;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for VesselAvailability instances
 *
 * @generated
 */
public class VesselAvailabilityComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselAvailabilityComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselAvailabilityComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.VESSEL_ASSIGNMENT_TYPE));
	}
	
	/**
	 * add editors to a composite, using VesselAvailability as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.VESSEL_AVAILABILITY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_fleetEditor(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_timeCharterRateEditor(detailComposite, topClass);
		add_startAtEditor(detailComposite, topClass);
		add_startAfterEditor(detailComposite, topClass);
		add_startByEditor(detailComposite, topClass);
		add_endAtEditor(detailComposite, topClass);
		add_endAfterEditor(detailComposite, topClass);
		add_endByEditor(detailComposite, topClass);
		add_startHeelEditor(detailComposite, topClass);
		add_endHeelEditor(detailComposite, topClass);
		add_forceHireCostOnlyEndRuleEditor(detailComposite, topClass);
		add_optionalEditor(detailComposite, topClass);
		add_repositioningFeeEditor(detailComposite, topClass);
		add_ballastBonusEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the fleet feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_fleetEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__FLEET));
	}

	/**
	 * Create the editor for the vessel feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL));
	}
	/**
	 * Create the editor for the timeCharterRate feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_timeCharterRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE));
	}
	/**
	 * Create the editor for the startAt feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_startAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AT));
	}
	/**
	 * Create the editor for the startAfter feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_startAfterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER));
	}
	/**
	 * Create the editor for the startBy feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_startByEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY));
	}
	/**
	 * Create the editor for the endAt feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_endAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT));
	}
	/**
	 * Create the editor for the endAfter feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_endAfterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER));
	}
	/**
	 * Create the editor for the endBy feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_endByEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY));
	}
	/**
	 * Create the editor for the startHeel feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_startHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL));
	}
	/**
	 * Create the editor for the endHeel feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_endHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL));
	}

	/**
	 * Create the editor for the forceHireCostOnlyEndRule feature on VesselAvailability
	 *
	 * @generated NOT
	 */
	protected void add_forceHireCostOnlyEndRuleEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE));
	}

	/**
	 * Create the editor for the optional feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_optionalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__OPTIONAL));
	}

	/**
	 * Create the editor for the repositioningFee feature on VesselAvailability
	 *
	 * @generated NOT
	 */
	protected void add_repositioningFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new VesselAvailabilityDetailWrapper(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__REPOSITIONING_FEE)));
	}

	/**
	 * Create the editor for the ballastBonus feature on VesselAvailability
	 *
	 * @generated NOT
	 */
	protected void add_ballastBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new VesselAvailabilityDetailWrapper(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS)));
	}

	/**
	 * Create the editor for the entity feature on VesselAvailability
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.VESSEL_AVAILABILITY__ENTITY));
	}
}