/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * @author Simon Goodall
 * 
 */
public class FuelCostManipulator extends DefaultToolTipProvider implements ICellManipulator, ICellRenderer {

	private static final String LAST_LNG_OPTION = "<Last LNG Price>";

	private final ICommandHandler commandHandler;

	private IExtraCommandsHook extraCommandsHook;

	private Object parent;
	private EStructuralFeature feature;

	/**
	 * Create a manipulator for the given field in the target object, taking values
	 * from the given valueProvider and creating set commands in the provided
	 * editingDomain.
	 * 
	 * @param field         the field to set
	 * @param valueProvider provides the names & values for the field
	 * @param editingDomain editing domain for setting
	 */
	public FuelCostManipulator(EStructuralFeature feature, final ICommandHandler commandHandler) {

		this.feature = feature;
		this.commandHandler = commandHandler;
	}

	@Override
	public String render(final Object object) {

		if (object == null) {
			return null;
		}
		if (object instanceof NotionalJourneyBallastBonusTerm term) {
			if (term.isPriceOnLastLNGPrice()) {
				return LAST_LNG_OPTION;
			}
			return term.getFuelPriceExpression();
		}

		throw new IllegalArgumentException();
	}

	public void doSetValue(final Object object, final Object value) {

		final String text = (String) value;
		EditingDomain editingDomain = commandHandler.getEditingDomain();
		CompoundCommand cmd = new CompoundCommand();

		if (LAST_LNG_OPTION.equals(text)) {
			cmd.append(SetCommand.create(editingDomain, object, CommercialPackage.eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression(), SetCommand.UNSET_VALUE));
			cmd.append(SetCommand.create(editingDomain, object, CommercialPackage.eINSTANCE.getNotionalJourneyTerm_PriceOnLastLNGPrice(), Boolean.TRUE));
		} else {
			cmd.append(SetCommand.create(editingDomain, object, CommercialPackage.eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression(), text));
			cmd.append(SetCommand.create(editingDomain, object, CommercialPackage.eINSTANCE.getNotionalJourneyTerm_PriceOnLastLNGPrice(), Boolean.FALSE));
		}
		if (!cmd.isEmpty()) {
			if (extraCommandsHook != null) {
				extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
			}
			commandHandler.handleCommand(cmd, (EObject) object, CargoPackage.eINSTANCE.getSlot_PriceExpression());

		}
	}

	public CellEditor createCellEditor(final Composite c, final Object object) {
		final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(c,  SWT.DROP_DOWN| SWT.BORDER) {

			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				IMMXContentProposalProvider proposalHelper = AutoCompleteHelper.createControlProposalAdapter(control, ExpressionAnnotationConstants.TYPE_BASE_FUEL);
				EditingDomain editingDomain = commandHandler.getEditingDomain();
				for (Resource r : editingDomain.getResourceSet().getResources()) {
					for (EObject o : r.getContents()) {
						if (o instanceof MMXRootObject mmxRootObject) {
							proposalHelper.setRootObject(mmxRootObject);
						}
					}
				}
				return control;
			}

			/**
			 * Override doGetValue to also return the custom string if a valid selection has
			 * not been made.
			 */
			@Override
			protected Object doGetValue() {
				final Object value = super.doGetValue();
				if (value == null) {
					return ((CCombo) getControl()).getText();
				}
				return value;
			}
		};
		editor.setContentProvider(new ArrayContentProvider());
		setEditorValues(editor, (EObject) object);
		return editor;
	}

	@Override
	public Object getValue(final Object object) {

		if (object == null) {
			return "";
		}

		final EObject eObject = (EObject) object;

		return eObject.eGet(feature);
	}

	@Override
	public boolean canEdit(final Object object) {
		return object != null;
	}

	private void setEditorValues(final ComboBoxViewerCellEditor editor, final EObject eObject) {

		final List<Object> valueList = new ArrayList<>();
		valueList.add(LAST_LNG_OPTION);
		if (commandHandler.getModelReference().getInstance() instanceof LNGScenarioModel m) {
			PricingModel pricingModel = ScenarioModelUtil.getPricingModel(m);
			pricingModel.getBunkerFuelCurves().stream() //
					.sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName())) //
					.map(BunkerFuelCurve::getName)
					.forEach(valueList::add);
		}

		// Remove any real null value
		while (valueList.remove(null))
			;
		editor.setInput(valueList);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? "" : setValue.toString();
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		doSetValue(object, value);
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		return createCellEditor(c, object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}

	@Override
	public void setParent(Object parent, Object object) {
		this.parent = parent;
	}

	@Override
	public void setExtraCommandsHook(IExtraCommandsHook extraCommandsHook) {
		this.extraCommandsHook = extraCommandsHook;
	}
	
	@Override
	public String getToolTipText(Object element) {
		if (element instanceof NotionalJourneyBallastBonusTerm term) {
			if (term.isPriceOnLastLNGPrice()) {
				return "Voyage priced only on LNG base-fuel equivalent quantity for the notional speed using the last sales price."; 
			}
			return "Voyage priced only on base-fuel quantity for the notional speed using specified curve.";
		}

		return null;
	}
}
