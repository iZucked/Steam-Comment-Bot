/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing PortAndTime instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class PortAndTimeComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public PortAndTimeComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public PortAndTimeComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port And Time", validate);
	}

	public PortAndTimeComposite(final Composite container, final int style) {
		this(container, style, "Port And Time", true);
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
    createPortAndTimeFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of PortAndTime.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to PortAndTime
	 * @generated
	 */
	protected static void createPortAndTimeFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createStartTimeEditor(composite, mainGroup);
    createEndTimeEditor(composite, mainGroup);
    createPortEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the startTime feature on PortAndTime
	 * @generated
	 */
	protected static void createStartTimeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortAndTime_StartTime()),
      "Start Time");
  }
		
	/**
	 * Create an editor for the endTime feature on PortAndTime
	 * @generated
	 */
	protected static void createEndTimeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortAndTime_EndTime()),
      "End Time");
  }
		
	/**
	 * Create an editor for the port feature on PortAndTime
	 * @generated
	 */
	protected static void createPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortAndTime_Port()),
      "Port");
  }
}
