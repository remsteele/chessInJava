public class Move {
     private final Spot start;
     private final Spot end;
     private final Spot taken;
     private final Piece promotesTo;
     public Move(Spot start, Spot end, Spot taken, Piece promotesTo) {
          this.start = start;
          this.end = end;
          this.taken = taken;
          this.promotesTo = promotesTo;
     }
     public Spot getStart() {
          return start;
     }
     public Spot getEnd() {
          return end;
     }
     public Spot getTaken() {
          return taken;
     }
     public Piece getPromotion() {
          return promotesTo;
     }
     public boolean equalsIgnorePiece(int startX, int startY, int endX, int endY) {
          return start.getX() == startX && start.getY() == startY && end.getX() == endX && end.getY() == endY;
     }
     public String toSpecialString() {
          return "\nStart: " + start + "\nEnd: " + end + "\nTaken: " + taken + "\nPromoted to: " + promotesTo;
     }
     @Override
     public String toString() {
          if (start.getPiece() == null) return "INVALID MOVE: NULL SQUARE CANNOT MOVE";
          if (taken.getPiece() == null) {
               if (start.getPiece() instanceof King) {
                    if (end.getY() - start.getY() == 2) {
                         return "O-O";
                    }
                    else if (end.getY() - start.getY() == -2) {
                         return "O-O-O";
                    }
               }
               if (start.getPiece() instanceof Pawn) {
                    return "" + (char) (97 + end.getY()) + (8 - end.getX());
               }
               else {
                    return "" + (start.getPiece() instanceof Knight ? "N" : start.getPiece().getClass().getName().charAt(0)) + (char) (97 + end.getY()) + (8 - end.getX());
               }
          }
          else {
               if (start.getPiece() instanceof Pawn) {
                    return "" + (char) (97 + start.getY()) + "x" + (char) (97 + end.getY()) + (8 - end.getX());
               }
               else {
                    return "" + (start.getPiece() instanceof Knight ? "N" : start.getPiece().getClass().getName().charAt(0)) + "x" + (char) (97 + end.getY()) + (8 - end.getX());
               }
          }
     }
}
