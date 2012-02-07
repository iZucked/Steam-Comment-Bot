/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.optimiser.lso.LsoPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing RandomMoveGeneratorSettings instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the
 * java class hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class RandomMoveGeneratorSettingsComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public RandomMoveGeneratorSettingsComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public RandomMoveGeneratorSettingsComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Random Move Generator Settings", validate);
	}

	public RandomMoveGeneratorSettingsComposite(final Composite container, final int style) {
		this(container, style, "Random Move Generator Settings", true);
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
		createRandomMoveGeneratorSettingsFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of RandomMoveGeneratorSettings.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		MoveGeneratorSettingsComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createRandomMoveGeneratorSettingsFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createUsing2over2Editor(composite, mainGroup);
		createUsing3over2Editor(composite, mainGroup);
		createUsing4over1Editor(composite, mainGroup);
		createUsing4over2Editor(composite, mainGroup);
		createWeightFor2opt2Editor(composite, mainGroup);
		createWeightFor3opt2Editor(composite, mainGroup);
		createWeightFor4opt1Editor(composite, mainGroup);
		createWeightFor4opt2Editor(composite, mainGroup);
	}

	/**
	 * Create an editor for the using2over2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createUsing2over2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_Using2over2()), "Using2over2");
	}

	/**
	 * Create an editor for the using3over2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createUsing3over2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_Using3over2()), "Using3over2");
	}

	/**
	 * Create an editor for the using4over1 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createUsing4over1Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_Using4over1()), "Using4over1");
	}

	/**
	 * Create an editor for the using4over2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createUsing4over2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_Using4over2()), "Using4over2");
	}

	/**
	 * Create an editor for the weightFor2opt2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createWeightFor2opt2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_WeightFor2opt2()), "Weight For2opt2");
	}

	/**
	 * Create an editor for the weightFor3opt2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createWeightFor3opt2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_WeightFor3opt2()), "Weight For3opt2");
	}

	/**
	 * Create an editor for the weightFor4opt1 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createWeightFor4opt1Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_WeightFor4opt1()), "Weight For4opt1");
	}

	/**
	 * Create an editor for the weightFor4opt2 feature on RandomMoveGeneratorSettings
	 * 
	 * @generated
	 */
	protected static void createWeightFor4opt2Editor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(LsoPackage.eINSTANCE.getRandomMoveGeneratorSettings_WeightFor4opt2()), "Weight For4opt2");
	}
}
