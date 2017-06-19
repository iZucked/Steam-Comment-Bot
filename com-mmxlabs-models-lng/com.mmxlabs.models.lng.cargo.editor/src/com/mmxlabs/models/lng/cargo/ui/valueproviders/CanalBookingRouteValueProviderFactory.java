/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.valueproviders;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * 
 */
public class CanalBookingRouteValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {
		return new IReferenceValueProvider() {
			@Override
			public boolean updateOnChangeToFeature(final Object changedFeature) {

				// ??
				if (changedFeature == CargoPackage.eINSTANCE.getCanalBookingSlot_Route()) {
					return true;
				}

				return false;
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getNotifiers(final EObject referer, final EReference feature, final EObject referenceValue) {
				if (referenceValue == null)
					return Collections.emptySet();
				return Collections.singleton(new Pair<Notifier, List<Object>>(referenceValue, Collections.singletonList((Object) feature)));
			}

			@Override
			public String getName(final EObject referer, final EReference feature, final EObject referenceValue) {
				if (referenceValue instanceof Route) {
					Route route = (Route) referenceValue;
					if (route.getName() != null) {
						return route.getName();
					} else {
						return "";
					}
				}
				return "<Not set>";
			}

			@Override
			public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {
				final List<Pair<String, EObject>> delegateValue = new LinkedList<>();

				delegateValue.add(new Pair<>("<Not set>", null));

				// I assume delegateValue is list of all ports??

				if (rootObject instanceof LNGScenarioModel) {
					LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
					@NonNull
					PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
					for (Route route : portModel.getRoutes()) {
						if (route.getRouteOption() == RouteOption.DIRECT) {
							continue;
						}
						// Only panama for now
						if (route.getRouteOption() == RouteOption.SUEZ) {
							continue;
						}
						delegateValue.add(new Pair<String, EObject>(route.getName(), route));
					}
				}

				return delegateValue;
			}

			@Override
			public void dispose() {
			}
		};
	}

	private boolean contains(final EList<APortSet<Port>> ips, final EObject value) {

		for (final APortSet<Port> aPort : ips) {
			if (aPort instanceof Port) {
				final Port port = (Port) aPort;
				if (port == value)
					return true;
			} else if (aPort instanceof PortGroup) {
				final PortGroup portGroup = (PortGroup) aPort;
				return contains(portGroup.getContents(), value);
			}
		}
		return false;
	}

}
