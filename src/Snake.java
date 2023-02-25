import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Snake {

    public static void main(String[] args) {
        new Snake().run();
    }

    private int[] field = new int[]{20, 20};
    private int cellSize = 40;

    private boolean running = true;

    ArrayList<int[]> snake = new ArrayList<>();

    int currentFrame = 0;
    int frameSkip = 10;

    enum Directions {UP, DOWN, LEFT, RIGHT}

    private void run() {
        snake.add(new int[]{field[0] / 2, field[1] / 2});

        JFrame frame = new JFrame();
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < field[0]; i++) {
                    for (int j = 0; j < field[1]; j++) {
                        g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
                    }
                }
                g.setColor(Color.BLUE);
                g.fillRect(food[0] * cellSize, food[1] * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                for (int[] cell : snake) {
                    g.fillRect(cell[0] * cellSize, cell[1] * cellSize, cellSize, cellSize);
                }
            }
        };

        panel.setPreferredSize(new Dimension(cellSize * field[0], cellSize * field[1]));
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
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_H -> direction = direction != Directions.RIGHT ? Directions.LEFT : Directions.RIGHT;
                    case KeyEvent.VK_J -> direction = direction != Directions.UP ? Directions.DOWN : Directions.UP;
                    case KeyEvent.VK_K -> direction = direction != Directions.DOWN ? Directions.UP : Directions.DOWN;
                    case KeyEvent.VK_L -> direction = direction != Directions.LEFT ? Directions.RIGHT : Directions.LEFT;
                }
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

            currentFrame++;
            currentFrame %= frameSkip;
            if (currentFrame == 0) {
                moveSnake();
            }

            panel.repaint();
        }
        frame.dispose();
    }

    private Directions direction = Directions.UP;

    private void moveSnake() {
        int[] head = snake.get(snake.size() - 1);
        switch (direction) {
            case UP -> snake.add(new int[]{head[0], head[1] - 1});
            case DOWN -> snake.add(new int[]{head[0], head[1] + 1});
            case LEFT -> snake.add(new int[]{head[0] - 1, head[1]});
            case RIGHT -> snake.add(new int[]{head[0] + 1, head[1]});
        }
        if (!hasConsumedLastMovement) {
            snake.remove(0);
        } else {
            hasConsumedLastMovement = false;
        }
        if (snake.get(snake.size() - 1)[0] < 0) reset();
        if (snake.get(snake.size() - 1)[0] > field[0]) reset();
        if (snake.get(snake.size() - 1)[1] < 0) reset();
        if (snake.get(snake.size() - 1)[1] > field[1]) reset();

        if (head[0] == food[0] && head[1] == food[1]) {
            consume();
        }
    }

    int[] food = summonFood();
    boolean hasConsumedLastMovement = false;
    private void consume() {
        hasConsumedLastMovement = true;
        food = summonFood();
    }

    private int[] summonFood() {
        return new int[]{(int) Math.floor(Math.random() * field[0]), (int) (Math.floor(Math.random() * field[1]))};
    }

    private void reset() {
        snake.clear();
        snake.add(new int[]{field[0] / 2, field[1] / 2});
    }
}
