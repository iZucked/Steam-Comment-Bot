package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

@NonNullByDefault
public class DefaultChangeSetRowGenerationCustomiser implements IChangeSetRowGenerationCustomiser {

	@Override
	public void setLhsValid(final ChangeSetRowData afterData, final ChangeSetTableRow outputRow) {
		// Valid can only be set for the after case. If there is no slot (i.e. slot exists in before case only) then valid is false.
		// Valid is only checked for slots currently
		// TODO: Should we do this for events too? Or rename the is LhsSlotValid?
		if (afterData.getLoadSlot() != null) {
			outputRow.setLhsValid(afterData.getLoadAllocation() != null || afterData.getLoadSlot().isOptional());
		}
	}

	@Override
	public void setCurrentRhsValid(ChangeSetRowData afterData, ChangeSetTableRow outputRow) {
		if (afterData.getDischargeSlot() != null) {
			outputRow.setCurrentRhsValid(afterData.getDischargeAllocation() != null || afterData.getDischargeSlot().isOptional());
		}
	}
}
