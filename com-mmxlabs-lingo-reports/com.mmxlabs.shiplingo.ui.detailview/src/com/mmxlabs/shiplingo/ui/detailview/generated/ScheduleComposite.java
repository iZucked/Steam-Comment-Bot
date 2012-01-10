/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing Schedule instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ScheduleComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ScheduleComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ScheduleComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Schedule", validate);
	}

	public ScheduleComposite(final Composite container, final int style) {
		this(container, style, "Schedule", true);
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
    createScheduleFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Schedule.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to Schedule
	 * @generated
	 */
	protected static void createScheduleFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createNameEditor(composite, mainGroup);
    createSequencesEditor(composite, mainGroup);
    createCargoAllocationsEditor(composite, mainGroup);
    createFitnessEditor(composite, mainGroup);
    createRevenueEditor(composite, mainGroup);
    createFleetEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the name feature on Schedule
	 * @generated
	 */
	protected static void createNameEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_Name()),
      "Name");
  }
		
	/**
	 * Create an editor for the sequences feature on Schedule
	 * @generated
	 */
	protected static void createSequencesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_Sequences()),
      "Sequences");
  }
		
	/**
	 * Create an editor for the cargoAllocations feature on Schedule
	 * @generated
	 */
	protected static void createCargoAllocationsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_CargoAllocations()),
      "Cargo Allocations");
  }
		
	/**
	 * Create an editor for the fitness feature on Schedule
	 * @generated
	 */
	protected static void createFitnessEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_Fitness()),
      "Fitness");
  }
		
	/**
	 * Create an editor for the revenue feature on Schedule
	 * @generated
	 */
	protected static void createRevenueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_Revenue()),
      "Revenue");
  }
		
	/**
	 * Create an editor for the fleet feature on Schedule
	 * @generated
	 */
	protected static void createFleetEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getSchedule_Fleet()),
      "Fleet");
  }
}
