package chess;

import java.awt.*;

public class ChessColor {
    private ColorPalette palette = new ColorPalette(
            Color.decode("#FFFFFF"), Color.decode("#000000"),
            Color.decode("#302E2B"),
            Color.decode("#F9F9F9"), Color.decode("#EBECD0"),
            Color.decode("#B9CA42"), Color.decode("#739552"),
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

    public ChessColor() {}

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
    public void lightPiece() {
        this.foreground = palette.lightPiece;
    }

    public void darkPiece() {
        this.foreground = palette.darkPiece;
    }

    public void lightText() {
        this.foreground = palette.lightText;
    }

    public void darkText() {
        this.foreground = palette.darkText;
    }


    // background modifiers
    public void lightSquare() {
        squareColor = SquareColor.LIGHT;
        calculate();

    }

    public void darkSquare() {
        squareColor = SquareColor.DARK;
        calculate();
    }

    public void noSquare() {
        squareColor = SquareColor.NONE;
        calculate();
    }

    public void primaryHighlight() {
        highlight = Highlight.PRIMARY;
        calculate();
    }

    public void secondaryHighlight() {
        highlight = Highlight.SECONDARY;
        calculate();
    }

    public void ternaryHighlight() {
        highlight = Highlight.TERNARY;
        calculate();
    }

    public void errorHighlight() {
        highlight = Highlight.ERROR;
        calculate();
    }

    public void noHighlight() {
        highlight = Highlight.NONE;
        calculate();
    }

    public String reset() {
        return "\033[0m";
    }
}
