/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.NonEditableColumn;

/**
 * @author Tom Hinton
 *
 */
public class PurchaseContractEVP extends ContractEVP {
	public PurchaseContractEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof IndexPricePurchaseContract) {
					return "Index Price";
				} else if (object instanceof FixedPricePurchaseContract) {
					return "Fixed Price";
				} else if (object instanceof NetbackPurchaseContract) {
					return "Netback";
				} else if (object instanceof ProfitSharingPurchaseContract) {
					return "Profit-sharing";
				} else {
					return object.getClass().getSimpleName();
				}
			}
		});
		super.init(path, adapterFactory);
	}
}
