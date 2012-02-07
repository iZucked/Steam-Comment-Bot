/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.editors.FuelCurveEditor;

/**
 * A composite containing a form for editing VesselStateAttributes instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java
 * class hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class VesselStateAttributesComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public VesselStateAttributesComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public VesselStateAttributesComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel State Attributes", validate);
	}

	public VesselStateAttributesComposite(final Composite container, final int style) {
		this(container, style, "Vessel State Attributes", true);
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
		createVesselStateAttributesFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of VesselStateAttributes.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to VesselStateAttributes
	 * 
	 * @generated
	 */
	protected static void createVesselStateAttributesFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createNboRateEditor(composite, mainGroup);
		createIdleNBORateEditor(composite, mainGroup);
		createIdleConsumptionRateEditor(composite, mainGroup);
		createFuelConsumptionCurveEditor(composite, mainGroup);
		createVesselStateEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the nboRate feature on VesselStateAttributes
	 * 
	 * @generated NO label
	 */
	protected static void createNboRateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselStateAttributes_NboRate()), "NBO Rate");
	}

	/**
	 * Create an editor for the idleNBORate feature on VesselStateAttributes
	 * 
	 * @generated NO label
	 */
	protected static void createIdleNBORateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselStateAttributes_IdleNBORate()), "Idle NBO Rate");
	}

	/**
	 * Create an editor for the idleConsumptionRate feature on VesselStateAttributes
	 * 
	 * @generated
	 */
	protected static void createIdleConsumptionRateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(FleetPackage.eINSTANCE.getVesselStateAttributes_IdleConsumptionRate()), "Idle Consumption Rate");
	}

	/**
	 * Create an editor for the fuelConsumptionCurve feature on VesselStateAttributes
	 * 
	 * @generated NO custom editor
	 */
	protected static void createFuelConsumptionCurveEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, new FuelCurveEditor(composite.getInputPath(), FleetPackage.eINSTANCE.getVesselStateAttributes_FuelConsumptionCurve(), composite.getEditingDomain(),
				composite.getCommandProcessor()), "Fuel Consumption Curve");
	}

	/**
	 * Create an editor for the vesselState feature on VesselStateAttributes
	 * 
	 * @generated NO removed editor
	 */
	protected static void createVesselStateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		// composite.createEditorControl(mainGroup,
		// composite.createEditor(FleetPackage.eINSTANCE.getVesselStateAttributes_VesselState()),
		// "Vessel State");
	}
}
