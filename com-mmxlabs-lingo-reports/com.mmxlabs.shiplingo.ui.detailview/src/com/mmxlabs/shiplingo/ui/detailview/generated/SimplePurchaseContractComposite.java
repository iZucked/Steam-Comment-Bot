/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing SimplePurchaseContract instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public abstract class SimplePurchaseContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public SimplePurchaseContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public SimplePurchaseContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Simple Purchase Contract", validate);
	}

	public SimplePurchaseContractComposite(final Composite container, final int style) {
		this(container, style, "Simple Purchase Contract", true);
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
    createSimplePurchaseContractFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of SimplePurchaseContract.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      PurchaseContractComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to SimplePurchaseContract
	 * @generated
	 */
	protected static void createSimplePurchaseContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCooldownPortsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the cooldownPorts feature on SimplePurchaseContract
	 * @generated
	 */
	protected static void createCooldownPortsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getSimplePurchaseContract_CooldownPorts()),
      "Cooldown Ports");
  }
}
