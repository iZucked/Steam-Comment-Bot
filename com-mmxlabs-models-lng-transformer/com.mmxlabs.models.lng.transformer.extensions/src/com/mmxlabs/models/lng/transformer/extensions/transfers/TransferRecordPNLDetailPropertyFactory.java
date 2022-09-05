package com.mmxlabs.models.lng.transformer.extensions.transfers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.TransferRecordPNLDetails;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class TransferRecordPNLDetailPropertyFactory extends AbstractDetailPropertyFactory {

	@Override
	public @Nullable DetailProperty createProperties(@NonNull EObject eObject) {
		if (eObject instanceof TransferRecordPNLDetails details) {
			return createTree(details);
		}
		return null;
	}

	private DetailProperty createTree(@NonNull final TransferRecordPNLDetails transferRecordPNLDetails) {
		final DetailProperty details = PropertiesFactory.eINSTANCE.createDetailProperty();
		{
			details.setName("Transfer Record Details");
		}
		{
			addDetailProperty("Record", "", "", "", transferRecordPNLDetails.getTransferRecord().getName(), new StringFormatLabelProvider("%s"), details);
			addDetailProperty("Price", "", "$/mmBtu", "", transferRecordPNLDetails.getTransferPrice(), new StringFormatLabelProvider("%.2f"), details);
			addDetailProperty("From", "", "", "", transferRecordPNLDetails.getFromEntity().getName(), new StringFormatLabelProvider("%s"), details);
			addDetailProperty("Cost", "", "$", "", transferRecordPNLDetails.getFromEntityCost(), new StringFormatLabelProvider("%,d"), details);
			addDetailProperty("Revenue", "", "$", "", transferRecordPNLDetails.getFromEntityRevenue(), new StringFormatLabelProvider("%,d"), details);
			// Line break or something?
			addDetailProperty("To", "", "", "", transferRecordPNLDetails.getToEntity().getName(), new StringFormatLabelProvider("%s"), details);
			addDetailProperty("Cost", "", "$", "", transferRecordPNLDetails.getToEntityCost(), new StringFormatLabelProvider("%,d"), details);
			addDetailProperty("Revenue", "", "$", "", transferRecordPNLDetails.getToEntityRevenue(), new StringFormatLabelProvider("%,d"), details);
		}
		return details;
	}
}
