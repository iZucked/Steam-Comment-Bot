/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.generated;

import org.eclipse.swt.widgets.Composite;

import scenario.contract.ContractPackage;

import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;

/**
 * A composite containing a form for editing GroupEntity instances. The EClass hierarchy is implemented by the static methods at the bottom of the class, and is not mirrored in the java class
 * hierarchy for the composites, because ECore supports multiple inheritance but java does not.
 * 
 * @generated
 */
public class GroupEntityComposite extends AbstractDetailComposite {
	private final String mainGroupTitle;

	/**
	 * Call superclass constructor
	 * 
	 * @generated
	 */
	public GroupEntityComposite(final Composite container, final int style, final String mainGroupTitle, final boolean validate) {
		super(container, style, validate);
		this.mainGroupTitle = mainGroupTitle;
	}

	public GroupEntityComposite(final Composite container, final int style, final boolean validate) {
		this(container, style, "Group Entity", validate);
	}

	public GroupEntityComposite(final Composite container, final int style) {
		this(container, style, "Group Entity", true);
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
		createGroupEntityFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging to all the supertypes of GroupEntity.
	 * 
	 * @generated
	 */
	protected static void createFieldsFromSupers(final AbstractDetailComposite composite, final Composite mainGroup) {
		EntityComposite.createFields(composite, mainGroup);
	}

	/**
	 * Create fields belonging directly to GroupEntity
	 * 
	 * @generated
	 */
	protected static void createGroupEntityFields(final AbstractDetailComposite composite, final Composite mainGroup) {
		createTaxRateEditor(composite, mainGroup);
		createOwnershipEditor(composite, mainGroup);
		createTransferOffsetEditor(composite, mainGroup);
	}

	/**
	 * Create an editor for the taxRate feature on GroupEntity
	 * 
	 * @generated
	 */
	protected static void createTaxRateEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getGroupEntity_TaxRate()), "Tax Rate");
	}

	/**
	 * Create an editor for the ownership feature on GroupEntity
	 * 
	 * @generated
	 */
	protected static void createOwnershipEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getGroupEntity_Ownership()), "Ownership");
	}

	/**
	 * Create an editor for the transferOffset feature on GroupEntity
	 * 
	 * @generated
	 */
	protected static void createTransferOffsetEditor(final AbstractDetailComposite composite, final Composite mainGroup) {
		composite.createEditorControl(mainGroup, composite.createEditor(ContractPackage.eINSTANCE.getGroupEntity_TransferOffset()), "Transfer Offset");
	}
}
