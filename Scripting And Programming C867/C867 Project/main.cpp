





#include "roster.h"
#include <string>
#include <iostream>
using namespace std;

int main()
{
	
	//Student info
	const string studentData[] =
	{ "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
		"A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
		"A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
		"A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
		"A5,Lorien,Hall,lorienorahall@gmail.com,18,10,20,24,SOFTWARE"
	};
	const int numstudents = 5;
	Roster roster;

	//Intro
	cout << "C867-Scripting and Programming: Applications \n" 
		<< "Language: C++\n" 
		<< "Student ID: 001467769\n" 
		<< "Name: Lorien Hall\n\n";

	//Printing
	for (int i = 0; i < numstudents; i++) roster.parse(studentData[i]);
	cout << "Displaying all students: " << endl;
	roster.printAll();
	cout << endl;

	cout << "Displaying students with invalid emails:" << endl;
	roster.printInvalidEmails();
	cout << endl;
	
	cout << "\nDisplaying the average days in a course: " << endl;
	for (int i = 0; i < numstudents; i++)
	{
		roster.printAvergeDaysInCourse(roster.classRosterArray[i]->getID());
	}
	

	cout << "\nDisplaying by degree program: "<< degreeProgramStrings[2] << endl;
	roster.printByDegreeProgram((DegreeProgram)2);


	cout << "Removing student with ID A3:" << endl;
	roster.removeStudentID("A3");
	cout << endl;

	cout << "Removing student with ID A3:" << endl;
	roster.removeStudentID("A3");
	cout << endl;

	system("pause");
	return 0;
}