#pragma once


#include "degree.h"
#include "student.h"

class Roster 
{
public:
	int lastStudent = -1;
	const static int numStudents = 5;
	Student* classRosterArray[numStudents];

public:

	//Parse and add
	void parse(string row);
	void add(string sID,string sfName, 
		string slName,string seAdress, 
		double sage,double daysInCourse1,double daysInCourse2, 
		double daysInCourse3,DegreeProgram dp);

	//Print
	void printAll();
	void printByDegreeProgram(DegreeProgram dp);
	void printInvalidEmails(); //Has to have an @ symbol a period and no spaces
	void printAvergeDaysInCourse(string studentID); 
	
	bool removeStudentID(string studentID);

	//Destructor
	~Roster();


};

