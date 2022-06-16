package com.mmxlabs.models.lng.commercial.ui.modelfactories;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

public class DefaultEntityModelFactory extends DefaultModelFactory {

	@Override
	protected EObject constructInstance(final EClass eClass) {
		final EObject instance = super.constructInstance(eClass);

		if (instance instanceof LegalEntity entity) {
			entity.setShippingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
			entity.setTradingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
			entity.setUpstreamBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
		}

		return instance;
	}
}
