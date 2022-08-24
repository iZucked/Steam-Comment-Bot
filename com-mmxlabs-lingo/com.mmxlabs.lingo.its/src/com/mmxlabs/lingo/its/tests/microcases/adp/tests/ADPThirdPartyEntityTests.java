package com.mmxlabs.lingo.its.tests.microcases.adp.tests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mmxlabs.lingo.its.tests.TestMode;
import com.mmxlabs.lingo.its.tests.TestingModes;
import com.mmxlabs.lingo.its.tests.microcases.adp.AbstractADPAndLightWeightTests;
import com.mmxlabs.lingo.its.tests.microcases.adp.OptimisationEMFTestUtils;
import com.mmxlabs.lingo.its.validation.ValidationTestUtil;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ext.impl.AbstractSlotTemplateFactory;
import com.mmxlabs.models.lng.adp.util.ADPModelBuilder;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class ADPThirdPartyEntityTests extends AbstractADPAndLightWeightTests {

	private static final String THIRD_PARTY_ENTITY_NAME = "ThirdPartyEntity";

	@Disabled
	@Test
	public void testThirdPartyCargoesStayPairedFeasible() {
		Assumptions.assumeTrue(TestingModes.OptimisationTestMode == TestMode.Run);
		
		final LNGScenarioModel lngScenarioModel = scenarioModelFinder.getLNGScenarioModel();
		lngScenarioModel.setPromptPeriodStart(LocalDate.of(2022, 11, 1));
		lngScenarioModel.setPromptPeriodEnd(LocalDate.of(2023, 2, 1));

		final YearMonth start = YearMonth.of(2022, 10);
		final YearMonth end = YearMonth.of(2023, 10);

		final CharterInMarket defaultCharterInMarket = setDefaultVesselsContractsAndEntities();
		final ADPModelBuilder adpModelBuilder = scenarioModelBuilder.initialiseADP(start, end, defaultCharterInMarket);

		final PurchaseContract purchaseContract = commercialModelFinder.findPurchaseContract("Purchase A");
		purchaseContract.setStartDate(start);
		purchaseContract.setEndDate(end);
		purchaseContract.setContractYearStart(9);

		final SalesContract salesContract = commercialModelFinder.findSalesContract("Sales A");
		salesContract.setStartDate(start);
		salesContract.setEndDate(end);
		salesContract.setContractYearStart(9);

		final SalesContract thirdPartySalesContract = commercialModelFinder.findSalesContract("Third party contract");
		thirdPartySalesContract.setStartDate(start);
		thirdPartySalesContract.setEndDate(end);
		thirdPartySalesContract.setContractYearStart(9);

		{
			final List<LocalDate> loadDates = new ArrayList<>();
			loadDates.add(LocalDate.of(2022, 10, 3));
			adpModelBuilder.withPurchaseContractProfile(purchaseContract) //
					.withSubContractProfile("Pre-generated") //
					.withContractType(ContractType.FOB) //
					.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_PURCHASE) //
					.withPreDefinedDistributionModel(3, TimePeriod.DAYS, loadDates).build() //
					.withPeriodConstraint().withRow(2, 2, YearMonth.of(2022, 10)).build();
		}
		{
			final List<LocalDate> dischargeDates = new ArrayList<>();
			dischargeDates.add(LocalDate.of(2022, 10, 1));
			dischargeDates.add(LocalDate.of(2022, 10, 1));
			adpModelBuilder.withSalesContractProfile(salesContract) //
					.withSubContractProfile("Pre-generated") //
					.withContractType(ContractType.DES) //
					.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_DES_SALE) //
					.withPreDefinedDistributionModel(1, TimePeriod.MONTHS, dischargeDates) //
					.build() //
					.withPeriodConstraint() //
					.withRow(1, 2, YearMonth.of(2022, 10)) //
					.build();
		}
		{
			final List<LocalDate> dischargeDates = new ArrayList<>();
			adpModelBuilder.withSalesContractProfile(thirdPartySalesContract) //
					.withSubContractProfile("Pre-generated") //
					.withContractType(ContractType.FOB) //
					.withSlotTemplate(AbstractSlotTemplateFactory.TEMPLATE_GENERIC_FOB_SALE) //
					.withPreDefinedDistributionModel(1, TimePeriod.MONTHS, dischargeDates) //
					.build().withPeriodConstraint() //
					.withRow(1, 1, YearMonth.of(2022, 10)) //
					.build()//
					.build();
		}
		ADPModelUtil.generateModelSlots(scenarioModelBuilder.getLNGScenarioModel(), adpModelBuilder.getADPModel());
		final ADPModel adpModel = adpModelBuilder.getADPModel();

		final Cargo thirdPartyCargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("ThirdPartyPurchase", LocalDate.of(2022, 10, 15), purchaseContract.getPreferredPort(), purchaseContract, thirdPartySalesContract.getEntity(), null) //
				.build() //
				.makeFOBSale("ThirdPartySale", FOBSaleDealType.SOURCE_ONLY, LocalDate.of(2022, 10, 15), purchaseContract.getPreferredPort(), thirdPartySalesContract,
						thirdPartySalesContract.getEntity(), null, fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160)) //
				.build() //
				.build();
		thirdPartyCargo.setAllowRewiring(false);

		{
			final IStatus status = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertTrue(status.isOK());
		}

		final OptimisationPlan optimisationPlan = createOptimisationPlan();
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(scenarioDataProvider, null) //
				.withOptimisationPlan(optimisationPlan) //
				.withThreadCount(1) //
				.buildDefaultRunner();
		runnerBuilder.run(true);

		Assertions.assertFalse(thirdPartyCargo.getSlots().isEmpty());

		{
			final IStatus status = ValidationTestUtil.validate(scenarioDataProvider, false, false);
			Assertions.assertTrue(status.isOK());
		}
	}

	private CharterInMarket setDefaultVesselsContractsAndEntities() {
		final Vessel vesselSmall = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_138);
		final Vessel vesselMedium = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Port darwin = portFinder.findPortById(InternalDataConstants.PORT_DARWIN);
		final Port himeji = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final CharterInMarket defaultCharterInMarket = spotMarketsModelBuilder.createCharterInMarket("ADP Default", vesselSmall, entity, "50000", 0);
		defaultCharterInMarket.setNominal(true);
		defaultCharterInMarket.setEnabled(false);

		cargoModelBuilder.makeVesselCharter(vesselMedium, entity) //
				.withStartWindow(LocalDateTime.of(2022, 10, 1, 0, 0)) //
				.withStartHeel(1_000, 3_000, 22.6, "5") //
				.withEndWindow(LocalDateTime.of(2023, 10, 1, 0, 0)) //
				.withEndHeel(0, 5_000, EVesselTankState.EITHER, "7") //
				.withCharterRate("50000") //
				.build();

		if (entity.getShippingBook().getTaxRates().isEmpty()) {
			entity.getShippingBook().getTaxRates().add(createDefaultTaxRate());
		}
		if (entity.getTradingBook().getTaxRates().isEmpty()) {
			entity.getTradingBook().getTaxRates().add(createDefaultTaxRate());
		}
		if (entity.getUpstreamBook().getTaxRates().isEmpty()) {
			entity.getTradingBook().getTaxRates().add(createDefaultTaxRate());
		}

		final BaseLegalEntity thirdPartyEntity = commercialModelBuilder.createEntity(THIRD_PARTY_ENTITY_NAME, LocalDate.of(2022, 10, 1), 0.05f);
		thirdPartyEntity.setThirdParty(true);

		final PurchaseContract purchaseContract = commercialModelBuilder.makeExpressionPurchaseContract("Purchase A", entity, "5");
		purchaseContract.setMinQuantity(120_000);
		purchaseContract.setMaxQuantity(180_000);
		purchaseContract.setVolumeLimitsUnit(VolumeUnits.M3);
		purchaseContract.setPreferredPort(darwin);

		final SalesContract salesContract = commercialModelBuilder.makeExpressionSalesContract("Sales A", entity, "8");
		salesContract.setPreferredPort(himeji);
		salesContract.setMinQuantity(120_000);
		salesContract.setMaxQuantity(180_000);
		salesContract.setVolumeLimitsUnit(VolumeUnits.M3);

		final SalesContract thirdPartySalesContract = commercialModelBuilder.makeExpressionSalesContract("Third party contract", thirdPartyEntity, "8");
		thirdPartySalesContract.setPreferredPort(darwin);
		thirdPartySalesContract.setMinQuantity(120_000);
		thirdPartySalesContract.setMaxQuantity(180_000);
		thirdPartySalesContract.setVolumeLimitsUnit(VolumeUnits.M3);
		thirdPartySalesContract.setContractType(ContractType.FOB);

		return defaultCharterInMarket;
	}

	@NonNull
	private TaxRate createDefaultTaxRate() {
		final TaxRate shippingTaxRate = CommercialFactory.eINSTANCE.createTaxRate();
		shippingTaxRate.setDate(LocalDate.of(2022, 1, 1));
		shippingTaxRate.setValue(0.05f);
		return shippingTaxRate;
	}

	protected OptimisationPlan createOptimisationPlan() {
		final OptimisationPlan optimisationPlan = super.createOptimisationPlan(createUserSettings(true));
		OptimisationEMFTestUtils.removeLSOAndHill(optimisationPlan);
		return optimisationPlan;
	}
}
