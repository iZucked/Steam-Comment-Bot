/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for ExposureDetail instances
 *
 * @generated
 */
public class ExposureDetailComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ExposureDetailComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ExposureDetailComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using ExposureDetail as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.EXPOSURE_DETAIL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_indexEditor(detailComposite, topClass);
		add_dateEditor(detailComposite, topClass);
		add_volumeInMMBTUEditor(detailComposite, topClass);
		add_volumeInNativeUnitsEditor(detailComposite, topClass);
		add_unitPriceEditor(detailComposite, topClass);
		add_nativeValueEditor(detailComposite, topClass);
		add_volumeUnitEditor(detailComposite, topClass);
		add_currencyUnitEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the index feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_indexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__INDEX));
	}
	/**
	 * Create the editor for the date feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_dateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__DATE));
	}
	/**
	 * Create the editor for the volumeInMMBTU feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_volumeInMMBTUEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_IN_MMBTU));
	}
	/**
	 * Create the editor for the volumeInNativeUnits feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_volumeInNativeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS));
	}
	/**
	 * Create the editor for the unitPrice feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_unitPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE));
	}
	/**
	 * Create the editor for the nativeValue feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_nativeValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__NATIVE_VALUE));
	}
	/**
	 * Create the editor for the volumeUnit feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_volumeUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_UNIT));
	}
	/**
	 * Create the editor for the currencyUnit feature on ExposureDetail
	 *
	 * @generated
	 */
	protected void add_currencyUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EXPOSURE_DETAIL__CURRENCY_UNIT));
	}
}