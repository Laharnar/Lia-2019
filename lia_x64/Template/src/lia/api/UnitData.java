package lia.api;

public class UnitData {
    public int id;
    public int health;
    public float x;
    public float y;
    public float orientationAngle;
    public Speed speed;
    public Rotation rotation;
    public boolean canShoot;
    public int nBullets;
    public OpponentInView[] opponentsInView;
    public BulletInView[] opponentBulletsInView;
    public Point[] navigationPath;

    public UnitData(int id,
                    int health,
                    float x, float y,
                    float orientationAngle,
                    Speed speed,
                    Rotation rotation,
                    boolean canShoot,
                    int nBullets,
                    OpponentInView[] opponentsInView,
                    BulletInView[] opponentBulletsInView,
                    Point[] navigationPath) {
        this.id = id;
        this.health = health;
        this.x = x;
        this.y = y;
        this.orientationAngle = orientationAngle;
        this.speed = speed;
        this.rotation = rotation;
        this.canShoot = canShoot;
        this.nBullets = nBullets;
        this.opponentsInView = opponentsInView;
        this.opponentBulletsInView = opponentBulletsInView;
        this.navigationPath = navigationPath;
    }
}

