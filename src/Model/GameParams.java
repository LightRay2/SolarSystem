package Model;

/**
 * Created by lost on 09.12.14.
 */
class GameParams{
    Point3 worldSize = new Point3(1000,1000,500);
    Point3 shipSize=new Point3(10,6,5); //х соответствует длине
    double shipSpeed= 0.05; //за кадр, надо подобрать
    double fastShellSpeed = 0.8,
            slowShellSpeed = 0.3;

    double smallestPlanetRadius = 20,
            rangePlanetRadius = 100; //размеры будут в этом диапазоне
    int planetColorCount=3; //пока 3 цвета
}
