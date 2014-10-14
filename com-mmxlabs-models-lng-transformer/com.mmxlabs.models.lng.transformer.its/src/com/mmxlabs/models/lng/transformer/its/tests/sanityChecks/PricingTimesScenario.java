package com.mmxlabs.models.lng.transformer.its.tests.sanityChecks;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformer;
import com.mmxlabs.models.lng.transformer.its.tests.CustomScenarioCreator;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;
import com.mmxlabs.models.lng.transformer.its.tests.TransformerExtensionTestModule;
import com.mmxlabs.models.lng.transformer.its.tests.calculation.ScenarioTools;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AbstractPairwiseConstraintChecker;

/**
 * Creates a simple scenario for testing changes in price indexes and price dating
 * 
 * @author Alex Churchill
 */
public class PricingTimesScenario {
	private static final int cscDischargePrice = 1;

	public Port smallLoadPort;
	public Port bigLoadPort;
	public Port smallDischargePort;
	public Port bigDischargePort;

	public Cargo indexedCargoA;
	public Cargo indexedCargoB;
	public Cargo notIndexedCargo;

	public String dischargePriceIndexedA = "datedA";
	public String dischargePriceIndexedB = "datedB";
	public String dischargePriceNotIndexed = "5";

	public LNGScenarioModel scenario;
	public CustomScenarioCreator csc;

	public PricingTimesScenario() {
		csc = new CustomScenarioCreator(cscDischargePrice);

		final int fuelPrice = 1;

		smallLoadPort = ScenarioTools.createPort("smallLoadPort");
		bigLoadPort = ScenarioTools.createPort("bigLoadPort");
		smallDischargePort = ScenarioTools.createPort("smallDischargePort");
		bigDischargePort = ScenarioTools.createPort("bigDischargePort");

		// a list of ports to use in the scenario
		final Port[] ports = new Port[] { smallLoadPort, bigLoadPort, smallDischargePort, bigDischargePort };

		// Add the ports, and set the distances.
		SanityCheckTools.setPortDistances(csc, ports);

		// create a few vessels and add them to the list of vessels created.
		final int numOfClassOne = 3;

		// createVessels creates and adds the vessels to the scenario.
		csc.addVesselSimple("classOne", numOfClassOne, fuelPrice, 25, 1000000, 10, 10, 0, 500, false);
	}

	void setScenario() {
		scenario = csc.buildScenario();
	}

	void addTestCommodityIndexes() {
		// add a commodity index to the pricing model and return it
		CommodityIndex ci1 = csc.addCommodityIndex(dischargePriceIndexedA);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 1), 4.0);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 13), 4.5);
		csc.addDataToCommodity(ci1, createDate(2014, 0, 31), 7.0);
		csc.addDataToCommodity(ci1, createDate(2014, 1, 2), 7.5);
		csc.addDataToCommodity(ci1, createDate(2014, 4, 1), 8.0);

		CommodityIndex ci2 = csc.addCommodityIndex(dischargePriceIndexedB);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 1), 6.0);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 13), 6.5);
		csc.addDataToCommodity(ci2, createDate(2014, 0, 31), 8.0);
		csc.addDataToCommodity(ci2, createDate(2014, 1, 2), 8.5);
		csc.addDataToCommodity(ci2, createDate(2014, 4, 1), 10.0);
	}

	void addCargo(Date cargoStart) {
		// create three identical cargoes with different price exps
		indexedCargoA = csc.addCargo("indexed-cargo-a", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedA, 22, cargoStart, 50);
		indexedCargoB = csc.addCargo("indexed-cargo-b", bigLoadPort, bigDischargePort, "1", dischargePriceIndexedB, 22, cargoStart, 50);
		notIndexedCargo = csc.addCargo("not-indexed-cargo", bigLoadPort, bigDischargePort, "1", dischargePriceNotIndexed, 22, cargoStart, 50);
	}

	void setPriceDate(Date cargoStart, Date priceDate) {
		indexedCargoA.getSlots().get(1).setPricingDate(priceDate);
		indexedCargoB.getSlots().get(1).setPricingDate(priceDate);
		notIndexedCargo.getSlots().get(1).setPricingDate(priceDate);
	}

	public void testSalesPrice(double priceA, double priceB, double priceC) {
		ScenarioRunner runner = new ScenarioRunner((LNGScenarioModel) this.scenario);
		try {
			runner.init();
			runner.updateScenario();
		} catch (IncompleteScenarioException e) {
			Assert.assertTrue("Scenario runner failed to initialise simple scenario.", false);
		}
		final Schedule schedule = runner.getIntialSchedule();
		// Workaround, would prefer to use ca.getInputCargo().getName() but hookup runner.updateScenario() not working
		String errorMsg = "Cargo %s has incorrect pricing %2f != %2f";
		for (int i = 0; i < schedule.getCargoAllocations().size(); i++) {
			CargoAllocation ca = schedule.getCargoAllocations().get(i);
			if (i == 0) {
				System.out.println("Discharge start:" + ca.getSlotAllocations().get(1).getLocalStart().getTime());
				System.out.println("Discharge end:" + ca.getSlotAllocations().get(1).getLocalEnd().getTime());
			}
			double salePrice = ca.getSlotAllocations().get(1).getPrice();
			System.out.println("Sale price: " + salePrice);
			switch (i) {
			case 0:
				System.out.println("Expected price:" + priceA);
				Assert.assertTrue(String.format(errorMsg, "A", salePrice, priceA), salePrice == priceA);
				break;
			case 1:
				Assert.assertTrue(String.format(errorMsg, "B", salePrice, priceB), salePrice == priceB);
				System.out.println("Expected price:" + priceB);
				break;
			case 2:
				Assert.assertTrue(String.format(errorMsg, "C", salePrice, priceC), salePrice == priceC);
				System.out.println("Expected price:" + priceC);
				break;
			default:
				Assert.assertTrue("Too many cargoes", false);
				break;
			}
		}
	}

	static Date createDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

}