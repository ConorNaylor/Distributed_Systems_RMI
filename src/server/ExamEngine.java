
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceptions.*;
import interfaces.Assessment;
import interfaces.ExamServer;

public class ExamEngine implements ExamServer {

	private Session sess;
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private ArrayList<Assessment> assessments = new ArrayList<Assessment>();

	// Constructor is required
	public ExamEngine() {
		super();
		
		Date date = new Date();
		
		Student s1 = new Student(12345678,"password");
		Student s2 = new Student(87654321,"password");
		
		assessments.add(new MCQAssessment(s1.getId(), "CT454", "Computer Science", date));
		assessments.add(new MCQAssessment(s2.getId(), "BE420", "Computer Engineering", date));
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period

	public long login(int studentid, String password) throws 
	UnauthorizedAccess, RemoteException {
		for(Student s: Student.students) {
			if(studentid == (s.getId()) && password.equals(s.getPassword())) {
				sess = new Session(s);
				sessions.add(sess);
				System.out.println("Session created for student: " + s.getId() + ".");
				return sess.getSessionNumber();
			}
		}
		throw new UnauthorizedAccess("Could not authenticate.");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(int token, int studentid) throws
	UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		List<String> list = new ArrayList<String>();
		if(isActiveSession(token)) {
			for(Assessment a: assessments) {
				if(a.getAssociatedID()==studentid) {
					list.add(a.getInformation());
				}
				throw new NoMatchingAssessment("User has no assessments.");
			}
		}
		throw new UnauthorizedAccess("");
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(int token, int studentid, String courseCode) throws
	UnauthorizedAccess, NoMatchingAssessment, RemoteException {

		// TBD: You need to implement this method!
		// For the moment method just returns an empty or null value to allow it to compile

		return null;
	}

	// Submit a completed assessment
	public void submitAssessment(int token, int studentid, Assessment completed) throws 
	UnauthorizedAccess, NoMatchingAssessment, RemoteException {

		// TBD: You need to implement this method!
	}

	public boolean isActiveSession(long sessionId) throws UnauthorizedAccess{
		for(Session s: sessions) {
			if(s.getSessionNumber() == sessionId) {
				return(s.isSessionActive());
			}
		}
        throw new UnauthorizedAccess("Cannot authenticate user.");
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			//        		System.setProperty("java.security.policy", "security.policy");
			//        		System.setProperty("java.rmi.server.hostname","localhost");
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "ExamServer";
			ExamServer engine = new ExamEngine();
			ExamServer stub = (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("ExamEngine bound");
		} catch (Exception e) {
			System.err.println("ExamEngine exception:");
			e.printStackTrace();
		}
	}
}
