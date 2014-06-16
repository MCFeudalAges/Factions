package com.massivecraft.factions.integration.herochat;

import com.massivecraft.massivecore.integration.IntegrationAbstract;;

public class HerochatFeatures extends IntegrationAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static HerochatFeatures i = new HerochatFeatures();
	public static HerochatFeatures get() { return i; }
	private HerochatFeatures() { super("Herochat"); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		HerochatEngine.get().activate();
	}
	
	@Override
	public void deactivate()
	{
		HerochatEngine.get().deactivate();
	}
	
}
