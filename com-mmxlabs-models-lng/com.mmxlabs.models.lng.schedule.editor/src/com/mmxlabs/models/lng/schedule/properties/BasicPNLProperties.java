package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
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

		final GroupProfitAndLoss groupPnL = profitAndLossContainer.getGroupProfitAndLoss();
		final DetailProperty dp = PropertiesFactory.eINSTANCE.createDetailProperty();

		// Create standard details
		createPnLDetailProperty(groupPnL, dp, "Group P&L", "Taxed Group profit and loss", false);
		
		final DetailProperty preTaxDP = PropertiesFactory.eINSTANCE.createDetailProperty();
		createPnLDetailProperty(groupPnL, preTaxDP, "Group Pre-Tax P&L", "Group profit and loss", true);
		dp.getChildren().add(preTaxDP);

		// Per Entity Details
		for (final EntityProfitAndLoss entityPnL : groupPnL.getEntityProfitAndLosses()) {
			final DetailProperty entityDP = PropertiesFactory.eINSTANCE.createDetailProperty();
			createPnLDetailProperty(entityPnL, entityDP, entityPnL.getEntity().getName() + " P&L", "Taxed profit and loss for " + entityPnL.getEntity().getName(), false);
			dp.getChildren().add(entityDP);

			final DetailProperty entityDPPretax = PropertiesFactory.eINSTANCE.createDetailProperty();
			createPnLDetailProperty(entityPnL, entityDPPretax, entityPnL.getEntity().getName() + " Pre-Tax P&L", "Pre-tax profit and loss for " + entityPnL.getEntity().getName(), true);
			dp.getChildren().add(entityDPPretax);

//			entityDP.setName(entityPnL.getEntity().getName() + " Profit and Loss");
//			entityDP.setDescription("Taxed Profit and Loss for " + entityPnL.getEntity().getName());
//			entityDP.setUnitsPrefix("$");
//			entityDP.setObject(entityPnL.getProfitAndLoss());
//			entityDP.setLabelProvider(new StringFormatLabelProvider("%,d"));

		}

		// Query the registry for extensions
		final DetailPropertyFactoryRegistry registry = DetailPropertyFactoryRegistry.createRegistry();
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

	private void createPnLDetailProperty(
			final GroupProfitAndLoss groupPnL,
			final DetailProperty dp, String name, String description, boolean pretax) {
		dp.setName(name);
		dp.setDescription(description);
		dp.setUnitsPrefix("$");
		dp.setObject(pretax ? groupPnL.getProfitAndLossPreTax() : groupPnL.getProfitAndLoss());
		dp.setLabelProvider(new StringFormatLabelProvider("%,d"));
	}

	private void createPnLDetailProperty(
			final EntityProfitAndLoss entityPnL,
			final DetailProperty dp, String name, String description, boolean pretax) {
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
