package scenario.presentation.cargoeditor.autocorrect;

import java.util.Date;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import scenario.presentation.cargoeditor.autocorrect.AutoCorrector.ICorrector;

import com.mmxlabs.common.Pair;

/**
 * Ensure slot load date is not too close to unload date
 * 
 * TODO incorporate travel time.
 * 
 * @author Tom Hinton
 */
public class SlotDateOrderCorrector implements ICorrector {
	final EAttribute slotWindowStart = CargoPackage.eINSTANCE
			.getSlot_WindowStart();

	final EReference cargoLoadSlot = CargoPackage.eINSTANCE.getCargo_LoadSlot();
	final EReference cargoDischargeSlot = CargoPackage.eINSTANCE
			.getCargo_DischargeSlot();

	@Override
	public Pair<String, Command> correct(final Notification notification,
			final EditingDomain editingDomain) {

		final Object feature = notification.getFeature();
		if (notification.getEventType() == Notification.SET
				&& slotWindowStart.equals(feature)) {
			final Slot slot = (Slot) (notification.getNotifier());
			final EReference containment = slot.eContainmentFeature();

			if (cargoLoadSlot.equals(containment)) {
				final Cargo cargo = (Cargo) slot.eContainer();
				if (cargo.getDischargeSlot() != null) {
					final Date latestDischargeDate = cargo.getDischargeSlot().getWindowEnd();
					final Date earliestLoadDate = slot.getWindowStart();
					if (latestDischargeDate == null || earliestLoadDate == null) return null;
					if (latestDischargeDate.before(earliestLoadDate)) {
						// push discharge date forwards; new discharge date is
						// load start - discharge window length - delta
					}
				}
			} else if (cargoDischargeSlot.equals(containment)) {
				final Cargo cargo = (Cargo) slot.eContainer();
				if (cargo.getLoadSlot() != null) {
					final Date latestDischargeDate = slot.getWindowEnd();
					final Date earliestLoadDate = cargo.getLoadSlot().getWindowStart();
					if (latestDischargeDate == null || earliestLoadDate == null) return null;
					if (latestDischargeDate.before(earliestLoadDate)) {
						// push load date backwards
						// new load date is discharge end - delta
					}
				}
			}
		}

		return null;
	}

}
