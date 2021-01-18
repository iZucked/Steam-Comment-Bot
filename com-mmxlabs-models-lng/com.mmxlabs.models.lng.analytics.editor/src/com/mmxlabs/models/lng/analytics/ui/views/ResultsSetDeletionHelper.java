/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class ResultsSetDeletionHelper {
	public static Collection<EObject> getRelatedResultSets(final Collection<EObject> c, final OptionAnalysisModel model) {
		final Set<EObject> relatedSets = new HashSet<>();
		AbstractSolutionSet results = model.getResults();
//		if (results != null) {
//			final List<ResultSet> resultSets = results.getOptions().stream() //
//					.filter(ResultSet.class::isInstance) //
//					.map(ResultSet.class::cast) //
//					.collect(Collectors.toList());
//			for (final EObject eObject : c) {
//				if (eObject instanceof ShippingOption) {
//					for (final ResultSet resultSet : resultSets) {
//						if (resultSet.getRows().stream().filter(r -> r.getShipping() == eObject).count() > 0) {
//							relatedSets.add(resultSet);
//						}
//					}
//				} else if (eObject instanceof BuyOption) {
//					for (final ResultSet resultSet : resultSets) {
//						if (resultSet.getRows().stream().filter(r -> r.getBuyOption() == eObject).count() > 0) {
//							relatedSets.add(resultSet);
//						}
//					}
//				} else if (eObject instanceof SellOption) {
//					for (final ResultSet resultSet : resultSets) {
//						if (resultSet.getRows().stream().filter(r -> r.getSellOption() == eObject).count() > 0) {
//							relatedSets.add(resultSet);
//						}
//					}
//				} else if (eObject instanceof VesselEventOption) {
//					// for (final ResultSet resultSet : resultSets) {
//					// if (resultSet.getRows().stream().filter(r -> r.getSellOption() == eObject).count() > 0) {
//					// relatedSets.add(resultSet);
//					// }
//					// }
//				}
//			}
//		}
		return relatedSets;
	}

}
