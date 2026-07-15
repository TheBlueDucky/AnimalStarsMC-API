# AnimalStarsMC API

A Spigot/Bukkit **framework API** for an Animal Stars (Brawl Stars-like) minigame network.
This plugin is the shared dependency for the **core** plugin and the **gamemode** plugins.

> Playable characters are called **Animals** (not "brawlers").

## Modules

| Package | Purpose |
|---------|---------|
| `animal` | `Animal` (health, speed, rarity, attack, super), `Gadget`, `StarPower`, `AnimalInstance` (per-player in-game state), `AnimalManager` registry |
| `team` | `Team` + `TeamManager` (supports FFA `teamSize=1` and 3v3) |
| `game` | `Gamemode` framework, `GameSession`, `GameArena`, `GameState`, `GameObjectiveType`, `WinResult`, `Lobby`, `QueueManager`, `GameManager` |
| `player` | `PlayerStats` + `PlayerManager` |
| `event` | All custom events (start/end, kill, join/leave, damage, death, tick, score, animal select, queue/match) |
| `data` | `DataManager` (flatfile YAML persistence) |
| `utils` | `ItemBuilder`, `Messages`, `ScoreboardBuilder` (HUD) |

## For consumers (core / gamemode plugins)

1. Add the API as a dependency (install it to your local Maven repo or shade it).
   ```xml
   <dependency>
       <groupId>me.theblueducky</groupId>
       <artifactId>AnimalStarsMCAPI</artifactId>
       <version>1.0-SNAPSHOT</version>
       <scope>provided</scope>
   </dependency>
   ```
2. Make your plugin `depend: [AnimalStarsMCAPI]` in `plugin.yml` (the API loads first).
3. Access the API via `AnimalStarsMCAPI.getInstance()` and the manager singletons
   (`GameManager.getInstance()`, `AnimalManager.getInstance()`, `QueueManager.getInstance()`, ...).

## Registering an Animal (in your core/gamemode plugin `onEnable`)

```java
Animal fox = new Animal("fox", "Fox", Material.FOX_SPAWN_EGG, Rarity.EPIC,
        3200, 1.0, 3, 100, 600, 5.0, 800) { // id, name, icon, rarity, hp, speed, ammo, superMax, dmg, range, cooldown
    @Override public void performAttack(Player p, AnimalInstance i) { /* shoot */ }
    @Override public void performSuper(Player p, AnimalInstance i) { /* ultimate */ }
};
fox.registerGadget(new Gadget("fox_dash", "Dash", "Quick dash", 8000) {
    @Override public void onActivate(Player p, AnimalInstance i) { /* ... */ }
});
fox.registerStarPower(new StarPower("fox_speed", "Swift", "Faster") {
    @Override public void onEquip(AnimalInstance i) { i.setMaxHealth(i.getMaxHealth() + 200); }
});
AnimalManager.getInstance().registerAnimal(fox);
```

## Registering a Gamemode

```java
Gamemode showdown = new Gamemode(
        "showdown", "Showdown", GameObjectiveType.LAST_STANDING,
        1, 8, 8, 8, 20 * 180) { // id, name, objective, teamSize, teamCount, min, max, durationTicks
    @Override
    public WinResult checkWinCondition(GameSession s) {
        if (s.getAliveCount() <= 1) {
            Player last = Bukkit.getPlayer(s.getAlivePlayers().iterator().next());
            return WinResult.playerWin(last, "last_standing");
        }
        return null;
    }
};
GameManager.getInstance().registerGamemode(showdown);
```

## Queuing a player (matchmaking)

```java
// Player selects an animal, then queues for a gamemode + arena
QueueManager.getInstance().queuePlayer("showdown", "arena1", player, AnimalManager.getInstance().getAnimal("fox"));
// When enough players queue, a GameSession is created, teams auto-balanced, and MatchFoundEvent fires.
```

## Listening to events

```java
@EventHandler
public void onKill(PlayerKillEvent e) { /* e.getKiller(), e.getVictim() */ }
@EventHandler
public void onTick(GameTickEvent e) { /* update HUD via ScoreboardBuilder */ }
@EventHandler
public void onMatch(MatchFoundEvent e) { GameSession s = e.getSession(); }
```

## Persistence

Player stats and arena configs are saved to `plugins/AnimalStarsMC-API/data/players.yml`
and `.../arenas.yml` automatically on disable (loaded on enable). Arenas are configured
via `GameArena` (per-team spawns, world, bounds) and serialized with `GameArena.serialize/deserialize`.

## Build

```bash
mvn clean package
```

The compiled jar is `target/AnimalStarsMCAPI-1.0-SNAPSHOT.jar`.
