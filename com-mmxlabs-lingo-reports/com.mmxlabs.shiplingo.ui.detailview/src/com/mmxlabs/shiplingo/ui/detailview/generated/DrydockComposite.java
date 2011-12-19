package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Drydock instances
 * @generated
 */
public  class DrydockComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

  /**
   * Call superclass constructor
     * @generated
   */
  public DrydockComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

  public DrydockComposite(final Composite container, final int style, final boolean validate) {
    this(container, style, "Drydock", validate);
  }

  /**
	 * Call superclass constructor
     * @generated
	 */
	public DrydockComposite(final Composite container, final int style) {
    this(container, style, "Drydock", true);
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
    createDrydockFields(composite, mainGroup);
  }

  /**
   * Create fields belonging to all the supertypes of Drydock.
   * @generated
   */
  protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      VesselEventComposite.createFields(composite, mainGroup);
  }

  /**
   * Create fields belonging directly to Drydock
   * @generated
   */
  protected static void createDrydockFields(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

}
