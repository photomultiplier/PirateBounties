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

- Category `general`: generic settings, affecting the core mechanic of
  the plugin.
    - `bountyIncrease`, default 1.  How much the bounty will increase
      on a kill.
    - `emperorAmount`, default 4.  How many emperors there will be.
    - `emperorThreshold`, default 1.  The minimum bounty required to
      be an emperor.
- Category `events`: configures actions on events.  Contains various
  fields, one for each event.  Right now, the events are:
  - `leaderboardEnter`, when a player enters the leaderboard.
    Placeholders: `%player%`, the display name of the player;
    `%bounty%`, the player's bounty.
  - `leaderboardLeave`, when a player leaves the leaderboard.
    Placeholders: `%player%`, the display name of the player;
    `%bounty%`, the player's bounty.
  Each field can have the same sub-fields to configure what happens
  when the event is fired:
  - `enabled`: if the event is enabled, defaults to `false`.
  - `commands`: a list of strings.  Leave out this field if you don't
    want any commands to be executed.  Each item is run as a different
    command.  Don't include the leading `/`.  Placeholders are
    supported and will be replaced.
  - `playerMessage`: a list of strings.  Leave out this field if you
    don't want any message to be sent.  This message is sent only to
    the player.  Placeholders and formatting codes are supported.
  - `broadcastMessage`: a list of strings.  Leave out this field if
    you don't want any message to be sent.  This message is broadcast
    to everyone on the server.  Placeholders and formatting codes are
    supported.
- Category `files`: configures file names for saving persistent data.
    - `emperorsFile`, default `"EmperorsLeaderboardData.bin.gz"`.  The
      file used to save the emperors leaderboard between plugin
      reloads.
- Category `commandsErrorMessages`: error messages common to all the
  commands.  Each field is a list of strings: each item is printed on
  a different line.  Each field has some placeholders (for example
  `%player%`) which will then be replaced by the command.
    - `noPlayer`, default:
      ```yaml
      - "&cInvalid command: &l%player%&c is offline or doesn't exist!"
      ```
      Error message printed when the requested player doesn't exist.
      Placeholders: `%player%`, the name typed by the user.
    - `insufficientArguments`, default:
      ```yaml
      - "&cInvalid command: not enough arguments!"
      ```
      Error message printed when a command is called with an
      insufficient number of arguments.  Placeholders: `%given%`, the
      number of arguments given; `%needed%`, the minimum number of arguments.
    - `wrongSyntax`, default:
      ```yaml
      - "&cInvalid command: wrong syntax!"
      ```
      Error message printed when a command is called with an
      unexpected keyword.  No placeholders.
    - `wrongType`, default:
      ```yaml
      - "&cInvalid command: &l%value%&c is not a &l%type%&c!"
      ```
      Error message printed when an argument failed to parse.
      Placeholders: `%value%`, the value typed by the user; `%type%`,
      the needed type.
    - `noPermission`, default:
      ```yaml
      - "&cYou aren't permitted to run this command!"
      ```
      Error message printed when the user trying to run the command
      hasn't the necessary permission.  Placeholders: `%player%`, the
      player trying to run the command; `%permission%`, the permission
      required to run the command.
    - `bountyDisabled`, default:
      ```yaml
      - "&cPlayer &l%player%&c doesn't have a bounty!"
      ```
      Error message printed when trying to access the bounty of a
      player who has it disabled.  Placeholders: `%player%`, the player
      trying to run the command.
- Category `bountyCommand`: configures the `/bounty` command.
    - Category `messages`: configures the responses to the command.
      Each field is a list of strings: each item is printed on a
      different line.  Each field has some placeholders (for example
      `%player%`) which will then be replaced by the command.
        - `self`, default:
          ```yaml
          - "You have a bounty of &e&l$%bounty%"
          ```
          Response to `/bounty`.  Placeholders: `%player%`, the name
          of the player; `%bounty%`, his bounty.
        - `other`, default:
          ```yaml
          - "&e%player%&r has a bounty of &e&l$%bounty%"
          ```
          Response to `/bounty <playername>`.  Placeholders:
          `%player%`, the name of the other player; `%bounty%`, his
          bounty.
- Category `setBountyCommand`: configures the `/setbounty` command.
    - Category `messages`: configures the responses to the command.
      Each field is a list of strings: each item is printed on a
      different line.  Each field has some placeholders (for example
      `%player%`) which will then be replaced by the command.
        - `ok`, default:
          ```yaml
          - "Set &e%player%&r's bounty to &e&l$%bounty%"
          ```
          Response to successful invocation. Placeholders: `%player%`,
          the name of the player; `%bounty%`, his new bounty.
- Category `emperorsCommand`: configures the `/emperors` command.
    - Category `messages`: configures the responses to the command.
      Each field is a list of strings: each item is printed on a
      different line.  Each field has some placeholders (for example
      `%player%`) which will then be replaced by the command.
        - `listHeader`, default:
          ```yaml
          - "The emperors are:"
          ```
          The header of the emperors list.  No placeholders.
        - `listLine`, default:
          ```yaml
          - "%index%. &e%player%&r, &e&l$%bounty%"
          ```
          The items of the emperors list.  Placeholders: `%index%`,
          the number of the item; `%player%`, the name of the emperor;
          `%bounty%`, his bounty.
        - `noEmperors`, default:
          ```yaml
          - "&eThere are no emperors!"
          - "&rEarn at least &e&l$%threshold%&r to become one!"
          ```
          Response when there aren't any emperors.  Placeholders: `%threshold%`,
          the minimum bounty required to be an emperor.
- Category `setEmperorsCommand`: configures the `/setemperors`
  command.
    - Category `messages`: configures the responses to the command.
      Each field is a list of strings: each item is printed on a
      different line.  Each field has some placeholders (for example
      `%player%`) which will then be replaced by the command.
        - `ok`, default:
          ```yaml
          - "Leaderboard updated successfully"
          ```
          Message printed on command success.  No placeholders.

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
