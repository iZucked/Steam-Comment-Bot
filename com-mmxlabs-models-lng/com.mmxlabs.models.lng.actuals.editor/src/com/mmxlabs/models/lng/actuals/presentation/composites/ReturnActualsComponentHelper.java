/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for ReturnActuals instances
 *
 * @generated NOT
 */
public class ReturnActualsComponentHelper extends DefaultComponentHelper {

	public ReturnActualsComponentHelper() {
		super(ActualsPackage.Literals.RETURN_ACTUALS);

		addEditor(ActualsPackage.Literals.RETURN_ACTUALS__TITLE_TRANSFER_POINT, topClass -> {
			IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__TITLE_TRANSFER_POINT);
			editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
			return editor;
		});
		addEditor(ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START, topClass -> {
			IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START);
			editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
			return editor;
		});
		addEditor(ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_M3, topClass -> {
			IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_M3);
			editor.addNotificationChangedListener(new ReturnActualsInlineEditorChangedListener());
			return editor;
		});
	}

}