package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.events.EventsPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FuelQuantity instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FuelQuantityComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FuelQuantityComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FuelQuantityComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fuel Quantity", validate);
	}

	public FuelQuantityComposite(final Composite container, final int style) {
		this(container, style, "Fuel Quantity", true);
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
    createFuelQuantityFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FuelQuantity.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to FuelQuantity
	 * @generated
	 */
	protected static void createFuelQuantityFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFuelTypeEditor(composite, mainGroup);
    createQuantityEditor(composite, mainGroup);
    createUnitPriceEditor(composite, mainGroup);
    createTotalPriceEditor(composite, mainGroup);
    createFuelUnitEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the fuelType feature on FuelQuantity
	 * @generated
	 */
	protected static void createFuelTypeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelQuantity_FuelType()),
      "Fuel Type");
  }
		
	/**
	 * Create an editor for the quantity feature on FuelQuantity
	 * @generated
	 */
	protected static void createQuantityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelQuantity_Quantity()),
      "Quantity");
  }
		
	/**
	 * Create an editor for the unitPrice feature on FuelQuantity
	 * @generated
	 */
	protected static void createUnitPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelQuantity_UnitPrice()),
      "Unit Price");
  }
		
	/**
	 * Create an editor for the totalPrice feature on FuelQuantity
	 * @generated
	 */
	protected static void createTotalPriceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelQuantity_TotalPrice()),
      "Total Price");
  }
		
	/**
	 * Create an editor for the fuelUnit feature on FuelQuantity
	 * @generated
	 */
	protected static void createFuelUnitEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(EventsPackage.eINSTANCE.getFuelQuantity_FuelUnit()),
      "Fuel Unit");
  }
}
