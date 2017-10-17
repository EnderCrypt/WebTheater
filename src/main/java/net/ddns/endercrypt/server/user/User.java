package net.ddns.endercrypt.server.user;

import net.ddns.endercrypt.web.socket.UserEndpointObject;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.CloseReason.CloseCodes;

import net.ddns.endercrypt.server.Server;
import net.ddns.endercrypt.server.user.event.NetMessageType;
import net.ddns.endercrypt.server.user.event.UserEventManager;
import net.ddns.endercrypt.server.user.event.WsEventListener;
import net.ddns.endercrypt.web.socket.UserEndpoint;

public class User implements UserEndpointObject
{
	private static final long PING_TIMEOUT = TimeUnit.SECONDS.toMillis(1);
	private static final UserEventManager<User> userEventManager = new UserEventManager<>(User.class, 100);

	private UserEndpoint userEndpoint;
	private UserState userState = UserState.UNCONNECTED;

	private int id;
	private String name;
	private Point position = new Point(0, 0);
	private long lastMessageRecieved;

	public User(UserEndpoint userEndpoint)
	{
		this.userEndpoint = userEndpoint;
		userEndpoint.bind(this);
		lastMessageRecieved = System.currentTimeMillis();
	}

	@Override
	public void onMessage(String message)
	{
		// find delimiter
		int delimiterIndex = message.indexOf(':');
		if (delimiterIndex == -1)
		{
			disconnect(CloseCodes.UNEXPECTED_CONDITION, "expected a ':'");
			return;
		}
		// register message (ping)
		lastMessageRecieved = System.currentTimeMillis();
		// extract message type
		int messageType = -1;
		try
		{
			messageType = Integer.parseInt(message.substring(0, delimiterIndex));
		}
		catch (NumberFormatException e)
		{
			disconnect(CloseCodes.UNEXPECTED_CONDITION, "expected a message type in the beginning of the message");
			return;
		}
		// extract data
		String data = message.substring(delimiterIndex + 1);
		// trigger event
		try
		{
			userEventManager.activateEvent(this, messageType, data);
		}
		catch (ReflectiveOperationException e)
		{
			errorDisconnect(e);
		}
	}

	@WsEventListener(NetMessageType.ENDPOINT_LOGIN)
	private void ENDPOINT_LOGIN(String data)
	{
		if (userState != UserState.UNCONNECTED)
		{
			errorDisconnect(new IllegalStateException("Already logged in"));
			return;
		}
		name = data;
		for (char c : name.toCharArray())
		{
			c = Character.toLowerCase(c);
			if ((c < 'a') || (c > 'z'))
			{
				throw new IllegalArgumentException("Name contains illegal characters: " + c);
			}
		}
		id = Server.idManager.giveId(this);
		getUserEndpoint().send(NetMessageType.ENDPOINT_LOGIN, String.valueOf(id));
		Server.announce("User joined: " + getName() + " (id: " + id + ")");
		Server.unconnectedUsers.remove(this);
		Server.room.addUser(this);
	}

	public void errorDisconnect(Throwable throwable)
	{
		throwable.printStackTrace();
		disconnect(CloseCodes.VIOLATED_POLICY, throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
	}

	public void disconnect(CloseCode closeCode, String reason)
	{
		CloseReason closeReason = new CloseReason(closeCode, reason);
		System.out.println("(Disconnect) sent to " + getName() + " (" + closeReason + ")");
		userEndpoint.disconnect(closeReason);
	}

	@Override
	public void onDisconnect(CloseReason closeReason)
	{
		// remove user
		Server.unconnectedUsers.remove(this);
		// TODO: remove user from all rooms
		// free up id
		Server.idManager.remove(getId());
		// disconnnect message
		StringBuilder sb = new StringBuilder();
		if (getName() == null)
			sb.append("Unconnected user");
		else
			sb.append("User ").append(getName());

		sb.append(" Disconnected, code: ").append(closeReason.getCloseCode().getCode());
		if (closeReason.getReasonPhrase().equals("") == false)
			sb.append(" reason: ").append(closeReason.getReasonPhrase());

		System.out.println(sb.toString());
	}

	public UserEndpoint getUserEndpoint()
	{
		return userEndpoint;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public Point position()
	{
		return position;
	}

	public boolean isTimedOut()
	{
		long time = System.currentTimeMillis();
		time = (time - lastMessageRecieved);
		return (time >= PING_TIMEOUT);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[Name=" + name + "]";
	}
}
