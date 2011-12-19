package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing DistanceModel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class DistanceModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public DistanceModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public DistanceModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Distance Model", validate);
	}

	public DistanceModelComposite(final Composite container, final int style) {
		this(container, style, "Distance Model", true);
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
    createDistanceModelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of DistanceModel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to DistanceModel
	 * @generated
	 */
	protected static void createDistanceModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createDistancesEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the distances feature on DistanceModel
	 * @generated
	 */
	protected static void createDistancesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(PortPackage.eINSTANCE.getDistanceModel_Distances()),
      "Distances");
  }
}
