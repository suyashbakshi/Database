# Database-Systems
The directory database homework contains code for the database normalization code I developed while enrolled in database systems cource. The input of program is in the form of a text file that contains schema of tables residing in a local or a remote database. The program connects to the given database using JDBC driver, queries the tables mentioned in the schema file and check which normal forms they satisfy and also suggests possible decomposition if tables do not satisfy 2NF,3NF or BCNF.

A sample input file will look like:

Tablename1(Column1(k), Column2(k), Column3, column4)
Tablename2(Column1(k), Column2)
