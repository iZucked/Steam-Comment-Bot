/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for ReturnActuals instances
 *
 * @generated
 */
public class ReturnActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ReturnActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ReturnActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.ITIMEZONE_PROVIDER));
	}
	
	/**
	 * add editors to a composite, using ReturnActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.RETURN_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_titleTransferPointEditor(detailComposite, topClass);
		add_operationsStartEditor(detailComposite, topClass);
		add_endHeelM3Editor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the titleTransferPoint feature on ReturnActuals
	 *
	 * @generated NOT
	 */
	protected void add_titleTransferPointEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__TITLE_TRANSFER_POINT);
		editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the operationsStart feature on ReturnActuals
	 *
	 * @generated NOT
	 */
	protected void add_operationsStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START);
		editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}
	/**
	 * Create the editor for the endHeelM3 feature on ReturnActuals
	 *
	 * @generated NOT
	 */
	protected void add_endHeelM3Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_M3);
		editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}
}