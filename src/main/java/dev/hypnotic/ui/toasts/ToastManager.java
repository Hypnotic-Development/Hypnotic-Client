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
*/
package dev.hypnotic.ui.toasts;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.util.math.MatrixStack;

/**
* @author BadGamesInc
*/
public class ToastManager {

	public static ToastManager INSTANCE = new ToastManager();
	
	public List<Toast> toasts = new ArrayList<>();
	
	public void showToast(Toast toast) {
		toasts.add(toast);
		toast.init();
	}
	
	public void renderToasts(MatrixStack matrices) {
		for (int i = 0; i < toasts.size(); i++) {
			Toast toast = toasts.get(i);
			toast.render(matrices, i);
			
			if (toast.hasLeft) toasts.remove(toast);
		}
	}
}
