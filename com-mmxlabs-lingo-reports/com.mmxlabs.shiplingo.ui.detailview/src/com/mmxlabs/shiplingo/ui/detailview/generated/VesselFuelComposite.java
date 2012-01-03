/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing VesselFuel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class VesselFuelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public VesselFuelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public VesselFuelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel Fuel", validate);
	}

	public VesselFuelComposite(final Composite container, final int style) {
		this(container, style, "Vessel Fuel", true);
	}

	/**
	 * Create the main contents
	 * @generated
	 */
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
    createVesselFuelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of VesselFuel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to VesselFuel
	 * @generated
	 */
	protected static void createVesselFuelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createUnitPriceEditor(composite, mainGroup);
    createEquivalenceFactorEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the unitPrice feature on VesselFuel
	 * @generated
	 */
	protected static void createUnitPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselFuel_UnitPrice()),
      "Unit Price");
  }
		
	/**
	 * Create an editor for the equivalenceFactor feature on VesselFuel
	 * @generated
	 */
	protected static void createEquivalenceFactorEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselFuel_EquivalenceFactor()),
      "Equivalence Factor");
  }
}
