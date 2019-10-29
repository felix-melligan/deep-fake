package org.melligans.deepfake.movie_utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ConvertMovieClip {
    private static int workerNumber;
    private final File destinationDirectory;
    private final Java2DFrameConverter converter;
    private final FFmpegFrameGrabber frameGrabber;
    private final double lengthInFrames;
    private int noOfWorkers = 1;
    private final String imgType = "png";

    public ConvertMovieClip(final MovieClip sourceMovie, final String destinationDirectory) {
        this.destinationDirectory = new File(destinationDirectory+"/src-frames/");
        converter = new Java2DFrameConverter();
        frameGrabber = sourceMovie.getFrameGrabber();
        lengthInFrames = this.frameGrabber.getLengthInFrames();
    }

    public ConvertMovieClip(final MovieClip sourceMovie, final int noOfWorkers, final String destinationDirectory) {
        this(sourceMovie, destinationDirectory);
        this.noOfWorkers = noOfWorkers;
    }

    private void convert() {
        workerNumber++;
        final double framesToProcess = (lengthInFrames / noOfWorkers);
        final double firstFrameToProcess = framesToProcess-(lengthInFrames / noOfWorkers)* workerNumber;
        final double lastFrameToProcess = firstFrameToProcess+framesToProcess;
        if(!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }
        if(frameGrabber != null) {
            try {
                frameGrabber.start();

                for (double i = firstFrameToProcess; i < lastFrameToProcess; i++) {
                    final Frame frame = frameGrabber.grabImage();
                    final String frameNumber = Double.toString(i);
                    final BufferedImage bi = converter.convert(frame);
                    final File img = new File(destinationDirectory + "/frame-" + frameNumber + ".png");
                    ImageIO.write(bi, imgType, img);
                }

                frameGrabber.stop();
                workerNumber--;
            } catch (final IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } else {
            System.out.println("FrameGrabber is null!");
        }
    }
}
