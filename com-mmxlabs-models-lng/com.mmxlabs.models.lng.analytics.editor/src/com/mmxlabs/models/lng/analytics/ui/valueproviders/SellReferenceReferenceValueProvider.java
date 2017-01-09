/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.valueproviders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;

/**
 * This is a restricted case reference value provider which filters the port list on slots to those allowed by the slots' contract.
 * 
 * @author hinton
 * 
 */
public class SellReferenceReferenceValueProvider extends BaseReferenceValueProvider {
	protected final MMXRootObject rootObject;

	public SellReferenceReferenceValueProvider(final MMXRootObject rootObject) {
		this.rootObject = rootObject;
	}

	@Override
	public List<Pair<String, EObject>> getAllowedValues(final EObject target, final EStructuralFeature field) {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);

			final List<DischargeSlot> options = new LinkedList<>(cargoModel.getDischargeSlots());

			final List<Pair<String, EObject>> values = options.stream() //
					.filter(s -> !(s instanceof SpotSlot)) //
					.map(s -> new Pair<String, EObject>(getName(s), s)) //
					.sorted((a,b) -> a.getFirst().compareTo(b.getFirst())) //
					.collect(Collectors.toList());

			// values.add(0, null);
			return values;
		}
		return Collections.emptyList();
	}

	private @NonNull String getName(DischargeSlot slot) {
		StringBuilder sb = new StringBuilder();
		sb.append(slot.getName());

		sb.append(String.format(": %s ", slot.getPort().getName()));
		LocalDate windowStart = slot.getWindowStart();
		sb.append(String.format(" %04d-%02d-%02d", windowStart.getYear(), windowStart.getMonthValue(), windowStart.getDayOfMonth()));

		return sb.toString();
	}

	@Override
	protected boolean isRelevantTarget(final Object target, final Object feature) {
		return super.isRelevantTarget(target, feature);
	}

	@Override
	public void dispose() {

	}

	@Override
	protected void cacheValues() {

	}
}
