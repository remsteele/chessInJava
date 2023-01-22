public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        // if same square
        if (start.equalsIgnorePiece(end)) {
            return false;
        }
        // if not moving diagonally
        if (Math.abs(end.getX() - start.getX()) != Math.abs(end.getY() - start.getY())) {
            return false;
        }
        // if path to piece blocked
        int x = end.getX() > start.getX() ? 1 : -1;
        int y = end.getY() > start.getY() ? 1 : -1;
        for (int i = 1; i < Math.abs(end.getX() - start.getX()); i++) {
            if (board.getSpot(start.getX() + i * x, start.getY() + i * y).getPiece() != null) {
                return false;
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