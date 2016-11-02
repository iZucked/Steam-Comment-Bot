package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;

public class ResultsSetDeletionHelper {
	public static Collection<EObject> getRelatedResultSets(final Collection<EObject> c, final OptionAnalysisModel model) {
		final Set<EObject> relatedSets = new HashSet<EObject>();
		final EList<ResultSet> resultSets = model.getResultSets();
		for (final EObject eObject : c) {
			if (eObject instanceof ShippingOption) {
				for (final ResultSet resultSet : resultSets) {
					if (resultSet.getRows().stream().filter(r -> r.getShipping() == eObject).count() > 0) {
						relatedSets.add(resultSet);
					}
				}
			} else if (eObject instanceof BuyOption) {
				for (final ResultSet resultSet : resultSets) {
					if (resultSet.getRows().stream().filter(r -> r.getBuyOption() == eObject).count() > 0) {
						relatedSets.add(resultSet);
					}
				}
			} else if (eObject instanceof SellOption) {
				for (final ResultSet resultSet : resultSets) {
					if (resultSet.getRows().stream().filter(r -> r.getSellOption() == eObject).count() > 0) {
						relatedSets.add(resultSet);
					}
				}
			}
		}
		return relatedSets;
	}

}
