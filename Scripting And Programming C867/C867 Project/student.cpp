
#include "roster.h"
#include "student.h"

//Parameterless constructor
Student::Student()
{
	this->studentID = "";
	this->firstName = "";
	this->lastName = "";
	this->emailAdress = "";
	this->age = 0.0;
	for (int i = 0; i < daysArraySize; i++) this->numDays[i] = 0.0;
	this->degreeProgram = DegreeProgram::SECURITY;
}

// Full Constructor
Student::Student(string studentID, string firstName, string lastName,
	string emailAdress, double age, double numDays[], 
	DegreeProgram degreeProgram)
{
	this->studentID = studentID;
	this->firstName = firstName;
	this->lastName = lastName;
	this->emailAdress = emailAdress;
	this->age = age;
	for (int i = 0; i < daysArraySize; i++) this->numDays[i] = numDays[i];
	this->degreeProgram = degreeProgram;
}

//Destructor
Student::~Student(){}

// Mutators or setters
void Student::setID(string ID) { this->studentID = ID; }
void Student::setfName(string fName) { this->firstName = fName; }
void Student::setlName(string lName) { this->lastName = lName; }
void Student::seteAdress(string eAdress) { this->emailAdress = eAdress; }
void Student::setage(double age) { this->age = age; }
void Student::setnumDays(double numDays[]) 
{ 
	for (int i = 0; i < daysArraySize; i++) this->numDays[i] = numDays[i];
}
void Student::setDegreeProgram(DegreeProgram dp) { this->degreeProgram = dp; }

//Accessors or getters
string Student::getID() { return this->studentID; }
string Student::getfName() { return this->firstName; }
string Student::getlName() { return this->lastName; }
string Student::geteAdress() { return this->emailAdress; }
double Student::getage() { return this->age; }
double* Student::getnumDays() { return this->numDays; }
DegreeProgram Student::getdegreeProgram() { return this->degreeProgram; }

//Print
void Student::print()
{
	cout << this->getID() << '\t';
	cout << "First Name: " << this->getfName() << '\t';
	cout << "Last Name: " << this->getlName() << "\t" ;
	cout << "Age: " << this->getage() << "\t";
	cout << "daysInCourse: {" << this->getnumDays()[0] << ',';
	cout << this->getnumDays()[1] << ',';
	cout << this->getnumDays()[2] << "}\t";
	cout << "Degreee Program:" << degreeProgramStrings[static_cast<int>(this->getdegreeProgram())] << endl;
}