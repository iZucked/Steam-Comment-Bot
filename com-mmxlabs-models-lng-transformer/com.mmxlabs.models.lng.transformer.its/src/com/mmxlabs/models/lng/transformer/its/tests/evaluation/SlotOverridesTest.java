package com.mmxlabs.models.lng.transformer.its.tests.evaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.IRestrictedElementsProvider;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.its.tests.MinimalScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestModule;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;
import com.mmxlabs.models.lng.transformer.util.ScenarioUtils;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class SlotOverridesTest {
	/* Tests needed:
	 * 
	 * Test that the transformer correctly pulls the following fields for a contract
	 * from the slot overrides if set, otherwise from the contract fields themselves
	 * 
	 * Contract
	 * - entity
	 * - price expression
	 * - restricted ports
	 * - restricted contracts
	 * - restrictions are permissive
	 * SalesContract
	 * - min cv value
	 * - max cv value
	 * - cargo type
	 * 
	 */
	
	static class SlotTester {
		final LNGTransformer transformer;
		final ModelEntityMap modelOptimiserMap;
		
		public SlotTester(LNGScenarioModel scenario) {
			this(scenario, false);
		}
		
		public SlotTester(LNGScenarioModel scenario, boolean failSilently) {
			transformer = new LNGTransformer(scenario, ScenarioUtils.createDefaultSettings(), new TransformerExtensionTestModule());
			try {
				LNGSchedulerJobUtils.evaluateCurrentState(transformer);
			}
			catch (Exception e) {
				if (failSilently == false)	e.printStackTrace(System.err);
			}
			modelOptimiserMap = transformer.getEntities();						
		}
		
		public <T> T getOptimiserObject(final EObject modelObject, final Class<? extends T> clz) {
			return modelOptimiserMap.getOptimiserObject(modelObject, clz);
		}
		
		public <T> T getModelObject(final Object internalObject, final Class<? extends T> clz) {
			return modelOptimiserMap.getModelObject(internalObject, clz);
		}
		
		public <T> T getProvider(final Class<? extends T> clz) {
			return transformer.getInjector().getInstance(clz);
		}
		
		public List<Slot> getRestrictedFollowerSlots(Slot slot) {
			final IPortSlotProvider psProvider = getProvider(IPortSlotProvider.class);
			
			// pull the load slot out of the transformed data
			final ILoadSlot oSlot = getOptimiserObject(slot, ILoadSlot.class);
			
			ISequenceElement element = psProvider.getElement(oSlot);			
			IRestrictedElementsProvider reProvider = getProvider(IRestrictedElementsProvider.class);				
			
			ArrayList<Slot> result = new ArrayList<Slot>();
			for (ISequenceElement restricted: reProvider.getRestrictedFollowerElements(element)) {
				IPortSlot restrictedSlot = psProvider.getPortSlot(restricted);
				result.add(getModelObject(restrictedSlot, Slot.class));
			}

			return result;			
		}

		public List<Slot> getRestrictedPrecedingSlots(Slot slot) {
			final IPortSlotProvider psProvider = getProvider(IPortSlotProvider.class);
			
			// pull the load slot out of the transformed data
			final IDischargeSlot oSlot = getOptimiserObject(slot, IDischargeSlot.class);
			
			ISequenceElement element = psProvider.getElement(oSlot);			
			IRestrictedElementsProvider reProvider = getProvider(IRestrictedElementsProvider.class);				
			
			ArrayList<Slot> result = new ArrayList<Slot>();
			for (ISequenceElement restricted: reProvider.getRestrictedPrecedingElements(element)) {
				IPortSlot restrictedSlot = psProvider.getPortSlot(restricted);
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
		IEntity oEntity = entityProvider.getEntityForSlot(oSlot);
		
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
		final LegalEntity eEntity = eSlot.getContract().getEntity();
		
		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);
		
		// pull the entity out of the transformed data
		final IEntityProvider entityProvider = tester.getProvider(IEntityProvider.class);
		final IPortSlot oSlot = tester.getOptimiserObject(eSlot, IPortSlot.class);
		IEntity oEntity = entityProvider.getEntityForSlot(oSlot);
		
		// make sure it matches
		Assert.assertEquals(eEntity, tester.getModelObject(oEntity, LegalEntity.class));
	}

	@Test
	public void testOverrideCvValues() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();		
		final LNGScenarioModel scenario = msc.buildScenario();
		
		int CONTRACT_MIN_CV = 10;
		int CONTRACT_MAX_CV = 100;
		int SLOT_MIN_CV = 15;
		int SLOT_MAX_CV = 150;
		
		// override the discharge slot's min & max CV
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setMinCvValue(CONTRACT_MIN_CV);
		eSalesContract.setMaxCvValue(CONTRACT_MAX_CV);
		eSlot.setMinCvValue(SLOT_MIN_CV);
		eSlot.setMaxCvValue(SLOT_MAX_CV);
		
		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);
		
		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);
		
		long minCv = oSlot.getMinCvValue();
		long maxCv = oSlot.getMaxCvValue();
		
		// make sure it matches
		Assert.assertEquals(Calculator.HighScaleFactor * SLOT_MIN_CV, minCv);
		Assert.assertEquals(Calculator.HighScaleFactor * SLOT_MAX_CV, maxCv);
	}

	@Test
	public void testNoOverrideCvValues() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();		
		final LNGScenarioModel scenario = msc.buildScenario();
		
		int CONTRACT_MIN_CV = 10;
		int CONTRACT_MAX_CV = 100;
		
		// override the discharge slot's min & max CV
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setMinCvValue(CONTRACT_MIN_CV);
		eSalesContract.setMaxCvValue(CONTRACT_MAX_CV);
		
		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);
		
		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);
		
		long minCv = oSlot.getMinCvValue();
		long maxCv = oSlot.getMaxCvValue();
		
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
		
		long minCv = oSlot.getMinCvValue();
		long maxCv = oSlot.getMaxCvValue();
		
		// make sure it matches
		Assert.assertEquals(0l, minCv);
		Assert.assertEquals(Long.MAX_VALUE, maxCv);
	}
	
	public <T> void assertArrayEqualsCollection(T [] expected, Collection<T> actual) {
		Assert.assertEquals(expected.length, actual.size());
		HashSet<T> expectedSet = new HashSet<T>();
		HashSet<T> remainder = new HashSet<T>();
				
		for (int i = 0 ; i < expected.length; i++) {
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
		final public Cargo [] cargoes = new Cargo [3];
		final public Port [] loadPorts = new Port [3];
		final public Port [] dischargePorts = new Port [3];
		final public LoadSlot [] loadSlots = new LoadSlot [3];
		final public DischargeSlot [] dischargeSlots = new DischargeSlot [3];
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
		msc.loadSlots[0].getRestrictedPorts().add(msc.dischargePorts[1]);
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.dischargeSlots[1].getRestrictedPorts().add(msc.loadPorts[2]);
		msc.dischargeSlots[1].getContract().getRestrictedPorts().add(msc.loadPorts[0]);

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
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
		Contract forbidden = msc.addSalesContract("Forbidden Purchase Contract", 14);
		msc.dischargeSlots[1].setContract(forbidden);		

		// do override the contracts' restricted contracts
		msc.loadSlots[0].getRestrictedContracts().add(forbidden);
		msc.loadSlots[0].getContract().getRestrictedContracts().add(msc.salesContract);

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.dischargeSlots[1] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));		
	}

	@Test
	public void testNoOverrideRestrictedContracts() {
		final MultipleCargoCreator msc = new MultipleCargoCreator();		
		final LNGScenarioModel scenario = msc.buildScenario();
		
		// set up a new contract on discharge slot 1
		Contract forbidden = msc.addSalesContract("Forbidden Purchase Contract", 14);
		msc.dischargeSlots[1].setContract(forbidden);		

		// do override the contracts' restricted contracts
		msc.loadSlots[0].getContract().getRestrictedContracts().add(msc.salesContract);

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
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

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
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

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
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
		msc.loadSlots[0].getContract().getRestrictedPorts().add(msc.dischargePorts[2]);
		msc.loadSlots[0].getContract().setRestrictedListsArePermissive(true);

		/* build the optimiser data (it currently barfs when trying to construct the 
		 * initial sequence, but the restrictions are built correctly)
		 */
		final SlotTester tester = new SlotTester(scenario, true);

		assertArrayEqualsCollection(new Slot[] { msc.loadSlots[0], msc.loadSlots[1], msc.loadSlots[2], msc.dischargeSlots[0], msc.dischargeSlots[2] }, tester.getRestrictedFollowerSlots(msc.loadSlots[0]));
		
	}

	//@Test
	public void testOverrideShipmentType() {
		final MinimalScenarioCreator msc = new MinimalScenarioCreator();		
		final LNGScenarioModel scenario = msc.buildScenario();
		
		// override the discharge slot's delivery type
		final DischargeSlot eSlot = (DischargeSlot) msc.cargo.getSortedSlots().get(1);
		SalesContract eSalesContract = (SalesContract) eSlot.getContract();
		eSalesContract.setPurchaseDeliveryType(CargoDeliveryType.NOT_SHIPPED);
		eSlot.setPurchaseDeliveryType(CargoDeliveryType.SHIPPED);
		
		// build the optimiser data
		final SlotTester tester = new SlotTester(scenario);
		
		// pull the discharge slot out of the transformed data
		final IDischargeSlot oSlot = tester.getOptimiserObject(eSlot, IDischargeSlot.class);

	}


	
	
}
