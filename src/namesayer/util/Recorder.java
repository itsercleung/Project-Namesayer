package namesayer.util;

import javafx.scene.control.ProgressBar;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

//Credit: https://stackoverflow.com/questions/26574326/how-to-calculate-the-level-amplitude-db-of-audio-signal-in-java
public class Recorder implements Runnable {
    private final ProgressBar meter;
    private volatile boolean exit = false;

    public Recorder(final ProgressBar meter) {
        this.meter = meter;
    }

    //Calculates appropriate peak level for mic levels to fix to
    public void run() {
        while (!exit) {
            AudioFormat fmt = new AudioFormat(44100f, 8, 1, true, false);
            final int bufferByteSize = 2048;

            TargetDataLine line = null;
            try {
                line = AudioSystem.getTargetDataLine(fmt);
                line.open(fmt, bufferByteSize);
            } catch (LineUnavailableException e) {
                System.err.println(e);
            }

            byte[] buf = new byte[bufferByteSize];
            float[] samples = new float[bufferByteSize / 2];

            float lastPeak = 0f;

            line.start();
            for (int b; (b = line.read(buf, 0, buf.length)) > -1; ) {

                // convert bytes to samples here
                for (int i = 0, s = 0; i < b; ) {
                    int sample = 0;

                    sample |= buf[i++] & 0xFF; // (reverse these two lines
                    sample |= buf[i++] << 8;   //  if the format is big endian)

                    // normalize to range of +/-1.0f
                    samples[s++] = sample / 44100f;
                }

                float rms = 0f;
                float peak = 0f;
                for (float sample : samples) {
                    float abs = Math.abs(sample);
                    if (abs > peak) {
                        peak = abs;
                    }
                    rms += sample * sample;
                }
                rms = (float) Math.sqrt(rms / samples.length);

                if (lastPeak > peak) {
                    peak = lastPeak * 0.875f;
                }
                lastPeak = peak;
                meter.setProgress(peak * 1.4);
            }
        }
    }

    public void stop() {
        exit = true;
    }
}
