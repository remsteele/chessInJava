public class Rook extends Piece {
    private boolean moved = false;
    public Rook(boolean white) {
        super(white);
    }
    public boolean hasMoved() {
        return moved;
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        // if same square
        if (start.equalsIgnorePiece(end)) {
            return false;
        }
        // if not moving straight
        if (end.getX() != start.getX() && end.getY() != start.getY()) {
            return false;
        }
        // if path to piece blocked
        if (end.getX() == start.getX()) {
            int y = end.getY() > start.getY() ? 1 : -1;
            for (int i = 1; i < Math.abs(end.getY() - start.getY()); i++) {
                if (board.getSpot(start.getX(), start.getY() + i * y).getPiece() != null) {
                    return false;
                }
            }
        } else if (end.getY() == start.getY()) {
            int x = end.getX() > start.getX() ? 1 : -1;
            for (int i = 1; i < Math.abs(end.getX() - start.getX()); i++) {
                if (board.getSpot(start.getX() + i * x, start.getY()).getPiece() != null) {
                    return false;
                }
            }
        }
        // if end piece is same color
        if (end.getPiece() != null && end.getPiece().isWhite() == start.getPiece().isWhite()) {
            return false;
        }
        // update board and return true
        return board.kingNotInCheck(start, end, start.getPiece().isWhite());
    }
}