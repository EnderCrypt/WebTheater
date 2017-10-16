package net.ddns.endercrypt.server.map.tileset;

import java.awt.image.BufferedImage;

public class Tile
{
	private BufferedImage image;
	private Solidity solidity;

	public Tile(BufferedImage image, Solidity solidity)
	{
		this.image = image;
		this.solidity = solidity;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public Solidity getSolidity()
	{
		return solidity;
	}
}
