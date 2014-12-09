package Model;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lost on 09.12.14.
 */



class Game {

    enum PlayerAction{turnLeft, turnRight, turnUp, turnDown, turnClockwise, turnCounterClockwise,
        selectLaserGun, selectFastGun, selectSlowGun,
        shoot
    }

    GameParams gameParams = new GameParams();
    Driver driver = new Driver();

    ArrayList<Planet> planets;
    ArrayList<Shell> shells;
    ArrayList<Ship> ships;

    Ship controlled;

    int currentTick=0;


    //единицы измерения - масса 1 = массе снаряда,
    //а планета массой 1 имеет радиус 1.
    //начало координат - рисуем параллелепипед на бумаге, это левая нижняя ближняя точка
    Game(){
        ArrayList gunset = new ArrayList();

        //создаем корабли в разных концах параллелепипеда
        //первый едет за плоскость, второй из-за плоскости
        //пушки у обоих на правом борту
        ships.add(new Ship(10, new Point3(0,0,0)
                , new Point3(0,0,gameParams.shipSpeed),
                0, createGunset() ));
        ships.add(new Ship(10, gameParams.worldSize.Copy()
                , new Point3(0,0,-gameParams.shipSpeed),
                1, createGunset() ));

        shells = new ArrayList<Shell>();

        //добавляем по 5 планет в 4 параллелограмма (для равномерности)
        planets = new ArrayList<Planet>();
        double x = gameParams.worldSize.x/2,
                y = gameParams.worldSize.y/2,
                z = gameParams.worldSize.z;

        addPlanets(new Point3(0,0,0), new Point3(x,y,z), 5 );
        addPlanets(new Point3(x,0,0), new Point3(x,y,z), 5 );
        addPlanets(new Point3(0,y,0), new Point3(x,y,z), 5 );
        addPlanets(new Point3(x,y,0), new Point3(x,y,z), 5 );
    }

    void AddPlayerAction(int teamNumber, PlayerAction action )
    {
        ships.get(teamNumber).actionQueue.add(action);
    }

    //тут все действия, которые происходят каждый тик
    void UpdateWorld(){
        //осуществляем накопившиеся действия
        //todo

        //двигаем снаряды и корабли
        driver.MoveShips(ships,planets);
        driver.MoveShells(shells,planets);

        //уничтожаем все, что далеко или столкнулось
        //todo
    }

    ArrayList<Sprite3D> getSpritesToDraw(){
        ArrayList<Sprite3D> r = new ArrayList<Sprite3D>();
        for(int i = 0; i < ships.size();i++){
            Ship p = ships.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    gameParams.shipSize.Copy(),
                    0));
        }
        for(int i = 0; i < planets.size();i++){
            Planet p = planets.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    new Point3(p.radius*2,p.radius*2,p.radius*2),
                    1));
        }
        for(int i = 0; i < shells.size();i++){
            Shell p = shells.get(i);
            r.add(new Sprite3D(p.position.Copy(),
                    new Point3(p.radius*2,p.radius*2,p.radius*2),
                    1));
        }

        return r;
    }

    private ArrayList<Gun> createGunset(){
        //пока разница только в расположении относительно корабля
        ArrayList<Gun> gunset = new ArrayList<Gun>();
        gunset.add(new Gun(Gun.GunType.laser, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(-gameParams.shipSize.x/3, gameParams.shipSize.y/2, 0)
        ));
        gunset.add(new Gun(Gun.GunType.fast, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(0, gameParams.shipSize.y/2, 0)
        ));
        gunset.add(new Gun(Gun.GunType.slow, 10000,50,
                new Point3(1,0,0),//нацелена вправо
                new Point3(gameParams.shipSize.x/3, gameParams.shipSize.y/2, 0)
        ));
        return gunset;
    }

    private void addPlanets(Point3 startPoint, Point3 parallSize, int count  ){
        Random rand = new Random();
        int maxLoopEntries = 10000; //чтоб бесконечно не крутился
        while(count > 0 && maxLoopEntries>0){
            maxLoopEntries--;

            Point3 randPoint = new Point3(
                    rand.nextDouble()*parallSize.x,
                    rand.nextDouble()*parallSize.y,
                    rand.nextDouble()*parallSize.z
            );

            double newRadius = gameParams.smallestPlanetRadius + rand.nextDouble()*(gameParams.rangePlanetRadius);

            Point3 newPosition = startPoint.Add(randPoint);

            boolean canBeCreatedHere=true;
            for(int i = 0; i < planets.size();i++){
                if(newPosition.distTo(planets.get(i).position) <= newRadius + planets.get(i).radius)
                {
                    canBeCreatedHere=false;
                    break;
                }
            }

            if(canBeCreatedHere){
                count--;
                planets.add(new Planet(newPosition, newRadius
                        , rand.nextInt(gameParams.planetColorCount)));
            }
        }
    }

}
