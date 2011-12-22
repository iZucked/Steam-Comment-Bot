package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing VesselEventRevenue instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class VesselEventRevenueComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public VesselEventRevenueComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public VesselEventRevenueComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Vessel Event Revenue", validate);
	}

	public VesselEventRevenueComposite(final Composite container, final int style) {
		this(container, style, "Vessel Event Revenue", true);
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
    createVesselEventRevenueFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of VesselEventRevenue.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      BookedRevenueComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to VesselEventRevenue
	 * @generated
	 */
	protected static void createVesselEventRevenueFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createVesselEventVisitEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the vesselEventVisit feature on VesselEventRevenue
	 * @generated
	 */
	protected static void createVesselEventVisitEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getVesselEventRevenue_VesselEventVisit()),
      "Vessel Event Visit");
  }
}
