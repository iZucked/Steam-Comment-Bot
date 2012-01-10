/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing SlotVisit instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class SlotVisitComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public SlotVisitComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public SlotVisitComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Slot Visit", validate);
	}

	public SlotVisitComposite(final Composite container, final int style) {
		this(container, style, "Slot Visit", true);
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
    createSlotVisitFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of SlotVisit.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      PortVisitComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to SlotVisit
	 * @generated
	 */
	protected static void createSlotVisitFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCargoAllocationEditor(composite, mainGroup);
    createSlotEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the cargoAllocation feature on SlotVisit
	 * @generated
	 */
	protected static void createCargoAllocationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation()),
      "Cargo Allocation");
  }
		
	/**
	 * Create an editor for the slot feature on SlotVisit
	 * @generated
	 */
	protected static void createSlotEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getSlotVisit_Slot()),
      "Slot");
  }
}
