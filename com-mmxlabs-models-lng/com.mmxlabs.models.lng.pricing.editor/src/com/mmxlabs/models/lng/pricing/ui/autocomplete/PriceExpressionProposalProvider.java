/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
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
			Character.valueOf('?'), //
			Character.valueOf(','), //
			Character.valueOf('(') //
			);

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
		final List<AbstractYearMonthCurve> curves = new LinkedList<>();
		for (final PriceIndexType type : types) {
			switch (type) {
			case BUNKERS:
				curves.addAll(pricingModel.getBunkerFuelCurves());
				break;
			case CHARTER:
				curves.addAll(pricingModel.getCharterCurves());
				break;
			case COMMODITY:
				curves.addAll(pricingModel.getCommodityCurves());
				break;
			case CURRENCY:
				curves.addAll(pricingModel.getCurrencyCurves());
				break;
			default:
				return new IContentProposal[0];
			}
		}
		for (final AbstractYearMonthCurve index : curves) {
			final String proposal = index.getName();
			String type = "";
			if (index instanceof CurrencyCurve) {
				type = " (currency conversion)";
			}
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + type, null, c.length()));
			}
		}

		String labelSuffix = " (unit conversion)";
		for (final UnitConversion factor : pricingModel.getConversionFactors()) {
			{
				final String proposal = PriceIndexUtils.createConversionFactorName(factor);
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					list.add(new ContentProposal(c, proposal + labelSuffix, String.format("Number of %s\'s per %s is %.6f", factor.getFrom(), factor.getTo(), factor.getFactor()), c.length()));
				}
			}
			{
				final String proposal = PriceIndexUtils.createReverseConversionFactorName(factor);
				if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
					final String c = proposal.substring(contents.length());
					list.add(new ContentProposal(c, proposal + labelSuffix, String.format("Number of %s\'s per %s is %.6f", factor.getTo(), factor.getFactor(), 1.0 / factor.getFactor()), c.length()));
				}
			}
		}
		{
			final String proposal = "CAP(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "a,b)", "Use this to put an upper limit (a or b) on a price expression, e.g. \"CAP(1.1, 115%HH)\" gives 115%HH but no higher than $1.10", c.length()));
			}
		}
		{
			final String proposal = "FLOOR(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "a,b)", "Use this to put a lower limit (a or b) on a price expression, e.g. \"FLOOR(1.1, 115%HH)\" gives 115%HH but no lower than $1.10", c.length()));
			}
		}
		{
			final String proposal = "MIN(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "a,b)", "Function returning the lower value of a or b.", c.length()));
			}
		}
		{
			final String proposal = "MAX(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "a,b)", "Function returning the higher value of a or b.", c.length()));
			}
		}
		{
			final String proposal = "SHIFT(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "index,months)",
						"Function shifting the index price by the number of months. A positive value will take the price from previous months. A negative value takes the price from future months.",
						c.length()));
			}
		}
		{
			final String proposal = "SPLITMONTH(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "index,index,day)",
						"Function switching between two index at a given point each month",
						c.length()));
			}
		}
		{
			final String proposal = "DATEDAVG(";
			if (proposal.length() >= contents.length() && proposal.substring(0, contents.length()).equalsIgnoreCase(contents)) {
				final String c = proposal.substring(contents.length());
				list.add(new ContentProposal(c, proposal + "index,window,delay,period)",
						"Function to use the average of a month window with delay and application period",
						c.length()));
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
