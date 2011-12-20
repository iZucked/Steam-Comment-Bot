package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Optimisation instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class OptimisationComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public OptimisationComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public OptimisationComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Optimisation", validate);
	}

	public OptimisationComposite(final Composite container, final int style) {
		this(container, style, "Optimisation", true);
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
    createOptimisationFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of Optimisation.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to Optimisation
	 * @generated
	 */
	protected static void createOptimisationFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createAllSettingsEditor(composite, mainGroup);
    createCurrentSettingsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the allSettings feature on Optimisation
	 * @generated
	 */
	protected static void createAllSettingsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisation_AllSettings()),
      "All Settings");
  }
		
	/**
	 * Create an editor for the currentSettings feature on Optimisation
	 * @generated
	 */
	protected static void createCurrentSettingsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisation_CurrentSettings()),
      "Current Settings");
  }
}
