package chess;

import java.awt.*;

public class ChessColor {
    private ColorPalette palette = new ColorPalette(
            Color.decode("#FFFFFF"), Color.decode("#000000"),
            Color.decode("#302E2B"),
            Color.decode("#F9F9F9"), Color.decode("#343534"),
            Color.decode("#d8daa4"), Color.decode("#739552"),
            Color.decode("#F5F580"), Color.decode("#B9CA42"),
            Color.decode("#82CBBA"), Color.decode("#46A07C"),
            Color.decode("#4EB7B4"), Color.decode("#302D29"),
            Color.decode("#F73E2C"), Color.decode("#F73E2C"));
    private Color foreground = palette.lightText;
    private Color background = palette.surface;
    private Highlight highlight = Highlight.NONE;
    private SquareColor squareColor = SquareColor.NONE;


    public enum Highlight {
        NONE,
        PRIMARY,
        SECONDARY,
        TERNARY,
        ERROR
    }

    public enum SquareColor {
        NONE,
        LIGHT,
        DARK
    }

    public record ColorPalette(
            Color lightText, Color darkText,
            Color surface,
            Color lightPiece, Color darkPiece,
            Color lightSquare, Color darkSquare,
            Color lightPrimary, Color darkPrimary,
            Color lightSecondary, Color darkSecondary,
            Color lightTernary, Color darkTernary,
            Color lightError, Color darkError) {
    }

    public ChessColor() {
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public ColorPalette getColorPalette() {
        return this.palette;
    }

    public void setColorPalette(ColorPalette palette) {
        this.palette = palette;
        calculate();
    }

    private void calculate() {
        this.background = switch (squareColor) {
            case NONE -> switch (highlight) {
                case NONE -> palette.surface;
                case PRIMARY -> palette.lightPrimary;
                case SECONDARY -> palette.lightSecondary;
                case TERNARY -> palette.lightTernary;
                case ERROR -> palette.lightError;
            };
            case LIGHT -> switch (highlight) {
                case NONE -> palette.lightSquare;
                case PRIMARY -> palette.lightPrimary;
                case SECONDARY -> palette.lightSecondary;
                case TERNARY -> palette.lightTernary;
                case ERROR -> palette.lightError;
            };
            case DARK -> switch (highlight) {
                case NONE -> palette.darkSquare;
                case PRIMARY -> palette.darkPrimary;
                case SECONDARY -> palette.darkSecondary;
                case TERNARY -> palette.darkTernary;
                case ERROR -> palette.darkError;
            };
        };
    }

    private String foregroundString() {
        return "38;2;" + foreground.getRed() + ";" + foreground.getGreen() + ";" + foreground.getBlue();
    }

    private String backgroundString() {
        return "48;2;" + background.getRed() + ";" + background.getGreen() + ";" + background.getBlue();
    }

    @Override
    public String toString() {
        return "\u001B[" + foregroundString() + ";" + backgroundString() + "m";
    }

    // foreground modifiers
    public ChessColor lightPiece() {
        this.foreground = palette.lightPiece;
        return this;
    }

    public ChessColor darkPiece() {
        this.foreground = palette.darkPiece;
        return this;
    }

    public ChessColor lightText() {
        this.foreground = palette.lightText;
        return this;
    }

    public ChessColor darkText() {
        this.foreground = palette.darkText;
        return this;
    }


    // background modifiers
    public ChessColor lightSquare() {
        squareColor = SquareColor.LIGHT;
        calculate();
        return this;
    }

    public ChessColor darkSquare() {
        squareColor = SquareColor.DARK;
        calculate();
        return this;
    }

    public ChessColor noSquare() {
        squareColor = SquareColor.NONE;
        calculate();
        return this;
    }

    public ChessColor primaryHighlight() {
        highlight = Highlight.PRIMARY;
        calculate();
        return this;
    }

    public ChessColor secondaryHighlight() {
        highlight = Highlight.SECONDARY;
        calculate();
        return this;
    }

    public ChessColor ternaryHighlight() {
        highlight = Highlight.TERNARY;
        calculate();
        return this;
    }

    public ChessColor errorHighlight() {
        highlight = Highlight.ERROR;
        calculate();
        return this;
    }

    public ChessColor noHighlight() {
        highlight = Highlight.NONE;
        calculate();
        return this;
    }

    public String getResetString() {
        return "\033[0m";
    }
}
