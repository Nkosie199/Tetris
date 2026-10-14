# Tetris

Classic Tetris, made with [libGDX](https://libgdx.com/). It runs in a browser, on a desktop and on Android.

**Play it:** https://nkosie199.github.io/Tetris/ (rebuilt and published by GitHub on every push to `master`, see
`.github/workflows/web.yml`).

**Controls:** arrows move, Up rotates, Down drops faster, Space drops, P pauses, Enter starts again after game over.
On a phone, tap the left or right of the board to move, the middle to rotate, the bottom to drop, and the side panel
to pause.

**Put it on LuckyB:** Profile → Publish a Game → **New Tab**, and use the link above as the Launch URL.

**Run it on this computer:** `gradlew lwjgl3:run` (Windows: `gradlew.bat lwjgl3:run`). To try the browser version
locally, `gradlew html:dist` builds it into `html/build/dist`.

**Tests:** `gradlew core:test` checks the game rules in `core/src/main/java/com/mygdx/tetris/Board.java`.

The project was generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.
- `ios`: iOS mobile platform using RoboVM.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

## Gradle

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

To build...
./gradlew clean build

To run...
Desktop:
./gradlew lwjgl3:run

Android:
./gradlew android:installDebug android:run

iOS:
./gradlew ios:launchIPhoneSimulator

HTML:
./gradlew html:superDev
Then go to http://localhost:8080/index.html.