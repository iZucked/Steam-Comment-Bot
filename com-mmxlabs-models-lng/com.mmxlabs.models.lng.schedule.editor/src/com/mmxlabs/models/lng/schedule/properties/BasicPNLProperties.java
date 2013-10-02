package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.IDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BasicPNLProperties implements IDetailPropertyFactory {

	@Override
	@NonNull
	public DetailProperty createProperties(@NonNull EObject eObject) {
		if (eObject instanceof ProfitAndLossContainer) {
			ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) eObject;
			GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
			return createTree(groupProfitAndLoss);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull GroupProfitAndLoss groupProfitAndLoss) {

		DetailProperty dp = PropertiesFactory.eINSTANCE.createDetailProperty();

		dp.setName("Profit and Loss");
		dp.setDescription("Profit and Loss");
		dp.setUnits("$");
		dp.setObject(groupProfitAndLoss.getProfitAndLoss());
		dp.setLabelProvider(new StringFormatLabelProvider("%,d"));

		return dp;
	}

	@Override
	@NonNull
	public DetailProperty createProperties(@NonNull EObject eObject, @NonNull MMXRootObject rootObject) {
		return createProperties(eObject);
	}

}
