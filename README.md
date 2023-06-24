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
    - `wrongType`, default:
      ```yaml
      - "&cInvalid command: &l%value%&c is not a &l%type%&c!"
      ```
      Error message printed when an argument failed to parse.
      Placeholders: `%value%`, the value typed by the user; `%type%`,
      the needed type.
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

## How to use

The plugin provides the following commands:

- `/bounty` shows your bounty.
- `/bounty <playername>` shows another player's bounty.
- `/setbounty <playername> <bounty>` sets the player's bounty to the
  specified amount.
- `/emperors` shows the list of emperors.

## How to compile

To compile this plugin from source, clone the repository and run the
gradle `jar` task in its root directory.  The `.jar` file will then be
located in the `build/libs` directory.

The javadoc documentation can be generated by running the gradle
`javadoc` task.
