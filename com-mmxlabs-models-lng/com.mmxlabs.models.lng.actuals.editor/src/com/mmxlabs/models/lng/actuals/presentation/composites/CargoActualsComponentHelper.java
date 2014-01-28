/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import com.mmxlabs.models.lng.actuals.ActualsPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for CargoActuals instances
 *
 * @generated
 */
public class CargoActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using CargoActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.CARGO_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_baseFuelPriceEditor(detailComposite, topClass);
		add_volumeEditor(detailComposite, topClass);
		add_insurancePremiumEditor(detailComposite, topClass);
		add_crewBonusEditor(detailComposite, topClass);
		add_actualsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseFuelPrice feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_baseFuelPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__BASE_FUEL_PRICE));
	}
	/**
	 * Create the editor for the volume feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_volumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__VOLUME));
	}
	/**
	 * Create the editor for the insurancePremium feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_insurancePremiumEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__INSURANCE_PREMIUM));
	}
	/**
	 * Create the editor for the crewBonus feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_crewBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CREW_BONUS));
	}
	/**
	 * Create the editor for the actuals feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_actualsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS));
	}
}