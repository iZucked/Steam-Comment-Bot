/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import org.eclipse.emf.ecore.EClass;

import scenario.market.MarketPackage;

/**
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
		if (MarketPackage.eINSTANCE.getStepwisePriceCurve().isSuperTypeOf(importClass)) {
			final PriceCurveImporter marketImporter = new PriceCurveImporter();
			marketImporter.setOutputEClass(importClass);
			return marketImporter;
		}
		
		final EObjectImporter result = new EObjectImporter();
		result.setOutputEClass(importClass);
		return result;
	}
}
