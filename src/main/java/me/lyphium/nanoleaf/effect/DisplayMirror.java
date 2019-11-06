package me.lyphium.nanoleaf.effect;

import lombok.Getter;
import me.lyphium.nanoleaf.api.NanoleafAPI;
import me.lyphium.nanoleaf.panel.LightPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayMirror extends Thread {

    private final NanoleafAPI api;
    private final int delay;

    private Robot robot;
    private ExecutorService service;
    private Rectangle screen;

    private LightPanel[] panels;

    private int smallW, smallH;
    private float maxY, maxX;

    @Getter
    private boolean running;

    public DisplayMirror(NanoleafAPI api, int delay) {
        this.api = api;
        this.delay = Math.max(100, delay);

        setup();
    }

    private void setup() {
        this.screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }

        this.service = Executors.newCachedThreadPool();
        this.panels = api.getPanelsRotated().toArray(new LightPanel[0]);

        this.smallW = (int) (screen.width * 0.15);
        this.smallH = (int) (screen.height * 0.25);

        this.maxY = this.maxX = 0;
        for (LightPanel panel : panels) {
            if (panel.getX() > maxX) {
                maxX = (float) panel.getX();
            }
            if (panel.getY() > maxY) {
                maxY = (float) panel.getY();
            }
        }
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                final long start = System.currentTimeMillis();

                update();

                final long time = System.currentTimeMillis() - start;
                System.out.println(time);

                Thread.sleep(Math.max(0, delay - time));
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        running = false;
    }

    private void update() {
        final BufferedImage screenhot = robot.createScreenCapture(screen);
        final int size = smallW * smallH;

        final int[][] colors = new int[panels.length][3];
        final int[] pixels = new int[size * 3];

        int index = 0;
        for (LightPanel panel : panels) {
            final int newX = (int) (panel.getX() / maxX * screen.width * 0.85f);
            final int newY = (int) (panel.getY() / maxY * screen.height * 0.75f);

            screenhot.getData().getPixels(
                    newX, newY,
                    smallW, smallH,
                    pixels
            );

            int red = 0, green = 0, blue = 0;
            for (int i = 0; i < pixels.length; i += 3) {
                red += pixels[i];
                green += pixels[i + 1];
                blue += pixels[i + 2];
            }

            colors[index][0] = red / size;
            colors[index][1] = green / size;
            colors[index][2] = blue / size;

            index++;
        }
        screenhot.flush();

        //Set max memory ~200M
//        System.gc();

        if (!running)
            return;
        service.submit(() -> api.setPanelColor(panels, colors));
    }

    public void cancel() {
        running = false;
        service.shutdown();
        interrupt();
    }

}