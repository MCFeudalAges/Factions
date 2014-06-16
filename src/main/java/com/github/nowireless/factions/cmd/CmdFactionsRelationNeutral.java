package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Rel;

public class CmdFactionsRelationNeutral extends CmdFactionsRelationAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelationNeutral()
	{
		// Aliases
		this.addAliases("neutral");
		
		// Misc
		this.targetRelation = Rel.NEUTRAL;
	}
	
}
