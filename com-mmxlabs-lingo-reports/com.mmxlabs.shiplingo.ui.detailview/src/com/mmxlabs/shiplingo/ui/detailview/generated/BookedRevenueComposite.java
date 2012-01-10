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
 * A composite containing a form for editing BookedRevenue instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class BookedRevenueComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public BookedRevenueComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public BookedRevenueComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Booked Revenue", validate);
	}

	public BookedRevenueComposite(final Composite container, final int style) {
		this(container, style, "Booked Revenue", true);
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
    createBookedRevenueFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of BookedRevenue.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      ScenarioObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to BookedRevenue
	 * @generated
	 */
	protected static void createBookedRevenueFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createDateEditor(composite, mainGroup);
    createEntityEditor(composite, mainGroup);
    createValueEditor(composite, mainGroup);
    createDetailsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the date feature on BookedRevenue
	 * @generated
	 */
	protected static void createDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getBookedRevenue_Date()),
      "Date");
  }
		
	/**
	 * Create an editor for the entity feature on BookedRevenue
	 * @generated
	 */
	protected static void createEntityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getBookedRevenue_Entity()),
      "Entity");
  }
		
	/**
	 * Create an editor for the value feature on BookedRevenue
	 * @generated
	 */
	protected static void createValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getBookedRevenue_Value()),
      "Value");
  }

  /**
   * Create an editor for the details feature on BookedRevenue
   * @generated
   */
  protected static void createDetailsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final DetailComposite sub = 
      new DetailComposite(composite, composite.getStyle(), 
        "Details", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), SchedulePackage.eINSTANCE.getBookedRevenue_Details()));
    composite.addSubEditor(sub);
  }
}
