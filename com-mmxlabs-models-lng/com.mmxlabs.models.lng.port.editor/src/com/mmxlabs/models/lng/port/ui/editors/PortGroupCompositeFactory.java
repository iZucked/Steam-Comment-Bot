package com.mmxlabs.models.lng.port.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

public class PortGroupCompositeFactory extends DefaultDisplayCompositeFactory {
	@Override
	public IDisplayComposite createSublevelComposite(Composite composite,
			EClass eClass) {
		return new PortGroupDetailComposite(composite, SWT.NONE);
	}
}
