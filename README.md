# Teleporter Tools: Fabric Mod


Here I present some simple teleporting tools:

- **Teleporter Wand**: A wand that can teleport between levels
- **Teleporter Plate**: A pressure plate that can be placed at any ground and random tp player when they activate pressure plate
- **Arrow of Teleportation**: An arrow that will random teleport an entity

Here is the feature and the future development:

- Teleporter Wand
  - This wand can be crafted using a blaze powder at the top, a stick at the middle, and a chorus fruit at the bottom of the crafting slots that are arranged in the same column. The wand has a durability of 35 uses and can be mended using the similar method of other durable tools. As of now, the teleporter wand can only teleport the user up one level if there is a non-air block (includes any fluid type) and an air block on top of the solid block. Otherwise, the player will be teleported 10.5 blocks above to the air. Each use triggers the blindness and slow-falling effect temporarily just so that this annoys the player.
  
- Teleporter Plate
  - Teleporter Plate is a random tp pressure plate whose teleportation distance can be change based on the block below the plate. Teleporter Plate can be crafted with 3 Ender Pearls in the top row, and in the row right below it, 1 Blaze Powder in the left slot and 1 in the right slot, 1 Light Weighted Pressure Plate in the middle slot. 
  - There are 7 teleportation strength:
      - BASIC: 2 to 5 blocks - place on any block that is not stated in the rest of the teleportation strength
      - WOOD: 10 to 50 blocks - place on any wood block
      - LOG: 50 to 100 blocks - place on any log block
      - IRON: 100 to 500 blocks - place on an iron block
      - GOLD: 500 to 2000 blocks - place on a gold block
      - DIAMOND: 2000 to 5000 blocks - place on a diamond block
      - EMERALD: 5000 to 12500 blocks - place on an emerald block

- Arrow of Teleportation
  - Arrow of Teleportation teleports target upon the contact of the arrow. With a normal bow, the radius of teleportation is 2 to 5 blocks. _The lower and upper limit of the range increase by 5 for each Punch enchant level. This arrow cannot be crafted and must be obtained from a skeleton drop that is killed on an End Block or any derivatives of the End Block._  
_*Italics: not yet implemented_
  
  
### Requirements

1. Minecraft Fabric Loader 1.15
2. Compatible Fabric API
3. A working computer

### How To Use
Just like any other Fabric mods, place the .jar file into the mods/ folder.

### Planned Implementation
- Make skeleton drop Arrow of Teleportation (AoT) when killed on End Block: v0.3.1 - drop update
- Add particles to teleporter items: v0.3.2 - graphics update
- Change teleportation range of Teleporter Plate and AoT: v0.3.2
- Change item graphics: v0.3.2
- Patch Teleporter Plate layout in item frame: v0.3.2
- Increase AoT teleportation range based on Punch enchantment on bow: v0.4 - feature update
- Add Teleporter Wand enchantment on the enchanting table (Unbreaking): v0.4
- Add mechanism to teleport downwards (possibly by making new enchantment?): v0.4
- Rigorous testing and patching: v0.5 - testing update
- Future feature additions: >= v0.6, < v1 - feature update
- Final release: v1