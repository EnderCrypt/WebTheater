package net.ddns.endercrypt.server.user.id;

import java.util.Iterator;

import net.ddns.endercrypt.server.user.User;

public class ArrayIdManager implements IdManager
{
	private User[] users;

	private int highestId;

	public ArrayIdManager(int maxUsers)
	{
		users = new User[maxUsers];
	}

	@Override
	public int giveId(User user)
	{
		for (int i = 0; i < users.length; i++)
		{
			if (users[i] == null)
			{
				users[i] = user;
				if (i > highestId)
				{
					highestId = i;
				}
				return i;
			}
		}
		throw new UserLimitReached();
	}

	@Override
	public User get(int id)
	{
		return users[id];
	}

	@Override
	public void remove(int id)
	{
		users[id] = null;
		while (users[highestId] == null)
		{
			if (highestId == 0)
			{
				break;
			}
			highestId--;
		}
	}

	@Override
	public Iterable<User> iterate()
	{
		return new Iterable<User>()
		{
			@Override
			public Iterator<User> iterator()
			{
				return new Iterator<User>()
				{
					private int id = -1;

					private User knownNext = null;

					private void locate()
					{
						if (knownNext == null)
						{
							for (int i = id + 1; i < highestId; i++)
							{
								if (users[i] != null)
								{
									knownNext = users[i];
									id = i;
									return;
								}
							}
						}
					}

					@Override
					public boolean hasNext()
					{
						if (knownNext != null)
							return true;

						locate();

						return (knownNext != null);
					}

					@Override
					public User next()
					{
						User value = knownNext;
						locate();
						return value;
					}
				};
			}
		};
	}
}
