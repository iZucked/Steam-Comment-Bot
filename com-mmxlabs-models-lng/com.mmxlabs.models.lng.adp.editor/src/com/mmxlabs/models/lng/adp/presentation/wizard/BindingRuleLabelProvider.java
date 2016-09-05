package com.mmxlabs.models.lng.adp.presentation.wizard;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DeliverToFlow;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.DeliverToSpotFlow;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SupplyFromFlow;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;
import com.mmxlabs.models.lng.adp.SupplyFromSpotFlow;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

public class BindingRuleLabelProvider implements ILabelProvider {

	@Override
	public void addListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(final Object element, final String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(final Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof BindingRule) {
			final BindingRule bindingRule = (BindingRule) element;
			final ContractProfile<?> profile = bindingRule.getProfile();
			final SubContractProfile<?> subProfile = bindingRule.getSubProfile();
			final String profileName = profile.getContract().getName();
			final String subProfileName = subProfile.getName();

			final FlowType flowType = bindingRule.getFlowType();
			String flowStr = "";
			if (flowType instanceof SupplyFromFlow) {
				if (flowType instanceof SupplyFromSpotFlow) {
					flowStr = "supplied by spot";
				} else if (flowType instanceof SupplyFromProfileFlow) {
					final SupplyFromProfileFlow supplyFromProfileFlow = (SupplyFromProfileFlow) flowType;
					flowStr = "supplied by " + ((ContractProfile<?>) supplyFromProfileFlow.getSubProfile().eContainer()).getContract().getName() + " - "
							+ supplyFromProfileFlow.getSubProfile().getName();
				} else {
					flowStr = "supplied by unknown";
				}
			} else if (flowType instanceof DeliverToFlow) {
				if (flowType instanceof DeliverToSpotFlow) {
					flowStr = "delivered to spot";
				} else if (flowType instanceof DeliverToProfileFlow) {
					final DeliverToProfileFlow deliverToProfileFlow = (DeliverToProfileFlow) flowType;
					flowStr = "delivered to " + ((ContractProfile<?>) deliverToProfileFlow.getSubProfile().eContainer()).getContract().getName() + " - "
							+ deliverToProfileFlow.getSubProfile().getName();
				} else {
					flowStr = "delivered to unknown";
				}
			}
			final ShippingOption option = bindingRule.getShippingOption();
			String vesselStr = "";
			if (option.getVessel() != null) {
				vesselStr = option.getVessel().getName();
			} else if (option.getVesselAssignmentType() instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) option.getVesselAssignmentType();
				vesselStr = vesselAvailability.getVessel().getName();
			} else if (option.getVesselAssignmentType() instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) option.getVesselAssignmentType();
				if (option.getSpotIndex() == -1) {
					vesselStr = String.format("Nominal - %s", charterInMarket.getName());
				} else {
					vesselStr = String.format("Market - %s", charterInMarket.getName());
				}
			}

			return String.format("%s-%s %s on %s", profileName, subProfileName, flowStr, vesselStr);

		}

		return null;
	}

}
