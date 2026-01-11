import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
public class Main extends JFrame {
    private JLabel resultLabel;
    private ModernButton[] boardButtons = new ModernButton[9];
    private boolean gameOver = false;
    private boolean playerTurn = true;
    private final Color X_COLOR = new Color(80, 150, 255);
    private final Color O_COLOR = new Color(255, 80, 80);
    private final Color BTN_BG = new Color(255, 255, 255, 30);
    public Main() {
        setTitle("Project: Tic-Tac-Toe Game");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(40, 10, 20);
                Color color2 = new Color(10, 10, 40);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        RoundedPanel gameCard = new RoundedPanel(40, new Color(255, 255, 255, 25));
        gameCard.setLayout(new BoxLayout(gameCard, BoxLayout.Y_AXIS));
        gameCard.setBorder(new EmptyBorder(30, 50, 30, 50));
        gameCard.setPreferredSize(new Dimension(600, 750));
        JLabel titleLabel = new JLabel("Tic-Tac-Toe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subTitleLabel = new JLabel("Player (X) vs CPU (O)");
        subTitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        subTitleLabel.setForeground(new Color(255, 255, 255, 150));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);
        boardPanel.setLayout(new GridLayout(3, 3, 10, 10));
        boardPanel.setMaximumSize(new Dimension(400, 400));
        boardPanel.setPreferredSize(new Dimension(400, 400));
        for (int i = 0; i < 9; i++) {
            final int index = i;
            boardButtons[i] = new ModernButton("", BTN_BG);
            boardButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 60));
            boardButtons[i].addActionListener(e -> playTurn(index));
            boardPanel.add(boardButtons[i]);
        }
        resultLabel = new JLabel("YOUR TURN");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        ModernButton restartBtn = new ModernButton("RESTART", new Color(80, 180, 100));
        restartBtn.setPreferredSize(new Dimension(140, 45));
        restartBtn.addActionListener(e -> resetGame());
        ModernButton exitBtn = new ModernButton("EXIT GAME", new Color(50, 50, 50));
        exitBtn.setForeground(Color.GRAY);
        exitBtn.setPreferredSize(new Dimension(140, 45));
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(restartBtn);
        buttonPanel.add(exitBtn);
        gameCard.add(titleLabel);
        gameCard.add(subTitleLabel);
        gameCard.add(Box.createRigidArea(new Dimension(0, 30)));
        gameCard.add(boardPanel);
        gameCard.add(Box.createRigidArea(new Dimension(0, 20)));
        gameCard.add(resultLabel);
        gameCard.add(Box.createRigidArea(new Dimension(0, 30)));
        gameCard.add(buttonPanel);
        mainPanel.add(gameCard);
        add(mainPanel);
    }
    private void playTurn(int index) {
        if (gameOver || !boardButtons[index].getText().equals("")) {
            return;
        }
        boardButtons[index].setText("X");
        boardButtons[index].setForeground(X_COLOR);
        
        if (checkWin("X")) {
            resultLabel.setText("YOU WIN!");
            resultLabel.setForeground(X_COLOR);
            gameOver = true;
            return;
        } else if (isBoardFull()) {
            resultLabel.setText("IT'S A DRAW!");
            gameOver = true;
            return;
        }
        playerTurn = false;
        resultLabel.setText("CPU THINKING...");
        cpuMove();
    }
    private void cpuMove() {
        if (gameOver) return;
        Random rand = new Random();
        ArrayList<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (boardButtons[i].getText().equals("")) {
                availableMoves.add(i);
            }
        }
        if (!availableMoves.isEmpty()) {
            int moveIndex = availableMoves.get(rand.nextInt(availableMoves.size()));
            boardButtons[moveIndex].setText("O");
            boardButtons[moveIndex].setForeground(O_COLOR);
            if (checkWin("O")) {
                resultLabel.setText("CPU WINS!");
                resultLabel.setForeground(O_COLOR);
                gameOver = true;
            } else if (isBoardFull()) {
                resultLabel.setText("IT'S A DRAW!");
                gameOver = true;
            } else {
                resultLabel.setText("YOUR TURN");
                playerTurn = true;
            }
        }
    }
    private boolean checkWin(String symbol) {
        int[][] winConditions = {{0, 1, 2},{3, 4, 5},{6, 7, 8},{0, 3, 6},{1, 4, 7},{2, 5, 8},{0, 4, 8},{2, 4, 6}};
        for (int[] condition : winConditions) {
            if (boardButtons[condition[0]].getText().equals(symbol) &&
                boardButtons[condition[1]].getText().equals(symbol) &&
                boardButtons[condition[2]].getText().equals(symbol)) {
                return true;
            }
        }
        return false;
    }
    private boolean isBoardFull() {
        for (ModernButton btn : boardButtons) {
            if (btn.getText().equals("")) return false;
        }
        return true;
    }
    private void resetGame() {
        for (ModernButton btn : boardButtons) {
            btn.setText("");
            btn.setBackgroundColor(BTN_BG); 
        }
        gameOver = false;
        playerTurn = true;
        resultLabel.setText("YOUR TURN");
        resultLabel.setForeground(Color.WHITE);
    }
    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
    class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        public ModernButton(String text, Color bg) {
            super(text);
            this.normalColor = bg;
            this.hoverColor = bg.brighter();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setContentAreaFilled(true); repaint(); }
                public void mouseExited(MouseEvent e) { setContentAreaFilled(false); repaint(); }
            });
        }
        public void setBackgroundColor(Color c) {
            this.normalColor = c;
            this.hoverColor = c.brighter();
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) g2d.setColor(hoverColor);
            else g2d.setColor(normalColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            FontMetrics metrics = g.getFontMetrics(getFont());
            int x = (getWidth() - metrics.stringWidth(getText())) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.setColor(getForeground());
            g2d.drawString(getText(), x, y);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}