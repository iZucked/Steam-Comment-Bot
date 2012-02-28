package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.DialogFeatureManipulator;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;

public class IndexEditorPane extends ScenarioTableViewerPane {
	public IndexEditorPane(final IWorkbenchPage page, final JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(Object object) {
				if (object instanceof DerivedIndex) {
					return "Expression";
				} else {
					return "Data";
				}
			}
		});
		addNameManipulator("Name");
		
		addTypicalColumn("Content", new IndexValueManipulator());
		
		defaultSetTitle("Indices");
	}
	
	private class IndexValueManipulator implements ICellRenderer, ICellManipulator {
		private BasicAttributeManipulator expressionManipulator = 
				new BasicAttributeManipulator(
						PricingPackage.eINSTANCE.getDerivedIndex_Expression(),
						getEditingDomain());
		
		private BasicAttributeManipulator dataManipulator = 
				new DialogFeatureManipulator(PricingPackage.eINSTANCE.getDataIndex_Points(), getEditingDomain()) {
					
					@Override
					protected String renderValue(Object value) {
						if (value == null) return "";
						return ((DataIndex) value).getPoints().size() + " points";
					}
					
					@Override
					protected Object openDialogBox(Control cellEditorWindow, Object object) {
						return null;
					}
				};
		
		private BasicAttributeManipulator pick(final Object object) {
			if (object instanceof DataIndex) return dataManipulator;
			else return expressionManipulator;
		}
		
		@Override
		public void setValue(Object object, Object value) {
			pick(object).setValue(object, value);
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			return pick(object).getCellEditor(parent, object);
		}

		@Override
		public Object getValue(Object object) {
			return pick(object).getValue(object);
		}

		@Override
		public boolean canEdit(Object object) {
			return pick(object).canEdit(object);
		}

		@Override
		public String render(Object object) {
			return pick(object).render(object);
		}

		@Override
		public Comparable getComparable(Object object) {
			return pick(object).getComparable(object);
		}

		@Override
		public Object getFilterValue(Object object) {
			return pick(object).getFilterValue(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(
				Object object) {
			return pick(object).getExternalNotifiers(object);
		}
		
	}
}
