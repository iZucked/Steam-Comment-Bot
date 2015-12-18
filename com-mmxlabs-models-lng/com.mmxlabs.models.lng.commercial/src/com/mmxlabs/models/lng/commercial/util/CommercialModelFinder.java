package com.mmxlabs.models.lng.commercial.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;

public class CommercialModelFinder {
	private final @NonNull CommercialModel commercialModel;

	public CommercialModelFinder(final @NonNull CommercialModel comercialModel) {
		this.commercialModel = comercialModel;
	}

	@NonNull
	public CommercialModel getCommercialModel() {
		return commercialModel;
	}

	@NonNull
	public BaseLegalEntity findEntity(@NonNull final String name) {
		for (final BaseLegalEntity entity : getCommercialModel().getEntities()) {
			if (name.equals(entity.getName())) {
				return entity;
			}
		}
		throw new IllegalArgumentException("Unknown entity");
	}
}
