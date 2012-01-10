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
 * A composite containing a form for editing CargoRevenue instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class CargoRevenueComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public CargoRevenueComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public CargoRevenueComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Cargo Revenue", validate);
	}

	public CargoRevenueComposite(final Composite container, final int style) {
		this(container, style, "Cargo Revenue", true);
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
    createCargoRevenueFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of CargoRevenue.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      BookedRevenueComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to CargoRevenue
	 * @generated
	 */
	protected static void createCargoRevenueFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCargoEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the cargo feature on CargoRevenue
	 * @generated
	 */
	protected static void createCargoEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoRevenue_Cargo()),
      "Cargo");
  }
}
