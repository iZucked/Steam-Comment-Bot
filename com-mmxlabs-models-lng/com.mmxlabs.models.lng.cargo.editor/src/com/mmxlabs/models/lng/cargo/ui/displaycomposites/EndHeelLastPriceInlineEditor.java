package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.EndHeelOptions;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.impl.AbstractComboInlineEditor;

public class EndHeelLastPriceInlineEditor extends AbstractComboInlineEditor {

	private static final String LAST_LNG_OPTION = "<Last LNG Price>";
	
	private boolean ininitalUse = false;
	
	public EndHeelLastPriceInlineEditor(ETypedElement feature) {
		super(feature);
	}
	
	@Override
	protected Command createSetCommand(final Object value) {
		
		final String text = (String) value;
		EditingDomain editingDomain = commandHandler.getEditingDomain();
		CompoundCommand cmd = new CompoundCommand();
		
		if (LAST_LNG_OPTION.equals(text)) {
			cmd.append(SetCommand.create(editingDomain, input, CommercialPackage.eINSTANCE.getEndHeelOptions_PriceExpression(), SetCommand.UNSET_VALUE));
			cmd.append(SetCommand.create(editingDomain, input, CommercialPackage.eINSTANCE.getEndHeelOptions_UseLastHeelPrice(), Boolean.TRUE));
		} else {
			cmd.append(SetCommand.create(editingDomain, input, CommercialPackage.eINSTANCE.getEndHeelOptions_PriceExpression(), text));
			cmd.append(SetCommand.create(editingDomain, input, CommercialPackage.eINSTANCE.getEndHeelOptions_UseLastHeelPrice(), Boolean.FALSE));
		}
		
		ininitalUse = true;

		return cmd;
	}
	
	@Override
	protected Object getValue() {
		Object result = super.getValue();
		
		if (result == null && input instanceof EndHeelOptions eho && eho.isUseLastHeelPrice()) {
			return LAST_LNG_OPTION;
		}
		
		return result;
	}

	@Override
	protected void doSelectionChange(IStructuredSelection sel) {
		if (sel.getFirstElement() instanceof String s) {
			doSetValue(s, true);
		}
	}

	@Override
	protected void setProposalHelper() {
		this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(ccombo, ExpressionAnnotationConstants.TYPE_COMMODITY);
		EditingDomain editingDomain = commandHandler.getEditingDomain();
		for (Resource r : editingDomain.getResourceSet().getResources()) {
			for (EObject o : r.getContents()) {
				if (o instanceof MMXRootObject mmxo) {
					this.proposalHelper.setRootObject(mmxo);
				}
			}
		}
	}
	
	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return CommercialPackage.Literals.END_HEEL_OPTIONS__USE_LAST_HEEL_PRICE.equals(changedFeature) ||
				super.updateOnChangeToFeature(changedFeature);
	}

	@Override
	protected void updateValueDisplay(Object value) {
		if (combo == null || ccombo.isDisposed()) {
			return;
		}
		String text = "";
		if (value instanceof String s) {
			text = s;
		} else if (input instanceof EndHeelOptions eho && eho.isUseLastHeelPrice()) {
			text = LAST_LNG_OPTION;
		}
		if (!ininitalUse) {
			combo.getCombo().setText(text);
		}
	}

	@Override
	protected void setInitialValues() {
		final List<Object> list = new ArrayList<>();
		list.add(LAST_LNG_OPTION);
		addValues(list, false);
	}

}
