/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FleetModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FleetModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FleetModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FleetModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fleet Model", validate);
	}

	public FleetModelComposite(final Composite container, final int style) {
		this(container, style, "Fleet Model", true);
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
    createFleetModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FleetModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to FleetModel
	 * @generated
	 */
	protected static void createFleetModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFleetEditor(composite, mainGroup);
    createVesselClassesEditor(composite, mainGroup);
    createVesselEventsEditor(composite, mainGroup);
    createFuelsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the fleet feature on FleetModel
	 * @generated
	 */
	protected static void createFleetEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFleetModel_Fleet()),
      "Fleet");
  }
		
	/**
	 * Create an editor for the vesselClasses feature on FleetModel
	 * @generated
	 */
	protected static void createVesselClassesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFleetModel_VesselClasses()),
      "Vessel Classes");
  }
		
	/**
	 * Create an editor for the vesselEvents feature on FleetModel
	 * @generated
	 */
	protected static void createVesselEventsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFleetModel_VesselEvents()),
      "Vessel Events");
  }
		
	/**
	 * Create an editor for the fuels feature on FleetModel
	 * @generated
	 */
	protected static void createFuelsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFleetModel_Fuels()),
      "Fuels");
  }
}
