package me.theblueducky.animalStarsMCAPI.game;

/**
 * Abstract base for all gamemodes (e.g. Showdown, Gem Grab, Knockout).
 * Concrete gamemodes are implemented by gamemode plugins and registered via
 * {@link GameManager#registerGamemode(Gamemode)}.
 *
 * Lifecycle: onInit -> onStart -> (onTick loop) -> checkWinCondition -> onEnd
 */
public abstract class Gamemode {

    private final String id;
    private final String name;
    private final GameObjectiveType objectiveType;

    private final int teamSize;   // 1 = FFA, 3 = 3v3
    private final int teamCount;  // number of teams
    private final int minPlayers;
    private final int maxPlayers;
    private final int durationTicks; // 0 = no time limit

    protected Gamemode(String id, String name, GameObjectiveType objectiveType,
                       int teamSize, int teamCount, int minPlayers, int maxPlayers, int durationTicks) {
        this.id = id;
        this.name = name;
        this.objectiveType = objectiveType;
        this.teamSize = teamSize;
        this.teamCount = teamCount;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.durationTicks = durationTicks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GameObjectiveType getObjectiveType() {
        return objectiveType;
    }

    /** Players per team (1 for FFA). */
    public int getTeamSize() {
        return teamSize;
    }

    /** Number of teams in the match. */
    public int getTeamCount() {
        return teamCount;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    /** Total match duration in ticks (0 = unlimited). */
    public int getDurationTicks() {
        return durationTicks;
    }

    /** Called once when a session is created for this gamemode. */
    public void onInit(GameSession session) {
    }

    /** Called when the match starts (players spawned). */
    public void onStart(GameSession session) {
    }

    /** Called every game tick while PLAYING. */
    public void onTick(GameSession session, int tick) {
    }

    /** Called when the match ends. */
    public void onEnd(GameSession session, WinResult result) {
    }

    /**
     * Evaluate the win condition.
     * @return a {@link WinResult} if the game is over, otherwise null.
     */
    public abstract WinResult checkWinCondition(GameSession session);
}
