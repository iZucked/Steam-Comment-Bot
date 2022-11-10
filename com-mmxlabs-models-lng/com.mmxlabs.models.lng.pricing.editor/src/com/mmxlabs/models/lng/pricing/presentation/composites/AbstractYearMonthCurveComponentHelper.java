package com.mmxlabs.models.lng.pricing.presentation.composites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.PricingBasis;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.PriceExpressionProposalFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class AbstractYearMonthCurveComponentHelper extends DefaultComponentHelper {

	public AbstractYearMonthCurveComponentHelper() {

		super(PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE);

		addEditor(PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION, topClass -> new TextInlineEditor(PricingPackage.Literals.ABSTRACT_YEAR_MONTH_CURVE__EXPRESSION) {
			protected IMMXContentProposalProvider createProposalHelper(final Text text) {

				return AutoCompleteHelper.createProposal(text, new TextContentAdapter(), new IMMXContentProposalProvider() {

					private IMMXContentProposalProvider delegate = null;
					private MMXRootObject rootObject = null;

					@Override
					public @Nullable IContentProposal[] getProposals(String contents, int position) {
						if (delegate != null) {
							return delegate.getProposals(contents, position);
						}
						return null;
					}

					@Override
					public void setRootObject(@Nullable MMXRootObject rootObject) {
						this.rootObject = rootObject;
						if (delegate != null) {
							delegate.setRootObject(rootObject);
						}
					}

					@Override
					public void setInputOject(@Nullable EObject eObject) {
						delegate = null;
						if (eObject instanceof CommodityCurve) {
							delegate = new PriceExpressionProposalFactory().create(ExpressionAnnotationConstants.TYPE_COMMODITY);
						} else if (eObject instanceof CurrencyCurve) {
							delegate = new PriceExpressionProposalFactory().create(ExpressionAnnotationConstants.TYPE_CURRENCY);
						} else if (eObject instanceof BunkerFuelCurve) {
							delegate = new PriceExpressionProposalFactory().create(ExpressionAnnotationConstants.TYPE_BASE_FUEL);
						} else if (eObject instanceof CharterCurve) {
							delegate = new PriceExpressionProposalFactory().create(ExpressionAnnotationConstants.TYPE_CHARTER);
						} else if (eObject instanceof PricingBasis) {
							delegate = new PriceExpressionProposalFactory().create(ExpressionAnnotationConstants.TYPE_PRICING_BASIS);
						}
						if (delegate != null) {
							delegate.setInputOject(eObject);
							delegate.setRootObject(rootObject);
						}
					}
				});

			}
		});

	}
}