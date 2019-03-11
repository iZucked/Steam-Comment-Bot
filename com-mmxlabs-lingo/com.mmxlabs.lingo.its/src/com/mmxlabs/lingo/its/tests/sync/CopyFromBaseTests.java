/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.sync;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.lingo.its.tests.TesterUtil;
import com.mmxlabs.lingo.its.tests.microcases.AbstractMicroTestCase;
import com.mmxlabs.lngdataserver.data.distances.DataLoader;
import com.mmxlabs.lngdataserver.integration.distances.model.DistancesVersion;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.UpdateJob;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelBuilder;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.manifest.StorageType;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;

@RunWith(value = ShiroRunner.class)
public class CopyFromBaseTests extends AbstractMicroTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		final IScenarioDataProvider scenarioDataProvider = importReferenceData("/trainingcases/Shipping_I/");
		return scenarioDataProvider;
	}

	private LNGScenarioModel initialiseBlankModel() {
		return ScenarioModelBuilder.instantiate().getLNGScenarioModel();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity("Entity");
	}

	@Ignore("Test not ready yet")
	@Test
	public void testDistanceCleanCopy() throws Exception {

		final ScenarioModelRecord baseCase = ScenarioStorageUtil.createFromCopyOf("baseCase", scenarioDataProvider);

		final LNGScenarioModel model = initialiseBlankModel();
		final Manifest manifest = createManifestFromModel(model);
		final ScenarioModelRecord destination = ScenarioStorageUtil.createFromCopyOf("destination", SimpleScenarioDataProvider.make(manifest, initialiseBlankModel()));

		final Set<DataOptions> options = EnumSet.of(DataOptions.PortData);
		try (IScenarioDataProvider baseCaseReference = baseCase.aquireScenarioDataProvider("CopyFromBaseTests:1")) {
			baseCaseReference.getModelReference().executeWithLock(true, () -> {
				try {
					updateDistances(baseCaseReference);
				} catch (final Exception e) {
					Assert.fail(e.getMessage());
				}
			});

			try (IScenarioDataProvider blankCaseReference = destination.aquireScenarioDataProvider("CopyFromBaseTests:2")) {
				final UpdateJob job = new UpdateJob(options, baseCase, Collections.singletonList(destination), true);
				job.run(new NullProgressMonitor());

				final PortModel basePortModel = ScenarioModelUtil.getPortModel(baseCaseReference);
				final PortModel destPortModel = ScenarioModelUtil.getPortModel(blankCaseReference);

				final Comparison comparison = TesterUtil.compareModels(basePortModel, destPortModel);
				Assert.assertTrue(comparison.getDifferences().isEmpty());
			}
		}

	}

	@Test
	public void testCleanPricingCopy() throws Exception {

		final ScenarioModelRecord baseCase = ScenarioStorageUtil.createFromCopyOf("baseCase", scenarioDataProvider);

		final LNGScenarioModel model = initialiseBlankModel();
		final Manifest manifest = createManifestFromModel(model);
		final ScenarioModelRecord destination = ScenarioStorageUtil.createFromCopyOf("destination", SimpleScenarioDataProvider.make(manifest, initialiseBlankModel()));

		final Set<DataOptions> options = EnumSet.of(DataOptions.PricingData);
		try (IScenarioDataProvider baseCaseReference = baseCase.aquireScenarioDataProvider("CopyFromBaseTests:1")) {

			final PricingModel baseModel = ScenarioModelUtil.getPricingModel(baseCaseReference);
			replaceWithSortedCopy(baseModel.getCommodityCurves());
			replaceWithSortedCopy(baseModel.getCharterCurves());
			replaceWithSortedCopy(baseModel.getCurrencyCurves());
			replaceWithSortedCopy(baseModel.getBunkerFuelCurves());
			baseModel.getConversionFactors().clear();
			baseModel.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, "pricinguuid");

			try (IScenarioDataProvider blankCaseReference = destination.aquireScenarioDataProvider("CopyFromBaseTests:2")) {
				final UpdateJob job = new UpdateJob(options, baseCase, Collections.singletonList(destination), true);
				job.run(new NullProgressMonitor());

				final PricingModel destModel = ScenarioModelUtil.getPricingModel(blankCaseReference);
				destModel.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, "pricinguuid");

				replaceWithSortedCopy(destModel.getCommodityCurves());
				replaceWithSortedCopy(destModel.getCharterCurves());
				replaceWithSortedCopy(destModel.getCurrencyCurves());
				replaceWithSortedCopy(destModel.getBunkerFuelCurves());
				destModel.getConversionFactors().clear();
				final Comparison comparison = TesterUtil.compareModels(baseModel, destModel);
				final EList<Diff> differences = comparison.getDifferences();
				// save(baseModel, "C:\\temp\\base.xmi");
				// save(destModel, "C:\\temp\\dest.xmi");
				Assert.assertTrue(differences.isEmpty());
			}
		}

	}

	private Manifest createManifestFromModel(@NonNull final LNGScenarioModel model) {
		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.DISTANCES.getID(), ScenarioModelUtil.getPortModel(model).getDistanceVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.LOCATIONS.getID(), ScenarioModelUtil.getPortModel(model).getPortVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.PORT_GROUPS.getID(), ScenarioModelUtil.getPortModel(model).getPortGroupVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.BUNKER_FUELS.getID(), ScenarioModelUtil.getFleetModel(model).getBunkerFuelsVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID(), ScenarioModelUtil.getFleetModel(model).getVesselGroupVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.FLEET.getID(), ScenarioModelUtil.getFleetModel(model).getFleetVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), ScenarioModelUtil.getPricingModel(model).getMarketCurvesVersionRecord()));
		manifest.getModelDependencies().add(createFor(LNGScenarioSharedModelTypes.SETTLED_PRICES.getID(), ScenarioModelUtil.getPricingModel(model).getSettledPricesVersionRecord()));

		return manifest;
	}

	private ModelArtifact createFor(final String type, final VersionRecord record) {
		final ModelArtifact modelArtifact = ManifestFactory.eINSTANCE.createModelArtifact();
		modelArtifact.setDataVersion(record.getVersion());
		modelArtifact.setStorageType(StorageType.INTERNAL);
		modelArtifact.setType("EOBJECT");
		modelArtifact.setKey(type);
		return modelArtifact;
	}

	private static final String DATAFILE_V1_0_11_250_4_JSON = "v1.0.11.250_4.json";

	private void updateDistances(final IScenarioDataProvider sdp) throws Exception {

		final String input = DataLoader.importData(DATAFILE_V1_0_11_250_4_JSON);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.registerModule(new JavaTimeModule());

		final DistancesVersion originalVersion = mapper.readerFor(DistancesVersion.class).readValue(input);

		final PortModel portModel = ScenarioModelUtil.getPortModel(sdp);
		final EditingDomain editingDomain = sdp.getEditingDomain();

		final Command updateCommand = PortAndDistancesToScenarioCopier.getUpdateCommand(editingDomain, portModel, originalVersion, true);
		RunnerHelper.syncExecDisplayOptional(() -> editingDomain.getCommandStack().execute(updateCommand));
	}

	private <T extends AbstractYearMonthCurve> void updateUUID(final List<T> objects) {

		for (final AbstractYearMonthCurve c : objects) {
			c.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, c.getName());

		}
	}

	private <T extends AbstractYearMonthCurve> void replaceWithSortedCopy(final List<T> objects) {
		final List<T> copy = createSortedCopy(objects);
		objects.clear();
		objects.addAll(copy);

		for (final AbstractYearMonthCurve c : objects) {
			c.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, c.getName());
			if (!c.isSetExpression()) {
				final List copy1 = createSortedCopy2(c.getPoints());
				c.getPoints().clear();
				c.getPoints().addAll(copy1);
			}
		}
	}

	private <T extends NamedObject> List<T> createSortedCopy(final List<T> objects) {
		return objects.stream() //
				.sorted((a, b) -> b.getName().compareTo(a.getName())) //
				.collect(Collectors.toList());
	}

	private List createSortedCopy2(final EList<YearMonthPoint> objects) {
		return objects.stream() //
				.sorted((a, b) -> a.getDate().compareTo(b.getDate())) //
				.collect(Collectors.toList());
	}

	private void save(final EObject model, final String filename) throws IOException {
		final ResourceSetImpl rs = new ResourceSetImpl();
		final Resource r = rs.createResource(URI.createFileURI(filename));
		r.getContents().add(model);
		r.save(null);
	}
}
