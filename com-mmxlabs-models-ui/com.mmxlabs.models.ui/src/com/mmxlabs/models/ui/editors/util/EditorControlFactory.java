/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import static com.mmxlabs.models.ui.editors.IDisplayComposite.LABEL_CONTROL_KEY;

import java.util.Map;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

;

/**
 */
public class EditorControlFactory {

	/**
	 * Make a set of controls for the given features[]
	 * 
	 * @param root
	 * @param object
	 * @param c
	 * @param fs
	 * @return
	 */
	public static Control makeControls(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Composite c, final EStructuralFeature[] fs,
			final Map<EStructuralFeature, IInlineEditor> feature2Editor, final EMFDataBindingContext dbc, final IDisplayCompositeLayoutProvider layoutProvider, final FormToolkit toolkit) {

		Composite holder = c;
		if (fs.length > 1) {
			holder = toolkit.createComposite(c, SWT.NONE);
			final GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = 2;
			holder.setLayoutData(gd);
			final GridLayout gl = new GridLayout(2 * fs.length, false);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			holder.setLayout(gl);
		}
		for (final EStructuralFeature f : fs) {
			final IInlineEditor editor = feature2Editor.get(f);
			if (editor != null) {
				createLabelledEditorControl(dialogContext, root, object, holder, editor, dbc, layoutProvider, toolkit);
			}
		}
		return holder;
	}

	/**
	 */
	public static Control createLabelledEditorControl(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Composite c, final IInlineEditor editor,
			final EMFDataBindingContext dbc, final IDisplayCompositeLayoutProvider layoutProvider, final FormToolkit toolkit) {
		final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(c, SWT.NONE) : null;
		if (label != null)
			label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		editor.setLabel(label);
		final Control control = editor.createControl(c, dbc, toolkit);
		control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
		control.setData(LABEL_CONTROL_KEY, label);
		control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		dialogContext.registerEditorControl(object, editor.getFeature(), control);
		if (label != null) {
			label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
			dialogContext.registerEditorControl(object, editor.getFeature(), label);
		}
		return control;
	}

}
