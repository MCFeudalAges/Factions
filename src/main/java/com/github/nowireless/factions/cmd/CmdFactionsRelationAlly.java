package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Rel;

public class CmdFactionsRelationAlly extends CmdFactionsRelationAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelationAlly()
	{
		// Aliases
		this.addAliases("ally");
		
		// Misc
		this.targetRelation = Rel.ALLY;
	}
	
}
