package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing DistanceLine instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class DistanceLineComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public DistanceLineComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public DistanceLineComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Distance Line", validate);
	}

	public DistanceLineComposite(final Composite container, final int style) {
		this(container, style, "Distance Line", true);
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
    createDistanceLineFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of DistanceLine.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to DistanceLine
	 * @generated
	 */
	protected static void createDistanceLineFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createFromPortEditor(composite, mainGroup);
    createToPortEditor(composite, mainGroup);
    createDistanceEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the fromPort feature on DistanceLine
	 * @generated
	 */
	protected static void createFromPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(PortPackage.eINSTANCE.getDistanceLine_FromPort()),
      "From Port");
  }
		
	/**
	 * Create an editor for the toPort feature on DistanceLine
	 * @generated
	 */
	protected static void createToPortEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(PortPackage.eINSTANCE.getDistanceLine_ToPort()),
      "To Port");
  }
		
	/**
	 * Create an editor for the distance feature on DistanceLine
	 * @generated
	 */
	protected static void createDistanceEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(PortPackage.eINSTANCE.getDistanceLine_Distance()),
      "Distance");
  }
}
