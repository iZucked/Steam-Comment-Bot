package com.mmxlabs.models.lng.analytics.navigator;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.dnd.IScenarioFragmentCopyHandler;

public class FragmentCopyHandler implements IScenarioFragmentCopyHandler {

	@Override
	public boolean copy(@NonNull final ScenarioFragment scenarioFragment, @NonNull final ScenarioInstance target) {

		final ScenarioInstance source = scenarioFragment.getScenarioInstance();

		try (ModelReference sourceReference = source.getReference("FragmentCopyHandler:1")) {
			final EObject fragment = scenarioFragment.getFragment();
			if (fragment instanceof OptionAnalysisModel) {
				final OptionAnalysisModel sourceModel = (OptionAnalysisModel) fragment;

				if (target == source) {
					final OptionAnalysisModel copyModel = EcoreUtil.copy(sourceModel);
					final LNGScenarioModel targetModel = (LNGScenarioModel) sourceReference.getInstance();
					targetModel.getOptionModels().add(copyModel);
					target.setDirty(true);
					return true;
				} else {
					//
					try (ModelReference targetReference = target.getReference("FragmentCopyHandler:2")) {
						final LNGScenarioModel targetModel = (LNGScenarioModel) targetReference.getInstance();

						final OptionAnalysisModel copyModel = EcoreUtil.copy(sourceModel);

						final Map<String, PurchaseContract> purchaseContractMapping = generatePurchaseContractMapping(targetModel);
						final Map<String, SalesContract> salesContractMapping = generateSalesContractMapping(targetModel);
						final Map<String, Port> portMapping = generatePortMapping(targetModel);
						final Map<String, BaseLegalEntity> entityMapping = generateEntityMapping(targetModel);

						final Map<String, LoadSlot> loadMapping = generateLoadMapping(targetModel);
						final Map<String, DischargeSlot> dischargeMapping = generateDischargeMapping(targetModel);

						final Map<String, SpotMarket> desSaleMarketMapping = generateDESSaleMarketMapping(targetModel);
						final Map<String, SpotMarket> fobSaleMarketMapping = generateFOBSaleMarketMapping(targetModel);
						final Map<String, SpotMarket> desPurchaseMarketMapping = generateDESPurchaseMarketMapping(targetModel);
						final Map<String, SpotMarket> fobPurchaseMarketMapping = generateFOBPurchaseMarketMapping(targetModel);

						final Map<String, Vessel> vesselMapping = generateVesselMapping(targetModel);
						final Map<String, VesselClass> vesselClassMapping = generateVesselClassMapping(targetModel);

						for (final BuyOption buy : copyModel.getBuys()) {
							if (buy instanceof BuyReference) {
								final BuyReference b = (BuyReference) buy;
								if (b.getSlot() != null) {
									b.setSlot(loadMapping.get(b.getSlot().getName()));
								}
							} else if (buy instanceof BuyOpportunity) {
								final BuyOpportunity b = (BuyOpportunity) buy;
								if (b.getPort() != null) {
									b.setPort(portMapping.get(b.getPort().getName()));
								}
								if (b.getContract() != null) {
									b.setContract(purchaseContractMapping.get(b.getContract().getName()));
								}
								if (b.getEntity() != null) {
									b.setEntity(entityMapping.get(b.getEntity().getName()));
								}
							} else if (buy instanceof BuyMarket) {
								final BuyMarket b = (BuyMarket) buy;
								if (b.getMarket() != null) {
									if (b.getMarket() instanceof FOBPurchasesMarket) {
										b.setMarket(fobPurchaseMarketMapping.get(b.getMarket().getName()));
									}
									if (b.getMarket() instanceof DESPurchaseMarket) {
										b.setMarket(desPurchaseMarketMapping.get(b.getMarket().getName()));
									}
								}

							} else {
								assert false;
							}
						}
						for (final SellOption sell : copyModel.getSells()) {
							if (sell instanceof SellReference) {
								final SellReference b = (SellReference) sell;
								if (b.getSlot() != null) {
									b.setSlot(dischargeMapping.get(b.getSlot().getName()));
								}
							} else if (sell instanceof SellOpportunity) {
								final SellOpportunity b = (SellOpportunity) sell;
								if (b.getPort() != null) {
									b.setPort(portMapping.get(b.getPort().getName()));
								}
								if (b.getContract() != null) {
									b.setContract(salesContractMapping.get(b.getContract().getName()));
								}
								if (b.getEntity() != null) {
									b.setEntity(entityMapping.get(b.getEntity().getName()));
								}
							} else if (sell instanceof SellMarket) {
								final SellMarket b = (SellMarket) sell;
								if (b.getMarket() != null) {
									if (b.getMarket() instanceof FOBSalesMarket) {
										b.setMarket(fobSaleMarketMapping.get(b.getMarket().getName()));
									}
									if (b.getMarket() instanceof DESSalesMarket) {
										b.setMarket(desSaleMarketMapping.get(b.getMarket().getName()));
									}
								}

							} else {
								assert false;
							}
						}

						final Consumer<ShippingOption> updateShipping = (option) -> {
							if (option == null) {
								return;
							}
							if (option instanceof NominatedShippingOption) {
								final NominatedShippingOption opt = (NominatedShippingOption) option;
								if (opt.getNominatedVessel() != null) {
									opt.setNominatedVessel(vesselMapping.get(opt.getNominatedVessel().getName()));
								}
							} else if (option instanceof FleetShippingOption) {
								final FleetShippingOption opt = (FleetShippingOption) option;
								if (opt.getVessel() != null) {
									opt.setVessel(vesselMapping.get(opt.getVessel().getName()));
								}
								if (opt.getEntity() != null) {
									opt.setEntity(entityMapping.get(opt.getEntity().getName()));
								}
							} else if (option instanceof RoundTripShippingOption) {
								final RoundTripShippingOption opt = (RoundTripShippingOption) option;
								if (opt.getVesselClass() != null) {
									opt.setVesselClass(vesselClassMapping.get(opt.getVesselClass().getName()));
								}
							}
						};

						// for (BaseCaseRow row : copyModel.getBaseCase().getBaseCase()) {
						// updateShipping.accept(row.getShipping());
						// }
						// for (PartialCaseRow row : copyModel.getPartialCase().getPartialCase()) {
						// updateShipping.accept(row.getShipping());
						// }
						for (final ShippingOption opt : copyModel.getShippingTemplates()) {
							updateShipping.accept(opt);
						}
						// target.setDirty(true);
						// targetModel.getOptionModels().add(copyModel);

						final EditingDomain domain = (EditingDomain) target.getAdapters().get(EditingDomain.class);
						domain.getCommandStack().execute(AddCommand.create(domain, targetModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_OptionModels(), copyModel));

						return true;
					}
				}
			}
		}

		// TODO Auto-generated method stub
		return false;

	}

	private Map<String, Port> generatePortMapping(final LNGScenarioModel lngScenarioModel) {
		final PortModel model = ScenarioModelUtil.getPortModel(lngScenarioModel);
		return model.getPorts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, BaseLegalEntity> generateEntityMapping(final LNGScenarioModel lngScenarioModel) {
		final CommercialModel model = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
		return model.getEntities().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, PurchaseContract> generatePurchaseContractMapping(final LNGScenarioModel lngScenarioModel) {
		final CommercialModel model = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
		return model.getPurchaseContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, SalesContract> generateSalesContractMapping(final LNGScenarioModel lngScenarioModel) {
		final CommercialModel model = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
		return model.getSalesContracts().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, SpotMarket> generateDESSaleMarketMapping(final LNGScenarioModel lngScenarioModel) {
		final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
		return model.getDesSalesSpotMarket().getMarkets().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, SpotMarket> generateFOBSaleMarketMapping(final LNGScenarioModel lngScenarioModel) {
		final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
		return model.getFobSalesSpotMarket().getMarkets().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, SpotMarket> generateFOBPurchaseMarketMapping(final LNGScenarioModel lngScenarioModel) {
		final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
		return model.getFobPurchasesSpotMarket().getMarkets().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, SpotMarket> generateDESPurchaseMarketMapping(final LNGScenarioModel lngScenarioModel) {
		final SpotMarketsModel model = ScenarioModelUtil.getSpotMarketsModel(lngScenarioModel);
		return model.getDesPurchaseSpotMarket().getMarkets().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, LoadSlot> generateLoadMapping(final LNGScenarioModel lngScenarioModel) {
		final CargoModel model = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		return model.getLoadSlots().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, DischargeSlot> generateDischargeMapping(final LNGScenarioModel lngScenarioModel) {
		final CargoModel model = ScenarioModelUtil.getCargoModel(lngScenarioModel);
		return model.getDischargeSlots().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, Vessel> generateVesselMapping(final LNGScenarioModel lngScenarioModel) {
		final FleetModel model = ScenarioModelUtil.getFleetModel(lngScenarioModel);
		return model.getVessels().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

	private Map<String, VesselClass> generateVesselClassMapping(final LNGScenarioModel lngScenarioModel) {
		final FleetModel model = ScenarioModelUtil.getFleetModel(lngScenarioModel);
		return model.getVesselClasses().stream().collect(Collectors.toMap(NamedObject::getName, Function.identity()));
	}

}
