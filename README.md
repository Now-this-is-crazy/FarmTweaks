[FarmTweaks](https://modrinth.com/mod/farmtweaks)
===========

Farm Tweaks is a fabric mod that enhances the Vanilla Minecraft farming experience by adding QOL features & improvements, that are fully server-side.

**Requires [MidnightLib](https://modrinth.com/mod/midnightlib)**

## Features
- *Leaves now decay faster
- *Interacting with crops with certain tools will cause them to break aswell as replant
- *Crops have a chance of dropping experience orbs
- *Crops can now be planted using a dispenser
- *Seeds can now be used to replenish grass, with each dirt block giving a separate grass type
- *Farmland can no longer get trampled by entities
- *You can now bonemeal more blocks to either grow or duplicate them, examples being sugarcane & small flowers
- *Path blocks are now easier to create when there are blocks above it
- Mycelium and Podzol are now tillable
- Oak Saplings now grow into Swamp Trees in the Swamp Biome

*Modifiable in the Config / via Datapacks

## Datapacks

### Alt Harvest
File Path: ``data/<mod-id>/farmtweaks/alt_harvest/<item-id>.json``

Data Options:
- `allowed` - Whether the tool can use the alternate harvest
- `range` - The harvest range
- `damage` - Durability damage received after use

[`copper_hoe.json`](src/main/resources/data/minecraft/farmtweaks/alt_harvest/copper_hoe.json):
```
{
  "allowed": true,
  "range": 1.0,
  "damage": 1,
}
```

### Harvestable
File Path: ``data/<mod-id>/farmtweaks/harvestable/<block-id>.json``

Data Options:
- `allowed` - Whether the tool can use the alternate harvest
- `required` - The required block properties
- `harvested` - The block properties to set / Blockstate to replace the block with

[`wheat.json`](src/main/resources/data/minecraft/farmtweaks/harvestable/wheat.json):
```
{
  "allowed": true,
  "required": [
    {
      "name": "age",
      "value": "7"
    }
  ],
  "harvested": [
    {
      "name": "age",
      "value": "0"
    }
  ]
}
```

for Blockstates:
```
{
[...]

  "harvested": {
    "Name": "minecraft:wheat,
    "Properties": [
        "age": "0"
    ]
  }

[...]
}
```

### Replenishable
File Path: ``data/<mod-id>/farmtweaks/harvestable/<block-id>.json``

Data Options:
- `allowed` - Whether the block can be replenished
- `replenished` - The replenished Block / Blockstate
- `items` - List of Items or Item Tag

[`dirt.json`](src/main/resources/data/minecraft/farmtweaks/replenishable/dirt.json):
```
{
  "allowed": true,
  "replenished": "minecraft:grass_block",
  "items": "#farmtweaks:grass_seeds"
}
```

## SUGGESTIONS / ISSUES
Have something to suggest or found a bug? Either [join our Discord](https://discord.gg/Dmp2dbyNrs) or [make an issue in our GitHub](https://github.com/Now-this-is-crazy/FarmTweaks/issues).