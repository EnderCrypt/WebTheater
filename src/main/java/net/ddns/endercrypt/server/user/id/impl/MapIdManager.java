package net.ddns.endercrypt.server.user.id.impl;

import java.util.HashMap;
import java.util.Map;

import net.ddns.endercrypt.server.user.User;
import net.ddns.endercrypt.server.user.id.IdManager;

public class MapIdManager implements IdManager
{
	private Map<Integer, User> users = new HashMap<>();

	public MapIdManager()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public int giveId(User user)
	{
		int id = 0;
		while (users.containsKey(id))
		{
			id++;
		}
		users.put(id, user);
		return id;
	}

	@Override
	public User get(int id)
	{
		return users.get(id);
	}

	@Override
	public void remove(int id)
	{
		users.remove(id);
	}

	@Override
	public Iterable<User> iterate()
	{
		return users.values();
	}

}
