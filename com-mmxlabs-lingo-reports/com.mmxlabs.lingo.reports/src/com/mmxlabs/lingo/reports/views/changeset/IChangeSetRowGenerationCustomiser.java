package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;

@NonNullByDefault
public interface IChangeSetRowGenerationCustomiser {
	void setLhsValid(ChangeSetRowData afterData, ChangeSetTableRow outputRow);

	void setCurrentRhsValid(ChangeSetRowData afterData, ChangeSetTableRow outputRow);
}
