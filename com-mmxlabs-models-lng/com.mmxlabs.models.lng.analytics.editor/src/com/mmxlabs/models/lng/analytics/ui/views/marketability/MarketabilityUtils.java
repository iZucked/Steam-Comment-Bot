/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.MarketabilityAssignableElement;
import com.mmxlabs.models.lng.analytics.MarketabilityEndEvent;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.analytics.MarketabilityResultContainer;
import com.mmxlabs.models.lng.analytics.MarketabilityRow;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public final class MarketabilityUtils {

	private MarketabilityUtils() {
	}

	public static MarketabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name, final Integer vesselSpeed) {
		final MarketabilityModel model = AnalyticsFactory.eINSTANCE.createMarketabilityModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);

		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null && spotMarket.isEnabled()) {
					model.getMarkets().add(spotMarket);
				}
			}
		}

		for (Cargo c : cargoModel.getCargoes()) {
			Slot<?> slot1 = c.getSortedSlots().get(0);
			Slot<?> slot2 = c.getSortedSlots().get(1);
			if (slot1 instanceof LoadSlot loadSlot && slot2 instanceof DischargeSlot dischargeSlot) {
				ShippingOption shippingOption = null;
				if (c.getVesselAssignmentType() instanceof VesselCharter vesselCharter) {
					final ExistingVesselCharterOption evco = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
					evco.setVesselCharter(vesselCharter);
					shippingOption = evco;
				}
				if (shippingOption == null) {
					continue;
				}
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(loadSlot);
				model.getBuys().add(buy);

				final SellReference sell = AnalyticsFactory.eINSTANCE.createSellReference();
				sell.setSlot(dischargeSlot);
				model.getSells().add(sell);

				model.getShippingTemplates().add(shippingOption);
				final MarketabilityRow row = AnalyticsFactory.eINSTANCE.createMarketabilityRow();
				row.setBuyOption(buy);
				row.setSellOption(sell);
				row.setShipping(shippingOption);
				model.getRows().add(row);

				final MarketabilityResultContainer container = AnalyticsFactory.eINSTANCE.createMarketabilityResultContainer();

				row.setResult(container);
			}
		}
		if (vesselSpeed != null) {
			model.setVesselSpeed(vesselSpeed);
		} else {
			model.unsetVesselSpeed();
		}

		return model;
	}
	


	public static @Nullable CanalJourneyEvent findNextPanama(final @NonNull Event start) {
		Event nextEvent = start.getNextEvent();
		while (!(nextEvent == null || nextEvent instanceof SlotVisit)) {
			if (nextEvent instanceof Journey journey && journey.getCanalJourneyEvent() != null) {
				return journey.getCanalJourneyEvent();
			}
			nextEvent = nextEvent.getNextEvent();
		}
		return null;
	}

	public static void addNextEventToRow(final @NonNull MarketabilityResultContainer container, final @NonNull Event start) {
		Event nextEvent = start.getNextEvent();
		while (nextEvent != null) {
			if (nextEvent instanceof VesselEventVisit vesselEventVisit) {
				MarketabilityAssignableElement marketabilityEvent = AnalyticsFactory.eINSTANCE.createMarketabilityAssignableElement();
				marketabilityEvent.setStart(nextEvent.getStart());
				marketabilityEvent.setElement(vesselEventVisit.getVesselEvent());
				container.setNextEvent(marketabilityEvent);
				return;
			} else if (nextEvent instanceof SlotVisit slotVisit) {
				MarketabilityAssignableElement marketabilityEvent = AnalyticsFactory.eINSTANCE.createMarketabilityAssignableElement();
				marketabilityEvent.setStart(nextEvent.getStart());
				marketabilityEvent.setElement(slotVisit.getSlotAllocation().getSlot().getCargo());
				container.setNextEvent(marketabilityEvent);
				return;
			} else if (nextEvent instanceof EndEvent e) {
				MarketabilityEndEvent marketabilityEndEvent = AnalyticsFactory.eINSTANCE.createMarketabilityEndEvent();
				marketabilityEndEvent.setStart(nextEvent.getStart());
				
				container.setNextEvent(marketabilityEndEvent);
				return;

			} else {
				nextEvent = nextEvent.getNextEvent();
			}
		}
	}
	
	public static boolean validateMarketabilityModel(final IScenarioDataProvider scenarioDataProvider, @Nullable EObject extraTarget,  final boolean displayWarnings,
			final boolean relaxedValidation) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter((constraint, target) -> {
			return constraint.getCategories().stream().anyMatch(cat -> cat.getId().endsWith(".marketability"));
		});

		final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);
			return helper.runValidation(validator, extraContext, rootObject, extraTarget);
		});

		if (status == null) {
			return false;
		}

		if (!status.isOK()) {

			if (status.getSeverity() == IStatus.WARNING || status.getSeverity() == IStatus.ERROR) {

				// See if this command was executed in the UI thread - if so fire up the dialog
				// box.
				if (displayWarnings) {
					final boolean[] res = new boolean[1];
					Display.getDefault().syncExec(() -> {
						final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getDefault().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

						// Wait for use to press a button before continuing.
						dialog.setBlockOnOpen(true);

						if (dialog.open() == Window.CANCEL) {
							res[0] = false;
						} else {
							res[0] = true;
						}
					});
					if (!res[0]) {
						return false;
					}
				}
			}
		}
		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}

}
