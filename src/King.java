public class King extends Piece {
    private boolean moved = false;
    public King(boolean white) {
        super(white);
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    @Override
    public boolean isValidMove(Board board, Spot start, Spot end) {
        if (start.equalsIgnorePiece(end)) return false;
        // check for castle
        int[] ranks = {0, 7};
        for (int rank : ranks) {
            if (!moved) {
                if (end.equalsIgnorePiece(rank, 6) && start.equalsIgnorePiece(rank, 4)) {
                    if ((rank == 0 && !start.getPiece().isWhite()) || (rank == 7 && start.getPiece().isWhite())) {
                        if (board.getSpot(rank, 7).getPiece() != null &&
                                board.getSpot(rank, 7).getPiece() instanceof Rook &&
                                !((Rook) board.getSpot(rank, 7).getPiece()).hasMoved()) {
                            if (board.getSpot(rank, 5).getPiece() == null &&
                                    board.getSpot(rank, 6).getPiece() == null) {
                                if (board.kingNotInCheck(start, start, start.getPiece().isWhite()) &&
                                        board.kingNotInCheck(start, new Spot(rank, 5, null), start.getPiece().isWhite()) &&
                                        board.kingNotInCheck(start, end, start.getPiece().isWhite())) {
                                    if (start.getPiece().isWhite()) {
                                        board.setWhiteKing(end);
                                    } else {
                                        board.setBlackKing(end);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
                else if (end.equalsIgnorePiece(rank, 2) && start.equalsIgnorePiece(rank, 4)) {
                    if ((rank == 0 && !start.getPiece().isWhite()) || (rank == 7 && start.getPiece().isWhite())) {
                        if (board.getSpot(rank, 0).getPiece() != null &&
                                board.getSpot(rank, 0).getPiece() instanceof Rook &&
                                !((Rook) board.getSpot(rank, 0).getPiece()).hasMoved()) {
                            if (board.getSpot(rank, 1).getPiece() == null &&
                                    board.getSpot(rank, 2).getPiece() == null &&
                                    board.getSpot(rank, 3).getPiece() == null) {
                                if (board.kingNotInCheck(start, start, start.getPiece().isWhite()) &&
                                        board.kingNotInCheck(start, new Spot(rank, 3, null), start.getPiece().isWhite()) &&
                                        board.kingNotInCheck(start, end, start.getPiece().isWhite())) {
                                    if (start.getPiece().isWhite()) {
                                        board.setWhiteKing(end);
                                    } else {
                                        board.setBlackKing(end);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        // if square is empty
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (start.equalsIgnorePiece(end.getX() + x, end.getY() + y)) {
                    if (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite()) {
                        // if king not in check return true;
                        if (board.kingNotInCheck(start, end, start.getPiece().isWhite())) {
                            if (start.getPiece().isWhite()) {
                                board.setWhiteKing(end);
                            }
                            else {
                                board.setBlackKing(end);
                            }
                            return true;
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}