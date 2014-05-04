package com.github.nowireless.factions.cmd.arg;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.entity.Region;
import com.github.nowireless.factions.entity.RegionCollection;
import com.github.nowireless.factions.entity.RegionCollections;
import com.massivecraft.mcore.cmd.arg.ArgReaderAbstract;
import com.massivecraft.mcore.cmd.arg.ArgResult;
import com.massivecraft.mcore.util.Txt;

public class ARRegion extends ArgReaderAbstract<Region>{
	public static ARRegion get(Object universe) {
		return new ARRegion(RegionCollections.get().get(universe));
	}
	
	public ARRegion(RegionCollection coll) {
		this.coll = coll;
	}
	
	private final RegionCollection coll;
	public RegionCollection getColl() { return this.coll; }
	
	@Override 
	public ArgResult<Region> read(String arg, CommandSender sender) {
		ArgResult<Region> result = new ArgResult<Region>();
		
		result.setResult(this.getColl().getByName(arg));
		if(result.hasResult()) return result;
		
		result.setResult(this.getColl().getBestNameMatch(arg));
		if(result.hasResult()) return result;
		
		result.setErrors(Txt.parse("<b> No Region matching \"<p>%s<b>\"."));
		return result;
	}
}
