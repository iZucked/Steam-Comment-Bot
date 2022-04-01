/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class DefaultPortGroupMigrationHelper {

	public static void migrateClientAddingStandardPortGroups(@NonNull MigrationModelRecord modelRecord) {
		//Add standard port groups for regions.
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");

		final EObjectWrapper portsModel = referenceModel.getRef("portModel");
		final List<EObjectWrapper> portGroups = portsModel.getRefAsList("portGroups");

		final List<EObjectWrapper> portModelCountryGroups = portsModel.getRefAsList("portCountryGroups");
		
		
		final EPackage portsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PortModel);
		final EClass portGroupClass = MetamodelUtils.getEClass(portsPackage, "PortGroup");
		final EFactory portsFactory = portsPackage.getEFactoryInstance();
	
		String[] stdPortGroups = new String[]{ "JKTC", "MESA", "SE Asia", "Europe" };
		String[][] stdPortGroupCountries = new String[][] {
				{ "Japan", "South Korea", "Taiwan", "China" },
				{  "Kuwait", "United Arab Emirates", "Bahrain", "Jordan", "Pakistan", "India", "Bangladesh" },
				{ "Malaysia", "Indonesia", "Singapore", "Thailand", "Philippines" },
				{ "United Kingdom", "France", "Belgium", "Netherlands", "Poland", "Lithuania", "Spain", "Portugal", "Italy", "Malta", "Croatia", "Greece", "Turkey", "Israel" }
		};
		
		List<Integer> portGroupIdxsToAdd = new ArrayList<>();
		
		for (int i = 0; i < stdPortGroups.length; i++) {
			String stdPortGroupName = stdPortGroups[i];
			if (!containsPortGroup(stdPortGroupName, portGroups)) {
				portGroupIdxsToAdd.add(i);
			}
		}
		
		for (int i = 0; i < portGroupIdxsToAdd.size(); i++) {
			int index = portGroupIdxsToAdd.get(i);
			String portGroupName = stdPortGroups[index];
			String[] portGroupCountries = stdPortGroupCountries[index];
			addPortGroup(portGroups, portsFactory, portGroupClass, portModelCountryGroups, portGroupName, portGroupCountries);
		}
	}
	
	private static boolean containsPortGroup(String portName, List<EObjectWrapper> portGroups) {
		for (EObjectWrapper portGroup : portGroups) {
			String pgName = portGroup.getAttrib("name");
			if (pgName != null && portName.equalsIgnoreCase(pgName)) {
				return true;
			}
		}
		return false;
	}
	
	private static EObjectWrapper getPortCountryGroup(String portCountry, List<EObjectWrapper> portCountryGroups) {
		for (EObjectWrapper portGroup : portCountryGroups) {
			String pgName = portGroup.getAttrib("name");
			if (pgName != null && portCountry.equalsIgnoreCase(pgName)) {
				return portGroup;
			}
		}
		return null;
	}
	
	private static void addPortGroup(List<EObjectWrapper> portModelGroups, final EFactory portsFactory, final EClass portGroupClass, List<EObjectWrapper> portCountryGroups, String name, String[] countries) {
		//Create a port group 
		final EObjectWrapper portGroup = (EObjectWrapper) portsFactory.create(portGroupClass);
				
		//Set name etc.
		portGroup.setAttrib("name", name);
		
		List<EObjectWrapper> contents = portGroup.getRefAsList("contents");
		
		//Add contents.
		for (String portCountry : countries) {
			EObjectWrapper pg = getPortCountryGroup(portCountry, portCountryGroups);
			
			if (pg == null) {
				System.err.println("Cannot find country group: "+portCountry);
			}
			else {
				contents.add(pg);
			}
		}
		
		portModelGroups.add(portGroup);
	}
}
