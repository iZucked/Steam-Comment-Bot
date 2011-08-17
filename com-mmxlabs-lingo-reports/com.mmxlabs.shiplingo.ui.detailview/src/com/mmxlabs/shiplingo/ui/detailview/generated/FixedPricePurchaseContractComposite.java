package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FixedPricePurchaseContract instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FixedPricePurchaseContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FixedPricePurchaseContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FixedPricePurchaseContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fixed Price Purchase Contract", validate);
	}

	public FixedPricePurchaseContractComposite(final Composite container, final int style) {
		this(container, style, "Fixed Price Purchase Contract", true);
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
    createFixedPricePurchaseContractFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FixedPricePurchaseContract.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      SimplePurchaseContractComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to FixedPricePurchaseContract
	 * @generated
	 */
	protected static void createFixedPricePurchaseContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createUnitPriceEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the unitPrice feature on FixedPricePurchaseContract
	 * @generated
	 */
	protected static void createUnitPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getFixedPricePurchaseContract_UnitPrice()),
      "Unit Price");
  }
}
