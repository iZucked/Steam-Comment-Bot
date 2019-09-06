/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.TextualSuggestionInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for ContractNomination instances
 *
 * @generated
 */
public class ContractNominationComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();
	protected AbstractNominationComponentHelper abstractComponentHelper;
	protected AbstractNominationSpecComponentHelper specComponentHelper;
	
	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ContractNominationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated NOT
	 */
	public ContractNominationComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(NominationsPackage.Literals.ABSTRACT_NOMINATION));
		for (final IComponentHelper ch : superClassesHelpers) {
			if (ch instanceof AbstractNominationComponentHelper) {
				this.abstractComponentHelper = (AbstractNominationComponentHelper) ch;
				this.specComponentHelper = this.abstractComponentHelper.specComponentHelper;
			}
		}
	}
	
	/**
	 * add editors to a composite, using ContractNomination as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, NominationsPackage.Literals.CONTRACT_NOMINATION);	
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

		specComponentHelper.add_sideEditor(detailComposite, topClass);
		add_nomineeIdEditor(detailComposite, topClass);
		abstractComponentHelper.add_dueDateEditor(detailComposite, topClass);
		abstractComponentHelper.add_alertDateEditor(detailComposite, topClass);
		specComponentHelper.add_counterpartyEditor(detailComposite, topClass);
		specComponentHelper.add_remarkEditor(detailComposite, topClass);
		abstractComponentHelper.add_doneEditor(detailComposite, topClass);
	}
	
	/**
	 * Create the editor for the nomineeId feature on AbstractNomination
	 *
	 * @generated NOT
	 */
	protected void add_nomineeIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualSuggestionInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__NOMINEE_ID,  (rootObject, target) -> {
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