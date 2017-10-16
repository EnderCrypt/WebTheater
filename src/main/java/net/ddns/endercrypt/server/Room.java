package net.ddns.endercrypt.server;

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
	}

	public void removeUser(User user)
	{
		if (users.remove(user) == false)
		{
			throw new IllegalStateException("User " + user + " was not in room " + this);
		}
		announce(user.getName() + " has left the room!");
	}

	public List<User> listUsers()
	{
		return immutableList;
	}

}
