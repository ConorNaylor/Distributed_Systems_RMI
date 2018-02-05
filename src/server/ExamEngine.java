package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import exceptions.NoMatchingAssessment;
import exceptions.UnauthorizedAccess;
import interfaces.Assessment;
import interfaces.ExamServer;

public class ExamEngine extends UnicastRemoteObject implements ExamServer {

	private ArrayList<Student> students;
	private ArrayList<SessionToken> sessions;
	private ArrayList<Assessment> assessments;

	// Constructor is required
	public ExamEngine() throws RemoteException {
		super();
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period
	public int login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
		SessionToken token = null;
		for (Student student : students) {
			if (studentid == (student.getstudentID())) {
				if (password.equals(student.getPassword())) {
					token = new SessionToken(student);
					sessions.add(token);

				} else {
					throw new UnauthorizedAccess("Incorrect password");
				}
			} else {
				throw new UnauthorizedAccess("Student not registered");
			}
		}
		return token.getToken();
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		List<String> summary = new ArrayList<String>();
		for (SessionToken session : sessions) {
			if (session.getToken() == token && session.getStudent().getstudentID() == studentid) {
				if (assessments.isEmpty()) {
					for (Assessment assessment : assessments) {
						summary.add(assessment.getInformation());
					}
				} else {
					throw new NoMatchingAssessment("No assessments available");
				}
			} else {
				throw new UnauthorizedAccess("Session expired");
			}
		}
		return summary;
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		if (!sessions.isEmpty()) {
			for (SessionToken session : sessions) {
				if (session.getToken() == token && session.getStudent().getstudentID() == studentid) {
					if (assessments.isEmpty()) {
						for (Assessment assessment : assessments) {
							if (assessment.getAssociatedID() == studentid
							// && assessment.getCourseCode().equals(courseCode)
							) {
								return assessment;
							}
						}
					} else {
						throw new NoMatchingAssessment("No assessments available");
					}
				} else {
					throw new UnauthorizedAccess("Session expired");
				}
			}
		} throw new NoMatchingAssessment("No assessments available");
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		if (!sessions.isEmpty()) {
			for (SessionToken session : sessions) {
				if (session.getToken() == token && session.getStudent().getstudentID() == studentid) {
					if (assessments.isEmpty()) {
						for (Assessment assessment : assessments) {
							if (assessment.getAssociatedID() == studentid
							 && assessment.getInformation().equals(completed.getInformation())) {
								assessment = completed;
							}
						}
					} else {
						throw new NoMatchingAssessment("No assessments available");
					}
				} else {
					throw new UnauthorizedAccess("Session expired");
				}
			}
		} throw new NoMatchingAssessment("No assessments available");
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "ExamServer";
			ExamServer engine = new ExamEngine();
			ExamServer stub = (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);

			System.err.println("Server ready");
			System.out.println("ExamEngine bound");
		} catch (Exception e) {
			System.err.println("ExamEngine exception:");
			e.printStackTrace();
		}
	}
}
