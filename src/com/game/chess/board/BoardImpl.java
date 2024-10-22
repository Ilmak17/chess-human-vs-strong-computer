package com.game.chess.board;

import com.game.chess.Position;
import com.game.chess.models.Board;
import com.game.chess.pieces.Bishop;
import com.game.chess.pieces.King;
import com.game.chess.pieces.Knight;
import com.game.chess.pieces.Pawn;
import com.game.chess.pieces.Piece;
import com.game.chess.pieces.Queen;
import com.game.chess.pieces.Rook;
import com.game.chess.pieces.enums.Color;

import java.util.ArrayList;
import java.util.List;

import static com.game.chess.pieces.enums.Color.BLACK;
import static com.game.chess.pieces.enums.Color.WHITE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class BoardImpl implements Board {
    private int checkingKingIdx;
    private int threateningPieceIdx;
    private final List<Piece> pieces = new ArrayList<>();

    public BoardImpl() {
        initBoard();
    }

    @Override
    public Piece getPieceByPosition(Position position) {
        return pieces.stream()
                .filter(piece -> piece.getPosition().equals(position))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean pieceExistsAt(Position position) {
        return nonNull(getPieceByPosition(position));
    }

    @Override
    public boolean isPieceColor(Position position, Color color) {
        return getPieceByPosition(position).getColor().equals(color);
    }

    @Override
    public void capture(Position position) {
        pieces.forEach(piece -> {
            if (piece.getPosition().equals(position)) {
                System.out.println("Killed: " + piece);
                piece.setCaptured(true);
                pieces.remove(piece);
            }
        });
    }

    @Override
    public boolean isKingInCheck(Color color) {
        Piece checkedKing = pieces.stream()
                .filter(piece -> piece.getColor().equals(color) && piece instanceof King)
                .findFirst()
                .orElse(null);

        if (isNull(checkedKing)) return false;

        boolean isKingInCheck = pieces.stream()
                .anyMatch(piece -> !piece.getColor().equals(color) && piece.isValidMove(checkedKing.getPosition()));

        if (isKingInCheck) {
            threateningPieceIdx = pieces.indexOf(checkedKing);
            checkingKingIdx = pieces.indexOf(checkedKing);
        }

        return isKingInCheck;
    }

    @Override
    public boolean isSquareUnderAttack(Position position, Color color) {
        return pieces.stream()
                .filter(piece -> !piece.getColor().equals(color))
                .anyMatch(piece -> piece.isValidMove(piece.getPosition()));
    }

    @Override
    public boolean canPreventCheckmate() {
        Piece threateningPiece = pieces.get(threateningPieceIdx);

        return pieces.stream()
                .filter(piece -> !piece.getColor().equals(threateningPiece.getColor()))
                .anyMatch(piece -> piece.isValidMove(threateningPiece.getPosition()));
    }

    @Override
    public boolean isCheckmate(Color color) {
        if (!isKingInCheck(color)) return false;

        for (int col = 0; col <= 7; col++) {
            for (int row = 0; row <= 7; row++) {
                if (pieces.get(checkingKingIdx).isValidMove(new Position(row, col))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<Piece> getPieces() {
        return pieces;
    }

    public void initBoard() {
        for (int col = 0; col < 8; col++) {
            pieces.add(new Pawn(this, WHITE, new Position(col, 1)));
            pieces.add(new Pawn(this, BLACK, new Position(col, 6)));
        }

        pieces.add(new Rook(this, WHITE, new Position(0, 0)));
        pieces.add(new Rook(this, WHITE, new Position(7, 0)));
        pieces.add(new Rook(this, BLACK, new Position(0, 7)));
        pieces.add(new Rook(this, BLACK, new Position(7, 7)));

        pieces.add(new Knight(this, WHITE, new Position(1, 0)));
        pieces.add(new Knight(this, WHITE, new Position(6, 0)));
        pieces.add(new Knight(this, BLACK, new Position(1, 7)));
        pieces.add(new Knight(this, BLACK, new Position(6, 7)));

        pieces.add(new Bishop(this, WHITE, new Position(2, 0)));
        pieces.add(new Bishop(this, WHITE, new Position(5, 0)));
        pieces.add(new Bishop(this, BLACK, new Position(2, 7)));
        pieces.add(new Bishop(this, BLACK, new Position(5, 7)));

        pieces.add(new Queen(this, WHITE, new Position(3, 0)));
        pieces.add(new Queen(this, BLACK, new Position(3, 7)));

        pieces.add(new King(this, WHITE, new Position(4, 0)));
        pieces.add(new King(this, BLACK, new Position(4, 7)));
    }

    public int getCheckingKingIdx() {
        return checkingKingIdx;
    }

    public int getThreateningPieceIdx() {
        return threateningPieceIdx;
    }

}
