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
 * A component helper for SlotActuals instances
 *
 * @generated
 */
public class SlotActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SlotActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SlotActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using SlotActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.SLOT_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_CVEditor(detailComposite, topClass);
		add_volumeEditor(detailComposite, topClass);
		add_mmBtuEditor(detailComposite, topClass);
		add_portChargesEditor(detailComposite, topClass);
		add_baseFuelConsumptionEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the CV feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_CVEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__CV));
	}
	/**
	 * Create the editor for the volume feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_volumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME));
	}
	/**
	 * Create the editor for the mmBtu feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_mmBtuEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__MM_BTU));
	}
	/**
	 * Create the editor for the portCharges feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_portChargesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__PORT_CHARGES));
	}
	/**
	 * Create the editor for the baseFuelConsumption feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_baseFuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION));
	}
}