class PackageStatus:

    def __init__(self, time=None, status=0):
        _status_types = ('at the hub', 'en route', 'delivered')
        self._status = _status_types[status]
        self._time = time

    def __repr__(self):
        return '{}, {}'.format(self._status, self._time)

    # This method changes the status/time of a package
    def changeStatus(self, num, time=None):
        _status_types = ('at the hub', 'en route', 'delivered')
        self._status = _status_types[num]
        self._time = time
