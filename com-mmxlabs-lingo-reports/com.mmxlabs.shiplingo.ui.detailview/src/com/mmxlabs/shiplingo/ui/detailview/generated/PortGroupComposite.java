package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing PortGroup instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy
 * for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class PortGroupComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public PortGroupComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public PortGroupComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port Group", validate);
	}

	public PortGroupComposite(final Composite container, final int style) {
		this(container, style, "Port Group", true);
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
		createPortGroupFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of PortGroup.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		PortSelectionComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to PortGroup
	 * 
	 * @generated
	 */
	protected static void createPortGroupFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createContentsEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the contents feature on PortGroup
	 * 
	 * @generated
	 */
	protected static void createContentsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPortGroup_Contents()), "Contents");
	}
}
