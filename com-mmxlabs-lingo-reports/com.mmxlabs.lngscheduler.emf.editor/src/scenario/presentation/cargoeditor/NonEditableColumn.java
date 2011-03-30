package scenario.presentation.cargoeditor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public abstract class NonEditableColumn implements ICellManipulator, ICellRenderer {

	@Override
	public Comparable getComparable(Object object) {
		return render(object);
	}

	@Override
	public void setValue(Object object, Object value) {

	}

	@Override
	public CellEditor getCellEditor(Composite parent, Object object) {
		return null;
	}

	@Override
	public Object getValue(Object object) {
		return null;
	}

	@Override
	public boolean canEdit(Object object) {
		return false;
	}

}
