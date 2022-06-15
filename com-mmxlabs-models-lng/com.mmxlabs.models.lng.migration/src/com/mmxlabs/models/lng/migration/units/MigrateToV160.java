/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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

public class MigrateToV160 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 159;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 160;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EPackage fleetPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);
		final EPackage schedulePackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		final EPackage adpPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ADPModel);

		// Part 1 - rename VesselAvailability to VesselCharter
		{

//		final EClass clsVesselAvailability = MetamodelUtils.getEClass(cargoPackage, "VesselAvailability");
			final EClass clsVesselCharter = MetamodelUtils.getEClass(cargoPackage, "VesselCharter");

			final EClass clsAbstractSolutionSet = MetamodelUtils.getEClass(analyticsPackage, "AbstractSolutionSet");
			final EClass clsSequence = MetamodelUtils.getEClass(schedulePackage, "Sequence");
			final EClass clsExistingVesselCharterOption = MetamodelUtils.getEClass(analyticsPackage, "ExistingVesselCharterOption");
			final EClass clsFullVesselCharterOption = MetamodelUtils.getEClass(analyticsPackage, "FullVesselCharterOption");
			final EClass clsDesSpacingAllocation = MetamodelUtils.getEClass(adpPackage, "DesSpacingAllocation");

			// Repeat!
			// cargo model
			// all solution sets

			// Also check the sandbox extra charters

			final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
			final EObjectWrapper analysisModel = scenarioModel.getRef("analyticsModel");

			final Map<EObjectWrapper, EObjectWrapper> replacements = new HashMap<>();

			// Step 1 create all the replacements
			{

				final UnaryOperator<EObjectWrapper> convert = source -> {
					final EObjectWrapper vesselCharter = (EObjectWrapper) cargoPackage.getEFactoryInstance().create(clsVesselCharter);
					for (final EStructuralFeature feature : source.eClass().getEAllStructuralFeatures()) {
						// Skip unset fields
						if (!source.eIsSet(feature)) {
							continue;
						}

						// Copy/move features
						if (feature instanceof final EAttribute attrib) {
							if (attrib.isMany()) {
								vesselCharter.setAttrib(feature.getName(), source.getAttribAsList(feature.getName()));
							} else {
								vesselCharter.setAttrib(feature.getName(), source.getAttrib(feature.getName()));
							}
						} else if (feature instanceof final EReference ref) {
							if (ref.isMany()) {
								vesselCharter.setRef(feature.getName(), source.getRefAsList(feature.getName()));
							} else {
								vesselCharter.setRef(feature.getName(), source.getRef(feature.getName()));
							}
						}
					}
					return vesselCharter;
				};

				// Check cargo model
				{
					final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
					if (vesselAvailabilities != null) {
						final List<EObjectWrapper> newCharters = new LinkedList<>();
						for (final var vesselAvailability : vesselAvailabilities) {
							final EObjectWrapper vesselCharter = convert.apply(vesselAvailability);
							replacements.put(vesselAvailability, vesselCharter);
							newCharters.add(vesselCharter);
						}
						cargoModel.unsetFeature("vesselAvailabilities");
						cargoModel.setRef("vesselCharters", newCharters);
					}
				}

				// Check extra charters in solutions
				final TreeIterator<EObject> itr = analysisModel.eAllContents();
				while (itr.hasNext()) {
					final EObject obj = itr.next();

					if (clsFullVesselCharterOption.isInstance(obj)) {
						final EObjectWrapper option = (EObjectWrapper) obj;
						final EObjectWrapper vesselAvailability = option.getRef("vesselCharter");
						final EObjectWrapper vesselCharter = convert.apply(vesselAvailability);
						replacements.put(vesselAvailability, vesselCharter);
						option.setRef("vesselCharterTmp", vesselCharter);
					}

					if (clsAbstractSolutionSet.isInstance(obj)) {

						final EObjectWrapper solutionSet = (EObjectWrapper) obj;
						final List<EObjectWrapper> vesselAvailabilities = solutionSet.getRefAsList("extraVesselAvailabilities");
						if (vesselAvailabilities != null) {
							final List<EObjectWrapper> newCharters = new LinkedList<>();
							for (final var vesselAvailability : vesselAvailabilities) {
								final EObjectWrapper vesselCharter = convert.apply(vesselAvailability);
								replacements.put(vesselAvailability, vesselCharter);
								newCharters.add(vesselCharter);
							}
							solutionSet.unsetFeature("extraVesselAvailabilities");
							solutionSet.setRef("extraVesselCharters", newCharters);
						}

						itr.prune();
					}
				}

				// Stage 2 - look up any cross-references and update

				final Map<EObject, Collection<EStructuralFeature.Setting>> crossReferences = EcoreUtil.UsageCrossReferencer.findAll(replacements.keySet(), scenarioModel);
				for (final var e : crossReferences.entrySet()) {
					final var vesselAvailability = e.getKey();
					final Collection<EStructuralFeature.Setting> usages = e.getValue();
					for (final EStructuralFeature.Setting setting : usages) {
						if (setting.getEStructuralFeature() instanceof final EReference ref && ref.isContainment()) {
							continue;
						}

						// These are all the places we expect a vessel charter to be used in the ecore
						// model
						if (clsSequence.isInstance(setting.getEObject()) && setting.getEStructuralFeature().getName().equals("vesselAvailability")) {
							final EObjectWrapper sequence = (EObjectWrapper) setting.getEObject();
							sequence.setRef("vesselCharter", replacements.get(vesselAvailability));
							sequence.unsetFeature("vesselAvailability");
						} else if (clsExistingVesselCharterOption.isInstance(setting.getEObject()) && setting.getEStructuralFeature().getName().equals("vesselCharter")) {
							final EObjectWrapper sequence = (EObjectWrapper) setting.getEObject();
							sequence.setRef("vesselCharterTmp", replacements.get(vesselAvailability));
							sequence.unsetFeature("vesselCharter");
						} else if (clsDesSpacingAllocation.isInstance(setting.getEObject()) && setting.getEStructuralFeature().getName().equals("vessel")) {
							final EObjectWrapper sequence = (EObjectWrapper) setting.getEObject();
							sequence.setRef("vesselCharter", replacements.get(vesselAvailability));
							sequence.unsetFeature("vessel");
						} else {
							// !! Here replace in the list e.g. the super type
							final EObjectWrapper owner = (EObjectWrapper) setting.getEObject();
							if (setting.getEStructuralFeature().isMany()) {
								final List l = ((List<?>) owner.eGet(setting.getEStructuralFeature()));
								final int idx = l.indexOf(vesselAvailability);
								l.set(idx, replacements.get(vesselAvailability));

							} else {
								owner.eSet(setting.getEStructuralFeature(), replacements.get(vesselAvailability));
							}
						}
					}
				}
			}
		}

		// Part 2 - rename VesselClassRouteParameters to VesselRouteParameters
		{
			final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
			final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
			
			final EClass clsVesselRouteParameters = MetamodelUtils.getEClass(fleetPackage, "VesselRouteParameters");

			final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
			if (vessels != null) {
				for (final EObjectWrapper vessel : vessels) {
					final List<EObjectWrapper> routeParameters = vessel.getRefAsList("routeParameters");
					if (routeParameters != null) {
						final List<EObjectWrapper> newParameters = new LinkedList<>();
						
						for (final var source : routeParameters) {
							final EObjectWrapper newParams = (EObjectWrapper) fleetPackage.getEFactoryInstance().create(clsVesselRouteParameters);

							for (final EStructuralFeature feature : source.eClass().getEAllStructuralFeatures()) {
								// Skip unset fields
								if (!source.eIsSet(feature)) {
									continue;
								}
	
								// Copy/move features
								if (feature instanceof final EAttribute attrib) {
									if (attrib.isMany()) {
										newParams.setAttrib(feature.getName(), source.getAttribAsList(feature.getName()));
									} else {
										newParams.setAttrib(feature.getName(), source.getAttrib(feature.getName()));
									}
								} else if (feature instanceof final EReference ref) {
									if (ref.isMany()) {
										newParams.setRef(feature.getName(), source.getRefAsList(feature.getName()));
									} else {
										newParams.setRef(feature.getName(), source.getRef(feature.getName()));
									}
								}
							}
							
							newParameters.add(newParams);
						}
						
						vessel.unsetFeature("routeParameters");
						vessel.setRef("routeParametersTmp", newParameters);
					}
				}
			}
		}

	}
}