
// Function defenition
#include "degree.h"
#include "student.h"
#include "roster.h"
#include <string>
using namespace std;

//Destructor
Roster::~Roster()
{
	cout << "DESTRUCTOR CALLED!!!" << endl << endl;
	for (int i = 0; i < numStudents; i++)
	{
		cout << "Destroying student #" << i + 1 << endl;
		delete classRosterArray[i];
		classRosterArray[i] = nullptr;

	}
}

// Parse
void Roster::parse(string studentdata)
{
	int rhs = studentdata.find(",");
	string sID = studentdata.substr(0, rhs);

	int lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	string sfName = studentdata.substr(lhs, rhs - lhs);

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	string slName = studentdata.substr(lhs, rhs - lhs);

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	string seAdress = studentdata.substr(lhs, rhs - lhs);

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	double sage = stod(studentdata.substr(lhs, rhs - lhs));

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	double daysInCourse1 = stod(studentdata.substr(lhs, rhs - lhs));

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	double daysInCourse2 = stod(studentdata.substr(lhs, rhs - lhs));

	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	double daysInCourse3 = stod(studentdata.substr(lhs, rhs - lhs));

	DegreeProgram dp = DegreeProgram::SECURITY;
	lhs = rhs + 1;
	rhs = studentdata.find(",", lhs);
	if (studentdata.substr(lhs, rhs - lhs) == "NETWORK") dp = DegreeProgram::NETWORK;
	else if (studentdata.substr(lhs, rhs - lhs) == "SOFTWARE") dp = DegreeProgram::SOFTWARE;

	add(sID, sfName, slName, seAdress, sage, daysInCourse1, daysInCourse2, daysInCourse3, dp);
}

//Add
void Roster::add(string sID, string sfName, string slName, string seAdress,
	double sage, double daysInCourse1, double daysInCourse2,
	double daysInCourse3, DegreeProgram dp)
{
	double nDays[3] = { daysInCourse1, daysInCourse2, daysInCourse3 };
	classRosterArray[++lastStudent] = new Student(sID, sfName, slName,
		seAdress, sage, nDays, dp);
}

// Print
void Roster::printAll()
{
	for (int i = 0; i <= Roster::lastStudent; i++)
	{
		cout << left;
			classRosterArray[i]->print();
		
	}
}

void Roster::printByDegreeProgram(DegreeProgram dp)
{
	for (int i = 0; i <= Roster::lastStudent; i++)
	{
		dp = DegreeProgram::SOFTWARE;
		if (Roster::classRosterArray[i]->getdegreeProgram() == dp)
			classRosterArray[i]->print();
	}
	cout << endl;
}

void Roster::printAvergeDaysInCourse(string studentID)
{
	for (int i = 0; i <= Roster::lastStudent; i++)
	{
		if (classRosterArray[i]->getID() == studentID)
		{
			cout << "StudentID: " << studentID << ", averages ";
			cout << (classRosterArray[i]->getnumDays()[0]
				+ classRosterArray[i]->getnumDays()[1]
				+ classRosterArray[i]->getnumDays()[2]) / 3.0;
			cout << " days in a course.";
		}
	}
	cout << endl;
}

bool Roster::removeStudentID(string studentID)
{
	bool success = false;
	for (int i = 0; i <= Roster::lastStudent; i++)
	{
		if (classRosterArray[i]->getID() == studentID)
		{
			success = true;
			if (i < numStudents - 1)
			{
				Student* temp = classRosterArray[i];
				classRosterArray[i] = classRosterArray[numStudents - 1];
				classRosterArray[numStudents - 1] = temp;
			}
			Roster::lastStudent--;
		}
	}
	if (success)
	{
		cout << studentID << " removed from Roster." << endl << endl;
		this->printAll();
	}
	else cout << studentID << " not found." << endl << endl;
	return 0;
}




void Roster::printInvalidEmails()
{
	bool any = false;
	for (int i = 0; i <= Roster::lastStudent; i++)
	{
		string eA = (classRosterArray[i]->geteAdress());
		if (eA.find(' ') != string::npos)
		{
			any = true;
			cout << eA << " - no spaces alowed.\n";

		}

		else if (eA.find('.') == string::npos)
		{
			any = true;
			cout << eA << " - missing a period.\n";
		}
		else if (eA.find('@') == string::npos)
		{
			any = true;
			cout << eA << " - missing an @ symbol.\n";

		}
	}
	if (!any) cout << "NONE" << endl;
}
	



