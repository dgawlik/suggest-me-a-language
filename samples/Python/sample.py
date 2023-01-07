#
#  == Suggest me a language, 2023 ==
#

class Coin(object):

    def __init__(self, nominal, isHeads):
        self.nominal = nominal
        self.isHeads = isHeads

    def __str__(self):
        return '{}¢:{}'.format(self.nominal, 'H' if self.isHeads else 'T')

class Cent20(Coin):

    def __init__(self, isHeads):
        Coin.__init__(self, 20, isHeads)

class Cent50(Coin):

    def __init__(self, isHeads):
        Coin.__init__(self, 50, isHeads)



POOL = [Cent20(True), Cent20(False), Cent50(True), Cent50(False)]

import random

def flip():
    while True:
        yield random.choice(POOL)


tosses = [str(flip().__next__()) for i in range(10)]

print(tosses)


# Running => python3 sample.py