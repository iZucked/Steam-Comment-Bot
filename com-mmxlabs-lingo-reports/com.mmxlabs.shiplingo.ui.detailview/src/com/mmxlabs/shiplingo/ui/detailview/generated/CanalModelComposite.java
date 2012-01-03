/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing CanalModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class CanalModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public CanalModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public CanalModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Canal Model", validate);
	}

	public CanalModelComposite(final Composite container, final int style) {
		this(container, style, "Canal Model", true);
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
    createCanalModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of CanalModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to CanalModel
	 * @generated
	 */
	protected static void createCanalModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCanalsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the canals feature on CanalModel
	 * @generated
	 */
	protected static void createCanalsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(PortPackage.eINSTANCE.getCanalModel_Canals()),
      "Canals");
  }
}
