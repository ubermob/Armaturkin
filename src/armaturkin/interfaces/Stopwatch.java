package armaturkin.interfaces;

import java.time.Instant;

public interface Stopwatch {
	default long getStartTime(){
		return Instant.now().toEpochMilli();
	}

	default long getStopwatch(long l) {
		return (Instant.now().toEpochMilli() - l);
	}
}