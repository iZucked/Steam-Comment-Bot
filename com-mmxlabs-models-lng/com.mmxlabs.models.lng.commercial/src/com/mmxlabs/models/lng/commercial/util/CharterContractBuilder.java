package com.mmxlabs.models.lng.commercial.util;

import java.time.YearMonth;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NextPortType;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

public class CharterContractBuilder {

	private final CommercialModel commercialModel;

	private final GenericCharterContract contract;

	public CharterContractBuilder(final CommercialModel commercialModel) {
		this.commercialModel = commercialModel;
		contract = CommercialFactory.eINSTANCE.createGenericCharterContract();
	}

	public GenericCharterContract build() {
		commercialModel.getCharterContracts().add(contract);

		return contract;
	}

	public CharterContractBuilder withName(final String name) {
		contract.setName(name);
		return this;
	}

	public BallastBonusMaker withStandardBallastBonus() {
		return new BallastBonusMaker();
	}

	public MonthlyBallastBonusMaker withMonthlyBallastBonus() {
		return new MonthlyBallastBonusMaker();
	}

	class RepositioningMaker {
		private SimpleRepositioningFeeContainer container;

		public RepositioningMaker() {
			container = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
			contract.setRepositioningFeeTerms(container);
		}

		public CharterContractBuilder build() {

			return CharterContractBuilder.this;
		}

		public RepositioningMaker addLumpSumRule(final @Nullable Port startPort, final String expression) {
			final LumpSumRepositioningFeeTerm term = CommercialFactory.eINSTANCE.createLumpSumRepositioningFeeTerm();
			if (startPort != null) {
				term.getStartPorts().add(startPort);
			}
			term.setPriceExpression(expression);
			container.getTerms().add(term);

			return this;

		}

		public RepositioningMaker addNotionalRule(final Port originPort, final @NonNull Collection<@NonNull APortSet<Port>> startPorts, final double speed, final @NonNull String hireExpression, final @NonNull String fuelExpression, final boolean includeCanalFees,
				final boolean includeCanalTime, final String lumpSumExpression) {

			final OriginPortRepositioningFeeTerm term = CommercialFactory.eINSTANCE.createOriginPortRepositioningFeeTerm();

			term.setOriginPort(originPort);
			term.getStartPorts().addAll(startPorts);
			term.setLumpSumPriceExpression(lumpSumExpression);
			term.setSpeed(speed);
			term.setFuelPriceExpression(fuelExpression);
			term.setHirePriceExpression(hireExpression);
			term.setIncludeCanal(includeCanalFees);
			term.setIncludeCanalTime(includeCanalTime);

			return this;
		}
	}

	class BallastBonusMaker {
		private final SimpleBallastBonusContainer ballastBonusContainer;

		public BallastBonusMaker() {
			ballastBonusContainer = CommercialFactory.eINSTANCE.createSimpleBallastBonusContainer();
			contract.setBallastBonusTerms(ballastBonusContainer);
		}

		public CharterContractBuilder build() {

			return CharterContractBuilder.this;
		}

		public BallastBonusMaker addLumpSumRule(@Nullable final Port redeliveryPort, final String expression) {
			final LumpSumBallastBonusTerm term = CommercialFactory.eINSTANCE.createLumpSumBallastBonusTerm();
			if (redeliveryPort != null) {
				term.getRedeliveryPorts().add(redeliveryPort);
			}
			term.setPriceExpression(expression);
			ballastBonusContainer.getTerms().add(term);
			return this;
		}

		public BallastBonusMaker addLumpSumRule(final Collection<@NonNull APortSet<Port>> redeliveryPorts, final String expression) {
			final LumpSumBallastBonusTerm term = CommercialFactory.eINSTANCE.createLumpSumBallastBonusTerm();

			term.getRedeliveryPorts().addAll(redeliveryPorts);
			term.setPriceExpression(expression);
			ballastBonusContainer.getTerms().add(term);
			return this;
		}

		public BallastBonusMaker addNotionalRule(final @NonNull Collection<@NonNull APortSet<Port>> redeliveryPorts, final double speed, final @NonNull String hireExpression,
				final @NonNull String fuelExpression, final boolean includeCanalFees, final boolean includeCanalTime, final @NonNull Collection<@NonNull APortSet<Port>> returnPorts) {
			final NotionalJourneyBallastBonusTerm term = CommercialFactory.eINSTANCE.createNotionalJourneyBallastBonusTerm();

			term.getRedeliveryPorts().addAll(redeliveryPorts);
			term.getReturnPorts().addAll(returnPorts);
			term.setFuelPriceExpression(fuelExpression);
			term.setHirePriceExpression(hireExpression);
			term.setIncludeCanal(includeCanalFees);
			term.setIncludeCanalTime(includeCanalTime);
			term.setSpeed(speed);

			ballastBonusContainer.getTerms().add(term);
			return this;
		}

	}

	class MonthlyBallastBonusMaker {
		private final MonthlyBallastBonusContainer ballastBonusContainer;

		public MonthlyBallastBonusMaker() {
			ballastBonusContainer = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContainer();
			contract.setBallastBonusTerms(ballastBonusContainer);
		}

		public CharterContractBuilder build() {

			return CharterContractBuilder.this;
		}

		public MonthlyBallastBonusMaker addTerm(final YearMonth ym, final NextPortType nextPortType, final String pctFuelRate, final String pctCharterRate,
				final @NonNull Collection<@NonNull APortSet<Port>> redeliveryPorts, final double speed, final @NonNull String hireExpression, final @NonNull String fuelExpression,
				final boolean includeCanalFees, final boolean includeCanalTime, final @NonNull Collection<@NonNull APortSet<Port>> returnPorts) {

			for (var term : ballastBonusContainer.getTerms()) {
				if (ym.equals(term.getMonth())) {
					throw new IllegalArgumentException("Duplicate month: " + ym);
				}
			}

			final MonthlyBallastBonusTerm term = CommercialFactory.eINSTANCE.createMonthlyBallastBonusTerm();

			term.setMonth(ym);
			term.setBallastBonusTo(nextPortType);
			term.setBallastBonusPctFuel(pctFuelRate);
			term.setBallastBonusPctCharter(pctCharterRate);

			term.getRedeliveryPorts().addAll(redeliveryPorts);
			term.getReturnPorts().addAll(returnPorts);
			term.setFuelPriceExpression(fuelExpression);
			term.setHirePriceExpression(hireExpression);
			term.setIncludeCanal(includeCanalFees);
			term.setIncludeCanalTime(includeCanalTime);
			term.setSpeed(speed);

			ballastBonusContainer.getTerms().add(term);

			return this;
		}

	}

}
