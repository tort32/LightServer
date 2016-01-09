package com.github.tort32.common.animation;

import java.io.IOException;

import com.github.tort32.common.ILight;

public class LightAnimator {
	
	private AnimatorTask task;
	private ILight light;
	
	public LightAnimator(ILight light) {
		this.light = light;
	}
	
	public IAnimation getAnimation() {
		return (task != null) ? task.anim : null;
	}
	
	public synchronized void setAnimation(IAnimation anim) {
		if (task != null) {
			task.stop();
			task = null;
		}
		if (anim != null) {
			task = new AnimatorTask(anim);
			new Thread(task).start();
		}
	}

	class AnimatorTask implements Runnable {
		
		public volatile boolean isRunning = true;
		private IAnimation anim;
		
		AnimatorTask(IAnimation anim) {
			this.anim = anim;
		}
		
		void stop() {
			isRunning = false;
		}
		
		@Override
		public void run() {
			while (isRunning) {
				AnimationFrame frame = anim.getNextFrame();
				try {
					light.setColor(frame.color, frame.transition, false);
				} catch(IOException e) {
					// Skip
				}
				try {
					Thread.sleep(frame.duration);
				} catch(InterruptedException e) {
					// Skip
				}
			}
		}
	}
}
