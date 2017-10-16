package net.ddns.endercrypt.server.map.tileset;

public class TilesetInfo
{
	private int count = 0;
	private int width = 0;
	private int height = 0;

	public TilesetInfo(int count, int width, int height)
	{
		this.count = count;
		this.width = width;
		this.height = height;
	}

	public int getCount()
	{
		return count;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public Object serializable()
	{
		return new Object()
		{
			@SuppressWarnings({ "unused", "hiding" })
			public int count = getCount();

			@SuppressWarnings({ "unused", "hiding" })
			public int width = getHeight();

			@SuppressWarnings({ "unused", "hiding" })
			public int height = getWidth();
		};
	}
}
