/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing ConstrainedMoveGeneratorSettings instances
 * 
 * @generated
 */
public class ConstrainedMoveGeneratorSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public ConstrainedMoveGeneratorSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public ConstrainedMoveGeneratorSettingsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Constrained Move Generator Settings", validate);
	}

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public ConstrainedMoveGeneratorSettingsComposite(final Composite container, final int style) {
		this(container, style, "Constrained Move Generator Settings", true);
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
		createConstrainedMoveGeneratorSettingsFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of ConstrainedMoveGeneratorSettings.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		MoveGeneratorSettingsComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to ConstrainedMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createConstrainedMoveGeneratorSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

}
