
from ChainingHashTable import myHash
from Package import *
from Objects import *
import Truck
import csv
import datetime
# Note that if a function has an underscore as the first character in its name, it does not need to be called in main

# Lorien Hall
# Student ID = 001467769


def loadPackageData(fileName):
    with open(fileName) as packages:
        package_data = csv.reader(packages, delimiter=',')
        next(package_data); next(package_data); next(package_data)  # skip headers
        # iterate through the lines in the file
        for package in package_data:
            p_id = int(package[0])
            p_address = package[1]
            p_city = package[2]
            p_state = package[3]
            p_zip = package[4]
            p_del_deadline = package[5]
            p_mass = package[6]
            if package.__sizeof__() == 7:  # determine if there is a special note
                p_spec_notes = package[7]
            else:
                p_spec_notes = None
            # create the package and insert it into the hash
            pack = Package(p_id, p_address, p_city, p_state, p_zip, p_del_deadline, p_mass, p_spec_notes)
            unloaded_package_list.append(pack)
            all_list.append(pack)
            first_status[pack.id()] = pack.delivery_status()
            myHash.insert(p_id, pack)


def loadDistanceData(fileName):
    with open(fileName) as distances:
        distance_data = csv.reader(distances, delimiter=',')
        next(distance_data); next(distance_data); next(distance_data)  # skip headers
        _list = []; _place = 4  # temporary variables
        # iterate through the lines in the file
        for distance in distance_data:
            if _place < 8:
                for cell in distance:
                    address_list.append(cell)

            elif _place > 8:
                distance_list.append(distance)
            _place = _place + 1


# This is the method that presents the menu
def menu():
    print('\nMenu')
    print('1 : Search for a package \n2 : Get the total mileage \n3 : Exit the program')
    option = input('Choose an option(1, 2 or 3): ')

    return option


# Note that truck1 and truck2 leave at 8
# truck 3 holds any packages that can't leave until later
# This method loads the trucks with packages
def loadTrucks():
    # determine if this is the first or second time loading the truck
    if load_list_1:
        # load truck2
        for i in load_list_1:
            p = myHash.search(i)
            truck2.loadPackage(p)
        load_list_1.clear()
        # load truck1
        for i in load_list_2:
            p = myHash.search(int(i))
            truck1.loadPackage(p)
        load_list_2.clear()
    elif load_list_3:
        # load truck3
        for i in load_list_3:
            p = myHash.search(i)
            truck1.loadPackage(p)
        load_list_3.clear()


# This method gets the distance between two addresses
def _getDistance(start, end):
    starting = 0
    ending = 1

    # find the given addresses in the list to determine their index
    for address in address_list:
        if start == address:
            starting = address_list.index(address)

        elif end == address:
            ending = address_list.index(address) + 1

    # get the distance at the determined indexes
    dist = distance_list[starting][ending]
    return dist


# This method orders all the trucks packages, choosing the closest first with respect to priority or delivery deadline
def _shortestPathAlgorithm(truck):
    priority = []
    package_list = truck.getPackages()

    # get the packages that have a delivery deadline
    for pack in package_list:
        if pack.delivery_deadline != '\'EOD\'':
            priority.append(pack)
            truck.removeFromPackageList(pack)

    nonpriority = package_list.copy()
    package_list.clear()

    # re-add the packages to the list in the correct order
    package_list = _getDeliverOrder(priority) + _getDeliverOrder(nonpriority)

    # add the packages to the trucks list from the package list in order
    for p in package_list:
        truck.appendToPackageList(p)


# This method orders the packages in a list by address
def _getDeliverOrder(id_listt):
    deliver_order_list = []
    current_address = 'HUB'
    closest_package = None

    while id_listt:
        # get the address that is closest
        check = True
        for pack in id_listt:
            if check:
                closest_package = pack
                check = False

            # determine the distances to compare
            c_distance = _getDistance(current_address, closest_package.address())
            n_distance = _getDistance(current_address, pack.address())

            if n_distance < c_distance:
                closest_package = pack

        # update values
        current_address = closest_package.address()
        id_listt.remove(closest_package)
        deliver_order_list.append(closest_package)

    # return the ordered list
    return deliver_order_list


# This method delivers the packages and makes sure to update all values including the status screenshots
def deliverPackages(truck):
    current_place = '\'HUB\''
    all_package = truck.getPackages().copy()

    # set all packages to en route
    for package in truck.getPackages():
        package.delivery_status(truck.time(), 1)
        package.delivery(2)

    # deliver each package
    for package in truck.getPackages():
        # get the new mileage
        i = _getDistance(current_place, package.address())
        mil = truck.mileage() + float(i)

        # get the new time
        time = (float(i) / 18) * 60
        sec = truck.get_second()
        mi = truck.get_minute() + time
        if mi >= 60:
            it = mi // 60
            mi = mi % 60
            hr = truck.get_hour() + it
        else:
            hr = truck.get_hour()

        # update the truck attributes
        truck.time(datetime.time(int(hr), int(mi), int(sec)))
        truck.mileage(mil)
        truck.unloadPackage(package)
        package.delivery(3)
        current_place = package.address()

        # check the time for the status prints
        if datetime.time(8, 35, 00) >= truck.time() and truck.time() <= datetime.time(9, 25, 00):
            for packag in all_package:
                first_status[packag.id()] = packag.delivery_status()
        if datetime.time(9, 35, 00) >= truck.time() and truck.time() <= datetime.time(10, 25, 00):
            for p in all_list:
                second_status[p.id()] = p.delivery_status()
        if datetime.time(12, 3, 00) >= truck.time() and truck.time() <= datetime.time(13, 12, 00):
            for p in all_list:
                third_status[p.id()] = p.delivery_status()
    # send truck2=1 back to the hub to get more packages
    if truck.name() == 'truck1' and truck.getPackages():
        # get the last package
        listt = truck.getPackages()
        package = listt[-1]
        if package.id() == 33:
            # get the new mileage
            i = _getDistance(current_place, package.address())
            mil = truck.mileage() + float(i)

            # get the new time
            time = (float(i) / 18) * 60
            sec = truck.get_second()
            mi = truck.get_minute() + time
            if mi >= 60:
                it = mi // 60
                mi = mi % 60
                hr = truck.get_hour() + it
            else:
                hr = truck.get_hour()

            # update the truck attributes
            truck.time(datetime.time(int(hr), int(mi), int(sec)))
            truck.mileage(mil)

    truck.clearPackages()


# This method prints the status of all the packages for the 3 different times
def statusScreenShot():
    # print the first screenshot
    print('Package Status between 8:35 a.m. and 9:25 a.m. :')
    for k, v in sorted(first_status.items()):
        if k in (8, 16, 24, 32, 40):
            print('({} : {})'.format(k, v))
        else:
            print('({} : {})'.format(k, v), end=' ')

    # print the second screenshot
    print('\nPackage Status between 9:35 a.m. and 10:25 a.m. :')
    for k, v in sorted(second_status.items()):
        if k in (8, 16, 24, 32, 40):
            print('({} : {})'.format(k, v))
        else:
            print('({} : {})'.format(k, v), end=' ')

    # print the third screenshot
    print('\nPackage Status 12:03 p.m. and 1:12 p.m. :')
    for k, v in sorted(third_status.items()):
        if k in (8, 16, 24, 32, 40):
            print('({} : {})'.format(k, v))
        else:
            print('({} : {})'.format(k, v), end=' ')


# Create the trucks
truck1 = Truck.Truck('truck1')
truck2 = Truck.Truck('truck2')



def main():
    # load data from the files
    loadPackageData('WGUPS Package File.csv')
    loadDistanceData('WGUPS Distance Table.csv')

    # load the trucks, and deliver one round of packages
    while unloaded_package_list:
        loadTrucks()
        deliverPackages(truck1)
        deliverPackages(truck2)

    # print all the packages
    for i in range(1, 41):
        if i == 40: print(myHash.search(i), end='\n\n')
        else: print(myHash.search(i))

    # print the package status
    statusScreenShot()

    # print the main menu
    exit_program = True
    while exit_program:
        option = menu()
        if option == '1':
            i = input('What is the id of the package you wish to search for? ')
            print(myHash.search(int(i)))
        elif option == '2':
            mileage = truck1.mileage() + truck2.mileage()
            print('{} total miles'.format(mileage))
        elif option == '3':
            exit_program = False
        else:
            print('Invalid entry. Please try again.')


if __name__ == '__main__': main()
