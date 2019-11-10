package me.lyphium.nanoleaf.effect;

import lombok.Getter;
import me.lyphium.nanoleaf.api.NanoleafAPI;
import me.lyphium.nanoleaf.panel.LightPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayMirror extends Thread {

    public static DisplayMirror CURRENT_MIRROR = null;

    private final List<Integer> times = new ArrayList<>();

    private final NanoleafAPI api;
    private final int delay;

    private ExecutorService service;
    private Rectangle screen;
    private Robot robot;

    private LightPanel[] panels;

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

                final int time = (int) (System.currentTimeMillis() - start);
                times.add(time);
//                System.out.println(time);

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
        final int scaledWidth = screen.width / 30;
        final int scaledHeight = screen.height / 30;
        final int smallW = (int) (scaledWidth * 0.15);
        final int smallH = (int) (scaledHeight * 0.25);
        final int size = smallW * smallH;

        final BufferedImage screenhot = robot.createScreenCapture(screen);
        final BufferedImage resized = createResizedCopy(screenhot, scaledWidth, scaledHeight);

        final int[][] colors = new int[panels.length][3];
        final int[] pixels = new int[size * 3];

        for (int index = 0; index < panels.length; index++) {
            final LightPanel panel = panels[index];

            final int newX = (int) (panel.getX() / maxX * scaledWidth * 0.85f);
            final int newY = (int) (panel.getY() / maxY * scaledHeight * 0.75f);

            resized.getData().getPixels(
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
        }

        if (!running)
            return;
        service.submit(() -> api.setPanelColor(panels, colors));

        resized.flush();
        screenhot.flush();
    }

    public void cancel() {
        running = false;
        service.shutdown();
        interrupt();
    }

    private static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight) {
        final BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = scaledBI.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        return scaledBI;
    }

    public static void stopMirror() {
        if (CURRENT_MIRROR != null) {
            CURRENT_MIRROR.cancel();

            final int time = CURRENT_MIRROR.times.parallelStream().mapToInt(i -> i).sum() / CURRENT_MIRROR.times.size();
            System.out.println("Average computing time: " + time);

            CURRENT_MIRROR = null;
        }
    }

}