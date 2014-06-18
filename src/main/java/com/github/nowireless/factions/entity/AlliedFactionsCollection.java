package com.github.nowireless.factions.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.util.MiscUtil;
import com.massivecraft.massivecore.store.Coll;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.util.Txt;

public class AlliedFactionsCollection extends Coll<AlliedFactions> {
	
	/*
	 * Construction
	 */
	public AlliedFactionsCollection(String name) {
		super(name, AlliedFactions.class, MStore.getDb(), Factions.get());
	}
	
	/*
	 * Overrides Coll
	 */
	
	@Override
	public void init() {
		super.init();
		this.createSpecialAlliances();
	}
	
	@Override 
	public AlliedFactions get(Object oid) {
		AlliedFactions ret = super.get(oid);
		
		if(ret == null && Factions.get().isDatabaseInitialized()) {
			String msg = Txt.parse("<b>Non existing alliedFactionId <h>%s <b>requested. <i>Cleaning all boards and uplayers.", this.fixId(oid));
			Factions.get().log(msg);
			
			UPlayerColls.get().clean();
			RegionBoardCollections.get().clean();
		}
		return ret;
	}
	
	/*
	 * Special Alliances
	 */
	
	public void createSpecialAlliances() {
		this.getNone();
	}
	
	public AlliedFactions getNone() {
		String id = UConf.get(this).alliedFactionIdNone;
		AlliedFactions alliedFactions = this.get(id);
		if(alliedFactions != null) return alliedFactions;
		
		alliedFactions = this.create();
		
		alliedFactions.setName(ChatColor.BLUE + "Not Allied");
		alliedFactions.setDescription(null);
		alliedFactions.setOpen(false);
		
		return alliedFactions;
	}
	
	public ArrayList<String> validateName(String str)
	{
		ArrayList<String> errors = new ArrayList<String>();
		
		if (MiscUtil.getComparisonString(str).length() < UConf.get(this).factionNameLengthMin)
		{
			errors.add(Txt.parse("<i>The faction name can't be shorter than <h>%s<i> chars.", UConf.get(this).alliedFactionNameLengthMin));
		}
		
		if (str.length() > UConf.get(this).factionNameLengthMax)
		{
			errors.add(Txt.parse("<i>The faction name can't be longer than <h>%s<i> chars.", UConf.get(this).alliedFactionNameLengthMax));
		}
		
		for (char c : str.toCharArray())
		{
			if ( ! MiscUtil.substanceChars.contains(String.valueOf(c)))
			{
				errors.add(Txt.parse("<i>Faction name must be alphanumeric. \"<h>%s<i>\" is not allowed.", c));
			}
		}
		
		return errors;
	}
	
	public AlliedFactions getByName(String str) {
		String compStr = MiscUtil.getComparisonString(str);
		for (AlliedFactions allied : this.getAll())
		{
			if (allied.getComparisonName().equals(compStr))
			{
				return allied;
			}
		}
		return null;
	}
	
	public AlliedFactions getBestNameMatch(String searchFor) {
		Map<String, AlliedFactions> name2allied = new HashMap<String, AlliedFactions>();
		
		// TODO: Slow index building
		for (AlliedFactions allied : this.getAll())
		{
			name2allied.put(ChatColor.stripColor(allied.getName()), allied);
		}
		
		String tag = Txt.getBestCIStart(name2allied.keySet(), searchFor);
		if (tag == null) return null;
		return name2allied.get(tag);
	}
	
	public boolean isNameTaken(String str) {
		return this.getByName(str) != null;
	}

}
