import java.awt.*;

public class Spot {
    private Piece piece;
    private final int x, y;
    private Point pieceLocation;
    private Point previousPoint;

    public Spot(int x, int y, Piece piece) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.pieceLocation = new Point(x * 60, y * 60);
        this.previousPoint = pieceLocation;
    }
    @Override
    public Spot clone() {
        return new Spot(x, y, piece);
    }
    public boolean equalsIgnorePiece(int x, int y) {
        if (this.getX() <= 7 && this.getX() >= 0 && this.getY() <= 7 && this.getY() >= 0 &&
                x <= 7 && x >= 0 && y <= 7 && y >= 0) {
            return this.getX() == x && this.getY() == y;
        }
        return false;
    }
    public boolean equalsIgnorePiece(Spot spot) {
        if (this.getX() <= 7 && this.getX() >= 0 && this.getY() <= 7 && this.getY() >= 0 &&
                spot.getX() <= 7 && spot.getX() >= 0 && spot.getY() <= 7 && spot.getY() >= 0) {
            return this.getX() == spot.getX() && this.getY() == spot.getY();
        }
        return false;
    }
    public Spot transform(int x, int y) {
        return new Spot(this.x + x, this.y + y, this.piece);
    }
    public Point getPoint() {
        return new Point(x, y);
    }
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void translate(int x, int y) {
        pieceLocation.translate(x, y);
    }
    public Point getPieceLocation() {
        return pieceLocation;
    }
    public void setPieceLocation(Point pieceLocation) {
        this.pieceLocation = pieceLocation;
    }
    public Point getPreviousPoint() {
        return previousPoint;
    }
    public void setPreviousPoint(Point previousPoint) {
        this.previousPoint = previousPoint;
    }
    public double getXLocation() {
        return pieceLocation.getX();
    }
    public double getYLocation() {
        return pieceLocation.getY();
    }
    @Override
    public String toString() {
        if (piece == null) return "Empty (" + x + ", " + y + ")";
        else return piece.getClass().getName() + " (" + x + ", " + y + ")";
    }

}
