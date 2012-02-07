/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing NamedObject instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class
 * hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public abstract class NamedObjectComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public NamedObjectComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public NamedObjectComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Named Object", validate);
	}

	public NamedObjectComposite(final Composite container, final int style) {
		this(container, style, "Named Object", true);
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
		createNamedObjectFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of NamedObject.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		ScenarioObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to NamedObject
	 * 
	 * @generated
	 */
	protected static void createNamedObjectFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createNameEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the name feature on NamedObject
	 * 
	 * @generated
	 */
	protected static void createNameEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ScenarioPackage.eINSTANCE.getNamedObject_Name()), "Name");
	}
}
