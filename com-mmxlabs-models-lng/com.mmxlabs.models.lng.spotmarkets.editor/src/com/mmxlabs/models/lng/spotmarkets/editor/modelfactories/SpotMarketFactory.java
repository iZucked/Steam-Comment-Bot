/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.modelfactories;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;

import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;

/**
 * Extended {@link DefaultModelFactory} to copy the {@link SpotMarketGroup#getType()} onto child {@link SpotMarket}s
 * 
 * @author Simon Goodall
 * 
 */
public class SpotMarketFactory extends DefaultModelFactory {
	protected String priceInfoClassName;

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

		final Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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
		final SpotMarket market;
		if (container instanceof SpotMarketGroup) {
			final SpotMarketGroup spotMarketGroup = (SpotMarketGroup) container;
			switch (spotMarketGroup.getType()) {
			case DES_PURCHASE:
				object = market = SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket();
				market.setPricingEvent(PricingEvent.START_DISCHARGE);
				break;
			case DES_SALE:
				object = market = SpotMarketsFactory.eINSTANCE.createDESSalesMarket();
				market.setPricingEvent(PricingEvent.START_DISCHARGE);
				break;
			case FOB_PURCHASE:
				object = market = SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket();
				market.setPricingEvent(PricingEvent.START_LOAD);
				break;
			case FOB_SALE:
				object = market = SpotMarketsFactory.eINSTANCE.createFOBSalesMarket();
				market.setPricingEvent(PricingEvent.START_LOAD);
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

	@Override
	protected EObject createSubInstance(final EObject top, final EReference reference) {
		if (reference.equals(SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO)) {
			EClass priceInfoClass = getEClassFromName(priceInfoClassName, null);
			if (priceInfoClass != null) {
				return priceInfoClass.getEPackage().getEFactoryInstance().create(priceInfoClass);
			}

			return null;

		}
		return super.createSubInstance(top, reference);
	}

	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, final String replacementEReference, final String replacementEClass) {
		super.initFromExtension(ID, label, "com.mmxlabs.models.lng.spotmarkets.SpotMarket", replacementEReference, replacementEClass);
		this.priceInfoClassName = prototype;
	}

}