import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connectdbv {
	
	Connection connection=null;
	public void connecting() {
	


		try {

			Class.forName("com.vertica.jdbc.Driver");

		} catch (ClassNotFoundException e1) {

			System.out.println("Where is your Vertica JDBC Driver? "
					+ "Include in your library path!");
			e1.printStackTrace();
			return;

		}

		System.out.println("Vertica JDBC Driver Registered!");

		

		try {

			connection = DriverManager.getConnection(
					"jdbc:vertica://129.7.243.243:5433/cosc6340s17", "team04","XCQyntKe");

		} catch (SQLException e11) {

			System.out.println("Connection Failed! Check output console");
			e11.printStackTrace();
			return;

		}

		if (connection != null) {
			System.out.println("Connected to the Database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}

}
