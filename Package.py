import datetime
import PackageStatus


class Package:

    def __init__(self, id, address, city, state, zip, delivery_deadline, mass, special_notes):
        self._id = id
        self._address = address
        self._city = city
        self._state = state
        self._zip = zip
        self._delivery_deadline = delivery_deadline
        self._mass = mass
        self._special_notes = special_notes
        self._delivery_status = PackageStatus.PackageStatus(datetime.time(8, 0, 0))
        self._delivery = 1

    def __repr__(self):
        return '{}, {}, {}, {}, {}, {}, {}, {}, {}'\
            .format(self._id, self._address, self._city, self._state, self._zip, self._delivery_deadline,
                    self._mass, self._special_notes, self._delivery_status)

    # setters and getters
    def id(self, i=None):
        if i: self._id = i
        return self._id

    def address(self, a=None):
        if a: self._address = a
        return '{}'.format(self._address)

    def city(self, c=None):
        if c: self._city = c
        return '{}'.format(self._city)

    def state(self, s=None):
        if s: self._state = s
        return '{}'.format(self._state)

    def zip(self, z=None):
        if z: self._zip = z
        return '{}'.format(self._zip)

    def delivery_deadline(self, d=None):
        if d: self._delivery_deadline = d
        return '{}'.format(self._delivery_deadline)

    def mass(self, m=None):
        if m: self._mass = m
        return '{}'.format(self._mass)

    def special_notes(self, s=None):
        if s: self._special_notes = s
        return '{}'.format(self._special_notes)

    def delivery_status(self, dt=None, ds=None):
        if ds and dt: self._delivery_status.changeStatus(ds, dt)
        return '{}'.format(self._delivery_status)

    def delivery(self, d=None):
        if d: self._delivery = d
        return '{}'.format(self._delivery)
