package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.lngscheduler.emf.extras.CompiledEMFPath;

/**
 * A composite containing a form for editing LineItem instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public  class LineItemComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public LineItemComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public LineItemComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Line Item", validate);
	}

	public LineItemComposite(final Composite container, final int style) {
		this(container, style, "Line Item", true);
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
    createLineItemFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of LineItem.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
      NamedObjectComposite.createFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging directly to LineItem
	 * @generated
	 */
	protected static void createLineItemFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createValueEditor(composite, mainGroup);
    createPartyEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the value feature on LineItem
	 * @generated
	 */
	protected static void createValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getLineItem_Value()),
      "Value");
  }
		
	/**
	 * Create an editor for the party feature on LineItem
	 * @generated
	 */
	protected static void createPartyEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
    composite.createEditorControl(mainGroup,
      composite.createEditor(SchedulePackage.eINSTANCE.getLineItem_Party()),
      "Party");
  }
}
