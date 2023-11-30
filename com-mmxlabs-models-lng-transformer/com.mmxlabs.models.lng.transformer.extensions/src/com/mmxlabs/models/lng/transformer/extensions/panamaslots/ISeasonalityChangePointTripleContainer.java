package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

@NonNullByDefault
public interface ISeasonalityChangePointTripleContainer {

	List<PanamaWaitingDaysTriple> getChangePointsFor(int year, DateAndCurveHelper dateAndCurveHelper);

	@Nullable
	ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints();

	boolean appliesToYear(int year);
}
