/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A component helper for BaseLegalEntity instances
 *
 * @generated
 */
public class BaseLegalEntityComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BaseLegalEntityComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BaseLegalEntityComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using BaseLegalEntity as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.BASE_LEGAL_ENTITY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_shippingBookEditor(detailComposite, topClass);
		add_tradingBookEditor(detailComposite, topClass);
		add_upstreamBookEditor(detailComposite, topClass);
		add_thirdPartyEditor(detailComposite, topClass);
		add_businessUnitsEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the shippingBook feature on BaseLegalEntity
	 *
	 * @generated
	 */
	protected void add_shippingBookEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK));
	}

	/**
	 * Create the editor for the tradingBook feature on BaseLegalEntity
	 *
	 * @generated
	 */
	protected void add_tradingBookEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK));
	}

	/**
	 * Create the editor for the upstreamBook feature on BaseLegalEntity
	 *
	 * @generated
	 */
	protected void add_upstreamBookEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK));
	}

	/**
	 * Create the editor for the thirdParty feature on BaseLegalEntity
	 *
	 * @generated NOT
	 */
	protected void add_thirdPartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted("features:third-party-entities")) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.BASE_LEGAL_ENTITY__THIRD_PARTY));
		}
	}
	
	/**
	 * Create the editor for the businessUnits feature on BaseLegalEntity
	 *
	 * @generated NOT
	 */
	protected void add_businessUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (true) {
			TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Business Units");
			b.withContentProvider(new ArrayContentProvider());

			b.buildColumn("   Name   ", MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
			.withWidth(100) //
			.withRMMaker((ed, rvp) -> new StringAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, ed)) //
			.build();

			b.buildColumn("  Description  ", CommercialPackage.Literals.BUSINESS_UNIT__DESCRIPTION) //
			.withWidth(150) //
			.withRMMaker((ed, rvp) -> new StringAttributeManipulator(CommercialPackage.Literals.BUSINESS_UNIT__DESCRIPTION, ed)) //
			.build();

//			b.buildColumn("Default", CommercialPackage.Literals.BUSINESS_UNIT__DEFAULT) //
//			.withWidth(50) //
//			.withRMMaker((ed, rvp) -> new BooleanAttributeManipulator(CommercialPackage.Literals.BUSINESS_UNIT__DEFAULT, ed)) //
//			.build();

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				if (input instanceof final BaseLegalEntity entity) {
					final BusinessUnit newBusinessUnit = CommercialFactory.eINSTANCE.createBusinessUnit();
					ch.handleCommand(AddCommand.create(ch.getEditingDomain(), entity,  CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS, newBusinessUnit), entity,  CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);
				}
			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (input instanceof final BaseLegalEntity entity) {
					if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
						ch.handleCommand(RemoveCommand.create(ch.getEditingDomain(), entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS, ss.getFirstElement()), entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);
					}
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			detailComposite.addInlineEditor(b.build(CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS));
		}
	}
}