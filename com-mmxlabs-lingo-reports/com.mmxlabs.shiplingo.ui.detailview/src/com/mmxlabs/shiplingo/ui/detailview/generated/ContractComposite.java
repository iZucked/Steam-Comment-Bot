/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing Contract instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class hierarchy
 * for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public abstract class ContractComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public ContractComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public ContractComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Contract", validate);
	}

	public ContractComposite(final Composite container, final int style) {
		this(container, style, "Contract", true);
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
		createContractFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of Contract.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		NamedObjectComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to Contract
	 * 
	 * @generated
	 */
	protected static void createContractFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createEntityEditor(composite, mainGroup);
		createDefaultPortsEditor(composite, mainGroup);
		createMinQuantityEditor(composite, mainGroup);
		createMaxQuantityEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the entity feature on Contract
	 * 
	 * @generated
	 */
	protected static void createEntityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getContract_Entity()), "Entity");
	}

	/**
	 * Create an editor for the defaultPorts feature on Contract
	 * 
	 * @generated
	 */
	protected static void createDefaultPortsEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getContract_DefaultPorts()), "Default Ports");
	}

	/**
	 * Create an editor for the minQuantity feature on Contract
	 * 
	 * @generated
	 */
	protected static void createMinQuantityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getContract_MinQuantity()), "Min Quantity");
	}

	/**
	 * Create an editor for the maxQuantity feature on Contract
	 * 
	 * @generated
	 */
	protected static void createMaxQuantityEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getContract_MaxQuantity()), "Max Quantity");
	}
}
