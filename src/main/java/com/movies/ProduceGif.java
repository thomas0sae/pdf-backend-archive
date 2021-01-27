package com.movies;

import com.github.kokorin.jaffree.ffmpeg.Frame;
import com.github.kokorin.jaffree.ffmpeg.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProduceGif {
    private final Path ffmpegBin;

    public ProduceGif(Path ffmpegBin) {
        this.ffmpegBin = ffmpegBin;
    }

    public void execute() {
        Path output = Paths.get("test.mp4");

        FrameProducer producer = new FrameProducer() {
            private long frameCounter = 0;

            @Override
            public List<Stream> produceStreams() {
                return Collections.singletonList(new Stream()
                        .setType(Stream.Type.VIDEO)
                        .setTimebase(1000L)
                        .setWidth(1024)
                        .setHeight(940)
                );
            }

            @Override
            public Frame produce() {
                if (frameCounter > 60) {
                    return null;
                }
                System.out.println("Creating frame " + frameCounter);

                BufferedImage image = new BufferedImage(1024, 940, BufferedImage.TYPE_3BYTE_BGR);

                Graphics2D graphics = image.createGraphics();
                graphics.setPaint(new Color(frameCounter * 1.0f / 60, 0, 0));
                graphics.fillRect(0, 0, 1024, 940);
                graphics.setColor(Color.RED);
                graphics.setFont(new Font("Arial", Font.BOLD, 18));
                graphics.drawString("Hi Easo, First videoFirst videoFirst videoFirst videoFirst videoFirst video", 100, 100);
                graphics.drawString("Easo, First videoFirst videoFirst videoFirst videoFirst videoFirst video", 120, 100);
                graphics.drawString("ASDSD, First videoFirst videoFirst videoFirst videoFirst videoFirst video", 140, 100);
                Frame videoFrame = new Frame(0, frameCounter * 1000 / 10, image);

                frameCounter++;

                return videoFrame;
            }
        };

        FFmpegResult result = FFmpeg.atPath(ffmpegBin)
                .addInput(
                        FrameInput.withProducer(producer)
                )
                .addOutput(
                        UrlOutput.toPath(output)
                )
                .execute();
    }

    public static void main(String[] args) {
        Iterator<String> argIter = Arrays.asList(args).iterator();

        String ffmpegBin = "I:\\ffmpeg-2020-09-16-git-c79f07cc0c-essentials_build\\bin\\";

        while (argIter.hasNext()) {
            String arg = argIter.next();

            if ("-ffmpeg_bin".equals(arg)) {
                ffmpegBin = argIter.next();
            }
        }

        if (ffmpegBin == null) {
            System.err.println("Arguments: -ffmpeg_bin </path/to/ffmpeg/bin>");
            return;
        }

        new ProduceGif(Paths.get(ffmpegBin)).execute();
    }
}