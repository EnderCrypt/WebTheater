package net.ddns.endercrypt.web.socket;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import net.ddns.endercrypt.server.Server;
import net.ddns.endercrypt.server.user.User;

public class WsTheaterEndpoint extends Endpoint
{
	private UserEndpoint userEndpoint;

	@Override
	public void onOpen(Session session, EndpointConfig config)
	{
		System.out.println("Recieving connection: " + session.getId());
		userEndpoint = new UserEndpoint(session);
		User user = new User(userEndpoint);
		Server.unconnectedUsers.add(user);
	}

	@Override
	public void onClose(Session session, CloseReason closeReason)
	{
		if (session.equals(userEndpoint.getSession()) == false)
			throw new AssertionError();
		userEndpoint.getUserEndpointObject().onDisconnect(closeReason);
	}
}
