package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing VesselEvent instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public abstract class VesselEventComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public VesselEventComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public VesselEventComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel Event", validate);
	}

	public VesselEventComposite(final Composite container, final int style) {
		this(container, style, "Vessel Event", true);
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
	 * @generated NO
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFieldsFromSupers(composite, mainGroup);
    createVesselEventFields(composite, mainGroup);
    AnnotatedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of VesselEvent.
	 * @generated NO
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
//      AnnotatedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to VesselEvent
	 * @generated
	 */
	protected static void createVesselEventFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createIdEditor(composite, mainGroup);
    createStartDateEditor(composite, mainGroup);
    createEndDateEditor(composite, mainGroup);
    createDurationEditor(composite, mainGroup);
    createVesselsEditor(composite, mainGroup);
    createVesselClassesEditor(composite, mainGroup);
    createStartPortEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the id feature on VesselEvent
	 * @generated
	 */
	protected static void createIdEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_Id()),
      "Id");
  }
		
	/**
	 * Create an editor for the startDate feature on VesselEvent
	 * @generated
	 */
	protected static void createStartDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_StartDate()),
      "Start Date");
  }
		
	/**
	 * Create an editor for the endDate feature on VesselEvent
	 * @generated
	 */
	protected static void createEndDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_EndDate()),
      "End Date");
  }
		
	/**
	 * Create an editor for the duration feature on VesselEvent
	 * @generated
	 */
	protected static void createDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_Duration()),
      "Duration");
  }
		
	/**
	 * Create an editor for the vessels feature on VesselEvent
	 * @generated
	 */
	protected static void createVesselsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_Vessels()),
      "Vessels");
  }
		
	/**
	 * Create an editor for the vesselClasses feature on VesselEvent
	 * @generated
	 */
	protected static void createVesselClassesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_VesselClasses()),
      "Vessel Classes");
  }
		
	/**
	 * Create an editor for the startPort feature on VesselEvent
	 * @generated
	 */
	protected static void createStartPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getVesselEvent_StartPort()),
      "Start Port");
  }
}
