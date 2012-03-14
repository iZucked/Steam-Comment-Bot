/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class EntityEditorPane extends ScenarioTableViewerPane {
	public EntityEditorPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");
		
		addTypicalColumn("Is Shipping", new ShippingEntityManipulator());
		
		defaultSetTitle("Entities");
	}
	
	class ShippingEntityManipulator implements ICellRenderer, ICellManipulator {
		@Override
		public void setValue(Object object, Object value) {
			if (object instanceof LegalEntity) {
				final LegalEntity settings = (LegalEntity) object;
				if (settings.eContainer() instanceof CommercialModel) {
					final CommercialModel cm = (CommercialModel) settings.eContainer();
					getEditingDomain().getCommandStack().execute(
							SetCommand.create(getEditingDomain(), cm, CommercialPackage.eINSTANCE.getCommercialModel_ShippingEntity(), settings));
				}
			}
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			return new ComboBoxCellEditor(parent, new String[] { "Y", "N" });
		}

		@Override
		public Object getValue(Object object) {
			if (object instanceof LegalEntity) {
				final LegalEntity settings = (LegalEntity) object;
				if (((CommercialModel) settings.eContainer()).getShippingEntity() == settings)
					return 0;
			}
			return 1;
		}

		@Override
		public boolean canEdit(Object object) {
			return true;
		}

		@Override
		public String render(Object object) {
			return ((Integer) getValue(object)) == 0 ? "Y" : "N";
		}

		@Override
		public Comparable getComparable(Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
			return Collections.emptySet();
		}
	}
}
