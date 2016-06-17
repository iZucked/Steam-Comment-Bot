/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.properties;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.EntityPNLDetails;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.DetailPropertyFactoryRegistry;
import com.mmxlabs.models.ui.properties.factory.IDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BasicPNLProperties implements IDetailPropertyFactory {

	private static final String CATEGORY_PNL = "pnl";

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject) {
		if (eObject instanceof ProfitAndLossContainer) {
			final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) eObject;
			return createTree(profitAndLossContainer, null);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final ProfitAndLossContainer profitAndLossContainer, @Nullable final MMXRootObject rootObject) {
		final DetailPropertyFactoryRegistry registry = DetailPropertyFactoryRegistry.createRegistry();

		final GroupProfitAndLoss groupPnL = profitAndLossContainer.getGroupProfitAndLoss();
		final DetailProperty dp = PropertiesFactory.eINSTANCE.createDetailProperty();

		if (groupPnL == null ){
			return null;
		}
		
		// Create standard details
		createPnLDetailProperty(groupPnL, dp, "Group P&L", "Taxed Group profit and loss", false);

		final DetailProperty preTaxDP = PropertiesFactory.eINSTANCE.createDetailProperty();
		createPnLDetailProperty(groupPnL, preTaxDP, "Group Pre-Tax P&L", "Group profit and loss", true);
		dp.getChildren().add(preTaxDP);

		final Map<BaseLegalEntity, DetailProperty> entityDetailsPropertiesMap = new HashMap<>();
		// Per Entity Details
		for (final EntityProfitAndLoss entityPnL : groupPnL.getEntityProfitAndLosses()) {
			final DetailProperty entityDP;
			final BaseLegalEntity entity = entityPnL.getEntity();
			final String name = entity.getName();
			if (entityDetailsPropertiesMap.containsKey(entity)) {
				entityDP = entityDetailsPropertiesMap.get(entity);
			} else {
				entityDP = PropertiesFactory.eINSTANCE.createDetailProperty();
				entityDP.setName(name);
				entityDetailsPropertiesMap.put(entity, entityDP);
				dp.getChildren().add(entityDP);
			}

			DetailProperty dpNode = null;
			final BaseEntityBook entityBook = entityPnL.getEntityBook();
			if (entityBook != null) {
				final DetailProperty entityBookDP = PropertiesFactory.eINSTANCE.createDetailProperty();
				entityDP.getChildren().add(entityBookDP);

				if (entityBook.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK) {
					entityBookDP.setName("Shipping Book");
				} else if (entityBook.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
					entityBookDP.setName("Trading Book");
				} else if (entityBook.eContainmentFeature() == CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK) {
					entityBookDP.setName("Upstream Book");
				}
				dpNode = entityBookDP;
			} else {
				dpNode = entityDP;
			}

			final DetailProperty entityDPPostTax = PropertiesFactory.eINSTANCE.createDetailProperty();

			createPnLDetailProperty(entityPnL, entityDPPostTax, "P&L", "Taxed profit and loss for " + name, false);
			dpNode.getChildren().add(entityDPPostTax);

			final DetailProperty entityDPPretax = PropertiesFactory.eINSTANCE.createDetailProperty();
			createPnLDetailProperty(entityPnL, entityDPPretax, "Pre-Tax P&L", "Pre-tax profit and loss for " + name, true);
			dpNode.getChildren().add(entityDPPretax);
		}

		final Map<Slot, DetailProperty> slotDetailsPropertiesMap = new HashMap<>();
		for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {

			if (generalPNLDetails instanceof EntityPNLDetails) {

				final EntityPNLDetails entityPNLDetails = (EntityPNLDetails) generalPNLDetails;

				final DetailProperty entityProperty = entityDetailsPropertiesMap.get(entityPNLDetails.getEntity());
				if (entityProperty == null) {
					continue;
				}
				for (final GeneralPNLDetails generalPNLDetails2 : entityPNLDetails.getGeneralPNLDetails()) {

					final EClass eClass = generalPNLDetails2.eClass();
					assert eClass != null;
					final IDetailPropertyFactory factory = registry.getFactory(CATEGORY_PNL, eClass);
					if (factory != null) {
						final DetailProperty p = factory.createProperties(generalPNLDetails2, rootObject);
						if (p != null) {
							entityProperty.getChildren().add(p);
						}
					}
				}

				continue;
			}

			if (generalPNLDetails instanceof SlotPNLDetails) {

				final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;

				DetailProperty slotProperty = slotDetailsPropertiesMap.get(slotPNLDetails.getSlot());
				if (slotProperty == null) {
					slotProperty = PropertiesFactory.eINSTANCE.createDetailProperty();
					dp.getChildren().add(slotProperty);
					final Slot slot = slotPNLDetails.getSlot();
					if (slot != null) {
						slotProperty.setName(slot.getName());
					}
				}

				for (final GeneralPNLDetails generalPNLDetails2 : slotPNLDetails.getGeneralPNLDetails()) {

					final EClass eClass = generalPNLDetails2.eClass();
					assert eClass != null;
					final IDetailPropertyFactory factory = registry.getFactory(CATEGORY_PNL, eClass);
					if (factory != null) {
						final DetailProperty p = factory.createProperties(generalPNLDetails2, rootObject);
						if (p != null) {
							slotProperty.getChildren().add(p);
						}
					}
				}

				continue;
			}

			final EClass eClass = generalPNLDetails.eClass();
			assert eClass != null;
			final IDetailPropertyFactory factory = registry.getFactory(CATEGORY_PNL, eClass);
			if (factory != null) {
				final DetailProperty p = factory.createProperties(generalPNLDetails, rootObject);
				if (p != null) {
					dp.getChildren().add(p);
				}
			}
		}

		// Query the registry for generic extensions
		for (final EObject eObj : profitAndLossContainer.getExtensions()) {
			final EClass eClass = eObj.eClass();
			assert eClass != null;
			final IDetailPropertyFactory factory = registry.getFactory(CATEGORY_PNL, eClass);
			if (factory != null) {
				final DetailProperty p = factory.createProperties(eObj, rootObject);
				if (p != null) {
					dp.getChildren().add(p);
				}
			}
		}
		return dp;

	}

	private void createPnLDetailProperty(final GroupProfitAndLoss groupPnL, final DetailProperty dp, final String name, final String description, final boolean pretax) {
		dp.setName(name);
		dp.setDescription(description);
		dp.setUnitsPrefix("$");
		dp.setObject(pretax ? groupPnL.getProfitAndLossPreTax() : groupPnL.getProfitAndLoss());
		dp.setLabelProvider(new StringFormatLabelProvider("%,d"));
	}

	private void createPnLDetailProperty(final EntityProfitAndLoss entityPnL, final DetailProperty dp, final String name, final String description, final boolean pretax) {
		dp.setName(name);
		dp.setDescription(description);
		dp.setUnitsPrefix("$");
		dp.setObject(pretax ? entityPnL.getProfitAndLossPreTax() : entityPnL.getProfitAndLoss());
		dp.setLabelProvider(new StringFormatLabelProvider("%,d"));
	}

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject, @Nullable final MMXRootObject rootObject) {
		if (eObject instanceof ProfitAndLossContainer) {
			final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) eObject;
			return createTree(profitAndLossContainer, rootObject);
		}
		return null;
	}
}
