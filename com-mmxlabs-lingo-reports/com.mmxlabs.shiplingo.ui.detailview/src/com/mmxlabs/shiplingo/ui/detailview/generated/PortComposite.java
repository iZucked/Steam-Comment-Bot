/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.editors.TimezoneInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.ValueListInlineEditor;

/**
 * A composite containing a form for editing Port instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy for
 * the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class PortComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public PortComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public PortComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port", validate);
	}

	public PortComposite(final Composite container, final int style) {
		this(container, style, "Port", true);
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
	 * @generated NO notes last
	 */
	protected static void createFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createFieldsFromSupers(composite, mainGroup);
		createPortFields(composite, mainGroup);
		AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Port.
	 * 
	 * @generated NO create notes last
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		UUIDObjectComposite.createFields(composite, mainGroup);
		NamedObjectComposite.createFields(composite, mainGroup);
		// AnnotatedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Port
	 * 
	 * @generated
	 */
	protected static void createPortFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createTimeZoneEditor(composite, mainGroup);
		createDefaultCVvalueEditor(composite, mainGroup);
		createDefaultWindowStartEditor(composite, mainGroup);
		createDefaultSlotDurationEditor(composite, mainGroup);
		createShouldArriveColdEditor(composite, mainGroup);
		createDefaultLoadDurationEditor(composite, mainGroup);
		createDefaultDischargeDurationEditor(composite, mainGroup);
		createCapabilitiesEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the capabilities feature on Port
	 * 
	 * @generated
	 */
	protected static void createCapabilitiesEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_Capabilities()), "Capabilities");
	}

	/**
	 * Create an editor for the timeZone feature on Port
	 * 
	 * @generated NO custom editor
	 */
	protected static void createTimeZoneEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup,
				new TimezoneInlineEditor(composite.getInputPath(), PortPackage.eINSTANCE.getPort_TimeZone(), composite.getEditingDomain(), composite.getCommandProcessor()), "Time Zone");
	}

	/**
	 * Create an editor for the defaultCVvalue feature on Port
	 * 
	 * @generated NO label change
	 */
	protected static void createDefaultCVvalueEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_DefaultCVvalue()), "Default CV Value");
	}

	/**
	 * Create an editor for the defaultWindowStart feature on Port
	 * 
	 * @generated NO custom editor
	 */
	protected static void createDefaultWindowStartEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		final List<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (int i = 0; i < 24; i++) {
			values.add(new Pair<String, Object>(String.format("%02d:00", i), i));
		}

		composite.createEditorControl(mainGroup,
				new ValueListInlineEditor(composite.getInputPath(), PortPackage.eINSTANCE.getPort_DefaultWindowStart(), composite.getEditingDomain(), composite.getCommandProcessor(), values),
				"Default Window Start");
	}

	/**
	 * Create an editor for the defaultSlotDuration feature on Port
	 * 
	 * @generated
	 */
	protected static void createDefaultSlotDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_DefaultSlotDuration()), "Default Slot Duration");
	}

	/**
	 * Create an editor for the shouldArriveCold feature on Port
	 * 
	 * @generated
	 */
	protected static void createShouldArriveColdEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_ShouldArriveCold()), "Should Arrive Cold");
	}

	/**
	 * Create an editor for the defaultLoadDuration feature on Port
	 * 
	 * @generated
	 */
	protected static void createDefaultLoadDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_DefaultLoadDuration()), "Default Load Duration");
	}

	/**
	 * Create an editor for the defaultDischargeDuration feature on Port
	 * 
	 * @generated
	 */
	protected static void createDefaultDischargeDurationEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPort_DefaultDischargeDuration()), "Default Discharge Duration");
	}
}
