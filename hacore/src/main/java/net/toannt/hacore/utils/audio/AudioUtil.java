package net.toannt.hacore.utils.audio;

import timber.log.Timber;

public class AudioUtil {

    public static byte[] adjustVolume(byte[] audioSamples, double volume) {
        byte[] array = new byte[audioSamples.length];
        for (int i = 0; i < array.length; i+=2) {
            // convert byte pair to int
            int audioSample = (int) ((audioSamples[i+1] & 0xff) << 8) | (audioSamples[i] & 0xff);

            audioSample = (int) (audioSample * volume);
            Timber.i("audioSample after value: $au");

            // convert back
            array[i] = (byte) audioSample;
            array[i+1] = (byte) (audioSample >> 8);

        }
        return array;
    }
}
