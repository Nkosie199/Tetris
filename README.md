# Tetris

Classic Tetris, made with [libGDX](https://libgdx.com/). It runs in a browser, on a desktop and on Android.

**Play it:** https://nkosie199.github.io/Tetris/

## How it plays

Pieces fall into a 10-wide board. Move and rotate them to fill whole rows; a full row clears and scores 50 points per
line. Every 10 lines the level goes up and pieces fall faster (0.8 seconds per row at level 1, 15% quicker each
level). The game ends when a new piece has no room.

## Controls

| | Keyboard (desktop and browser) | Touch (phones and tablets) |
|---|---|---|
| Move left / right | Left / Right (hold to repeat) | Tap the left or right third of the board |
| Rotate | Up | Tap the middle of the board |
| Fall faster | Down (hold) | |
| Drop straight down | Space | Tap the bottom fifth of the board |
| Pause / carry on | P or Esc | Tap the side panel |
| Play again after game over | Enter | Tap anywhere |

## Platforms

- **Browser** (what LuckyB players get): fills the window and keeps the board's shape. On a desktop browser, click the
  game once so it gets the keyboard. On a phone use the touch controls; one tap is one move.
- **Desktop** (Windows, macOS, Linux): a 416x588 window.
- **Android**: same touch controls; needs the Android SDK to build.

## Build and run

- Desktop: `gradlew lwjgl3:run` (Windows: `gradlew.bat lwjgl3:run`).
- Browser: `gradlew html:dist` builds into `html/build/dist`; serve it, e.g.
  `python -m http.server 8090 --directory html/build/dist`, then open http://localhost:8090.
- Android: `gradlew android:installDebug android:run`.
- Tests: `gradlew core:test` checks the game rules in `core/src/main/java/com/mygdx/tetris/Board.java`.

## Deploy

Every push to `master` runs `.github/workflows/web.yml`, which builds the browser version and pushes it to the
`gh-pages` branch. GitHub Pages serves it at https://nkosie199.github.io/Tetris/ (Pages is turned on in the repo's
Settings → Pages: deploy from branch `gh-pages`, `/ (root)`).

**Put it on LuckyB:** Profile → Publish a Game, Launch Mode **New Tab**, the link above as Launch URL, 1 COA. The full
process is in LuckyB's `docs/HOST_YOUR_GAME.md`.

## Project layout

The project was generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

### Modules

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.
- `ios`: iOS mobile platform using RoboVM.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

### Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
