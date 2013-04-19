/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CharterCostModel instances
 *
 * @generated
 */
public class CharterCostModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterCostModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterCostModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CharterCostModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SpotMarketsPackage.Literals.CHARTER_COST_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselClassesEditor(detailComposite, topClass);
		add_charterInPriceEditor(detailComposite, topClass);
		add_charterOutPriceEditor(detailComposite, topClass);
		add_spotCharterCountEditor(detailComposite, topClass);
		add_minCharterOutDurationEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vesselClasses feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_vesselClassesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_COST_MODEL__VESSEL_CLASSES));
	}
	/**
	 * Create the editor for the charterInPrice feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_charterInPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_COST_MODEL__CHARTER_IN_PRICE));
	}
	/**
	 * Create the editor for the charterOutPrice feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_charterOutPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_COST_MODEL__CHARTER_OUT_PRICE));
	}
	/**
	 * Create the editor for the spotCharterCount feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_spotCharterCountEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_COST_MODEL__SPOT_CHARTER_COUNT));
	}
	/**
	 * Create the editor for the minCharterOutDuration feature on CharterCostModel
	 *
	 * @generated
	 */
	protected void add_minCharterOutDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_COST_MODEL__MIN_CHARTER_OUT_DURATION));
	}
}