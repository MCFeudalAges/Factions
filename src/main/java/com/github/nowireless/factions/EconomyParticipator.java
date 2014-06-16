package com.github.nowireless.factions;


public interface EconomyParticipator extends RelationParticipator
{
	public boolean msg(String msg, Object... args);
}