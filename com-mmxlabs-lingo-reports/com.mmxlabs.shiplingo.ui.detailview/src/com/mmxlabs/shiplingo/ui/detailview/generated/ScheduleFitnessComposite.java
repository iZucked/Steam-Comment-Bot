package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing ScheduleFitness instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class ScheduleFitnessComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public ScheduleFitnessComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public ScheduleFitnessComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Schedule Fitness", validate);
	}

	public ScheduleFitnessComposite(final Composite container, final int style) {
		this(container, style, "Schedule Fitness", true);
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
    createScheduleFitnessFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of ScheduleFitness.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to ScheduleFitness
	 * @generated
	 */
	protected static void createScheduleFitnessFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createNameEditor(composite, mainGroup);
    createValueEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the name feature on ScheduleFitness
	 * @generated
	 */
	protected static void createNameEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getScheduleFitness_Name()),
      "Name");
  }
		
	/**
	 * Create an editor for the value feature on ScheduleFitness
	 * @generated
	 */
	protected static void createValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getScheduleFitness_Value()),
      "Value");
  }
}
