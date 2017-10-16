package net.ddns.endercrypt.server.map.tileset;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;

public class Tileset
{
	private TilesetInfo tilesetInfo;
	private Tile[] tileset;

	public Tileset(File imageFile, File solidityFile, Dimension tileSize) throws IOException
	{
		int width = tileSize.width;
		int height = tileSize.height;

		BufferedImage image = ImageIO.read(imageFile);
		int tilesx = image.getWidth() / width;
		int tilesy = image.getHeight() / height;
		int count = tilesx * tilesy;

		tilesetInfo = new TilesetInfo(count, width, height);

		List<String> solidityList = Files.readAllLines(solidityFile.toPath());

		tileset = new Tile[tilesetInfo.getCount()];
		int i = -1;
		for (int y = 0; y < tilesy; y++)
		{
			for (int x = 0; x < tilesx; x++)
			{
				i++;
				BufferedImage img = image.getSubimage(x * tileSize.width, y * tileSize.height, tileSize.width, tileSize.height);
				Solidity solidity = Solidity.valueOf(solidityList.get(i));
				tileset[i] = new Tile(img, solidity);
			}
		}
	}

	public TilesetInfo getTilesetInfo()
	{
		return tilesetInfo;
	}

	public Tile get(int index)
	{
		return tileset[index];
	}
}
