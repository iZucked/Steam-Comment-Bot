/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.port.PortPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing PortModel instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy
 * for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class PortModelComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public PortModelComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public PortModelComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Port Model", validate);
	}

	public PortModelComposite(final Composite container, final int style) {
		this(container, style, "Port Model", true);
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
		createPortModelFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of PortModel.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
	}

	/**
	 * Create fields belonging directly to PortModel
	 * 
	 * @generated
	 */
	protected static void createPortModelFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createPortsEditor(composite, mainGroup);
		createPortGroupsEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the ports feature on PortModel
	 * 
	 * @generated
	 */
	protected static void createPortsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPortModel_Ports()), "Ports");
	}

	/**
	 * Create an editor for the portGroups feature on PortModel
	 * 
	 * @generated
	 */
	protected static void createPortGroupsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(PortPackage.eINSTANCE.getPortModel_PortGroups()), "Port Groups");
	}
}
