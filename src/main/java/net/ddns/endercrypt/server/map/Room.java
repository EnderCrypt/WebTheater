package net.ddns.endercrypt.server.map;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.ddns.endercrypt.server.event.NetMessageType;
import net.ddns.endercrypt.server.user.User;

public class Room
{
	private List<User> users = new ArrayList<>();
	private List<User> immutableList = Collections.unmodifiableList(users);

	private Dimension roomSize = new Dimension(10, 10);
	private boolean youtube;
	private int[][] room;

	public Room()
	{
		youtube = false;
		room = new int[roomSize.width][roomSize.height];
		for (int y = 0; y < roomSize.width; y++)
		{
			for (int x = 0; x < roomSize.height; x++)
			{
				if ((x == 0) || (y == 0) || (x == roomSize.width) || (y == roomSize.height))
					room[x][y] = 30;
				else
					room[x][y] = 3;
			}
		}
	}

	public boolean isYoutube()
	{
		return youtube;
	}

	public boolean containsUser(User user)
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

	public JSONObject getRoomJson()
	{
		JSONObject json = new JSONObject();
		json.put("width", roomSize.width);
		json.put("height", roomSize.height);
		json.put("youtube", isYoutube());
		JSONArray tiles = new JSONArray();
		for (int y = 0; y < roomSize.width; y++)
		{
			for (int x = 0; x < roomSize.height; x++)
			{
				tiles.put(room[x][y]);
			}
		}
		json.append("tiles", tiles);
		return json;
		/*
		StringBuilder sb = new StringBuilder();
		sb.append(roomSize.width).append(',');
		sb.append(roomSize.height).append(',');
		sb.append(0); // IS YOUTUBE ROOM OR NOT
		sb.append(':');
		for (int y = 0; y < roomSize.width; y++)
		{
			for (int x = 0; x < roomSize.height; x++)
			{
				sb.append(String.valueOf(room[x][y])).append(',');
			}
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
		*/
	}

	public void addUser(User user)
	{
		if (containsUser(user))
			throw new IllegalStateException("User " + user + " already in room " + this);
		announce(user.getName() + " has entered the room!");
		// send room data
		user.getUserEndpoint().send(NetMessageType.LOAD_ROOM, getRoomJson().toString());
		// add user
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
