package com.robodoot.dr.RoboApp;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
/**
 * Created by john on 2/21/16.
 */
//based on example found at https://stackoverflow.com/questions/4777060/android-sample-microphone-without-recording-to-get-live-amplitude-level
public class CatEars {
    private AudioRecord ar = null;

private int minSize;

public void start() {
        minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
        ar.startRecording();
        }

public void stop() {
        if (ar != null) {
        ar.stop();
        }
        }

public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer)
        {
        if (Math.abs(s) > max)
        {
        max = Math.abs(s);
        }
        }
        return max;
        }

}
