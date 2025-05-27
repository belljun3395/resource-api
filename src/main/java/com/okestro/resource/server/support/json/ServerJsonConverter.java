package com.okestro.resource.server.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.okestro.resource.server.domain.model.flavor.Flavor;
import com.okestro.resource.server.domain.model.instance.Instance;
import com.okestro.resource.server.domain.vo.ImageSource;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerJsonConverter {
	private static final String ACTION_KEY = "@action";
	private static final String TYPE_KEY = "@type";

	private final ObjectMapper objectMapper;

	public ServerActionJson toJson(
			ServerAction action, Instance instance, Flavor flavor, ImageSource imageSource) {
		ObjectNode json = objectMapper.createObjectNode();
		json.put(ACTION_KEY, action.name());

		if (Objects.isNull(instance)) {
			throw new IllegalArgumentException("Instance must not be null");
		}
		ObjectNode instanceNode = objectMapper.valueToTree(instance);
		instanceNode.put(TYPE_KEY, instance.getClass().getSimpleName());
		json.set("instance", instanceNode);

		if (!Objects.isNull(flavor)) {
			ObjectNode flavorNode = objectMapper.valueToTree(flavor);
			flavorNode.put(TYPE_KEY, flavor.getClass().getSimpleName());
			json.set("flavor", flavorNode);
		}

		if (!Objects.isNull(imageSource)) {
			ObjectNode sourceNode = objectMapper.valueToTree(imageSource);
			sourceNode.put(TYPE_KEY, imageSource.getClass().getSimpleName());
			json.set("imageSource", sourceNode);
		}

		return new ServerActionJson(action, json);
	}

	public ServerActionJson toJson(ServerAction action, Instance instance) {
		return toJson(action, instance, null, null);
	}

	public ServerActionJson toJson(ServerAction action, Instance instance, Flavor flavor) {
		return toJson(action, instance, flavor, null);
	}

	public ServerActionJson toJson(ServerAction action, Instance instance, ImageSource imageSource) {
		return toJson(action, instance, null, imageSource);
	}
}
