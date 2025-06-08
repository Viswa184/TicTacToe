package com.TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private JLabel statusLabel;
    private boolean playerVsPlayer = true;
    private Random random = new Random();

    public TicTacToe() {
        String[] options = {"1 vs 1 (Friend)", "1 vs Computer"};
        int choice = JOptionPane.showOptionDialog(
                null, "Choose Game Mode", "Tic-Tac-Toe Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        playerVsPlayer = (choice == 0);

        setTitle("Tic-Tac-Toe");
        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        Font btnFont = new Font(Font.SANS_SERIF, Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(btnFont);
                buttons[i][j] = btn;
                boardPanel.add(btn);

                int row = i;
                int col = j;
                btn.addActionListener(e -> buttonClicked(row, col));
            }
        }

        statusLabel = new JLabel("X's Turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(resetButton, BorderLayout.EAST);

        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void buttonClicked(int row, int col) {
        JButton btn = buttons[row][col];
        if (!btn.getText().equals("") || isGameOver()) return;

        btn.setText(xTurn ? "X" : "O");

        if (checkWin(xTurn ? "X" : "O")) {
            statusLabel.setText((xTurn ? "X" : "O") + " wins! 🎉");
            disableButtons();
            return;
        }

        if (isBoardFull()) {
            statusLabel.setText("It's a draw! 🤝");
            return;
        }

        xTurn = !xTurn;

        if (playerVsPlayer) {
            statusLabel.setText((xTurn ? "X" : "O") + "'s Turn");
        } else {
            statusLabel.setText("Computer's Turn");
            disableButtons();
            Timer timer = new Timer(500, e -> {
                computerMove();
                enableButtons();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void computerMove() {
        if (isGameOver()) return;

        // Try to win
        if (trySmartMove("O")) {
            if (checkWin("O")) {
                statusLabel.setText("Computer wins! 💻🎉");
                disableButtons();
            } else if (isBoardFull()) {
                statusLabel.setText("It's a draw! 🤝");
            } else {
                xTurn = true;
                statusLabel.setText("X's Turn");
            }
            return;
        }

        // Try to block player
        if (trySmartMove("X")) {
            xTurn = true;
            statusLabel.setText("X's Turn");
            return;
        }

        // Try center
        if (buttons[1][1].getText().equals("")) {
            buttons[1][1].setText("O");
        } else {
            // Random move
            int row, col;
            do {
                row = random.nextInt(3);
                col = random.nextInt(3);
            } while (!buttons[row][col].getText().equals(""));
            buttons[row][col].setText("O");
        }

        if (checkWin("O")) {
            statusLabel.setText("Computer wins! 💻🎉");
            disableButtons();
        } else if (isBoardFull()) {
            statusLabel.setText("It's a draw! 🤝");
        } else {
            xTurn = true;
            statusLabel.setText("X's Turn");
        }
    }

    private boolean trySmartMove(String symbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(symbol);
                    boolean win = checkWin(symbol);
                    if (win && symbol.equals("O")) return true;
                    if (symbol.equals("X") && win) {
                        buttons[i][j].setText("O");
                        return true;
                    }
                    buttons[i][j].setText("");
                }
            }
        }
        return false;
    }

    private boolean checkWin(String player) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(player) &&
                    buttons[i][1].getText().equals(player) &&
                    buttons[i][2].getText().equals(player)) return true;

            if (buttons[0][i].getText().equals(player) &&
                    buttons[1][i].getText().equals(player) &&
                    buttons[2][i].getText().equals(player)) return true;
        }

        if (buttons[0][0].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][2].getText().equals(player)) return true;

        if (buttons[0][2].getText().equals(player) &&
                buttons[1][1].getText().equals(player) &&
                buttons[2][0].getText().equals(player)) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().equals("")) return false;
        return true;
    }

    private void disableButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }

    private void enableButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().equals("")) btn.setEnabled(true);
    }

    private boolean isGameOver() {
        return statusLabel.getText().contains("wins") || statusLabel.getText().contains("draw");
    }

    private void resetGame() {
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
            }
        xTurn = true;
        statusLabel.setText("X's Turn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe game = new TicTacToe();
            game.setVisible(true);
        });
    }
}
