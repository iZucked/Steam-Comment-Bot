/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.presentation.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.formattedtext.ITextFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.nebula.widgets.formattedtext.StringFormatter;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.ContractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.presentation.composites.NominatedValueInlineEditor.NominatedValueMode;
import com.mmxlabs.models.lng.nominations.presentation.composites.NominatedValueInlineEditor.NominatedValueProvider;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextualSuggestionInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for AbstractNomination instances
 *
 * @generated
 */
public class AbstractNominationComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();
	protected AbstractNominationSpecComponentHelper specComponentHelper;

	protected IInlineEditor nominationTypeEditor;
	
	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AbstractNominationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated NOT
	 */
	public AbstractNominationComponentHelper(final IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(NominationsPackage.Literals.ABSTRACT_NOMINATION_SPEC));
		for (final IComponentHelper ch : superClassesHelpers) {
			if (ch instanceof AbstractNominationSpecComponentHelper) {
				this.specComponentHelper = (AbstractNominationSpecComponentHelper) ch;
			}
		}
	}

	/**
	 * add editors to a composite, using AbstractNomination as the supertype
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, NominationsPackage.Literals.ABSTRACT_NOMINATION);	
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

		if (!ContractNomination.class.isAssignableFrom(topClass.getInstanceClass())) {
			specComponentHelper.add_typeEditor(detailComposite, topClass);
			specComponentHelper.add_sideEditor(detailComposite, topClass);
			add_nomineeIdEditor(detailComposite, topClass);
			add_nomineeDateEditor(detailComposite, topClass);
			specComponentHelper.add_sizeEditor(detailComposite, topClass);
			specComponentHelper.add_sizeUnitsEditor(detailComposite, topClass);
			add_dueDateEditor(detailComposite, topClass);
			add_alertDateEditor(detailComposite, topClass);
			specComponentHelper.add_counterpartyEditor(detailComposite, topClass);
			specComponentHelper.add_remarkEditor(detailComposite, topClass);
			add_doneEditor(detailComposite, topClass);
			add_nominatedValueEditor(detailComposite, topClass);
		}
	}

	/**
	 * Create the editor for the nomineeId feature on AbstractNomination
	 *
	 * @generated NOT
	 */
	protected void add_nomineeIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualSuggestionInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__NOMINEE_ID,  (rootObject, target) -> {
			if (rootObject instanceof LNGScenarioModel && target instanceof AbstractNomination) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel)rootObject;
				final AbstractNomination nomination = (AbstractNomination)target;
				return NominationsModelUtils.getPossibleSlotNames(scenarioModel, nomination);
			}
			else {
				return Collections.emptyList();
			}}));
	}

	/**
	 * Create the editor for the window start for a slot nomination.
	 *
	 * @generated NOT
	 */
	protected void add_nomineeDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new NomineeDateInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__NOMINEE_ID));
	}
	
	/**
	 * Create the editor for the dueDate feature on AbstractNomination
	 *
	 * @generated NOT
	 */
	protected void add_dueDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new DueDateInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__DUE_DATE));
	}

	/**
	 * Create the editor for the done feature on AbstractNomination
	 *
	 * @generated
	 */
	protected void add_doneEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION__DONE));
	}

	/**
	 * Create the editor for the alertDate feature on AbstractNomination
	 *
	 * @generated NOT
	 */
	protected void add_alertDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new AlertDateInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__ALERT_DATE));
	}

	/**
	 * Create the editor for the specUuid feature on AbstractNomination
	 *
	 * @generated
	 */
	protected void add_specUuidEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION__SPEC_UUID));
	}

	/**
	 * Create the editor for the deleted feature on AbstractNomination
	 *
	 * @generated
	 */
	protected void add_deletedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, NominationsPackage.Literals.ABSTRACT_NOMINATION__DELETED));
	}

	/**
	 * Create the editor for the nominatedValue feature on AbstractNomination
	 *
	 * @generated NOT
	 */
	protected void add_nominatedValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new NominatedValueInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__NOMINATED_VALUE, 
			new NominatedValueProvider() {
				@Override
				public @NonNull List<String> getPossibleValues(MMXRootObject rootObject, Notifier target) {
					if (rootObject instanceof LNGScenarioModel && target instanceof AbstractNomination) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel)rootObject;
						final AbstractNomination nomination = (AbstractNomination)target;
						return NominationsModelUtils.getPossibleNominatedValues(scenarioModel, nomination);
					}
					else {
						return Collections.emptyList();
					}
				}

				@Override
				public @NonNull ITextFormatter getTextFormatter(String nominationType) {
					if (nominationType != null && nominationType.toLowerCase().contains("volume")) {
						return new NumberFormatter("##,###,###");
					}
					
					if (nominationType != null && nominationType.toLowerCase().contains("window")) {
						//TODO formatted date time + window string.
						return new DateWindowFormatter();
					}												
					
					//Everything else just a string.
					return new StringFormatter();
				}
				
				@Override
				public @NonNull NominatedValueMode getMode(MMXRootObject rootObject, Notifier target) {
					if (rootObject instanceof LNGScenarioModel && target instanceof AbstractNomination) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel)rootObject;
						final AbstractNomination nomination = (AbstractNomination)target;
						
						if (nomination != null && nomination.getType() != null && 
							(nomination.getType().toLowerCase().contains("port") ||
							nomination.getType().toLowerCase().contains("vessel"))) {
							return NominatedValueMode.PICKER;
						}
						else {
							return NominatedValueMode.FORMATTED_TEXT;
						}
					}
				
					return NominatedValueMode.FORMATTED_TEXT;
				}	
		}));
		//detailComposite.addInlineEditor(new TextInlineEditor(NominationsPackage.Literals.ABSTRACT_NOMINATION__NOMINATED_VALUE));
	}
}