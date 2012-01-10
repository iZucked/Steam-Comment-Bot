/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.fleetallocation.FleetallocationPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FleetVessel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FleetVesselComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FleetVesselComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FleetVesselComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fleet Vessel", validate);
	}

	public FleetVesselComposite(final Composite container, final int style) {
		this(container, style, "Fleet Vessel", true);
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
    createFleetVesselFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FleetVessel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      AllocatedVesselComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to FleetVessel
	 * @generated
	 */
	protected static void createFleetVesselFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVesselEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the vessel feature on FleetVessel
	 * @generated
	 */
	protected static void createVesselEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetallocationPackage.eINSTANCE.getFleetVessel_Vessel()),
      "Vessel");
  }
}
