package profegg;

public final class Point {
    public final int row;
    public final int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        Point that = (Point) otherObject;
        if (this.row != that.row) return false;
        if (this.col != that.col) return false;
            
        return true;
    }
}
