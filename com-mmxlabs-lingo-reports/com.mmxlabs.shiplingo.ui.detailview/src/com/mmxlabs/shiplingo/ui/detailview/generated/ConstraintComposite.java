package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Constraint instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ConstraintComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ConstraintComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ConstraintComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Constraint", validate);
	}

	public ConstraintComposite(final Composite container, final int style) {
		this(container, style, "Constraint", true);
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
    createConstraintFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Constraint.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to Constraint
	 * @generated
	 */
	protected static void createConstraintFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createEnabledEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the enabled feature on Constraint
	 * @generated
	 */
	protected static void createEnabledEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getConstraint_Enabled()),
      "Enabled");
  }
}
