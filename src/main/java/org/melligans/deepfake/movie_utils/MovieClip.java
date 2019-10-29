package org.melligans.deepfake.movie_utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class MovieClip {
    private long length;
    private String name;
    private String sourcePath;
    private double frames;
    private String format;
    private FFmpegFrameGrabber frameGrabber;

    public MovieClip(String sourcePath, String name) {
        this.sourcePath = sourcePath;
        this.name = name;
        setFrameGrabber(sourcePath);
    }

    public FFmpegFrameGrabber getFrameGrabber() {
        return this.frameGrabber;
    }

    private void setFrameGrabber(String sourcePath) {
        try {
            frameGrabber = new FFmpegFrameGrabber(sourcePath);
        } catch (NoClassDefFoundError ncdfe) {
            System.out.println(ncdfe.getMessage());
            System.out.println("No source found at: "+sourcePath);
        }

    }
}
