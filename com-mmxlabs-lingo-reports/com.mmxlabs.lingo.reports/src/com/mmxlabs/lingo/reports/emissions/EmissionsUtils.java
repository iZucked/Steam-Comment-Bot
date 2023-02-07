package com.mmxlabs.lingo.reports.emissions;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;

public class EmissionsUtils {
	public static double getBaseFuelEmissionRate(final @NonNull Vessel vessel) {
		return vessel.getVesselOrDelegateBaseFuelEmissionRate();
	}
	
	public static double getBOGEmissionRate(final @NonNull Vessel vessel) {
		return vessel.getVesselOrDelegateBogEmissionRate();
	}
	
	public static double getPilotLightEmissionRate(final @NonNull Vessel vessel) {
		return vessel.getVesselOrDelegatePilotLightEmissionRate();
	}
	
	public static long getBaseFuelEmission(final IVesselEmission model, List<FuelQuantity> fuelQuantity) {
		long result = 0L;
		for (final FuelQuantity fq : fuelQuantity) {
			if (fq.getFuel()==Fuel.BASE_FUEL) {
				final Optional<FuelAmount> optMtFuelAmount = fq.getAmounts().stream() //
						.filter(fa -> fa.getUnit() == FuelUnit.MT) //
						.findFirst();
				if (optMtFuelAmount.isPresent()) {
					result += (long) (optMtFuelAmount.get().getQuantity() * model.getBaseFuelEmissionRate());
				}
			}
		}
		return result;
	}
	
	public static long getBOGEmission(final IVesselEmission model, List<FuelQuantity> fuelQuantity) {
		long result = 0L;
		for (final FuelQuantity fq : fuelQuantity) {
			if (fq.getFuel()==Fuel.FBO || fq.getFuel() == Fuel.NBO) {
				final Optional<FuelAmount> optM3FuelAmount = fq.getAmounts().stream() //
						.filter(fa -> fa.getUnit() == FuelUnit.M3) //
						.findFirst();
				if (optM3FuelAmount.isPresent()) {
					result += (long) (optM3FuelAmount.get().getQuantity() * model.getBOGEmissionRate());
				}
			}
		}
		return result;
	}
	
	public static long getPilotLightEmission(final IVesselEmission model, List<FuelQuantity> fuelQuantity) {
		long result = 0L;
		for (final FuelQuantity fq : fuelQuantity) {
			if (fq.getFuel()==Fuel.FBO || fq.getFuel() == Fuel.NBO) {
				final Optional<FuelAmount> optMtFuelAmount = fq.getAmounts().stream() //
						.filter(fa -> fa.getUnit() == FuelUnit.MT) //
						.findFirst();
				if (optMtFuelAmount.isPresent()) {
					result += (long) (optMtFuelAmount.get().getQuantity() * model.getPilotLightEmissionRate());
				}
			}
		}
		return result;
	}
}
