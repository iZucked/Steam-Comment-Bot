/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.modelfactories;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 * Extended {@link DefaultModelFactory} to copy the {@link SpotMarketGroup#getType()} onto child {@link SpotMarket}s
 * 
 * @author Simon Goodall
 * 
 */
public class SpotMarketFactory extends DefaultModelFactory {

	public SpotMarketFactory() {
		super();
	}

	@Override
	public Collection<ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment, final ISelection selection) {
		EObject output = null;

		if (prototypeClass == null) {
			output = constructInstance(containment.getEReferenceType(), container);
		} else {
			for (final EClassifier e : containment.getEReferenceType().getEPackage().getEClassifiers()) {
				if (e instanceof EClass) {
					if (e.getInstanceClass().getCanonicalName().equals(prototypeClass)) {
						output = constructInstance((EClass) e, container);
						break;
					}
				}
			}
			if (output == null)
				output = constructInstance(containment.getEReferenceType(), container);
		}

		addSelectionToInstance(output.eClass(), output, selection);

		final Calendar nowCal = Calendar.getInstance();
		nowCal.clear(Calendar.MILLISECOND);
		nowCal.clear(Calendar.SECOND);
		nowCal.clear(Calendar.MINUTE);

		now = nowCal.getTime();
		postprocess(output);
		final EObject finalOutput = output;
		final ISetting setting = new ISetting() {
			@Override
			public EObject getInstance() {
				return finalOutput;
			}

			@Override
			public EObject getContainer() {
				return container;
			}

			@Override
			public EReference getContainment() {
				return containment;
			}

			@Override
			public ISelection getSelection() {
				return selection;
			}
		};
		return Collections.singleton(setting);

	}

	protected EObject constructInstance(final EClass eClass, EObject container) {
		final EObject object;
		if (container instanceof SpotMarketGroup) {
			final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) container;
			switch (spotMarketGroup.getType()) {
			case DES_PURCHASE:
				object = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
				break;
			case DES_SALE:
				object = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
				break;
			case FOB_PURCHASE:
				object = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
				break;
			case FOB_SALE:
				object = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
				break;
				default:
				object = eClass.getEPackage().getEFactoryInstance().create(eClass);
			}

		} else {
			object = eClass.getEPackage().getEFactoryInstance().create(eClass);
		}
		// create singly-contained sub objects, by default

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isMany() == false && reference.isContainment() == true) {
				object.eSet(reference, createSubInstance(object, reference));
			}
		}

		return object;
	}
}