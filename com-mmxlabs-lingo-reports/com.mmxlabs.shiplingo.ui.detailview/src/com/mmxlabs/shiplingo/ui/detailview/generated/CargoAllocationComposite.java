package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing CargoAllocation instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class CargoAllocationComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public CargoAllocationComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public CargoAllocationComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Cargo Allocation", validate);
	}

	public CargoAllocationComposite(final Composite container, final int style) {
		this(container, style, "Cargo Allocation", true);
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
    createCargoAllocationFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of CargoAllocation.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to CargoAllocation
	 * @generated
	 */
	protected static void createCargoAllocationFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFuelVolumeEditor(composite, mainGroup);
    createDischargeVolumeEditor(composite, mainGroup);
    createLoadDateEditor(composite, mainGroup);
    createDischargeDateEditor(composite, mainGroup);
    createLoadPriceM3Editor(composite, mainGroup);
    createDischargePriceM3Editor(composite, mainGroup);
    createLoadRevenueEditor(composite, mainGroup);
    createShippingRevenueEditor(composite, mainGroup);
    createDischargeRevenueEditor(composite, mainGroup);
    createLadenLegEditor(composite, mainGroup);
    createBallastLegEditor(composite, mainGroup);
    createLadenIdleEditor(composite, mainGroup);
    createBallastIdleEditor(composite, mainGroup);
    createLoadSlotVisitEditor(composite, mainGroup);
    createDischargeSlotVisitEditor(composite, mainGroup);
    createVesselEditor(composite, mainGroup);
    createDischargeSlotEditor(composite, mainGroup);
    createLoadSlotEditor(composite, mainGroup);
    createCargoTypeEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the fuelVolume feature on CargoAllocation
	 * @generated
	 */
	protected static void createFuelVolumeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_FuelVolume()),
      "Fuel Volume");
  }
		
	/**
	 * Create an editor for the dischargeVolume feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargeVolumeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargeVolume()),
      "Discharge Volume");
  }
		
	/**
	 * Create an editor for the loadDate feature on CargoAllocation
	 * @generated
	 */
	protected static void createLoadDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LoadDate()),
      "Load Date");
  }
		
	/**
	 * Create an editor for the dischargeDate feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargeDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargeDate()),
      "Discharge Date");
  }
		
	/**
	 * Create an editor for the loadPriceM3 feature on CargoAllocation
	 * @generated
	 */
	protected static void createLoadPriceM3Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LoadPriceM3()),
      "Load Price M3");
  }
		
	/**
	 * Create an editor for the dischargePriceM3 feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargePriceM3Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargePriceM3()),
      "Discharge Price M3");
  }
		
	/**
	 * Create an editor for the loadRevenue feature on CargoAllocation
	 * @generated
	 */
	protected static void createLoadRevenueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LoadRevenue()),
      "Load Revenue");
  }
		
	/**
	 * Create an editor for the shippingRevenue feature on CargoAllocation
	 * @generated
	 */
	protected static void createShippingRevenueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_ShippingRevenue()),
      "Shipping Revenue");
  }
		
	/**
	 * Create an editor for the dischargeRevenue feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargeRevenueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargeRevenue()),
      "Discharge Revenue");
  }
		
	/**
	 * Create an editor for the ladenLeg feature on CargoAllocation
	 * @generated
	 */
	protected static void createLadenLegEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LadenLeg()),
      "Laden Leg");
  }
		
	/**
	 * Create an editor for the ballastLeg feature on CargoAllocation
	 * @generated
	 */
	protected static void createBallastLegEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_BallastLeg()),
      "Ballast Leg");
  }
		
	/**
	 * Create an editor for the ladenIdle feature on CargoAllocation
	 * @generated
	 */
	protected static void createLadenIdleEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LadenIdle()),
      "Laden Idle");
  }
		
	/**
	 * Create an editor for the ballastIdle feature on CargoAllocation
	 * @generated
	 */
	protected static void createBallastIdleEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_BallastIdle()),
      "Ballast Idle");
  }
		
	/**
	 * Create an editor for the loadSlotVisit feature on CargoAllocation
	 * @generated
	 */
	protected static void createLoadSlotVisitEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LoadSlotVisit()),
      "Load Slot Visit");
  }
		
	/**
	 * Create an editor for the dischargeSlotVisit feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargeSlotVisitEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargeSlotVisit()),
      "Discharge Slot Visit");
  }
		
	/**
	 * Create an editor for the vessel feature on CargoAllocation
	 * @generated
	 */
	protected static void createVesselEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_Vessel()),
      "Vessel");
  }
		
	/**
	 * Create an editor for the dischargeSlot feature on CargoAllocation
	 * @generated
	 */
	protected static void createDischargeSlotEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_DischargeSlot()),
      "Discharge Slot");
  }
		
	/**
	 * Create an editor for the loadSlot feature on CargoAllocation
	 * @generated
	 */
	protected static void createLoadSlotEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_LoadSlot()),
      "Load Slot");
  }
		
	/**
	 * Create an editor for the cargoType feature on CargoAllocation
	 * @generated
	 */
	protected static void createCargoTypeEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getCargoAllocation_CargoType()),
      "Cargo Type");
  }
}
