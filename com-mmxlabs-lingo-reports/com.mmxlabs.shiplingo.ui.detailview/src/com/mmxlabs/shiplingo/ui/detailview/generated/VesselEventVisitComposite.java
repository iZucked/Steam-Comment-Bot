/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing VesselEventVisit instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class VesselEventVisitComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public VesselEventVisitComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public VesselEventVisitComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel Event Visit", validate);
	}

	public VesselEventVisitComposite(final Composite container, final int style) {
		this(container, style, "Vessel Event Visit", true);
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
    createVesselEventVisitFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of VesselEventVisit.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      PortVisitComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to VesselEventVisit
	 * @generated
	 */
	protected static void createVesselEventVisitFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVesselEventEditor(composite, mainGroup);
    createRevenueEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the vesselEvent feature on VesselEventVisit
	 * @generated
	 */
	protected static void createVesselEventEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getVesselEventVisit_VesselEvent()),
      "Vessel Event");
  }

  /**
   * Create an editor for the revenue feature on VesselEventVisit
   * @generated
   */
  protected static void createRevenueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getVesselEventVisit_Revenue()),
      "Revenue");
  }
}
