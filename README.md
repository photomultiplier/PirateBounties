# PirateBounties

A Spigot plugin adding bounties to minecraft.

## How to install

Download the `.jar` file from the "Releases" section and copy it in
the `plugins` directory of your Spigot installation.

## How it works

The plugin introduces a bounty mechanic.  Each player starts with a
bounty of $0.  When player A kills player B, A's bounty will increase
of a fixed, configurable amount and B's bounty will halve.

The players with the highest bounties above a certain threshold become
emperors.  There's also a maximum number of emperors.

## How to configure

After running the plugin for the first time, Spigot will create a
directory called `PirateBounties` in the `plugins` directory.  Inside
you will find a file named `config.yml` containing the default
configuration.  There are three parameters which can be configured:

- `bountyIncreaseAmount`, default 1.  How much the bounty will increase
  on a kill.
- `emperorAmount`, default 4.  How many emperors there will be.
- `emperorThreshold`, default 1.  The minimum bounty required to be an emperor.

## How to use

The plugin provides three commands:

- `/bounty` shows your bounty.
- `/bounty <playername>` shows another player's bounty.
- `/emperors` shows the list of emperors.

## How to compile

To compile this plugin from source, clone the repository and run the
gradle `jar` task in its root directory.  The `.jar` file will then be
located in the `build/libs` directory.

The javadoc documentation can be generated by running the gradle
`javadoc` task.
