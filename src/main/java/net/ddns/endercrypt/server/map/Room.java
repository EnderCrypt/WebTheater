package net.ddns.endercrypt.server.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.ddns.endercrypt.server.user.User;
import net.ddns.endercrypt.server.user.event.NetMessageType;

public class Room
{
	private List<User> users = new ArrayList<>();
	private List<User> immutableList = Collections.unmodifiableList(users);

	public Room()
	{
		// TODO Auto-generated constructor stub
	}

	public boolean isUserInRoom(User user)
	{
		return users.contains(user);
	}

	public void announce(String message)
	{
		for (User user : users)
		{
			user.getUserEndpoint().send(NetMessageType.SERVER_MESSAGE, message);
		}
	}

	public void addUser(User user)
	{
		if (isUserInRoom(user))
			throw new IllegalStateException("User " + user + " already in room " + this);
		announce(user.getName() + " has entered the room!");
		users.add(user);
		// send JOIN's to this user
		StringBuilder sb = new StringBuilder();
		for (User other : users)
		{
			sb.append(other.getId()).append(":");
			sb.append(other.getName()).append(":");
			sb.append(other.position().getX()).append(":");
			sb.append(other.position().getY());
			sb.append('/');
		}
		if (sb.length() > 0)
		{
			sb.setLength(sb.length() - 1);
		}
		user.getUserEndpoint().send(NetMessageType.USER_JOIN, sb.toString());
		// send JOIN's to all users in room
		for (User other : users)
		{
			other.getUserEndpoint().send(NetMessageType.USER_JOIN, String.valueOf(user.getId()));
		}
	}

	public void removeUser(User user)
	{
		if (users.remove(user) == false)
		{
			throw new IllegalStateException("User " + user + " was not in room " + this);
		}
		// send JOIN's to all users in room
		for (User other : users)
		{
			other.getUserEndpoint().send(NetMessageType.USER_LEAVE, String.valueOf(user.getId()));
		}
		announce(user.getName() + " has left the room!");
	}

	public List<User> listUsers()
	{
		return immutableList;
	}

}
