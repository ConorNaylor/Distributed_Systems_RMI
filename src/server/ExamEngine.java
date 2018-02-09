
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private ArrayList<Assessment> assessments = new ArrayList<Assessment>();

	// Constructor is required
	public ExamEngine() {
		super();

		Date d = null, d1 = null, d2 = null;
		Student s1 = new Student(12345678, "password");
		Student s2 = new Student(87654321, "password");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			d = sdf.parse("3/05/2018");
			d1 = sdf.parse("13/05/2018");
			d2 = sdf.parse("01/01/2018");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		MCQAssessment assA = new MCQAssessment(s1.getId(), "CT454", "Computer Science", d);
		MCQAssessment assB = new MCQAssessment(s2.getId(), "BE420", "Computer Engineering", d1);
		MCQAssessment assC = new MCQAssessment(s1.getId(), "CT111", "Electronic Engineering", d);
		MCQAssessment assD = new MCQAssessment(s2.getId(), "BE100", "IT", d1);
		MCQAssessment assE = new MCQAssessment(s2.getId(), "EG100", "IT", d2);
		MCQAssessment assF = new MCQAssessment(s1.getId(), "EG100", "IT", d2);

		assessments.add(assA);
		assessments.add(assB);
		assessments.add(assC);
		assessments.add(assD);
		assessments.add(assE);
		assessments.add(assF);

		String[] answers = new String[] { "An Gunna Mór", "Carrauntoohil", "Mount Brandon", "Croagh Patrick" };
		String[] answers2 = new String[] { "Imaginary", "Fake News", "Hardware", "D" };
		String[] answers3 = new String[] { "1", "2", "3", "4", "5" };
		String[] answers4 = new String[] { "Everything", "Nothing", "Brogan", "D", "or E" };

		MCQQuestion question1 = new MCQQuestion("What is the tallest mountain in Ireland?", answers, 1, 1);
		MCQQuestion question2 = new MCQQuestion("What is an FPGA?", answers2, 2, 1);
		MCQQuestion question3 = new MCQQuestion("Which is the biggest number?", answers3, 3, 3);
		MCQQuestion question4 = new MCQQuestion("What is raaaank?", answers4, 4, 2);

		assA.setQuestion(question1);
		assA.setQuestion(question2);

		assB.setQuestion(question3);
		assB.setQuestion(question4);

		assC.setQuestion(question3);
		assC.setQuestion(question4);

		assD.setQuestion(question1);
		assD.setQuestion(question2);
		
		assE.setQuestion(question1);
		assF.setQuestion(question2);
	}

	// Implement the methods defined in the ExamServer interface...
	// Return an access token that allows access to the server for some time period
	public long login(int studentid, String password) throws UnauthorizedAccess, RemoteException {
		for (Session s : sessions) {
			if (s.getStudent().getId() == studentid) {
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
		} throw new UnauthorizedAccess("Could not authenticate.");
	}

	// Return a summary list of Assessments currently available for this studentid
	public List<String> getAvailableSummary(long token, int studentid)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		Date date = new Date();
		List<String> list = new ArrayList<String>();
		if (isActiveSession(token)) {
			if (!assessments.isEmpty()) {
				for (Assessment a : assessments) {
					if (date.before(a.getClosingDate())) {
						if (a.getAssociatedID() == studentid) {
							list.add(a.getInformation());
						}
					}
				}
				if (!list.isEmpty()) {
					return list;
				}
			} throw new NoMatchingAssessment("User has no assessments.");
		} throw new UnauthorizedAccess("");
	}

	// Return an Assessment object associated with a particular course code
	public Assessment getAssessment(long token, int studentid, String courseCode)
			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
		if (isActiveSession(token)) {
			for (Assessment a : assessments) {
				String info = a.getInformation();
				String course = info.substring(info.lastIndexOf("course")+7, info.lastIndexOf(" for"));
				if (a.getAssociatedID() == studentid && course.equals(courseCode)) {
					for(Question question:a.getQuestions()) {
						System.out.println("Get assessment where answer to question " + question.getQuestionDetail() + " is " +   a.getSelectedAnswer(question.getQuestionNumber()));
					}
					return a;
				}
			} throw new NoMatchingAssessment("User has no assessments.");
		} throw new UnauthorizedAccess("Cannot authenticate user.");
	}

//	// Submit a completed assessment
//	public void submitAssessment(long token, int studentid, Assessment completed)
//			throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
//		for(Question question:completed.getQuestions()) {
//			System.out.println(completed.getSelectedAnswer(question.getQuestionNumber()));
//		}
//		if (isActiveSession(token)) {
//			for(Assessment assessment:assessments) {
//				if(assessment.getAssociatedID() == studentid && assessment.getInformation().equals(completed.getInformation())) {;
//					assessment = completed;
//					for(Question question:assessment.getQuestions()) {
//						System.out.println(assessment.getSelectedAnswer(question.getQuestionNumber()));
//					}
//					System.out.println("Assessment completed for user " + studentid);
//				}
//			}
//		} else {
//			throw new UnauthorizedAccess("Cannot authenticate user.");
//		}
//	}
	// Submit a completed assessment
		public void submitAssessment(long token, int studentid, Assessment completed)
				throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
			for(Question question:completed.getQuestions()) {
				System.out.println(completed.getSelectedAnswer(question.getQuestionNumber()));
			}
			if (isActiveSession(token)) {
				for(Assessment assessment:assessments) {
					if(assessment.getAssociatedID() == studentid && assessment.getInformation().equals(completed.getInformation())) {;
						assessments.remove(assessment);
						assessments.add(completed);
						for(Question question:completed.getQuestions()) {
							System.out.println("Submitted answer:" + completed.getSelectedAnswer(question.getQuestionNumber()) + "for question: " + question.getQuestionDetail());
						}
						System.out.println("Assessment completed for user " + studentid);
					}
				}
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
