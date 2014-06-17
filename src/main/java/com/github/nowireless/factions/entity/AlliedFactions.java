package com.github.nowireless.factions.entity;

import com.github.nowireless.factions.Lang;
import com.github.nowireless.factions.util.MiscUtil;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class AlliedFactions extends Entity<AlliedFactions>{
	public static AlliedFactions get(Object oid) {
		return AlliedFactionsCollections.get().get2(oid);
	}
	
	/*
	 * Override Entity
	 */
	
	public AlliedFactions load(AlliedFactions that) {
		this.setName(that.name);
		this.setDescription(that.description);
		this.setCreatedAtMillis(that.createdAtMillis);
		this.setHome(that.home);
		this.setOpen(that.open);
		return this;
	}
	
	@Override
	public void preDetach(String id) {
		
	}
	
	private String name = null;
	private String description = null;
	
	private long createdAtMillis = System.currentTimeMillis();
	
	private PS home = null;
	
	private Boolean open = null;

	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		String target = name;
		
		if(MUtil.equals(this.name, name)) return;
		
		this.name = target;
		
		this.changed();
		
	}
	
	
	public String getComparisonName() {
		return MiscUtil.getComparisonString(this.getName());
	}
	
	public String getName(String prefix) {
		return prefix + this.getName();
	}
	
	public boolean hasDescription()
	{
		return this.description != null;
	}
	
	public String getDescription() {
		if (this.hasDescription()) return this.description;
		return Lang.ALLIED_FACTION_NODESCRIPTION;
	}
	
	public void setDescription(String description) {
		// Clean input
		String target = description;
		if (target != null)
		{
			target = target.trim();
		}
		
		// Detect Nochange
		if (MUtil.equals(this.description, target)) return;

		// Apply
		this.description = target;
		
		// Mark as changed
		this.changed();
	}
	
	public long getCreatedAtMillis() {
		return this.createdAtMillis;
	}
	
	public void setCreatedAtMillis(long createdAtMillis) {
		// Clean input
		long target = createdAtMillis;
		
		// Detect Nochange
		if (MUtil.equals(this.createdAtMillis, createdAtMillis)) return;

		// Apply
		this.createdAtMillis = target;
		
		// Mark as changed
		this.changed();
	}
	
	public PS getHome() {
		this.verifyHomeIsValid();
		return this.home;
	}
	
	public void verifyHomeIsValid() {
		if (this.isValidHome(this.home)) return;
		this.home = null;
		//msg("<b>Your faction home has been un-set since it is no longer in your territory.");
	}
	
	public boolean isValidHome(PS ps) {
		if (ps == null) return true;
		
		//TODO Figure out what makes a home valid.
		
		return false;
	}
	
	public boolean hasHome() {
		return this.getHome() != null;
	}
	
	public void setHome(PS home) {
		// Clean input
		PS target = home;
		
		// Detect Nochange
		if (MUtil.equals(this.home, target)) return;
		
		// Apply
		this.home = target;
		
		// Mark as changed
		this.changed();
	}
	
	public boolean isDefaultOpen() {
		//TODO Add defualt to UCOnf
		return true;
	}
	
	public boolean isOpen() {
		Boolean ret = this.open;
		if (ret == null) ret = this.isDefaultOpen();
		return ret;
	}
	
	public void setOpen(Boolean open) {
		// Clean input
		Boolean target = open;
		
		// Detect Nochange
		if (MUtil.equals(this.open, target)) return;
		
		// Apply
		this.open = target;
		
		// Mark as changed
		this.changed();
	}
	
	
}
