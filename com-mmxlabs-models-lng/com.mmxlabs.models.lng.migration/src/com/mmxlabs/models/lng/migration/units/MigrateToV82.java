/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV82 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 81;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 82;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final @NonNull EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EPackage package_FleetModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);

		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");

		final Map<EObjectWrapper, List<EObjectWrapper>> vesselClassToVessels = new HashMap<>();

		final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
		final List<EObjectWrapper> vesselClasses = fleetModel.getRefAsList("vesselClasses");
		// Copy using normal list for use in final cross-reference check. Original will be empty be this point
		final List<EObjectWrapper> copyOfVesselClasses = new LinkedList<>(vesselClasses);

		final EClass class_VesselStateAttributes = MetamodelUtils.getEClass(package_FleetModel, "VesselStateAttributes");

		// Update vessels
		for (final EObjectWrapper vessel : vessels) {
			final EObjectWrapper vesselClass = vessel.getRef("vesselClass");
			String className = vesselClass.getAttrib("name");
			setVesselType(vessel, className);

			vesselClassToVessels.computeIfAbsent(vesselClass, k -> new LinkedList<>()).add(vessel);
			// Existing overrides
			final List<EObjectWrapper> refAsList = vessel.getRefAsList("inaccessiblePorts");
			if (refAsList == null || refAsList.isEmpty()) {
				vessel.setRef("inaccessiblePorts", vesselClass.getRefAsList("inaccessiblePorts"));
			} else {
				vessel.setAttrib("inaccessiblePortsOverride", Boolean.TRUE);

			}
			if (vessel.getAttribAsBoolean("overrideInaccessibleRoutes") == false) {
				vessel.setAttrib("inaccessibleRoutes", vesselClass.getAttribAsList("inaccessibleRoutes"));
			} else {
				vessel.setAttrib("inaccessibleRoutesOverride", Boolean.TRUE);
			}
			vessel.unsetFeature("overrideInaccessibleRoutes");

			if (!vessel.isSetFeature("capacity")) {
				vessel.setAttrib("capacity", vesselClass.getAttrib("capacity"));
			}
			if (!vessel.isSetFeature("fillCapacity")) {
				vessel.setAttrib("fillCapacity", vesselClass.getAttrib("fillCapacity"));
			}
			if (!vessel.isSetFeature("scnt")) {
				vessel.setAttrib("scnt", vesselClass.getAttrib("scnt"));
			}

			vessel.unsetFeature("vesselClass");

			vessel.setRef("ladenAttributes", package_FleetModel.getEFactoryInstance().create(class_VesselStateAttributes));
			vessel.setRef("ballastAttributes", package_FleetModel.getEFactoryInstance().create(class_VesselStateAttributes));
		}

		final Map<EObjectWrapper, EObjectWrapper> vesselClassToPrototypeMap = new HashMap<>();
		final Map<EObjectWrapper, EObjectWrapper> vesselClassToVesselClassGroup = new HashMap<>();

		final EClass class_VesselGroup = MetamodelUtils.getEClass(package_FleetModel, "VesselGroup");
		final EClass class_Vessel = MetamodelUtils.getEClass(package_FleetModel, "Vessel");

		final List<EObjectWrapper> newVessels = new LinkedList<>();
		final List<EObjectWrapper> newVesselGroups = new LinkedList<>();

		// Convert to new prototype vessel
		for (final EObjectWrapper vesselClass : vesselClasses) {

			// Attempt to determine if there is a one-to-one mapping between a class and a vessel (i.e. vessel class name is similar to vessel name)
			boolean buildPrototype = true;
			if (vesselClassToVessels.computeIfAbsent(vesselClass, k -> new LinkedList<>()).size() > 0) {
				List<EObjectWrapper> classList = vesselClassToVessels.get(vesselClass);

				for (final EObjectWrapper vessel : classList) {
					final String vesselName = vessel.getAttrib("name");
					final String className = vesselClass.getAttrib("name");
					setVesselType(vessel, className);

					if (className.replaceAll(" ", "").replaceAll("-", "").toLowerCase().contains(vesselName.replaceAll(" ", "").replaceAll("-", "").toLowerCase())) {
						buildPrototype = false;
						vesselClassToPrototypeMap.put(vesselClass, vessel);

						// Copy over other features
						vessel.setRef("baseFuel", vesselClass.getRef("baseFuel"));
						vessel.setRef("ladenAttributes", EcoreUtil.copy(vesselClass.getRef("ladenAttributes")));
						vessel.setRef("ballastAttributes", EcoreUtil.copy(vesselClass.getRef("ballastAttributes")));
						vessel.setRef("routeParameters", EcoreUtil.copyAll(vesselClass.getRefAsList("routeParameters")));
						//
						vessel.setAttrib("minSpeed", vesselClass.getAttrib("minSpeed"));
						vessel.setAttrib("maxSpeed", vesselClass.getAttrib("maxSpeed"));
						vessel.setAttrib("safetyHeel", vesselClass.getAttrib("minHeel"));
						vessel.setAttrib("warmingTime", vesselClass.getAttrib("warmingTime"));
						vessel.setAttrib("coolingVolume", vesselClass.getAttrib("coolingVolume"));
						vessel.setAttrib("pilotLightRate", vesselClass.getAttrib("pilotLightRate"));
						vessel.setAttrib("minBaseFuelConsumption", vesselClass.getAttrib("minBaseFuelConsumption"));
						vessel.setAttrib("hasReliqCapability", vesselClass.getAttrib("hasReliqCapability"));

						for (final EObjectWrapper p : vessel.getRefAsList("routeParameters")) {
							p.setAttrib("routeOption", p.getRef("route").getAttrib("routeOption"));
							p.unsetFeature("route");
						}

						// Assume this vessel is the prototype
						classList.forEach(v -> {
							if (v != vessel) {
								v.setRef("reference", vessel);
							}
						});

						break;
					}
				}
			}
			String className = vesselClass.getAttrib("name");
			if (buildPrototype) {
				// Create prototype
				final EObjectWrapper vessel = (EObjectWrapper) package_FleetModel.getEFactoryInstance().create(class_Vessel);

				vesselClassToVessels.getOrDefault(vesselClass, Collections.emptyList()).forEach(v -> v.setRef("reference", vessel));

				setVesselType(vessel, className);
				// Update existing vessels to reference this class
				// vesselClassToVessels.get(vesselClass).forEach(v -> v.setRef("reference", vessel));

				vesselClassToVessels.computeIfAbsent(vesselClass, k -> new LinkedList<>()).add(vessel);

				vessel.setRef("inaccessiblePorts", vesselClass.getRefAsList("inaccessiblePorts"));
				vessel.setAttrib("name", className);
				vessel.setAttrib("inaccessibleRoutes", vesselClass.getAttribAsList("inaccessibleRoutes"));
				vessel.setAttrib("capacity", vesselClass.getAttrib("capacity"));
				vessel.setAttrib("fillCapacity", vesselClass.getAttrib("fillCapacity"));
				vessel.setAttrib("scnt", vesselClass.getAttrib("scnt"));

				vessel.setRef("baseFuel", vesselClass.getRef("baseFuel"));
				vessel.setRef("ladenAttributes", EcoreUtil.copy(vesselClass.getRef("ladenAttributes")));
				vessel.setRef("ballastAttributes", EcoreUtil.copy(vesselClass.getRef("ballastAttributes")));
				vessel.setRef("routeParameters", EcoreUtil.copyAll(vesselClass.getRefAsList("routeParameters")));

				vessel.setAttrib("minSpeed", vesselClass.getAttrib("minSpeed"));
				vessel.setAttrib("maxSpeed", vesselClass.getAttrib("maxSpeed"));
				vessel.setAttrib("safetyHeel", vesselClass.getAttrib("minHeel"));
				vessel.setAttrib("warmingTime", vesselClass.getAttrib("warmingTime"));
				vessel.setAttrib("coolingVolume", vesselClass.getAttrib("coolingVolume"));
				vessel.setAttrib("pilotLightRate", vesselClass.getAttrib("pilotLightRate"));
				vessel.setAttrib("minBaseFuelConsumption", vesselClass.getAttrib("minBaseFuelConsumption"));
				vessel.setAttrib("hasReliqCapability", vesselClass.getAttrib("hasReliqCapability"));

				for (final EObjectWrapper p : vessel.getRefAsList("routeParameters")) {
					p.setAttrib("routeOption", p.getRef("route").getAttrib("routeOption"));
					p.unsetFeature("route");
				}

				newVessels.add(vessel);

				vesselClassToPrototypeMap.put(vesselClass, vessel);
			} else {
			}
			final EObjectWrapper group = (EObjectWrapper) package_FleetModel.getEFactoryInstance().create(class_VesselGroup);
			group.setAttrib("name", className);
			group.setRef("vessels", vesselClassToVessels.get(vesselClass));

			newVesselGroups.add(group);

			vesselClassToVesselClassGroup.put(vesselClass, group);
		}
		{
			final List<EObjectWrapper> vesselsList = fleetModel.getRefAsList("vessels");
			if (vesselsList != null) {
				newVessels.addAll(0, vesselsList);
			}
			fleetModel.setRef("vessels", newVessels);
		}
		{
			final List<EObjectWrapper> vesselGroupsList = fleetModel.getRefAsList("vesselGroups");
			if (vesselGroupsList != null) {
				newVesselGroups.addAll(0, vesselGroupsList);
			}
			fleetModel.setRef("vesselGroups", newVesselGroups);
		}

		// Update external references.

		// Analytics model
		{
			final EPackage package_AnalyticsModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
			final EClass class_RoundTripShippingOption = MetamodelUtils.getEClass(package_AnalyticsModel, "RoundTripShippingOption");

			final EPackage package_ScheduleModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
			final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");

			final EObjectWrapper analyticsModel = modelRoot.getRef("analyticsModel");
			final TreeIterator<EObject> itr = analyticsModel.eAllContents();
			while (itr.hasNext()) {
				final EObject obj = itr.next();
				if (class_ScheduleModel.isInstance(obj)) {
					itr.prune();
				} else if (class_RoundTripShippingOption.isInstance(obj)) {
					final EObjectWrapper w = (EObjectWrapper) obj;
					final EObjectWrapper vesselClass = w.getRef("vesselClass");
					if (vesselClass != null) {
						w.unsetFeature("vesselClass");
						w.setRef("vessel", vesselClassToPrototypeMap.get(vesselClass));
					}
				}
			}
		}

		// Spot markets model
		{

			final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");

			final List<EObjectWrapper> charterInMarkets = spotMarketsModel.getRefAsList("charterInMarkets");
			if (charterInMarkets != null) {
				for (final EObjectWrapper charterInMarket : charterInMarkets) {
					final EObjectWrapper vesselClass = charterInMarket.getRef("vesselClass");
					if (vesselClass != null) {
						charterInMarket.unsetFeature("vesselClass");
						charterInMarket.setRef("vessel", vesselClassToPrototypeMap.get(vesselClass));
					}
				}
			}

			final List<EObjectWrapper> charterOutMarkets = spotMarketsModel.getRefAsList("charterOutMarkets");
			if (charterOutMarkets != null) {
				for (final EObjectWrapper charterOutMarket : charterOutMarkets) {
					final EObjectWrapper vesselClass = charterOutMarket.getRef("vesselClass");
					if (vesselClass != null) {
						charterOutMarket.unsetFeature("vesselClass");
						final List<EObjectWrapper> marketVessels = new LinkedList<>();
						marketVessels.add(vesselClassToVesselClassGroup.get(vesselClass));
						charterOutMarket.setRef("vessels", marketVessels);
					}
				}
			}
		}

		// Pricing Model
		{
			final EObjectWrapper costModel = referenceModel.getRef("costModel");

			final List<EObjectWrapper> routeCosts = costModel.getRefAsList("routeCosts");
			if (routeCosts != null) {
				for (final EObjectWrapper routeCost : routeCosts) {
					final EObjectWrapper vesselClass = routeCost.getRef("vesselClass");
					if (vesselClass != null) {
						routeCost.unsetFeature("vesselClass");
						final List<EObjectWrapper> routeCostVessels = new LinkedList<>();
						routeCostVessels.add(vesselClassToVesselClassGroup.get(vesselClass));
						routeCost.setRef("vessels", routeCostVessels);
					}

					routeCost.setAttrib("routeOption", routeCost.getRef("route").getAttrib("routeOption"));
					routeCost.unsetFeature("route");

				}
			}
		}
		fleetModel.unsetFeature("vesselClasses");

		// Everything else
		{
			final Map<EObject, Collection<Setting>> crossReferences = EcoreUtil.UsageCrossReferencer.findAll(copyOfVesselClasses, modelRoot);
			for (final Map.Entry<EObject, Collection<Setting>> e : crossReferences.entrySet()) {
				final EObjectWrapper vesselClass = (EObjectWrapper) e.getKey();
				for (final Setting setting : e.getValue()) {
					EcoreUtil.replace(setting, vesselClass, vesselClassToVesselClassGroup.get(vesselClass));
				}
			}
		}
	}

	private void setVesselType(final EObjectWrapper vessel, String className) {
		if (className == null || className.isEmpty()) {
			return;
		}

		if (className.toUpperCase().startsWith("DFDE")) {
			vessel.setAttrib("type", "DFDE");
		} else if (className.toUpperCase().startsWith("TFDE")) {
			vessel.setAttrib("type", "TFDE");
		} else if (className.toUpperCase().startsWith("STEAM")) {
			vessel.setAttrib("type", "STEAM");
		} else if (className.toUpperCase().startsWith("ST ")) {
			vessel.setAttrib("type", "STEAM");
		} else if (className.toUpperCase().startsWith("STRH")) {
			vessel.setAttrib("type", "STRH");
		} else if (className.toUpperCase().startsWith("MEGI ")) {
			vessel.setAttrib("type", "MEGI");
		}
	}
}
