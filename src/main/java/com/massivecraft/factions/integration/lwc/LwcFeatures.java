package com.massivecraft.factions.integration.lwc;

import com.massivecraft.massivecore.integration.IntegrationAbstract;;

public class LwcFeatures extends IntegrationAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static LwcFeatures i = new LwcFeatures();
	public static LwcFeatures get() { return i; }
	private LwcFeatures() { super("LWC"); }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void activate()
	{
		LwcEngine.get().activate();
	}
	
	@Override
	public void deactivate()
	{
		LwcEngine.get().deactivate();
	}
	
}
