package com.mmxlabs.lngdataserver.distances.ui.lng.importer.tests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.integration.distances.Via;
import com.mmxlabs.lngdataserver.lng.importers.distances.PortAndDistancesToScenarioCopier;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.impl.LocationImpl;
import com.mmxlabs.models.lng.port.impl.PortImpl;
import com.mmxlabs.models.lng.port.provider.PortItemProviderAdapterFactory;

public class CopyDistancesToScenarioTests {

	@Test
	public void testCopyDistances() {

		final List<Port> ports = new ArrayList<>();
		final Port a = Mockito.mock(Port.class);
		final Location l_a = Mockito.mock(Location.class);
		Mockito.when(a.getLocation()).thenReturn(l_a);
		Mockito.when(l_a.getMmxId()).thenReturn("a");
		ports.add(a);

		final Port b = Mockito.mock(Port.class);
		final Location l_b = Mockito.mock(Location.class);
		Mockito.when(b.getLocation()).thenReturn(l_b);
		Mockito.when(l_b.getMmxId()).thenReturn("b");
		ports.add(b);

		final Port c = Mockito.mock(Port.class);
		final Location l_c = Mockito.mock(Location.class);
		Mockito.when(c.getLocation()).thenReturn(l_c);
		Mockito.when(l_c.getMmxId()).thenReturn("c");
		ports.add(c);

		final List<Route> routes = new ArrayList<>();
		final Route rd = PortFactory.eINSTANCE.createRoute();
		rd.setRouteOption(RouteOption.DIRECT);
		routes.add(rd);

		final Route rp = PortFactory.eINSTANCE.createRoute();
		rp.setRouteOption(RouteOption.PANAMA);
		routes.add(rp);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		portModel.getPorts().addAll(ports);
		portModel.getRoutes().addAll(routes);
		final EditingDomain ed = createLocalEditingDomain(portModel);

		final IDistanceProvider dp = Mockito.mock(IDistanceProvider.class);
		final Set<@NonNull String> knownLocations = CollectionsUtil.makeHashSet(a.getLocation().getMmxId(), b.getLocation().getMmxId(), c.getLocation().getMmxId());
		when(dp.getKnownPorts()).thenReturn(knownLocations);

		when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(1);
		when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(2);
		when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(3);
		when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(4);
		when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(5);
		when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(6);
		when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0);

		when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(7);
		when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(8);
		when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(9);
		when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(10);
		when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(11);
		when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(12);
		when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);

		final PortAndDistancesToScenarioCopier copier = new PortAndDistancesToScenarioCopier();
		final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = null;// copier.getUpdateDistancesCommand(ed,null,  dp, portModel);

		Assert.assertNotNull(updateDistancesCommand.getFirst());
		Assert.assertTrue(updateDistancesCommand.getFirst().canExecute());
		ed.getCommandStack().execute(updateDistancesCommand.getFirst());

		Assert.assertEquals(6, portModel.getRoutes().get(0).getLines().size()); // i.e. no RouteLine for identity routes
		Assert.assertEquals(6, portModel.getRoutes().get(1).getLines().size());

		final Optional<RouteLine> potential = portModel.getRoutes().get(1).getLines().stream().filter(e -> {
			return Objects.equals(e.getFrom().getLocation().getMmxId(), a.getLocation().getMmxId()) && Objects.equals(e.getTo().getLocation().getMmxId(), b.getLocation().getMmxId());
		}).findFirst();
		Assert.assertTrue(potential.isPresent());
		Assert.assertEquals(7, potential.get().getDistance());
	}

	@Test
	public void lostRouteLinesAfterUpdateTest() {

		final List<Port> ports = new ArrayList<>();
		final Port a = Mockito.mock(PortImpl.class);
		final Location l_a = Mockito.mock(Location.class);

		Mockito.when(a.getLocation()).thenReturn(l_a);
		when(a.getName()).thenReturn("a");
		when(l_a.getMmxId()).thenReturn("a");
		ports.add(a);

		final Port b = Mockito.mock(PortImpl.class);
		final Location l_b = Mockito.mock(Location.class);
		Mockito.when(b.getLocation()).thenReturn(l_b);
		when(b.getName()).thenReturn("b");
		when(l_b.getMmxId()).thenReturn("b");
		ports.add(b);

		final Port c = Mockito.mock(PortImpl.class);
		final Location l_c = Mockito.mock(Location.class);
		Mockito.when(c.getLocation()).thenReturn(l_c);
		when(c.getName()).thenReturn("c");
		when(l_c.getMmxId()).thenReturn("c");
		ports.add(c);

		final List<Route> routes = new ArrayList<>();
		final Route rd = PortFactory.eINSTANCE.createRoute();
		rd.setRouteOption(RouteOption.DIRECT);
		routes.add(rd);

		final Route rp = PortFactory.eINSTANCE.createRoute();
		rp.setRouteOption(RouteOption.PANAMA);
		routes.add(rp);

		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();
		final EditingDomain ed = createLocalEditingDomain(portModel);

		portModel.getPorts().addAll(ports);
		portModel.getRoutes().addAll(routes);

		final IDistanceProvider dp = Mockito.mock(IDistanceProvider.class);
		final Set<@NonNull String> knownLocations = CollectionsUtil.makeHashSet(a.getLocation().getMmxId(), b.getLocation().getMmxId(), c.getLocation().getMmxId());
		when(dp.getKnownPorts()).thenReturn(knownLocations);

		when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(1);
		when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(2);
		when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(3);
		when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(4);
		when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(5);
		when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(6);
		when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0);

		when(dp.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(7);
		when(dp.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(8);
		when(dp.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(9);
		when(dp.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(10);
		when(dp.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(11);
		when(dp.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(12);
		when(dp.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);

		final PortAndDistancesToScenarioCopier copier = new PortAndDistancesToScenarioCopier();
		final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesCommand = null;// copier.getUpdateDistancesCommand(ed, dp, portModel);
		updateDistancesCommand.getFirst().execute();

		final IDistanceProvider dp2 = Mockito.mock(IDistanceProvider.class);
		when(dp2.getKnownPorts()).thenReturn(knownLocations);

		when(dp2.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp2.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(13);
		when(dp2.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(14);
		when(dp2.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(15);
		when(dp2.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(0);
		when(dp2.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(16);
		when(dp2.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.Direct)).thenReturn(Integer.MAX_VALUE);
		when(dp2.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.Direct)).thenReturn(18);
		when(dp2.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.Direct)).thenReturn(0);

		when(dp2.getDistance(a.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp2.getDistance(a.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(19);
		when(dp2.getDistance(a.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(20);
		when(dp2.getDistance(b.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(21);
		when(dp2.getDistance(b.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);
		when(dp2.getDistance(b.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(22);
		when(dp2.getDistance(c.getLocation().getMmxId(), a.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(23);
		when(dp2.getDistance(c.getLocation().getMmxId(), b.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(Integer.MAX_VALUE);
		when(dp2.getDistance(c.getLocation().getMmxId(), c.getLocation().getMmxId(), Via.PanamaCanal)).thenReturn(0);

		final Pair<Command, Map<RouteOption, List<RouteLine>>> updateDistancesIncomplete = null;//copier.getUpdateDistancesCommand(ed, dp2, portModel);
		Assert.assertNotNull(updateDistancesIncomplete.getFirst());
		final boolean canExecute = updateDistancesIncomplete.getFirst().canExecute();
		Assert.assertTrue(canExecute);
		Assert.assertEquals(1, updateDistancesIncomplete.getSecond().get(RouteOption.DIRECT).size());
		Assert.assertEquals(5, updateDistancesIncomplete.getSecond().get(RouteOption.DIRECT).get(0).getDistance());

		Assert.assertEquals(12, updateDistancesIncomplete.getSecond().get(RouteOption.PANAMA).get(0).getDistance());
		updateDistancesIncomplete.getFirst().execute();
	}

	@NonNull
	public static EditingDomain createLocalEditingDomain(final EObject rootObject) {
		final BasicCommandStack commandStack = new BasicCommandStack();
		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new PortItemProviderAdapterFactory());
		final EditingDomain ed = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
		final ResourceImpl r = new ResourceImpl();

		ed.getResourceSet().getResources().add(r);
		r.getContents().add(rootObject);
		return ed;
	}
}
