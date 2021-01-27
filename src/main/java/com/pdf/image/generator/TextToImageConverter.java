package com.pdf.image.generator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TextToImageConverter {



    public static void main(String[] args) throws Exception {
        BufferedImage image = new TextToImageConverter().convertTextToGraphic("my text", new Font("Arial", Font.PLAIN, 18));
        //write BufferedImage to file
        ImageIO.write(image, "png", new File("path-to-file.png"));
    }

    public BufferedImage convertTextToGraphic(String text, Font font) {

        BufferedImage img = new BufferedImage(350, 200, BufferedImage.TYPE_BYTE_INDEXED);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(350, 200, BufferedImage.TYPE_BYTE_INDEXED);

        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.RED);
        g2d.drawString(text, 100, 100);
        g2d.dispose();
        return img;
    }
}