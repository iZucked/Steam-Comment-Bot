/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing AllocatedVessel instances
 * @generated
 */
public  class AllocatedVesselComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

  /**
   * Call superclass constructor
     * @generated
   */
  public AllocatedVesselComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

  public AllocatedVesselComposite(final Composite container, final int style, final boolean validate) {
    this(container, style, "Allocated Vessel", validate);
  }

  /**
	 * Call superclass constructor
     * @generated
	 */
	public AllocatedVesselComposite(final Composite container, final int style) {
    this(container, style, "Allocated Vessel", true);
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
    createAllocatedVesselFields(composite, mainGroup);
  }

  /**
   * Create fields belonging to all the supertypes of AllocatedVessel.
   * @generated
   */
  protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

  /**
   * Create fields belonging directly to AllocatedVessel
   * @generated
   */
  protected static void createAllocatedVesselFields(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

}
