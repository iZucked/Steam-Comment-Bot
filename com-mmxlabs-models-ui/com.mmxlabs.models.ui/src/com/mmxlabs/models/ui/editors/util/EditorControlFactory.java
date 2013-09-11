package com.mmxlabs.models.ui.editors.util;

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

import static com.mmxlabs.models.ui.editors.IDisplayComposite.LABEL_CONTROL_KEY;;

/**
 * @since 7.0
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
	public static Control makeControls(MMXRootObject root, EObject object, Composite c, EStructuralFeature[] fs, Map<EStructuralFeature, IInlineEditor> feature2Editor, EMFDataBindingContext dbc, IDisplayCompositeLayoutProvider layoutProvider, FormToolkit toolkit) {
	
		Composite holder = c;
		if (fs.length > 1) {
			holder = new Composite(c, SWT.NONE);
			holder.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			GridData gd = new GridData();
			gd.horizontalSpan = 5;
			holder.setLayoutData(gd);
			GridLayout gl = new GridLayout(2 * fs.length, false);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			holder.setLayout(gl);
		}
		for (EStructuralFeature f : fs) {
			IInlineEditor editor = feature2Editor.get(f);
			if (editor != null) {
			createLabelledEditorControl(root, object, holder, editor, dbc, layoutProvider, toolkit);
			}
		}
		return holder;
	}

	/**
	 * @since 6.1
	 */
	 	public static Control createLabelledEditorControl(MMXRootObject root, EObject object, Composite c, IInlineEditor editor, EMFDataBindingContext dbc, IDisplayCompositeLayoutProvider layoutProvider, FormToolkit toolkit) {
		final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(c, SWT.NONE) : null;
		if (label != null) label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		editor.setLabel(label);
		final Control control = editor.createControl(c, dbc, toolkit);
		control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
		control.setData(LABEL_CONTROL_KEY, label);
		control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if (label != null) label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
		return control;
	}

}
