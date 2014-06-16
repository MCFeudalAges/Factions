package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Region;
import com.github.nowireless.factions.entity.RegionBoardCollections;
import com.github.nowireless.factions.entity.RegionCollections;
import com.github.nowireless.factions.event.FactionsEventRegionChunkChange;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsRegionRemoveChunk extends FCommand {
	public CmdFactionsRegionRemoveChunk() {
		this.addAliases("regionremovechunk", "rrc");

		this.addRequiredArg("region");

		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(Perm.REGION_REMOVE_CHUNK.node));
	}

	@Override
	public void perform() {
		PS chunk = PS.valueOf(me).getChunk(true);
		Region region = RegionCollections.get().get(me).getNone();

		this.removeChunk(region, chunk);
	}

	private void removeChunk(Region newRegion, PS ps) {
		PS chunk = ps.getChunk(true);
		Region oldRegion = RegionBoardCollections.get().getRegionAt(chunk);

		if(newRegion == oldRegion) {
			msg("Chunk is already a part of the region %s.", newRegion.getName());
			return;
		}

		FactionsEventRegionChunkChange event = new FactionsEventRegionChunkChange(sender, chunk, oldRegion.getName(), oldRegion.getId(), newRegion.getName(), newRegion.getId());
		event.run();
		if(event.isCancelled()) return;

		RegionBoardCollections.get().setRegionAt(chunk, newRegion);
		
		//RegionBoardCollections.get().clean();
		
		String chunkCoords = "[" + chunk.getChunkX() + "," + chunk.getChunkZ() + "," + chunk.getWorld() + "]";
		String msg = "Added Chunk: " + chunkCoords + " to region: " + newRegion.getName();
		usender.sendMessage(msg);
		Factions.get().log(msg);
	}
}
