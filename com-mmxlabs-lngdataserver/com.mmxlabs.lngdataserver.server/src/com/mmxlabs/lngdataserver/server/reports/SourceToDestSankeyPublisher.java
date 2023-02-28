package com.mmxlabs.lngdataserver.server.reports;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.server.api.IReportPublisher;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class SourceToDestSankeyPublisher implements IReportPublisher {

	@Override
	public String getReportName() {
		return "sourcetodestcounter";
	}

	@Override
	public String getJSONData(ScenarioResult sr, ServletRequest req) throws Exception {

		final ScheduleModel scheduleModel = sr.getTypedResult(ScheduleModel.class);

		JSONArray data = new JSONArray();
		{
			JSONArray row = new JSONArray();
			{
				JSONObject a = new JSONObject();
				a.put("label", "From");
				a.put("type", "string");
				row.put(a);
			}
			{
				JSONObject a = new JSONObject();
				a.put("label", "To");
				a.put("type", "string");
				row.put(a);
			}
			 
			{
				JSONObject a = new JSONObject();
				a.put("label", "Count");
				a.put("type", "number");
				row.put(a);
			}
			data.put(row);
		}

		Map<Pair<Port, Port>, Integer> m = new HashMap<>();

		for (CargoAllocation ca : scheduleModel.getSchedule().getCargoAllocations()) {

			Pair<Port, Port> p = Pair.of(ca.getSlotAllocations().get(0).getPort(), ca.getSlotAllocations().get(1).getPort());
			if (p.getFirst() == p.getSecond()) {
				continue;
			}
			m.merge(p, ca.getSlotAllocations().get(0).getVolumeTransferred(), Integer::sum);
		}
		for (var e : m.entrySet()) {
			JSONArray row = new JSONArray();
			row.put(e.getKey().getFirst().getName());
			row.put(e.getKey().getSecond().getName());
			row.put(e.getValue().toString());
			data.put(row);
		}
		return data.toString();
	}
}
