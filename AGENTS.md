# AGENTS.md

App Android "Marcador de Pontuação Volei" (placar de vôlei). Módulo Gradle único `:app`, Kotlin + Jetpack Compose, MVVM.

## Build / teste

- `./gradlew assembleDebug` — gera o APK
- `./gradlew test` — testes unitários (`app/src/test/`)
- `./gradlew installDebug` — instala no device/emulador conectado
- Não há config de lint, formatter, typecheck ou CI. Não há README.

Toolchain de ponta e auto-provisionado: Gradle 9.7.0, AGP 9.3.1, Kotlin 2.4.10, toolchain JDK 21 (`jvmToolchain(21)`), `compileSdk = 37`. O plugin `foojay-resolver-convention` em `settings.gradle.kts` baixa o JDK automaticamente; não é preciso instalar o JDK 21 manualmente.

## Arquitetura

- Package / namespace: `br.dev.saed.volei` (note `volei`, não `voleibr` — o projeto foi renomeado, mas o package não). A classe Application é `MercadoApplication` (nome histórico, não renomeie).
- DI: Koin. Iniciado em `MercadoApplication`; módulos em `app/src/main/java/.../model/di/ModuleForKoin.kt` (`appModule`, `storageModule`). ViewModels são obtidos com `koinViewModel()`.
- Persistência:
  - **Room** — `TeamDatabase` (`model/repositories/db/`), entidades `TeamEntity` + `WinnerEntity`, versão 2. Os schemas são exportados para `app/schemas/` (via `room.schemaDirectory`). O compilador Room está configurado com **ambos** `ksp` e `annotationProcessor` — mantenha os dois ao adicionar entidades.
  - **DataStore Preferences** — `DataStoreHelper` (`model/repositories/datastore/`), store `"settings"`. Guarda estado de pontuação/times/configurações que sobrevive à morte do processo.
- **Navigation 3** (`androidx.navigation3`, API não padrão): as rotas são `@Serializable data object ... : NavKey` (ex.: `HomeRoute`, `StatsRoute`) declaradas em `ui/navigation/*Navigation.kt`. As telas são registradas via `entryProvider`/`entry<Route>` e renderizadas com `NavDisplay` em `MainActivity`. Para adicionar uma tela, siga o padrão de `statsScreen`: objeto de rota, `entry`, registro em `MainActivity.App`.
- Os ViewModels expõem um único `MutableStateFlow` `uiState` e mutam através de um dispatcher `onEvent(...)` (`MainScreenEvent`, `StatsScreenEvent`). O estado de UI fica em `ui/state/`, as telas em `ui/screens/`.

## Convenções / pegadinhas

- Textos de UI e a maioria dos identificadores estão em português (`nome`, `pontos`, `Vibrar`, nomes padrão `"Time 1"`/`"Time 2"`). Strings devem ficar em `app/src/main/res/values/strings.xml`.
- A activity principal é travada em paisagem: `android:screenOrientation="sensorLandscape"` no `AndroidManifest.xml`, além de um toggle "manter tela ativa" no app.
- `local.properties` (caminho do SDK) está no `.gitignore`; não o commite.
- O banco Room se chama `"team_database"`; as migrações usam `fallbackToDestructiveMigration(false)` — aumente a `version` e exporte um novo schema em `app/schemas/` ao mudar entidades.
