/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.importers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class SpotMarketsModelImporter implements ISubmodelImporter {
	private static final HashMap<String, String> inputs = new HashMap<String, String>();
	public static final String CHARTER_PRICING_KEY = "CHARTER_PRICING";
	public static final String SPOT_CARGO_MARKETS_KEY = "SPOT_CARGO_MARKETS";
	public static final String SPOT_CARGO_MARKETS_AVAILABILITY_KEY = "SPOT_CARGO_MARKETS_AVAILABILITY";

	static {
		inputs.put(CHARTER_PRICING_KEY, "Charter Markets");
		inputs.put(SPOT_CARGO_MARKETS_KEY, "Spot Cargo Markets");
		// inputs.put(SPOT_CARGO_MARKETS_AVAILABILITY_KEY, "Spot Cargo Markets Availability");
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private CharterMarketImporter charterPriceImporter;

	private IClassImporter spotCargoMarketImporter;

	// private IClassImporter spotCargoMarketAvailabilityImporter;

	/**
	 */
	public SpotMarketsModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {

			charterPriceImporter = new CharterMarketImporter();// importerRegistry.getClassImporter(SpotMarketsPackage.eINSTANCE.getCharterCostModel());
			spotCargoMarketImporter = importerRegistry.getClassImporter(SpotMarketsPackage.eINSTANCE.getSpotMarket());
			// spotCargoMarketAvailabilityImporter = importerRegistry.getClassImporter(SpotMarketsPackage.eINSTANCE.getSpotAvailability());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final SpotMarketsModel spotMarketsModel = SpotMarketsFactory.eINSTANCE.createSpotMarketsModel();

		if (inputs.containsKey(CHARTER_PRICING_KEY)) {
			// Pass in an example class for importer code to obtain the package from.
			for (final Object obj : charterPriceImporter.importObjects(SpotMarketsPackage.Literals.SPOT_CHARTER_MARKET, inputs.get(CHARTER_PRICING_KEY), context)) {
				if (obj instanceof CharterInMarket) {
					spotMarketsModel.getCharterInMarkets().add((CharterInMarket) obj);
				} else if (obj instanceof CharterOutMarket) {
					spotMarketsModel.getCharterOutMarkets().add((CharterOutMarket) obj);
				} else if (obj instanceof CharterOutStartDate) {
					final CharterOutStartDate charterOutStartDate = (CharterOutStartDate) obj;
					spotMarketsModel.setCharterOutStartDate(charterOutStartDate);
				}
			}
		}

		if (inputs.containsKey(SPOT_CARGO_MARKETS_KEY)) {
			final Collection<EObject> markets = (spotCargoMarketImporter.importObjects(SpotMarketsPackage.eINSTANCE.getSpotMarket(), inputs.get(SPOT_CARGO_MARKETS_KEY), context));

			SpotMarketGroup desPurchaseGroup = null;
			SpotMarketGroup desSaleGroup = null;
			SpotMarketGroup fobPurchaseGroup = null;
			SpotMarketGroup fobSaleGroup = null;

			final List<SpotMarket> desPurchaseMarkets = new LinkedList<SpotMarket>();
			final List<SpotMarket> desSaleMarkets = new LinkedList<SpotMarket>();
			final List<SpotMarket> fobPurchaseMarkets = new LinkedList<SpotMarket>();
			final List<SpotMarket> fobSaleMarkets = new LinkedList<SpotMarket>();
			for (final EObject market : markets) {

				if (market instanceof SpotMarketGroup) {
					final SpotMarketGroup group = (SpotMarketGroup) market;
					switch (group.getType()) {
					case DES_PURCHASE:
						desPurchaseGroup = group;
						break;
					case DES_SALE:
						desSaleGroup = group;
						break;
					case FOB_PURCHASE:
						fobPurchaseGroup = group;
						break;
					case FOB_SALE:
						fobSaleGroup = group;
						break;
					default:
						break;

					}
				} else if (market instanceof DESPurchaseMarket) {
					desPurchaseMarkets.add((SpotMarket) market);
				} else if (market instanceof DESSalesMarket) {
					desSaleMarkets.add((SpotMarket) market);
				} else if (market instanceof FOBPurchasesMarket) {
					fobPurchaseMarkets.add((SpotMarket) market);
				} else if (market instanceof FOBSalesMarket) {
					fobSaleMarkets.add((SpotMarket) market);
				}

			}
			// Set the groups
			spotMarketsModel.setDesPurchaseSpotMarket(desPurchaseGroup);
			spotMarketsModel.setDesSalesSpotMarket(desSaleGroup);
			spotMarketsModel.setFobPurchasesSpotMarket(fobPurchaseGroup);
			spotMarketsModel.setFobSalesSpotMarket(fobSaleGroup);

			// set the markets
			if (desPurchaseGroup != null) {
				desPurchaseGroup.getMarkets().addAll(desPurchaseMarkets);
			}
			if (desSaleGroup != null) {
				desSaleGroup.getMarkets().addAll(desSaleMarkets);
			}
			if (fobPurchaseGroup != null) {
				fobPurchaseGroup.getMarkets().addAll(fobPurchaseMarkets);
			}
			if (fobSaleGroup != null) {
				fobSaleGroup.getMarkets().addAll(fobSaleMarkets);
			}
		}

		if (spotMarketsModel.getDesPurchaseSpotMarket() == null) {
			final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
			group.setType(SpotType.DES_PURCHASE);
			spotMarketsModel.setDesPurchaseSpotMarket(group);
		}
		if (spotMarketsModel.getDesSalesSpotMarket() == null) {
			final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
			group.setType(SpotType.DES_SALE);
			spotMarketsModel.setDesSalesSpotMarket(group);
		}
		if (spotMarketsModel.getFobPurchasesSpotMarket() == null) {
			final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
			group.setType(SpotType.FOB_PURCHASE);
			spotMarketsModel.setFobPurchasesSpotMarket(group);
		}
		if (spotMarketsModel.getFobSalesSpotMarket() == null) {
			final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
			group.setType(SpotType.FOB_SALE);
			spotMarketsModel.setFobSalesSpotMarket(group);
		}

		return spotMarketsModel;
	}

	@Override
	public void exportModel(@NonNull final EObject model, @NonNull final Map<String, Collection<Map<String, String>>> output, @NonNull final IMMXExportContext context) {
		final SpotMarketsModel spotMarketsModel = (SpotMarketsModel) model;
		{
			final List<EObject> charterObjects = new LinkedList<EObject>(spotMarketsModel.getCharterInMarkets());
			charterObjects.addAll(spotMarketsModel.getCharterOutMarkets());
			final CharterOutStartDate charterOutStartDate = spotMarketsModel.getCharterOutStartDate();
			if (charterOutStartDate != null) {
				charterObjects.add(charterOutStartDate);
			}

			output.put(CHARTER_PRICING_KEY, charterPriceImporter.exportObjects(charterObjects, context));
		}
		{
			// Group the SpotMarketGroup and SpotMarkets all into the same export file
			final LinkedList<EObject> spotMarkets = new LinkedList<EObject>();
			spotMarkets.add(spotMarketsModel.getDesPurchaseSpotMarket());
			spotMarkets.add(spotMarketsModel.getDesSalesSpotMarket());
			spotMarkets.add(spotMarketsModel.getFobPurchasesSpotMarket());
			spotMarkets.add(spotMarketsModel.getFobSalesSpotMarket());

			spotMarkets.addAll(spotMarketsModel.getDesPurchaseSpotMarket().getMarkets());
			spotMarkets.addAll(spotMarketsModel.getDesSalesSpotMarket().getMarkets());
			spotMarkets.addAll(spotMarketsModel.getFobPurchasesSpotMarket().getMarkets());
			spotMarkets.addAll(spotMarketsModel.getFobSalesSpotMarket().getMarkets());

			output.put(SPOT_CARGO_MARKETS_KEY, spotCargoMarketImporter.exportObjects(spotMarkets, context));
		}

	}

	@Override
	public EClass getEClass() {
		return PricingPackage.eINSTANCE.getPricingModel();
	}
}
