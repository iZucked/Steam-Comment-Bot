package com.mmxlabs.lingo.reports.views.cargodiff;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lingo.reports.views.changeset.IChangeSetRowGenerationCustomiser;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

@NonNullByDefault
public class ScenarioDiffChangeSetRowGenerationCustomiser implements IChangeSetRowGenerationCustomiser {

	@Override
	public void setLhsValid(final ChangeSetRowData afterData, final ChangeSetTableRow outputRow) {
		outputRow.setLhsValid(afterData.getLoadSlot() != null);
	}

	@Override
	public void setCurrentRhsValid(final ChangeSetRowData afterData, final ChangeSetTableRow outputRow) {
		outputRow.setCurrentRhsValid(afterData.getDischargeSlot() != null);
	}

}
