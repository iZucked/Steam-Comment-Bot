/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing VesselClassCost instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class
 * hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class VesselClassCostComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public VesselClassCostComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public VesselClassCostComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel Class Cost", validate);
	}

	public VesselClassCostComposite(final Composite container, final int style) {
		this(container, style, "Vessel Class Cost", true);
	}

	/**
	 * Create the main contents
	 * 
	 * @generated
	 */
	@Override
	protected void createContents(final Composite group) {
		final Composite mainGroup;

		if (group == null) {
			mainGroup = createGroup(this, mainGroupTitle);
		} else {
			mainGroup = group;
		}

		super.createContents(mainGroup);

		createFields(this, mainGroup);
	}

	/**
	 * @generated
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createFieldsFromSupers(composite, mainGroup);
		createVesselClassCostFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of VesselClassCost.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to VesselClassCost
	 * 
	 * @generated
	 */
	protected static void createVesselClassCostFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createCanalEditor(composite, mainGroup);
		createLadenCostEditor(composite, mainGroup);
		createUnladenCostEditor(composite, mainGroup);
		createTransitTimeEditor(composite, mainGroup);
		createTransitFuelEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the canal feature on VesselClassCost
	 * 
	 * @generated NO
	 */
	protected static void createCanalEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		// composite.createEditorControl(mainGroup,
		// composite.createEditor(FleetPackage.eINSTANCE.getVesselClassCost_Canal()),
		// "Canal");
	}

	/**
	 * Create an editor for the ladenCost feature on VesselClassCost
	 * 
	 * @generated
	 */
	protected static void createLadenCostEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselClassCost_LadenCost()), "Laden Cost");
	}

	/**
	 * Create an editor for the unladenCost feature on VesselClassCost
	 * 
	 * @generated
	 */
	protected static void createUnladenCostEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselClassCost_UnladenCost()), "Unladen Cost");
	}

	/**
	 * Create an editor for the transitTime feature on VesselClassCost
	 * 
	 * @generated
	 */
	protected static void createTransitTimeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselClassCost_TransitTime()), "Transit Time");
	}

	/**
	 * Create an editor for the transitFuel feature on VesselClassCost
	 * 
	 * @generated
	 */
	protected static void createTransitFuelEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselClassCost_TransitFuel()), "Transit Fuel");
	}
}
