package com.github.nowireless.factions.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import com.github.nowireless.factions.entity.Region;
import com.massivecraft.massivecore.ps.PS;

public class FactionsEventRegionChunkChange  extends FactionsEventAbstractSender {
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }

	private final String oldRegionName;
	public String getOldRegion() { return oldRegionName; }

	private final String oldRegionID;
	public String getOldRegionId() { return oldRegionID; }
	
	private final String newRegionName;
	public String newOldRegion() { return newRegionName; }

	private final String newRegionID;
	public String newOldRegionId() { return newRegionID; }

	private final PS chunk;
	public PS getChunk() { return chunk; }

	public FactionsEventRegionChunkChange(CommandSender sender, PS chunk, String oldRegionName, String oldRegionId, String  newRegionName, String newRegionId) {
		super(sender);
		this.oldRegionName = oldRegionName;
		this.oldRegionID = oldRegionId;
		this.newRegionName = newRegionName;
		this.newRegionID = newRegionId;
		this.chunk = chunk;
	}
}
