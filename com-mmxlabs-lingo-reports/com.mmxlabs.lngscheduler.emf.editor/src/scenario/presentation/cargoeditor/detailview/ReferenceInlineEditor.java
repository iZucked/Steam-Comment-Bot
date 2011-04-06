/**
 * 
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.IReferenceValueProvider;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * @author hinton
 * 
 */
public class ReferenceInlineEditor extends UnsettableInlineEditor {
	private Combo combo;
	private IReferenceValueProvider valueProvider;

	private final ArrayList<String> nameList = new ArrayList<String>();
	private final ArrayList<EObject> valueList = new ArrayList<EObject>();

	public ReferenceInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain,
			final IReferenceValueProvider valueProvider) {
		super(path, feature, editingDomain);
		this.valueProvider = valueProvider;
	}

	@Override
	public Control createValueControl(final Composite parent) {
		final Combo combo = new Combo(parent, SWT.READ_ONLY);
		this.combo = combo;

		combo.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				combo.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						combo.removeSelectionListener(sl);
					}
				});
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetValue(valueList.get(nameList.indexOf(combo.getText())));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		return combo;
	}

	@Override
	public void setInput(EObject source) {
		super.setInput(source);
	}

	@Override
	protected void updateControl() {
		final List<Pair<String, EObject>> values = valueProvider
				.getAlloweValues(input, feature);
		// update combo contents
		combo.removeAll();
		nameList.clear();
		valueList.clear();

		for (final Pair<String, EObject> object : values) {
			valueList.add(object.getSecond());
			nameList.add(object.getFirst());
			combo.add(object.getFirst());
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		final int curIndex = valueList.indexOf(value);
		if (curIndex == -1) combo.setText(""); 
		else combo.setText(nameList.get(curIndex));
	}
}
