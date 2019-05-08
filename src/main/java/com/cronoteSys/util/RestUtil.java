package com.cronoteSys.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RestUtil {
	public boolean isConnectedToTheServer() {
		String response = response("http://localhost:8081/Test/webapi/myresource/connection").readEntity(String.class);
		if(response.equalsIgnoreCase("success")) {
			return true;
		}
		return false;
	}
	
	public Response response(String link) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(link);
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		return response;
	}
}
