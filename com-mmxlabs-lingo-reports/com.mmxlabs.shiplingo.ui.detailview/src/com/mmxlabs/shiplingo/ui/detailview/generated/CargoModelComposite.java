package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.cargo.CargoPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing CargoModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class CargoModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public CargoModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public CargoModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Cargo Model", validate);
	}

	public CargoModelComposite(final Composite container, final int style) {
		this(container, style, "Cargo Model", true);
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
    createCargoModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of CargoModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to CargoModel
	 * @generated
	 */
	protected static void createCargoModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createCargoesEditor(composite, mainGroup);
    createSpareDischargeSlotsEditor(composite, mainGroup);
    createSpareLoadSlotsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the cargoes feature on CargoModel
	 * @generated
	 */
	protected static void createCargoesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargoModel_Cargoes()),
      "Cargoes");
  }
		
	/**
	 * Create an editor for the spareDischargeSlots feature on CargoModel
	 * @generated
	 */
	protected static void createSpareDischargeSlotsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargoModel_SpareDischargeSlots()),
      "Spare Discharge Slots");
  }
		
	/**
	 * Create an editor for the spareLoadSlots feature on CargoModel
	 * @generated
	 */
	protected static void createSpareLoadSlotsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(CargoPackage.eINSTANCE.getCargoModel_SpareLoadSlots()),
      "Spare Load Slots");
  }
}
