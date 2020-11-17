package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class InventoryProfileTopLevelComposite extends DefaultTopLevelComposite {
	private Group distributionComposite;
	private EClass eClass;
	
	public InventoryProfileTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		// addDisposeListener(e -> removeAdapter());
	}
	
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		this.eClass = object.eClass();
//		initialiseFactories();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);
		
		int numCols = 1;
		String groupName = EditorUtils.unmangle(object);
		
		final InventoryProfile inventoryProfile = (InventoryProfile) object;
		
		if (groupName != null) {
			g.setText(groupName);
		}
		g.setLayout(new GridLayout(numCols, false));
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
	}
	
	void removeAdapter() {
		
	}
	
//	private void initialiseFactories() {
//		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
//		final BundleContext bundleContext = bundle.getBundleContext();
//		
//		try {
//			profile
//		}
//	}
}
