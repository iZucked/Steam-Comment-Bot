/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.extensions;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

public interface IChangeSetColumnValueExtender {

	default long getAdditionalUpsideValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

	default long getAdditionalUpstreamValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

	default long getAdditionalCargoOtherValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

	default long getAdditionalShippingFOBValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

	default long getAdditionalShippingDESValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

	default long getAdditionalTaxEtcValue(@NonNull ChangeSetTableRow tableRow, @NonNull ResultType resultType) {
		return 0L;
	}

}
