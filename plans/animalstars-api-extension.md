# AnimalStarsMC API — Extension Plan (Animal Stars, Brawl Stars-like)

## Goal
Extend the current API so it can serve as the shared dependency for the **core** and **gamemode** plugins of an Animal Stars (Brawl Stars-like) game, where playable characters are **Animals** rather than Brawlers.

## Current State (what exists)
- `GameManager` (string-keyed game modes, flat `List<Player>`)
- `GameState` enum (WAITING/STARTING/PLAYING/ENDING/ENDED)
- `GameArena` (hardcoded spawn1..4, no world/bounds/serialization)
- `PlayerStats` + `PlayerManager` (in-memory only)
- Events: `GameStartEvent`, `GameEndEvent`, `PlayerKillEvent`
- Utils: `ItemBuilder`, `Messages`

## Decisions (confirmed with user)
1. Brawler/Animal + Gamemode interfaces live **in the API**.
2. Persistence = **Flatfile (YAML)**.
3. Gamemodes to support first: **Showdown (8 FFA)**, **Gem Grab (3v3)**, **Knockout (3v3)**.

## Target Architecture

```mermaid
graph TD
    API[AnimalStarsMCAPI plugin] --> AM[AnimalManager registry]
    API --> GM[GameManager]
    API --> TM[TeamManager]
    API --> QM[QueueManager lobby/matchmaking]
    API --> DM[DataManager flatfile]
    API --> EM[Events]

    AM --> A[Animal abstract: health, speed, rarity, attack, super]
    A --> G[Gadget]
    A --> SP[StarPower]
    A --> AI[AnimalInstance: hp, ammo, cooldowns]

    GM --> GS[GameSession: gamemode, arena, teams, timer, objective]
    GS --> GA[GameArena: team spawns, world, bounds, serialize]
    GS --> T[Team: id, color, members, spawn, score]
    GS --> GAM[Gamemode abstract: onInit/onStart/onTick/onEnd + checkWinCondition]
    GAM --> OT[GameObjectiveType: ELIMINATION, COLLECT, LAST_STANDING]

    DM --> PS[playerdata.yml: PlayerStats]
    DM --> AC[arenas.yml: GameArena config]

    EM --> E1[PlayerJoinGameEvent]
    EM --> E2[PlayerLeaveGameEvent]
    EM --> E3[PlayerDamageEvent]
    EM --> E4[PlayerDeathEvent]
    EM --> E5[GameTickEvent]
    EM --> E6[ScoreChangeEvent]
    EM --> E7[BrawlerSelectEvent]
```

## Game flow (Showdown / Gem Grab / Knockout)

```mermaid
sequenceDiagram
    participant P as Player
    participant Core as Core Plugin
    participant API as GameManager
    participant S as GameSession
    participant G as Gamemode

    P->>Core: /play showdown
    Core->>API: queuePlayer(gamemode, player)
    API->>QM: add to Lobby -> PlayerQueueEvent
    QM->>QM: min players reached?
    QM->>S: create GameSession + auto-balance teams
    QM->>EM: MatchFoundEvent
    P->>API: joinSession -> PlayerJoinGameEvent
    API->>S: assign Team (FFA=size1 / 3v3=size3)
    S->>G: onStart
    G->>S: spawn players at team spawns
    loop every tick
        G->>S: onTick -> GameTickEvent
        G->>S: checkWinCondition
    end
    G->>S: onEnd -> GameEndEvent
    API->>DM: save PlayerStats
```

## Work Breakdown (todo list)

1. **Team system** — `Team` (id, color, members, spawn, score) + `TeamManager`. Supports FFA (teamSize 1) and 3v3.
2. **Animal abstraction** — abstract `Animal` (health, move speed, rarity, attack, super), `Gadget`, `StarPower`, `AnimalManager` registry, `AnimalInstance` (per-player hp/ammo/cooldowns).
3. **Gamemode framework** — abstract `Gamemode` (onInit/onStart/onTick/onEnd + checkWinCondition), `GameObjectiveType` enum, registration in `GameManager`.
4. **Refactor GameArena** — per-team spawns, world reference, bounds/region, YAML serialize/deserialize.
5. **Refactor GameManager + GameSession** — replace string maps with `GameSession` objects tying gamemode + arena + teams + timer + objective state.
6. **Lobby/Queue/Matchmaking** — `Lobby` per gamemode (waiting players), `QueueManager` (auto-balance into teams/sessions when min players reached), events `PlayerQueueEvent`, `PlayerDequeueEvent`, `MatchFoundEvent`.
7. **Missing events** — PlayerJoinGame, PlayerLeaveGame, PlayerDamage, PlayerDeath, GameTick, ScoreChange, BrawlerSelect.
7. **Flatfile persistence** — `DataManager` saving/loading `PlayerStats` and arena configs under `plugins/AnimalStarsMC-API/data`.
8. **HUD/Scoreboard framework** — `ScoreboardBuilder` for timers, scores, brawler info.
9. **Usage docs/example** — how core + gamemode plugins `depend: [AnimalStarsMC-API]`, register brawlers/gamemodes, and consume events.

## Notes
- The API remains a `JavaPlugin` (loads first); core/gamemode plugins access it via `AnimalStarsMCAPI.getInstance()`.
- Existing `GameStartEvent`/`GameEndEvent`/`PlayerKillEvent` are kept; new events are additive.
- `PlayerStats` extended to persist trophies/coins per player (global for v1; per-brawler later if needed).
