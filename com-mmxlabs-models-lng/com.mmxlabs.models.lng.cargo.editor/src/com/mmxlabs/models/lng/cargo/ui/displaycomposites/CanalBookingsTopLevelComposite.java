package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author FM
 *
 */
public class CanalBookingsTopLevelComposite extends DefaultTopLevelComposite {

	public CanalBookingsTopLevelComposite(Composite parent, int style, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final EClass eClass = object.eClass();
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry() //
				.getDisplayCompositeFactory(eClass).createSublevelComposite(this, eClass, dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		topLevel.display(dialogContext, root, object, range, dbc);

		final int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, this);
		setLayout(layoutProvider.createTopLevelLayout(root, object, numChildren + 1));
	}
}
