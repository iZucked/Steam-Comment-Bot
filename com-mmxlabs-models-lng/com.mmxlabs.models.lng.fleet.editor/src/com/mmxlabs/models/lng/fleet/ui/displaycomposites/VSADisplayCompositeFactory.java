package com.mmxlabs.models.lng.fleet.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;

public class VSADisplayCompositeFactory implements IDisplayCompositeFactory {
	final IDisplayCompositeFactory defaultFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(EcorePackage.eINSTANCE.getEObject());
	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass) {
		return defaultFactory.createToplevelComposite(composite, eClass);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass) {
		return new VSADetailComposite(composite, SWT.NONE);
	}
}
