package com.mmxlabs.models.lng.optimiser.ui.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;

public class OptimiserSettingsCompositeFactory implements
		IDisplayCompositeFactory {

	final IDisplayCompositeFactory delegate = Activator.getDefault().getDisplayCompositeFactoryRegistry()
			.getDisplayCompositeFactory(TypesPackage.eINSTANCE.getAOptimisationSettings());
	
	@Override
	public IDisplayComposite createToplevelComposite(Composite composite,
			EClass eClass) {
		return new OptimiserSettingsToplevelComposite(composite, SWT.NONE);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite composite,
			EClass eClass) {
		return delegate.createSublevelComposite(composite, eClass);
	}

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root,
			EObject value) {
		return delegate.getExternalEditingRange(root, value);
	}
}
