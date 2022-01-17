/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MinPriorityThreadFactory implements ThreadFactory
{
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;
	
	public MinPriorityThreadFactory()
	{
		group = Thread.currentThread().getThreadGroup();
		namePrefix = "pool-min-" + poolNumber.getAndIncrement() + "-thread-";
	}
	
	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(group, r,
			namePrefix + threadNumber.getAndIncrement(), 0);
		if(t.isDaemon())
			t.setDaemon(false);
		if(t.getPriority() != Thread.MIN_PRIORITY)
			t.setPriority(Thread.MIN_PRIORITY);
		return t;
	}
	
	public static ExecutorService newFixedThreadPool()
	{
		return Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(),
			new MinPriorityThreadFactory());
	}
}
