/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for HeelOptions instances
 * 
 * @generated
 */
public class HeelOptionsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public HeelOptionsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public HeelOptionsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}

	/**
	 * add editors to a composite, using HeelOptions as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.HEEL_OPTIONS);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_volumeAvailableEditor(detailComposite, topClass);
		add_cvValueEditor(detailComposite, topClass);
		add_pricePerMMBTUEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the volumeAvailable feature on HeelOptions
	 * 
	 * @generated
	 */
	protected void add_volumeAvailableEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.HEEL_OPTIONS__VOLUME_AVAILABLE));
	}

	/**
	 * Create the editor for the cvValue feature on HeelOptions
	 * 
	 * @generated NOT
	 */
	protected void add_cvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.HEEL_OPTIONS__CV_VALUE);
		detailComposite.addInlineEditor(new HeelOptionsInlineEditorWrapper(editor));
	}

	/**
	 * Create the editor for the pricePerMMBTU feature on HeelOptions
	 * 
	 * @generated NOT
	 */
	protected void add_pricePerMMBTUEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.HEEL_OPTIONS__PRICE_PER_MMBTU);
		detailComposite.addInlineEditor(new HeelOptionsInlineEditorWrapper(editor));
	}
}