package com.github.nowireless.factions.task;

import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayerColl;
import com.github.nowireless.factions.entity.UPlayerColls;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;

public class TaskPlayerDataRemove extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskPlayerDataRemove i = new TaskPlayerDataRemove();
	public static TaskPlayerDataRemove get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskPlayerDataRemoveMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().taskPlayerDataRemoveMinutes = delayMillis / (double) TimeUnit.MILLIS_PER_MINUTE;
	}
	
	@Override
	public void invoke(long now)
	{
		for (UPlayerColl coll : UPlayerColls.get().getColls())
		{
			// Check disabled
			if (UConf.isDisabled(coll)) continue;
			
			coll.removePlayerDataAfterInactiveDaysRoutine();
		}
	}
	
}
