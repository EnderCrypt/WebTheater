package net.ddns.endercrypt.web.socket;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class UserEndpoint
{
	private Session session;
	private UserEndpointObject userEndpointObject;
	private LocalEndpoint localEndpoint = new LocalEndpoint();

	public UserEndpoint(Session session)
	{
		this.session = session;
		getSession().addMessageHandler(localEndpoint);
	}

	public void bind(UserEndpointObject userEndpointObject)
	{
		this.userEndpointObject = userEndpointObject;
	}

	public Session getSession()
	{
		return session;
	}

	public UserEndpointObject getUserEndpointObject()
	{
		return userEndpointObject;
	}

	public Future<Void> send(int messageType, String message)
	{
		return getSession().getAsyncRemote().sendText(messageType + ":" + message);
	}

	public void disconnect(CloseReason closeReason)
	{
		try
		{
			session.close(closeReason);
		}
		catch (IOException e)
		{
			System.err.println("Failed to cleanly close connection");
			e.printStackTrace();
		}
	}

	protected class LocalEndpoint extends Endpoint implements MessageHandler.Whole<String>
	{
		@Override
		public void onOpen(Session session, EndpointConfig config)
		{
			throw new AssertionError("This shouldnt get called");
			// this wont get called anyways, so ignore
		}

		@Override
		public void onMessage(String message)
		{
			//System.out.println("[RAW RECIEVE] " + message);
			userEndpointObject.onMessage(message);
		}

		@Override
		public void onClose(Session session, CloseReason closeReason)
		{
			System.out.println("LocalEndpoint.onClose");
			//userEndpointObject.onDisconnect(closeReason);
		}
	}
}
