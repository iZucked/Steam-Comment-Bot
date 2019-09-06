/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextualSuggestionInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for AbstractNominationSpec instances
 *
 * @generated
 */
public class AbstractNominationSpecComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AbstractNominationSpecComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AbstractNominationSpecComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}

	/**
	 * add editors to a composite, using AbstractNominationSpec as the supertype
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass'
	 * features.
	 *
	 * @generated NOT
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers)
			helper.addEditorsToComposite(detailComposite, topClass);

		if (!AbstractNomination.class.isAssignableFrom(topClass.getInstanceClass())) {
			add_typeEditor(detailComposite, topClass);
			add_sideEditor(detailComposite, topClass);
			add_refererIdEditor(detailComposite, topClass);
			add_sizeEditor(detailComposite, topClass);
			add_sizeUnitsEditor(detailComposite, topClass);
			add_dayOfMonthEditor(detailComposite, topClass);
			add_alertSizeEditor(detailComposite, topClass);
			add_alertSizeUnitsEditor(detailComposite, topClass);
			add_counterpartyEditor(detailComposite, topClass);
			add_remarkEditor(detailComposite, topClass);
		}
	}

	/**
	 * Create the editor for the type feature on AbstractNominationSpec
	 *
	 * @generated NOT
	 */
	protected void add_typeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualSuggestionInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__TYPE, NominationsModelUtils.getNominationTypes()));
	}

	/**
	 * Create the editor for the counterparty feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_counterpartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__COUNTERPARTY));
	}

	/**
	 * Create the editor for the remark feature on AbstractNominationSpec
	 *
	 * @generated NOT
	 */
	protected void add_remarkEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__REMARK));
	}

	/**
	 * Create the editor for the size feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_sizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE));
	}

	/**
	 * Create the editor for the sizeUnits feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_sizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIZE_UNITS));
	}

	/**
	 * Create the editor for the dayOfMonth feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_dayOfMonthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__DAY_OF_MONTH));
	}

	/**
	 * Create the editor for the alertSize feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_alertSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE));
	}

	/**
	 * Create the editor for the alertSizeUnits feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_alertSizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__ALERT_SIZE_UNITS));
	}

	/**
	 * Create the editor for the side feature on AbstractNominationSpec
	 *
	 * @generated
	 */
	protected void add_sideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__SIDE));
	}

	/**
	 * Create the editor for the refererId feature on AbstractNominationSpec
	 * 
	 * @generated NOT
	 */
	protected void add_refererIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualSuggestionInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC__REFERER_ID,  (rootObject, target) -> {
				if (rootObject instanceof LNGScenarioModel && target instanceof AbstractNominationSpec) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel)rootObject;
					final AbstractNominationSpec spec = (AbstractNominationSpec)target;
					return NominationsModelUtils.getContracts(scenarioModel, spec.getSide());
				}
				else {
					return Collections.emptyList();
				}}));
	}
}