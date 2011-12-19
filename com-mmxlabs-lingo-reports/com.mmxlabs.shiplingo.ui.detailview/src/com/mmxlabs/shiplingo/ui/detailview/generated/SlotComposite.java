package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.cargo.CargoPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing Slot instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class SlotComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public SlotComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public SlotComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Slot", validate);
	}

	public SlotComposite(final Composite container, final int style) {
		this(container, style, "Slot", true);
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
    createSlotFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Slot.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to Slot
	 * @generated
	 */
	protected static void createSlotFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createIdEditor(composite, mainGroup);
    createPortEditor(composite, mainGroup);
    createWindowStartEditor(composite, mainGroup);
    createContractEditor(composite, mainGroup);
    createFixedPriceEditor(composite, mainGroup);
    createMinQuantityEditor(composite, mainGroup);
    createMaxQuantityEditor(composite, mainGroup);
    createWindowDurationEditor(composite, mainGroup);
    createSlotDurationEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the id feature on Slot
	 * @generated NO hide editor
	 */
	protected static void createIdEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
//    composite.createEditorControl(mainGroup,
//      composite.createEditor(CargoPackage.eINSTANCE.getSlot_Id()),
//      "Id");
  }
		
	/**
	 * Create an editor for the minQuantity feature on Slot
	 * @generated
	 */
	protected static void createMinQuantityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_MinQuantity()),
      "Min Quantity");
  }
		
	/**
	 * Create an editor for the maxQuantity feature on Slot
	 * @generated
	 */
	protected static void createMaxQuantityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_MaxQuantity()),
      "Max Quantity");
  }
		
	/**
	 * Create an editor for the port feature on Slot
	 * @generated
	 */
	protected static void createPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_Port()),
      "Port");
  }
		
	/**
	 * Create an editor for the windowStart feature on Slot
	 * @generated
	 */
	protected static void createWindowStartEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_WindowStart()),
      "Window Start");
  }
		
	/**
	 * Create an editor for the windowDuration feature on Slot
	 * @generated
	 */
	protected static void createWindowDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_WindowDuration()),
      "Window Duration");
  }
		
	/**
	 * Create an editor for the slotDuration feature on Slot
	 * @generated
	 */
	protected static void createSlotDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_SlotDuration()),
      "Slot Duration");
  }
		
	/**
	 * Create an editor for the fixedPrice feature on Slot
	 * @generated
	 */
	protected static void createFixedPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_FixedPrice()),
      "Fixed Price");
  }
		
	/**
	 * Create an editor for the contract feature on Slot
	 * @generated
	 */
	protected static void createContractEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getSlot_Contract()),
      "Contract");
  }
}
