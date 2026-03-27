# Moss Keeper Mod — Claude Code Briefing

## Project Identity
- Mod name: Moss Keeper
- Mod ID: moss-keeper  
- Package: com.izzulnajmi
- Loader: Fabric
- Minecraft version: 1.21.11
- Java version: 25
- Language: Java only, no Kotlin

## The Mod — What We're Building
A passive creature called the Moss Keeper slowly grows moss on any 
structure it lives near. Leave it alone long enough and your base 
looks ancient and overgrown — beautifully.

### Core mechanics
- Small tortoise-like passive mob with a mossy shell
- Cannot be tamed — only invited (place mossy cobblestone near base)
- Converts nearby blocks to mossy variants over time using server ticks
- 4 growth stages: Settling → Awakening → Claiming → Ancient
- Bone meal speeds growth, shears pauses it
- Silk touch pickaxe moves the Keeper
- On death: all converted blocks slowly revert over 2 in-game days
- Stage data saved in NBT on the entity

## Workflow Rules
- After every feature, run: ./gradlew build
- Fix ALL compile errors before moving to next feature
- After successful build, commit with message format:
  feat: [feature name] - [one line description]
- Push to origin main after every commit
- Never leave the codebase in a broken state

## File Structure
- Entity class: src/main/java/com/izzulnajmi/entity/MossKeeperEntity.java
- AI goals: src/main/java/com/izzulnajmi/entity/ai/
- Registry: src/main/java/com/izzulnajmi/registry/ModEntities.java
- Main mod: src/main/java/com/izzulnajmi/MossKeeper.java
- Textures: src/main/resources/assets/moss-keeper/textures/entity/
- Models: src/main/resources/assets/moss-keeper/geo/

## Common Mistakes to Avoid
- Do NOT use NeoForge APIs — this is Fabric only
- Do NOT reference 1.20 or older Minecraft APIs
- Always use Yarn mappings, not Mojang mappings
- Block scanning radius max 16 blocks for performance
- Only scan every 200 ticks minimum — not every tick
- Build artifacts are in build/ — never commit these

## Build Command
./gradlew build

## Run Client Command  
./gradlew runClient