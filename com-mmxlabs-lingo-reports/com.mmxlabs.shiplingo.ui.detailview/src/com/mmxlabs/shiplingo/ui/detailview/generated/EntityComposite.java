/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Entity instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for
 * the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class EntityComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public EntityComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public EntityComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Entity", validate);
	}

	public EntityComposite(final Composite container, final int style) {
		this(container, style, "Entity", true);
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
		createEntityFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Entity.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		NamedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Entity
	 * 
	 * @generated
	 */
	protected static void createEntityFields(final AbstractDetailComposite composite, final Composite mainGroup) {
	}
}
