/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import org.eclipse.emf.ecore.EClass;

import scenario.fleet.FleetPackage;
import scenario.market.MarketPackage;
import scenario.port.PortPackage;

/**
 * Provides subclasses of {@link EObjectImporter} suitable for a given {@link EClass}.
 * 
 * In most cases these will just be the base class, but some EClasses require a special format.
 * 
 * 
 * @author Tom Hinton
 *
 */
public class EObjectImporterFactory {
	private static final EObjectImporterFactory INSTANCE = new EObjectImporterFactory();
	
	private EObjectImporterFactory() {
		
	}
	
	public static EObjectImporterFactory getInstance() {
		return INSTANCE;
	}
	
	public EObjectImporter getImporter(final EClass importClass) {
		//TODO handle any other special cases here.
		if (FleetPackage.eINSTANCE.getVesselClass().isSuperTypeOf(importClass)) {
			final VesselClassImporter i = new VesselClassImporter();
			i.setOutputEClass(importClass);
			return i;
		}
		if (MarketPackage.eINSTANCE.getStepwisePriceCurve().isSuperTypeOf(importClass)) {
			final PriceCurveImporter marketImporter = new PriceCurveImporter();
			marketImporter.setOutputEClass(importClass);
			return marketImporter;
		}
		if (FleetPackage.eINSTANCE.getFuelConsumptionLine().isSuperTypeOf(importClass)) {
			final FuelConsumptionLineImporter i = new FuelConsumptionLineImporter();
			i.setOutputEClass(importClass);
			return i;
		}
		if (PortPackage.eINSTANCE.getDistanceModel().isSuperTypeOf(importClass)) {
			final DistanceModelImporter i = new DistanceModelImporter();
			i.setOutputEClass(importClass);
			return i;
		}
		final EObjectImporter result = new EObjectImporter();
		result.setOutputEClass(importClass);
		return result;
	}
}
