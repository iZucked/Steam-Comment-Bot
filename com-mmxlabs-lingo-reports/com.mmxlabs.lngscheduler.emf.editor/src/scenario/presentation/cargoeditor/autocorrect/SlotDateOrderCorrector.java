package scenario.presentation.cargoeditor.autocorrect;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.presentation.cargoeditor.autocorrect.AutoCorrector.ICorrector;

import com.mmxlabs.common.Pair;

public class SlotDateOrderCorrector implements ICorrector {

	@Override
	public Pair<String, Command> correct(Notification notification,
			EditingDomain editingDomain) {
		
		final Object feature = notification.getFeature();
		
		return null;
	}


}
