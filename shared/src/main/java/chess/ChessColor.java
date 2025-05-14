package chess;

import java.awt.*;

public class ChessColor {
    private Color foreground;
    private Color background;
    public String reset = "\033[0m";
    public static final ColorPalette palette = new ColorPalette(
            Color.decode("#FFFFFF"), Color.decode("#000000"),
            Color.decode("#302E2B"),
            Color.decode("#F9F9F9"), Color.decode("#EBECD0"),
            Color.decode("#B9CA42"), Color.decode("#739552"),
            Color.decode("#F5F580"), Color.decode("#B9CA42"),
            Color.decode("#82CBBA"), Color.decode("#46A07C"),
            Color.decode("#4EB7B4"), Color.decode("#302D29"),
            Color.decode("#F73E2C"), Color.decode("#F73E2C"));

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
        Color lightError, Color darkError) {}

    public ChessColor(ColorPalette palette, SquareColor squareColor, Highlight highlight, Color foreground) {
        this.foreground = foreground;
        this.background = switch (squareColor) {
            case NONE -> switch (highlight) {
                case NONE -> palette.surface;
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
            case LIGHT -> switch (highlight) {
                case NONE -> palette.lightSquare;
                case PRIMARY -> palette.lightPrimary;
                case SECONDARY -> palette.lightSecondary;
                case TERNARY -> palette.lightTernary;
                case ERROR -> palette.lightError;
            };
        };

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

    public static class ChessColorBuilder {
        ColorPalette palette;
        Color foreground;
        Highlight highlight;
        SquareColor squareColor;

        public ChessColorBuilder(ColorPalette palette) {
            this.palette = palette;
            foreground = palette.lightText;
            highlight = Highlight.NONE;
            squareColor = SquareColor.NONE;
        }
        public ChessColorBuilder darkPiece() {
            this.foreground = palette.darkPiece;
            return this;
        };
        public ChessColorBuilder lightPiece() {
            this.foreground = palette.darkPiece;
            return this;
        };
        public ChessColorBuilder lightText() {
            this.foreground = palette.lightText;
            return this;
        };
        public ChessColorBuilder darkText() {
            this.foreground = palette.darkText;
            return this;
        };
        public ChessColorBuilder lightSquare() {
            squareColor = SquareColor.LIGHT;
            return this;
        };
        public ChessColorBuilder darkSquare() {
            squareColor = SquareColor.DARK;
            return this;
        };
        public ChessColorBuilder noSquare() {
            squareColor = SquareColor.NONE;
            return this;
        };
        public ChessColorBuilder primaryHighlight() {
            highlight = Highlight.PRIMARY;
            return this;
        };
        public ChessColorBuilder secondaryHighlight() {
            highlight = Highlight.SECONDARY;
            return this;
        };
        public ChessColorBuilder ternaryHighlight() {
            highlight = Highlight.TERNARY;
            return this;
        };
        public ChessColorBuilder errorHighlight() {
            highlight = Highlight.ERROR;
            return this;
        };
        public ChessColorBuilder noHighlight() {
            highlight = Highlight.NONE;
            return this;
        };
        public ChessColor build() {
            return new ChessColor(palette, squareColor, highlight, foreground);
        };
    }

}
