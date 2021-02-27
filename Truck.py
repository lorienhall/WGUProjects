
from Objects import *
import datetime


class Truck:
    def __init__(self, name):
        self._name = name
        self._package_list = []
        self._number_of_packages = 0
        self._mileage_traveled = 0
        self._time = datetime.time(8, 0, 0)

    def __repr__(self):
        return '{}, {}'.format(self._number_of_packages, self._mileage_traveled)

    def loadPackage(self, p):
        unloaded_package_list.remove(p)
        self._package_list.append(p)
        n = 0
        for i in self._package_list:
            n = n + 1
        self._number_of_packages = n

    def unloadPackage(self, pack):
        pack.delivery_status(self._time, 2)
        n = 0
        for i in self._package_list:
            n = n + 1
        self._number_of_packages = n

    def removeFromPackageList(self, p, boo=False):
        self._package_list.remove(p)
        if boo:
            unloaded_package_list.append(p)

    def appendToPackageList(self, p):
        self._package_list.append(p)

    def getPackages(self):
        return self._package_list

    def clearPackages(self):
        self._package_list.clear()
        self._number_of_packages = 0

    # setters and getters
    def name(self):
        return self._name

    def number_packages(self, n=None):
        if n: self._number_of_packages = n
        return self._number_of_packages

    def mileage(self, m=None):
        if m: self._mileage_traveled = m
        return self._mileage_traveled

    def time(self, t=None):
        if t: self._time = t
        return self._time

    def get_second(self):
        return self._time.second

    def get_minute(self):
        return self._time.minute

    def get_hour(self):
        return self._time.hour

