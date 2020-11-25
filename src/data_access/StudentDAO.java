package data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import java.util.ArrayList;

import model.Student;

public class StudentDAO {
	public StudentDAO() {
		try {
			Class.forName(ConnectionParameters.jdbcDriver);
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		}
	}

	private Connection openConnection() throws SQLException {
		return DriverManager.getConnection(ConnectionParameters.connectionString, ConnectionParameters.username,
				ConnectionParameters.password);
	}

	public List<Student> getAllStudents() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Student> studentList = new ArrayList<Student>();

		try {
			connection = openConnection();

			String sqlText = "SELECT id, firstname, lastname, streetaddress, postcode, postoffice FROM Student ORDER by lastname";

			preparedStatement = connection.prepareStatement(sqlText);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String streetaddress = resultSet.getString("streetaddress");
				String postcode = resultSet.getString("postcode");
				String postoffice = resultSet.getString("postoffice");

				studentList.add(new Student(id, firstname, lastname, streetaddress, postcode, postoffice));
			}

		} catch (SQLException sqle) {
			System.out.println("\n[ERROR] StudentDAO: getStudents() failed. " + sqle.getMessage() + "\n");
			studentList = null;

		} finally {
			DbUtils.closeQuietly(resultSet, preparedStatement, connection);
		}

		return studentList;
	}

	public String getAllStudentsJSON() {
		StudentDAO student = new StudentDAO();
		List<Student> students = student.getAllStudents();
		Gson gson = new Gson();
		return gson.toJson(students);
	}

	public Student getStudentById(int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Student> studentList = new ArrayList<Student>();

		try {
			connection = openConnection();

			String sqlText = "SELECT id, firstname, lastname, streetaddress, postcode, postoffice FROM Student WHERE id = ?";

			preparedStatement = connection.prepareStatement(sqlText);
			preparedStatement.setInt(1, id);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int resultId = resultSet.getInt("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String streetaddress = resultSet.getString("streetaddress");
				String postcode = resultSet.getString("postcode");
				String postoffice = resultSet.getString("postoffice");

				studentList.add(new Student(resultId, firstname, lastname, streetaddress, postcode, postoffice));
			}

		} catch (SQLException sqle) {
			System.out.println("[ERROR] StudentDAO: getStudentById() failed. " + sqle.getMessage() + "\n");
			studentList = null;
		} finally {
			DbUtils.closeQuietly(resultSet, preparedStatement, connection);
		}
		if (studentList.isEmpty()) {
			return null;
		} else {
			return studentList.get(0);
		}
	}

	public int insertStudent(int id, String first, String last, String street, String code, String office) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = openConnection();

			String sqlText = "INSERT INTO Student (id, firstname, lastname, streetaddress, postcode, postoffice) VALUES (?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(sqlText);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, first);
			preparedStatement.setString(3, last);
			preparedStatement.setString(4, street);
			preparedStatement.setString(5, code);
			preparedStatement.setString(6, office);
			preparedStatement.executeUpdate();

			return 0;

		} catch (SQLException sqle) {
			if (sqle.getErrorCode() == ConnectionParameters.PK_VIOLATION_ERROR) {
				return 1;
			} else {
				return -1;
			}
		} finally {
			DbUtils.closeQuietly(preparedStatement, connection);
		}
	}

	public int deleteStudent(int id) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = openConnection();

			String sqlText = "DELETE FROM Student WHERE id = ?";

			preparedStatement = connection.prepareStatement(sqlText);
			preparedStatement.setInt(1, id);

			if (preparedStatement.executeUpdate() != 0) {
				return 0;
			} else {
				return 1;
			}

		} catch (SQLException sqle) {
			return -1;

		} finally {
			DbUtils.closeQuietly(preparedStatement, connection);
		}
	}

	public int updateStudent(int id, String first, String last, String street, String code, String office) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = openConnection();

			String sqlText = "UPDATE Student SET firstname = ?, lastname = ?, streetaddress = ?, postcode = ? , postoffice  = ?  WHERE id = ?";

			preparedStatement = connection.prepareStatement(sqlText);
			preparedStatement.setInt(6, id);
			preparedStatement.setString(1, first);
			preparedStatement.setString(2, last);
			preparedStatement.setString(3, street);
			preparedStatement.setString(4, code);
			preparedStatement.setString(5, office);

			int result = preparedStatement.executeUpdate();

			if (result == 1) {
				return 0;
			} else {
				return 1;
			}

		} catch (SQLException sqle) {
			return -1;
		} finally {
			DbUtils.closeQuietly(preparedStatement, connection);
		}

	}

}

// End