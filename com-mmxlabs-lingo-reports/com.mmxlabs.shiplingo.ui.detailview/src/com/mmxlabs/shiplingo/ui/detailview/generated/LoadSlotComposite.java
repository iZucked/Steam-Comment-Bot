/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.cargo.CargoPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing LoadSlot instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class LoadSlotComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public LoadSlotComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public LoadSlotComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Load Slot", validate);
	}

	public LoadSlotComposite(final Composite container, final int style) {
		this(container, style, "Load Slot", true);
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
    createLoadSlotFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of LoadSlot.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      SlotComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to LoadSlot
	 * @generated
	 */
	protected static void createLoadSlotFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCargoCVvalueEditor(composite, mainGroup);
    createArriveColdEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the cargoCVvalue feature on LoadSlot
	 * @generated NO change label
	 */
	protected static void createCargoCVvalueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getLoadSlot_CargoCVvalue()),
      "Cargo CV");
  }

  /**
   * Create an editor for the arriveCold feature on LoadSlot
   * @generated
   */
  protected static void createArriveColdEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getLoadSlot_ArriveCold()),
      "Arrive Cold");
  }
}
