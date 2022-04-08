package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.Collections;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

public class SensitivityModelEditorContribution extends BaseJointModelEditorContribution<SensitivityModel> {
	private SensitivityCurvesPane curvesPane = null;

	private int curvesPage = -1;

	@Override
	public void addPages(Composite parent) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PRICE_SENSITIVITY)) {
			curvesPane = new SensitivityCurvesPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			curvesPane.createControl(parent);
			curvesPane.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getSensitivityModel_SensitivityModel()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			final LNGScenarioModel model = ScenarioModelUtil.findScenarioModel(modelObject);
			if (model != null) {
				final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(model);
				curvesPane.setInput(modelObject, pricingModel);
			}

			curvesPage = editorPart.addPage(curvesPane.getControl());
			editorPart.setPageText(curvesPage, "Sensitivity");
		}
	}

	@Override
	public void setLocked(boolean locked) {
		if (curvesPane != null) {
			curvesPane.setLocked(locked);
		}

	}
}
