package lia;

import lia.api.GameEnvironment;
import lia.api.GameState;

public interface Bot {
    void processGameEnvironment(GameEnvironment gameEnvironment);
    void processGameState(GameState gameState, Api response);
}
