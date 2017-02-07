import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connectdb {
	
	Connection connection=null;
	public void connecting() {
	


		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e1) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e1.printStackTrace();
			return;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		

		try {

			connection = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/first","postgres"
					,"ha20000");

		} catch (SQLException e11) {

			System.out.println("Connection Failed! Check output console");
			e11.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

}
