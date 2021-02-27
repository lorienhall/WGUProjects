                                    MySQL Database Scheduler

By Lorien Hall
Using: IntelliJ Community 2020.2.3
       Java SE 15.0.1
       JavaFX-SDK-11.0.4
Application Version: 2.3
Date: 11/26/2020
Contact at: lhall@wgu.edu

Purpose: This application is a a GUI-based scheduling desktop application. It connects to a database,
and allows you to add, modify, and delete appointments and customers. The MySQL Database Scheduler also
allow you to view said appointments and customer.

Directions: There are "View All" buttons for each table in the main menu. These buttons will lead you to
screens for each table that shows all the required attributes, and any other required items missing from
the main screen. The exit buttons on those view all screens will not exit the entire program, rather it
will just return you to the main menu.
I have also used certain abbreviations throughout the program. Cust = customer, and app = appointment.
When looking at the reports, specifically the two that ask you to choose a month from a combo box its
important to pick months that actually have appointments. Otherwise its not going to look like it changes.
I recommend using december if your testing the tableview that displays appointments by type and
month. If you are testing the number of new customers in a month, then december is the only month that
customers have been created in.
If you are having trouble finding where I used and justified the use of lambda expressions in the javadoc,
you have to scroll down to the method details in order to find them. There are some in the initialize
method of the controllers addAppointment, addCustomer, modifyAppointment, modifyCustomer, and the reports
screen.

My Report: The report I chose as my third report is based on the customers create date. There is a combo
box that displays months. This report displays the number of customer created in, or new customers, in
that month. You will notice that the there is also a year after the month combo box. This displays the
year in which the customers were created in. By specifying the year the report can actually be used to
track the rate of incoming customers. I chose not to allow the year to be changed because at the time of
creation there are no customers created in any other years.
