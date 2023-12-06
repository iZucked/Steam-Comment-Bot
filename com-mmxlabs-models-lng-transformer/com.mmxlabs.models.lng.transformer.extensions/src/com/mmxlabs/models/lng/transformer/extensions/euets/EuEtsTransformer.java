package com.mmxlabs.models.lng.transformer.extensions.euets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
		
		List<Pair<Integer, Integer>> curvePoints = new ArrayList<>();
		for(EmissionsCoveredTable tableData : euEtsModel.getEmissionsCovered()) {
			curvePoints.add(new Pair<>(tableData.getStartYear(), tableData.getEmissionsCovered()));
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
}
