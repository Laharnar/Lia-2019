package lia.api;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;

public class BulletInView {
    float x;
    float y;
    float orientation;
    float velocity;

    public BulletInView(float x, float y,
                        float orientation, float velocity) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.velocity = velocity;
    }

    Vec2d getDirection(){
        return new Vec2d(Math.cos(Math.toRadians(orientation)), Math.sin(Math.toRadians(orientation)));
    }
}
