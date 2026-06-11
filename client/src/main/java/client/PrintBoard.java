package client;

import chess.*;
import ui.EscapeSequences;

public class PrintBoard{
    private static String[] letters ={ "a", "b", "c", "d", "e", "f", "g", "h" };

    public static void printBoard(ChessBoard board, boolean isWhite){
        int step = isWhite ? 1 : -1;
        int start = isWhite ? 0 : 7;
        int end = isWhite ? 8 : -1;

        letterRow(step, start, end);
        for(int i = 0; i < 8; i++){
            int displayRank = isWhite ? 8 - i : i + 1;
            printBorder(String.valueOf(displayRank));
            for(int j = 0; j < 8; j++){
                int row;
                int col;

                if(isWhite){
                    row = 8 - i;
                    col = j + 1;
                }else{
                    row = i + 1;
                    col = 8 - j;
                }

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String pieceColor;

                if(piece == null){
                    pieceColor = EscapeSequences.SET_TEXT_COLOR_DARK_GREY;
                }else{
                    pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                            ? EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY
                            : EscapeSequences.SET_TEXT_COLOR_RED;
                }

                if((row + col) % 2 == 1){
                    printWhite(pieceSymbol(piece), pieceColor);
                }else{
                    printBlack(pieceSymbol(piece), pieceColor);
                }
            }
            printBorder(String.valueOf(displayRank));
            System.out.println();
        }
        letterRow(step, start, end);
    }

    public static void printBoardWithHighlights(ChessBoard board, boolean isWhite, ChessPosition startPos,
            java.util.Collection<ChessMove> highlights){
        java.util.HashSet<ChessPosition> endPositions = new java.util.HashSet<>();
        for(ChessMove move : highlights){
            endPositions.add(move.getEndPosition());
        }

        int step = isWhite ? 1 : -1;
        int start = isWhite ? 0 : 7;
        int end = isWhite ? 8 : -1;

        letterRow(step, start, end);
        for(int i = 0; i < 8; i++){
            int displayRank = isWhite ? 8 - i : i + 1;
            printBorder(String.valueOf(displayRank));
            for(int j = 0; j < 8; j++){
                int row;
                int col;

                if(isWhite){
                    row = 8 - i;
                    col = j + 1;
                }else{
                    row = i + 1;
                    col = 8 - j;
                }

                ChessPosition currentPos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(currentPos);
                String pieceColor;

                if(piece == null){
                    pieceColor = EscapeSequences.SET_TEXT_COLOR_DARK_GREY;
                }else{
                    pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                            ? EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY
                            : EscapeSequences.SET_TEXT_COLOR_RED;
                }

                String bgColor;
                if(currentPos.equals(startPos)){
                    bgColor = EscapeSequences.SET_BG_COLOR_YELLOW;
                }else if(endPositions.contains(currentPos)){
                    bgColor = (row + col) % 2 == 1 ? EscapeSequences.SET_BG_COLOR_GREEN
                            : EscapeSequences.SET_BG_COLOR_DARK_GREEN;
                }else{
                    bgColor = (row + col) % 2 == 1 ? EscapeSequences.SET_BG_COLOR_WHITE
                            : EscapeSequences.SET_BG_COLOR_BLACK;
                }

                System.out.print(bgColor + pieceColor + pieceSymbol(piece) + EscapeSequences.RESET_TEXT_COLOR
                        + EscapeSequences.RESET_BG_COLOR);
            }
            printBorder(String.valueOf(displayRank));
            System.out.println();
        }
        letterRow(step, start, end);
    }

    private static String pieceSymbol(ChessPiece piece){
        if(piece == null || piece.getTeamColor() == null){
            return "   ";
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return switch(piece.getPieceType()){
                case ChessPiece.PieceType.KING -> EscapeSequences.WHITE_KING;
                case ChessPiece.PieceType.QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ChessPiece.PieceType.ROOK -> EscapeSequences.WHITE_ROOK;
                case ChessPiece.PieceType.KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ChessPiece.PieceType.BISHOP -> EscapeSequences.WHITE_BISHOP;
                case ChessPiece.PieceType.PAWN -> EscapeSequences.WHITE_PAWN;
                default -> " ";
            };
        }else{
            return switch(piece.getPieceType()){
                case ChessPiece.PieceType.KING -> EscapeSequences.BLACK_KING;
                case ChessPiece.PieceType.QUEEN -> EscapeSequences.BLACK_QUEEN;
                case ChessPiece.PieceType.ROOK -> EscapeSequences.BLACK_ROOK;
                case ChessPiece.PieceType.KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ChessPiece.PieceType.BISHOP -> EscapeSequences.BLACK_BISHOP;
                case ChessPiece.PieceType.PAWN -> EscapeSequences.BLACK_PAWN;
                default -> " ";
            };
        }
    }

    private static void printBorder(String content){
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + " "
                + content + " " + EscapeSequences.RESET_BG_COLOR);
    }

    private static void printWhite(String content, String textColor){
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + textColor
                + content + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    private static void printBlack(String content, String textColor){
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + textColor
                + content + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    private static void letterRow(int step, int start, int end){
        printBorder(" ");
        for(int i = start; i != end; i += step){
            printBorder(letters[i]);
        }
        printBorder(" ");
        System.out.println();
    }
}
