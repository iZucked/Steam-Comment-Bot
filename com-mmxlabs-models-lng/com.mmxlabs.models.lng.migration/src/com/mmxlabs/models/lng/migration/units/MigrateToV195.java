/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV195 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 194;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 195;
	}

	// Renames CharterLengthEvent to GeneratedCharterLengthEvent
	// Adds CharterLengthEvent to cargo model
	// Make FuelUsage a super type of VesselEventVisit
	@Override
		protected void doMigration(final MigrationModelRecord modelRecord) {
			final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
	        final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
	        final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
	        final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
	        if (vessels != null) {
	            for (var vessel : vessels) {
	                vessel.unsetFeature("baseFuelEmissionRate");
	                vessel.unsetFeature("pilotLightEmissionRate");
	                vessel.unsetFeature("bogEmissionRate");
	            }
	        }
	        final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
	        final List<EObjectWrapper> vesselCharters = cargoModel.getRefAsList("vesselCharters");
	        final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
	        final EFactory cargoFactory = cargoPackage.getEFactoryInstance();
	        
	        final EClass ciiStartOptions = MetamodelUtils.getEClass(cargoPackage, "CIIStartOptions");
	        final EClass ciiEndOptions = MetamodelUtils.getEClass(cargoPackage, "CIIEndOptions");
	        if(vesselCharters != null) {
	        	for(var vc : vesselCharters) {
	        		EObject defaultStartOptions = cargoFactory.create(ciiStartOptions);
	        		EObject defaultEndOptions = cargoFactory.create(ciiEndOptions);
	        		vc.setRef("ciiStartOptions", defaultStartOptions);
	        		vc.setRef("ciiEndOptions", defaultEndOptions);
	        	}
	        }
	        
		}

}
