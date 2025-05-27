package com.okestro.resource.server.support.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
public class ServerActionJson extends ServerJson {
	private final ServerAction action;

	public ServerActionJson(ServerAction action, ObjectNode source) {
		super(source.toString());
		this.action = action;
	}
}
