# Shroom

### The 1.0 Vision

Emulator frontend for any emulator that lets us Intent-invoke them, that gives you a browsable, metadata-enhanced list of games from a cloud storage service, which syncs savegames back. We have sections for recently played games, recently added games, and favorites.

Using the OpenVGDB database, we can turn crappy ROM filenames into beautiful cover art with descriptions, titles, the whole works. Instead of being a crap experience of browsing `/sdcard`, we'll make something nice to use, and sync with the :cloud: too.

### Iterations

1. Get a list of games from a Dropbox folder, you pick a ROM, we sync it down, invoke the game, then we sync the savegame back ASAP.
1. Split games by console type, add support for every console that we know how to support (i.e. ones that Robert Broglia emulators support).
1. Add metadata support and image fetching, make it :sparkle:
1. Add recents, recently added, favorites
1. Ouya / Android TV / Fire TV support?
