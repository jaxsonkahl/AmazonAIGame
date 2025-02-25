package ubc.cosc322;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

public class COSC322Timer {

	public final static int TURN_MILLIS = 30_000;

	private Timer timer = null;
	private TimerTask task = null;
	private long startMillis;

	public void start() {
		if (timer != null) {
			throw new IllegalStateException("timer is already started");
		}
		logTime();
		System.out.println("1's timer has been started");
		timer = new Timer();
		task = new Task();
		timer.schedule(task, TURN_MILLIS);
		startMillis = System.currentTimeMillis();
	}

	public void stop() {
		if (timer == null) {
			return;
		}
		timer.cancel();
		timer = null;
		long elapsedMillis = System.currentTimeMillis() - startMillis;
		System.out.println("'s timer has been stopped, " + elapsedMillis + "ms elapsed");
		logTime();
	}

	private static void logTime() {
		System.out.println("CURRENT TIME is " + LocalDateTime.now().atZone(ZoneId.of("America/Los_Angeles")));
	}

	private class Task extends TimerTask {

		@Override
		public void run() {
			timer = null;
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("! "  + " HAS RUN OUT OF TIME !");
			System.out.println("! " + " HAS RUN OUT OF TIME !");
			System.out.println("! " + " HAS RUN OUT OF TIME !");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logTime();
		}

	}
	
}