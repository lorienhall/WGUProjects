#pragma once


#include <iostream>
#include <iomanip>
#include "degree.h"
using namespace std;

class Student 
{
public:
	const static int daysArraySize = 3;
private:
	// A student has 7 diff characteristics
	string studentID;
	string firstName;
	string lastName;
	string emailAdress;
	double age;
	double numDays[daysArraySize];
	DegreeProgram degreeProgram;

public:
	//Constuctors
	Student();
	Student(string studentID, string firstName, string lastName, 
		string emailAdress, double age, double numDays[], 
		DegreeProgram degreeProgram);
	//Destructor
	~Student();

	// Mutators or setters
	void setID(string ID);
	void setfName(string fName);
	void setlName(string lName);
	void seteAdress(string eAdress);
	void setage(double age);
	void setnumDays(double numDays[]);
	void setDegreeProgram(DegreeProgram dp);

	//Accessors or getters
	string getID();
	string getfName();
	string getlName();
	string geteAdress();
	double getage();
	double* getnumDays();
	DegreeProgram getdegreeProgram();

	//Print
	void print();

};

