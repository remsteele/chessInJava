import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

public class Paint {
    // TEMP >
    public static void main(String[] args) {
        Board board = new Board();
        Paint panel = new Paint(board);
        System.out.println(board.getFenPosition());
//        System.out.printf("Total moves: %,d\n", Board.countMoves(board, 1, true, 0));
    }
    // < TEMP
    static Point current;
    static boolean selectedPiece = false;
    static boolean pushed = false;
    static boolean dragged = false;
    public Paint(Board gameState) {
        printBoard(gameState);
    }
    public static void printBoard(Board board) {
        final int PIX = 60; // pixels
        final int DIM = 8; // dimensions
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBounds(10, 10, PIX * DIM/* + 16*/, PIX * DIM/* + 39*/);
        JPanel panel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                boolean color = true;
                for (int x = 0; x < DIM; x++) {
                    for (int y = 0; y < DIM; y++) {
                        if (color) {
                            g.setColor(new Color(218, 237, 207));
                        }
                        else {
                            g.setColor(new Color(134, 166, 102));
                        }
                        g.fillRect(x * PIX, y * PIX, PIX, PIX);
                        color = !color;
                    }
                    color = !color;
                }

                for (int x = 0; x < DIM; x++) {
                    for (int y = 0; y < DIM; y++) {
                        if (board.getSpot(x, y) != null) {
                            try {
                                g.drawImage(getImage(board.getSpot(x, y).getPiece()), (int) board.getSpot(x, y).getYLocation(), (int) board.getSpot(x, y).getXLocation(), this);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        };
        frame.add(panel);
        frame.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e) {
                dragged = true;
                if (pushed) {
                    board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX)).translate(
                            (int) (getX(e) - board.getSpot( (int) (current.getX() / PIX), (int) (current.getY() / PIX)).getPreviousPoint().getX()),
                            (int) (getY(e) - board.getSpot( (int) (current.getX() / PIX), (int) (current.getY() / PIX)).getPreviousPoint().getY()));
                    board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX)).setPreviousPoint(getPoint(e));
                    frame.repaint();
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board.getSpot((int) (getX(e) / PIX), (int) (getY(e) / PIX)).getPiece() != null
                        && board.getSpot((int) (getX(e) / PIX), (int) (getY(e) / PIX)).getPiece().isWhite() == board.getTurnWhite()) {
                    selectedPiece = true;
                    current = getPoint(e);
                    board.getSpot((int) (getX(e) / PIX), (int) (getY(e) / PIX)).setPreviousPoint(getPoint(e));
                    pushed = true;
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedPiece) {
                    pushed = false;
                    dragged = false;
                    // INVALID MOVE
                    if (getX(e) / PIX > DIM || getX(e) / PIX < 0 || getY(e) / PIX > DIM || getY(e) / PIX < 0
                            || !board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX)).getPiece().
                                    isValidMove(board, board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX)),
                                            board.getSpot((int) (getX(e) / PIX), (int) (getY(e) / PIX)))) {
                        board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX)).setPieceLocation(new Point(((int) (current.getX() / PIX) * PIX), ((int) (current.getY() / PIX)) * PIX));
                        frame.repaint();
                    }
                    // VALID MOVE
                    else {
                        Spot start = board.getSpot((int) (current.getX() / PIX), (int) (current.getY() / PIX));
                        Spot end = board.getSpot((int) (getX(e) / PIX), (int) (getY(e) / PIX));
                        board.setMove(start, end);
                        checkForCastle(start, end);
                        playMoveOnBoard(start, end, e);
                        board.setTurnWhite(!board.getTurnWhite());
                        frame.repaint();
                        printMoveToConsole(start, end);
                    }
                    selectedPiece = false;
                }
            }
            private void checkForCastle(Spot start, Spot end) {
                if (start.getPiece() instanceof King) {
                    ((King) start.getPiece()).setMoved(true);
                    if (end.getY() - start.getY() == 2) {
                        castle(board, start.getX() == 7, true);

                    }
                    else if (end.getY() - start.getY() == -2) {
                        castle(board, start.getX() == 7, false);
                    }
                }
                if (start.getPiece() instanceof Rook) {
                    ((Rook) start.getPiece()).setMoved(true);
                }
            }
            private void playMoveOnBoard(Spot start, Spot end, MouseEvent e) {
                start.setPiece(null);
                end.setPieceLocation(new Point((int) (getX(e) / PIX) * PIX, (int) (getY(e) / PIX) * PIX));
                end.setPreviousPoint(new Point((int) (getX(e) / PIX) * PIX, (int) (getY(e) / PIX) * PIX));
            }
            private void printMoveToConsole(Spot start, Spot end) {
                if (end.getPiece().isWhite()) {
                    System.out.print((board.getMovesAmt() / 2) + 1 + ". " + board.getLastMove());
                } else {
                    System.out.print(" " + board.getLastMove());
                }
                if (!board.kingNotInCheck(start, start, !end.getPiece().isWhite())) {
                    System.out.print("+");
                }
                if (!end.getPiece().isWhite()) {
                    System.out.println();
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void castle(Board board, boolean white, boolean kingSide) {
        int startX, startY;
        int endX, endY;
        startX = white ? 7 : 0;
        startY = kingSide ? 5 : 3;
        endX = white ? 7 : 0;
        endY = kingSide ? 7 : 0;
        board.getSpot(startX, startY).setPiece(new Rook(white));
        board.getSpot(startX, startY).setPieceLocation(new Point( startX * 60, startY * 60));
        board.getSpot(startX, startY).setPreviousPoint(new Point(startX * 60, startY * 60));
        board.getSpot(endX, endY).setPiece(null);
    }
    // NECESSARY BECAUSE X AND Y FOR ARRAY AND PIXELS ARE SWAPPED
    public static double getX(MouseEvent e) {
        return e.getY();
    }
    public static double getY(MouseEvent e) {
        return e.getX();
    }
    public static Point getPoint(MouseEvent e) {
        return new Point(e.getY(), e.getX());
    }
    private static Image getImage(Piece piece) throws IOException {
        if (piece == null) {
            return null;
        }
        else if (piece instanceof King) {
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_king.png" : "src/Resource/b_king.png"));
        }
        else if (piece instanceof Queen) {
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_queen.png" : "src/Resource/b_queen.png"));
        }
        else if (piece instanceof Rook) {
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_rook.png" : "src/Resource/b_rook.png"));
        }
        else if (piece instanceof Bishop) {
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_bishop.png" : "src/Resource/b_bishop.png"));
        }
        else if (piece instanceof Knight) {
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_knight.png" : "src/Resource/b_knight.png"));
        }
        else if (piece instanceof Pawn){
            return ImageIO.read(new File(piece.isWhite() ? "src/Resource/w_pawn.png" : "src/Resource/b_pawn.png"));
        }
        return null;
    }
}