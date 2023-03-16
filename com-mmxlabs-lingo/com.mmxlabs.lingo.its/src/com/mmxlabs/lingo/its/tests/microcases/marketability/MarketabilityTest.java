package com.mmxlabs.lingo.its.tests.microcases.marketability;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.lingo.its.tests.microcases.MicroTestUtils;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityResult;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.marketability.MarketabilityUtils;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.transformer.its.RequireFeature;
import com.mmxlabs.models.lng.transformer.its.ShiroRunner;
import com.mmxlabs.models.lng.types.TimePeriod;

@ExtendWith(ShiroRunner.class)
@RequireFeature({ KnownFeatures.FEATURE_MARKETABLE_WINDOWS })

public class MarketabilityTest extends AbstractMarketablilityTest {
	private Vessel vTDFE160;

	private Port pKarratha;
	private Port pBonny;
	private Port pPecem;
	private Port pBarcelona;
	private Port pDragon;
	private Port pAltamira;

	@BeforeEach
	private void initialiseVesselsAndPorts() {
		vTDFE160 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);
		pKarratha = portFinder.findPortById(InternalDataConstants.PORT_KARRATHA);
		pBonny = portFinder.findPortById(InternalDataConstants.PORT_BONNY);
		pPecem = portFinder.findPortById(InternalDataConstants.PORT_PECEM);
		pBarcelona = portFinder.findPortById(InternalDataConstants.PORT_BARCELONA);
		pDragon = portFinder.findPortById(InternalDataConstants.PORT_DRAGON);
		pAltamira = portFinder.findPortById(InternalDataConstants.PORT_ALTAMIRA);
	}

	@SuppressWarnings("null")
	private void createDefaultScenario(@NonNull VesselCharter vesselCharter) {
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2023, 1, 3), pBonny, null, entity, "1")//
				.with(x -> x.setWindowSize(1)).with(x -> x.setWindowSizeUnits(TimePeriod.MONTHS)).build()//
				.makeDESSale("D1", LocalDate.of(2023, 1, 19), pBarcelona, null, entity, "1")//
				.with(x -> x.setWindowSize(10)).with(x -> x.setWindowSizeUnits(TimePeriod.DAYS)).build()//
				.withVesselAssignment(vesselCharter, 1)//
				.build();
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2023, 2, 18), pKarratha, null, entity, "1")//
				.with(x -> x.setWindowSize(1)).with(x -> x.setWindowSizeUnits(TimePeriod.MONTHS)).build()//
				.makeDESSale("D2", LocalDate.of(2023, 3, 10), pDragon, null, entity, "1")//
				.with(x -> x.setWindowSize(6)).with(x -> x.setWindowSizeUnits(TimePeriod.DAYS)).build()//
				.withVesselAssignment(vesselCharter, 1)//
				.build();
	}

	@SuppressWarnings("null")
	@Test
	public void testEarliestDateValid() {

		initialiseVesselsAndPorts();
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).withStartWindow(LocalDateTime.of(2013, 1, 1, 0, 0)).withEndWindow(LocalDateTime.of(2013, 1, 1, 0, 0))
				.build();
		final DESSalesMarket dsBrazil = spotMarketsModelBuilder.makeDESSaleMarket("DS-BRAZIL", pPecem, entity, "1").withEnabled(true).build();

		createDefaultScenario(vesselCharter);
		final @NonNull MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketablilityModel", null);

		evaluateMarketabilityModel(model);

		Assertions.assertNotNull(model);
		LoadSlot l1 = cargoModelFinder.findLoadSlot("L1");
		LoadSlot l2 = cargoModelFinder.findLoadSlot("L2");
		DischargeSlot d1 = cargoModelFinder.findDischargeSlot("D1");
		DischargeSlot d2 = cargoModelFinder.findDischargeSlot("D2");
		Optional<@NonNull MarketabilityRow> resultRow = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();

		Assertions.assertTrue(resultRow.isPresent());

		MarketabilityRow row = resultRow.orElseThrow();

		Assertions.assertEquals(1, row.getResult().getRhsResults().size());

		MarketabilityResult result = row.getResult().getRhsResults().get(0);
		Assertions.assertNotNull(result.getEarliestETA());
		Assertions.assertNotNull(result.getLatestETA());

		LocalDateTime earliest = result.getEarliestETA().toLocalDateTime();
		LocalDateTime latest = result.getLatestETA().toLocalDateTime();

		Assertions.assertTrue(earliest.isBefore(latest));

		LocalDateTime marketTime = earliest;
		l1.setWindowStart(getArrivalDate(model, l1).toLocalDate());
		l1.setWindowStartTime(getArrivalDate(model, l1).toLocalTime().getHour());
		l1.setWindowSize(0);
		d1.setWindowStart(marketTime.toLocalDate());
		d1.setWindowStartTime(marketTime.toLocalTime().getHour());
		d1.setWindowSize(0);
		d1.setPort(dsBrazil.getNotionalPort());
		l2.setWindowStart(getArrivalDate(model, l2).toLocalDate());
		l2.setWindowStartTime(getArrivalDate(model, l2).toLocalTime().getHour());
		l2.setWindowSize(0);
		d2.setWindowStart(getArrivalDate(model, d2).toLocalDate());
		d2.setWindowStartTime(getArrivalDate(model, d2).toLocalTime().getHour());
		d2.setWindowSize(0);
		evaluateTest();
		SlotVisit visit = MicroTestUtils.findSlotVisit(l2, lngScenarioModel);

		final PortVisitLateness lateness = visit.getLateness();
		Assertions.assertNull(lateness);

	}

	@Test
	public void testLatestDateValid() {
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).build();
		vesselCharter.setStartBy(LocalDateTime.of(2013, 1, 1, 0, 0));
		vesselCharter.setStartAfter(LocalDateTime.of(2013, 1, 1, 0, 0));
		final DESSalesMarket dsBrazil = spotMarketsModelBuilder.makeDESSaleMarket("DS-BRAZIL", pPecem, entity, "1").withEnabled(true).build();

		createDefaultScenario(vesselCharter);

		@SuppressWarnings("null")
		final @NonNull MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketablilityModel", null);

		evaluateMarketabilityModel(model);

		Assertions.assertNotNull(model);
		LoadSlot l1 = cargoModelFinder.findLoadSlot("L1");
		LoadSlot l2 = cargoModelFinder.findLoadSlot("L2");
		DischargeSlot d1 = cargoModelFinder.findDischargeSlot("D1");
		DischargeSlot d2 = cargoModelFinder.findDischargeSlot("D2");
		Optional<@NonNull MarketabilityRow> resultRow = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();

		Assertions.assertTrue(resultRow.isPresent());

		MarketabilityRow row = resultRow.orElseThrow();

		Assertions.assertEquals(1, row.getResult().getRhsResults().size());

		MarketabilityResult result = row.getResult().getRhsResults().get(0);
		Assertions.assertNotNull(result.getEarliestETA());
		Assertions.assertNotNull(result.getLatestETA());

		LocalDateTime earliest = result.getEarliestETA().toLocalDateTime();
		LocalDateTime latest = result.getLatestETA().toLocalDateTime();

		Assertions.assertTrue(earliest.isBefore(latest));

		LocalDateTime marketTime = latest;
		l1.setWindowStart(getArrivalDate(model, l1).toLocalDate());
		l1.setWindowStartTime(getArrivalDate(model, l1).toLocalTime().getHour());
		l1.setWindowSize(0);
		d1.setWindowStart(marketTime.toLocalDate());
		d1.setWindowStartTime(marketTime.toLocalTime().getHour());
		d1.setWindowSize(0);
		d1.setPort(dsBrazil.getNotionalPort());
		l2.setWindowStart(getArrivalDate(model, l2).toLocalDate());
		l2.setWindowStartTime(getArrivalDate(model, l2).toLocalTime().getHour());
		l2.setWindowSize(0);
		d2.setWindowStart(getArrivalDate(model, d2).toLocalDate());
		d2.setWindowStartTime(getArrivalDate(model, d2).toLocalTime().getHour());
		d2.setWindowSize(0);
		evaluateTest();
		SlotVisit visit = MicroTestUtils.findSlotVisit(l2, lngScenarioModel);

		final PortVisitLateness lateness = visit.getLateness();
		Assertions.assertNull(lateness);

	}

	@Test
	public void testInvalidBeforeEarliestDate() {
		initialiseVesselsAndPorts();
		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).build();
		vesselCharter.setStartBy(LocalDateTime.of(2013, 1, 1, 0, 0));
		vesselCharter.setStartAfter(LocalDateTime.of(2013, 1, 1, 0, 0));
		final DESSalesMarket dsBrazil = spotMarketsModelBuilder.makeDESSaleMarket("DS-BRAZIL", pPecem, entity, "1").withEnabled(true).build();

		createDefaultScenario(vesselCharter);

		@SuppressWarnings("null")
		final @NonNull MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketablilityModel", null);

		evaluateMarketabilityModel(model);

		Assertions.assertNotNull(model);
		LoadSlot l1 = cargoModelFinder.findLoadSlot("L1");
		LoadSlot l2 = cargoModelFinder.findLoadSlot("L2");
		DischargeSlot d1 = cargoModelFinder.findDischargeSlot("D1");
		DischargeSlot d2 = cargoModelFinder.findDischargeSlot("D2");
		Optional<@NonNull MarketabilityRow> resultRow = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();

		Assertions.assertTrue(resultRow.isPresent());

		MarketabilityRow row = resultRow.orElseThrow();

		Assertions.assertEquals(1, row.getResult().getRhsResults().size());

		MarketabilityResult result = row.getResult().getRhsResults().get(0);
		Assertions.assertNotNull(result.getEarliestETA());
		Assertions.assertNotNull(result.getLatestETA());

		LocalDateTime earliest = result.getEarliestETA().toLocalDateTime();
		LocalDateTime latest = result.getLatestETA().toLocalDateTime();

		Assertions.assertTrue(earliest.isBefore(latest));

		LocalDateTime marketTime = earliest.minusHours(1);
		l1.setWindowStart(getArrivalDate(model, l1).toLocalDate());
		l1.setWindowStartTime(getArrivalDate(model, l1).toLocalTime().getHour());
		l1.setWindowSize(0);
		d1.setWindowStart(marketTime.toLocalDate());
		d1.setWindowStartTime(marketTime.toLocalTime().getHour());
		d1.setWindowSize(0);
		d1.setPort(dsBrazil.getNotionalPort());
		l2.setWindowStart(getArrivalDate(model, l2).toLocalDate());
		l2.setWindowStartTime(getArrivalDate(model, l2).toLocalTime().getHour());
		l2.setWindowSize(0);
		d2.setWindowStart(getArrivalDate(model, d2).toLocalDate());
		d2.setWindowStartTime(getArrivalDate(model, d2).toLocalTime().getHour());
		d2.setWindowSize(0);
		evaluateTest();
		SlotVisit visit = MicroTestUtils.findSlotVisit(d1, lngScenarioModel);

		final PortVisitLateness lateness = visit.getLateness();
		Assertions.assertNotNull(lateness);
		Assertions.assertEquals(1, lateness.getLatenessInHours());
	}

	@Test
	public void testInvalidAfterLatestDate() {

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).build();
		vesselCharter.setStartBy(LocalDateTime.of(2013, 1, 1, 0, 0));
		vesselCharter.setStartAfter(LocalDateTime.of(2013, 1, 1, 0, 0));
		final DESSalesMarket dsBrazil = spotMarketsModelBuilder.makeDESSaleMarket("DS-BRAZIL", pPecem, entity, "1").withEnabled(true).build();

		createDefaultScenario(vesselCharter);

		@SuppressWarnings("null")
		final @NonNull MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketablilityModel", null);

		evaluateMarketabilityModel(model);

		Assertions.assertNotNull(model);
		LoadSlot l1 = cargoModelFinder.findLoadSlot("L1");
		LoadSlot l2 = cargoModelFinder.findLoadSlot("L2");
		DischargeSlot d1 = cargoModelFinder.findDischargeSlot("D1");
		DischargeSlot d2 = cargoModelFinder.findDischargeSlot("D2");
		Optional<@NonNull MarketabilityRow> resultRow = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();

		Assertions.assertTrue(resultRow.isPresent());

		MarketabilityRow row = resultRow.orElseThrow();

		Assertions.assertEquals(1, row.getResult().getRhsResults().size());

		MarketabilityResult result = row.getResult().getRhsResults().get(0);
		Assertions.assertNotNull(result.getEarliestETA());
		Assertions.assertNotNull(result.getLatestETA());

		LocalDateTime earliest = result.getEarliestETA().toLocalDateTime();
		LocalDateTime latest = result.getLatestETA().toLocalDateTime();

		Assertions.assertTrue(earliest.isBefore(latest));

		LocalDateTime marketTime = latest.plusHours(1);
		l1.setWindowStart(getArrivalDate(model, l1).toLocalDate());
		l1.setWindowStartTime(getArrivalDate(model, l1).toLocalTime().getHour());
		l1.setWindowSize(0);
		d1.setWindowStart(marketTime.toLocalDate());
		d1.setWindowStartTime(marketTime.toLocalTime().getHour());
		d1.setWindowSize(0);
		d1.setPort(dsBrazil.getNotionalPort());
		l2.setWindowStart(getArrivalDate(model, l2).toLocalDate());
		l2.setWindowStartTime(getArrivalDate(model, l2).toLocalTime().getHour());
		l2.setWindowSize(0);
		d2.setWindowStart(getArrivalDate(model, d2).toLocalDate());
		d2.setWindowStartTime(getArrivalDate(model, d2).toLocalTime().getHour());
		d2.setWindowSize(0);
		evaluateTest();
		SlotVisit visit = MicroTestUtils.findSlotVisit(l2, lngScenarioModel);

		final PortVisitLateness lateness = visit.getLateness();
		Assertions.assertNotNull(lateness);
		Assertions.assertEquals(1, lateness.getLatenessInHours());
	}

	@Test
	public void testForNoMarketableWindow() {
		initialiseVesselsAndPorts();

		final VesselCharter vesselCharter = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).build();
		vesselCharter.setStartBy(LocalDateTime.of(2013, 1, 1, 0, 0));
		vesselCharter.setStartAfter(LocalDateTime.of(2013, 1, 1, 0, 0));

		spotMarketsModelBuilder.makeDESSaleMarket("DES MEXICO", pAltamira, entity, "1").build();
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2023, 1, 3), pBonny, null, entity, "1")//
				.build()//
				.makeDESSale("D1", LocalDate.of(2023, 1, 19), pBarcelona, null, entity, "1")//
				.build()//
				.withVesselAssignment(vesselCharter, 1)//
				.build();
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2023, 2, 9), pKarratha, null, entity, "1")//
				.build()//
				.makeDESSale("D2", LocalDate.of(2023, 3, 15), pDragon, null, entity, "1")//
				.build()//
				.withVesselAssignment(vesselCharter, 1)//
				.build();
		Slot<?> l1 = cargoModelFinder.findSlot("L1");
		l1.setWindowSize(1);
		l1.setWindowSizeUnits(TimePeriod.MONTHS);
		Slot<?> d1 = cargoModelFinder.findDischargeSlot("D1");
		d1.setWindowSize(20);
		d1.setWindowSizeUnits(TimePeriod.DAYS);

		Slot<?> l2 = cargoModelFinder.findLoadSlot("L2");
		l2.setWindowSize(1);
		l2.setWindowSizeUnits(TimePeriod.DAYS);

		Slot<?> d2 = cargoModelFinder.findSlot("D2");
		d2.setWindowSize(3);
		d2.setWindowSizeUnits(TimePeriod.DAYS);

		MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketabilityModel", null);

		evaluateMarketabilityModel(model);

		Assertions.assertNotNull(model);
		Optional<@NonNull MarketabilityRow> resultRow = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();

		Assertions.assertTrue(resultRow.isPresent());

		MarketabilityRow row = resultRow.get();
		row.getResult().getRhsResults().stream().forEach(result -> {
			Assertions.assertNull(result.getEarliestETA());
			Assertions.assertNull(result.getLatestETA());
		});
	}
	
	@Test
	public void testMarketabilityModel_cargoDoesNotTakeOtherPanamaBookings() {
		final Vessel vTDFE155 = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_155);
		final Port pSabinePass = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		final Port pTokyo =  portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);
		final VesselCharter v1 = cargoModelBuilder.makeVesselCharter(vTDFE160, entity).build();
		final VesselCharter v2 = cargoModelBuilder.makeVesselCharter(vTDFE155, entity).build();
		//final int vesselSpeed = 15;

		spotMarketsModelBuilder.makeDESSaleMarket("DES JAPAN", pTokyo, entity, "1").build();
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L1", LocalDate.of(2023, 1, 1), pSabinePass, null, entity, "1")//
				.withWindowSize(1, TimePeriod.DAYS)//
				.build()//
				.makeDESSale("D1", LocalDate.of(2023, 2, 1), pTokyo, null, entity, "1")//
				.withWindowSize(1, TimePeriod.DAYS)//
				.build()//
				.withVesselAssignment(v1, 1)//
				.build();
		cargoModelBuilder.makeCargo()//
				.makeFOBPurchase("L2", LocalDate.of(2023, 1, 1), pSabinePass, null, entity, "1")//
				.withWindowSize(1, TimePeriod.DAYS)//
				.build()//
				.makeDESSale("D2", LocalDate.of(2023, 2, 1), pDragon, null, entity, "1")//
				.withWindowSize(1, TimePeriod.DAYS)//
				.build()//
				.withVesselAssignment(v2, 1)//
				.build();
		
		final CanalBookings canalBookings =  CargoFactory.eINSTANCE.createCanalBookings();
		final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
		final VesselGroupCanalParameters vgcp = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
		psr.setNorthboundWaitingDays(10);
		psr.setSouthboundWaitingDays(10);
		psr.setVesselGroupCanalParameter(vgcp);
		final CanalBookingSlot cbs = CargoFactory.eINSTANCE.createCanalBookingSlot();
		cbs.setBookingDate(LocalDate.of(2023, 1, 13));
		cbs.setCanalEntrance(CanalEntry.NORTHSIDE);
		cbs.setBookingCode(vgcp);
		vgcp.setName("default");
		
		canalBookings.getCanalBookingSlots().add(cbs);
		canalBookings.getPanamaSeasonalityRecords().add(psr);
		canalBookings.getVesselGroupCanalParameters().add(vgcp);
		cargoModelFinder.getCargoModel().setCanalBookings(canalBookings);
		
		MarketabilityModel model = MarketabilityUtils.createModelFromScenario(lngScenarioModel, "marketabilityModel", null);

		evaluateMarketabilityModel(model);
		Assertions.assertNotNull(model);
		Slot<?> l1 = cargoModelFinder.findSlot("L1");
		Slot<?> l2 = cargoModelFinder.findSlot("L2");		
		Optional<@NonNull MarketabilityRow> oRow1 = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l1;
			} else {
				return false;
			}
		}).findFirst();
		
		Assertions.assertTrue(oRow1.isPresent());
		Optional<@NonNull MarketabilityRow> oRow2 = model.getRows().stream().filter(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				return br.getSlot() == l2;
			} else {
				return false;
			}
		}).findFirst();
		Assertions.assertTrue(oRow2.isPresent());

		MarketabilityRow row1 = oRow1.get();
		MarketabilityRow row2 = oRow2.get();
		
		LocalDateTime earliest1 = row1.getResult().getRhsResults().get(0).getEarliestETA().toLocalDateTime();
		LocalDateTime earliest2 = row2.getResult().getRhsResults().get(0).getEarliestETA().toLocalDateTime();
		Assertions.assertTrue(earliest2.isAfter(earliest1));
	}

	private LocalDateTime getArrivalDate(MarketabilityModel model, Slot<?> slot) {
		@SuppressWarnings("null")
		Optional<LocalDateTime> date = model.getRows().stream().map(x -> {
			if (x.getBuyOption() instanceof BuyReference br) {
				LocalDateTime buyDate = x.getResult().getBuyDate();
				if (br.getSlot() == slot) {
					return buyDate;
				}
			}
			if (x.getSellOption() instanceof SellReference sr) {
				LocalDateTime sellDate = x.getResult().getSellDate();
				if (sr.getSlot() == slot) {
					return sellDate;
				}
			}
			return null;

		}).filter(Objects::nonNull).findAny();
		Assertions.assertTrue(date.isPresent(), "Slot is not in marketability model");
		return date.get();
	}

}
