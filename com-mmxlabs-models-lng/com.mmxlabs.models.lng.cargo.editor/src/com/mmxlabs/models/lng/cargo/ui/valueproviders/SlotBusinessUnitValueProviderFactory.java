package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class SlotBusinessUnitValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject theRootObject) {
		if (!(theRootObject instanceof LNGScenarioModel)) {
			return null;
		}
		
		return new IReferenceValueProvider() {

			@Override
			public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
				final List<Pair<String, EObject>> results = new LinkedList<>();
				if (target instanceof Slot<?> slot) {
					final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
					if (entity != null) {
						for (final BusinessUnit bu : entity.getBusinessUnits()) {
							results.add(Pair.of(bu.getName(), bu));
						}
					}
				}
				return results;
			}

			@Override
			public String getName(EObject referer, EReference feature, EObject referenceValue) {
				if (referenceValue instanceof NamedObject no) {
					return no.getName();
				}
				return referenceValue.toString();
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
				return null;
			}

			@Override
			public boolean updateOnChangeToFeature(Object changedFeature) {
				return changedFeature == CargoPackage.eINSTANCE.getSlot_Entity();
			}

			@Override
			public void dispose() {
			}			
		};
	}

}
