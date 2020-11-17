package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class InventoryProfileTopLevelCompositeFactory extends DefaultDisplayCompositeFactory {
	
	public InventoryProfileTopLevelCompositeFactory() {
		
	}
	
	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		final DefaultDetailComposite s = (DefaultDetailComposite) super.createSublevelComposite(parent, eClass, dialogContext, toolkit);
		
		s.setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			@Override
			public Layout createDetailLayout(@NonNull MMXRootObject root, EObject value) {
				return GridLayoutFactory.swtDefaults().numColumns(3).create();
			}
			
			@Override
			public boolean showLabelFor(@NonNull MMXRootObject root, EObject value, @NonNull IInlineEditor editor) {
				boolean r = super.showLabelFor(root, value, editor);
				return r;
			}
			
			@Override
			public Object createLabelLayoutData(@NonNull MMXRootObject root, EObject value, @NonNull IInlineEditor editor, Control control, Label label) {
				return super.createLabelLayoutData(root, value, editor, control, label);
			}
			
			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				final EStructuralFeature feature = editor.getFeature();
				if (feature == ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE) {
					final GridData gd = GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).create();
					
					final Label label = editor.getLabel();
					if (label != null) {
						label.setText("Entity Table");
					}
					editor.setLabel(null);
					return gd;
				}
				return GridDataFactory.swtDefaults().span(2, 1).grab(true, false).create();
			}
		});
		return s;
	}
	
	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new InventoryProfileTopLevelComposite(composite, SWT.None, dialogContext, toolkit);
	}
}
