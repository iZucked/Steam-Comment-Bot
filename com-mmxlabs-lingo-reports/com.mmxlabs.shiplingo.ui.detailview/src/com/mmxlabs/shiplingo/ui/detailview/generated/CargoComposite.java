/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.cargo.CargoPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Cargo instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class CargoComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public CargoComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public CargoComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Cargo", validate);
	}

	public CargoComposite(final Composite container, final int style) {
		this(container, style, "Cargo", true);
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
	 * @generated NO create notes at end
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFieldsFromSupers(composite, mainGroup);
    createCargoFields(composite, mainGroup);
    AnnotatedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Cargo.
	 * @generated NO create notes field at end
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      
  }

	/**
	 * Create fields belonging directly to Cargo
	 * @generated
	 */
	protected static void createCargoFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createIdEditor(composite, mainGroup);
    createAllowedVesselsEditor(composite, mainGroup);
    createLoadSlotEditor(composite, mainGroup);
    createDischargeSlotEditor(composite, mainGroup);
    createCargoTypeEditor(composite, mainGroup);
    createAllowRewiringEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the id feature on Cargo
	 * @generated
	 */
	protected static void createIdEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargo_Id()),
      "Id");
  }
		
	/**
	 * Create an editor for the allowedVessels feature on Cargo
	 * @generated NO change label
	 */
	protected static void createAllowedVesselsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargo_AllowedVessels()),
      "Restrict To");
  }
		
	/**
	 * Create an editor for the loadSlot feature on Cargo
	 * @generated NO rename group
	 */
	protected static void createLoadSlotEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final LoadSlotComposite sub = 
      new LoadSlotComposite(composite, composite.getStyle(), 
        "Load", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), CargoPackage.eINSTANCE.getCargo_LoadSlot()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the dischargeSlot feature on Cargo
	 * @generated NO rename group
	 */
	protected static void createDischargeSlotEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final SlotComposite sub = 
      new SlotComposite(composite, composite.getStyle(), 
        "Discharge", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), CargoPackage.eINSTANCE.getCargo_DischargeSlot()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the cargoType feature on Cargo
	 * @generated
	 */
	protected static void createCargoTypeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargo_CargoType()),
      "Cargo Type");
  }

  /**
   * Create an editor for the allowRewiring feature on Cargo
   * @generated
   */
  protected static void createAllowRewiringEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargo_AllowRewiring()),
      "Allow Rewiring");
  }
}
