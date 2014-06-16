package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Rel;

public class CmdFactionsRelationEnemy extends CmdFactionsRelationAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelationEnemy()
	{
		// Aliases
		this.addAliases("enemy");
		
		// Misc
		this.targetRelation = Rel.ENEMY;
	}
	
}
