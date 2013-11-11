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

		final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
		final DetailProperty dp = PropertiesFactory.eINSTANCE.createDetailProperty();

		// Create standard details
		{
			dp.setName("Group Profit and Loss");
			dp.setDescription("Taxed Group Profit and Loss");
			dp.setUnitsPrefix("$");
			dp.setObject(groupProfitAndLoss.getProfitAndLoss());
			dp.setLabelProvider(new StringFormatLabelProvider("%,d"));
		}

		// Per Entity Details
		for (final EntityProfitAndLoss e : groupProfitAndLoss.getEntityProfitAndLosses()) {
			final DetailProperty entityDP = PropertiesFactory.eINSTANCE.createDetailProperty();

			entityDP.setName(e.getEntity().getName() + " Profit and Loss");
			entityDP.setDescription("Taxed Profit and Loss for " + e.getEntity().getName());
			entityDP.setUnitsPrefix("$");
			entityDP.setObject(e.getProfitAndLoss());
			entityDP.setLabelProvider(new StringFormatLabelProvider("%,d"));

			dp.getChildren().add(entityDP);
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
