#!/usr/bin/python

"""
Partly taken from the Portal ARG
https://web.archive.org/web/20110316085023/http://portalwiki.net/index.php/BBS_Resources_List
"""

import random

_prefixes = [ 'CAUTION', 'ERROR', 'WARNING' ]
_messages = [
    'ambidextrous pointer detected inside data',
    'anomalous emotional response detected',
    'bios insufficiently basic',
    'bios insufficiently advanced',
    'could not load error message',
    'data-line encoding not 100% dos-compatible',
    'error not found',
    'error not undetected',
    'external data-line stability compromised, add more beans to network code',
    'no error detected',
    'not in the mood',
    'out of memory above 640kb',
    'Keyboard not present, press any key',
    'segment segmentation segmented at segmentation pointer',
    'this space intentionally left blank',
    'unexpected disconnection expected at <./sh/update_plugins.py>, got <expected disconnection> instead',
    'receipient server is afraid of message, robot instills fear.',
    'wrong moon phase for delivery',
    'i am on fire, help!',
    'alan please insert message',
    'generic error message',
    'delivery path not greasy enough manual intervention required',
    'you have not enough vespene gas',
    'we require more minerals',
    'you have not enough mana',
    'mechanical turk wants to be paid before filtering more <blockid>',
    'no error',
    'help i actually dog throw bone please',
    'an error might have occurred before i noticed any problems',
    'this can not happen',
    'you rang?',
    'unexpected <blockid> expecting <blockid>',
    'nuclear reactor going critical... we are all going to die down here',
    'server expecting pancake, bunny added for receipt.',
    "nominal",
    "normal",
    "a little to the left",
    "a little to the right",
    "zzzzzz",
    "calculating splines",
    "what was that?",
    "Ignore the fire...",
    "PC LOAD LETTER",
    "Keyboard not detected! Press F1 to continue",
    "Error: Success",
    "Error: The operation completed successfully",
    "HTTPError 418: I'm a teapot",
    "oops you should never see this error",
    "Momentaraly writing while reading",
    "Something Rotten in Denmark, Interp Stack Not ALigned",
    'Unexpected <;>,  expecting <;>',
    "malloc error: WE NEED MORE RAM FOR THAT CAPITAN!",
    "I am lost here... need a map.",
    "eh?",
    "Error: command <`whoami`> returned not <$username> but <philosophy> instead",
    "Warning: Insufficient funds for world domination, dotate at http://www.admalledd.com/mc/",
    "Error opening log file, please see log file for details"
    ]

def generate():
    dynmsgs = [
        #'DETECTING POSSIBLE MFM CORRUPTION ON ATA BUS %d' % random.randint(1,8),
        'PLEASE PLACE DISK #%d IN DRIVE %s:' % (random.randint(1,100), random.choice(['A', 'B'])),
        ]

    allmsgs = _messages + dynmsgs

    #return '%s: %s' % (random.choice(_prefixes), random.choice(allmsgs))
    return random.choice(allmsgs)

def main():
    print generate()

if __name__ == "__main__":
    main()
