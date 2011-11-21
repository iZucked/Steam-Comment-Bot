package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing OptimisationSettings instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class OptimisationSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public OptimisationSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public OptimisationSettingsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Optimisation Settings", validate);
	}

	public OptimisationSettingsComposite(final Composite container, final int style) {
		this(container, style, "Optimisation Settings", true);
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
    createOptimisationSettingsFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of OptimisationSettings.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to OptimisationSettings
	 * @generated
	 */
	protected static void createOptimisationSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createRandomSeedEditor(composite, mainGroup);
    createInitialScheduleEditor(composite, mainGroup);
    createFreezeDaysFromStartEditor(composite, mainGroup);
    createIgnoreElementsAfterEditor(composite, mainGroup);
    createConstraintsEditor(composite, mainGroup);
    createObjectivesEditor(composite, mainGroup);
    createDefaultDiscountCurveEditor(composite, mainGroup);
    createAllowRewiringByDefaultEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the randomSeed feature on OptimisationSettings
	 * @generated
	 */
	protected static void createRandomSeedEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_RandomSeed()),
      "Random Seed");
  }
		
	/**
	 * Create an editor for the initialSchedule feature on OptimisationSettings
	 * @generated
	 */
	protected static void createInitialScheduleEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_InitialSchedule()),
      "Initial Schedule");
  }
		
	/**
	 * Create an editor for the freezeDaysFromStart feature on OptimisationSettings
	 * @generated
	 */
	protected static void createFreezeDaysFromStartEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_FreezeDaysFromStart()),
      "Freeze Days From Start");
  }
		
	/**
	 * Create an editor for the ignoreElementsAfter feature on OptimisationSettings
	 * @generated
	 */
	protected static void createIgnoreElementsAfterEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_IgnoreElementsAfter()),
      "Ignore Elements After");
  }
		
	/**
	 * Create an editor for the constraints feature on OptimisationSettings
	 * @generated
	 */
	protected static void createConstraintsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_Constraints()),
      "Constraints");
  }
		
	/**
	 * Create an editor for the objectives feature on OptimisationSettings
	 * @generated
	 */
	protected static void createObjectivesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_Objectives()),
      "Objectives");
  }
		
	/**
	 * Create an editor for the defaultDiscountCurve feature on OptimisationSettings
	 * @generated
	 */
	protected static void createDefaultDiscountCurveEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    final DiscountCurveComposite sub = 
      new DiscountCurveComposite(composite, composite.getStyle(), 
        "Default Discount Curve", false);
    sub.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));
    sub.setPath(new CompiledEMFPath(composite.getInputPath(), OptimiserPackage.eINSTANCE.getOptimisationSettings_DefaultDiscountCurve()));
    composite.addSubEditor(sub);
  }

  /**
   * Create an editor for the allowRewiringByDefault feature on OptimisationSettings
   * @generated
   */
  protected static void createAllowRewiringByDefaultEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getOptimisationSettings_AllowRewiringByDefault()),
      "Allow Rewiring By Default");
  }
}
