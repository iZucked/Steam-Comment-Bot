/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class ExhaustiveLNGScenarioChecker extends ExhaustiveModelChecker {
	@Override
	protected Collection<Class<?>> createExcludedPackageTypes() {
		Collection<Class<?>> result = super.createExcludedPackageTypes();
		result.add(ParametersPackage.class);
		result.add(ScenarioServicePackage.class);
		result.add(SchedulePackage.class);
		result.add(AnalyticsPackage.class);
		return result;
	}

	@Override
	protected AutoSetMap<EStructuralFeature, Object> createExcludedRequiredValues() {
		AutoSetMap<EStructuralFeature, Object> result = super.createExcludedRequiredValues();
		/*
		result.get(FleetPackage.Literals.FLEET_MODEL__VESSELS).add(emptyListObject);
		result.get(PortPackage.Literals.PORT_MODEL__PORTS).add(emptyListObject);
		result.get(CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS).add(emptyListObject);
		result.get(CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS).add(emptyListObject);
		result.get(CargoPackage.Literals.CARGO_MODEL__CARGOES).add(emptyListObject);
		*/
		//result.get(CommercialPackage.Literals.).add(emptyListObject);
		return result;
	}
	
	@Override
	protected List<EStructuralFeature> createExcludedFeatures() {
		List<EStructuralFeature> result = super.createExcludedFeatures();
		result.add(MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS);
		result.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
		result.add(CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
		return result;
	}
	
	@Override	
	protected Set<EClass> createExcludedEClasses() {
		Set<EClass> result = super.createExcludedEClasses();
		// following EClass types should probably be abstract
		result.add(MMXCorePackage.Literals.MMX_ROOT_OBJECT);
		result.add(MMXCorePackage.Literals.NAMED_OBJECT);
		result.add(MMXCorePackage.Literals.UUID_OBJECT);
		result.add(PricingPackage.Literals.NAMED_INDEX_CONTAINER);
		result.add(CommercialPackage.Literals.CONTRACT);
		result.add(CargoPackage.Literals.VESSEL_EVENT);
		result.add(TypesPackage.Literals.AVESSEL_SET);
		result.add(TypesPackage.Literals.APORT_SET);
		return result;
	}
}
