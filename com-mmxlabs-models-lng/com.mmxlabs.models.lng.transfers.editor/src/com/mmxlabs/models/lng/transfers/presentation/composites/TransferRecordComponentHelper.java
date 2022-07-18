package com.mmxlabs.models.lng.transfers.presentation.composites;

import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class TransferRecordComponentHelper extends DefaultComponentHelper {

	public TransferRecordComponentHelper() {
		super(TransfersPackage.Literals.TRANSFER_RECORD);
		addEditor(TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY, topClass -> {
			
			return ComponentHelperUtils.createDefaultEditor(targetClass, TransfersPackage.Literals.TRANSFER_RECORD___GET_FROM_ENTITY);
		});
	}

}
