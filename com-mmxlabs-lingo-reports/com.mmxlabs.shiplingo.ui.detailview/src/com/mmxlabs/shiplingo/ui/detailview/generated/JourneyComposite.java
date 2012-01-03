/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Journey instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class JourneyComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public JourneyComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public JourneyComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Journey", validate);
	}

	public JourneyComposite(final Composite container, final int style) {
		this(container, style, "Journey", true);
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
    createJourneyFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Journey.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      ScheduledEventComposite.createFields(composite, mainGroup);
      FuelMixtureComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to Journey
	 * @generated
	 */
	protected static void createJourneyFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVesselStateEditor(composite, mainGroup);
    createRouteEditor(composite, mainGroup);
    createSpeedEditor(composite, mainGroup);
    createDistanceEditor(composite, mainGroup);
    createRouteCostEditor(composite, mainGroup);
    createToPortEditor(composite, mainGroup);
    createFromPortEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the vesselState feature on Journey
	 * @generated
	 */
	protected static void createVesselStateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_VesselState()),
      "Vessel State");
  }
		
	/**
	 * Create an editor for the route feature on Journey
	 * @generated
	 */
	protected static void createRouteEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_Route()),
      "Route");
  }
		
	/**
	 * Create an editor for the speed feature on Journey
	 * @generated
	 */
	protected static void createSpeedEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_Speed()),
      "Speed");
  }
		
	/**
	 * Create an editor for the distance feature on Journey
	 * @generated
	 */
	protected static void createDistanceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_Distance()),
      "Distance");
  }
		
	/**
	 * Create an editor for the routeCost feature on Journey
	 * @generated
	 */
	protected static void createRouteCostEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_RouteCost()),
      "Route Cost");
  }
		
	/**
	 * Create an editor for the toPort feature on Journey
	 * @generated
	 */
	protected static void createToPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_ToPort()),
      "To Port");
  }
		
	/**
	 * Create an editor for the fromPort feature on Journey
	 * @generated
	 */
	protected static void createFromPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getJourney_FromPort()),
      "From Port");
  }
}
