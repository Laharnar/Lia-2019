import lia.api.UnitData;

public class UnitInfo {


    // different layers of responding
    UnitMechanics mechanics = new UnitMechanics();
    UnitNavigation navigation;


    public UnitData unitState;
    public boolean shoot;

    // IDEA: maybe predict for movement when it should begin rotating in the future again, just to have
    // bullet ready?
    //public float nextReloadTime;
    //unit.nextReloadTime = Constants.GAME_DURATION+Constants.RELOAD_TIME;
    //if (Constants.GAME_DURATION >= unit.nextReloadTime){ // we know it reloaded 1

}
