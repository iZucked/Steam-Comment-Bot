package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing PortTimeAndHeel instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class PortTimeAndHeelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public PortTimeAndHeelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public PortTimeAndHeelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port Time And Heel", validate);
	}

	public PortTimeAndHeelComposite(final Composite container, final int style) {
		this(container, style, "Port Time And Heel", true);
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
    createPortTimeAndHeelFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of PortTimeAndHeel.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      PortAndTimeComposite.createFields(composite, mainGroup);
      HeelOptionsComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to PortTimeAndHeel
	 * @generated
	 */
	protected static void createPortTimeAndHeelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

}
