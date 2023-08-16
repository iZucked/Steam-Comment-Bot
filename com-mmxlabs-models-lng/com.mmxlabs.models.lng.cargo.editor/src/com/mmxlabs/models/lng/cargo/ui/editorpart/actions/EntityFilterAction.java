package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.cargo.ui.filters.EMFPathFilter;
import com.mmxlabs.models.lng.cargo.ui.filters.PairUnionFilter;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.emfpath.IEMFPath;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class EntityFilterAction extends EMFPathFilterAction {
	private static final IEMFPath LOAD_PATH = new TradesRowEMFPath(true, false, TradesRowEMFPath.Type.LOAD, CargoPackage.Literals.SLOT___GET_SLOT_OR_DELEGATE_ENTITY);
	private static final IEMFPath DISCHARGE_PATH = new TradesRowEMFPath(true, false, TradesRowEMFPath.Type.DISCHARGE, CargoPackage.Literals.SLOT___GET_SLOT_OR_DELEGATE_ENTITY);

	public EntityFilterAction(StructuredViewerFilterManager filterManager, CommercialModel sourceObject) {
		super(filterManager, "Entities", sourceObject, CommercialPackage.Literals.COMMERCIAL_MODEL__ENTITIES, LOAD_PATH);
	}

	@Override
	protected Action createAction(final NamedObject value) {
		ViewerFilter filter = new PairUnionFilter(//
				new EMFPathFilter(LOAD_PATH, value), //
				new EMFPathFilter(DISCHARGE_PATH, value)//
		);

		if (!getFilterManager().containsFilter(EntityFilterAction.this.getText(), filter)) {
			return new Action(value.getName(), IAction.AS_CHECK_BOX) {
				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) == 0) {
						getFilterManager().addFilter(EntityFilterAction.this.getText(), filter);
					} else {
						getFilterManager().addFilterAsUnion(EntityFilterAction.this.getText(), filter);
					}
				}
			};
		} else {
			Action action = new Action(value.getName(), IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					getFilterManager().removeFilter(EntityFilterAction.this.getText(), filter, true);
				}
			};
			action.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
			return action;
		}
	}

}
