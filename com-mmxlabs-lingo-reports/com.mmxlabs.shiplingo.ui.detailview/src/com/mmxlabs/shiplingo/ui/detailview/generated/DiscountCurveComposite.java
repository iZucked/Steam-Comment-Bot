package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.OptimiserPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing DiscountCurve instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class DiscountCurveComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public DiscountCurveComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public DiscountCurveComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Discount Curve", validate);
	}

	public DiscountCurveComposite(final Composite container, final int style) {
		this(container, style, "Discount Curve", true);
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
    createDiscountCurveFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of DiscountCurve.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to DiscountCurve
	 * @generated
	 */
	protected static void createDiscountCurveFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createStartDateEditor(composite, mainGroup);
    createDiscountsEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the startDate feature on DiscountCurve
	 * @generated
	 */
	protected static void createStartDateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getDiscountCurve_StartDate()),
      "Start Date");
  }
		
	/**
	 * Create an editor for the discounts feature on DiscountCurve
	 * @generated
	 */
	protected static void createDiscountsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(OptimiserPackage.eINSTANCE.getDiscountCurve_Discounts()),
      "Discounts");
  }
}
