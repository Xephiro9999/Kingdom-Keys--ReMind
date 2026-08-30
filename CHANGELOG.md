# KK Re:Mind Changelog

## 12.25.2

### Additions
- Added Creative Tabs for improved item sorting:
	- Keyblades
	- Equipables
	- Shotlocks
	- Spells
	- Misc Items
- Prevented Epic Fight Mod-exclusive abilities from being added if Epic Fight is not loaded.
- Reworked Gazing Omen. *(Commission request.)*
- Added new original Attack Commands:
	- Blizzard Surge
	- Water Surge
	- Aero Surge
	- Light Surge
	- Dark Surge
- Each Surge gains **10% damage per matching elemental boost equipped**.

### Fixes
- Fixed a crash that could occur when Epic Fight was not loaded.
- Fixed a crash that could occur when entering Rage Form through its Reaction Command for the first time.
- Fixed Epic Fight Guards and Counters not working properly.
- Restored localization for certain Reaction Commands.

---

## 12.25.2a

### Fixes
- Fixed `?????????` in the menu not showing when requirements are met.
- Fixed a crash when using the Surge Commands.
- Fixed Counter Rush visuals not playing/showing.
- Fixed Light Step and Dark Step.

---

## 12.25.2b

### Changes
- Sanguine Gaze now uses proper form animations while in forms.
- Added visuals when landing Attack Commands.
- Made Quick Blitz and Sliding Dash easier to land and added animations when using Epic Fight.

---

## 1.26.1

### Changes
- Adjusted abilities for Light Form and Dark Form.
- Fixed Step abilities being interrupted by taking damage.
- Fixed spells locking and becoming unable to cast.
- Fixed friendly fire from Surges, Sliding Dash, and Quick Blitz.
- Dark Mines now **SPIN IN PLACE! WOOOOOOOOOOOOOOOOOOOOO!**
- Dark Form's Easter Egg is no longer client-side only.
- Spark, Sparkra, and Sparkga now have sounds.
- Dark Firaga's Reaction Command now has a proper sound.
- Added a radius visual for Slow.
- Added a radius visual for Dispel.
- Reduced Dispel's radius because it was **WAAAAY too big**.
- Fixed Comet and Meteor.

### Journal
- The Journal page now contains information.
- Keyblades, Attack Commands, and Magic Commands are currently complete.

### New Form-Exclusive Abilities

#### Light Infusion — Light Form Exclusive
Imbues spells, Shotlocks, and other non-melee attacks with **Light Damage**.

> Does not affect attacks that already deal Light Damage.

#### Dark Infusion — Dark Form Exclusive
Imbues spells, Shotlocks, and other non-melee attacks with **Darkness Damage**.

> Does not affect attacks that already deal Darkness Damage.

### New Spells

#### Mine Square & Mine Shield
- Added Mine Square and Mine Shield from *Birth by Sleep*.
- They function similarly to their BBS versions, but deal **Fire Damage**.
- Their damage can be boosted with **Fire Boost**.
- Both gain **Seeker variants at Spell Level 4**.

#### Confuse
- Disorients enemies and prevents them from acting properly.
- Radius and duration increase with spell level.
- Duration:
	- Level 1: **5 seconds**
	- Level 2: **6 seconds**
	- Level 3: **7 seconds**

### Dream Eaters
- Chirithy is finally usable.
- Chirithy can be summoned through a keybind.
- Chirithy's stats scale and grow alongside the player.
- Chirithy supports the player in combat rather than fighting directly.
- Chirithy can cast:
	- Cure
	- Aero
	- Esuna

More Dream Eaters will be added later.

---

## 1.26.2

### New Keyblade
#### Voidlight
A Darkness-corrupted version of Starlight. **Re:Mind Original!**

### Changes
- Cleaned up the Journal Menu and added a scrollbar. *(Big thank you to Abelatox.)*
- Magic such as Aero and Cure can now target Dream Eaters. *(Thanks to Abelatox.)*
- Dream Eater HP now appears near the player's HP, MP, Drive, and other HUD elements. *(Thanks to Abelatox.)*
- Chirithy can now cast Auto-Life on the player every 5 minutes by default if the player does not already have the effect.
- Chirithy now prioritizes healing the player over itself.
- Having a Dream Eater summoned now counts toward **Friends Are Power** as a party member.
- Added configs to disable Dream Eaters.
- Updated Stop Block's description to avoid confusion.
- Increased Stop Block's afflicted duration from **2 seconds to 3 seconds**.
- Added descriptions for Attack Commands in the Journal Menu.

### Fixes
- Fixed Surges not dealing damage.
- Fixed Spark not dealing damage.

---

## 2.26.1

### Dream Eater Changes
- Chirithy now properly despawns when the player logs out.
- Fixed Chirithy not healing properly.
- Chirithy is now affected by equipped MP Haste, MP Hastera, and MP Hastega, allowing it to cast magic more often.

### Keyblades & Abilities
- Voidlight now has its recipe.
- HP Boost and MP Boost now work properly. *(Thanks to Willaby_Neko.)*
- Added **Ultima Weapon Ability**. *(Thanks to Willaby_Neko.)*
	- Boosts weapon Strength and Magic to their absolute limits.

### New Attack Commands
- Added Zantetsuken.
- Added Sephiroth's **Swift Strike** as a command-exclusive Attack Command for now.

### Wallet Changes
Added more coins to the Wallet Menu and adjusted the values of all coins except Heart:

| Coin | Value |
|---|---:|
| Copper | 1 Munny |
| Silver | 5 Munny |
| Gold | 10 Munny |
| Emerald | 50 Munny |
| Diamond | 100 Munny |
| Netherite | 500 Munny |
| Amethyst | 1,000 Munny |

### Planned / Added Ability
- Munny Magic — pay Munny to cast magic while in MP Recharge.

---

## 2.26.1b

### Changes
- Added Epic Fight Mod support.

---

## 3.26.1

### Fixes
- Chirithy can no longer dual-cast.
- Fixed Chirithy's Esuna crash. *(Hopefully.)*

---

## 3.26.2

### Fixes
- Summoning Dream Eaters no longer crashes the game due to a removed config.
	- **Update Kingdom Keys to the latest version.**
- Regen can now target party members and Dream Eaters.

### New Accessories

#### Brave Warrior — KH1
- AP: **1**
- STR: **2**
- Abilities: **2× HP Boost**

#### Omega Arts — KH1
- AP: **3**
- STR: **4**
- Abilities: **2× HP Boost**

#### Ray of Light
- AP: **3**
- MAG: **4**
- Abilities:
	- HP Boost
	- MP Boost

#### Alluring Skull
*For some Jolly Cooperation, I hope?*
- AP: **3**
- Abilities: **2× Encounter Plus**

#### White Fang — KH1
- AP: **1**
- STR: **1**
- Ability: **Critical Boost**

---

## 3.26.3

### Additions
- Began introducing **Formchanges and Command Styles**, starting with:
	- Firestorm
	- Diamond Dust
	- Thunder Bolt
- Added a base Finisher accessible outside of forms.
- Added **Situation Boost**.
- Esuna can now level into **Group Esuna**.
- Added **Poison Block**:
	- Poisons attackers after a successful Epic Fight guard.
	- Parrying boosts the effect.
- Added **Block Replenisher**:
	- Restores a small amount of MP after a successful guard.
	- Parrying boosts the effect.
- Added **Cure Converter**.

### Fixes
- Tags are now properly applied to various items, allowing Kingdom Keys advancements to trigger correctly. *(Thanks to Willaby_Neko.)*
- Coin values are now configurable. *(Thanks to Willaby_Neko.)*
- Chirithy now has a cooldown after being summoned, preventing free spells on summon.

### Adjustments
- Block abilities now restore slightly less.
- Adjusted Counter Blast's damage and range for balance.
- Breakthrough now has the Cure Converter ability.

---

## 3.26.3a

### Additions
- Added **Fever Pitch Style**:
	- Fast-paced style focused on speed and lightning-fast strikes.
	- Only accessible with Ventus' Keyblades.
- Added **Critical Impact Style**:
	- Slower, Strength-focused style built around brutal strikes.
	- Only accessible with Terra's Keyblades.
- Added **Spellweaver Style**:
	- Magic-focused style that emphasizes spellcasting over Keyblade attacks.
	- Only accessible with Aqua's Keyblades.
- Players now leave Styles after a period of no attacking or spellcasting.

### Fixes
- Epic Fight compatibility fixes.
- Brutal Blitzer's bonus damage now applies only to **melee hits**.
- Players can no longer manually revert from Styles.
- Organization members no longer have the cursed fifth Command Menu option: **Revert**.
- Fixed the `COMMAND` text color.
- Being afflicted with Stop now stops the Situation Gauge from draining.

---

## 3.26.3b

### Fixes
- Fixed a server packet issue that prevented loading into the game.

---

## 3.26.3c

### Fixes
- Fixed normal Drive Forms kicking the player out upon use. *(Thanks to Abelatox.)*

---

## 4.26.1

### Changes
- Revamped and future-proofed Styles/Formchanges. *(Thanks to Neko.)*
- New Game+ now grants permanent abilities. *(Thanks to Abel.)*
- Fixed Rage Form's Reaction Command. *(Thanks to Abel.)*
- General cleanup.

### New Item
#### Ability Orb
Grants a player a permanent copy of an ability.

Example:
```mcfunction
/give @s kkremind:ability_orb[kkremind:ability="kingdomkeys:ability_mp_haste"]
```

> **One-time use.**

---

## 5.26.1

### Compatibility
- Added compatibility with the latest Kingdom Keys version.

---

## 5.26.2

### Fixes
- Chirithy cloning is no longer a thing.
- Fixed a Chirithy-related crash.
- BBS Trio base Styles deal damage again.

---

## 5.26.2a

### Fixes
- Fixed a Drive Form crash.
- Hidden spells are now obtainable only through Creative Mode.

---

## 5.26.2b

### Config
- Added Panel configs allowing leveling to apply to the player or forms.

---

## 5.26.2c

### Fixes & Adjustments
- Added a potential fix for guarding against certain attacks while using Stop Block or Poison Block.
- Form Boost now extends how long players can idle in Stylechanges before being forced out.
- The BBS Trio's respective Styles are now Keyblade-locked.
- Adjusted Counter Ability damage.
- Increased Ravenous Saber's hitbox size.

---

## 5.26.3

### Changes
- Adjusted Wrongful Inheritor's abilities.
- Changed EXP Walker's formula to respect Kingdom Keys' `xpMultiplier` server config.
- Changed Heart Walker's formula to respect Kingdom Keys' `heartMultiplier` server config.
- Adjusted Wrongful Inheritor's abilities again.
- Chirithy can cast Cura, Curaga, Aerora, and Aeroga again.
- Thunder Surge now deals **Lightning Damage** instead of Air Damage.
- Finishers for Styles and non-Styles no longer time out after roughly 3 seconds.

### New Keyblade
## Fortuna
*Requested by Nameless Dreamer and inspired by Nero in DMC4.*

- Tier: **SSS — Smokin' Sexy Style**
- Base Stats:
	- STR: **7**
	- MAG: **7**
- Ability: **Exceed**

#### Exclusive Reaction Command — Exceed
- Cost: **10 Focus**
- Can stack up to **3 times**.
- Boosts the next attack with explosive fury.
- Timing Exceed with attacks grants increased damage.
- Damage scales with:
	- Exceed stacks
	- Fire Boosts
- Perfect timing grants **MAX stacks** and refunds some Focus.

---

## 5.26.4

### Changes & Fixes
- Adjusted Friends Are Power.
- Fixed Group Esuna's data.
- Migrated some spells and commands to the Melding System.
- Attack Commands no longer double-hit in Epic Fight.
- Added Brightcrest to Spellweaver's Keyblade pool.

### New Attack Commands
Added **11 new Attack Commands**:

- Fire Strike
- Blizzard Strike *(Re:Mind Original)*
- Thunder Strike *(Re:Mind Original)*
- Water Strike *(Re:Mind Original)*
- Aero Strike *(Re:Mind Original)*
- Light Strike *(Re:Mind Original)*
- Dark Strike *(Re:Mind Original)*
- Binding Strike
- Confusion Strike
- Blitz
- Slot Edge

---

## 5.26.4a

### Adjustments
- Adjusted Daredevil's synthesis recipe/cost.
- Adjusted Blitz and Slot Edge's jump power.

### New Meld Recipes
Added Meld Recipes for:

- Confusion Strike
- Dispel
- Auto-Life
- Drain Tree
- Osmose Tree
- Esuna + Group Esuna
- Holy Tree
- Ruin Tree

### Shops
- Added various commands to Moogle Shops.

---

## 6.26.1

### Organization Panel System
The Organization Panel System has been expanded into a full progression and customization feature.

Players can now collect, purchase, equip, and arrange Panels on a grid to customize stats, abilities, and growth options. Panels can grant stat bonuses, unlock movement abilities, boost spell effects, and interact with nearby Panels through special link areas.

### New Panel Features
- Panel Shop for purchasing new Panels.
- Panel Inventory for managing owned Panels.
- Slot Releasers for expanding available grid space.
- **Unequip All** button for quickly clearing equipped Panels.
- Custom Panel icons for improved readability.
- Link-area Panels that enhance compatible Panels placed within linked slots.

### Panel Types
- **Stat Panels**
	- Strength
	- Magic
	- Defense
	- AP
	- Level Up
- **Growth Panels**
	- High Jump
	- Dodge Roll
	- Aerial Dodge
	- Quick Run
	- Glide
- **Boost Panels**
	- Fire Boost
	- Blizzard Boost
	- Thunder Boost
	- Water Boost
	- Light Boost
	- Dark Boost
	- Attack Haste
	- Draw
	- Jackpot
	- Lucky Lucky
	- Combo Plus
- **Special Panels**
	- Hearts Are Power
	- Ultima Weapon
- **Link Panels**
	- Power Link
	- Magic Link
	- Guard Link
	- Level Link
- **LV Doubler Panels**
	- Special-shaped Panels that increase Level bonuses when Level Up Panels are placed inside their link areas.

### Link Panel System
Power Link, Magic Link, Guard Link, and LV Doubler now use shaped link areas.

- **Power Link** enhances linked Strength Panels.
- **Magic Link** enhances linked Magic Panels.
- **Guard Link** enhances linked Defense Panels.
- **LV Doubler** boosts Level Up Panels placed within its link area.

### New Meld Recipes
- Slot Edge + Slot Edge → **Steal**
- Curaga + Aerora → **Haste**
- Haste + Haste → **Hastera**
- Hastera + Hastera → **Hastega**
- Ruin + Firaga → **Comet**
- Comet + Comet → **Meteor**
- Berserk + Berserk → **Berserkra**
- Berserkra + Berserkra → **Berserkga**
- Slow + Slow → **Slowra**
- Slowra + Slowra → **Slowga**
- Silence + Silence → **Silencera**
- Silencera + Silencera → **Silencega**

### Fixes
- Updated compatibility for the latest Kingdom Keys release.

### Miscellaneous
- Moved several spells to the **Special Moogle Shop**.

---

## 6.26.1b

### Fixes
- Fixed Organization fake Growth abilities activating without being in the Organization, owning the Panels, or having the boosts active.
- Fixed Organization fake High Jump being too high.
	- It now scales with each High Jump Panel equipped.
- Fixed the Organization leave option taking Hearts when the player did not have enough Hearts.
- Added feedback to some buttons in the Panel System.
- Organization Glide should now feel better with only one Panel equipped.

---

## 6.26.2

### New Dream Eaters

#### Meow Wow
**Role:** Melee Offensive Support

Can cast:
- Cure
- Balloon
- Slow

#### Komory Bat
**Role:** Ranged Offensive Debuffer

Can cast:
- Haste
- Drain
- Zero Gravity

### New System — Spirit Links
- Each Dream Eater will have bonuses based as closely as possible on its *Dream Drop Distance* progression.
- Bonuses are granted as Dream Eaters level up.
- A Dream Eater must be summoned to gain EXP.
- Each Dream Eater has its own EXP multipliers and growth.
- This system will be made more faithful to DDD later.

### New Items
#### Dream Eater Charms
Two charms are currently available:
- Meow Wow Charm
- Komory Bat Charm

More will be added as additional Dream Eaters are introduced.

### New Server Config
#### Dream Eater XP Multiplier
- Default: **1.0×**
- Setting this to **0** disables Dream Eater EXP gain.

### New Ability
#### Confusion Block — Epic Fight
- Inflicts Confusion on attackers after a successful guard.
- Parrying increases the effect.

### Changes
- Reworked Chirithy to better reflect its intended **Pure Support** role.
- Renewal Block is no longer granted at Level 1 on login.
	- Chirithy now grants it while active at **Level 20**.
- Poison Block is no longer granted at Level 1 on login.
	- Meow Wow now grants it while active at **Level 35**.
- Slightly buffed Haste and removed its FOV effect.
- Added the following Keyblades to Roxas' Keyblade pool:
	- Fortuna
	- Blitzer's Dream
	- Legend's Fang
	- Fierce Deity Key
- Removed spells from the Synth tag.
- Refactored the Style Change System for the current Kingdom Keys version. *(Thanks to Willaby_Neko.)*

### Fixes
- Chirithy is no longer immortal.
- Dream Eaters can now be petted.

---

## 7.26.1

### New Enemies

#### Cactuar
- Cactuar can now spawn naturally in desert biomes.
- Fast, tricky, and dangerous if ignored.
- Watch out for its needle attacks.
- **Water magic is especially effective.**

#### Jumbo Cactuar — Boss
- Added Jumbo Cactuar as a triggered boss encounter.
- Much tougher than a normal Cactuar.
- **Water magic is highly recommended.**

### New Recipes
- Added synthesis recipes for the Meow Wow Charm.
- Added synthesis recipes for the Komory Bat Charm.

### Fixes
- Fixed an Ability Links duplication exploit.
- Dream Eaters now properly despawn when their owner logs out.
- Improved Dream Eater cleanup handling to prevent lingering summoned Spirits.

### Important
> This update includes fixes for known exploits. Server owners are strongly encouraged to update as soon as possible.

---

## 7.26.1a

### Additions
- Jumbo Cactuar now has battle music.

### Changes
- Adjusted Cactuar's base stats to better reflect *Final Fantasy VIII*.
- Adjusted Cactuar's spawn rate so it should now spawn during the day.
- Adjusted Jumbo Cactuar's base stats to better reflect *Final Fantasy VIII*.
- Dream Eaters now grant their owners Kingdom Keys EXP on kill.
- Meow Wow can now be gifted:
	- Cod: **15 EXP**
	- Salmon: **15 EXP**
- Komory Bat can now be gifted:
	- Spider Eye: **15 EXP**
	- Phantom Membrane: **45 EXP**
- Removed the Ability Link list from the main Dream Eater Menu because it made the Ability Link Menu redundant.

### Fixes
- Fixed a Dark Divide crash.
- Ability Link Menu now displays Dream Eater level instead of Player Level.

---

## 7.26.2

### Additions
- Added sounds for Cactuar and Jumbo Cactuar.

### New Enemies

#### Tonberry
A mysterious being found in dungeons. Slowly approaches its enemy before attacking with the Chef's Knife.

#### Tonberry King — Boss
The king of the Tonberries. Appears to seek revenge for defeated Tonberries.

### Changes
- Increased cooldowns on some Cactuar and Jumbo Cactuar attacks.

### Fixes
- Fixed certain Meld Recipes not showing or working.

---

## 7.26.2a

### Dream Eater Changes
- Dream Eater-granted abilities no longer appear in the Ability List.
- Dream Eater-granted abilities no longer consume AP.

---

## 7.26.2b

### Compatibility
- Updated support for the latest Kingdom Keys release.

### Fixes
- Fixed Confusion Strike's Meld Recipe.

---

## 7.26.2c

### Fixes
- Fixed certain Meld Recipes.

### Shops
- Added the Elemental Strike Attack Commands to the Special Moogle Shop.

---

## 8.26.2

### Enemy Changes
- Reduced the chance of Cactuar fleeing.
- Reduced knockback from Cactuar's Needles.

### Dream Eater Changes
- Adjusted Chirithy's Cure to be more consistent.
- Separated Chirithy's self-healing and owner-healing behavior for better consistency.
- Adjusted Chirithy's cooldowns.
- If the player is killed while a Dream Eater is summoned, the player now enters **KO State**.
	- If the Dream Eater can heal the player, it can revive them.

### General Changes
- Removed recipes that are no longer needed.
- Re-localized Faith *(Re:Mind's version)* to **Faith (Re:CoM)** to avoid confusion.
- Removed Faith (Re:CoM)'s synthesis recipe.
- Added a Meld Recipe for Faith (Re:CoM):
	- Holyga + Holyga

---

## 8.26.2a

### New Items

#### Chef's Knife
- Drops from Tonberries: **75% chance**
- Drops from Tonberry King: **100% chance**

#### Cactuar Needle
- Drops from Cactuars and Jumbo Cactuars.

### Enemy Changes
- Tonberry and Tonberry King now take slightly more damage.

### Dream Eater Adjustments
- Fixed Chirithy's Aero and Esuna.
- Chirithy can now be gifted:
	- Amethyst Shard: **12 EXP**
	- Ghast Tear: **25 EXP**
- Tonberry can now be gifted Chef's Knives for a large amount of EXP.
- Cactuar can now be gifted Cactuar Needles for a large amount of EXP.

### Ability Links Reworked
- Abilities are now granted **permanently** as a Dream Eater levels up.
- Fixed an issue where the previous system could remove certain abilities gained through level-ups.

### Epic Fight Fixes
- All Keyblades now work with Kingdom Keys' Styles/Movesets.

---

## 8.26.3

### Ability Adjustments
- Spellblade (Water) now deals **Water Damage** instead of Ice Damage.
- Reduced Brutal Blitzer's damage.
- HP Boost now grants **+12.5% HP per stack**.
- MP Boost now grants:
	- **+5 MP minimum per stack**
	- **+12.5% MP per stack** instead when that value exceeds 5

### Epic Fight Adjustments
- Renewal Block now restores slightly less Health and Hunger/Saturation.

### Fixes
- HP Boost now persists after death.

---

## 8.26.3a

### Compatibility
- Added compatibility with Kingdom Keys **2.9.2a / 2.9.2b**.

---

## 8.26.3b

### Fixes
- Dark Passage no longer removes Attack Commands.
- Meow Wow now properly despawns when logging out.
- Fixed Attack Haste conflicting with other attack-speed effects.
	- Attack Haste now stacks correctly with other bonuses.

---

## 8.26.3c

### Epic Fight
- Blocking now plays the **Guard sound from the games**.

### Dream Eaters
- Improved the Dream Eater Level Up visual.

### Magic
- Reworked **Ultima**.
- Added **Zettaflare**.
- Adjusted Jumbo Cactuar spawn chance.
- Adjusted Tonberry King spawn chance.
- 
