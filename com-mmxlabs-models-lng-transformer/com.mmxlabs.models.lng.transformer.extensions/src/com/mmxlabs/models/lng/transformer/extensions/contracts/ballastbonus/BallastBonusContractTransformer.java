/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NextPortType;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IBallastBonusContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContractRule;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.DefaultLumpSumBallastBonusContractRule;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.DefaultNotionalJourneyBallastBonusContractRule;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.MonthlyBallastBonusContractRule;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;

/**
 * 
 * 
 * @author alex
 * 
 */
public class BallastBonusContractTransformer implements IBallastBonusContractTransformer {

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getExpressionPriceParameters(), CommercialPackage.eINSTANCE.getDateShiftExpressionPriceParameters(),
			CommercialPackage.eINSTANCE.getSalesContract(), CommercialPackage.eINSTANCE.getPurchaseContract());

	@Inject
	private Injector injector;

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	@Named(SchedulerConstants.Parser_BaseFuel)
	private SeriesParser fuelIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Charter)
	private SeriesParser charterIndices;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	@Inject
	private ModelEntityMap modelEntityMap;

	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
	}

	@Override
	public void finishTransforming() {
	}

	@Override
	public IBallastBonusContract createBallastBonusContract(BallastBonusContract eBallastBonusContract) {
		if (eBallastBonusContract == null) {
			return null;
		}
		@Nullable
		IBallastBonusContract oBallastBonusContract = modelEntityMap.getOptimiserObject(eBallastBonusContract, IBallastBonusContract.class);
		if (oBallastBonusContract != null) {
			return oBallastBonusContract;
		}
		
		EList<APortSet<Port>> hubs = null;
		if (eBallastBonusContract instanceof MonthlyBallastBonusContract) {
			MonthlyBallastBonusContract monthlyBallastBonusContract = (MonthlyBallastBonusContract)eBallastBonusContract;
			hubs = monthlyBallastBonusContract.getHubs();
		}
		
		if (eBallastBonusContract instanceof RuleBasedBallastBonusContract) {
			EList<BallastBonusContractLine> rules = ((RuleBasedBallastBonusContract ) eBallastBonusContract).getRules();
			List<IBallastBonusContractRule> oRules = new LinkedList<>();
			for (BallastBonusContractLine ballastBonusContractLine : rules) {
				Set<IPort> redeliveryPorts = transformPorts(ballastBonusContractLine.getRedeliveryPorts());
				IBallastBonusContractRule oRule = null;
				if (ballastBonusContractLine instanceof LumpSumBallastBonusContractLine) {
					String priceExpression = ((LumpSumBallastBonusContractLine) ballastBonusContractLine).getPriceExpression();
					ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
					oRule = new DefaultLumpSumBallastBonusContractRule(redeliveryPorts, lumpSumCurve);
				} else if (ballastBonusContractLine instanceof MonthlyBallastBonusContractLine) {
					MonthlyBallastBonusContractLine monthlyBBLine = (MonthlyBallastBonusContractLine)ballastBonusContractLine;
					YearMonth ym = monthlyBBLine.getMonth();
					int oStartYMInclusive = this.dateHelper.convertTime(ym);
					YearMonth ymNext = ym.plusMonths(1);
					int oEndYMExclusive = this.dateHelper.convertTime(ymNext);
					
					NextPortType ballastBonusTo = monthlyBBLine.getBallastBonusTo();
					//Use BigDecimal to avoid loss of precision :-)
					BigDecimal pctCharterRate = new BigDecimal(monthlyBBLine.getBallastBonusPctCharter());
					BigDecimal pctFuelRate = new BigDecimal(monthlyBBLine.getBallastBonusPctFuel());
					long oPctCharterRate = OptimiserUnitConvertor.convertToInternalPercentage(pctCharterRate) / 100;
					long oPctFuelRate = OptimiserUnitConvertor.convertToInternalPercentage(pctFuelRate) / 100;
					String priceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getLumpSumPriceExpression();
					ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
					String fuelPriceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getFuelPriceExpression();
					ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
					String charterPriceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getHirePriceExpression();
					ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
					//Set<IPort> returnPorts = transformPorts(((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getReturnPorts());
					int speed = OptimiserUnitConvertor.convertToInternalSpeed(((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getSpeed());
					boolean includeCanalTime = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).isIncludeCanalTime();	
					
					Set<IPort> returnPorts = null;
					
					switch (ballastBonusTo) {
					case NEAREST_HUB:
						returnPorts = transformPorts(hubs);
						break;
					case LOAD_PORT:
						returnPorts = null;
						break;
					}
					
					oRule = new MonthlyBallastBonusContractRule(oStartYMInclusive, oEndYMExclusive, oPctCharterRate, oPctFuelRate, redeliveryPorts, lumpSumCurve, fuelCurve, charterCurve, returnPorts, ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).isIncludeCanal(), includeCanalTime, speed);
				}
				else if (ballastBonusContractLine instanceof NotionalJourneyBallastBonusContractLine) {
					String priceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getLumpSumPriceExpression();
					ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
					String fuelPriceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getFuelPriceExpression();
					ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
					String charterPriceExpression = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getHirePriceExpression();
					ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
					Set<IPort> returnPorts = transformPorts(((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getReturnPorts());
					int speed = OptimiserUnitConvertor.convertToInternalSpeed(((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).getSpeed());
					boolean includeCanalTime = ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).isIncludeCanalTime();
					oRule = new DefaultNotionalJourneyBallastBonusContractRule(redeliveryPorts, lumpSumCurve, fuelCurve, charterCurve, returnPorts, ((NotionalJourneyBallastBonusContractLine) ballastBonusContractLine).isIncludeCanal(), includeCanalTime, speed);
				} 
				else {
					throw new IllegalArgumentException("Not implemented yet. Please contact Minimax support.");
				}
				assert oRule != null;
				injector.injectMembers(oRule);
				oRules.add(oRule);
			}
			if (eBallastBonusContract instanceof MonthlyBallastBonusContract) {
				oBallastBonusContract = new com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.MonthlyBallastBonusContract(oRules);
			}	
			else {
				oBallastBonusContract = new com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.DefaultBallastBonusContract(oRules);
			}
			modelEntityMap.addModelObject(eBallastBonusContract, oBallastBonusContract);
			return oBallastBonusContract;
		} else {
			return null;
		}
	}
	
	private @NonNull Set<IPort> transformPorts(Collection<APortSet<Port>> redeliveryPorts) {
		Set<IPort> ports = new LinkedHashSet<>();
		for (Port ePort : (SetUtils.getObjects(redeliveryPorts))) {
			@NonNull
			IPort oPort = modelEntityMap.getOptimiserObjectNullChecked(ePort, IPort.class);
			ports.add(oPort);
		}
		return ports;
	}

	private final ICurve getBaseFuelPriceCurveFromExpression(final String expression, SeriesParser indices) {
		ICurve result = dateHelper.generateExpressionCurve(expression, indices);
		return result;
	}

	private final ILongCurve getPriceCurveFromExpression(final String expression, SeriesParser indices) {
		ILongCurve result = dateHelper.generateLongExpressionCurve(expression, indices);
		return result;
	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}
}