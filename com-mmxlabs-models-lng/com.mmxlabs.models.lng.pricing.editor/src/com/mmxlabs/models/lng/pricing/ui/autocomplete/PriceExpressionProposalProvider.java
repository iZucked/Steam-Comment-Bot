/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.autocomplete;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;

/**
 * SimpleContentProposalProvider is a class designed to map a static list of Strings to content proposals.
 *
 * @see IContentProposalProvider
 * @since 3.2
 *
 */
public class PriceExpressionProposalProvider implements IMMXContentProposalProvider {

	private final Set<Character> operators = Sets.newHashSet( //
			Character.valueOf('+'), //
			Character.valueOf('-'), //
			Character.valueOf('*'), //
			Character.valueOf('/'), //
			Character.valueOf('%'), //
			Character.valueOf('?'));

	private LNGScenarioModel scenarioModel;

	private final @NonNull PriceIndexType[] types;

	/**
	 * Construct a SimpleContentProposalProvider whose content proposals are always the specified array of Objects.
	 *
	 * @param proposals
	 *            the array of Strings to be returned whenever proposals are requested.
	 */
	public PriceExpressionProposalProvider(final @NonNull PriceIndexType... types) {
		super();
		this.types = types;
	}

	/**
	 * Return an array of Objects representing the valid content proposals for a field.
	 *
	 * @param contents
	 *            the current contents of the field (only consulted if filtering is set to <code>true</code>)
	 * @param position
	 *            the current cursor position within the field (ignored)
	 * @return the array of Objects that represent valid proposals for the field given its current content.
	 */
	@Override
	public IContentProposal[] getProposals(final String full_contents, final int position) {
		if (scenarioModel == null) {
			return new IContentProposal[0];
		}

		final int completeFrom;
		if (position == 0) {
			// Start of the text, full auto complete)
			completeFrom = 0;
		} else {
			final String substring = full_contents.substring(0, position);
			int lastIndex = substring.lastIndexOf(" ");
			for (final char operator : operators) {
				lastIndex = Math.max(lastIndex, substring.lastIndexOf(operator));
			}
			if (lastIndex != -1) {
				lastIndex += 1;
			}
			completeFrom = Math.max(0, lastIndex);

		}

		final String contents = full_contents.substring(completeFrom, position);
		final ArrayList<ContentProposal> list = new ArrayList<ContentProposal>();
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(scenarioModel);
		final List<NamedIndexContainer<?>> curves = new LinkedList<>();
		for (final PriceIndexType type : types) {
			switch (type) {
			case BUNKERS:
				curves.addAll(pricingModel.getBaseFuelPrices());
				break;
			case CHARTER:
				curves.addAll(pricingModel.getCharterIndices());
				break;
			case COMMODITY:
				curves.addAll(pricingModel.getCommodityIndices());
				break;
			case CURRENCY:
				curves.addAll(pricingModel.getCurrencyIndices());
				break;
			default:
				return new IContentProposal[0];
			}
		}
		for (final NamedIndexContainer<?> index : curves) {
			final String proposal = index.getName();
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal, null, c.length()));
			}
		}

		for (final UnitConversion factor : pricingModel.getConversionFactors()) {
			{
				final String proposal = PriceIndexUtils.createConversionFactorName(factor);
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					list.add(new ContentProposal(c, proposal, String.format("Number of %s\'s per %s is %.6f", factor.getFrom(), factor.getTo(), factor.getFactor()), c.length()));
				}
			}
			{
				final String proposal = PriceIndexUtils.createReverseConversionFactorName(factor);
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					list.add(new ContentProposal(c, proposal, String.format("Number of %s\'s per %s is %.6f", factor.getTo(), factor.getFactor(), 1.0 / factor.getFactor()), c.length()));
				}
			}
		}

		return list.toArray(new IContentProposal[list.size()]);

	}

	@Override
	public void setRootObject(final MMXRootObject rootObject) {
		if (rootObject instanceof LNGScenarioModel) {
			this.scenarioModel = (LNGScenarioModel) rootObject;
		} else {
			this.scenarioModel = null;
		}
	}
}
