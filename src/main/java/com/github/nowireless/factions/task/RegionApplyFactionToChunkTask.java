package com.github.nowireless.factions.task;

import java.util.Set;






import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.Region;
//import com.massivecraft.factions.entity.BoardColls;
import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.event.FactionsEventChunkChange;
import com.massivecraft.massivecore.ps.PS;

public class RegionApplyFactionToChunkTask extends RegionChunkTask {
	private UPlayer usender;

	public RegionApplyFactionToChunkTask(Set<PS> chunks, Faction newFaction, Region workingRegion, UPlayer sender) {
		super(chunks, newFaction, workingRegion);
		usender = sender;
	}

	@Override
	public boolean work(PS chunk, Faction faction, Region region) {
		Factions.get().log("In work mehtod of region chunk task");
		FactionsEventChunkChange event = new FactionsEventChunkChange(usender.getSender(), chunk, faction);
		event.run();
		if(event.isCancelled()) return false;
		//BoardColls.get().setFactionAt(chunk, faction);
		String chunkCoords = "[" + chunk.getChunkX() + "," + chunk.getChunkZ() + "," + chunk.getWorld() + "]";
		String msg = "Chunk: " + chunkCoords + " of Region: " + region.getName() + " has been given to faction: " + faction.getName(); 
		Factions.get().log(msg);
		return true;
	}
}