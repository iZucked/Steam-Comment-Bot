package com.mmxlabs.models.lng.transformer.extensions.euets;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.impl.EuEtsSeasonalityCurve;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.IEuEtsProviderEditor;

public class EuEtsTransformer implements ITransformerExtension {

	@Inject
	private IEuEtsProviderEditor euEtsProviderEditor;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;
	
	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser indices;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private IScenarioDataProvider scenarioDataProvider;

	private EuEtsModel euEtsModel;
	
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		euEtsModel = cargoModel.getEuEtsModel();
	}
	
	@Override
	public void finishTransforming() {
		euEtsProviderEditor.setEuPorts(getEuPorts());
		euEtsProviderEditor.setSeasonalityCurve(getEmissionsCurve());
		euEtsProviderEditor.setPriceCurve(getPricingCurve());
		euEtsProviderEditor.setFuelEmissionsRates(getFuelEmissions());
	}
	
	private Set<IPort> getEuPorts(){
		Set<Port> ePorts = SetUtils.getObjects(euEtsModel.getEuPortGroup());
		Set<IPort> oPorts = new HashSet<>();
		
		// Get optimiser ports
		for(final Port port : ePorts) {
			final IPort oPort = modelEntityMap.getOptimiserObject(port, IPort.class);
			oPorts.add(oPort);
		}
		
		return oPorts;
	}
	
	private EuEtsSeasonalityCurve getEmissionsCurve() {
		if(euEtsModel.getEmissionsCovered() == null)
			throw new IllegalStateException("Emissions table is empty");
		
		ZonedDateTime startTime = dateAndCurveHelper.getEarliestTime().plusDays(1); // Fixes having start event at 01/01/26 leading to earliest date of 31/12/25
		List<Pair<Integer, Integer>> curvePoints = new ArrayList<>();
		for(EmissionsCoveredTable tableData : euEtsModel.getEmissionsCovered()) {
			curvePoints.add(new Pair<>(dateAndCurveHelper.convertTime(startTime, YearMonth.of(tableData.getStartYear(), 1)), tableData.getEmissionsCovered()));
		}
		
		return new EuEtsSeasonalityCurve(curvePoints);
	}
	
	private IParameterisedCurve getPricingCurve() {
		final Pair<IParameterisedCurve, IIntegerIntervalCurve> curveData = dateAndCurveHelper.createCurveAndIntervals(indices, euEtsModel.getEuaPriceExpression(), dateAndCurveHelper::generateParameterisedExpressionCurve);
		
		if(curveData == null) {
			throw new IllegalStateException("Retrieved pricing curve is null");
		}
		
		return curveData.getFirst();
	}
	
	private Map<String, Double> getFuelEmissions(){
		Map<String, Double> fuelEmissionsMap = new HashMap<>();
		
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioDataProvider);
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioDataProvider);
		final CIIReferenceData ciiReferenceData = fleetModel.getCiiReferences();
		
		for(var emissionEntry : ciiReferenceData.getFuelEmissions()) {
			final String name = emissionEntry.getName();
			final double tonneOfCo2ePerTonneOfFuel = emissionEntry.getCf();
			// Get fuel names stored in (../../..)
			if(name != null && !name.isBlank() && emissionEntry.getCf() != 0.0) {
				List<String> fuelNames = extractFuelNames(name);
				
				for(String fuel : fuelNames) {
					fuelEmissionsMap.put(fuel, tonneOfCo2ePerTonneOfFuel);
				}
			} 
		}
		
		return fuelEmissionsMap;
	}
	
	private List<String> extractFuelNames(String inputString) {
        List<String> fuels = new ArrayList<>();

        // Define the pattern for extracting fuels within parentheses or without parentheses
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)|\\b([A-Z]+)\\b");

        // Match the pattern in the input string
        Matcher matcher = pattern.matcher(inputString);

        // Check if a match is found
        while (matcher.find()) {
            // Extract the contents within parentheses or standalone fuel
            String fuelsString = matcher.group(1);

            // If contents within parentheses are found, split them using '/'
            if (fuelsString != null) {
                String[] fuelArray = fuelsString.split("/");
                for (String fuel : fuelArray) {
                    fuels.add(fuel.trim());
                }
            } else {
                // If no contents within parentheses, add the standalone fuel
                fuels.add(matcher.group(2).trim());
            }
        }

        return fuels;
    }
}
