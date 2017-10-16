package net.ddns.endercrypt.server;

import java.util.ArrayList;
import java.util.List;
import net.ddns.endercrypt.server.user.User;
import net.ddns.endercrypt.server.user.id.ArrayIdManager;
import net.ddns.endercrypt.server.user.id.IdManager;

public class Server
{
	public static final IdManager idManager = new ArrayIdManager(1000);

	public static final List<User> unconnectedUsers = new ArrayList<>();

	public static final Room room = new Room();

	public static void announce(String message)
	{
		System.out.println("[GLOBAL MSG] " + message);
		room.announce(message);
	}
}
