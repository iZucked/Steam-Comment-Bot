/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing ProfitSharingPurchaseContract instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ProfitSharingPurchaseContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ProfitSharingPurchaseContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ProfitSharingPurchaseContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Profit Sharing Purchase Contract", validate);
	}

	public ProfitSharingPurchaseContractComposite(final Composite container, final int style) {
		this(container, style, "Profit Sharing Purchase Contract", true);
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
    createProfitSharingPurchaseContractFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of ProfitSharingPurchaseContract.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      PurchaseContractComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createProfitSharingPurchaseContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createAlphaEditor(composite, mainGroup);
    createBetaEditor(composite, mainGroup);
    createGammaEditor(composite, mainGroup);
    createIndexEditor(composite, mainGroup);
    createReferenceIndexEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the alpha feature on ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createAlphaEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getProfitSharingPurchaseContract_Alpha()),
      "Alpha");
  }
		
	/**
	 * Create an editor for the beta feature on ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createBetaEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getProfitSharingPurchaseContract_Beta()),
      "Beta");
  }
		
	/**
	 * Create an editor for the gamma feature on ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createGammaEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getProfitSharingPurchaseContract_Gamma()),
      "Gamma");
  }
		
	/**
	 * Create an editor for the index feature on ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createIndexEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getProfitSharingPurchaseContract_Index()),
      "Index");
  }
		
	/**
	 * Create an editor for the referenceIndex feature on ProfitSharingPurchaseContract
	 * @generated
	 */
	protected static void createReferenceIndexEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getProfitSharingPurchaseContract_ReferenceIndex()),
      "Reference Index");
  }
}
