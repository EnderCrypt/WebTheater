package net.ddns.endercrypt.server.user.id;

import net.ddns.endercrypt.server.user.User;

public interface IdManager
{
	public int giveId(User user);

	public User get(int id);

	public void remove(int id);

	public Iterable<User> iterate();
}
