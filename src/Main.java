import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private int[][] field = new int[20][20];
    private int cellSize = 40;

    private boolean running = true;

    private int[] player = new int[]{0, 0};

    private ArrayList<Character> register = new ArrayList<>();

    private boolean[] keysDown = new boolean[KeyEvent.VK_Z];

    private void run() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                for (int i = 0; i < field.length; i++) {
                    for (int j = 0; j < field[0].length; j++) {
                        g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    }
                }
                g.fillRect(player[0] * cellSize, player[1] * cellSize, cellSize, cellSize);
            }
        };

        panel.setPreferredSize(new Dimension(cellSize * field.length, cellSize * field[0].length));
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    running = false;
                }
                register.add((char) e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.setVisible(true);

        while (running) {
            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handleRegister();
            panel.repaint();
        }
        frame.dispose();
    }

    private void handleRegister() {
        if (register.size() > 0) {
            switch (register.get(register.size() - 1)) {
                case KeyEvent.VK_H -> {
                    movePlayer(-calculateMultiplier(), 0);
                    resetRegister();
                }
                case KeyEvent.VK_L -> {
                    movePlayer(calculateMultiplier(), 0);
                    resetRegister();
                }
                case KeyEvent.VK_J -> {
                    movePlayer(0, calculateMultiplier());
                    resetRegister();
                }
                case KeyEvent.VK_K -> {
                    movePlayer(0, -calculateMultiplier());
                    resetRegister();
                }
            }
        }
    }

    private void movePlayer(int x, int y) {
        player[0] += x;
        player[1] += y;
        if (player[0] < 0) player[0] = 0;
        if (player[0] >= field.length) player[0] = field.length - 1;
        if (player[1] < 0) player[1] = 0;
        if (player[1] >= field[0].length) player[1] = field[0].length - 1;
    }

    private void resetRegister() {
        register.clear();
    }

    private int calculateMultiplier() {
        int mult = 1;
        if (register.size() > 1) {
            StringBuilder builder = new StringBuilder(register.size() - 1);
            for (int i = 0; i < builder.capacity(); i++) {
                builder.append(register.get(i));
            }
            String value = builder.toString();
            if (value.matches("-?\\d+")) {
                mult = Integer.parseInt(value);
            }
        }
        return mult;
    }
}
