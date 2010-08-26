package com.mmxlabs.demo.app.wizards;

import java.util.Date;
import java.util.Random;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.cargo.CargoFactory;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;
import scenario.port.DistanceLine;
import scenario.port.Port;

/**
 * A class for constructing the EMF representations of random test scenarios
 * @author hinton
 *
 */
public class RandomScenarioUtils {
	FleetFactory ff = FleetPackage.eINSTANCE.getFleetFactory();
	private Random random;
	public RandomScenarioUtils(Random random) {
		this.random = random;
	}
	public RandomScenarioUtils() {
		this(new Random(1));
	}
	
	void addStandardFleet(Scenario scenario) {
		//generate the standard fleet
		VesselClass class1 = addVesselClass(scenario, "STEAM-138", 12, 20, 138000, 200);
		VesselClass class2 = addVesselClass(scenario, "STEAM-145", 12, 20, 145000, 200);
		VesselClass class3 = addVesselClass(scenario, "DFDE-177", 12, 20, 177000, 200);
		VesselClass class4 = addVesselClass(scenario, "STEAM-126", 12, 19.5f, 138000, 200); //TODO units in the model; should it be a float?
		
		float[][] steam = new float[][] {{12, 12}, {20, 20}};
		float[][] dfde = new float[][] {{12, 8}, {20, 16}};
		
		//create class parameters; currently model uses containment for curves, so we need to do duplicates
		class1.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 138/24.0f, 118/24.0f, 10/24.0f, steam));
		class1.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 138/24.0f, 118/24.0f, 10/24.0f, steam));
		
		
		
		class2.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 145/24.0f, 125/24.0f, 10/24.0f, steam));
		class2.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 145/24.0f, 125/24.0f, 10/24.0f, steam));
		
		class3.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 177/24.0f, 157/24.0f, 10/24.0f, dfde));
		class3.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 177/24.0f, 157/24.0f, 10/24.0f, dfde));
		
		class4.setLadenAttributes(createVesselStateAttributes(VesselState.LADEN, 126/24.0f, 106/24.0f, 10/24.0f, steam));
		class4.setBallastAttributes(createVesselStateAttributes(VesselState.BALLAST, 125/24.0f, 106/24.0f, 10/24.0f, steam));
		
		class1.setSpotCharterCount(3);
		class2.setSpotCharterCount(3);
		class3.setSpotCharterCount(3);
		class4.setSpotCharterCount(3);
		
		//create vessels in each class
		
		randomiseAvailability(scenario, addVessel(scenario, "Methane Rita Andrea", class1));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Jane Elizabeth", class2));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Lydon Volney", class2));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Shirley Elizabeth", class2));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Heather Sally", class2));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Alison Victoria", class2));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Nile Eagle", class2));
		
		randomiseAvailability(scenario, addVessel(scenario, "Methane Julia Louise", class3));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Becki Anne", class3));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Mickie Harper", class3));
		randomiseAvailability(scenario, addVessel(scenario, "Methane Unknown", class3));
		
		addVessel(scenario, "Hilli", class4);
		addVessel(scenario, "Gimi", class4);
		addVessel(scenario, "Khannur", class4);
		addVessel(scenario, "Golar Freeze", class4);
		addVessel(scenario, "Methane Princes", class1);
		
//		addVessel(scenario, "Extra-Charter-1", class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-2", class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-3", class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-4", class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-5", class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-6", class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-7", class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-8", class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-9", class1).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-10", class2).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-11", class3).setInstanceType(VesselInstanceType.SPOT_CHARTER);
//		addVessel(scenario, "Extra-Charter-12", class4).setInstanceType(VesselInstanceType.SPOT_CHARTER);
	}
	
	private void randomiseAvailability(Scenario scenario, Vessel addVessel) {
		//Add a random start constraint.
		PortAndTime start = ff.createPortAndTime();
		EList<Port> ports = scenario.getPortModel().getPorts();
		start.setPort(ports.get(random.nextInt(ports.size())));
	}
	
	private Vessel addVessel(Scenario scenario, String string, VesselClass class1) {
		Vessel v = ff.createVessel();
		
		v.setName(string);
		v.setClass(class1);
		
		scenario.getFleetModel().getFleet().add(v);
		
		return v;
	}
	
	private VesselStateAttributes createVesselStateAttributes(VesselState state,
			float nbo, float idlenbo, float idleconsumption, float[][] curve) {
		VesselStateAttributes vsa = ff.createVesselStateAttributes();
		
		vsa.setVesselState(state);
		vsa.setIdleConsumptionRate(idleconsumption);
		vsa.setIdleNBORate(nbo);
		vsa.setIdleNBORate(idlenbo);
		
		for (float[] point : curve) {
			FuelConsumptionLine line = ff.createFuelConsumptionLine();
			line.setConsumption(point[0]);
			line.setSpeed(point[1]);
			vsa.getFuelConsumptionCurve().add(line);
		}
		
		return vsa;
	}
	VesselClass addVesselClass(Scenario scenario, String name, int minSpeed, float f, long capacity, int baseFuelUnitPrice) {
		VesselClass vc = ff.createVesselClass();
		
		vc.setName(name);
		vc.setMinSpeed(minSpeed);
		vc.setMaxSpeed(f);
		vc.setCapacity(capacity);
		vc.setBaseFuelUnitPrice(baseFuelUnitPrice);
		vc.setDailyCharterPrice(40000);
		
		scenario.getFleetModel().getVesselClasses().add(vc);
		
		return vc;
	}
	
	void createRandomCargoes(Scenario scenario, int cargoCount) {
		//TODO this is not how it was before; load and discharge ports are more random; there will be less contention.
		CargoFactory cf = CargoPackage.eINSTANCE.getCargoFactory();
		EList<DistanceLine> lines = scenario.getDistanceModel().getDistances();
		
		long now = (new Date()).getTime();
		
		for (int i = 0; i<cargoCount; i++) {
			//create a random cargo
			Cargo c = cf.createCargo();
			Slot load = cf.createSlot();
			Slot discharge = cf.createSlot();
			
			//invalid distance lines shouldn't be in the model
			final DistanceLine dl = lines.get(random.nextInt(lines.size()));
			load.setPort(dl.getFromPort());
			discharge.setPort(dl.getToPort());
			
			load.setId("load-" + i);
			discharge.setId("discharge-"+i);
			
			final int minTime = (int) Math.ceil(dl.getDistance()/20/24);
			final int maxTime = (int) Math.ceil(dl.getDistance()/12/24);
			
			final int start = random.nextInt(365);
			final int end = 1+start+minTime + random.nextInt(maxTime - minTime + 15);
			
			Date startDate = new Date(now + start * Timer.ONE_DAY);
			Date endDate = new Date(now + end * Timer.ONE_DAY);
			
			load.setWindowStart(startDate);
			discharge.setWindowStart(endDate);
			
			load.setWindowDuration(1);
			discharge.setWindowDuration(1);

			discharge.setUnitPrice(170+random.nextInt(100));
			load.setUnitPrice(discharge.getUnitPrice() - 20);
			
			load.setMinQuantity(0);
			load.setMaxQuantity(200000);
			
			discharge.setMinQuantity(0);
			discharge.setMaxQuantity(200000);
			
			c.setId("cargo-" + i);
			c.setLoadSlot(load);
			c.setDischargeSlot(discharge);
			scenario.getCargoModel().getCargoes().add(c);
		}
	}
}
