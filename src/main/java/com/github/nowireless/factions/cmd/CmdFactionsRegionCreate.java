package com.github.nowireless.factions.cmd;

import java.util.ArrayList;

import com.github.nowireless.factions.entity.Region;
import com.github.nowireless.factions.entity.RegionCollection;
import com.github.nowireless.factions.entity.RegionCollections;
import com.github.nowireless.factions.event.FactionsEventRegionCreate;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.store.MStore;

public class CmdFactionsRegionCreate extends FCommand{
	public CmdFactionsRegionCreate() {
		this.addAliases("createregion", "cr");
		this.addRequiredArg("name");
		this.addRequiredArg("world");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.CREATE_REGION.node));
	}
	
	@Override
	public void perform() {
		String newName = this.arg(0);
		String newWorld = this.arg(1);
		
		Factions.get().log("Retriving Region Collection");
		RegionCollection coll = RegionCollections.get().get(usender);
		
		if(coll.isNameTaken(newName)) {
			msg("<b>That name is already in use.");
			return;
		}
		
		ArrayList<String> nameValidationErrors = coll.validateName(newName);
		if (nameValidationErrors.size() > 0) {
			sendMessage(nameValidationErrors);
			return;
		}
		
		String regionId = MStore.createId();
		
		FactionsEventRegionCreate event = new FactionsEventRegionCreate(sender, coll.getUniverse(), regionId, newName);
		event.run();
		if(event.isCancelled()) return;
		
		Region region = coll.create(regionId);
		region.setName(newName);
		region.setWorld(newWorld);
		
		String msg = "Created Region: " + region.getName() + " for world " + region.getWorld() + "with ID:" + region.getId();
		usender.sendMessage(msg);
		Factions.get().log(msg);
	}
}
