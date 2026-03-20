# 🏺 Temple Runes

**Team Members:**  
- Siddhant Jain (301596400)  
- Tsz Him Lee (301637517)  
- Daniil Simonian (301609164)  
- Jinglin Zhang (301568883)

# Temple Runes 🗝️
A JavaFX top‑down adventure where you guide the Seeker through trap‑laden ruins, collect mystical runes, and outsmart sentient guardians using carefully crafted navigation logic.

## Prerequisites
- **Java 17+** (Adoptium Temurin or Oracle JDK)
- **Maven 3.8+**
- **JavaFX 20 runtime**  
  - If your JDK doesn’t bundle JavaFX, install the JavaFX SDK and either set `PATH_TO_FX` (for `mvn javafx:run`) or copy the modules into your Maven repo.

## Project Layout
```
TempleRunes/
├── pom.xml                   # Maven build + plugin config
├── src/main/java             # App entry point, domain logic, UI
├── src/main/resources        # FXML, images, audio, fonts, levels
└── src/test/java             # Unit + integration tests
```

## Building
```bash
mvn clean verify
```

## Running the Game
```bash
cd TempleRunes
mvn javafx:run
```
> On systems without JavaFX in the JDK, set `PATH_TO_FX` or pass `--module-path` & `--add-modules javafx.controls,javafx.fxml`.

## Running Tests
```bash
mvn test
```
Reports:
- `target/surefire-reports` – JUnit results
- `target/site/jacoco/index.html` – JaCoCo coverage (run `mvn test jacoco:report`)
- `target/reports/apidocs/index.html` - JavaDoc (run mvn javadoc:javadoc)


## Troubleshooting
- **JavaFX media download errors:** ensure Maven can write to `~/.m2`; rerun `mvn clean`.
- **Graphics issues on macOS/ARM:** try `-Dprism.order=sw` or a JavaFX-enabled JDK.
- **Git merge conflicts:** resolve, `git add` each file, then `git commit` to finalize.

Happy exploring Temple Runes!
