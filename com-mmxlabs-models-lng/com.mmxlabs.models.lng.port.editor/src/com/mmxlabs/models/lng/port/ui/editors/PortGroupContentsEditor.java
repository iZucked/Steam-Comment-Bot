package com.mmxlabs.models.lng.port.ui.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.provider.PortItemProviderAdapterFactory;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.models.ui.editors.util.CommandUtil;

/**
 * Editor for port groups which displays as a ticklist (maybe with categories?)
 * 
 * @author hinton
 *
 */
public class PortGroupContentsEditor extends BasicAttributeInlineEditor {
	private CheckboxTableViewer viewer;

	public PortGroupContentsEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(Composite parent) {
		viewer = CheckboxTableViewer.newCheckList(parent, SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new AdapterFactoryContentProvider(new PortItemProviderAdapterFactory())
		{

			@Override
			public Object[] getElements(Object object) {
				final ArrayList<Object> result = new ArrayList<Object>();
				for (final Object o : super.getElements(object)) {
					if (o instanceof Port && o != input) {
						result.add(o);
					}
				}
				
				Collections.sort(result, (Comparator) new Comparator<NamedObject>() {
					@Override
					public int compare(NamedObject arg0, NamedObject arg1) {
						if (arg0 == null) {
							if (arg1 == null) {
								return 0;
							} else {
								return 1;
							}
						} else if (arg1 == null) {
							return -1;
						}
						return arg0.getName().compareTo(arg1.getName());
					}
				});
				
				return result.toArray();
			}

		});
		
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				// todo label groups differently?
				if (element instanceof NamedObject) return ((NamedObject) element).getName();
				return super.getText(element);
			}
		});
		viewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				return element == input;
			}
			
			@Override
			public boolean isChecked(Object element) {
				final Object value = getValue();
				if (value instanceof Collection) return ((Collection) value).contains(element);
				return false;
			}
		});
		
		viewer.addCheckStateListener(new ICheckStateListener() {			
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
				final ArrayList<Object> c = new ArrayList<Object>((Collection) getValue());
				
				if (event.getChecked()) {
					c.add(element);
				} else {
					c.remove(element);
				}
				
				doSetValue(c, false);
			}
		});
		
		return wrapControl(viewer.getControl());
	}
	
	@Override
	protected Command createSetCommand(Object value) {
		final CompoundCommand setter = CommandUtil
				.createMultipleAttributeSetter(
						commandHandler.getEditingDomain(), input, feature,
						(Collection) value);
		return setter;
	}

	PortModel pm = null;
	
	@Override
	public void display(MMXRootObject context, EObject input,
			Collection<EObject> range) {
		pm = context.getSubModel(PortModel.class);
		super.display(context, input, range);
	}

	@Override
	protected void updateDisplay(Object value) {
		viewer.setInput(pm);
		viewer.refresh();
	}
	

	@Override
	public void setEnabled(final boolean enabled) {

		viewer.getControl().setEnabled(enabled);
		
		super.setEnabled(enabled);
	}
}
