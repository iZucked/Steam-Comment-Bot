package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.fleet.FleetPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing PortExclusion instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class PortExclusionComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public PortExclusionComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public PortExclusionComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port Exclusion", validate);
	}

	public PortExclusionComposite(final Composite container, final int style) {
		this(container, style, "Port Exclusion", true);
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
    createPortExclusionFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of PortExclusion.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      ScenarioObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to PortExclusion
	 * @generated
	 */
	protected static void createPortExclusionFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createStartDateEditor(composite, mainGroup);
    createEndDateEditor(composite, mainGroup);
    createPortEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the startDate feature on PortExclusion
	 * @generated
	 */
	protected static void createStartDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortExclusion_StartDate()),
      "Start Date");
  }
		
	/**
	 * Create an editor for the endDate feature on PortExclusion
	 * @generated
	 */
	protected static void createEndDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortExclusion_EndDate()),
      "End Date");
  }
		
	/**
	 * Create an editor for the port feature on PortExclusion
	 * @generated
	 */
	protected static void createPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(FleetPackage.eINSTANCE.getPortExclusion_Port()),
      "Port");
  }
}
