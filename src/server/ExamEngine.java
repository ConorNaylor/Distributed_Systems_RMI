
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
import interfaces.Question;

public class ExamEngine implements ExamServer {

	private Session sess;
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private ArrayList<MCQAssessment> assessments = new ArrayList<MCQAssessment>();

	// Constructor is required
	public ExamEngine() {
		super();

		Date date = new Date();

		Student s1 = new Student(12345678, "password");
		Student s2 = new Student(87654321, "password");

		MCQAssessment assA = new MCQAssessment(s1.getId(), "CT454", "Computer Science", date);
		MCQAssessment assB = new MCQAssessment(s2.getId(), "BE420", "Computer Engineering", date);
		MCQAssessment assC = new MCQAssessment(s1.getId(), "CT111", "Electronic Engineering", date);
		MCQAssessment assD = new MCQAssessment(s2.getId(), "BE100", "IT", date);

		assessments.add(assA);
		assessments.add(assB);
		assessments.add(assC);
		assessments.add(assD);

		String[] answers = new String[] { "An Gunna Mór", "Carrauntoohil", "Mount Brandon", "Croagh Patrick" };
		String[] answers2 = new String[] { "Imaginary", "Fake News", "Hardware", "D" };
		String[] answers3 = new String[] {"1","2","3","4","5"};
		String[] answers4 = new String[] {"Everything","Nothing","Brogan","D","or E"};

		MCQQuestion question1 = new MCQQuestion("What is the tallest mountain in Ireland?", answers, 1, 1);
		MCQQuestion question2 = new MCQQuestion("What is an FPGA?", answers2, 2, 1);
		MCQQuestion question3 = new MCQQuestion("Which is the biggest number?", answers3, 3, 3);
		MCQQuestion question4 = new MCQQuestion("What is raaaank?", answers4, 4, 2);


		assA.setQuestion(question1);
		assA.setQuestion(question2);
		assA.setQuestion(question3);
		assA.setQuestion(question4);

		assB.setQuestion(question1);
		assB.setQuestion(question2);
		assB.setQuestion(question3);
		assB.setQuestion(question4);
		
		assC.setQuestion(question1);
		assC.setQuestion(question2);
		assC.setQuestion(question3);
		assC.setQuestion(question4);

		assD.setQuestion(question1);
		assD.setQuestion(question2);
		assD.setQuestion(question3);
		assD.setQuestion(question4);
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period

	public long login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
		for(Session s: sessions) {
			if(s.getStudent().getId() == studentid) {
				return s.getSessionNumber();
			}
		}
		for (Student s : Student.students) {
			if (studentid == (s.getId()) && password.equals(s.getPassword())) {
				sess = new Session(s);
				sessions.add(sess);
				System.out.println("Session created for student: " + s.getId() + ".");
				return sess.getSessionNumber();
			}
		}
		throw new UnauthorizedAccess("Could not authenticate.");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(long token, int studentid)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		List<String> list = new ArrayList<String>();
		if (isActiveSession(token)) {
			if (!assessments.isEmpty()) {
				for (MCQAssessment a : assessments) {
					if (a.getAssociatedID() == studentid) {
						list.add(a.getInformation());
						System.out.println("Adding assessment: " + a.getName() + " to list for " + studentid);
					}
				}
				if (!list.isEmpty()) {
					return list;
				}
			}
			throw new NoMatchingAssessment("User has no assessments.");
		}
		throw new UnauthorizedAccess("");
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(long token, int studentid, String courseCode)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		if (isActiveSession(token)) {
			for (MCQAssessment a : assessments) {
				if (a.getAssociatedID() == studentid && a.getCourseCode().equals(courseCode)) {
					System.out.println("Returning assessment: " + a.getName() + " for " + studentid);
					return a;
				}
			}
			throw new NoMatchingAssessment("User has no assessments.");
		}
		throw new UnauthorizedAccess("Cannot authenticate user.");
	}

	// Submit a completed assessment
	public void submitAssessment(long token, int studentid, Assessment completed)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		if (isActiveSession(token)) {
			MCQAssessment assessment = (MCQAssessment) this.getAssessment(token, studentid,
					((MCQAssessment) completed).getCourseCode());
			this.assessments.remove(assessment);
			System.out.println("Assessment completed: " + assessment.getName() + "for user " + studentid);
		} else {
			throw new UnauthorizedAccess("Cannot authenticate user.");
		}
	}

	public boolean isActiveSession(long sessionId) throws UnauthorizedAccess {
		for (Session s : sessions) {
			if (s.getSessionNumber() == sessionId) {
				return (s.isSessionActive());
			}
		}
		throw new UnauthorizedAccess("Cannot authenticate user.");
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setProperty("java.security.policy", "security.policy");
			// System.setProperty("java.rmi.server.hostname","localhost");
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
