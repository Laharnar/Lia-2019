
import com.sun.javafx.geom.Vec2d;
import lia.Constants;
import lia.MathUtil;
import lia.api.OpponentInView;
import lia.api.Rotation;
import lia.api.Speed;


public class UnitMechanics{

    public boolean ManeveurNearClosestEnemy(UnitInfo unit, float preferredDistance){
        OpponentInView[] opponents =  unit.unitState.opponentsInView;
        if (opponents.length == 0) return false;

        float minDist = 1000000000f;
        int bestChoice = -1;
        for (int i = 0; i < opponents.length; i++) {
            float dist = MathUtil.distance(opponents[i].x, opponents[i].y, unit.unitState.x, unit.unitState.y);
            if (dist < minDist)
            {
                minDist = dist;
                bestChoice = i;
            }
        }

        int enemyId = bestChoice;
        Vec2d enemy = new Vec2d(opponents[enemyId].x, opponents[enemyId].y);
        CommandRotation(unit, RotationToTurnToPoint(unit, enemy));
        CommandMove(unit, SpeedToMoveNearPoint(unit, enemy, preferredDistance));
        return true;
    }

    public Speed SpeedToMoveNearPoint(UnitInfo unit, Vec2d pt, float preferedDist) {
        System.out.println("[Untested]CommandMoveToPtAhead");
        // assumed point is either directly agead or directly behind
        float dist = MathUtil.distance(unit.unitState.x, unit.unitState.y, (float)pt.x, (float)pt.y);

        double dotProd = UnitHelper.GetLen(pt)*UnitHelper.GetLen(UnitHelper.GetPos(unit.unitState))
                * Math.cos(UnitHelper.GetRightAngle(unit.unitState.orientationAngle));

        float goTo  = preferedDist;
        boolean ahead = dotProd > goTo;
        Speed speed = Speed.NONE;
        if (dist > goTo && ahead && dotProd != goTo) {

            if (Constants.UNIT_FORWARD_VELOCITY > dist){
                speed = Speed.FORWARD;
            }
            else if (Constants.UNIT_FORWARD_VELOCITY > dist){
                speed = Speed.BACKWARD;
            }
        }
        return speed;
    }

    public Speed SpeedToMoveToPoint(UnitInfo unit, Vec2d pt) {
        System.out.println("[Untested]CommandMoveToPtAhead");
        // assumed point is either directly agead or directly behind
        float dist = MathUtil.distance(unit.unitState.x, unit.unitState.y, (float)pt.x, (float)pt.y);

        double dotProd = UnitHelper.GetLen(pt)*UnitHelper.GetLen(UnitHelper.GetPos(unit.unitState))
                * Math.cos(UnitHelper.GetRightAngle(unit.unitState.orientationAngle));

        boolean ahead = dotProd > 0;
        Speed speed = Speed.NONE;
        if (dist > 0 && ahead && dotProd != 0) {

            if (Constants.UNIT_FORWARD_VELOCITY > dist){
                speed = Speed.FORWARD;
            }
            else if (Constants.UNIT_FORWARD_VELOCITY > dist){
                speed = Speed.BACKWARD;
            }
        }
        return speed;
    }

    public Rotation RotationToTurnToPoint(UnitInfo unit, Vec2d pt){
        System.out.println("[Untested]RotationToTurnToPoint");

        float angle = MathUtil.angleBetweenUnitAndPoint(unit.unitState, (float)pt.x, (float)pt.y);
        Rotation rotation= Rotation.NONE;
        if (angle < 0) {
            if (Constants.UNIT_ROTATION_VELOCITY > Math.abs(angle))
                rotation = Rotation.SLOW_RIGHT;
            if (Constants.UNIT_SLOW_ROTATION_VELOCITY > Math.abs(angle))
                rotation = Rotation.NONE;
            else
                rotation = Rotation.RIGHT;
        }else if (angle > 0) {
            if (Constants.UNIT_ROTATION_VELOCITY > Math.abs(angle))
                rotation = Rotation.SLOW_LEFT;
            else if (Constants.UNIT_SLOW_ROTATION_VELOCITY > Math.abs(angle))
                rotation = Rotation.NONE;
            else
                rotation = Rotation.LEFT;
        }
        return rotation;
    }

    public void CommandMove(UnitInfo unit, Speed speed){
        CombatManager.api.setSpeed(unit.unitState.id, speed);
    }

    public void CommandRotation(UnitInfo unit, Rotation rotation){
        CombatManager.api.setRotation(unit.unitState.id, rotation);
    }

    public void CommandShoot(UnitInfo unit, FiringTactics tactics){
        System.out.println("[Untested]CommandShoot");

        switch (tactics){
            case HoldFire:
                unit.shoot = false;
                break;
            case OneBullet:
                if(unit.unitState.nBullets == Constants.MAX_BULLETS){
                    unit.shoot = true;
                }else
                    unit.shoot = false;
                break;
            case KeepOne:
                if(unit.unitState.nBullets > 1){
                    unit.shoot = true;
                }else
                    unit.shoot = false;
                break;
            case EmptyAll:
                if(unit.unitState.nBullets > 0){
                    unit.shoot = true;
                }else
                    unit.shoot = false;
                break;
            case EmptyHalf:
                if(unit.unitState.nBullets > Constants.MAX_BULLETS/2){
                    unit.shoot = true;
                }else
                    unit.shoot = false;
                break;
        }
        if (unit.shoot) {
            CombatManager.api.shoot(unit.unitState.id);
        }
    }

    enum FiringTactics{ HoldFire, OneBullet, EmptyAll, KeepOne, EmptyHalf  }// optimize on enemy hits
}
