/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests.microcases;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.lingo.its.tests.category.TestCategories;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.lngdataserver.lng.importers.creator.ScenarioBuilder;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.util.PortModelBuilder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintChecker;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioToOptimiserBridge;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@ExtendWith(ShiroRunner.class)
public class CargoShippingTypeRequirementConstraintTest extends AbstractMicroTestCase {

	@Override
	public @NonNull IScenarioDataProvider importReferenceData() throws Exception {
		ScenarioBuilder sb = ScenarioBuilder.initialiseBasicScenario();
		sb.loadDefaultData();
		return sb.getScenarioDataProvider();
	}

	@Override
	protected BaseLegalEntity importDefaultEntity() {
		return commercialModelFinder.findEntity(ScenarioBuilder.DEFAULT_ENTITY_NAME);
	}
	
	private static Stream<Arguments> provideCargoDeliveryTypeCombinations() {
		return Stream.of(
				Arguments.of(CargoDeliveryType.ANY, CargoDeliveryType.ANY),
				Arguments.of(CargoDeliveryType.ANY, CargoDeliveryType.NOT_SHIPPED),
				Arguments.of(CargoDeliveryType.ANY, CargoDeliveryType.SHIPPED),
				Arguments.of(CargoDeliveryType.NOT_SHIPPED, CargoDeliveryType.ANY),
				Arguments.of(CargoDeliveryType.NOT_SHIPPED, CargoDeliveryType.NOT_SHIPPED),
				Arguments.of(CargoDeliveryType.NOT_SHIPPED, CargoDeliveryType.SHIPPED),
				Arguments.of(CargoDeliveryType.SHIPPED, CargoDeliveryType.ANY),
				Arguments.of(CargoDeliveryType.SHIPPED, CargoDeliveryType.NOT_SHIPPED),
				Arguments.of(CargoDeliveryType.SHIPPED, CargoDeliveryType.SHIPPED));
	}
	
	@ParameterizedTest
	@MethodSource("provideCargoDeliveryTypeCombinations")
	@Tag(TestCategories.MICRO_TEST)
	@Tag(TestCategories.REGRESSION_TEST)
	public void testNotShippedFOBFOBCargo(CargoDeliveryType sdt, CargoDeliveryType pdt) throws Exception {
		setupFOBFOBCargoPair(sdt, pdt);
		//FOB-FOB is by definition not shipped, so if either sdt or pdt is set to shipped, we expect the constraint checker to fail.
		validateConstraintCheckers(!(sdt.equals(CargoDeliveryType.SHIPPED) || pdt.equals(CargoDeliveryType.SHIPPED)));
	}

	//For DES purchase, does not make sense to set any restriction on sales delivery type, therefore the constraint checker should
	//always pass.
	@ParameterizedTest
	@MethodSource("provideCargoDeliveryTypeCombinations")
	@Tag(TestCategories.MICRO_TEST)
	@Tag(TestCategories.REGRESSION_TEST)
	public void testNotShippedDESDESCargo(CargoDeliveryType sdt, CargoDeliveryType pdt) throws Exception {
		setupDESDESCargoPair(sdt, pdt);
		//FOB-FOB is by definition not shipped, so if either sdt or pdt is set to shipped, we expect the constraint checker to fail.
		validateConstraintCheckers(!(sdt.equals(CargoDeliveryType.SHIPPED) || pdt.equals(CargoDeliveryType.SHIPPED)));
	}

	@ParameterizedTest
	@MethodSource("provideCargoDeliveryTypeCombinations")
	@Tag(TestCategories.MICRO_TEST)
	@Tag(TestCategories.REGRESSION_TEST)
	public void testShippedFOBDESCargo(CargoDeliveryType sdt, CargoDeliveryType pdt) throws Exception {
		setupFOBDESCargoPair(sdt, pdt);
		//FOB-DES is by definition shipped, so if either sdt or pdt is set to not shipped, we expect the constraint checker to fail.
		validateConstraintCheckers(!(sdt.equals(CargoDeliveryType.NOT_SHIPPED) || pdt.equals(CargoDeliveryType.NOT_SHIPPED)));
	}
	
	private void setupFOBFOBCargoPair(CargoDeliveryType salesDeliveryType, CargoDeliveryType purchaseDeliveryType) {
		if (salesDeliveryType == null) {
			salesDeliveryType = CargoDeliveryType.ANY;
		}
		if (purchaseDeliveryType == null) {
			purchaseDeliveryType = CargoDeliveryType.ANY;
		}
		
		final FOBSalesMarket market = spotMarketsModelBuilder.makeFOBSaleMarket("FOBSaleMarket", 
				PortModelBuilder.makePortSet(portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)), entity, "5") //
				.withAvailabilityConstant(0)//
				.build();

		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withSalesDeliveryType(salesDeliveryType)
				.build() //
				.makeMarketFOBSale("D1", market, YearMonth.of(2015, 6), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN)) //
				.withPurchaseDeliveryType(purchaseDeliveryType)//
				.build() //
				.build();
	}

	private void setupFOBDESCargoPair(CargoDeliveryType salesDeliveryType, CargoDeliveryType purchaseDeliveryType) {
		if (salesDeliveryType == null) {
			salesDeliveryType = CargoDeliveryType.ANY;
		}
		if (purchaseDeliveryType == null) {
			purchaseDeliveryType = CargoDeliveryType.ANY;
		}
		
		final Vessel vessel = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final CharterInMarket charterInMarket1 = spotMarketsModelBuilder.createCharterInMarket("CharterIn 1", vessel, entity, "50000", 1);
			
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2015, 6, 1), portFinder.findPortById(InternalDataConstants.PORT_POINT_FORTIN), null, entity, "5") //
				.withSalesDeliveryType(salesDeliveryType)
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7") //
				.withPurchaseDeliveryType(purchaseDeliveryType)//
				.build() //
				.withVesselAssignment(charterInMarket1, 0, 1)
				.build();
	}
	
	private void setupDESDESCargoPair(CargoDeliveryType salesDeliveryType, CargoDeliveryType purchaseDeliveryType) {

		if (salesDeliveryType == null) {
			salesDeliveryType = CargoDeliveryType.ANY;
		}
		if (purchaseDeliveryType == null) {
			purchaseDeliveryType = CargoDeliveryType.ANY;
		}
		
		cargoModelBuilder.makeCargo()//
				.makeDESPurchase("L1", DESPurchaseDealType.DEST_ONLY, LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "?", 22.8, null)
				.withSalesDeliveryType(salesDeliveryType)
				.build() //
				.makeDESSale("D1", LocalDate.of(2015, 12, 1), portFinder.findPortById(InternalDataConstants.PORT_COVE_POINT), null, entity, "7")
				.withPurchaseDeliveryType(purchaseDeliveryType)//
				.build() //
				.build();
	}

	private void validateConstraintCheckers(boolean expectedConstraintCheckResult) {
		evaluateWithLSOTest(scenarioRunner -> {
			final LNGScenarioToOptimiserBridge scenarioToOptimiserBridge = scenarioRunner.getScenarioToOptimiserBridge();

			// Assert initial state can be evaluated
			final ISequences initialRawSequences = scenarioToOptimiserBridge.getDataTransformer().getInitialSequences();
			// Validate the initial sequences are invalid
			final List<IConstraintChecker> violatedConstraints = MicroTestUtils.validateConstraintCheckers(scenarioToOptimiserBridge.getDataTransformer(), initialRawSequences);
			if (!expectedConstraintCheckResult) {
				Assertions.assertNotNull(violatedConstraints);
				Assertions.assertTrue(violatedConstraints.size() == 1);
				Assertions.assertTrue(violatedConstraints.get(0) instanceof ShippingTypeRequirementConstraintChecker);
			}
			else {
				//All constraints passed.
				Assertions.assertNull(violatedConstraints);
			}
		});
	}
}