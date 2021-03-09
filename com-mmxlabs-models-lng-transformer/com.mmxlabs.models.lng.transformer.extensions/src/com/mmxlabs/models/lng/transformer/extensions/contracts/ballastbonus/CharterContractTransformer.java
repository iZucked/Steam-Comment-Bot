/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NextPortType;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ICharterContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.DefaultCharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.MonthlyBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultLumpSumBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultLumpSumRepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultNotionalJourneyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultOriginPortRepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.MonthlyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;

/**
 * 
 * 
 * @author alex
 * 
 */
public class CharterContractTransformer implements ICharterContractTransformer {

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
	public ICharterContract createCharterContract(GenericCharterContract eCharterContract) {
		if (eCharterContract == null) {
			return null;
		}
		@Nullable
		ICharterContract oCharterContract = modelEntityMap.getOptimiserObject(eCharterContract, ICharterContract.class);
		if (oCharterContract != null) {
			return oCharterContract;
		}
		
		final List<ICharterContractTerm> terms = new LinkedList<>();
		if (eCharterContract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
			terms.addAll(processMonthlyBallastBonus(eCharterContract));
		} else if (eCharterContract.getBallastBonusTerms() instanceof SimpleBallastBonusContainer) {
			terms.addAll(processSimpleBallastBonus(eCharterContract));
		} else {
			throw new IllegalArgumentException("Not implemented yet. Please contact Minimax Labs support.");
		}

		if (eCharterContract.getRepositioningFeeTerms() instanceof SimpleRepositioningFeeContainer) {
			terms.addAll(processSimpleRepositioningFee(eCharterContract));
		} else {
			throw new IllegalArgumentException("Not implemented yet. Please contact Minimax Labs support.");
		}

		if (eCharterContract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
			oCharterContract = new MonthlyBallastBonusContract(terms);
		} else {
			oCharterContract = new DefaultCharterContract(terms);
		}
		modelEntityMap.addModelObject(eCharterContract, oCharterContract);
		return oCharterContract;

	}

	private List<ICharterContractTerm> processMonthlyBallastBonus(GenericCharterContract eCharterContract) {
		final MonthlyBallastBonusContainer ballastBonus = (MonthlyBallastBonusContainer) eCharterContract.getBallastBonusTerms();
		final EList<APortSet<Port>> hubs = ballastBonus.getHubs();
		final List<ICharterContractTerm> terms = new LinkedList<>();
		for(final MonthlyBallastBonusTerm term : ballastBonus.getTerms()) {
			Set<IPort> redeliveryPorts = transformPorts(term.getRedeliveryPorts());
			YearMonth ym = term.getMonth();
			int oStartYMInclusive = this.dateHelper.convertTime(ym);
			YearMonth ymNext = ym.plusMonths(1);
			int oEndYMExclusive = this.dateHelper.convertTime(ymNext);
			
			NextPortType ballastBonusTo = term.getBallastBonusTo();
			//Use BigDecimal to avoid loss of precision :-)
			BigDecimal pctCharterRate = new BigDecimal(term.getBallastBonusPctCharter());
			BigDecimal pctFuelRate = new BigDecimal(term.getBallastBonusPctFuel());
			long oPctCharterRate = OptimiserUnitConvertor.convertToInternalPercentage(pctCharterRate) / 100;
			long oPctFuelRate = OptimiserUnitConvertor.convertToInternalPercentage(pctFuelRate) / 100;
			String priceExpression = term.getLumpSumPriceExpression();
			ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
			String fuelPriceExpression = term.getFuelPriceExpression();
			ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
			String charterPriceExpression = term.getHirePriceExpression();
			ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
			int speed = OptimiserUnitConvertor.convertToInternalSpeed(term.getSpeed());
			boolean includeCanalTime = term.isIncludeCanalTime();	
			
			Set<IPort> returnPorts = null;
			
			switch (ballastBonusTo) {
			case NEAREST_HUB:
				returnPorts = transformPorts(hubs);
				break;
			case LOAD_PORT:
				returnPorts = null;
				break;
			}
			
			ICharterContractTerm tt = new MonthlyBallastBonusContractTerm(oStartYMInclusive, oEndYMExclusive, oPctCharterRate, //
					oPctFuelRate, redeliveryPorts, lumpSumCurve, fuelCurve, charterCurve, returnPorts, term.isIncludeCanal(), includeCanalTime, speed);
			
			assert tt != null;
			injector.injectMembers(tt);
			terms.add(tt);
		}
		return terms;
	}
	
	private List<ICharterContractTerm> processSimpleBallastBonus(GenericCharterContract eCharterContract) {
		final SimpleBallastBonusContainer ballastBonus = (SimpleBallastBonusContainer) eCharterContract.getBallastBonusTerms();
		
		final List<ICharterContractTerm> terms = new LinkedList<>();
		for(final BallastBonusTerm term : ballastBonus.getTerms()) {
			Set<IPort> redeliveryPorts = transformPorts(term.getRedeliveryPorts());
			ICharterContractTerm tt = null;
			if (term instanceof LumpSumBallastBonusTerm) {
				String priceExpression = ((LumpSumBallastBonusTerm)term).getPriceExpression();
				ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				tt = new DefaultLumpSumBallastBonusContractTerm(redeliveryPorts, lumpSumCurve);
			} else if (term instanceof NotionalJourneyBallastBonusTerm) {
				final NotionalJourneyBallastBonusTerm t = (NotionalJourneyBallastBonusTerm) term;
				String priceExpression = t.getLumpSumPriceExpression();
				ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				String fuelPriceExpression = t.getFuelPriceExpression();
				ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
				String charterPriceExpression = t.getHirePriceExpression();
				ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
				Set<IPort> returnPorts = transformPorts(t.getReturnPorts());
				int speed = OptimiserUnitConvertor.convertToInternalSpeed(t.getSpeed());
				boolean includeCanalTime = t.isIncludeCanalTime();
				tt = new DefaultNotionalJourneyBallastBonusContractTerm(redeliveryPorts, lumpSumCurve, fuelCurve, //
						charterCurve, returnPorts, t.isIncludeCanal(), includeCanalTime, speed);
			} else {
				throw new IllegalArgumentException("Not implemented yet. Please contact Minimax support.");
			}
			
			assert tt != null;
			injector.injectMembers(tt);
			terms.add(tt);
		}
		return terms;
	}
	
	private List<ICharterContractTerm> processSimpleRepositioningFee(GenericCharterContract eCharterContract) {
		final SimpleRepositioningFeeContainer repositioningFee = (SimpleRepositioningFeeContainer) eCharterContract.getRepositioningFeeTerms();
		
		final List<ICharterContractTerm> terms = new LinkedList<>();
		for(final RepositioningFeeTerm term : repositioningFee.getTerms()) {
			
			ICharterContractTerm tt = null;
			if (term instanceof LumpSumRepositioningFeeTerm) {
				final LumpSumRepositioningFeeTerm t = (LumpSumRepositioningFeeTerm) term;
				String priceExpression = t.getPriceExpression();
				ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				IPort originPort = transformPort(t.getOriginPort());
				tt = new DefaultLumpSumRepositioningFeeContractTerm(originPort, lumpSumCurve);
			} else if (term instanceof OriginPortRepositioningFeeTerm) {
				final OriginPortRepositioningFeeTerm t = (OriginPortRepositioningFeeTerm) term;
				String priceExpression = t.getLumpSumPriceExpression();
				ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				String fuelPriceExpression = t.getFuelPriceExpression();
				ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
				String charterPriceExpression = t.getHirePriceExpression();
				ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
				IPort originPort = transformPort(t.getOriginPort());
				int speed = OptimiserUnitConvertor.convertToInternalSpeed(t.getSpeed());
				boolean includeCanalTime = t.isIncludeCanalTime();
				tt = new DefaultOriginPortRepositioningFeeContractTerm(originPort, lumpSumCurve, fuelCurve, charterCurve, t.isIncludeCanal(), includeCanalTime, speed);
			} else {
				throw new IllegalArgumentException("Not implemented yet. Please contact Minimax support.");
			}
			
			assert tt != null;
			injector.injectMembers(tt);
			terms.add(tt);
		}
		return terms;
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
	
	private @NonNull IPort transformPort(Port originPort) {
		return modelEntityMap.getOptimiserObjectNullChecked(originPort, IPort.class);
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