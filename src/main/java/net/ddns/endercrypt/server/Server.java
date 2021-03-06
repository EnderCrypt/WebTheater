package net.ddns.endercrypt.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.websocket.CloseReason.CloseCodes;

import net.ddns.endercrypt.server.event.NetMessageType;
import net.ddns.endercrypt.server.map.Room;
import net.ddns.endercrypt.server.user.User;
import net.ddns.endercrypt.server.user.id.IdManager;
import net.ddns.endercrypt.server.user.id.impl.MapIdManager;

public class Server
{
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	static
	{
		scheduledExecutorService.scheduleAtFixedRate(new UpdateService(), 0, 100, TimeUnit.MILLISECONDS);
		scheduledExecutorService.scheduleAtFixedRate(new PingService(), 0, 5, TimeUnit.SECONDS);
	}

	public static final IdManager idManager = new MapIdManager();

	public static final List<User> unconnectedUsers = new ArrayList<>();

	public static final Room room = new Room();

	public static void announce(String message)
	{
		System.out.println("[GLOBAL MSG] " + message);
		room.announce(message);
	}

	private static class UpdateService implements Runnable
	{
		@Override
		public void run()
		{
			room.update();
		}
	}

	private static class PingService implements Runnable
	{
		@Override
		public void run()
		{
			unconnectedUsers.forEach(this::pingCheck);
			idManager.iterate().forEach(this::pingCheck);
		}

		private void pingCheck(User user)
		{
			if (user.isTimedOut())
			{
				user.disconnect(CloseCodes.VIOLATED_POLICY, "Ping timeout");
			}
		}
	}
}
