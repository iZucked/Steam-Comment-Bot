/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contracts.charter;

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
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
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
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ICharterContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGTransformerModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IBallastBonusTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.IRepositioningFeeTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.DefaultCharterContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.impl.MonthlyBallastBonusContract;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultLumpSumBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultLumpSumRepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultNotionalJourneyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultNotionalJourneyBallastBonusContractTerm.FuelCalculationMode;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.DefaultOriginPortRepositioningFeeContractTerm;
import com.mmxlabs.scheduler.optimiser.chartercontracts.terms.MonthlyBallastBonusContractTerm;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 * @author alex
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
	private IPortProvider portProvider;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	@Named(LNGTransformerModule.MONTH_ALIGNED_INTEGER_INTERVAL_CURVE)
	private IIntegerIntervalCurve monthIntervalsInHoursCurve;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Override
	public ICharterContract createCharterContract(final GenericCharterContract eCharterContract) {
		if (eCharterContract == null) {
			return null;
		}
		@Nullable
		ICharterContract oCharterContract = modelEntityMap.getOptimiserObject(eCharterContract, ICharterContract.class);
		if (oCharterContract != null) {
			return oCharterContract;
		}

		final List<IBallastBonusTerm> bbTerms = new LinkedList<>();
		final List<IRepositioningFeeTerm> rfTerms = new LinkedList<>();
		final IBallastBonus ballastBonus = eCharterContract.getBallastBonusTerms();
		if (ballastBonus != null) {
			if (eCharterContract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
				bbTerms.addAll(processMonthlyBallastBonus(eCharterContract));
			} else if (eCharterContract.getBallastBonusTerms() instanceof SimpleBallastBonusContainer) {
				bbTerms.addAll(processSimpleBallastBonus(eCharterContract));
			} else {
				throw new IllegalArgumentException("Not implemented yet. Please contact Minimax Labs support.");
			}
		}

		final IRepositioningFee repositioningFee = eCharterContract.getRepositioningFeeTerms();
		if (repositioningFee != null) {
			if (eCharterContract.getRepositioningFeeTerms() instanceof SimpleRepositioningFeeContainer) {
				rfTerms.addAll(processSimpleRepositioningFee(eCharterContract));
			} else {
				throw new IllegalArgumentException("Not implemented yet. Please contact Minimax Labs support.");
			}
		}

		if (eCharterContract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
			oCharterContract = new MonthlyBallastBonusContract(bbTerms, rfTerms);
		} else {
			oCharterContract = new DefaultCharterContract(bbTerms, rfTerms);
		}
		modelEntityMap.addModelObject(eCharterContract, oCharterContract);
		return oCharterContract;

	}

	private List<IBallastBonusTerm> processMonthlyBallastBonus(final GenericCharterContract eCharterContract) {
		final MonthlyBallastBonusContainer ballastBonus = (MonthlyBallastBonusContainer) eCharterContract.getBallastBonusTerms();
		final EList<APortSet<Port>> hubs = ballastBonus.getHubs();
		final List<IBallastBonusTerm> terms = new LinkedList<>();
		for (final MonthlyBallastBonusTerm term : ballastBonus.getTerms()) {
			final Set<IPort> redeliveryPorts = transformPorts(term.getRedeliveryPorts());
			final YearMonth ym = term.getMonth();
			final int oStartYMInclusive = this.dateHelper.convertTime(ym);
			final YearMonth ymNext = ym.plusMonths(1);
			final int oEndYMExclusive = this.dateHelper.convertTime(ymNext);

			final NextPortType ballastBonusTo = term.getBallastBonusTo();
			// Use BigDecimal to avoid loss of precision :-)
			final BigDecimal pctCharterRate = new BigDecimal(term.getBallastBonusPctCharter());
			final BigDecimal pctFuelRate = new BigDecimal(term.getBallastBonusPctFuel());
			final long oPctCharterRate = OptimiserUnitConvertor.convertToInternalPercentage(pctCharterRate) / 100;
			final long oPctFuelRate = OptimiserUnitConvertor.convertToInternalPercentage(pctFuelRate) / 100;
			final String priceExpression = term.getLumpSumPriceExpression();
			final ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
			final String fuelPriceExpression = term.getFuelPriceExpression();
			final ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
			final String charterPriceExpression = term.getHirePriceExpression();
			final ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
			final int speed = OptimiserUnitConvertor.convertToInternalSpeed(term.getSpeed());
			final boolean includeCanalTime = term.isIncludeCanalTime();

			final Set<IPort> returnPorts = switch (ballastBonusTo) {
			case NEAREST_HUB -> transformPorts(hubs);
			case LOAD_PORT -> null;
			};

			final IBallastBonusTerm tt = new MonthlyBallastBonusContractTerm(oStartYMInclusive, oEndYMExclusive, oPctCharterRate, //
					oPctFuelRate, redeliveryPorts, lumpSumCurve, fuelCurve, charterCurve, returnPorts, term.isIncludeCanal(), includeCanalTime, speed);

			assert tt != null;
			injector.injectMembers(tt);
			terms.add(tt);
		}
		return terms;
	}

	private List<IBallastBonusTerm> processSimpleBallastBonus(final GenericCharterContract eCharterContract) {
		final SimpleBallastBonusContainer ballastBonus = (SimpleBallastBonusContainer) eCharterContract.getBallastBonusTerms();

		final List<IBallastBonusTerm> terms = new LinkedList<>();
		for (final BallastBonusTerm term : ballastBonus.getTerms()) {
			final Set<IPort> redeliveryPorts = transformPorts(term.getRedeliveryPorts());
			IBallastBonusTerm tt = null;
			if (term instanceof LumpSumBallastBonusTerm t) {
				final String priceExpression = t.getPriceExpression();
				final ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				tt = new DefaultLumpSumBallastBonusContractTerm(redeliveryPorts, lumpSumCurve);
			} else if (term instanceof NotionalJourneyBallastBonusTerm t) {

				final String priceExpression = t.getLumpSumPriceExpression();
				final ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				final String charterPriceExpression = t.getHirePriceExpression();
				final ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
				final Set<IPort> returnPorts = transformPorts(t.getReturnPorts());
				final int speed = OptimiserUnitConvertor.convertToInternalSpeed(t.getSpeed());
				final boolean includeCanalTime = t.isIncludeCanalTime();

				FuelCalculationMode fuelCalculationMode = t.isPriceOnLastLNGPrice() ? FuelCalculationMode.LNG_ONLY_WITH_LAST_PRICE : FuelCalculationMode.BUNKERS_ONLY_WITH_CURVE_PRICE;
				final String fuelPriceExpression = t.getFuelPriceExpression();
				final ICurve fuelCurve = t.isPriceOnLastLNGPrice() ? new ConstantValueCurve(0) : getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);

				tt = new DefaultNotionalJourneyBallastBonusContractTerm(redeliveryPorts, lumpSumCurve, fuelCurve, fuelCalculationMode, //
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

	private List<IRepositioningFeeTerm> processSimpleRepositioningFee(final GenericCharterContract eCharterContract) {
		final SimpleRepositioningFeeContainer repositioningFee = (SimpleRepositioningFeeContainer) eCharterContract.getRepositioningFeeTerms();

		final List<IRepositioningFeeTerm> terms = new LinkedList<>();
		final List<IRepositioningFeeTerm> anyPortTerms = new LinkedList<>();
		for (final RepositioningFeeTerm term : repositioningFee.getTerms()) {

			IRepositioningFeeTerm tt = null;
			boolean isAnyPort = false;
			if (term instanceof final LumpSumRepositioningFeeTerm t) {
				final String priceExpression = t.getPriceExpression();
				final ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);

				assert lumpSumCurve != null;

				final Set<IPort> startPorts = transformPorts(t.getStartPorts());
				isAnyPort = t.getStartPorts().isEmpty();

				tt = new DefaultLumpSumRepositioningFeeContractTerm(startPorts, lumpSumCurve);

			} else if (term instanceof final OriginPortRepositioningFeeTerm t) {
				final String priceExpression = t.getLumpSumPriceExpression();
				ILongCurve lumpSumCurve = getPriceCurveFromExpression(priceExpression, charterIndices);
				if (lumpSumCurve == null) {
					lumpSumCurve = new ConstantValueLongCurve(0L);
				}
				final String fuelPriceExpression = t.getFuelPriceExpression();
				final ICurve fuelCurve = getBaseFuelPriceCurveFromExpression(fuelPriceExpression, fuelIndices);
				final String charterPriceExpression = t.getHirePriceExpression();
				final ILongCurve charterCurve = getPriceCurveFromExpression(charterPriceExpression, charterIndices);
				final IPort originPort = transformPort(t.getOriginPort());
				final int speed = OptimiserUnitConvertor.convertToInternalSpeed(t.getSpeed());
				final boolean includeCanalTime = t.isIncludeCanalTime();
				final Set<IPort> startPorts = transformPorts(t.getStartPorts());
				isAnyPort = t.getStartPorts().isEmpty();

				tt = new DefaultOriginPortRepositioningFeeContractTerm(originPort, startPorts, lumpSumCurve, fuelCurve, charterCurve, t.isIncludeCanal(), includeCanalTime, speed);
			} else {
				throw new IllegalArgumentException("Not implemented yet. Please contact Minimax support.");
			}

			assert tt != null;
			injector.injectMembers(tt);
			if (isAnyPort) {
				anyPortTerms.add(tt);
			} else {
				terms.add(tt);
			}
		}
		if (!anyPortTerms.isEmpty()) {
			terms.addAll(anyPortTerms);
		}
		return terms;
	}

	private @NonNull Set<IPort> transformPorts(final Collection<APortSet<Port>> redeliveryPorts) {
		final Set<IPort> ports = new LinkedHashSet<>();
		for (final Port ePort : (SetUtils.getObjects(redeliveryPorts))) {
			@NonNull
			final IPort oPort = modelEntityMap.getOptimiserObjectNullChecked(ePort, IPort.class);
			ports.add(oPort);
		}
		return ports;
	}

	private @NonNull IPort transformPort(final Port originPort) {
		if (originPort == null) {
			return portProvider.getAnywherePort();
		}
		return modelEntityMap.getOptimiserObjectNullChecked(originPort, IPort.class);
	}

	private final ICurve getBaseFuelPriceCurveFromExpression(final String expression, final SeriesParser indices) {
		final ICurve result = dateHelper.generateExpressionCurve(expression, indices);
		return result;
	}

	private final ILongCurve getPriceCurveFromExpression(final String expression, final SeriesParser indices) {
		final ILongCurve result = dateHelper.generateLongExpressionCurve(expression, indices);
		return result;
	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}
}