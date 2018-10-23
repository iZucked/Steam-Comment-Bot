package com.mmxlabs.lingo.its.tests.microcases.adp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;

public class SimpleADPTests extends AbstractADPAndLightWeightTests {
	
	@Test
	public void testEvaluateDoesNotUpair() {

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsAndContracts();

		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(YearMonth.of(2018, 10), YearMonth.of(2019, 10), defaultCharterInMarket);

		setSimple12CargoCase(adpModelBuilder);

		// Generate all the ADP slots
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());

		final PurchaseContract purchaseContract = commercialModelFinder.findPurchaseContract("Purchase A");
		final SalesContract salesContract = commercialModelFinder.findSalesContract("Sales A");

		Cargo testCargo = cargoModelBuilder.makeCargo() ///
				.makeFOBPurchase("F1", LocalDate.of(2018, 11, 1), purchaseContract.getPreferredPort(), purchaseContract, null, null).build() //
				.makeDESSale("D1", LocalDate.of(2018, 12, 1), salesContract.getPreferredPort(), salesContract, null, null).build() //
				.build();

		Slot load = testCargo.getSlots().get(0);
		Slot discharge = testCargo.getSlots().get(1);

		final OptimisationPlan optimisationPlan = createOptimisationPlan();

		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withThreadCount(1) //
				.buildDefaultRunner();

		try {
			runnerBuilder.evaluateInitialState();
		} finally {
			runnerBuilder.dispose();
		}

		Assert.assertNotNull(testCargo.eContainer());
		Assert.assertNotNull(load.getCargo());
		Assert.assertNotNull(discharge.getCargo());
	}

	private void setSimple12CargoCase(final ADPModelBuilder adpModelBuilder) {
		adpModelBuilder.withPurchaseContractProfile(commercialModelFinder.findPurchaseContract("Purchase A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.FOB) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_PURCHASE) //
				.withCargoNumberDistributionModel(3_000_000, LNGVolumeUnit.MMBTU, 12) //
				.build() //
				//
				.addMaxCargoConstraint(1, IntervalType.MONTHLY) //
				.build();

		adpModelBuilder.withSalesContractProfile(commercialModelFinder.findSalesContract("Sales A")) //
				.withVolume(3_000_000 * 12, LNGVolumeUnit.MMBTU) // Not really used...
				//
				.withSubContractProfile("Volume") //
				.withContractType(ContractType.DES) //
				.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
				.withCargoNumberDistributionModel(3_000_000, LNGVolumeUnit.MMBTU, 12) //
				.build() //
				//
				.build();
	}
	
	private CharterInMarket setDefaultVesselsAndContracts() {
		final Vessel vesselEbisu = fleetModelFinder.findVessel("LNG Ebisu");
		final Vessel vesselRogers = fleetModelFinder.findVessel("Woodside Rogers");
		final Port pluto = portFinder.findPort("Pluto");
		final Port himeji = portFinder.findPort("Himeji");
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselEbisu, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(true);

		@SuppressWarnings("unused")
		final VesselAvailability vesselAvailability = cargoModelBuilder.makeVesselAvailability(vesselRogers, entity) //
				.withStartWindow(LocalDateTime.of(2018, 10, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2019, 10, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();
		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setPreferredPort(pluto);
		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, "8");
		salesContract.setPreferredPort(himeji);
		return defaultCharterInMarket;
	}

	
	protected OptimisationPlan createOptimisationPlan() {
		// create plan in parent
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(createUserSettings());
		// and now delete lso and hill
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}

}
