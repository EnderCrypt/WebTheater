package net.ddns.endercrypt.server.map.tileset;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class Tileset
{
	private BufferedImage fullTileset;
	private TilesetInfo tilesetInfo;
	private Tile[] tileset;

	public Tileset(File imageFile, File solidityFile, Dimension tileSize) throws IOException
	{
		int width = tileSize.width;
		int height = tileSize.height;

		fullTileset = ImageIO.read(imageFile);
		int tilesx = fullTileset.getWidth() / width;
		int tilesy = fullTileset.getHeight() / height;
		int count = tilesx * tilesy;

		List<String> solidityList = Files.readAllLines(solidityFile.toPath());

		tileset = new Tile[count];
		int i = -1;
		for (int y = 0; y < tilesy; y++)
		{
			for (int x = 0; x < tilesx; x++)
			{
				i++;
				BufferedImage img = fullTileset.getSubimage(x * tileSize.width, y * tileSize.height, tileSize.width, tileSize.height);
				Solidity solidity = Solidity.valueOf(solidityList.get(i));
				tileset[i] = new Tile(img, solidity);
			}
		}

		tilesetInfo = new TilesetInfo(count, width, height);
	}

	public BufferedImage getFullTileset()
	{
		return fullTileset;
	}

	public TilesetInfo getTilesetInfo()
	{
		return tilesetInfo;
	}

	public Tile get(int index)
	{
		return tileset[index];
	}

	public List<Tile> getAll()
	{
		return Arrays.asList(tileset);
	}

	public class TilesetInfo
	{
		private int count = 0;
		private int width = 0;
		private int height = 0;
		private List<Integer> solid;

		public TilesetInfo(int count, int width, int height)
		{
			this.count = count;
			this.width = width;
			this.height = height;
			solid = getAll().stream().map(t -> t.getSolidity().ordinal()).collect(Collectors.toList());
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

		public List<Integer> getSolid()
		{
			return solid;
		}
	}
}
