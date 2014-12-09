package Model;

/**
 * Created by lost on 09.12.14.
 */
public class Sprite3D {
    Point3 position, size; //координата центра
    int spriteNumber; //0-корабль, 1 - шар

    public Sprite3D(Point3 position, Point3 size, int spriteNumber) {
        this.position = position;
        this.size = size;
        this.spriteNumber = spriteNumber;
    }
}
