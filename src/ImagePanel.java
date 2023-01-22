import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ImagePanel extends JPanel {
    boolean b_isPushed = false;
    boolean b_isDragged = false;
    ImageIcon image;
    final int IMG_WIDTH;
    final int IMG_HEIGHT;
    Point imageCenter;
    Point previousPoint;
    public void changeSize(int x, int y) {
        this.setSize(x, y);
    }
    public ImagePanel(Piece piece) {
        if (piece instanceof King) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_king.png" : "src/Resource/b_king.png");
        }
        else if (piece instanceof Queen) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_queen.png" : "src/Resource/b_queen.png");
        }
        else if (piece instanceof Rook) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_rook.png" : "src/Resource/b_rook.png");
        }
        else if (piece instanceof Bishop) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_bishop.png" : "src/Resource/b_bishop.png");
        }
        else if (piece instanceof Knight) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_knight.png" : "src/Resource/b_knight.png");
        }
        else if (piece instanceof Pawn) {
            image = new ImageIcon(piece.isWhite() ? "src/Resource/w_pawn.png" : "src/Resource/b_pawn.png");
        }
        IMG_WIDTH = image.getIconWidth();
        IMG_HEIGHT = image.getIconHeight();
        imageCenter = new Point(IMG_WIDTH / 2, IMG_HEIGHT / 2);
        previousPoint = imageCenter;

        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);

        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) imageCenter.getX() - IMG_WIDTH / 2, (int) imageCenter.getY() - IMG_HEIGHT / 2);
    }
    private class ClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            Point currentPoint = e.getPoint();
            if (currentPoint.getX() > previousPoint.getX() - IMG_WIDTH / 2 && currentPoint.getX() < previousPoint.getX() + IMG_WIDTH / 2 && currentPoint.getY() > previousPoint.getY() - IMG_HEIGHT / 2 && currentPoint.getY() < previousPoint.getY() + IMG_HEIGHT / 2) {
                b_isPushed = true;
                if (b_isDragged) {
                    previousPoint = currentPoint;
                }
            }
//            }
        }
        public void mouseReleased(MouseEvent e) {
            b_isPushed = false;
            b_isDragged = false;
        }
    }
    private class DragListener extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            Point currentPoint = e.getPoint();
            b_isDragged = true;
            if (b_isPushed) {
                imageCenter.translate(
                        (int) (currentPoint.getX() - previousPoint.getX()),
                        (int) (currentPoint.getY() - previousPoint.getY()));
                previousPoint = currentPoint;
                repaint();
            }
        }
    }
}
