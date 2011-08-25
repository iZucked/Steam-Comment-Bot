package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing FuelConsumptionLine instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class FuelConsumptionLineComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public FuelConsumptionLineComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public FuelConsumptionLineComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Fuel Consumption Line", validate);
	}

	public FuelConsumptionLineComposite(final Composite container, final int style) {
		this(container, style, "Fuel Consumption Line", true);
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
    createFuelConsumptionLineFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of FuelConsumptionLine.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to FuelConsumptionLine
	 * @generated
	 */
	protected static void createFuelConsumptionLineFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createSpeedEditor(composite, mainGroup);
    createConsumptionEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the speed feature on FuelConsumptionLine
	 * @generated
	 */
	protected static void createSpeedEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFuelConsumptionLine_Speed()),
      "Speed");
  }
		
	/**
	 * Create an editor for the consumption feature on FuelConsumptionLine
	 * @generated
	 */
	protected static void createConsumptionEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getFuelConsumptionLine_Consumption()),
      "Consumption");
  }
}
