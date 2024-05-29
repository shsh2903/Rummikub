import java.util.Comparator;

class TileComparator implements Comparator<Tile1> {
    @Override
    public int compare(Tile1 t1, Tile1 t2) {
        int colorComparison = t1.getColor().toString().compareTo(t2.getColor().toString());
        if (colorComparison != 0) {
            return colorComparison;
        }
        return Integer.compare(t1.getNumber(), t2.getNumber());
    }
}