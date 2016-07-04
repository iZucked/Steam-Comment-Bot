/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.IRestrictedElementsProvider;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestBootstrapModule;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScopeModule;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;

@RunWith(value = ShiroRunner.class)
public class SlotOverridesTest {
	/*
	 * Tests needed:
	 * 
	 * Test that the transformer correctly pulls the following fields for a contract from the slot overrides if set, otherwise from the contract fields themselves
	 * 
	 * Contract - entity - price expression - restricted ports - restricted contracts - restrictions are permissive SalesContract - min cv value - max cv value - cargo type
	 */

	static class SlotTester {
		@NonNull
		private final Injector parentInjector;

		@NonNull
		final ModelEntityMap modelEntityMap;

		public SlotTester(@NonNull final LNGScenarioModel scenario) {
			this(scenario, false);
		}

		public SlotTester(@NonNull final LNGScenarioModel scenario, final boolean failSilently) {

			final OptimiserSettings settings = ScenarioUtils.createDefaultSettings();
			final @NonNull Set<@NonNull String> hints = LNGTransformerHelper.getHints(settings);

			final Collection<IOptimiserInjectorService> services = LNGTransformerHelper.getOptimiserInjectorServices(new TransformerExtensionTestBootstrapModule(), null);

			final List<Module> modules = new LinkedList<>();
			modules.add(new PerChainUnitScopeModule());
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new DataComponentProviderModule(), services, IOptimiserInjectorService.ModuleType.Module_DataComponentProviderModule, hints));
			modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGTransformerModule(scenario, hints), services, IOptimiserInjectorService.ModuleType.Module_LNGTransformerModule, hints));

			parentInjector = Guice.createInjector(modules);

			modelEntityMap = parentInjector.getInstance(ModelEntityMap.class);

			// This triggers a side-effect of building the internal optimisation data and must be called.
			parentInjector.getInstance(IOptimisationData.class);
		}

		public <T> T getOptimiserObject(@NonNull final EObject modelObject, @NonNull final Class<? extends T> clz) {
			return modelEntityMap.getOptimiserObject(modelObject, clz);
		}

		public <T> T getModelObject(final Object internalObject, final Class<? extends T> clz) {
			return modelEntityMap.getModelObject(internalObject, clz);
		}

		public <T> T getProvider(final Class<? extends T> clz) {
			return parentInjector.getInstance(clz);
		}

		public List<Slot> getRestrictedFollowerSlots(final Slot slot) {
			final IPortSlotProvider psProvider = getProvider(IPortSlotProvider.class);

			// pull the load slot out of the transformed data
			final ILoadSlot oSlot = getOptimiserObject(slot, ILoadSlot.class);

			final ISequenceElement element = psProvider.getElement(oSlot);
			final IRestrictedElementsProvider reProvider = getProvider(IRestrictedElementsProvider.class);

			final ArrayList<Slot> result = new ArrayList<Slot>();
			for (final ISequenceElement restricted : reProvider.getRestrictedFollowerElements(element)) {
				final IPortSlot restrictedSlot = psProvider.getPortSlot(restricted);
				result.add(getModelObject(restrictedSlot, Slot.class));
			}

			return result;
		}

		public List<Slot> getRestrictedPrecedingSlots(final Slot slot) {
			final IPortSlotProvider psProvider = getProvider(IPortSlotProvider.class);

			// pull the load slot out of the transformed data
			final IDischargeSlot oSlot = getOptimiserObject(slot, IDischargeSlot.class);

			final ISequenceElement element = psProvider.getElement(oSlot);
			final IRestrictedElementsProvider reProvider = getProvider(IRestrictedElementsProvider.class);

			final ArrayList<Slot> result = new ArrayList<Slot>();
			for (final ISequenceElement restricted : reProvider.getRestrictedPrecedingElements(element)) {
				final IPortSlot restrictedSlot = psProvider.getPortSlot(restricted);
				result.add(getModelObject(restrictedSlot, Slot.class));
			}

			return result;
		}
	}

	@Test
	public void testOverrideEntity() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// add a new legal entity
		final LegalEntity eEntity = msc.addEntity("Override Entity");

		// override the first slot's entity
		final Slot eSlot = msc.cargo.getSortedSlots().get(0);
		eSlot.setEntity(eEntity);

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the entity out of the transformed data
		final IEntityProvider entityProvider = tester.getProvider(IEntityProvider.class);
		final IPortSlot oSlot = tester.getOptimiserObject(eSlot, IPortSlot.class);
		final IEntity oEntity = entityProvider.getEntityForSlot(oSlot);

		// make sure it matches
		Assert.assertEquals(eEntity, tester.getModelObject(oEntity, LegalEntity.class));
	}

	@Test
	public void testNoOverrideEntity() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// add a new legal entity
		final LegalEntity dummyEntity = msc.addEntity("Override Entity");

		// don't override the first slot's entity
		final Slot eSlot = msc.cargo.getSortedSlots().get(0);
		// expect the entity from the contract
		final BaseLegalEntity eEntity = eSlot.getContract().getEntity();

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the entity out of the transformed data
		final IEntityProvider entityProvider = tester.getProvider(IEntityProvider.class);
		final IPortSlot oSlot = tester.getOptimiserObject(eSlot, IPortSlot.class);
		final IEntity oEntity = entityProvider.getEntityForSlot(oSlot);

		// make sure it matches
		Assert.assertEquals(eEntity, tester.getModelObject(oEntity, BaseLegalEntity.class));
	}

	@Test
	public void testOverrideCvValues() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final int CONTRACT_MIN_CV = 10;
		final int CONTRACT_MAX_CV = 100;
		final int SLOT_MIN_CV = 15;
		final int SLOT_MAX_CV = 150;

		// override the discharge slot's min & max CV
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		final SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setMinCvValue(CONTRACT_MIN_CV);
		eSalesContract.setMaxCvValue(CONTRACT_MAX_CV);
		eSlot.setMinCvValue(SLOT_MIN_CV);
		eSlot.setMaxCvValue(SLOT_MAX_CV);

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);

		final long minCv = oSlot.getMinCvValue();
		final long maxCv = oSlot.getMaxCvValue();

		// make sure it matches
		Assert.assertEquals(Calculator.HighScaleFactor * SLOT_MIN_CV, minCv);
		Assert.assertEquals(Calculator.HighScaleFactor * SLOT_MAX_CV, maxCv);
	}

	@Test
	public void testNoOverrideCvValues() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		final int CONTRACT_MIN_CV = 10;
		final int CONTRACT_MAX_CV = 100;

		// override the discharge slot's min & max CV
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		final SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setMinCvValue(CONTRACT_MIN_CV);
		eSalesContract.setMaxCvValue(CONTRACT_MAX_CV);

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);

		final long minCv = oSlot.getMinCvValue();
		final long maxCv = oSlot.getMaxCvValue();

		// make sure it matches
		Assert.assertEquals(Calculator.HighScaleFactor * CONTRACT_MIN_CV, minCv);
		Assert.assertEquals(Calculator.HighScaleFactor * CONTRACT_MAX_CV, maxCv);
	}

	@Test
	public void testDefaultCvValues() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// override the discharge slot's min & max CV
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);

		final long minCv = oSlot.getMinCvValue();
		final long maxCv = oSlot.getMaxCvValue();

		// make sure it matches
		Assert.assertEquals(0l, minCv);
		Assert.assertEquals(Long.MAX_VALUE, maxCv);
	}

	public <T> void assertArrayEqualsCollection(final T[] expected, final Collection<T> actual) {
		Assert.assertEquals(expected.length, actual.size());
		final HashSet<T> expectedSet = new HashSet<T>();
		final HashSet<T> remainder = new HashSet<T>();

		for (int i = 0; i < expected.length; i++) {
			expectedSet.add(expected[i]);
		}

		remainder.addAll(actual);
		remainder.removeAll(expectedSet);

		Assert.assertTrue(String.format("%d unexpected element(s) found in collection", remainder.size()), remainder.isEmpty());

		remainder.addAll(expectedSet);
		remainder.removeAll(actual);

		Assert.assertTrue(String.format("%d expected element(s) not found in collection", remainder.size()), remainder.isEmpty());
	}

	class MultipleCargoCreator extends MinimalScenarioCreator {
		final public Cargo[] cargoes = new Cargo[3];
		final public Port[] loadPorts = new Port[3];
		final public Port[] dischargePorts = new Port[3];
		final public LoadSlot[] loadSlots = new LoadSlot[3];
		final public DischargeSlot[] dischargeSlots = new DischargeSlot[3];

		public MultipleCargoCreator() {
			super();
			loadPorts[0] = loadPort;
			loadPorts[1] = portCreator.createPort("Load Port 2", null);
			loadPorts[2] = portCreator.createPort("Load Port 3", null);

			dischargePorts[0] = dischargePort;
			dischargePorts[1] = portCreator.createPort("Discharge Port 2", null);
			dischargePorts[2] = portCreator.createPort("Discharge Port 3", null);

			cargoes[0] = cargo;
			cargoes[1] = cargoCreator.createDefaultCargo("Cargo2", loadPorts[1], dischargePorts[1], null, 120);
			cargoes[2] = cargoCreator.createDefaultCargo("Cargo3", loadPorts[2], dischargePorts[2], null, 120);

			for (int i = 0; i < 3; i++) {
				loadSlots[i] = (LoadSlot) cargoes[i].getSortedSlots().get(0);
				dischargeSlots[i] = (DischargeSlot) cargoes[i].getSortedSlots().get(1);
			}
		}
	}

	@Test
	public void testOverrideRestrictedPorts() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// do override the contracts' restricted ports
		msc.loadSlots[0].setOverrideRestrictions(true);
		msc.dischargeSlots[1].setOverrideRestrictions(true);

		msc.loadSlots[0].getRestrictedPorts().add(msc.dischargePorts[1]);
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.dischargeSlots[1].getRestrictedPorts().add(msc.loadPorts[2]);
		msc.dischargeSlots[1].getContract().getRestrictedPorts().add(msc.loadPorts[0]);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[1] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
		assertArrayEqualsCollection(new Slot[] { msc.loadSlots[2] }, tester.getRestrictedPrecedingSlots(msc.dischargeSlots[1]));

	}

	@Test
	public void testOverrideRestrictedContracts() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// set up a new contract on discharge slot 1
		final Contract forbidden = msc.addSalesContract("Forbidden Purchase Contract", 14);
		msc.dischargeSlots[1].setContract(forbidden);

		// do override the contracts' restricted contracts
		msc.loadSlots[0].getRestrictedContracts().add(forbidden);
		msc.loadSlots[0].getContract().getRestrictedContracts().add(msc.salesContract);

		msc.loadSlots[0].setOverrideRestrictions(true);
		msc.dischargeSlots[1].setOverrideRestrictions(true);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[1] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
	}

	@Test
	public void testNoOverrideRestrictedContracts() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// set up a new contract on discharge slot 1
		final Contract forbidden = msc.addSalesContract("Forbidden Purchase Contract", 14);
		msc.dischargeSlots[1].setContract(forbidden);

		// do override the contracts' restricted contracts
		msc.loadSlots[0].getContract().getRestrictedContracts().add(msc.salesContract);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[0], msc.dischargeSlots[2] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
	}

	@Test
	public void testNoOverrideRestrictedPorts() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// don't override the contracts' restricted ports
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.dischargeSlots[1].getContract().getRestrictedPorts().add(msc.loadPorts[0]);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[2] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
		assertArrayEqualsCollection(new Slot[] { msc.loadSlots[0] }, tester.getRestrictedPrecedingSlots(msc.dischargeSlots[1]));

	}

	@Test
	public void testOverrideRestrictedPermissivity() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// do override the contracts' restricted permissivity
		msc.loadSlots[0].getRestrictedPorts().add(msc.dischargePorts[1]);
		msc.loadSlots[0].setRestrictedListsArePermissive(false);
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.loadSlots[0].getContract().setRestrictedListsArePermissive(true);

		msc.loadSlots[0].setOverrideRestrictions(true);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[1] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
	}

	@Test
	public void testNoOverrideRestrictedPermissivity() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// do override the contracts' restricted permissivity
		msc.loadSlots[0].getRestrictedPorts().add(msc.dischargePorts[1]);
		// These should be ignored due to slot override
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.loadSlots[0].getContract().setRestrictedListsArePermissive(true);

		msc.loadSlots[0].setOverrideRestrictions(true);

		/*
		 * build the optimiser data (it currently barfs when trying to construct the initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[1] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));

	}

	// @Test
	public void testOverrideShipmentType() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();
		final LNGScenarioModel scenario = msc.buildScenario();

		// override the discharge slot's delivery type
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		final SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setPurchaseDeliveryType(CargoDeliveryType.NOT_SHIPPED);
		eSlot.setPurchaseDeliveryType(CargoDeliveryType.SHIPPED);

		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);

		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);

	}

}
