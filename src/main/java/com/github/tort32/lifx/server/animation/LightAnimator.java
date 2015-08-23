package com.github.tort32.lifx.server.animation;

import java.io.IOException;

import com.github.tort32.lifx.server.Light;

public class LightAnimator {
	
	private AnimatorTask task;
	private Light light;
	
	public LightAnimator(Light light) {
		this.light = light;
	}
	
	public IAnimation getAnimation() {
		return (task != null) ? task.anim : null;
	}
	
	public void setAnimation(IAnimation anim) {
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
			while(isRunning) {
				AnimationFrame frame = anim.getNextFrame();
				try {
					light.setColor(frame.color, frame.transition);
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
