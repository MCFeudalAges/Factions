package com.github.nowireless.factions.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class FactionsEventRegionDestroy extends FactionsEventAbstractSender {
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {return handlers; }
	public static HandlerList getHadlerList() {return handlers; }
	
	private final String regionId;
	public String getRegionId() { return this.regionId; }
	
	private final String universe;
	public String getUniverse() { return this.universe; }

	private final String regionName;
	public String getRegion() { return this.regionName; }
	
	public FactionsEventRegionDestroy(CommandSender sender, String universe, String regionId, String regionName) {
		super(sender);
		this.universe = universe;
		this.regionId = regionId;
		this.regionName = regionName;
	}
	
}
