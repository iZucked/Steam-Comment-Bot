package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing ContractModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ContractModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ContractModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ContractModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Contract Model", validate);
	}

	public ContractModelComposite(final Composite container, final int style) {
		this(container, style, "Contract Model", true);
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
    createContractModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of ContractModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to ContractModel
	 * @generated
	 */
	protected static void createContractModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVolumeConstraintsEditor(composite, mainGroup);
    createEntitiesEditor(composite, mainGroup);
    createShippingEntityEditor(composite, mainGroup);
    createPurchaseContractsEditor(composite, mainGroup);
    createSalesContractsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the volumeConstraints feature on ContractModel
	 * @generated
	 */
	protected static void createVolumeConstraintsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getContractModel_VolumeConstraints()),
      "Volume Constraints");
  }
		
	/**
	 * Create an editor for the entities feature on ContractModel
	 * @generated
	 */
	protected static void createEntitiesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getContractModel_Entities()),
      "Entities");
  }
		
	/**
	 * Create an editor for the shippingEntity feature on ContractModel
	 * @generated
	 */
	protected static void createShippingEntityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final EntityComposite sub = 
      new EntityComposite(composite, composite.getStyle(), 
        "Shipping Entity", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), ContractPackage.eINSTANCE.getContractModel_ShippingEntity()));
    composite.addSubEditor(sub);
  }
		
	/**
	 * Create an editor for the purchaseContracts feature on ContractModel
	 * @generated
	 */
	protected static void createPurchaseContractsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getContractModel_PurchaseContracts()),
      "Purchase Contracts");
  }
		
	/**
	 * Create an editor for the salesContracts feature on ContractModel
	 * @generated
	 */
	protected static void createSalesContractsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(ContractPackage.eINSTANCE.getContractModel_SalesContracts()),
      "Sales Contracts");
  }
}
