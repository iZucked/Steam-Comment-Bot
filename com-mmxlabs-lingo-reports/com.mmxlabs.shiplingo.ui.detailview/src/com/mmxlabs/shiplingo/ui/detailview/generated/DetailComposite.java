package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Detail instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for
 * the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class DetailComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public DetailComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public DetailComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Detail", validate);
	}

	public DetailComposite(final Composite container, final int style) {
		this(container, style, "Detail", true);
	}

	/**
	 * Create the main contents
	 * 
	 * @generated
	 */
	@Override
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
		createDetailFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Detail.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to Detail
	 * 
	 * @generated
	 */
	protected static void createDetailFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createNameEditor(composite, mainGroup);
		createChildrenEditor(composite, mainGroup);
		createValueEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the name feature on Detail
	 * 
	 * @generated
	 */
	protected static void createNameEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ScenarioPackage.eINSTANCE.getDetail_Name()), "Name");
	}

	/**
	 * Create an editor for the children feature on Detail
	 * 
	 * @generated
	 */
	protected static void createChildrenEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ScenarioPackage.eINSTANCE.getDetail_Children()), "Children");
	}

	/**
	 * Create an editor for the value feature on Detail
	 * 
	 * @generated
	 */
	protected static void createValueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ScenarioPackage.eINSTANCE.getDetail_Value()), "Value");
	}
}
