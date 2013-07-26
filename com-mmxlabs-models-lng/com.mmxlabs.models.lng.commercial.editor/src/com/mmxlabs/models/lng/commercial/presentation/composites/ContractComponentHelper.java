/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Contract instances
 *
 * @generated
 */
public class ContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Contract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_allowedPortsEditor(detailComposite, topClass);
		add_preferredPortEditor(detailComposite, topClass);
		add_minQuantityEditor(detailComposite, topClass);
		add_maxQuantityEditor(detailComposite, topClass);
		add_restrictedListsArePermissiveEditor(detailComposite, topClass);
		add_restrictedContractsEditor(detailComposite, topClass);
		add_restrictedPortsEditor(detailComposite, topClass);
		add_priceInfoEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
	}
	
	/**
	 * Create the editor for the notes feature on Contract
	 * 
	 * @generated NO
	 */
	private void add_notesEditor(IInlineEditorContainer detailComposite,
			EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(CommercialPackage.Literals.CONTRACT__NOTES));
	}

	/**
	 * Create the editor for the entity feature on Contract
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__ENTITY));
	}

	/**
	 * Create the editor for the allowedPorts feature on Contract
	 *
	 * @generated
	 */
	protected void add_allowedPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__ALLOWED_PORTS));
	}

	/**
	 * Create the editor for the preferredPort feature on Contract
	 *
	 * @generated
	 */
	protected void add_preferredPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__PREFERRED_PORT));
	}

	/**
	 * Create the editor for the minQuantity feature on Contract
	 *
	 * @generated
	 */
	protected void add_minQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__MIN_QUANTITY));
	}

	/**
	 * Create the editor for the maxQuantity feature on Contract
	 *
	 * @generated
	 */
	protected void add_maxQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__MAX_QUANTITY));
	}

	/**
	 * Create the editor for the restrictedListsArePermissive feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedListsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedContracts feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedContractsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS));
	}

	/**
	 * Create the editor for the restrictedPorts feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS));
	}

	/**
	 * Create the editor for the priceInfo feature on Contract
	 *
	 * @generated NO
	 */
	protected void add_priceInfoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		//Composite composite = new Composite(detailComposite);
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__PRICE_INFO));
	}
}