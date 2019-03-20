package ar.edu.itba.sia.Ohn0;

import java.util.Objects;

public class Cell {
    private Boolean fixed;
    private Integer value;
    private Color color;

    public Cell(Boolean fixed, Integer value, Color color) {
        this.fixed = fixed;
        this.value = value;
        this.color = color;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (fixed && color == Color.BLUE) {
            builder.append(value);
        }
        else if (color == Color.RED) {
            builder.append("X");
        }
        else if (color == Color.BLUE) {
            builder.append("O");
        }
        else if (color == Color.WHITE) {
            builder.append(".");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return fixed == cell.fixed &&
                value == cell.value &&
                color.equals(cell.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fixed, value, color);
    }

    public Boolean getFixed() {
        return fixed;
    }

    public Integer getValue() {
        if (fixed && color.equals(Color.BLUE)) {
            return value;
        }
        return 0;
    }

    public Color getColor() {
        return color;
    }

    public boolean isBlank() {
        return color.equals(Color.WHITE);
    }
}
