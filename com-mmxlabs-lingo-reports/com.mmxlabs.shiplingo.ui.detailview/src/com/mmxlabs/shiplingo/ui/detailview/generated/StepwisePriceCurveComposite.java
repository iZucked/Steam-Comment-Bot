package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.market.MarketPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing StepwisePriceCurve instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class StepwisePriceCurveComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public StepwisePriceCurveComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public StepwisePriceCurveComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Stepwise Price Curve", validate);
	}

	public StepwisePriceCurveComposite(final Composite container, final int style) {
		this(container, style, "Stepwise Price Curve", true);
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
    createStepwisePriceCurveFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of StepwisePriceCurve.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to StepwisePriceCurve
	 * @generated
	 */
	protected static void createStepwisePriceCurveFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createDefaultValueEditor(composite, mainGroup);
    createPricesEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the defaultValue feature on StepwisePriceCurve
	 * @generated
	 */
	protected static void createDefaultValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(MarketPackage.eINSTANCE.getStepwisePriceCurve_DefaultValue()),
      "Default Value");
  }
		
	/**
	 * Create an editor for the prices feature on StepwisePriceCurve
	 * @generated
	 */
	protected static void createPricesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(MarketPackage.eINSTANCE.getStepwisePriceCurve_Prices()),
      "Prices");
  }
}
