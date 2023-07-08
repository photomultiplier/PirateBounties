# PirateBounties

A Spigot plugin adding bounties to minecraft.

## How to install

Download the `.jar` file from the "Releases" section and copy it in
the `plugins` directory of your Spigot installation.

## How it works

The plugin introduces a bounty mechanic.  Each player starts with a
bounty of 0.  When player A kills player B, A's bounty will increase
of a fixed, configurable amount and B's bounty will halve.

The players with the highest bounties above a certain threshold become
emperors.  There's also a maximum number of emperors.

## How to configure

After running the plugin for the first time, Spigot will create a
directory called `PirateBounties` in the `plugins` directory.  Inside
you will find a file named `config.yml` containing the default
configuration.  Here are the parameters that can be configured:

### Category `general`

Generic settings, affecting the core mechanic of the plugin.

- `bountyIncrease`, default `1`.  How much the bounty will increase on
  a kill.
- `emperorAmount`, default `4`.  How many emperors there will be.
- `emperorThreshold`, default `1`.  The minimum bounty required to be
    an emperor.
- `emperorTotal`, default `10`.  The total number of emperors to keep
  track of.  Only the first `emperorAmount` are actual emperors: the
  others (in the default case, other 6) are only used to fill in the
  spots left by emperors that lose enough of its bounty to fall of the
  leaderboard.  It must be greater (or equal) to `emperorAmount`.
- `recalculateLeaderboardIfVacant`, default `false`.  If enough
  emperors leave the leaderboard (in the default case, more than 6),
  some of the last spots will remain vacant.  If this happens, this
  settings tells the plugin if it should scan all the players for new
  emperors - a quite resource intensive task if your server has many
  players.  Also keep in mind that any player can update his position
  on the leaderboard by simply reconnecting to the server.

### Category `events`

Configures actions on events.  Contains various fields, one for each
event.  Right now, the available events are:

- `leaderboardEnter`, when a player enters the leaderboard.
- `leaderboardLeave`, when a player leaves the leaderboard.

Each event field can have the same sub-fields to configure what
happens when the event is fired:

- `enabled`, default `false`.  If the event is enabled.
- `commands`: a list of strings.  Leave out this field if you don't
  want any commands to be executed.  Each item is run as a separate
  command.  Don't include the leading `/`.  Placeholders are supported
  and will be replaced.  **Note**: if the the event is fired while the
  player is offline, only some placeholders will work!  As an example,
  `%player_name%` and `%piratebounteis_emperors_thr%` work even if the
  player is offline; but `%player_displayname%` and
  `%piratebounties_bounty%` don't.  You can check if a placeholder
  works on an offline player by running `/papi parse <playername>
  <placeholder(s)>` with an offline player.  If the placeholders gets
  replaced, it works with offline players.  All of this holds true for
  `broadcastMessage`; while `playerMessage` is just ignored if the
  player is offline.
- `playerMessage`: a list of strings.  Leave out this field if you
  don't want any message to be sent.  This message is sent only to the
  player.  Placeholders and formatting codes (e.g. `&e` for yellow and
  `&l` for bold) are supported.
- `broadcastMessage`: a list of strings.  Leave out this field if you
  don't want any message to be sent.  This message is broadcast to
  everyone on the server.  Placeholders and formatting codes
  (e.g. `&e` for yellow and `&l` for bold) are supported.  Note that
  placeholders are relative to the player triggering the event, not to
  the one receiving the broadcast message!

Here is an example configuration for the `leaderboardEnter` event:

``` yaml
events:
  leaderboardEnter:
    enabled: true
    commands:
      - "give %player_name% minecraft:diamond 64"
    playerMessage:
      - "&lCongrats on becoming an emperor!"
      - "&e&l *** %player_name%, with $%piratebounties_bounty% ***"
    broadcastMessage:
      - "&l%player_name% just became an emperor!"
```

### Category `files`

Configures file names for saving persistent data.

- `emperorsFile`, default `"EmperorsLeaderboardData.bin.gz"`.  The
  file used to save the emperors leaderboard between plugin reloads.

### Category `commandsErrorMessages`

Error messages common to all the commands.  Each field is a list of
strings: each item is printed on a different line.

- `noPlayer`, default:
  ```yaml
  - "&cInvalid command: &l%player%&c is offline or doesn't exist!"
  ```
  Error message printed when the requested player doesn't exist.
  Placeholders are relative to the player invoking the command.
  The special placeholder `%player%` stores the name of the
  inexistent player.
- `insufficientArguments`, default:
  ```yaml
  - "&cInvalid command: not enough arguments!"
  ```
  Error message printed when a command is called with an
  insufficient number of arguments.  Placeholders are relative to
  the player invoking the command.  The special placeholders
  `%given%` and `%needed%` store the given and needed number of
  arguments, respectively.
- `wrongSyntax`, default:
  ```yaml
  - "&cInvalid command: wrong syntax!"
  ```
  Error message printed when a command is called with an
  unexpected keyword.  Placeholders are relative to the player
  invoking the command.
- `wrongType`, default:
  ```yaml
  - "&cInvalid command: &l%value%&c is not a &l%type%&c!"
  ```
  Error message printed when an argument failed to parse.
  Placeholders are relative to the player invoking the command.
  The special placeholders `%value%` and `%type%` store the given
  value and the expected type, respectively.
- `noPermission`, default:
  ```yaml
  - "&cYou aren't permitted to run this command!"
  ```
  Error message printed when the user trying to run the command
  hasn't the necessary permission.  Placeholders are relative to
  the player invoking the command.  The special placeholder
  `%permission%` stores the missing permission.
- `bountyDisabled`, default:
  ```yaml
  - "&cPlayer &l%player_name%&c doesn't have a bounty!"
  ```
  Error message printed when trying to access the bounty of a
  player who has it disabled.  Placeholders are relative to the
  player with the bounty disabled.

### Category `bountyCommand`

Configures the `/bounty` command.

-  `messages`: configures the responses to the command.
  Each field is a list of strings: each item is printed on a
  different line.
    - `self`, default:
      ```yaml
      - "You have a bounty of &e&l$%piratebounties_bounty%"
      ```
      Response to `/bounty`.  Placeholders are relative to the
      player invoking the command.
    - `other`, default:
      ```yaml
      - "&e%player_name%&r has a bounty of &e&l$%piratebounties_bounty%"
      ```
      Response to `/bounty <playername>`.  Placeholders are
      relative to `<playername>`.

### Category `setBountyCommand`

Configures the `/setbounty` command.

-  `messages`: configures the responses to the command.
  Each field is a list of strings: each item is printed on a
  different line.
    - `ok`, default:
      ```yaml
      - "Set &e%player_name%&r's bounty to &e&l$%piratebounties_bounty%"
      ```
      Response to successful invocation. Placeholders are relative
      to the player whose bounty was set.

### Category `emperorsCommand`

Configures the `/emperors` command.

-  `messages`: configures the responses to the command.
  Each field is a list of strings: each item is printed on a
  different line.
    - `listHeader`, default:
      ```yaml
      - "The emperors are:"
      ```
      The header of the emperors list.  Placeholders are relative
      to the player invoking the command.
    - `listLine`, default:
      ```yaml
      - "%index%. &e%player_name%&r, &e&l$%piratebounties_bounty%"
      ```
      The items of the emperors list, when the emperor is online.
      Placeholders are relative to the emperor.  The special
      placeholder `%index%` stores the index of the current item.
    - `offlineListLine`, default:
      ```yaml
      - "%index%. &e%player&r, &e&l$%bounty%"
      ```
      The items of the emperors list, when the emperor is offline.
      Placeholders are relative to the player invoking the command.
      The special placeholders `%index%`, `%player%` and `%bounty%`
      store the index of the current item, the display name of the
      emperor and his bounty, respectively.
    - `noEmperors`, default:
      ```yaml
      - "&eThere are no emperors!"
      - "&rEarn at least &e&l$%piratebounties_emperors_thr%&r to become one!"
      ```
      Response when there aren't any emperors.  Placeholders are
      relative to the player invoking the command.

### Category `setEmperorsCommand`

Configures the `/setemperors` command.

-  `messages`: configures the responses to the command.
  Each field is a list of strings: each item is printed on a
  different line.
    - `ok`, default:
      ```yaml
      - "Leaderboard updated successfully"
      ```
      Message printed on command success.  Placeholders are
      relative to the player invoking the command.

## Permissions

The plugin uses the following permissions:

- `pirateobounties.bounties.enabled` enables bounty on a player.  If
  this is disabled, the player's bounty can't be read or set.
- `pirateobounties.bounties.get` enables use of the `/bounty` command.
- `pirateobounties.bounties.set` enables use of the `/setbounty`
  command.
- `pirateobounties.emperors.get` enables use of the `/emperors` command.
- `pirateobounties.emperors.set` enables use of the `/setemperors` command.

## How to use

The plugin provides the following commands:

- `/bounty` shows your bounty.
- `/bounty <playername>` shows another player's bounty.
- `/setbounty <playername> <bounty>` sets the player's bounty to the
  specified amount.
- `/emperors` shows the list of emperors.
- `/setemperors update` updates the whole emperors leaderboard.
- `/setemperors update <playername>` updates a single player on the
  leaderboard.
- `/setemperors remove <playername>` removes a player from the
  leaderboard.
- `/setemperors clear` empties the leaderboard.

## How to compile

To compile this plugin from source, clone the repository and run the
gradle `jar` task in its root directory.  The `.jar` file will then be
located in the `build/libs` directory.

The javadoc documentation can be generated by running the gradle
`javadoc` task.
