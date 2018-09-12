<p align="center">
<img src="https://user-images.githubusercontent.com/22243493/45451357-67478b80-b70d-11e8-8846-bbbecd1a5f24.png" alt="BioChem Logo" width="300" height="300">
</p>

A commissioned Database Management System (DBMS) with report generating functionality
for BioChem Dau, a company that offers various medical tests at cheap costs. 

## Project Features
* CRUD Functionality for BioChem patients.
* Automated file creation for each patient.
* Automated directory creation for each organization/company.
* Navigational functionality for searching a patient directory.
* CRUD Functionality for medical test results of the patients. (Complete Blood Count, Urinalysis, Fecalysis, X-Ray, etc.)
* Automated report generation for medical test results to .docx file format.
* "Print" medical test results of individual or every patient.
* Modifiable pre-defined templates for report generation.


## Project Dependencies
* Java FX 8 (Frontend code)
* C# 6.0 (Backend code for Report Generation)
* OpenXML (Word and .docx file format)
* Jfoenix 9.0.0 (For UI design)
* SQLite JDBC 3.21.0 (DB Connection)

**Note:** Additional dependencies may be required by the above listed modules.

## Installing Project Dependencies
1. .NET 2015 and JDK 8 must be installed first.
2. External dependencies are pre-packaged to the released jar file and also resides with the project directory. 
No need for any other external installations.

## Using the DBMS
* Simply compile the "src" directory into a .jar file (a META-INF folder already exists). 
* If you would like to run it on the IDE or console, navigate to the ph.biochem.prototypes package and compile the Main.java file.

## Developer
* Jimenez, John Joshua (Solo Project)

## License
GNU Affero General Public License v3.0. For a more detailed explanation, check it out [here.](https://github.com/jjjimenez100/A-Silent-Voice/blob/master/LICENSE)

