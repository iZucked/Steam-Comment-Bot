package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing UUIDObject instances. The EClass hierarchy is implemented
 * by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for the composites,
 * because ECore supports multiple inheritance but java does not.
 *
 * @generated
 */
public abstract class UUIDObjectComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;
	/**
	 * Call superclass constructor
     * @generated
	 */
	public UUIDObjectComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
    super(container, style, validate);
    this.mainGroupTitle = mainGroupTitle;
  }

	public UUIDObjectComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "U UIDObject", validate);
	}

	public UUIDObjectComposite(final Composite container, final int style) {
		this(container, style, "U UIDObject", true);
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
    createUUIDObjectFields(composite, mainGroup);
  }

	/**
	 * Create fields belonging to all the supertypes of UUIDObject.
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
  }

	/**
	 * Create fields belonging directly to UUIDObject
	 * @generated
	 */
	protected static void createUUIDObjectFields(final AbstractDetailComposite composite, final Composite mainGroup) {
    createUUIDEditor(composite, mainGroup);
  }

		
	/**
	 * Create an editor for the UUID feature on UUIDObject
	 * @generated NO don't create a uuid editor
	 */
	protected static void createUUIDEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
//    composite.createEditorControl(mainGroup,
//      composite.createEditor(ScenarioPackage.eINSTANCE.getUUIDObject_UUID()),
//      "UUID");
  }
}
